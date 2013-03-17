package com.ramshteks.nimble;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public class NimbleEvent extends Event {
	public static final String LOOP_START = "NimbleEvent#LOOP_START";
	public static final String LOOP_END = "NimbleEvent#LOOP_END";

	public static final String ENTER_IN_QUEUE = "NimbleEvent#ENTER_IN_QUEUE";
	public static final String EXIT_FROM_QUEUE = "NimbleEvent#EXIT_FROM_QUEUE";

	public static final String CYCLE_STARTED = "NimbleEvent#CYCLE_STARTED";
	public static final String CYCLE_STOPPED = "NimbleEvent#CYCLE_STOPPED";

	public NimbleEvent(String eventType) {
		super(eventType);
	}
}
