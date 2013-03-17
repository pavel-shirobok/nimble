package com.ramshteks.nimble;

import java.util.ArrayList;

import static com.ramshteks.nimble.EventIO.*;


public class Nimble implements Runnable {

	private final ArrayList<EventBase> queueToAdd;
	private final ArrayList<EventBase> queueToRemove;

	private Thread mainThread;
	private NimbleFlow flow;

	private boolean mainThreadStarted = false;

	public Nimble(){

		flow = new NimbleFlow();
		queueToAdd = new ArrayList<>();
		queueToRemove = new ArrayList<>();
	}

	public void addPlugin(EventBase eventBase){
		synchronized (queueToAdd){
			queueToAdd.add(0, eventBase);
		}
	}

	@SuppressWarnings("UnusedDeclaration")
	public void removePlugin(EventBase eventBase){
		synchronized (queueToRemove){
			queueToRemove.add(0, eventBase);
		}
	}

	public void start(){
		if(mainThreadStarted)throw new RuntimeException("Nimble already started!");
		mainThread = createMainThread(this);
		mainThread.start();
	}

	private Thread createMainThread(Runnable runnable){
		Thread thread = new Thread(runnable);
		thread.setPriority(Thread.MAX_PRIORITY);
		thread.setName("Nimble-thread");
		return thread;
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

		flow.notifyCycleStarted();

		while (mainThreadStarted){

			processEnterToQueue();
			//{main loop start
				flow.notifyStartLoop();
				flow.marshallEvents();
				flow.notifyFinishLoop();
			//}main loop finish
			processExitFromQueue();

			sleepIfNecessary(flow.elapsedTimeForLoop());
		}

		flow.notifyCycleStopped();
	}

	private void sleepIfNecessary(long loopTime){
		long sleepTime = 1;
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

	private void processExitFromQueue() {
		EventBase eventBase = null;
		synchronized (queueToRemove){
			if (queueToRemove.size() != 0) {
				eventBase = queueToRemove.remove(queueToRemove.size() - 1);
			}
		}

		if(eventBase == null)return;

		if(eventBase instanceof EventReceiver){
			flow.remove((EventReceiver)eventBase);
		}

		if(eventBase instanceof EventSender){
			flow.remove((EventSender)eventBase);
		}

	}

	private void processEnterToQueue() {
		EventBase eventBase = null;
		synchronized (queueToAdd){
			if (queueToAdd.size() != 0) {

				eventBase = queueToAdd.remove(queueToAdd.size() - 1);
			}
		}

		if(eventBase == null)return;

		if(eventBase instanceof EventSender){
			flow.add((EventSender) eventBase);
		}

		if(eventBase instanceof EventReceiver){
			flow.add((EventReceiver) eventBase);
		}
	}

}
