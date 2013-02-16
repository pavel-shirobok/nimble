package com.ramshteks.nimble.tcp;

import com.ramshteks.nimble.core.Event;
import com.ramshteks.nimble.core.EventIO;
import com.ramshteks.nimble.core.EventStack;
import com.ramshteks.nimble.server.IPacketProcessor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public class TcpConnection implements EventIO.EventFull {

	private Socket socket;
	private OutputStream outputStream;
	private InputStream inputStream;
	private int connection_id;

	private EventStack inputEvents;
	private EventStack outputEvents;

	public TcpConnection(Socket socket, int connection_id, IPacketProcessor packetProcessor) throws IOException{

		this.socket = socket;
		this.connection_id = connection_id;

		inputStream = socket.getInputStream();
		outputStream = socket.getOutputStream();

		inputEvents = new EventStack(new String[]{});
		outputEvents = new EventStack(new String[]{});

	}

	@Override
	public void pushEvent(Event event) {
		inputEvents.pushEvent(event);
	}

	@Override
	public boolean compatibleInput(String eventType) {
		return inputEvents.compatibleInput(eventType);
	}

	@Override
	public boolean hasEventToHandle() {
		//reading from socket

		//readFromSocket();
		//writeToSocket();

		return outputEvents.hasEventToHandle();
	}

	@Override
	public Event nextEvent() {
		return outputEvents.nextEvent();
	}
}
