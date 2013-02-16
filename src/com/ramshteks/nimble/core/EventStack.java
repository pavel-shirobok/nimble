package com.ramshteks.nimble.core;

import java.util.LinkedList;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public class EventStack implements EventIO.EventSender, EventIO.EventReceiver {
	private LinkedList<Event> events;
	private String[] acceptable;

	public EventStack(String[] acceptable){
		this.acceptable = acceptable;
		events = new LinkedList<Event>();
	}

	public void pushEvent(Event event){
		events.addLast(event);
	}

	@Override
	public boolean compatibleInput(String coreEventType) {
		for(String type : acceptable){
			if(type.equals(coreEventType))return true;
		}
		return false;
	}

	public boolean hasEventToHandle(){
		return !events.isEmpty();
	}

	@Override
	public Event nextEvent() {
		return events.pollFirst();
	}

}
