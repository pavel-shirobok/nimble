package com.ramshteks.nimble.core;

public interface EventIO{

	public static interface EventFull extends EventReceiver, EventSender{}

	public static interface EventReceiver {
		void pushEvent(Event event);
		boolean compatibleInput(String eventType);
	}

	public static interface EventSender {
		boolean hasEventToHandle();
		Event nextEvent();
	}

}