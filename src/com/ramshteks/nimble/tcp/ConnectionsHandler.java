package com.ramshteks.nimble.tcp;

import com.ramshteks.nimble.tcp.events.ConnectionEvent;
import com.ramshteks.nimble.core.CoreEventType;
import com.ramshteks.nimble.core.Event;
import com.ramshteks.nimble.core.EventIO;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public class ConnectionsHandler implements EventIO.EventReceiver, EventIO.EventSender {
	//private Hashtable<Integer, Connection> connections;


	@Override
	public void pushEvent(Event event) {
		switch (event.getCoreEventType()) {
			case CloseConnection:
				closeConnection(((ConnectionEvent)event).connection_id());
				break;

			case NewConnection:
				createConnection(((ConnectionEvent)event).connection_id());
				break;

			case SendTCPPacket:
				//TODO
				break;
			default:

				break;
		}
	}

	private void createConnection(int connection_id) {

	}

	private void closeConnection(int connection_id) {

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
