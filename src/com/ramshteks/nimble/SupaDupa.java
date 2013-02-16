/*
 * Copyright (C) 2011 Shirobok Pavel
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License,
 * or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation,
 * Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 * Contact information:
 * email: ramshteks@gmail.com
 */
package com.ramshteks.nimble;
import com.ramshteks.nimble.core.Accepter;
import com.ramshteks.nimble.core.Connection;
import com.ramshteks.nimble.core.Core;
import com.ramshteks.nimble.core.SDError;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

public final class SupaDupa {
	/**
	 * Generator unique ids of connection
	 */
	private IDGenerator cids;

	/**
	 * List of socket wait of adding to core
	 */
	private final List<Socket> socketRequests;

	/**
	 * List of active connections
	 */
	private Hashtable<Integer, Connection> connections;

	/**
	 * Core data
	 */
	private Core.ICoreData coreData;

	/**
	 * Business logic data
	 */
	private Core.IBusinessLogicData business;

	/**
	 * Accepter event handler
	 */
	private Core.IAccepterEvent accepterEvent;

	/**
	 * Connection's event handler
	 */
	private Core.IConnectionEvent connectionEvent;

	/**
	 * Implementation of logic
	 */
	private Core.ILogic logic;

	/**
	 * Implementation of logger
	 */
	private Core.ILogger logger;

	/**
	 * Constructor
	 */
	public SupaDupa() {
		cids = new IDGenerator();
		socketRequests = new LinkedList<Socket>();
		connections = new Hashtable<Integer, Connection>();


		accepterEvent = new Core.IAccepterEvent() {
			public void onNewSocket(Socket socket) {
				synchronized (socketRequests) {
					socketRequests.add(socket);
				}
			}

			public void onAccepterError(SDError error) {
				logger.log("SupaDupa:AcceptEventListener:onAccepterError", Core.LoggerLevel.WARNING);
			}
		};

		connectionEvent = new Core.IConnectionEvent() {
			public void onConnectionData(int cid, Core.IPacket packet) {
				try {
					logic.onConnectionPacketReceived(cid, packet);
				} catch (Exception e) {
					logger.log("ILogic.onConnectionPacketReceived", e, Core.LoggerLevel.CRITICAL);
				}
			}

			public void onConnectionError(int cid, SDError error) {
				try {
					logic.onConnectionError(cid, error);
				} catch (Exception e) {
					logger.log("ILogic.onConnectionError", e, Core.LoggerLevel.CRITICAL);
				}
			}

			public void onConnectionClosed(int cid) {
				if (connections.containsKey(cid)) {
					Connection connection = connections.remove(cid);
					connection.close();
					try {
						logic.onConnectionDisconnect(cid);
					} catch (Exception e) {
						logger.log("ILogic.onConnectionDisconnect", e, Core.LoggerLevel.CRITICAL);
					}
				}
			}

			public void onRawDataReceived(int cid, byte[] bytes) {
				if (!connections.containsKey(cid)) {
					return;
				}

				try {
					logic.onRawData(cid, bytes);
				} catch (Exception e) {
					logger.log("ILogic.onRawData", e, Core.LoggerLevel.CRITICAL);
				}
			}
		};

	}

	/**
	 * Starting server
	 *
	 * @param netDataIn net data
	 * @param coreData  core data
	 * @param business  logic and logger
	 * @throws java.io.IOException		  when... mmm, I'm confused =(
	 * @throws NullPointerException when one of the start arguments is <code>null</code>
	 */
	public void start(Core.INetData netDataIn, Core.ICoreData coreData, Core.IBusinessLogicData business) throws IOException, NullPointerException {

		if (netDataIn == null) throw new NullPointerException("Core.INetData == null");
		if (coreData == null) throw new NullPointerException("Core.ICoreData == null");
		if (business == null) throw new NullPointerException("Core.IBusinessLogicData == null");

		this.coreData = coreData;
		this.business = business;

		logic = business.logic();
		logger = business.logger();

		Core.IAgent agent = new Core.IAgent() {
			public Core.ILogger logger() {
				return logger;
			}

			public void send(int cid, Core.IPacket packet) {
				if (connections.containsKey(cid)) {
					Connection connection = connections.get(cid);
					connection.sendPacket(packet);
				}
			}

			public void close(int cid) {
				if (connections.containsKey(cid)) {
					connections.remove(cid);
					try {
						logic.onConnectionDisconnect(cid);
					} catch (Exception e) {
						logger.log("ILogic.onConnectionDisconnect", e, Core.LoggerLevel.CRITICAL);
					}
				}
			}

			public boolean exist_cid(int cid){
				return  connections.containsKey(cid);
			}

		};

		logic.setAgent(agent);

		ServerSocket socket;
		socket = new ServerSocket(netDataIn.port(), 0, netDataIn.address());


		Accepter accepter = new Accepter(socket);
		accepter.setEventListener(accepterEvent);

		Thread accepterThread = new Thread(accepter);
		accepterThread.setPriority(Thread.MAX_PRIORITY);
		accepterThread.start();

		try {
			logic.onCoreStart();
		} catch (Exception e) {
			logger.log("SupaDupa:start: Error on server started event", e, Core.LoggerLevel.CRITICAL);
		}
	}

	/**
	 * Checking server for new event
	 */
	public void check() {
		checkNewSockets();

		try {
			logic.onCoreStartTick();
		} catch (Exception e) {
			logger.log("ILogic.onCoreStartTick", e, Core.LoggerLevel.CRITICAL);
			return;
		}

		Enumeration<Connection> elements = connections.elements();
		Connection currentConnection;
		while (elements.hasMoreElements()) {
			currentConnection = elements.nextElement();
			currentConnection.check();
		}

		try {
			logic.onCoreEndTick();
		} catch (Exception e) {
			logger.log("ILogic.onCoreEndTick", e, Core.LoggerLevel.CRITICAL);
		}
	}

	/**
	 * Checking queue of new sockets
	 */
	private void checkNewSockets() {
		Socket socket;
		synchronized (socketRequests) {
			if (socketRequests.size() == 0){
				return;
			}
			socket = socketRequests.remove(0);
		}
		createConnection(socket);
	}

	/**
	 * Creating new connection from new socket
	 *
	 * @param socket socket instance
	 */
	private void createConnection(Socket socket) {
		int newID = cids.getNextID();
		//logger.log("NEW CID "+newID);
		Connection connection;
		try {
			connection = new Connection(newID, socket, coreData, business.logger(), false);
		} catch (IOException e) {
			logger.log("IOException when creating new Connection object", e, Core.LoggerLevel.CRITICAL);
			return;
		}

		connection.setConnectionEvent(connectionEvent);

		connections.put(newID, connection);
		logger.log("Total connections: " + connections.size());
		try {
			logic.onConnectionConnect(newID);
		} catch (Exception e) {
			logger.log("ILogic.onConnectionConnect", e, Core.LoggerLevel.CRITICAL);
		}
	}

}
