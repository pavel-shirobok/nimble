import com.ramshteks.nimble.NimbleExt;

import java.util.Iterator;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public class Main {
	public static void main(String[] args){
		System.out.print("Hello world");

		TestIterable iterable = new TestIterable();

		for (Integer integer : iterable) {
			System.out.println(integer);
		}

		for (Integer integer : iterable) {
			System.out.println(integer);
		}

	}

	public static class TestIterable implements Iterable<Integer>, Iterator<Integer>{

		private int i = 0;

		public TestIterable() {

		}

		@Override
		public Iterator<Integer> iterator() {
			i = 0;
			return this;
		}

		@Override
		public boolean hasNext() {
			return i < 10;
		}

		@Override
		public Integer next() {
			return i++;
		}

		@Override
		public void remove() {
		}
	}

}
