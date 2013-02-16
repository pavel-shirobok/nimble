package com.ramshteks.nimble.event_machine;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public interface EventIO{

	public static interface EventReceiver {
		void pushEvent(Event event);
		boolean compatibleInput(CoreEventType coreEventType);
	}

	public static interface EventSender {
		boolean hasEventToHandle();
		Event nextEvent();
	}
}