package com.ramshteks.nimble.server.tcp.events;

import com.ramshteks.nimble.core.Event;
import com.ramshteks.nimble.server.tcp.TcpConnectionInfo;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public class TcpPacketEvent extends Event {
	public static final String TCP_PACKET_SEND = "TcpPacketEvent#TCP_PACKET_SEND";
	public static final String TCP_PACKET_RECV = "TcpPacketEvent#TCP_PACKET_RECV";

	private TcpConnectionInfo target;

	private byte[] bytes;

	public TcpPacketEvent(String eventType, TcpConnectionInfo target, byte[] bytes) {
		super(eventType);
		this.target = target;
		this.bytes = bytes;
	}

	public TcpConnectionInfo target() {
		return target;
	}

	public byte[] bytes() {
		return bytes;
	}
}
