package com.md.logger;

import org.slf4j.LoggerFactory;

import com.md.utils.ConfigParser;
import com.md.utils.ConfigParser.ConfigParams;

public class FileLogger extends Logger {
	private final org.slf4j.Logger log;

	public FileLogger(String name) {
		super(name);
		log = LoggerFactory.getLogger(name);
	}

	@Override
	public void info(String msg) {
		log.info(msg);

	}

	@Override
	public void debug(String msg) {
		if (ConfigParser.getInstance().getAsBoolean(ConfigParams.enable_debug_log)){
			log.debug(msg);
		}

	}

	@Override
	public void error(String msg) {
		log.error(msg);

	}

	@Override
	public void error(String msg, Throwable e) {
		log.error(msg, e);

	}

	@Override
	public void perfLog(String msg) {
		if (ConfigParser.getInstance().getAsBoolean(ConfigParams.enable_perf_log)){
			log.debug(LOG_TYPE_PERF+msg);
		}

	}

}
