package com.ramshteks.nimble.event_machine;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public interface EventDispatcher{
	void captureEvent(Event event);
	void dispatchEvent(Event event);
	void addEventListener(EventDispatcher listener);
	void removeEventListener(EventDispatcher listener);
	boolean hasListener(EventDispatcher listener);
}