package com.md.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConfigParser {

	public static enum ConfigParams {
		//image capture
		burst_enable("enable.burst.mode", "false"),
		burst_duration("burst.duration.in.seconds","3"),
		multiple_image_count("multiple.image.capture.count", "5"),
		interval_between_multiple_images("interval.between.multiple.images.in.milliseconds","500"),
		simulate_image_recorder("enable.image.recorder.simulation","false"),
		
		//motion detection
		detection_interval("motion.detection.interval.in.millisecond", "100"),
		save_diff_image("motion.detection.save.diff.image", "false"),
		
		//image comparison
		image_comparison_threshold("image.diff.threshold", "5"),
		image_comparison_horizontal_blocks("image.diff.horizontal.blocks","8"),
		image_comparison_vertical_blocks("image.diff.vertical.blocks","6"),
		
		//Logging
		enable_debug_log("enable.debug.log", "false"),
		enable_perf_log("enable.perf.log", "false"),
		logger("logger", "fileLogger");

		private final String paramName;
		private final String defaultValue;

		private ConfigParams(String paramName, String defaultValue) {
			this.paramName = paramName;
			this.defaultValue = defaultValue;
		}

		public String getParamName() {
			return paramName;
		}

		public String getDefaultValue() {
			return defaultValue;
		}
	}
	
	private static final String CONFIG_FILE_PATH="config/config.ini";

	private final Map<String, String> configContentsMap;
	
	private static ConfigParser INSTANCE=null;

	private ConfigParser() throws IOException {
		this(CONFIG_FILE_PATH);
	}
	
	private ConfigParser(String configPath) throws IOException {
		super();
		this.configContentsMap = parse(configPath);
	}

	private Map<String, String> parse(String configPath) throws IOException {
		Map<String, String> paramsMap = new HashMap<String, String>();
		BufferedReader bufferedReader=null;
		try {
			bufferedReader = new BufferedReader(new FileReader(configPath));
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				line = line.trim();
				if (isValid(line)){
					String[] param = line.split("=");
					paramsMap.put(param[0], param[1]);
				}
			}
		}finally{
			bufferedReader.close();
		}
		return paramsMap;
	}

	private boolean isValid(String line) {
		if (line.startsWith("#")){
			return false;
		}else if(line.isEmpty()){
			return false;
		}else if(line.split("=").length != 2){
			System.out.println("Skipping invalid line "+line);
			return false;
		}
		return true;
	}

	public int getAsInt(ConfigParams param) {
		return Integer.parseInt(getParamValue(param));
	}

	private String getParamValue(ConfigParams param) {
		return configContentsMap.getOrDefault(param.getParamName(), param.getDefaultValue());
	}

	public boolean getAsBoolean(ConfigParams param) {
		return Boolean.parseBoolean(getParamValue(param));
	}

	public String getAsString(ConfigParams param) {
		return getParamValue(param);
	}
	
	public synchronized static ConfigParser getInstance(){
		if (INSTANCE==null){
			throw new RuntimeException(new InstantiationException("Not instantiated.  Call 'instantiate' before getting instance"));
		}
		return INSTANCE;
	}
	
	public synchronized static void instantiate(String configPath) throws IOException{
		if (INSTANCE==null){
			INSTANCE = new ConfigParser(configPath);
		}else{
			throw new RuntimeException(new InstantiationException("Already instantiated"));
		}
	}
	
	public synchronized static void instantiate() throws IOException{
		if (INSTANCE==null){
			INSTANCE = new ConfigParser();
		}else{
			throw new RuntimeException(new InstantiationException("Already instantiated"));
		}
	}

}
