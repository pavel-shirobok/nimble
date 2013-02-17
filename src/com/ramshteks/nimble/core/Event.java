package com.ramshteks.nimble.core;

public class Event{
	public static final String LOOP_START = "Event#LOOP_START";
	public static final String LOOP_END = "Event#LOOP_END";
	private String eventType;
	public Event(String eventType){
		this.eventType = eventType;
	}

	public String eventType() {
		return eventType;
	}
}