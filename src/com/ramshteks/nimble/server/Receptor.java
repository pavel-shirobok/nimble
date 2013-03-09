package com.ramshteks.nimble.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public abstract class Receptor {
	public static interface ReceptorEvent{
		void onAcceptedSocket(Socket socket);
		void onError(Exception exception, String message);
		void onWarning(String string);
	}

	private ReceptorEvent eventHandler;

	public abstract void startBinding(InetAddress bindAddress, int port) throws IOException;
	public abstract void stopBinding() throws IOException;
	public abstract boolean bindStarted();

	public void addReceptorEvent(ReceptorEvent eventHandler){
		this.eventHandler = eventHandler;
	}

	protected boolean eventHandlerAvailable(){
		return eventHandler!=null;
	}

	protected void dispatchNewSocket(Socket socket){
		if(eventHandlerAvailable()){
			eventHandler.onAcceptedSocket(socket);
		}
	}

	protected void dispatchError(Exception e, String message){
		if(eventHandlerAvailable()){
			eventHandler.onError(e, message);
		}
	}

	protected void dispatchWarning(String message){
		if(eventHandlerAvailable()){
			eventHandler.onWarning(message);
		}
	}

}
