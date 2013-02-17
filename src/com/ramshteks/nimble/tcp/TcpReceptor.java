package com.ramshteks.nimble.tcp;

import com.ramshteks.nimble.core.Event;
import com.ramshteks.nimble.core.EventIO;
import com.ramshteks.nimble.core.EventStack;
import com.ramshteks.nimble.server.*;
import com.ramshteks.nimble.tcp.events.*;

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

	public TcpReceptor(TcpConnectionsStack connectionsStack) {
		this.connectionsStack = connectionsStack;
		eventStack = new EventStack(new String[]{Event.LOOP_START});
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

		//check socket to disconnect &
	}

	@Override
	public boolean compatibleInput(String eventType) {
		return eventStack.compatibleInput(eventType);
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
			} catch (SecurityException secEx) {
				System.out.println("tcp accepter security error");
				break;
			} catch (SocketTimeoutException socTEx) {
				System.out.println("tcp accepter timeout error");
				break;
			} catch (IOException ioEx) {
				System.out.println("tcp accepter io error");
				break;
			}

			if(null != acceptedSocket){
				TcpConnectionInfo connectionInfo = connectionsStack.createConnection(acceptedSocket);
				pushEvent(new TcpConnectionEvent(TcpConnectionEvent.CONNECT, connectionInfo));
			}
		}
	}
}
