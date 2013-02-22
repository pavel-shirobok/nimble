package com.ramshteks.nimble.server.tcp;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public interface ITcpConnectionEvent {
	void onConnectionClosed(TcpConnectionInfo connectionInfo);
}