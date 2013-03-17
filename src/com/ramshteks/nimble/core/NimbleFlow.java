package com.ramshteks.nimble.core;

import java.util.*;

import static com.ramshteks.nimble.core.EventIO.*;
/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public class NimbleFlow {
	private NimbleEvent startLoopEvent = new NimbleEvent(NimbleEvent.LOOP_START);
	private NimbleEvent endLoopEvent = new NimbleEvent(NimbleEvent.LOOP_END);

	private NimbleEvent enter_in_queue = new NimbleEvent(NimbleEvent.ENTER_IN_QUEUE);
	private NimbleEvent exit_from_queue = new NimbleEvent(NimbleEvent.EXIT_FROM_QUEUE);

	private List<EventReceiver> receivers;
	private List<EventSender> senders;

	private EventStack eventsFlow;

	private long startLoopTime;
	private long endLoopTime;

	public NimbleFlow() {
		receivers = new LinkedList<>();
		senders = new LinkedList<>();
		eventsFlow = new EventStack();
	}

	public void add(EventSender sender){
		senders.add(sender);
	}

	public void add(EventReceiver receiver){
	   	receivers.add(receiver);
		receiver.pushEvent(enter_in_queue);
	}

	public void remove(EventSender sender){
		senders.remove(sender);
	}

	public void remove(EventReceiver receiver){
		receivers.remove(receiver);
		receiver.pushEvent(exit_from_queue);
	}

	public void notifyCycleStarted(){
		notifyAllReceivers(new NimbleEvent(NimbleEvent.CYCLE_STARTED));
	}

	public void notifyCycleStopped(){
		notifyAllReceivers(new NimbleEvent(NimbleEvent.CYCLE_STOPPED));
	}


	public void notifyStartLoop(){
		startLoopTime = System.currentTimeMillis();
		notifyAllReceivers(startLoopEvent);
	}

	public void notifyFinishLoop(){
		notifyAllReceivers(endLoopEvent);
		endLoopTime = System.currentTimeMillis();
	}

	public void notifyAllReceivers(Event event){
		for (EventReceiver receiver : receivers) {
			receiver.pushEvent(event);
		}
	}

	public void marshallEvents(){
		dispatchEvents(eventsFlow);
		scrapeEvents(eventsFlow);
	}

	public void dispatchEvents(EventSender source){
		int len = receivers.size();
		Event event;
		//noinspection ForLoopReplaceableByForEach
		for (int i = 0; i < len && source.hasEventToHandle(); i++)
		{
			event = source.nextEvent();
			notifyAllReceivers(event);
		}
	}

	public void scrapeEvents(EventReceiver destination){
		//noinspection ForLoopReplaceableByForEach
		for (EventSender sender : senders) {
			if(sender.hasEventToHandle()){
				destination.pushEvent(sender.nextEvent());
			}
		}
	}

	public long elapsedTimeForLoop(){
		return endLoopTime - startLoopTime;
	}

}
