package com.md.motiondetector;

import java.awt.image.BufferedImage;

import com.md.camera.control.ImageCapture;
import com.md.camera.control.ImageCaptureFactory;
import com.md.camera.control.ImageCaptureFactory.NAMES;
import com.md.logger.LogFactory;
import com.md.logger.Logger;
import com.md.utils.ConfigParser;
import com.md.utils.ConfigParser.ConfigParams;
import com.md.utils.ImageCompare;
import com.md.utils.ImageIOUtils;


public class ImageMotionDetector implements MotionDetector {
	private final ImageCapture imageCapture;
	private final Logger log;
	private final ImageCompare imageCompare;
	private final boolean saveDiffImages;

	private BufferedImage prevImg;

	public ImageMotionDetector()
			throws InstantiationException, IllegalAccessException {
		this(NAMES.WebCamLaptopImageCapture);
	}
	
	public ImageMotionDetector(NAMES imageCapture)
			throws InstantiationException, IllegalAccessException {
		this.imageCapture = ImageCaptureFactory
				.createSingleImageCapture(imageCapture);
		this.log = LogFactory.getLogger(this.getClass());
		this.imageCompare = new ImageCompare();
		this.saveDiffImages = ConfigParser.getInstance().getAsBoolean(ConfigParams.save_diff_image);
	}

	public void initialize() throws Exception {
		imageCapture.connect();
		prevImg = imageCapture.capture();
	}

	public void destroy() throws Exception {
		imageCapture.disconnect();
	}

	@Override
	public boolean isMotionDetected() throws Exception {
		log.perfLog("capturing image for motion detection");
		BufferedImage nextImg = imageCapture.capture();
		log.perfLog("captured image for motion detection");
		boolean isMotionDetected = !imageCompare.isMatch(prevImg, nextImg);
		if(saveDiffImages && isMotionDetected){
			ImageIOUtils.saveImg(prevImg, "img/Diff"+System.currentTimeMillis()+"_1.jpg");
			ImageIOUtils.saveImg(nextImg, "img/Diff"+System.currentTimeMillis()+"_2.jpg");
		}
		log.perfLog("image comparison done");
		prevImg = nextImg;
		return isMotionDetected;
	}

}
