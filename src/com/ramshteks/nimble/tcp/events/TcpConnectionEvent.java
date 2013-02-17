package com.ramshteks.nimble.tcp.events;

import com.ramshteks.nimble.core.Event;
import com.ramshteks.nimble.tcp.TcpConnectionInfo;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public class TcpConnectionEvent extends Event {

	public static final String CONNECT = "com.ramshteks.nimble.tcp.events.TcpConnectionEvent#CONNECT";
	public static final String DISCONNECT = "com.ramshteks.nimble.tcp.events.TcpConnectionEvent#DISCONNECT";
	private TcpConnectionInfo connectionInfo;


	public TcpConnectionEvent(String eventType, TcpConnectionInfo connectionInfo) {
		super(eventType);
		this.connectionInfo = connectionInfo;
	}

	public TcpConnectionInfo connectionInfo() {
		return connectionInfo;
	}
}
