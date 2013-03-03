package com.ramshteks.nimble.server.tcp;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public interface ITcpConnectionCallback {
	void onConnectionClosed(TcpConnectionInfo connectionInfo);
}