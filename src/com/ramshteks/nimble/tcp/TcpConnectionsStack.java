package com.ramshteks.nimble.tcp;

import com.ramshteks.nimble.core.*;
import com.ramshteks.nimble.tcp.events.TcpConnectionEvent;

import java.util.Hashtable;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public class TcpConnectionsStack implements EventIO.EventReceiver, EventIO.EventSender {
	private Hashtable<Integer, TcpConnection> connections;


	@Override
	public void pushEvent(Event event) {
		String eventType = event.eventType();
		TcpConnectionEvent tcpConnectionEvent;

		if(equalEvent(eventType, TcpConnectionEvent.CONNECT)){

			createConnection((TcpConnectionEvent)event);

		}else if(equalEvent(eventType, TcpConnectionEvent.DISCONNECT)){

			closeConnection((TcpConnectionEvent)event);
		}
	}

	private boolean equalEvent(String actual, String expect){
		return actual.equals(expect);
	}

	private void createConnection(TcpConnectionEvent event) {
		//TODO:
	}

	private void closeConnection(TcpConnectionEvent event) {
		//TODO:
	}

	@Override
	public boolean compatibleInput(String eventType) {
		return TcpConnectionEvent.DISCONNECT.equals(eventType) || TcpConnectionEvent.CONNECT.equals(eventType);
	}

	@Override
	public boolean hasEventToHandle() {
		//TODO:
		return false;
	}

	@Override
	public Event nextEvent() {
		return null;
	}
}
