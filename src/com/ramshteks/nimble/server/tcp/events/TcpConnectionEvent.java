package com.ramshteks.nimble.server.tcp.events;

import com.ramshteks.nimble.core.Event;
import com.ramshteks.nimble.server.tcp.TcpConnectionInfo;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public class TcpConnectionEvent extends Event {

	public static final String CONNECT = "TcpConnectionEvent#CONNECT";
	public static final String DISCONNECT = "TcpConnectionEvent#DISCONNECT";

	private TcpConnectionInfo connectionInfo;

	public TcpConnectionEvent(String eventType, TcpConnectionInfo connectionInfo) {
		super(eventType);
		this.connectionInfo = connectionInfo;
	}

	public TcpConnectionInfo connectionInfo() {
		return connectionInfo;
	}

	public static TcpConnectionEvent createDisconnect(TcpConnectionInfo connectionInfo){
		return new TcpConnectionEvent(DISCONNECT, connectionInfo);
	}
}
