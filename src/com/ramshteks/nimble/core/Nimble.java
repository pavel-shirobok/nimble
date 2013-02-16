package com.ramshteks.nimble.core;

import java.util.LinkedList;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public class Nimble implements Runnable {

	private LinkedList<EventIO.EventReceiver> receivers;
	private LinkedList<EventIO.EventSender> senders;

	private Thread mainThread;
	private final EventStack serverInputEventStream;

	public Nimble(){
		receivers = new LinkedList<EventIO.EventReceiver>();
		senders = new LinkedList<EventIO.EventSender>();
		serverInputEventStream = new EventStack(getAcceptableEventsType());
	}

	public void addReceiverPlugin(EventIO.EventReceiver receiver){
		receivers.addLast(receiver);
	}

	public void addSenderPlugin(EventIO.EventSender sender){
		senders.addLast(sender);
	}

	public void addFullEventPlugin(EventIO.EventFull eventFull){
		addSenderPlugin(eventFull);
		addReceiverPlugin(eventFull);
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
		//main server loop
		Event event;
		while (true){
			if(serverInputEventStream.hasEventToHandle()){
				event = serverInputEventStream.nextEvent();

				for(EventIO.EventSender sender: senders){
					if(sender.hasEventToHandle()){
						serverInputEventStream.pushEvent(sender.nextEvent());
					}
				}

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
