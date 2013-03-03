package com.ramshteks.nimble.server.tcp;

import com.ramshteks.nimble.core.*;
import com.ramshteks.nimble.server.IPacketProcessor;
import com.ramshteks.nimble.server.logger.LogHelper;
import com.ramshteks.nimble.server.tcp.events.RawTcpPacketEvent;
import com.ramshteks.nimble.server.tcp.events.TcpConnectionEvent;
import com.ramshteks.nimble.server.tcp.events.TcpPacketEvent;

import java.io.*;
import java.net.Socket;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public class TcpConnection implements EventIO.EventFull {

	private ITcpConnectionCallback connectionEvent;

	private enum IOAction {READ, WRITE}
	private Socket socket;
	private TcpConnectionInfo connectionInfo;
	private IPacketProcessor packetProcessor;
	private OutputStream outputStream;
	private InputStream inputStream;
	private LogHelper logger;
	private EventStack outputEvents;
	private long timeout;
	private long lastTime;

	public TcpConnection(Socket socket, TcpConnectionInfo connectionInfo, IPacketProcessor packetProcessor, int timeout) throws IOException{

		this.socket = socket;
		this.connectionInfo = connectionInfo;
		this.packetProcessor = packetProcessor;
		this.timeout = timeout;

		inputStream = socket.getInputStream();
		outputStream = socket.getOutputStream();

		outputEvents = new EventStack();
		logger = new LogHelper(outputEvents, "TcpConnection$"+connectionInfo().toString());
		resetTimeout();
	}

	public void setConnectionEvent(ITcpConnectionCallback connectionEvent){
		this.connectionEvent = connectionEvent;
	}

	@Override
	public void pushEvent(Event event) {

		if(Event.equalHash(event, NimbleEvent.ENTER_IN_QUEUE)){
			resetTimeout();
		}

		if(Event.equalHash(event, NimbleEvent.LOOP_START)){
			if(isTimeout()){
				dispatchDisconnect();
				return;
			}

			readFromStream(inputStream, packetProcessor);
			//read events for receiving packet
			checkAndIO(packetProcessor.fromSocket(), IOAction.READ);
			//read events for sending in socket
			checkAndIO(packetProcessor.toSocket(), IOAction.WRITE);
		}

		if(Event.equalHash(event, TcpPacketEvent.TCP_PACKET_SEND)){
			if(event instanceof TcpPacketEvent){

				TcpPacketEvent packetEvent = ((TcpPacketEvent)event);

				if(isSelfConnectionInfo(packetEvent.target())){
					//adding bytes to packet processor for send-packing
					addBytesToSend(packetEvent.bytes());
				}
			}
		}
	}

	private Boolean isSelfConnectionInfo(TcpConnectionInfo ci){
		return ci.connection_id() == connectionInfo().connection_id();
	}

	private void dispatchDisconnect(){
		if(connectionEvent!=null){
			connectionEvent.onConnectionClosed(connectionInfo());
			outputEvents.pushEvent(TcpConnectionEvent.createDisconnect(connectionInfo));
		}
	}

	private boolean isTimeout(){
		long timeElapsed = System.currentTimeMillis() - lastTime;
		return  timeElapsed >= timeout;
	}

	private void resetTimeout(){
		lastTime = System.currentTimeMillis();
	}

	private void readFromStream(InputStream stream, IPacketProcessor processor) {
		int available;
		try {
			if ((available = stream.available()) == 0) {
				return;
			}
		} catch (IOException ioException) {
			logger.logException("Available method failed", ioException);
			return;
		}

		//long time = System.currentTimeMillis();
		byte[] raw_input = new byte[available];

		int bytesCountReadFromStream;
		try {
			bytesCountReadFromStream = stream.read(raw_input);
		} catch (IOException ioException) {
			dispatchDisconnect();
			return;
		}

		if(bytesCountReadFromStream == -1){
			dispatchDisconnect();
			return;
		}

		resetTimeout();
		processor.addToProcessFromSocket(connectionInfo, raw_input);
		//System.out.println("Read: "+(System.currentTimeMillis() - time));
	}

	private void addBytesToSend(byte[] bytes) {
		packetProcessor.addToProcessToSocket(connectionInfo, bytes);
	}

	private void checkAndIO(EventIO.EventSender eventProvider, IOAction ioAction) {
		if(eventProvider.hasEventToHandle()){
			Event event = eventProvider.nextEvent();

			switch (ioAction) {
				case READ:
					outputEvents.pushEvent(event);
					break;

				case WRITE:
					if(event instanceof RawTcpPacketEvent){
						RawTcpPacketEvent rawTcpPacketEvent = (RawTcpPacketEvent)event;
						flushToSocket(rawTcpPacketEvent.bytes());
					}else{
						logger.logError("Event from 'PacketProcessor#toSocket' has unexpected type " + event.eventType() + "'");
					}
					break;
			}
		}
	}

	private void flushToSocket(byte[] bytes) {
		try {
			outputStream.write(bytes);
			outputStream.flush();
		} catch (IOException e) {
			dispatchDisconnect();
			return;
		}
		resetTimeout();
	}

	@Override
	public boolean hasEventToHandle() {
		return outputEvents.hasEventToHandle();
	}

	@Override
	public Event nextEvent() {
		return outputEvents.nextEvent();
	}

	public void close() {

		try {
			outputStream.close();
		} catch (IOException exp) {
			logger.logWarning("Closing output stream failed");
			System.out.println("Connection:close: outputStream ");
		}

		try {
			inputStream.close();
		} catch (IOException exp) {
			logger.logWarning("Closing input stream failed");
		}

		try {
			socket.close();
		} catch (IOException exp) {
			logger.logWarning("Closing socket failed");
		}

		connectionEvent = null;
		outputStream = null;
		inputStream = null;
		socket = null;
	}

	public TcpConnectionInfo connectionInfo() {
		return connectionInfo;
	}

}
