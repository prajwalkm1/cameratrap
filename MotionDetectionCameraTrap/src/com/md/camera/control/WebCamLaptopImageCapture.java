package com.md.camera.control;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Comparator;

import com.github.sarxos.webcam.Webcam;
import com.md.logger.LogFactory;
import com.md.logger.Logger;

public class WebCamLaptopImageCapture extends ImageCapture {
	
	private final Webcam webcam;
	private final Logger log;
	
	public WebCamLaptopImageCapture(){
		log = LogFactory.getLogger(this.getClass());
		
		webcam = Webcam.getDefault();
		Dimension maxViewSize = getMaxViewSize(webcam.getViewSizes());
		log.debug("Max view size: "+maxViewSize.getWidth()+" * "+maxViewSize.getHeight());
		webcam.setViewSize(maxViewSize);
	}

	@Override
	public void connect() throws Exception {
		webcam.open();
	}

	@Override
	public void disconnect() throws Exception {
		webcam.close();
	}
	
	public BufferedImage capture() throws Exception{
		return webcam.getImage();
	}
	
	private Dimension getMaxViewSize(Dimension[] viewSizes) {
		Arrays.sort(viewSizes, new Comparator<Dimension>() {
			@Override
			public int compare(Dimension d1, Dimension d2) {
				if (d1.getHeight() < d2.getHeight() && d1.getWidth() < d2.getWidth()) {
					return -1;
				} else if (d1.getHeight() > d2.getHeight() && d1.getWidth() > d2.getWidth()) {
					return 1;
				}
				return 0;
			}
		});
		return viewSizes[viewSizes.length - 1];
	}

}
