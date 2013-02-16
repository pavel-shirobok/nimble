package com.ramshteks.nimble.core;

import com.ramshteks.nimble.event_machine.ThreadSafeEventDispatcher;

import java.io.IOException;
import java.net.InetAddress;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public abstract class Accepter extends ThreadSafeEventDispatcher {
	public abstract void startBinding(InetAddress bindAddress, int port) throws IOException;
	public abstract void stopBinding() throws IOException;
	public abstract boolean bindStarted();
}
