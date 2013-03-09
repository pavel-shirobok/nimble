import com.ramshteks.jqueue.FIFOArrayQueue;
import com.ramshteks.jqueue.Queue;
import com.ramshteks.nimble.core.*;
import com.ramshteks.nimble.server.IPacketProcessor;
import com.ramshteks.nimble.server.IPacketProcessorFactory;
import com.ramshteks.nimble.server.ServerUtils;
import com.ramshteks.nimble.server.logger.StandardOutLoggerPlugin;
import com.ramshteks.nimble.server.statistic.ServerStatistics;
import com.ramshteks.nimble.server.tcp.TcpConnectionInfo;
import com.ramshteks.nimble.server.tcp.TcpReceptor;
import com.ramshteks.nimble.server.tcp_server.TcpServer;

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
					public void processBytesFromSocket(TcpConnectionInfo connectionInfo, byte[] bytes) {

					}

					@Override
					public void processBytesToSocket(TcpConnectionInfo connectionInfo, byte[] bytes) {
					}

					@Override
					public boolean hasReceivedPacket() {
						return false;
					}

					@Override
					public boolean hasSendingPacket() {
						return false;
					}

					@Override
					public byte[] nextReceivedPacket() {
						return new byte[0];
					}

					@Override
					public byte[] nextPacketForSend() {
						return new byte[0];
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
