package tcp_echo;

import com.ramshteks.nimble.core.Nimble;
import com.ramshteks.nimble.server.ServerUtils;
import com.ramshteks.nimble.server.logger.StandardOutLoggerPlugin;
import com.ramshteks.nimble.server.tcp.TcpReceptor;
import com.ramshteks.nimble.server.statistic.ServerStatistics;

import java.io.IOException;
import java.net.InetAddress;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public class TcpEchoServer {

	public static void main(String[] args){

		//common event machine
		Nimble nimble = new Nimble();

		//range of available for receptor id range
		ServerUtils.IDGenerator idGenerator = new ServerUtils.IDGenerator(0, 100000);

		//packet processor factory

		InetAddress inetAddress = null;
		try{
			inetAddress = InetAddress.getByName("localhost");
		}catch (Exception e){
			e.printStackTrace();
			return;
		}

		TcpReceptor tcpReceptor = new TcpReceptor(nimble, idGenerator, EchoPacketProcessor.factory, 10000);

		try {
			tcpReceptor.startBinding(inetAddress, 2305);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		//adding tcp receptor
		nimble.addPlugin(tcpReceptor);
		nimble.addPlugin(new StandardOutLoggerPlugin());
		//adding statistic plugin
		nimble.addPlugin(new ServerStatistics());

		nimble.start();


	}

}
