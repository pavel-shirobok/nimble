package com.ramshteks.nimble;

import java.util.LinkedList;
import static com.ramshteks.nimble.Event.Priority.*;
/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public class EventStack implements EventIO.EventSender, EventIO.EventReceiver, EventIO.EventFull {
	private LinkedList<Event> events;

	public EventStack(){
		events = new LinkedList<>();
	}

	public void pushEvent(Event event){
		if(HIGH == event.priority()){
			events.addLast(event);
		}else{
			events.addFirst(event);
		}
	}

	public boolean hasEventToHandle(){
		return !events.isEmpty();
	}

	@Override
	public Event nextEvent() {
		return events.pollLast();
	}

}
