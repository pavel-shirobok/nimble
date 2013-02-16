/*
 * Copyright (C) 2011 Shirobok Pavel
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License,
 * or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation,
 * Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 * Contact information:
 * email: ramshteks@gmail.com
 *//*


package com.ramshteks.nimble.core;

import java.net.InetAddress;
import java.net.Socket;


public class Core {

	*/
/**
	 * Internal commands kit
	 *//*

	public static class InternalCommands {
		*/
/**
		 * Ping command code
		 *//*

		public static final short PING = -1;
		public static final short PONG = -2;
	}

	*/
/**
	 * Interface of abstract packet
	 *//*

	public static interface IPacket {
		*/
/**
		 * Byte's array with packet's content
		 *
		 * @return byte array
		 *//*

		byte[] bytes();
	}

	*/
/**
	 * Interface of abstract business logic
	 *//*

	public static interface ILogic {

		*/
/**
		 * Install instance of <code>Core.IAgent</code>
		 *
		 * @param agent instance of <code>Core.IAgent</code>
		 * @throws IllegalAccessError throws when instance is already installed
		 *//*

		void setAgent(IAgent agent) throws IllegalAccessError;

		*/
/**
		 * Event handler for core started
		 *//*

		void onCoreStart();

		*/
/**
		 * Event handler for start core's checking
		 *//*

		void onCoreStartTick();

		*/
/**
		 * Event handler for finish core's checking
		 *//*

		void onCoreEndTick();

		*/
/**
		 * Event handler for incoming connection
		 *
		 * @param cid id of connection, installed in core
		 *//*

		void onConnectionConnect(int cid);

		*/
/**
		 * Event handler of disconnect of connection
		 *
		 * @param cid id of connection, installed in core
		 *//*

		void onConnectionDisconnect(int cid);

		*/
/**
		 * Event handler for internal error in connection
		 *
		 * @param cid   id of connection, installed in core
		 * @param error instance of enum <code>SDError</code> with description of error
		 *//*

		void onConnectionError(int cid, SDError error);

		*/
/**
		 * Event handler for raw data read from input stream
		 *
		 * @param cid   id of connection
		 * @param bytes byte array
		 *//*

		void onRawData(int cid, byte[] bytes);

		*/
/**
		 * Event handler for receiving data
		 *
		 * @param cid	id of connection, installed in core
		 * @param packet instance of <code>Core.IPacket</code>
		 *//*

		void onConnectionPacketReceived(int cid, IPacket packet);

	}

	*/
/**
	 * Interface of abstract logger
	 *//*

	public static interface ILogger {
		*/
/**
		 * Log message
		 *
		 * @param message custom message
		 * @param except  instance of <code>Exception</code>
		 *//*

		void log(String message, Exception except);

		*/
/**
		 * Log message
		 *
		 * @param message custom message
		 * @param except  instance of <code>Exception</code>
		 * @param level   instance of enum <code>Core.LoggerLevel</code>
		 *//*

		void log(String message, Exception except, LoggerLevel level);

		*/
/**
		 * Log message
		 *
		 * @param message custom message
		 *//*

		void log(String message);

		*/
/**
		 * Log message
		 *
		 * @param message custom message
		 * @param level   instance of enum <code>Core.LoggerLevel</code>
		 *//*

		void log(String message, LoggerLevel level);
	}

	*/
/**
	 * Enum for logging levels
	 *//*

	public static enum LoggerLevel {
		CRITICAL("[level=CRITICAL]"), WARNING("[level=WARNING]"), INFO("");

		private String label;

		LoggerLevel(String label) {
			this.label = label;
		}

		*/
/**
		 * Label for printing
		 *
		 * @return label for printing
		 *//*

		public String label() {
			return label;
		}
	}

	*/
/**
	 * Interface for abstract core agent
	 *//*

	public static interface IAgent {
		*/
/**
		 * Logger
		 *
		 * @return instance of <code>Core.ILogger</code>
		 *//*

		ILogger logger();

		*/
/**
		 * Sending <code>Core.IPacket</code> to connection with the specified cid
		 *
		 * @param cid	cid of target connection
		 * @param packet instance <code>Core.IPacket</code>
		 *//*

		void send(int cid, IPacket packet);

		*/
/**
		 * Closing connection with the specified cid
		 *
		 * @param cid cid of target connection
		 *//*

		void close(int cid);

		boolean  exist_cid(int cid);
	}

	*/
/**
	 * Interface for Accepter event handler
	 *//*

	public static interface IAccepterEvent {
		*/
