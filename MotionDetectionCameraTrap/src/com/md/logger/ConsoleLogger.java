package com.md.logger;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.md.utils.ConfigParser;
import com.md.utils.ConfigParser.ConfigParams;

public class ConsoleLogger extends Logger{
	private final SimpleDateFormat dateFormatter;
	
	public ConsoleLogger(String name) {
		super(name);
		dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss.SSS a");
	}

	@Override
	public void info(String msg) {
		System.out.println(getLogMessage(msg, LOG_TYPE_INFO));
		
	}

	private String getLogMessage(String msg, String logType) {
		return dateFormatter.format(new Date())+" : "+Thread.currentThread().getName()+logType+this.name+" : "+msg;
	}

	@Override
	public void debug(String msg) {
		if (ConfigParser.getInstance().getAsBoolean(ConfigParams.enable_debug_log)){
			System.out.println(getLogMessage(msg, LOG_TYPE_DEBUG));
		}
	}

	@Override
	public void error(String msg) {
		System.out.println(getLogMessage(msg, LOG_TYPE_ERROR));
	}

	@Override
	public void error(String msg, Throwable e) {
		System.out.println(getLogMessage(msg, LOG_TYPE_ERROR)+" : "+e.getMessage());
		e.printStackTrace();
		
	}

	@Override
	public void perfLog(String msg) {
		if (ConfigParser.getInstance().getAsBoolean(ConfigParams.enable_perf_log)){
			System.out.println(System.currentTimeMillis()+":"+getLogMessage(msg, LOG_TYPE_PERF));
		}
		
	}
	
}