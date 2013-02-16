

package com.ramshteks.nimble.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.LinkedList;

public final class Connection {

	private int cid;


	private Socket socket;

	private long pingTimeOut;

	private long disconnectTimeout;

	private long pingLastTime;

	private long disconnectLastTime;

	private Core.ILogger logger;

	private Core.IConnectionEvent connectionEvent;

	private InputStream inputStream;

	private OutputStream outputStream;

	private LinkedList<Core.IPacket> packetsToSend;

	private LinkedList<Core.IPacket> packetsToLogic;

	private boolean isClient = false;

	public Connection(int cid, Socket socket, Core.ICoreData coreData, Core.ILogger logger, boolean isClient) throws IOException {
		this.isClient = isClient;

		packetsToSend = new LinkedList<Core.IPacket>();
		packetsToLogic = new LinkedList<Core.IPacket>();

		pingTimeOut = coreData.pingTimeout();
		disconnectTimeout = coreData.timeout();

		pingLastTime = getCurrentTime();
		disconnectLastTime = getCurrentTime();

		this.logger = logger;
		this.socket = socket;
		this.cid = cid;

		inputStream = socket.getInputStream();
		outputStream = socket.getOutputStream();
	}

	public void check() {

		try{
			reading();
		}catch (Exception e){
			logger.log("Reading fail ", e, Core.LoggerLevel.CRITICAL);
		}

		if (packetsToLogic.size() != 0) {
			//logger.log("dispatch message");
			dispatchPacket(packetsToLogic.poll());
		}

		if (checkForDisconnectTimeout()) {
			//logger.log("TIMEOUT cid="+cid);
			connectionEvent.onConnectionClosed(cid);
			return;
		}

		if (checkForPingPongTimeout()) {
			sendPacket(generateInternalCommandPacket(Core.InternalCommands.PING));
			pingLastTime = getCurrentTime();
		}

		if (packetsToSend.size() > 0) {
			writing();
		}
	}

	public void sendPacket(Core.IPacket packet) {
		if (packet == null || packet.bytes() == null) {
			logger.log("TRYING SEND NULL PACKET", Core.LoggerLevel.CRITICAL);
		}

		packetsToSend.add(packet);
	}

	public void setConnectionEvent(Core.IConnectionEvent connectionEvent) {
		this.connectionEvent = connectionEvent;
	}

	public void close() {

		try {
			outputStream.close();
		} catch (IOException exp) {
			logger.log("Connection:close: outputStream ", exp, Core.LoggerLevel.WARNING);
		}

		try {
			inputStream.close();
		} catch (IOException exp) {
			logger.log("Connection:close: inputStream ", exp, Core.LoggerLevel.WARNING);
		}

		try {
			socket.close();
		} catch (IOException exp) {
			logger.log("Connection:close: socket ", exp, Core.LoggerLevel.WARNING);
		}

		connectionEvent = null;
		outputStream = null;
		inputStream = null;
		socket = null;

		packetsToSend.clear();
	}

	private void reading() {
		int available;
		try {
			if ((available = inputStream.available()) < 2) {
				return;
			}
		} catch (IOException ioException) {
			logger.log("Connection.reading()>available block", ioException, Core.LoggerLevel.CRITICAL);
			return;
		}


		byte[] raw_input = new byte[available];

		int read;
		try {
			read = inputStream.read(raw_input);
		} catch (IOException ioException) {
			logger.log("Connection.reading()>reading block", ioException, Core.LoggerLevel.CRITICAL);
			return;
		}

		pingLastTime = getCurrentTime();
		disconnectLastTime = getCurrentTime();

		ByteBuffer buffer = ByteBuffer.wrap(raw_input);

		do {
			short packet_len = buffer.getShort();
			if (packet_len + buffer.position() > read) {
				break;
			}

			byte[] raw_packet = new byte[packet_len];
			buffer.get(raw_packet, 0, packet_len);

			Core.IPacket packet = createPacket(raw_packet);

			if (computePacket(packet)) {
				packetsToLogic.add(packet);
			}

			if (read - buffer.position() < 2) {
				break;
			}
		} while (true);
	}

	private void writing() {
		if (packetsToSend.size() == 0) return;

		Core.IPacket packet = packetsToSend.poll();
		byte[] raw_packet = packet.bytes();
		short len = (short) raw_packet.length;

		ByteBuffer buffer = ByteBuffer.allocate(2 + len);
		buffer.putShort(len);
		buffer.put(raw_packet);

		byte[] complete_packet = buffer.array();
		buffer.clear();

		try {
			outputStream.write(complete_packet);
			outputStream.flush();
		} catch (IOException e) {
			if (connectionEvent != null) {
				connectionEvent.onConnectionClosed(cid);
			}
		}
	}

	private boolean computePacket(Core.IPacket packet) {

		byte[] b = packet.bytes();
		ByteBuffer bb = ByteBuffer.wrap(b);
		short command = bb.getShort();

		if (command < 0) {
			switch (command) {
				case Core.InternalCommands.PING:
					sendPacket(generateInternalCommandPacket(Core.InternalCommands.PONG));
					break;
				default:
					break;
			}
		  return false;
		}

		return true;
	}

	private Core.IPacket generateInternalCommandPacket(short command) {
		return new Core.InternalCommandPacket(command);
	}

	private Core.IPacket createPacket(byte[] bytes) {
		return new Core.RawPacket(bytes);
	}


	private boolean checkForPingPongTimeout() {
		return (getCurrentTime() - pingLastTime > pingTimeOut);
	}

	private boolean checkForDisconnectTimeout() {
		return (getCurrentTime() - disconnectLastTime > disconnectTimeout);
	}

	private long getCurrentTime() {
		return System.currentTimeMillis();
	}

	private void dispatchPacket(Core.IPacket packet) {
		if (connectionEvent != null) {
			//logger.log("Connection: "+cid);
			connectionEvent.onConnectionData(cid, packet);
		}
	}

	private void dispatchError(SDError error) {
		if (connectionEvent != null) {
			connectionEvent.onConnectionError(cid, error);
		}
	}

	private void dispatchRawData(byte[] bytes) {
		if (connectionEvent != null) connectionEvent.onRawDataReceived(cid, bytes);
	}
}
