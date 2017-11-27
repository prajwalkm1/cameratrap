package com.md.main;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.md.camera.control.ImageCapture;
import com.md.camera.control.ImageCaptureFactory;
import com.md.camera.control.ImageCaptureFactory.NAMES;
import com.md.logger.LogFactory;
import com.md.logger.Logger;
import com.md.motiondetector.ImageMotionDetector;
import com.md.motiondetector.MotionDetector;
import com.md.utils.ConfigParser;
import com.md.utils.ConfigParser.ConfigParams;

public class StartApp {
	private static final int CONFIG_FILE_PATH = 0;
	private static Logger log;

	public static void main(String[] userArgs) throws InstantiationException,
			IllegalAccessException, Exception {
		init(userArgs);
		ConfigParser configParser = ConfigParser.getInstance();
		CaptureTask captureTask = createMotionDetector(configParser);
		ScheduledExecutorService scheduler = Executors
				.newSingleThreadScheduledExecutor();
		try {
			captureTask.initialize();
			ScheduledFuture future = scheduler.scheduleAtFixedRate(captureTask,
					0, configParser.getAsInt(ConfigParams.detection_interval),
					TimeUnit.MILLISECONDS);
			// wait forever or until an exception occurs
			while (true) {
				//calling get just to make sure that the program exits in case of any exceptions in captureTask
				future.get();
			}
		} catch (Error | Exception e) {
			log.error("Unknown exception while running", e);
		} finally {
			scheduler.shutdown();
			captureTask.destroy();
		}
	}

	private static void init(String[] userArgs) throws IOException {
		if (userArgs.length == 1) {
			ConfigParser.instantiate(userArgs[CONFIG_FILE_PATH]);
		} else {
			ConfigParser.instantiate();
		}
		log = LogFactory.getLogger(StartApp.class);
	}

	private static CaptureTask createMotionDetector(ConfigParser configParser)
			throws InstantiationException, IllegalAccessException {
		NAMES imageRecorderName = configParser
				.getAsBoolean(ConfigParams.simulate_image_recorder) ? NAMES.imageCaptureSimulator
				: NAMES.eosCapture;
		ImageCapture imageRecorder = ImageCaptureFactory
				.createImageCapture(imageRecorderName);
		return new CaptureTask(new ImageMotionDetector(), imageRecorder);
	}

}

class CaptureTask implements Runnable {
	private final MotionDetector motionDetector;
	private final ImageCapture imageRecorder;
	private final Logger log;

	public CaptureTask(MotionDetector motionDetector, ImageCapture imageRecorder) {
		super();
		this.motionDetector = motionDetector;
		this.imageRecorder = imageRecorder;
		this.log = LogFactory.getLogger(this.getClass());
	}

	public void initialize() throws Exception {
		this.imageRecorder.connect();
		this.motionDetector.initialize();
	}

	public void destroy() throws Exception {
		this.imageRecorder.disconnect();
		this.motionDetector.destroy();
	}

	@Override
	public void run() {
		try {
			if (motionDetector.isMotionDetected()) {
				log.info("Motion detected.  Capturing ...");
				imageRecorder.captureInCamera();
			}
		} catch (Exception e) {
			log.error("Error while capturing", e);
			throw new RuntimeException();
		}
	}
}
