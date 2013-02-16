package com.ramshteks.nimble;


import com.ramshteks.nimble.core.ClientCore;
import com.ramshteks.nimble.core.Connection;
import com.ramshteks.nimble.core.Core;
import com.ramshteks.nimble.core.SDError;

import java.net.Socket;

public class SupaDupaClient implements Runnable{
	private ClientCore.ClientListener clientListener;
	private ClientCore.ClientLogicListener clientLogic;

	private Core.ILogger logger;
	private Core.ICoreData coreData;
	private Core.INetData netData;
	private boolean close_req = false;
	private Thread thread;

	public void setLogic(ClientCore.ClientLogicListener clientLogic) {
		this.clientLogic = clientLogic;
	}

	public void setClientListener(ClientCore.ClientListener clientListener) {
		this.clientListener = clientListener;
	}



	public void connect(Core.INetData netData, Core.ICoreData coreData, Core.ILogger logger) throws Exception{

		this.netData = netData;
		this.coreData = coreData;
		this.logger = logger;

		thread = new Thread(this);
		thread.start();

	}

	public void run() {
		//boolean work_flag = true;

		Core.IConnectionEvent connectionEvent = new Core.IConnectionEvent() {
			public void onConnectionData(int cid, Core.IPacket packet) {
				synchronized (clientLogic){
					clientLogic.onPacketReceived(packet);
				}
			}

			public void onConnectionError(int cid, SDError error) {
				synchronized (clientListener){
					clientListener.onIOError();
				}
			}

			public void onConnectionClosed(int cid) {
				//work_flag = false;
				synchronized (clientLogic){
					clientLogic.onStopLogic();
				}
				synchronized (clientListener){
					clientListener.onClose();
				}
				close();
			}

			public void onRawDataReceived(int cid, byte[] bytes) {

			}
		};


		Socket socket;
		final Connection connection;
		try{
			socket = new Socket(netData.address(), netData.port());
		}catch (Exception e){
			logger.log("Creation socket failed", e);
			dispatchIOError();
			return;
		}

		try{
			connection = new Connection(0, socket, coreData, logger, true);
			connection.setConnectionEvent(connectionEvent);
		}catch (Exception conn_ex){
			logger.log("Creation connection failed: ", conn_ex);
			//dispatchIOError();
			return;
		}

		clientLogic.initLogic(new ClientCore.IClientAgent() {
			public void send(Core.IPacket packet) {
				synchronized (connection){
					connection.sendPacket(packet);
				}
			}
		}, logger);

		synchronized (clientListener){
			clientListener.onConnect();
		}

		synchronized (clientLogic){
			clientLogic.onStartLogic();
		}


		while (true){


			connection.check();
			synchronized (this){
				if(close_req){
					close_req = false;
					connection.close();
					break;
				}
			}

			try{
				Thread.sleep(10);
			}catch (Exception thr_e){}
		}
	}

	public void close(){
		synchronized (this){
			close_req = true;
		}
	}

	public void destroy(){
		clientListener = null;
		thread.destroy();
	}

	private void dispatchIOError() {
		synchronized (clientListener){
			clientListener.onIOError();
		}
	}
}
