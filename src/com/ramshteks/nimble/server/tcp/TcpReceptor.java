package com.ramshteks.nimble.server.tcp;

import com.ramshteks.nimble.server.Receptor;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public class TcpReceptor extends Receptor implements Runnable {

	private boolean isSocketBind = false;
	private ServerSocket socket;

	public TcpReceptor() {}

	@Override
	public void startBinding(InetAddress bindAddress, int port) throws IOException {
		if(isSocketBind){
			throw new IOException("Address already bind");
		}
		isSocketBind = true;

		socket = new ServerSocket(port, 0, bindAddress);
		createAndStartThread();
	}

	private void createAndStartThread() {
		Thread thread = new Thread(this);
		thread.setPriority(Thread.MAX_PRIORITY);
		thread.start();
	}

	@Override
	public void stopBinding() throws IOException {
		isSocketBind = false;
	}

	@Override
	public boolean bindStarted() {
		return isSocketBind;
	}

	@Override
	public void run() {
		Socket acceptedSocket;
		while (!socket.isClosed()) {
			try {
				acceptedSocket = socket.accept();
			} catch (Exception ex) {
				dispatchError(ex, "Accepting failed");
				break;
			}

			if(null != acceptedSocket){

				dispatchNewSocket(acceptedSocket);
			}
		}
	}
}
