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
	private boolean compatibleAll = false;
	public EventStack(){
		events = new LinkedList<Event>();
		compatibleAll = true;
	}

	public EventStack(String[] acceptable){
		this.acceptable = acceptable;
		events = new LinkedList<Event>();
	}

	public void pushEvent(Event event){
		events.addLast(event);
	}

	@Override
	public boolean compatibleInput(String eventType) {
		if(compatibleAll)return true;

		int len = acceptable.length;
		for (int i = 0; i < len; i++){
			if(acceptable[i].equals(eventType))return true;
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
