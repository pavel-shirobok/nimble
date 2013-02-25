package com.ramshteks.nimble.core;

import java.util.ArrayList;

import static com.ramshteks.nimble.core.EventIO.*;


public class Nimble implements Runnable {

	private final EventIOQueue<EventBase> queueToAdd;
	private final EventIOQueue<EventBase> queueToRemove;

	//private final Object lock = new Object();

	private Thread mainThread;
	private final EventStack serverInputEventStream;
	private boolean mainThreadStarted = false;

	@SuppressWarnings("Convert2Diamond")
	public Nimble(){

		queueToAdd = new EventIOQueue<EventBase>();
		queueToRemove = new EventIOQueue<EventBase>();

		serverInputEventStream = new EventStack();
	}

	public void addPlugin(EventBase eventBase){
		synchronized (queueToAdd){
			queueToAdd.addLast(eventBase);
		}
	}

	public void removePlugin(EventBase eventBase){
		synchronized (queueToRemove){
			queueToRemove.addLast(eventBase);
		}
	}

	public void start(){
		mainThread = new Thread(this);
		mainThread.setPriority(Thread.MAX_PRIORITY);
		mainThread.start();
	}

	@SuppressWarnings("UnusedDeclaration")
	public void stop(){
		mainThreadStarted = false;
		mainThread = null;
	}

	@Override
	public void run() {
		//main server loop

		mainThreadStarted = true;

		long startTimeMillis;
		long endTimeMillis;
		boolean firstCycle = true;

		Event event;

		NimbleEvent startLoopEvent = new NimbleEvent(NimbleEvent.LOOP_START);
		NimbleEvent endLoopEvent = new NimbleEvent(NimbleEvent.LOOP_END);
		NimbleEvent enter_in_queue = new NimbleEvent(NimbleEvent.ENTER_IN_QUEUE);
		NimbleEvent exit_from_queue = new NimbleEvent(NimbleEvent.EXIT_FROM_QUEUE);

		//noinspection Convert2Diamond
		ArrayList<EventReceiver> receivers = new ArrayList<EventReceiver>();
		//noinspection Convert2Diamond
		ArrayList<EventSender> senders = new ArrayList<EventSender>();

		while (true){

			if(firstCycle){
				firstCycle = false;
				pushEventTo(receivers, new NimbleEvent(NimbleEvent.CYCLE_STARTED));
			}

			if(!mainThreadStarted){
				pushEventTo(receivers, new NimbleEvent(NimbleEvent.CYCLE_STOPPED));
				break;
			}

			processEnterToQueue(enter_in_queue, receivers, senders);

			startTimeMillis = System.currentTimeMillis();

			pushEventTo(receivers, startLoopEvent);


			if(serverInputEventStream.hasEventToHandle()){
				event = serverInputEventStream.nextEvent();
				pushEventTo(receivers, event);
			}

			int len = senders.size();
			for(int i = 0; i < len; i++)
			{
				EventIO.EventSender sender = senders.get(i);
				if(sender.hasEventToHandle()){
					serverInputEventStream.pushEvent(sender.nextEvent());
				}
			}


			pushEventTo(receivers, endLoopEvent);

			processExitFromQueue(exit_from_queue, receivers, senders);

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
				//noinspection EmptyCatchBlock
				try{
					Thread.sleep(sleepTime);
				}catch (Exception exception){}
			}
		}
	}

	private void processExitFromQueue(NimbleEvent exitEvent, ArrayList<EventReceiver> receivers, ArrayList<EventSender> senders) {
		synchronized (queueToRemove){
			if (queueToRemove.size() != 0) {
				EventBase eventBase;
				EventReceiver receiver;

				eventBase = queueToRemove.removeFirst();

				if(receivers.contains(eventBase)){
					receiver = (EventReceiver)eventBase;
					receivers.remove(receiver);
					receiver.pushEvent(exitEvent);
				}

				if(senders.contains(eventBase)){
					senders.remove(eventBase);
				}
			}
		}
	}

	private void processEnterToQueue(NimbleEvent enterEvent, ArrayList<EventReceiver> receivers, ArrayList<EventSender> senders) {
		synchronized (queueToAdd){
			if (queueToAdd.size() != 0) {
				int len = queueToAdd.size();
				EventBase eventBase;

				EventSender sender;
				EventReceiver receiver;

				//for (int index = 0; index < len; index++) {
				eventBase = queueToAdd.removeFirst();

				if(eventBase instanceof EventSender){
					senders.add(senders.size(), (EventSender)eventBase);
				}

				if(eventBase instanceof EventReceiver){
					receiver = (EventReceiver)eventBase;
					receivers.add(receivers.size(), receiver);
					receiver.pushEvent(enterEvent);
				}
				//}
			}
		}
	}

	private void pushEventTo(ArrayList<EventReceiver> receivers, Event event){
		int len = receivers.size();
		EventIO.EventReceiver receiver;
		for (int i = 0; i < len; i++){
			receiver = receivers.get(i);
			receiver.pushEvent(event);
		}
	}

}
