import com.ramshteks.nimble.core.Event;
import com.ramshteks.nimble.core.EventIO;
import com.ramshteks.nimble.core.Nimble;
import com.ramshteks.nimble.server.IPacketProcessor;
import com.ramshteks.nimble.server.IPacketProcessorFactory;
import com.ramshteks.nimble.server.ServerUtils;
import com.ramshteks.nimble.tcp.TcpConnectionsStack;
import com.ramshteks.nimble.tcp.TcpReceptor;

import java.net.InetAddress;
import java.util.Iterator;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public class Main {
	public static void main(String[] args) {
		System.out.print("Hello world");

		Nimble nimble = new Nimble();
		TcpReceptor tcpReceptor = new TcpReceptor(new TcpConnectionsStack(nimble, new ServerUtils.IDRange(0, 100000), null));

		try {
			tcpReceptor.startBinding(InetAddress.getLocalHost(), 4000);
		} catch (Exception e) {
		}

		nimble.addFullEventPlugin(tcpReceptor);

		nimble.start();


	}

	public static class TestPlugin implements EventIO.EventFull{
		private int counter = 0;
		private long last = System.currentTimeMillis();
		private String s;

		public TestPlugin(String s) {

			this.s = s;
		}

		@Override
		public void pushEvent(Event event) {
			if("Test".equals(event.eventType())){
				//if(counter % 1000 == 0){
					System.out.println(s + " " + (System.currentTimeMillis() - last));
					last = System.currentTimeMillis();
					counter= 0;
				//}
				counter++;
			}
		}

		@Override
		public boolean compatibleInput(String eventType) {
			return true;
		}

		@Override
		public boolean hasEventToHandle() {
			return true;
		}
		private Event event = new Event("Test");
		@Override
		public Event nextEvent() {
			return event;
		}
	}

}
