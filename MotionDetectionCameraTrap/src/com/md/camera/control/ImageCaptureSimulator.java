package com.md.camera.control;

import com.md.logger.LogFactory;
import com.md.logger.Logger;

public class ImageCaptureSimulator extends ImageCapture {
	private final Logger log;
	
	public ImageCaptureSimulator() {
		super();
		this.log = LogFactory.getLogger(this.getClass());
	}
	
	@Override
	public void connect() throws Exception {
		log.info("Connected");

	}

	@Override
	public void disconnect() throws Exception {
		log.info("disconnected");

	}
	
	public void captureInCamera() throws Exception{
		log.info("Image captured");
	}

}
