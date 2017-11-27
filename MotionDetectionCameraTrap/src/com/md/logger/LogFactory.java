package com.md.logger;

import java.lang.reflect.InvocationTargetException;

import com.md.utils.ConfigParser;
import com.md.utils.ConfigParser.ConfigParams;

public class LogFactory {
	
	private static enum LOGGERS {
		consoleLogger(ConsoleLogger.class),
		fileLogger(FileLogger.class);

		private final Class<? extends Logger> loggerClass;

		private LOGGERS(Class<? extends Logger> loggerClass) {
			this.loggerClass = loggerClass;
		}

		public Class<? extends Logger> getLoggerClass() {
			return loggerClass;
		}

	}

	private LogFactory() {
	}
	
	public static Logger getLogger(String name){
		try {
			LOGGERS loggerToUse = LOGGERS.valueOf(ConfigParser.getInstance().getAsString(ConfigParams.logger));
			return loggerToUse.getLoggerClass().getConstructor(name.getClass()).newInstance(name);
		} catch (InstantiationException|IllegalAccessException|IllegalArgumentException|InvocationTargetException|NoSuchMethodException|SecurityException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static Logger getLogger(Class loggingClass){
		return getLogger(loggingClass.getName());
	}
}
