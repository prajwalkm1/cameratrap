package com.md.logger;

public abstract class Logger {
	protected static final String LOG_TYPE_DEBUG = " :DEBUG: ";
	protected static final String LOG_TYPE_INFO = " :INFO: ";
	protected static final String LOG_TYPE_ERROR = " :ERROR: ";
	protected static final String LOG_TYPE_PERF = " :PERF: ";
	
	protected final String name;
	
	protected Logger(String name){
		this.name = name;
	}
	
	public abstract void info(String msg);
	
	public abstract void debug(String msg);
	
	public abstract void error(String msg);
	
	public abstract void error(String msg, Throwable e);
	
	public abstract void perfLog(String msg);

}
