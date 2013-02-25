package com.ramshteks.nimble.core;

import java.util.ArrayList;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public class EventIOQueue<T extends EventIO.EventBase> {

	private ArrayList<T> list = new ArrayList<T>();

	public synchronized void addLast(T object) {
		list.add(list.size(), object);
	}

	public synchronized T removeFirst() {
		return list.remove(0);
	}

	public synchronized void remove(T object) {
		list.remove(object);
	}

	public synchronized boolean contains(T object) {
		return list.contains(object);
	}

	public synchronized int size() {
		return list.size();
	}

	public synchronized T get(int index) {
		return list.get(index);
	}
}
