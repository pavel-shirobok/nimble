package com.ramshteks.nimble.tcp.events;

import com.ramshteks.nimble.core.Event;

import java.net.Socket;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public class TcpConnectionEvent extends Event {

	public static final String CONNECT = "com.ramshteks.nimble.tcp.events.TcpConnectionEvent#CONNECT";
	public static final String DISCONNECT = "com.ramshteks.nimble.tcp.events.TcpConnectionEvent#DISCONNECT";

	private Socket socket;

	public TcpConnectionEvent(String eventType, Socket socket) {
		super(eventType);
		this.socket = socket;
	}

	public Socket socket() {
		return socket;
	}
}
