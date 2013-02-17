package com.ramshteks.nimble.server;

import com.ramshteks.nimble.core.EventIO;

import java.io.IOException;
import java.net.InetAddress;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public abstract class Receptor implements EventIO.EventFull {

	public abstract void startBinding(InetAddress bindAddress, int port) throws IOException;
	public abstract void stopBinding() throws IOException;
	public abstract boolean bindStarted();

}
