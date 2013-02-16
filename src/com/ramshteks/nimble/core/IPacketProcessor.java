package com.ramshteks.nimble.core;

import com.ramshteks.nimble.event_machine.*;
import static com.ramshteks.nimble.core.Nimble.*;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public interface IPacketProcessor extends EventDispatcher {

	void addToPacking(RawPacket packet);
	void addToUnpacking(byte[] rawData);

	boolean doPacking();
	boolean doUnpacking();

}
