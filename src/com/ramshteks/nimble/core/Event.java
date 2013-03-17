package com.ramshteks.nimble.core;

import com.ramshteks.nimble.server.ServerUtils;

import java.util.HashMap;

public class Event{
	public static enum Priority{
		HIGH, COMMON
	}

	private static HashMap<String, Integer> typeToIntegerIdentifier;
	private static ServerUtils.IDGenerator idGenerator;

	static {
		typeToIntegerIdentifier = new HashMap<>();
		idGenerator = new ServerUtils.IDGenerator(0, 1000);
	}

	public static int getHashCodeOfEventType(String eventType){
		return getOrCreateHashCode(eventType);
	}

	public static int getOrCreateHashCode(String eventType){
		if(!typeToIntegerIdentifier.containsKey(eventType)){
			typeToIntegerIdentifier.put(eventType, idGenerator.nextID());
		}
		return typeToIntegerIdentifier.get(eventType);
	}

	public static boolean equalHash(Event event, String eventType){
		return event.eventTypeHashCode() == getHashCodeOfEventType(eventType);
	}

	private String eventType;
	private int hashCode;
	private Priority priority = Priority.COMMON;

	public Event(String eventType){
		this.eventType = eventType;
		hashCode = getOrCreateHashCode(eventType);
	}

	public final Priority priority(){
		return priority;
	}

	public void setHighPriority(){
		priority = Priority.HIGH;
	}

	@SuppressWarnings("UnusedDeclaration")
	public final String eventType() {
		return eventType;
	}

	public final int eventTypeHashCode(){
		return hashCode;
	}
}