/**
		 * Event handler for new connection
		 *
		 * @param socket instance for new <code>Socket</code>
		 *//*

		void onNewSocket(Socket socket);

		*/
/**
		 * Event handler for internal accepter error
		 *
		 * @param error instance of enum <code>SDError</code>
		 *//*

		void onAccepterError(SDError error);
	}

	*/
/**
	 * Interface for connection event handler
	 *//*

	public static interface IConnectionEvent {

		*/
/**
		 * Event handler for receiving new data
		 *
		 * @param cid	id of connection
		 * @param packet data
		 *//*

		void onConnectionData(int cid, IPacket packet);

		*/
/**
		 * Event handler for internal error in connection process
		 *
		 * @param cid   id of connection
		 * @param error instance enum <code>SDError</code>
		 *//*

		void onConnectionError(int cid, SDError error);

		*/
/**
		 * Event handler for connection closing
		 *
		 * @param cid id of connection
		 *//*

		void onConnectionClosed(int cid);

		*/
/**
		 * Event handler for receiving raw data
		 *
		 * @param cid   id of connection
		 * @param bytes byte array
		 *//*

		void onRawDataReceived(int cid, byte[] bytes);
	}

	*/
/**
	 * Interface for abstract net init data
	 *//*

	public static interface INetData {
		*/
/**
		 * Port number for binding <code>SocketServer</code>
		 *
		 * @return port number
		 *//*

		int port();

		*/
/**
		 * <code>InetAddress</code> for binding <code>SocketServer</code>
		 *
		 * @return instance of <code>InetAddress</code>
		 *//*

		InetAddress address();
	}

	*/
/**
	 * Interface for abstract core init data
	 *//*

	public static interface ICoreData {
		*/
/**
		 * Timeout for sending PING command
		 *
		 * @return timeout
		 *//*

		long pingTimeout();

		*/
/**
		 * Timeout for disconnect client
		 *
		 * @return timeout
		 *//*

		long timeout();
	}

	*/
/**
	 * Interface for abstract business logic init data
	 *//*

	public static interface IBusinessLogicData {
		*/
/**
		 * Instance of <code>Core.ILogic</code>
		 *
		 * @return ILogic
		 *//*

		ILogic logic();

		*/
/**
		 * Instance of <code>Core.ILogger</code>
		 *
		 * @return ILogger
		 *//*

		ILogger logger();
	}

	*/
/**
	 * Standard implementation of <code>Core.INetData</code>
	 *//*

	public static class NetData implements INetData {
		private int port;
		private InetAddress inetAddress;

		public NetData(InetAddress inetAddress, int port) {
			this.port = port;
			this.inetAddress = inetAddress;
		}

		public int port() {
			return port;
		}

		public InetAddress address() {
			return inetAddress;
		}
	}

	*/
/**
	 * Standard implementation of <code>Core.ICoreData</code>
	 *//*

	public static class CoreData implements ICoreData {

		private long timeout;
		private long pingTimeout;

		public CoreData(long pingTimeOut, long timeOut) {
			this.pingTimeout = pingTimeOut;
			this.timeout = timeOut;
		}

		public long pingTimeout() {
			return pingTimeout;
		}

		public long timeout() {
			return timeout;
		}
	}

	*/
/**
	 * Standard implementation of <code>Core.IBusinessLogicData</code>
	 *//*

	public static class BusinessData implements IBusinessLogicData {

		private ILogic logic;
		private ILogger logger;

		public BusinessData(ILogic logic, ILogger logger) {
			this.logic = logic;
			this.logger = logger;
		}

		public ILogic logic() {
			return logic;
		}

		public ILogger logger() {
			return logger;
		}
	}

	*/
/**
	 * Implementation of <code>Core.IPacket</code> for internal commands
	 *//*

	public static class InternalCommandPacket implements IPacket {

		private byte[] bytes;

		public InternalCommandPacket(short command) {
			bytes = new byte[2];
			byte b1 = (byte) (command >> 8);
			byte b2 = (byte) (command & 0xff);
			//Short
			bytes[0] = b1;
			bytes[1] = b2;
		}

		public byte[] bytes() {
			return bytes;
		}
	}

	*/
/**
	 * Standard core's implementation of <code>Core.IPacket</code> for receiving data
	 *//*

	public static class RawPacket implements IPacket {
		private byte[] bytes;

		public RawPacket(byte[] bytes) {
			this.bytes = bytes.clone();
		}

		public byte[] bytes() {
			return bytes;
		}

	}
}
*/
