package com.ramshteks.nimble.server;

import com.ramshteks.nimble.core.EventIO;
import com.ramshteks.nimble.core.EventStack;

import java.io.IOException;
import java.net.InetAddress;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public abstract class Receptor extends EventStack implements EventIO.EventFull {

	public Receptor(String[] acceptable) {
		super(acceptable);
	}

	public abstract void startBinding(InetAddress bindAddress, int port) throws IOException;
	public abstract void stopBinding() throws IOException;
	public abstract boolean bindStarted();

}
