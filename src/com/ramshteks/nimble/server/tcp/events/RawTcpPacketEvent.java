package com.ramshteks.nimble.server.tcp.events;

import com.ramshteks.nimble.core.Event;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public class RawTcpPacketEvent extends Event {
	public static final String RAW_PACKET_RECV = "RawTcpPacketEvent#RAW_PACKET_RECV";

	public RawTcpPacketEvent(String eventType) {
		super(eventType);
	}

	public byte[] bytes() {
		return new byte[0];
	}
}
