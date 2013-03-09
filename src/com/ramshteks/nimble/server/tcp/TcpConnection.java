package com.ramshteks.nimble.server.tcp;

import com.ramshteks.nimble.server.IPacketProcessor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TcpConnection {
	public static interface TcpConnectionCallback{
		void onDataSended(TcpConnectionInfo connectionInfo, byte[] bytes);
		void onDataReceived(TcpConnectionInfo connectionInfo, byte[] bytes);
		void onConnectionClosed(TcpConnectionInfo connectionInfo);
		void onError(Exception error, String message);
		void onWarning(String message);
	}

	private TcpConnectionCallback connectionEvent;

	private Socket socket;
	private TcpConnectionInfo connectionInfo;
	private IPacketProcessor packetProcessor;
	private OutputStream outputStream;
	private InputStream inputStream;
	private long timeout;
	private long lastTime;

	public TcpConnection(Socket socket, TcpConnectionInfo connectionInfo, IPacketProcessor packetProcessor, int timeout) throws IOException{

		this.socket = socket;
		this.connectionInfo = connectionInfo;
		this.packetProcessor = packetProcessor;
		this.timeout = timeout;

		inputStream = socket.getInputStream();
		outputStream = socket.getOutputStream();

		resetTimeout();
	}

	public void setConnectionEvent(TcpConnectionCallback connectionEvent){
		this.connectionEvent = connectionEvent;
	}

	public void send(byte[] bytes){
		packetProcessor.processBytesToSocket(connectionInfo(), bytes);
	}

	public void doCycle(){
		if(isTimeout()){
			dispatchDisconnect();
			return;
		}

		tryReadFromStream();
		byte[] writtenBytes = tryWriteToSocket();

		if(packetProcessor.hasReceivedPacket()){
			if(connectionEvent != null){
				connectionEvent.onDataReceived(connectionInfo(), packetProcessor.nextReceivedPacket());
			}
		}

		if(writtenBytes!=null){
			if(connectionEvent!=null){
				connectionEvent.onDataSended(connectionInfo(), writtenBytes);
			}
		}

	}

	private byte[] tryWriteToSocket() {
		if(packetProcessor.hasSendingPacket()){
			byte[] bytes = packetProcessor.nextPacketForSend();
			flushToSocket(bytes);
			return bytes;
		}
		return null;
	}

	private void dispatchDisconnect(){
		if(connectionEvent!=null){
			connectionEvent.onConnectionClosed(connectionInfo());
		}
	}

	private boolean isTimeout(){
		long timeElapsed = System.currentTimeMillis() - lastTime;
		return  timeElapsed >= timeout;
	}

	private void resetTimeout(){
		lastTime = System.currentTimeMillis();
	}

	private boolean tryReadFromStream() {
		int available;
		try {
			if ((available = inputStream.available()) == 0) {
				return false;
			}
		} catch (IOException ioException) {
			dispatchError(ioException, "Available method failed");
			return false;
		}

		byte[] raw_input = new byte[available];

		int bytesCountReadFromStream;
		try {
			bytesCountReadFromStream = inputStream.read(raw_input);
		} catch (IOException ioException) {
			dispatchDisconnect();
			return false;
		}

		if(bytesCountReadFromStream == -1){
			dispatchDisconnect();
			return false;
		}

		resetTimeout();
		packetProcessor.processBytesFromSocket(connectionInfo, raw_input);
		return true;
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

	public void close() {

		try {
			outputStream.close();
		} catch (IOException exp) {
			dispatchWarning("Closing output stream failed");
		}

		try {
			inputStream.close();
		} catch (IOException exp) {
			dispatchWarning("Closing input stream failed");
		}

		try {
			socket.close();
		} catch (IOException exp) {
			dispatchWarning("Closing socket failed");
		}

		connectionEvent = null;
		outputStream = null;
		inputStream = null;
		socket = null;
	}

	protected boolean eventHandlerAvailable(){
		return connectionEvent!=null;
	}

	protected void dispatchError(Exception e, String message){
		if(eventHandlerAvailable()){
			connectionEvent.onError(e, message);
		}
	}

	protected void dispatchWarning(String message){
		if(eventHandlerAvailable()){
			connectionEvent.onWarning(message);
		}
	}

	public TcpConnectionInfo connectionInfo() {
		return connectionInfo;
	}

}
