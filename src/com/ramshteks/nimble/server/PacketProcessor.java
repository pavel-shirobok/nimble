package com.ramshteks.nimble.server;

import com.ramshteks.nimble.server.tcp.TcpConnectionInfo;

import java.util.ArrayList;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public abstract class PacketProcessor {

	public abstract void processBytesFromSocket(TcpConnectionInfo connectionInfo, byte[] bytes);

	public abstract void processBytesToSocket(TcpConnectionInfo connectionInfo, byte[] bytes);

	protected ArrayList<byte[]> packetsToSocket;
	protected ArrayList<byte[]> packetsFromSocket;

	public PacketProcessor() {
		packetsFromSocket = new ArrayList<>();
		packetsToSocket = new ArrayList<>();
	}

	protected void pushReceivedPacket(byte[] bytes) {
		packetsFromSocket.add(bytes);
	}

	protected void pushSendPacket(byte[] bytes) {
		packetsToSocket.add(bytes);
	}

	public boolean hasReceivedPacket() {
		return !packetsFromSocket.isEmpty();
	}

	public boolean hasSendingPacket() {
		return !packetsToSocket.isEmpty();
	}

	public byte[] nextReceivedPacket() {
		return packetsFromSocket.remove(0);
	}

	public byte[] nextPacketForSend() {
		return packetsToSocket.remove(0);
	}
}

