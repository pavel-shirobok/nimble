package com.ramshteks.nimble.event_machine;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public abstract class Event{
	public static final String PACKET = "packet";

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