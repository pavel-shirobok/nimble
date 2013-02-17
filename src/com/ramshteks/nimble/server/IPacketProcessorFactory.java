package com.ramshteks.nimble.server;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public interface IPacketProcessorFactory {
	IPacketProcessor createNewInstance();
}
