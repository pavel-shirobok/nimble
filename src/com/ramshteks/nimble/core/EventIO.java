package com.ramshteks.nimble.core;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public interface EventIO{

	public static interface EventFull extends EventReceiver, EventSender{}

	public static interface EventReceiver {
		void pushEvent(Event event);
		boolean compatibleInput(CoreEventType coreEventType);
	}

	public static interface EventSender {
		boolean hasEventToHandle();
		Event nextEvent();
	}

}