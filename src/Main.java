import com.ramshteks.nimble.core.EventIO;
import com.ramshteks.nimble.core.Nimble;
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

		TcpReceptor tcpReceptor = new TcpReceptor(nimble, idGenerator, packetProcessorFactory);

		try {
			tcpReceptor.startBinding(InetAddress.getByName("localhost"), 2305);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		nimble.addFullEventPlugin(tcpReceptor);
		nimble.addReceiverPlugin(new StandardOutLoggerPlugin());

		nimble.start();




	}

}
