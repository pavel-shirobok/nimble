package com.ramshteks.nimble.tcp.events;

import com.ramshteks.nimble.core.CoreEventType;
import com.ramshteks.nimble.core.Event;

import java.net.Socket;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public class ConnectionEvent extends Event {
	private int connection_id;
	private Socket socket;

	public ConnectionEvent(CoreEventType coreEventType, int connection_id, Socket socket) {
		super(coreEventType, "Connection");
		this.connection_id = connection_id;
		this.socket = socket;
	}

	public int connection_id() {
		return connection_id;
	}

	public Socket socket() {
		return socket;
	}
}
