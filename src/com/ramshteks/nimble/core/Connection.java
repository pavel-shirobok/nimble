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

package com.ramshteks.nimble.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.LinkedList;

public final class Connection {
	/**
	 * id of current connection
	 */
	private int cid;

	/**
	 * Socket handler
	 */
	private Socket socket;

	/**
	 * ping time out
	 */
	private long pingTimeOut;

	/**
	 * disconnect timeout
	 * Max time in milliseconds waiting of incoming message
	 */
	private long disconnectTimeout;

	/**
	 * last time of sending ping command to client
	 */
	private long pingLastTime;

	/**
	 * last time of receiving incoming message
	 */
	private long disconnectLastTime;

	/**
	 * logger instance
	 */
	private Core.ILogger logger;

	/**
	 * connection event listener object
	 */
	private Core.IConnectionEvent connectionEvent;

	/**
	 * input stream of socket
	 */
	private InputStream inputStream;

	/**
	 * output stream of socket
	 */
	private OutputStream outputStream;

	/**
	 * queue list of packets to send
	 */
	private LinkedList<Core.IPacket> packetsToSend;

	/**
	 * queue list of received packets
	 */
	private LinkedList<Core.IPacket> packetsToLogic;

	private boolean isClient = false;

	/**------------------------------------------------------------------------------------------
	 * Public methods ---------------------------------------------------------------------------
	 * ----------------------------------------------------------------------------------------*/

	/**
	 * Constructor of <code>Connection</code> object
	 *
	 * @param cid	  id for this object
	 * @param socket   socket handler
	 * @param coreData core data instance
	 * @param logger   logger instance
	 * @throws java.io.IOException when exist problem with streams
	 */
	public Connection(int cid, Socket socket, Core.ICoreData coreData, Core.ILogger logger, boolean isClient) throws IOException {
		this.isClient = isClient;

		packetsToSend = new LinkedList<Core.IPacket>();
		packetsToLogic = new LinkedList<Core.IPacket>();

		pingTimeOut = coreData.pingTimeout();
		disconnectTimeout = coreData.timeout();

		pingLastTime = getCurrentTime();
		disconnectLastTime = getCurrentTime();

		this.logger = logger;
		this.socket = socket;
		this.cid = cid;

		inputStream = socket.getInputStream();
		outputStream = socket.getOutputStream();
	}

	/**
	 * Checking socket for input and output event
	 */
	public void check() {

		try{
			reading();
		}catch (Exception e){
			logger.log("Reading fail ", e, Core.LoggerLevel.CRITICAL);
		}

		if (packetsToLogic.size() != 0) {
			//logger.log("dispatch message");
			dispatchPacket(packetsToLogic.poll());
		}

		if (checkForDisconnectTimeout()) {
			//logger.log("TIMEOUT cid="+cid);
			connectionEvent.onConnectionClosed(cid);
			return;
		}

		if (checkForPingPongTimeout()) {
			sendPacket(generateInternalCommandPacket(Core.InternalCommands.PING));
			pingLastTime = getCurrentTime();
		}

		if (packetsToSend.size() > 0) {
			writing();
		}
	}

	/**
	 * Send packet to output stream
	 *
	 * @param packet abstract representation of data
	 */
	public void sendPacket(Core.IPacket packet) {
		if (packet == null || packet.bytes() == null) {
			logger.log("TRYING SEND NULL PACKET", Core.LoggerLevel.CRITICAL);
		}

		packetsToSend.add(packet);
	}

	/**
	 * Set event listener object
	 *
	 * @param connectionEvent instance of event listener object
	 */
	public void setConnectionEvent(Core.IConnectionEvent connectionEvent) {
		this.connectionEvent = connectionEvent;
	}

	/**
	 * Closes the current connection and stops all processes
	 */
	public void close() {

		try {
			outputStream.close();
		} catch (IOException exp) {
			logger.log("Connection:close: outputStream ", exp, Core.LoggerLevel.WARNING);
		}

		try {
			inputStream.close();
		} catch (IOException exp) {
			logger.log("Connection:close: inputStream ", exp, Core.LoggerLevel.WARNING);
		}

		try {
			socket.close();
		} catch (IOException exp) {
			logger.log("Connection:close: socket ", exp, Core.LoggerLevel.WARNING);
		}

		connectionEvent = null;
		outputStream = null;
		inputStream = null;
		socket = null;

		packetsToSend.clear();
	}

	/**-------------------------------------------------------------------------------------------
	 * Private methods ---------------------------------------------------------------------------
	 * -----------------------------------------------------------------------------------------*/

