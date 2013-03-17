package tcp_echo;

import com.ramshteks.nimble.server.PacketProcessor;
import com.ramshteks.nimble.server.IPacketProcessorFactory;
import com.ramshteks.nimble.server.tcp.TcpConnectionInfo;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)*/


public class EchoPacketProcessor extends PacketProcessor {
	public static final IPacketProcessorFactory factory = new IPacketProcessorFactory() {
		@Override
		public PacketProcessor createNewInstance(TcpConnectionInfo connectionInfo) {
			return new EchoPacketProcessor();
		}
	};

	public EchoPacketProcessor() {
		super();
	}

	@Override
	public void processBytesFromSocket(TcpConnectionInfo connectionInfo, byte[] bytes) {
		pushSendPacket(getUTFStringBytes("Hello, world"));
	}

	@Override
	public void processBytesToSocket(TcpConnectionInfo connectionInfo, byte[] bytes) {
		pushSendPacket(bytes);
	}

	public  static byte[] getUTFStringBytes(String string){
		byte[] say_bytes = null;
		try{
			say_bytes = string.getBytes("UTF-8");
		}catch (Exception e){}
		return say_bytes;
	}
}
