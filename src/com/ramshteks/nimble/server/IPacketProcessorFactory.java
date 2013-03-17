package com.ramshteks.nimble.server;

import com.ramshteks.nimble.server.tcp.TcpConnectionInfo;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public interface IPacketProcessorFactory {
	PacketProcessor createNewInstance(TcpConnectionInfo connectionInfo);
}
