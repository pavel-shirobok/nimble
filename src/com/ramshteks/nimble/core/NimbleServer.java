package com.ramshteks.nimble.core;

import com.ramshteks.nimble.event_machine.ThreadSafeEventDispatcher;

import java.util.LinkedList;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public class NimbleServer extends ThreadSafeEventDispatcher implements Runnable {

	private LinkedList<Accepter> accepterList;

	public NimbleServer(){
		accepterList = new LinkedList<Accepter>();
	}

	public void addAccepter(Accepter accepter){
		accepterList.addLast(accepter);
		accepter.addEventListener(this);
	}




	@Override
	public void run() {

	}
}
