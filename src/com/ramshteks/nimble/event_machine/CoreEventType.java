package com.ramshteks.nimble.event_machine;

public enum CoreEventType{

	CloseConnection(2),
	NewConnection(2),
	ReceiveTCPPacket(1),
	SendTCPPacket(0),
	ReceiveUDPPacket(1),
	SendUDPPacket(0);

	private int _priority;

	CoreEventType(int priority){
		_priority = priority;
	}

	public int getPriority() {
		return _priority;
	}
}
