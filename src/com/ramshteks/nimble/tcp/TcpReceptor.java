package com.ramshteks.nimble.tcp;

import com.ramshteks.nimble.server.Receptor;
import com.ramshteks.nimble.tcp.events.ConnectionEvent;
import com.ramshteks.nimble.core.CoreEventType;

import java.io.*;
import java.net.*;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public class TcpReceptor extends Receptor implements Runnable {

	private boolean isBinded = false;
	private ServerSocket socket;
	private Thread thread;

	public TcpReceptor() {
		super(new CoreEventType[]{});
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
				pushEvent(new ConnectionEvent(CoreEventType.NewConnection, 0, acceptedSocket));
			}
		}
	}
}
