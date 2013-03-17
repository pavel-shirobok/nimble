import com.ramshteks.nimble.core.*;
import com.ramshteks.nimble.server.PacketProcessor;
import com.ramshteks.nimble.server.IPacketProcessorFactory;
import com.ramshteks.nimble.server.ServerUtils;
import com.ramshteks.nimble.server.logger.StandardOutLoggerPlugin;
import com.ramshteks.nimble.server.statistic.ServerStatistics;
import com.ramshteks.nimble.server.tcp.TcpConnectionInfo;
import com.ramshteks.nimble.server.tcp.TcpReceptor;
import com.ramshteks.nimble.server.tcp_server.TcpServer;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public class Main {
	public static void main(String[] args) {

		Nimble nimble = new Nimble();

		IPacketProcessorFactory packetProcessorFactory = (TcpConnectionInfo connectionInfo)->
				new PacketProcessor() {
					@Override
					public void processBytesFromSocket(TcpConnectionInfo connectionInfo, byte[] bytes) {
					}

					@Override
					public void processBytesToSocket(TcpConnectionInfo connectionInfo, byte[] bytes) {
					}
				};

		InetAddress inetAddress;
		try {
			inetAddress = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return;
		}

		TcpServer tcpServer = new TcpServer(packetProcessorFactory, new ServerUtils.IDGenerator(0, 100000));
		tcpServer.addReceptor(new TcpReceptor(), inetAddress, 2305);

		nimble.addPlugin(tcpServer);
		nimble.addPlugin(new StandardOutLoggerPlugin());
		nimble.addPlugin(new ServerStatistics());
		nimble.start();

	}

}
