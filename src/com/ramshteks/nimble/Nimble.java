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
