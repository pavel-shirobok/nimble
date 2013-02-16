package com.ramshteks.nimble.core;

import com.ramshteks.nimble.event_machine.*;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public class Nimble {

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

	public abstract class PacketProcessor
	{
		public abstract void addToPacking(RawPacket packet);
		public abstract void addToUnpacking(byte[] rawData);

		public abstract boolean doPacking();
		public abstract boolean doUnpacking();
	}
}
