package com.ramshteks.nimble.core;

import java.util.LinkedList;

import static com.ramshteks.nimble.core.EventIO.*;


public class Nimble implements Runnable {

	private final LinkedList<EventBase> queueToAdd;
	private final LinkedList<EventBase> queueToRemove;

	//private final Object lock = new Object();

	private Thread mainThread;
	private final EventStack serverInputEventStream;
	private boolean mainThreadStarted = false;
	private int count = 0;
	@SuppressWarnings("Convert2Diamond")
	public Nimble(){

		queueToAdd = new LinkedList<EventBase>();
		queueToRemove = new LinkedList<EventBase>();

		serverInputEventStream = new EventStack();
	}

	public void addPlugin(EventBase eventBase){
		count ++;
		synchronized (queueToAdd){
			queueToAdd.add(0, eventBase);
		}
	}

	public void removePlugin(EventBase eventBase){
		count --;
		synchronized (queueToRemove){
			queueToRemove.add(0, eventBase);
		}
	}

	public int pluginsCount(){
		return count;
	}

	public void start(){
		mainThread = new Thread(this);
		mainThread.setPriority(Thread.MAX_PRIORITY);
		mainThread.setName("Nimble-thread");
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
		LinkedList<EventReceiver> receivers = new LinkedList<EventReceiver>();
		//noinspection Convert2Diamond
		LinkedList<EventSender> senders = new LinkedList<EventSender>();

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

			//main loop start
			pushEventTo(receivers, startLoopEvent);

			int len = senders.size();
			for (int i = 0; i < len; i++)
			{
				if(serverInputEventStream.hasEventToHandle()){
					event = serverInputEventStream.nextEvent();
					pushEventTo(receivers, event);
				}
			}

			int eventsCount = 0;
			//int len = senders.size();
			for(int i = 0; i < len; i++)
			{
				EventIO.EventSender sender = senders.get(i);
				if(sender.hasEventToHandle()){
					serverInputEventStream.pushEvent(sender.nextEvent());
					eventsCount ++;
				}
			}


			pushEventTo(receivers, endLoopEvent);
			//main loop finish

			processExitFromQueue(exit_from_queue, receivers, senders);

			endTimeMillis = System.currentTimeMillis();
			//System.out.println("loop "+(endTimeMillis - startTimeMillis) + " read events: "+eventsCount);
			//balancing sleep time
			long sleepTime = 5;
			/*long loopTime = endTimeMillis - startTimeMillis;
			if(loopTime >= sleepTime){
				sleepTime = 0;
			}else{
				sleepTime = sleepTime - loopTime;
			}
			if(loopTime > 100){
				System.out.println("                                ###Overhead: " + loopTime +" ###");
			}
			if(sleepTime!=0){
				//noinspection EmptyCatchBlock

			}*/
			try{
				Thread.sleep(sleepTime);
			}catch (Exception exception){}
		}
	}

	private void processExitFromQueue(NimbleEvent exitEvent, LinkedList<EventReceiver> receivers, LinkedList<EventSender> senders) {
		synchronized (queueToRemove){
			if (queueToRemove.size() != 0) {
				EventBase eventBase;
				EventReceiver receiver;

				eventBase = queueToRemove.remove(queueToRemove.size() - 1);

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

	private void processEnterToQueue(NimbleEvent enterEvent, LinkedList<EventReceiver> receivers, LinkedList<EventSender> senders) {

		synchronized (queueToAdd){
			if (queueToAdd.size() != 0) {
				int len = queueToAdd.size();
				EventBase eventBase;

				EventSender sender;
				EventReceiver receiver;

				for (int index = 0; index < len; index++) {
					eventBase = queueToAdd.remove(queueToAdd.size() - 1);

					if(eventBase instanceof EventSender){
						senders.addLast((EventSender)eventBase);
					}

					if(eventBase instanceof EventReceiver){
						receiver = (EventReceiver)eventBase;
						receivers.addLast(receiver);
						receiver.pushEvent(enterEvent);
					}
				}
			}
		}
	}

	private void pushEventTo(LinkedList<EventReceiver> receivers, Event event){
		int len = receivers.size();
		EventIO.EventReceiver receiver;
		for (int i = 0; i < len; i++){
			receiver = receivers.get(i);
			receiver.pushEvent(event);
		}
	}

}
