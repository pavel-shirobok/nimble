package com.ramshteks.nimble.tcp;

import com.ramshteks.nimble.core.CoreEventType;
import com.ramshteks.nimble.core.Event;
import com.ramshteks.nimble.core.EventIO;

/**
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public class TcpConnection implements EventIO.EventSender, EventIO.EventReceiver {



	@Override
	public void pushEvent(Event event) {
	}

	@Override
	public boolean compatibleInput(CoreEventType coreEventType) {
		return false;
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
