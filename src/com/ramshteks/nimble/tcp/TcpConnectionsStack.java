package com.ramshteks.nimble.tcp;

import com.ramshteks.nimble.core.Nimble;
import com.ramshteks.nimble.server.ServerUtils;

import java.net.Socket;

public class TcpConnectionsStack {
	public TcpConnectionsStack(Nimble nimble, ServerUtils.IDGenerator idGenerator, Object o) {

	}

	public TcpConnectionInfo createConnection(Socket socket) {

		return null;
	}
	/*private Hashtable<Integer, TcpConnection> id2connections;
	private Nimble nimble;
	private ServerUtils.IDGenerator idRange;
	private IPacketProcessorFactory packetProcessorFactory;

	public TcpConnectionsStack(Nimble nimble, ServerUtils.IDGenerator idRange, IPacketProcessorFactory packetProcessorFactory) {
		this.nimble = nimble;
		this.idRange = idRange;
		this.packetProcessorFactory = packetProcessorFactory;
		id2connections = new Hashtable<Integer, TcpConnection>();
	}

	private boolean equalEvent(String actual, String expect){
		return actual.equals(expect);
	}*/

	/*private void createConnection(TcpConnectionEvent event) {
		//TODO:
int connection_id = idRange.nextID();
		IPacketProcessor packetProcessor = packetProcessorFactory.createNewInstance();
		TcpConnection connection;
		try{
			connection = new TcpConnection(event.socket(), connection_id, packetProcessor);
		}catch (IOException ioException){
			//SHIT must be something
			return;
		}

		//id2socket.put(connection_id, event.socket());
		id2connections.put(connection_id, connection);
		nimble.addFullEventPlugin(connection);

	}*/

	/*private void closeConnection(TcpConnectionEvent event) {
		//TODO:


		TcpConnection connection = null;
		Enumeration<TcpConnection> connectionEnumeration = id2connections.elements();
		while (connectionEnumeration.hasMoreElements()){
			connection = connectionEnumeration.nextElement();
			if(connection.socket() == event.socket()){
				break;
			}
		}

		if(connection==null){
			//SHIT error
		}else{
			id2connections.remove(connection.connection_id());
			nimble.removeFullEventPlugin(connection);
			connection.destroy();
		}

	}*/

}
