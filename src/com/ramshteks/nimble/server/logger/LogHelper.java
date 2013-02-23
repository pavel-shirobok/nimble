package com.ramshteks.nimble.server.logger;

import com.ramshteks.nimble.core.EventIO;
import static com.ramshteks.nimble.server.logger.LogLevel.*;
/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public class LogHelper {

	private EventIO.EventReceiver receiver;
	private String componentName;

	public LogHelper(EventIO.EventReceiver receiver, String componentName){
		this.receiver = receiver;
		this.componentName = componentName;
	}

	public void logMessage(String message){
		receiver.pushEvent(createEvent(Message, message, null));
	}

	public void logException(String message, Exception e){
		receiver.pushEvent(createEvent(Exception, message, e));
	}

	public void logWarning(String message){
		receiver.pushEvent(createEvent(Warning, message, null));
	}

	public void logError(String message){
		receiver.pushEvent(createEvent(Error, message, null));
	}

	private LoggerEvent createEvent(LogLevel level, String message, Exception e){
		return new LoggerEvent(level, message, componentName, e);
	}
}
