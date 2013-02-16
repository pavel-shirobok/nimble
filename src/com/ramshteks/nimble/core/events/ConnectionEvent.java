package com.ramshteks.nimble.core.events;

import com.ramshteks.nimble.event_machine.CoreEventType;
import com.ramshteks.nimble.event_machine.Event;

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

	public int getConnection_id() {
		return connection_id;
	}

	public Socket getSocket() {
		return socket;
	}
}
