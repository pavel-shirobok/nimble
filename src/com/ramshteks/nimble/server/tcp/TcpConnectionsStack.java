/*
package com.ramshteks.nimble.server.tcp;

import static com.ramshteks.nimble.server.ServerUtils.*;

import com.ramshteks.nimble.core.*;
import com.ramshteks.nimble.server.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class TcpConnectionsStack implements ITcpConnectionCallback {
	private Hashtable<Integer, TcpConnection> connections;

	private IPacketProcessorFactory packetProcessorFactory;
	private ServerUtils.IDGenerator idGenerator;
	private Nimble nimble;
	private int timeout;

	public TcpConnectionsStack(Nimble nimble, IDGenerator generator, IPacketProcessorFactory factory, int timeout) {
		this.nimble = nimble;
		this.idGenerator = generator;
		this.packetProcessorFactory = factory;
		this.timeout = timeout;

		//noinspection Convert2Diamond
		connections = new Hashtable<Integer, TcpConnection>();
	}

	public TcpConnectionInfo createConnection(Socket socket) throws IOException{
		TcpConnection connection;

		connection= createTcpConnection(socket);
		addToCommonQueue(connection);

		return connection.connectionInfo();
	}

	private int getNextId(){
		return idGenerator.nextID();
	}

	private void freeId(int id){
		idGenerator.free(id);
	}

	private TcpConnectionInfo createConnectionInfo(){
		return new TcpConnectionInfo(getNextId());
	}

	private IPacketProcessor createPacketProcessor(TcpConnectionInfo connectionInfo){
		return packetProcessorFactory.createNewInstance(connectionInfo);
	}

	private TcpConnection createTcpConnection(Socket s) throws IOException{
		TcpConnectionInfo connectionInfo;
		IPacketProcessor packetProcessor;
		TcpConnection connection;

		connectionInfo = createConnectionInfo();
		packetProcessor = createPacketProcessor(connectionInfo);
		connection = new TcpConnection(s, connectionInfo, packetProcessor, timeout);
		connection.setConnectionEvent(this);
		add(connection);


		return connection;
	}

	public void destroyConnection(TcpConnectionInfo connectionInfo){
		TcpConnection connection;

		connection = get(connectionInfo);
		freeId(connectionInfo.connection_id());
		remove(connectionInfo);
		removeFromCommonQueue(connection);
		connection.close();
	}

	@Override
	public void onConnectionClosed(TcpConnectionInfo connectionInfo) {
		destroyConnection(connectionInfo);
	}

	private void addToCommonQueue(TcpConnection connection){
		nimble.addPlugin(connection);
	}

	private void removeFromCommonQueue(TcpConnection connection){
		nimble.removePlugin(connection);
	}

	private void add(TcpConnection connection) {
		connections.put(connection.connectionInfo().connection_id(), connection);
	}

	private TcpConnection remove(TcpConnectionInfo connectionInfo){
		return remove(connectionInfo.connection_id());
	}

	private TcpConnection remove(int connection_id){
		if(exist(connection_id)){
			return connections.remove(connection_id);
		}
		return null;
	}

	private TcpConnection get(TcpConnectionInfo connectionInfo){
		return get(connectionInfo.connection_id());
	}

	private TcpConnection get(int connection_id){
		if(exist(connection_id)){
			return connections.get(connection_id);
		}
		return null;
	}

	private Boolean exist(int connection_id){
		return connections.containsKey(connection_id);
	}
}
*/
