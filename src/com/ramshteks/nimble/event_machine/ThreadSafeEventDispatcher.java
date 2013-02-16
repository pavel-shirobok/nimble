package com.ramshteks.nimble.event_machine;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public class ThreadSafeEventDispatcher extends LinkedListBasedEventDispatcher {
	@Override
	public synchronized void addEventListener(EventDispatcher listener) {
		//synchronized (listener){
			super.addEventListener(listener);
		//}
	}

	@Override
	public synchronized void captureEvent(Event event) {

	}

	@Override
	public synchronized void dispatchEvent(Event event) {
		//synchronized (event){
			super.dispatchEvent(event);
		//}
	}

	@Override
	public synchronized boolean hasListener(EventDispatcher listener) {
		boolean result;
		//synchronized (listener){
			result = super.hasListener(listener);
		//}
		return result;
	}

	@Override
	public synchronized void removeEventListener(EventDispatcher listener) {
		super.removeEventListener(listener);
	}
}
