package com.ramshteks.nimble.server.tcp_server;

import com.ramshteks.nimble.core.Event;
import com.ramshteks.nimble.core.EventIO;
import com.ramshteks.nimble.core.EventStack;
import com.ramshteks.nimble.core.NimbleEvent;
import com.ramshteks.nimble.server.IPacketProcessor;
import com.ramshteks.nimble.server.IPacketProcessorFactory;
import com.ramshteks.nimble.server.Receptor;
import com.ramshteks.nimble.server.ServerUtils;
import com.ramshteks.nimble.server.tcp.TcpConnection;
import com.ramshteks.nimble.server.tcp.TcpConnectionInfo;
import com.ramshteks.nimble.server.tcp.TcpReceptor;
import com.ramshteks.nimble.server.tcp.events.TcpConnectionEvent;
import com.ramshteks.nimble.server.tcp.events.TcpPacketEvent;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public class TcpServer implements EventIO.EventFull {
	private final ArrayList<Socket> newSockets;
	private Hashtable<Integer, TcpConnection> connections;
	private IPacketProcessorFactory packetProcessorFactory;
	private ServerUtils.IDGenerator idGenerator;
	private Receptor.ReceptorEvent receptorHandler;
	private TcpConnection.TcpConnectionCallback connectionCallback;
	private Receptor receptor;

	private final EventIO.EventFull outputEvent;
	private final EventIO.EventReceiver inputEvent;

	public TcpServer(IPacketProcessorFactory packetProcessorFactory, ServerUtils.IDGenerator idGenerator) {
		this.packetProcessorFactory = packetProcessorFactory;
		this.idGenerator = idGenerator;
		newSockets = new ArrayList<>();
		connections = new Hashtable<>();
		outputEvent = new EventStack();
		inputEvent = new EventStack();
		receptorHandler = createRecepterEventHandler();
		connectionCallback = createConnectionEventHandler();
		receptor = new TcpReceptor();
		receptor.addReceptorEvent(receptorHandler);
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
					TcpConnection newConnection = null;
					IPacketProcessor packetProcessor = packetProcessorFactory.createNewInstance(connectionInfo);

					try{
						newConnection = (new TcpConnection(socket, connectionInfo, packetProcessor, 1000));
					}catch (Exception exception){

						//TODO:
					}
					if(newConnection == null){
						//TODO:
					}
					newConnection.setConnectionEvent(connectionCallback);
					connections.put(connectionInfo.connection_id(), newConnection);
					outputEvent.pushEvent(new TcpConnectionEvent(TcpConnectionEvent.CONNECT, connectionInfo));
				}
			}


			Collection<TcpConnection> connectionCollection = connections.values();
			for(TcpConnection connection: connectionCollection){
				connection.doCycle();
			}
		}

		if(Event.equalHash(event, TcpPacketEvent.TCP_PACKET_SEND)){
			TcpPacketEvent packetEvent = (TcpPacketEvent)event;
			TcpConnection connection = connections.get(packetEvent.target().connection_id());
			connection.send(packetEvent.bytes());
		}
	}

	@Override
	public boolean hasEventToHandle() {
		return outputEvent.hasEventToHandle();
	}


	@Override
	public Event nextEvent() {
		return outputEvent.nextEvent();
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

	private TcpConnection.TcpConnectionCallback createConnectionEventHandler() {
		return new TcpConnection.TcpConnectionCallback() {
			@Override
			public void onDataSended(TcpConnectionInfo connectionInfo, byte[] bytes) {
				outputEvent.pushEvent(new TcpPacketEvent(TcpPacketEvent.TCP_PACKET_SEND, connectionInfo, bytes));
			}

			@Override
			public void onDataReceived(TcpConnectionInfo connectionInfo, byte[] bytes) {
				outputEvent.pushEvent(new TcpPacketEvent(TcpPacketEvent.TCP_PACKET_RECV, connectionInfo, bytes));

			}

			@Override
			public void onConnectionClosed(TcpConnectionInfo connectionInfo) {
				connections.remove(connectionInfo.connection_id());
				outputEvent.pushEvent((TcpConnectionEvent.createDisconnect(connectionInfo)));
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
