package com.ramshteks.nimble.core;

public class Event{

	private String eventType;

	public Event(String eventType){
		this.eventType = eventType;
	}

	public String eventType() {
		return eventType;
	}
}