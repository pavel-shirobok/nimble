package com.ramshteks.nimble.server;

import com.ramshteks.nimble.core.*;
import com.ramshteks.nimble.tcp.*;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public interface IPacketProcessor {

	void addToProcessFromSocket(TcpConnectionInfo connectionInfo, byte[] bytes);
	void addToProcessToSocket(TcpConnectionInfo connectionInfo, byte[] bytes);

	EventIO.EventSender toSocket();
	EventIO.EventSender fromSocket();
}

