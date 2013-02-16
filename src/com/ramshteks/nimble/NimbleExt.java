/*
package com.ramshteks.nimble;

*/
/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 *//*

public class NimbleExt {

	*/
/**
	 * Enumeration of core oriented event types.
	 * Each type have priority, then low value , then low priority
	 *//*

	public static enum CoreEventType{
		CoreCommand(3),
		CorePacket(2),
		ReceivePacket(1),
		SendPacket(0),
		LogicCommand(1);

		private int _priority;
		CoreEventType(int priority){
			_priority = priority;
		}

		public int getPriority() {
			return _priority;
		}
	}

	public static interface Destroyable{
		void destroy();
	}

	public static interface EventGenerative extends Iterable<CoreEvent>{}

	public interface CoreEvent extends Destroyable{
		CoreEventType getEventType();
	}

	public static class Event implements CoreEvent{

		private CoreEventType eventType;

		public Event(CoreEventType eventType){
			this.eventType = eventType;
		}

		@Override
		public CoreEventType getEventType() {
			return eventType;
		}

		@Override
		public void destroy() {
			eventType = null;
		}
	}

	public static final class RawPacket extends Event{
		private byte[] bytes;

		public RawPacket(CoreEventType eventType, byte[] bytes) {
			super(eventType);
			if(eventType == CoreEventType.CoreCommand){
				throw new IllegalArgumentException("Unexpected CoreEventType '"+eventType.name()+"'");
			}
			this.bytes = bytes;
		}

		byte[] getData(){
			return  bytes;
		}

		@Override
		public void destroy() {
			super.destroy();
			bytes = null;
		}
	}

	public static interface PacketProcessorFactory {
		PacketProcessor createPacketProcessor();
	}

	public static interface PacketProcessor extends Destroyable, EventGenerative{
		void addToPacking(RawPacket packet);
		void addToUnpacking(byte[] rawData);

		boolean doPacking();
		boolean doUnpacking();
	}

}
*/
