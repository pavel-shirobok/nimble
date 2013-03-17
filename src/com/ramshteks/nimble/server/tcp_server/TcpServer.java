package com.ramshteks.nimble.server.tcp_server;

import com.ramshteks.nimble.core.*;
import com.ramshteks.nimble.server.*;
import com.ramshteks.nimble.server.tcp.*;
import com.ramshteks.nimble.server.tcp.events.*;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public class TcpServer implements EventIO.EventFull {
	private final ArrayList<Socket> newSockets;
	private final Hashtable<Integer, TcpConnection> connections;
	private IPacketProcessorFactory packetProcessorFactory;
	private ServerUtils.IDGenerator idGenerator;
	@SuppressWarnings("FieldCanBeLocal")
	private Receptor.ReceptorEvent receptorHandler;
	private TcpConnection.TcpConnectionCallback connectionCallback;
	private Receptor receptor;

	private final EventIO.EventFull outputEvent;

	public TcpServer(IPacketProcessorFactory packetProcessorFactory, ServerUtils.IDGenerator idGenerator) {
		this.packetProcessorFactory = packetProcessorFactory;
		this.idGenerator = idGenerator;
		newSockets = new ArrayList<>();
		connections = new Hashtable<>();
		outputEvent = new EventStack();
		receptorHandler = createRecepterEventHandler();
		connectionCallback = createConnectionEventHandler();
		receptor = createReceptor(receptorHandler);
	}

	private TcpReceptor createReceptor(Receptor.ReceptorEvent receptorEvent){
		TcpReceptor receptor = new TcpReceptor();
		receptor.addReceptorEvent(receptorEvent);
		return receptor;
	}

	@Override
	public void pushEvent(Event event) {
		if(Event.equalHash(event, NimbleEvent.ENTER_IN_QUEUE)){
			InetAddress inetAddress = null;
			try {
				inetAddress = InetAddress.getByName("localhost") ;
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			try {
				receptor.startBinding(inetAddress, 2305);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if(Event.equalHash(event, NimbleEvent.LOOP_START)){
			synchronized (newSockets){
				if(newSockets.size() != 0){
					Socket socket = newSockets.remove(newSockets.size() - 1);
					TcpConnectionInfo connectionInfo = new TcpConnectionInfo(idGenerator.nextID());
					TcpConnection newConnection;
					PacketProcessor packetProcessor = packetProcessorFactory.createNewInstance(connectionInfo);

					try{
						newConnection = (new TcpConnection(socket, connectionInfo, packetProcessor, 1000));
					}catch (Exception exception){
						return;
						//TODO:
					}

					newConnection.setConnectionEvent(connectionCallback);
					connections.put(connectionInfo.connection_id(), newConnection);
					TcpConnectionEvent connectionEvent = TcpConnectionEvent.createConnect(connectionInfo);
					dispatchEvent(connectionEvent);
				}
			}

			Enumeration<TcpConnection> connectionCollection = connections.elements();
			TcpConnection currentConnection;
			while (connectionCollection.hasMoreElements()){
				currentConnection = connectionCollection.nextElement();
				currentConnection.doCycle();
			}
		}

		if(Event.equalHash(event, TcpPacketEvent.TCP_PACKET_SEND)){
			TcpPacketEvent packetEvent = (TcpPacketEvent)event;
			TcpConnection connection = connections.get(packetEvent.target().connection_id());
			if(connection!=null){
				connection.send(packetEvent.bytes());
			}
		}
	}

	@Override
	public boolean hasEventToHandle() {
		synchronized (outputEvent){
			return outputEvent.hasEventToHandle();
		}
	}


	@Override
	public Event nextEvent() {
		synchronized (outputEvent){
			return outputEvent.nextEvent();
		}
	}

	private Receptor.ReceptorEvent createRecepterEventHandler() {
		return new Receptor.ReceptorEvent() {
			@Override
			public void onAcceptedSocket(Socket socket) {
				synchronized (newSockets){
					newSockets.add(socket);
				}
			}

			@Override
			public void onError(Exception exception, String message) {
				//TODO:
			}

			@Override
			public void onWarning(String string) {
				//TODO:
			}
		};
	}

	private void dispatchEvent(Event event){
		synchronized (outputEvent){
			outputEvent.pushEvent(event);
		}
	}

	private TcpConnection.TcpConnectionCallback createConnectionEventHandler() {
		return new TcpConnection.TcpConnectionCallback() {
			@Override
			public void onDataSend(TcpConnectionInfo connectionInfo, byte[] bytes) {
				dispatchEvent(new TcpPacketEvent(TcpPacketEvent.TCP_PACKET_SEND, connectionInfo, bytes));
			}

			@Override
			public void onDataReceived(TcpConnectionInfo connectionInfo, byte[] bytes) {
				dispatchEvent(new TcpPacketEvent(TcpPacketEvent.TCP_PACKET_RECV, connectionInfo, bytes));
			}

			@Override
			public void onConnectionClosed(TcpConnectionInfo connectionInfo) {
				connections.remove(connectionInfo.connection_id());
				dispatchEvent(TcpConnectionEvent.createDisconnect(connectionInfo));
			}

			@Override
			public void onError(Exception error, String message) {
			}

			@Override
			public void onWarning(String message) {
			}
		};
	}
}
