package com.ramshteks.nimble.core;

import com.ramshteks.nimble.event_machine.CoreEventType;
import com.ramshteks.nimble.event_machine.Event;
import com.ramshteks.nimble.event_machine.EventIO;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public class ConnectionsHolder implements EventIO.EventReceiver, EventIO.EventSender {



	@Override
	public void pushEvent(Event event) {

	}

	@Override
	public boolean compatibleInput(CoreEventType coreEventType) {
		return  coreEventType == CoreEventType.NewConnection ||
				coreEventType == CoreEventType.SendTCPPacket ||
				coreEventType == CoreEventType.CloseConnection;
	}

	@Override
	public boolean hasEventToHandle() {
		return false;
	}

	@Override
	public Event nextEvent() {
		return null;
	}
}
