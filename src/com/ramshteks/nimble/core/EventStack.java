package com.ramshteks.nimble.core;

import java.util.LinkedList;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public class EventStack implements EventIO.EventSender, EventIO.EventReceiver, EventIO.EventFull {
	private LinkedList<Event> events;
	public EventStack(){
		events = new LinkedList<Event>();
	}

	public void pushEvent(Event event){
		events.addFirst(event);
	}

	public boolean hasEventToHandle(){
		return !events.isEmpty();
	}

	@Override
	public Event nextEvent() {
		return events.pollLast();
	}

}
