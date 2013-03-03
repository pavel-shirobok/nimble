import com.ramshteks.jqueue.FIFOArrayQueue;
import com.ramshteks.jqueue.Queue;
import com.ramshteks.nimble.core.*;
import com.ramshteks.nimble.server.IPacketProcessor;
import com.ramshteks.nimble.server.IPacketProcessorFactory;
import com.ramshteks.nimble.server.ServerUtils;
import com.ramshteks.nimble.server.logger.StandardOutLoggerPlugin;
import com.ramshteks.nimble.server.tcp.TcpConnectionInfo;
import com.ramshteks.nimble.server.tcp.TcpReceptor;

import java.net.InetAddress;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public class Main {
	public static void main(String[] args) {

		EventStack stack = new EventStack();

		int N = (int)1e6;



		long time = System.currentTimeMillis();
		Event event = new Event(NimbleEvent.ENTER_IN_QUEUE);
		for(int i =0; i<N; i++){

			stack.pushEvent(event);
		}
		System.out.println("Create " + N + " time:" + (System.currentTimeMillis() - time));

		time = System.currentTimeMillis();
		while(stack.hasEventToHandle()){
			stack.nextEvent();
		}
		System.out.println("Get " + N + " time:" + (System.currentTimeMillis() - time));

/*
		Nimble nimble = new Nimble();

		IPacketProcessorFactory packetProcessorFactory = new IPacketProcessorFactory() {
			@Override
			public IPacketProcessor createNewInstance(TcpConnectionInfo connectionInfo) {
				return new IPacketProcessor() {
					@Override
					public void addToProcessFromSocket(TcpConnectionInfo connectionInfo, byte[] bytes) {
					}

					@Override
					public void addToProcessToSocket(TcpConnectionInfo connectionInfo, byte[] bytes) {
					}

					@Override
					public EventIO.EventSender toSocket() {
						return null;
					}

					@Override
					public EventIO.EventSender fromSocket() {
						return null;
					}
				};
			}
		};

		ServerUtils.IDGenerator idGenerator = new ServerUtils.IDGenerator(0, 100000);

		TcpReceptor tcpReceptor = new TcpReceptor(nimble, idGenerator, packetProcessorFactory, 1000);

		try {
			tcpReceptor.startBinding(InetAddress.getByName("localhost"), 2305);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		nimble.addPlugin(tcpReceptor);
		nimble.addPlugin(new StandardOutLoggerPlugin());

		nimble.start();*/
	}

}
