/*
 * Copyright (c) 2013, Shirobok Pavel (ramshteks@gmail.com)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <organization> nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.ramshteks.nimble;

import java.util.*;

import static com.ramshteks.nimble.EventIO.*;
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
