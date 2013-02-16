package com.ramshteks.nimble;

import java.util.Iterator;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public class Nimble {
	public static interface Destroyable{
		void destroy();
	}

	public static enum CoreEventType{
		CoreCommand(3),
		CorePacket(2),
		ReceivePacket(1),
		SendPacket(0);

		private int _priority;

		CoreEventType(int priority){
			_priority = priority;
		}

		public int getPriority() {
			return _priority;
		}
	}

	public interface EventDispatcher extends Iterable<Event>, Destroyable{
		boolean dispatchEvent(Event event);
	}

	public class ThreadSafeEventDispatcher implements EventDispatcher{

		@Override
		public synchronized boolean dispatchEvent(Event event) {
			return false;
		}

		@Override
		public synchronized void destroy() {
		}

		@Override
		public synchronized Iterator<Event> iterator() {
			return null;
		}
	}

	public abstract class Event{

		private CoreEventType coreEventType;
		private final String eventType;

		public Event(CoreEventType coreEventType, String eventType){
			this.coreEventType = coreEventType;
			this.eventType = eventType;
		}

		public CoreEventType getCoreEventType() {
			return coreEventType;
		}

		public String getEventType() {
			return eventType;
		}
	}

}
