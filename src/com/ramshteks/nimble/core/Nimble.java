package com.ramshteks.nimble.core;

import java.util.LinkedList;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public class Nimble implements Runnable {

	private final LinkedList<EventIO.EventReceiver> receivers;
	private final LinkedList<EventIO.EventSender> senders;

	private Thread mainThread;
	private final EventStack serverInputEventStream;
	private boolean mainThreadStarted = false;
	public Nimble(){
		receivers = new LinkedList<EventIO.EventReceiver>();
		senders = new LinkedList<EventIO.EventSender>();
		serverInputEventStream = new EventStack();
	}

	public void addReceiverPlugin(EventIO.EventReceiver receiver){
		synchronized (receivers){
			receivers.addLast(receiver);
		}
	}

	public void addSenderPlugin(EventIO.EventSender sender){
		synchronized (senders){
			senders.addLast(sender);
		}
	}

	public void addFullEventPlugin(EventIO.EventFull eventFull){
		addSenderPlugin(eventFull);
		addReceiverPlugin(eventFull);
	}

	public void removeReceiverPlugin(EventIO.EventReceiver receiver){
		synchronized (receivers){
			receivers.remove(receiver);
		}
	}

	public void removeSenderPlugin(EventIO.EventSender sender){
		synchronized (senders){
			senders.remove(sender);
		}
	}

	public void removeFullEventPlugin(EventIO.EventFull eventFull){
		removeSenderPlugin(eventFull);
		removeReceiverPlugin(eventFull);
	}

	public boolean existReceiverPlugin(EventIO.EventReceiver receiver){
		boolean result;
		synchronized (receivers){
			result = receivers.contains(receiver);
		}
		return result;
	}

	public boolean existSenderPlugin(EventIO.EventSender sender){
		boolean result;
		synchronized (senders){
			result = senders.contains(sender);
		}
		return result;
	}

	public boolean existFullEventPlugin(EventIO.EventFull eventFull){
		return existReceiverPlugin(eventFull) && existSenderPlugin(eventFull);
	}

	public void start(){

		mainThread = new Thread(this);
		mainThread.setPriority(Thread.MAX_PRIORITY);
		mainThread.start();
	}

	public void stop(){
		//TODO: make stop thread
		mainThreadStarted = false;
		mainThread = null;
	}

	@Override
	public void run() {
		//main server loop

		mainThreadStarted = true;

		long startTimeMillis;
		long endTimeMillis;

		Event event;
		boolean firstCycle = true;

		Event startLoopEvent = new Event(Event.LOOP_START);
		Event endLoopEvent = new Event(Event.LOOP_END);

		while (true){

			if(!mainThreadStarted){
				break;
			}

			startTimeMillis = System.currentTimeMillis();

			if(firstCycle){
				firstCycle = false;
				synchronizedSendToAll(new Event(Event.START), receivers);
			}

			synchronizedSendToAll(startLoopEvent, receivers);

			if(serverInputEventStream.hasEventToHandle()){
				event = serverInputEventStream.nextEvent();
				synchronizedSendToAll(event, receivers);
			}

			synchronized (senders){
				for(EventIO.EventSender sender: senders){
					if(sender.hasEventToHandle()){
						serverInputEventStream.pushEvent(sender.nextEvent());
					}
				}
			}

			synchronizedSendToAll(endLoopEvent, receivers);

			endTimeMillis = System.currentTimeMillis();

			//balancing sleep time
			long sleepTime = 5;
			long loopTime = endTimeMillis - startTimeMillis;
			if(loopTime >= sleepTime){
				sleepTime = 0;
			}else{
				sleepTime = sleepTime - loopTime;
			}

			if(sleepTime!=0){
				try{
					Thread.sleep(sleepTime);
				}catch (Exception exception){}
			}
		}
	}

	private void synchronizedSendToAll(Event event, LinkedList<EventIO.EventReceiver> eventReceivers){
		synchronized (eventReceivers){
			int len = eventReceivers.size();
			EventIO.EventReceiver receiver;
			for (int i = 0; i < len; i++){
				receiver = eventReceivers.get(i);
				receiver.pushEvent(event);
			}
		}
	}

}
