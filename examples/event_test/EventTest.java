/*
package event_test;

import com.ramshteks.nimble.core.Event;
import com.ramshteks.nimble.core.EventIO;
import com.ramshteks.nimble.core.Nimble;
import com.ramshteks.nimble.core.NimbleEvent;

import static com.ramshteks.nimble.core.EventIO.EventFull;
import static com.ramshteks.nimble.core.EventIO.EventSender;

*/
/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 *//*

public class EventTest {
	public static void main(String[] args){
		Nimble nimble = new Nimble();
		nimble.addPlugin(new AddingPlugin(nimble));
		nimble.addPlugin(new TestStatistics(nimble));
		nimble.start();
	}

	public static class AddingPlugin implements EventFull {
		private long lastTime = System.currentTimeMillis();
		private Nimble nimble;
		private int pluginCount = 10;
		public AddingPlugin(Nimble nimble) {
			this.nimble = nimble;
		}

		@Override
		public void pushEvent(Event event) {
			if(Event.equalHash(event, NimbleEvent.LOOP_START)){
				if(pluginCount == 0)return;
				pluginCount--;
				int x  = 0 ;
				while (x < 1000){
					nimble.addPlugin(new FloodPlugin());
					x++;
				}
			}
		}

		@Override
		public boolean hasEventToHandle() {
			return false;
		}

		@Override
		public Event nextEvent() {
			return null;
		}
	}

	public static class FloodPlugin implements EventSender {

		@Override
		public boolean hasEventToHandle() {
			return true;
		}

		@Override
		public Event nextEvent() {
			return new Event("Test flood");
		}
	}

	public static class TestStatistics implements EventIO.EventReceiver{
		private int loopCount = 0;
		private long totalCount = 0;
		private long startLoopTime;
		private Nimble nimble;

		public TestStatistics(Nimble nimble) {

			this.nimble = nimble;
		}

		@Override
		public void pushEvent(Event event) {
			totalCount++;
			loopCount++;
			if(Event.equalHash(event, NimbleEvent.LOOP_START)){
				startLoopTime = System.currentTimeMillis();
				loopCount=0;
			}

			if(Event.equalHash(event, NimbleEvent.LOOP_END)){
				System.out.println("* ------------------------------");
				System.out.println("*  Plugins count: " + nimble.pluginsCount());
				System.out.println("*      Loop time: " + (System.currentTimeMillis() - startLoopTime));
				System.out.println("* Event per loop: " + loopCount);
				System.out.println("*   total events: " + totalCount);
			}
		}
	}

}
*/
