package com.ramshteks.nimble.server.tcp;

import com.ramshteks.nimble.core.Event;
import com.ramshteks.nimble.core.EventIO;
import com.ramshteks.nimble.core.EventStack;
import com.ramshteks.nimble.core.Nimble;
import com.ramshteks.nimble.server.*;
import com.ramshteks.nimble.server.logger.LogHelper;
import com.ramshteks.nimble.server.logger.LogLevel;
import com.ramshteks.nimble.server.logger.LoggerEvent;
import com.ramshteks.nimble.server.tcp.events.*;

import java.io.*;
import java.net.*;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public class TcpReceptor extends Receptor implements EventIO.EventFull, Runnable {

	private boolean isBinded = false;
	private ServerSocket socket;
	private Thread thread;
	private TcpConnectionsStack connectionsStack;
	private EventStack eventStack;
	private LogHelper logger;
	public TcpReceptor(Nimble nimble, ServerUtils.IDGenerator idGenerator, IPacketProcessorFactory packetProcessorFactory) {

		this.connectionsStack = createStack(nimble, idGenerator, packetProcessorFactory);
		eventStack = new EventStack();

		logger = new LogHelper(eventStack, "TcpReceptor");
	}

	private TcpConnectionsStack createStack(Nimble nimble, ServerUtils.IDGenerator idGenerator, IPacketProcessorFactory packetProcessorFactory) {
		return new TcpConnectionsStack(nimble, idGenerator, packetProcessorFactory);
	}

	@Override
	public void startBinding(InetAddress bindAddress, int port) throws IOException {
		if(isBinded){
			throw new IOException("Address already bind");
		}
		isBinded = true;

		socket = new ServerSocket(port, 0, bindAddress);
		createAndStartThread();
	}

	private void createAndStartThread() {
		thread = new Thread(this);
		thread.setPriority(Thread.MAX_PRIORITY);
		thread.start();
	}

	@Override
	public void stopBinding() throws IOException {
		isBinded = false;
	}

	@Override
	public boolean bindStarted() {
		return isBinded;
	}

	@Override
	public void pushEvent(Event event) {
		if(Event.START.equals(event.eventType())){
			eventStack.pushEvent(new LoggerEvent(LogLevel.Message, "Server started", "TcpReceptor"));
		}
	}

	@Override
	public boolean hasEventToHandle() {
		return eventStack.hasEventToHandle();
	}

	@Override
	public Event nextEvent() {
		return eventStack.nextEvent();
	}

	@Override
	public void run() {
		Socket acceptedSocket;
		while (!socket.isClosed()) {
			try {
				acceptedSocket = socket.accept();
			} catch (Exception secEx) {
				logger.logException("Accepting failed", secEx);
				break;
			}

			if(null != acceptedSocket){
				TcpConnectionInfo connectionInfo;

				try {
					connectionInfo = connectionsStack.createConnection(acceptedSocket);
				}catch (IOException exception){
					logger.logException("Creating connection failed", exception);
					continue;
				}

				eventStack.pushEvent(new TcpConnectionEvent(TcpConnectionEvent.CONNECT, connectionInfo));
			}
		}
	}

}
