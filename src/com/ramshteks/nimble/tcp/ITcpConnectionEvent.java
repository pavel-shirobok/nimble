package com.ramshteks.nimble.tcp;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public interface ITcpConnectionEvent {
	void onConnectionClosed(int cid);
}