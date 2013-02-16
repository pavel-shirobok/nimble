package com.ramshteks.nimble.core;

import java.net.Socket;

public class ConnectionReader implements Runnable{

	private Core.ILogger log;
	private Core.IConnectionEvent listener;
	private Connection connection;
	private Socket socket;
	private Core.ICoreData coreData;
	private boolean close_req = false;

	public ConnectionReader(Socket socket, Core.ICoreData coreData, Core.ILogger logger, ClientCore.ClientLogicListener listener) throws Exception{
		//this.listener = listener;
		this.log = logger;
		this.coreData = coreData;
		this.socket = socket;
		//log.log("listener"+(listener==null));
		/*try{
			connection = new Connection(0, socket, coreData, log, true);
			connection.setConnectionEvent(listener);
		}catch (Exception conn_ex){
			log.log("Connection create", conn_ex);
			listener.onConnectionError(0, SDError.ACCEPT_IO);
			throw conn_ex;
		}*/

	}



	public void run() {


		while (socket.isConnected()){
			if(close_req){
				close_req = false;
				synchronized (connection){
					connection.close();
				}

				break;
			}

			synchronized (connection){
				connection.check();
				//System.out.println(">");
			}



			try{
				Thread.sleep(1);
			}
			catch (Exception thr_ex){

			}
		}

		listener.onConnectionClosed(0);
	}

	public void send(Core.IPacket packet){
		synchronized (connection){
			connection.sendPacket(packet);
		}
	}

	public void close(){
		close_req = true;
	}

}
