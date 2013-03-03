package com.ramshteks.nimble.core;

public interface EventIO{
	public static interface EventBase{}
	public static interface EventFull extends EventReceiver, EventSender{}

	public static interface EventReceiver extends EventBase{
		void pushEvent(Event event);
	}

	public static interface EventSender extends EventBase{
		boolean hasEventToHandle();
		Event nextEvent();
	}

}