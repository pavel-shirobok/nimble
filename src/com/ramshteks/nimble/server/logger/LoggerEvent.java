package com.ramshteks.nimble.server.logger;

import com.ramshteks.nimble.core.Event;

public class LoggerEvent extends Event {

	public static final String LOG_MESSAGE = "LoggerEvent#LOG_MESSAGE";

	private LogLevel loglevel;
	private String message;
	private String componentName;
	private Exception exception;

	public LoggerEvent(LogLevel loglevel, String message, String componentName) {
		super(LOG_MESSAGE);
		this.loglevel = loglevel;
		this.message = message;
		this.componentName = componentName;
	}

	public LoggerEvent(LogLevel loglevel, String message, String componentName, Exception exception) {
		super(LOG_MESSAGE);
		this.loglevel = loglevel;
		this.message = message;
		this.componentName = componentName;
		this.exception = exception;
	}


	public LogLevel loglevel() {
		return loglevel;
	}

	public Exception exception() {
		return exception;
	}

	public String message() {
		return message;
	}

	public String componentName() {
		return componentName;
	}
}
