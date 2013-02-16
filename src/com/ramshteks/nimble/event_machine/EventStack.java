package com.ramshteks.nimble.event_machine;

import java.util.LinkedList;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public class EventStack implements EventIO.EventSender, EventIO.EventReceiver {
	private LinkedList<Event> events;
	private CoreEventType[] acceptable;

	public EventStack(CoreEventType[] acceptable){
		this.acceptable = acceptable;
		events = new LinkedList<Event>();
	}

	public void pushEvent(Event event){
		events.addLast(event);
	}

	@Override
	public boolean compatibleInput(CoreEventType coreEventType) {
		for(CoreEventType type : acceptable){
			if(type == coreEventType)return true;
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
