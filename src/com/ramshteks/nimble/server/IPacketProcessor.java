package com.ramshteks.nimble.server;

import com.ramshteks.nimble.core.CoreEventType;
import com.ramshteks.nimble.core.Event;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public interface IPacketProcessor {

	void addToPacking(RawPacket packet);
	void addToUnpacking(byte[] rawData);

	boolean doPacking();
	boolean doUnpacking();
	public static final class RawPacket extends Event {
		private byte[] bytes;

		public RawPacket(CoreEventType eventType, byte[] bytes) {
			super(eventType, PACKET);
			this.bytes = bytes;
		}

		byte[] getData(){
			return  bytes;
		}
	}

}

