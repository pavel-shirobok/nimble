import com.ramshteks.nimble.core.*;
import com.ramshteks.nimble.server.PacketProcessor;
import com.ramshteks.nimble.server.IPacketProcessorFactory;
import com.ramshteks.nimble.server.ServerUtils;
import com.ramshteks.nimble.server.logger.StandardOutLoggerPlugin;
import com.ramshteks.nimble.server.statistic.ServerStatistics;
import com.ramshteks.nimble.server.tcp.TcpConnectionInfo;
import com.ramshteks.nimble.server.tcp_server.TcpServer;

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
			public PacketProcessor createNewInstance(TcpConnectionInfo connectionInfo) {
				return new PacketProcessor() {
					@Override
					public void processBytesFromSocket(TcpConnectionInfo connectionInfo, byte[] bytes) {
						//
					}

					@Override
					public void processBytesToSocket(TcpConnectionInfo connectionInfo, byte[] bytes) {
						//
					}
				};
			}
		};

		TcpServer tcpServer = new TcpServer(packetProcessorFactory, new ServerUtils.IDGenerator(0, 100000));

		nimble.addPlugin(tcpServer);
		nimble.addPlugin(new StandardOutLoggerPlugin());
		nimble.addPlugin(new ServerStatistics());
		nimble.start();

	}

}
