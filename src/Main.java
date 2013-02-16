import com.ramshteks.nimble.core.Nimble;
import com.ramshteks.nimble.tcp.TcpReceptor;

import java.net.InetAddress;
import java.util.Iterator;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public class Main {
	public static void main(String[] args){
		System.out.print("Hello world");

		Nimble nimble = new Nimble();
		nimble.start();
	}

}
