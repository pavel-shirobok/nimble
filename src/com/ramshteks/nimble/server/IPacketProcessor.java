package com.ramshteks.nimble.server;

import com.ramshteks.nimble.server.tcp.TcpConnectionInfo;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public interface IPacketProcessor {

	void processBytesFromSocket(TcpConnectionInfo connectionInfo, byte[] bytes);
	void processBytesToSocket(TcpConnectionInfo connectionInfo, byte[] bytes);

	boolean hasReceivedPacket();
	boolean hasSendingPacket();

	byte[] nextReceivedPacket();
	byte[] nextPacketForSend();

	/*ArrayList<byte[]> toSocket();
	ArrayList<byte[]> fromSocket();*/
}

