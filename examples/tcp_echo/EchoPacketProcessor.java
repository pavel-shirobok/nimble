package tcp_echo;

import com.ramshteks.nimble.core.EventIO;
import com.ramshteks.nimble.core.EventStack;
import com.ramshteks.nimble.server.IPacketProcessor;
import com.ramshteks.nimble.server.IPacketProcessorFactory;
import com.ramshteks.nimble.server.tcp.TcpConnectionInfo;
import com.ramshteks.nimble.server.tcp.events.RawTcpPacketEvent;
import com.ramshteks.nimble.server.tcp.events.TcpPacketEvent;

import java.io.ByteArrayOutputStream;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public class EchoPacketProcessor implements IPacketProcessor{
	public static final IPacketProcessorFactory factory = new IPacketProcessorFactory() {
		@Override
		public IPacketProcessor createNewInstance(TcpConnectionInfo connectionInfo) {
			return new EchoPacketProcessor();
		}
	};

	private EventStack toSocket = new EventStack();
	private EventStack fromSocket = new EventStack();

	@Override
	public void addToProcessFromSocket(TcpConnectionInfo connectionInfo, byte[] bytes) {
		fromSocket.pushEvent(new TcpPacketEvent(TcpPacketEvent.TCP_PACKET_RECV, connectionInfo, bytes));
	}


	@Override
	public void addToProcessToSocket(TcpConnectionInfo connectionInfo, byte[] bytes) {
		RawTcpPacketEvent rawTcpPacketEvent;
		rawTcpPacketEvent = new RawTcpPacketEvent(RawTcpPacketEvent.RAW_TCP_PACKET_TO_SEND, bytes);
		toSocket.pushEvent(rawTcpPacketEvent);
	}

	@Override
	public EventIO.EventSender toSocket() {
		return toSocket;
	}

	@Override
	public EventIO.EventSender fromSocket() {
		return fromSocket;
	}
}
