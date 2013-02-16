package com.ramshteks.nimble.event_machine;

import java.util.LinkedList;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public abstract class LinkedListBasedEventDispatcher implements EventDispatcher {
	protected LinkedList<EventDispatcher> listeners = new LinkedList<EventDispatcher>();;

	public abstract void captureEvent(Event event);

	@Override
	public void dispatchEvent(Event event) {
		for(EventDispatcher listener: listeners){
			listener.captureEvent(event);
		}
	}

	@Override
	public void addEventListener(EventDispatcher listener) {
		if(hasListener(listener)){
			throw new IllegalArgumentException("Passed listener already added");
		}
		listeners.addLast(listener);
	}

	@Override
	public void removeEventListener(EventDispatcher listener) {
		if(!hasListener(listener)){
			throw new IllegalArgumentException("Passed does not exist");
		}
	}

	@Override
	public boolean hasListener(EventDispatcher listener) {
		return listeners.contains(listener);
	}

}
