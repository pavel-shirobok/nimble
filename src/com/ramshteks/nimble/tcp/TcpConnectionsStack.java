package com.ramshteks.nimble.tcp;

import com.ramshteks.nimble.core.*;
import com.ramshteks.nimble.server.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class TcpConnectionsStack {
	private Nimble nimble;
	private ServerUtils.IDGenerator idGenerator;
	private IPacketProcessorFactory packetProcessorFactory;
	private Hashtable<Integer, TcpConnection> connections;

	public TcpConnectionsStack(Nimble nimble, ServerUtils.IDGenerator idGenerator, IPacketProcessorFactory packetProcessorFactory) {
		this.nimble = nimble;
		this.idGenerator = idGenerator;
		this.packetProcessorFactory = packetProcessorFactory;

		connections = new Hashtable<Integer, TcpConnection>();
	}

	public TcpConnectionInfo createConnection(Socket socket) throws IOException{

		int connection_id = idGenerator.nextID();
		TcpConnectionInfo connectionInfo = new TcpConnectionInfo(connection_id);
		IPacketProcessor packetProcessor = packetProcessorFactory.createNewInstance(connectionInfo);
		TcpConnection connection;

		connection = new TcpConnection(socket, connectionInfo, packetProcessor);
		connections.put(connection_id, connection);
		nimble.addFullEventPlugin(connection);

		return connectionInfo;
	}

	public void destroyConnection(TcpConnectionInfo connectionInfo){

		TcpConnection connection = connections.get(connectionInfo.connection_id());
		nimble.removeFullEventPlugin(connection);
		idGenerator.free(connectionInfo.connection_id());
		connections.remove(connectionInfo.connection_id());

		//Shit connection.destroy();

	}
}
