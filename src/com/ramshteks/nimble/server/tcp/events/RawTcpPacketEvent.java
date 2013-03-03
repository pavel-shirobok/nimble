package com.ramshteks.nimble.server.tcp.events;

import com.ramshteks.nimble.core.Event;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public class RawTcpPacketEvent extends Event {
	public static final String RAW_TCP_PACKET_TO_SEND = "RawTcpPacketEvent#RAW_TCP_PACKET_TO_SEND";
	private byte[] bytes;

	public RawTcpPacketEvent(String eventType, byte[] bytes) {
		super(eventType);
		this.bytes = bytes;
	}

	public byte[] bytes() {
		return bytes;
	}
}