	/**
	 * Reading process
	 */
	private void reading() {
		int available;
		try {
			if ((available = inputStream.available()) < 2) {
				return;
			}
		} catch (IOException ioException) {
			logger.log("Connection.reading()>available block", ioException, Core.LoggerLevel.CRITICAL);
			return;
		}


		byte[] raw_input = new byte[available];

		int read;
		try {
			read = inputStream.read(raw_input);
		} catch (IOException ioException) {
			logger.log("Connection.reading()>reading block", ioException, Core.LoggerLevel.CRITICAL);
			return;
		}

		pingLastTime = getCurrentTime();
		disconnectLastTime = getCurrentTime();

		ByteBuffer buffer = ByteBuffer.wrap(raw_input);

		do {
			short packet_len = buffer.getShort();
			if (packet_len + buffer.position() > read) {
				break;
			}

			byte[] raw_packet = new byte[packet_len];
			buffer.get(raw_packet, 0, packet_len);

			Core.IPacket packet = createPacket(raw_packet);

			if (computePacket(packet)) {
				packetsToLogic.add(packet);
			}

			if (read - buffer.position() < 2) {
				break;
			}
		} while (true);
	}

	/**
	 * Writing process
	 */
	private void writing() {
		if (packetsToSend.size() == 0) return;

		Core.IPacket packet = packetsToSend.poll();
		byte[] raw_packet = packet.bytes();
		short len = (short) raw_packet.length;

		ByteBuffer buffer = ByteBuffer.allocate(2 + len);
		buffer.putShort(len);
		buffer.put(raw_packet);

		byte[] complete_packet = buffer.array();
		buffer.clear();

		try {
			outputStream.write(complete_packet);
			outputStream.flush();
		} catch (IOException e) {
			if (connectionEvent != null) {
				connectionEvent.onConnectionClosed(cid);
			}
		}
	}

	/**
	 * Checks incoming packets to the internal command
	 *
	 * @param packet incoming packet
	 * @return true if packet is a internal command
	 */
	private boolean computePacket(Core.IPacket packet) {

		byte[] b = packet.bytes();
		ByteBuffer bb = ByteBuffer.wrap(b);
		short command = bb.getShort();

		if (command < 0) {
			switch (command) {
				case Core.InternalCommands.PING:
					sendPacket(generateInternalCommandPacket(Core.InternalCommands.PONG));
					break;
				default:
					break;
			}
		  return false;
		}

		return true;
	}

	/**
	 * Generate packet included internal command
	 *
	 * @param command command code
	 * @return instance of <code>Core.IPacket</code>
	 */
	private Core.IPacket generateInternalCommandPacket(short command) {
		return new Core.InternalCommandPacket(command);
	}

	/**
	 * Generate packet object from <code>byte[]</code>
	 *
	 * @param bytes bytes array
	 * @return instance of <code>Core.IPacket</code>
	 */
	private Core.IPacket createPacket(byte[] bytes) {
		return new Core.RawPacket(bytes);
	}

	/**
	 * Checks whether the waiting period has expired to send the Ping command
	 *
	 * @return <code>true</code> if period has expired
	 */
	private boolean checkForPingPongTimeout() {
		return (getCurrentTime() - pingLastTime > pingTimeOut);
	}

	/**
	 * Checks whether the waiting period has expired to disconnect
	 *
	 * @return <code>true</code> if period has expired
	 */
	private boolean checkForDisconnectTimeout() {
		return (getCurrentTime() - disconnectLastTime > disconnectTimeout);
	}

	/**
	 * Return current time in milliseconds
	 *
	 * @return current time
	 */
	private long getCurrentTime() {
		return System.currentTimeMillis();
	}

	/**
	 * Dispatch event about received packet
	 *
	 * @param packet instance of <code>Core.IPacket</code>
	 */
	private void dispatchPacket(Core.IPacket packet) {
		if (connectionEvent != null) {
			//logger.log("Connection: "+cid);
			connectionEvent.onConnectionData(cid, packet);
		}
	}

	/**
	 * Dispatch event about error
	 *
	 * @param error instance of enum <code>SDError</code>
	 */
	private void dispatchError(SDError error) {
		if (connectionEvent != null) {
			connectionEvent.onConnectionError(cid, error);
		}
	}

	/**
	 * Dispatching raw bytes data
	 *
	 * @param bytes bytes
	 */
	private void dispatchRawData(byte[] bytes) {
		if (connectionEvent != null) connectionEvent.onRawDataReceived(cid, bytes);
	}
}
