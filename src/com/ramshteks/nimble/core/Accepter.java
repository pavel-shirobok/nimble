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
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.channels.IllegalBlockingModeException;

public class Accepter implements Runnable {

	/**
	 * Instance of <code>ServerSocket</code> object
	 */
	private ServerSocket socket;

	/**
	 * Instance of <code>Core.IAccepterEvent</code>
	 * Event listener object
	 */
	private Core.IAccepterEvent eventListener;

	/**
	 * Constructor
	 *
	 * @param s <code>ServerSocket</code> instance
	 */
	public Accepter(ServerSocket s) {
		socket = s;
	}

	public void run() {
		Socket s = null;
		SDError error = null;
		while (!socket.isClosed()) {
			try {
				s = socket.accept();
			} catch (SecurityException secEx) {
				error = SDError.ACCEPT_SECURITY;
			} catch (SocketTimeoutException socTEx) {
				error = SDError.ACCEPT_TIMEOUT;
			} catch (IllegalBlockingModeException illEx) {
				error = SDError.ACCEPT_ILLEGAL_BLOCKING;
			} catch (IOException ioEx) {
				error = SDError.ACCEPT_IO;
			}

			if (error != null) {
				dispatchError(error);
				break;
			}

			if (s != null) {
				dispatchEvent(s);
			} else {
				dispatchError(SDError.ACCEPT_RECV_NULL_SOCKET);
			}
		}
	}

	/**
	 * Dispatch event with <code>Socket</code> instance
	 *
	 * @param socket instance of <code>Socket</code>
	 */
	private void dispatchEvent(Socket socket) {
		if (eventListener != null) {
			eventListener.onNewSocket(socket);
		}
	}

	/**
	 * Dispatch event about error
	 *
	 * @param error instance of enum <code>SDError</code>
	 */
	private void dispatchError(SDError error) {
		if (eventListener != null) {
			eventListener.onAccepterError(error);
		}
	}

	/**
	 * Set event listener object
	 *
	 * @param eventListener object, instance of <code>Core.IAccepterEvent</code>
	 */
	public void setEventListener(Core.IAccepterEvent eventListener) {
		this.eventListener = eventListener;
	}
}
