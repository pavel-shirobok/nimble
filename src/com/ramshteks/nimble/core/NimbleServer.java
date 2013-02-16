package com.ramshteks.nimble.core;

import com.ramshteks.nimble.event_machine.*;

import java.util.LinkedList;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public class NimbleServer implements Runnable {

	private LinkedList<Receptor> receptors;
	private LinkedList<EventIO.EventReceiver> receivers;
	private LinkedList<EventIO.EventSender> senders;

	private Thread mainThread;
	private final EventStack serverInputEventStream;

	public NimbleServer(){
		receptors = new LinkedList<Receptor>();
		receivers = new LinkedList<EventIO.EventReceiver>();
		serverInputEventStream = new EventStack(getAcceptableEventsType());
	}

	public void addReceptor(Receptor receptor){
		receptors.addLast(receptor);
		receptor.setEventInput(serverInputEventStream);
	}

	public void start(){
		mainThread = new Thread(this);
		mainThread.start();
	}

	public void stop(){
		//TODO: make stop thread
	}

	@Override
	public void run() {
		while (true){
			if(serverInputEventStream.hasEventToHandle()){
				Event event = serverInputEventStream.nextEvent();
				//main server loop

				for(EventIO.EventReceiver receiver: receivers){
					if(receiver.compatibleInput(event.getCoreEventType())){
						receiver.pushEvent(event);
					}
				}

			}
		}
	}

	public CoreEventType[] getAcceptableEventsType() {
		return new CoreEventType[]{
				CoreEventType.CloseConnection,
				CoreEventType.NewConnection,
				CoreEventType.ReceiveTCPPacket,
				CoreEventType.ReceiveUDPPacket,
				CoreEventType.SendTCPPacket,
				CoreEventType.SendUDPPacket
		};
	}
}
