package tcp_echo;

import com.ramshteks.nimble.core.Nimble;
import com.ramshteks.nimble.server.ServerUtils;
import com.ramshteks.nimble.server.logger.StandardOutLoggerPlugin;
import com.ramshteks.nimble.server.statistic.ServerStatistics;
import com.ramshteks.nimble.server.tcp_server.TcpServer;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)*/


public class TcpEchoServer {

	public static void main(String[] args){

		//common event machine
		Nimble nimble = new Nimble();

		TcpServer tcpServer = new TcpServer(EchoPacketProcessor.factory, new ServerUtils.IDGenerator(0, 100000));

		nimble.addPlugin(tcpServer);
		nimble.addPlugin(new StandardOutLoggerPlugin());
		nimble.addPlugin(new ServerStatistics());
		nimble.start();
	}

}
