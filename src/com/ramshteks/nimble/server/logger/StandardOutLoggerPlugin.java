package com.ramshteks.nimble.server.logger;

/**
 * ...
 *
 * @author Pavel Shirobok (ramshteks@gmail.com)
 */
public class StandardOutLoggerPlugin extends LoggerPlugin {

	@Override
	protected String formatLoggerEvent(LoggerEvent loggerEvent) {
		return "[" + loggerEvent.componentName() + "("+loggerEvent.loglevel().toString() + ")] " + loggerEvent.message();
	}

	@Override
	protected void write(LoggerEvent event, String formattedMessage) {
		System.out.println(formattedMessage);
	}
}
