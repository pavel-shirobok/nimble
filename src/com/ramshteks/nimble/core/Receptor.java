package com.ramshteks.nimble.core;

import com.ramshteks.nimble.event_machine.EventIO;

import java.io.*;
import java.net.*;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public abstract class Receptor {

	public abstract void startBinding(InetAddress bindAddress, int port) throws IOException;
	public abstract void stopBinding() throws IOException;
	public abstract boolean bindStarted();

	private EventIO.EventReceiver input;

	public final void setEventInput(EventIO.EventReceiver input){
		this.input = input;
	}

	protected final EventIO.EventReceiver eventInput(){
		return input;
	}
}
