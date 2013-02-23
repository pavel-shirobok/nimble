package com.ramshteks.nimble.server.logger;

import com.ramshteks.nimble.core.Event;
import com.ramshteks.nimble.core.EventIO.EventReceiver;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public abstract class LoggerPlugin implements EventReceiver{

	@Override
	public void pushEvent(Event event) {
		if(LoggerEvent.LOG_MESSAGE.equals(event.eventType())){
			processLogEvent((LoggerEvent)event);
		}
	}

	private void processLogEvent(LoggerEvent loggerEvent) {
		String formattedMessage = formatLoggerEvent(loggerEvent);
		write(loggerEvent, formattedMessage);
	}

	protected abstract String formatLoggerEvent(LoggerEvent loggerEvent);
	protected abstract void write(LoggerEvent event, String formattedMessage);
}
