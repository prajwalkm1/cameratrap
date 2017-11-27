package com.md.camera.control;

import com.md.utils.ConfigParser;
import com.md.utils.ConfigParser.ConfigParams;

public class ImageCaptureFactory {

	public static enum NAMES {
		OpenCvLaptopImageCapture(OpenCvLaptopImageCapture.class), WebCamLaptopImageCapture(
				WebCamLaptopImageCapture.class), eosCapture(
				CanonEOSCapture.class), imageCaptureSimulator(
				ImageCaptureSimulator.class);

		private final Class<? extends ImageCapture> imageCaptureClass;

		private NAMES(Class<? extends ImageCapture> imageCaptureClass) {
			this.imageCaptureClass = imageCaptureClass;
		}

		public Class<? extends ImageCapture> getImageCaptureClass() {
			return imageCaptureClass;
		}
	}

	private ImageCaptureFactory() {
	}

	public static ImageCapture createImageCapture(NAMES captureName)
			throws InstantiationException, IllegalAccessException {
		return createImageCapture(captureName, ConfigParser.getInstance()
				.getAsBoolean(ConfigParams.burst_enable), ConfigParser
				.getInstance().getAsInt(ConfigParams.burst_duration),
				ConfigParser.getInstance().getAsInt(ConfigParams.multiple_image_count),
				ConfigParser.getInstance().getAsInt(ConfigParams.interval_between_multiple_images));
	}
	
	public static ImageCapture createSingleImageCapture(NAMES captureName)
			throws InstantiationException, IllegalAccessException {
		return createImageCapture(captureName, false, 0, 1, 0);
	}

	private static ImageCapture createImageCapture(NAMES captureName,
			boolean enableBurst, int burstDuration, int multipleImageCount, int multImageInterval)
			throws InstantiationException, IllegalAccessException {
		ImageCapture imageCapture = captureName.getImageCaptureClass().newInstance();
		imageCapture.setBurstEnabled(enableBurst);
		imageCapture.setBurstDuration(burstDuration);
		imageCapture.setMultImageCount(multipleImageCount);
		imageCapture.setMultImageInterval(multImageInterval);
		return imageCapture;
	}

}
