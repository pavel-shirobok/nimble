package com.ramshteks.nimble.server.statistic;

import com.ramshteks.nimble.core.Event;
import com.ramshteks.nimble.core.EventIO;
import com.ramshteks.nimble.server.tcp.events.TcpConnectionEvent;
import com.ramshteks.nimble.server.tcp.events.TcpPacketEvent;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public class ServerStatistics implements EventIO.EventReceiver {
	private int newConnectionCount = 0;
	private int disconnectedCount = 0;

	private int sendedPackets = 0;
	private int receivedPackets = 0;

	private int totalSendedPackets = 0;
	private int totalReceivedPackets = 0;
	private int totalConnectionsCount = 0;
	private long lastLoopTime = System.currentTimeMillis();

	@Override
	public void pushEvent(Event event) {
		switch (event.eventType()){
			case Event.LOOP_START:
				long newTime = System.currentTimeMillis();
				if(newTime - lastLoopTime > 1000){
					printStatistics();
					clear();
					lastLoopTime = newTime;
				}
				break;

			case Event.LOOP_END:

				break;

			case TcpPacketEvent.TCP_PACKET_SEND:
				sendedPackets++;
				totalSendedPackets++;
				break;

			case TcpPacketEvent.TCP_PACKET_RECV:
				receivedPackets++;
				totalReceivedPackets++;
				break;

			case TcpConnectionEvent.CONNECT:
				newConnectionCount++;
				totalConnectionsCount++;
				break;

			case TcpConnectionEvent.DISCONNECT:
				disconnectedCount++;
				break;
		}
	}

	private void clear() {
		receivedPackets = 0;
		sendedPackets = 0;
		newConnectionCount = 0;
		disconnectedCount = 0;
	}

	private void printStatistics() {
		System.out.println("For current second: ");
		System.out.println("*         connected = " + newConnectionCount);
		System.out.println("*      disconnected = " + disconnectedCount);
		System.out.println("*          received = " + receivedPackets);
		System.out.println("*            sended = " + sendedPackets);
		System.out.println("* total connections = " + totalConnectionsCount);
		System.out.println();

	}
}
