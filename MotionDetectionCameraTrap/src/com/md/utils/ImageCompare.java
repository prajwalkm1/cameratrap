package com.md.utils;

import com.md.logger.LogFactory;
import com.md.logger.Logger;
import com.md.utils.ConfigParser.ConfigParams;

import java.io.*;
import java.awt.*;
import java.awt.image.*;


 public class ImageCompare {
	private final int horizontalBlocks;
	private final int verticalBlocks;
	private final int diffThreshold;
	private final Logger log;
	
	public ImageCompare(){
		this.log = LogFactory.getLogger(this.getClass());
		this.diffThreshold = ConfigParser.getInstance().getAsInt(ConfigParams.image_comparison_threshold);
		this.horizontalBlocks = ConfigParser.getInstance().getAsInt(ConfigParams.image_comparison_horizontal_blocks);
		this.verticalBlocks = ConfigParser.getInstance().getAsInt(ConfigParams.image_comparison_vertical_blocks);
	}


	public boolean isMatch(BufferedImage img1, BufferedImage img2) {
		log.perfLog("Starting comparison");
		// how big are each section
		int blocksx = (int)(img1.getWidth() / horizontalBlocks);
		int blocksy = (int)(img1.getHeight() / verticalBlocks);
		log.perfLog("Calculated block size");
		// set to a match by default, if a change is found then flag non-match
		boolean match = true;
		// loop through whole image and compare individual blocks of images
		log.perfLog("Comparing brightness");
		Raster r1 = img1.getData();
		Raster r2 = img2.getData();
		for (int y = 0; y < verticalBlocks; y++) {
			log.perfLog("Y comparison done");
			for (int x = 0; x < horizontalBlocks; x++) {
				int b1 = getAverageBrightness(r1, x*blocksx, y*blocksy, blocksx - 1, blocksy - 1);
				int b2 = getAverageBrightness(r2, x*blocksx, y*blocksy, blocksx - 1, blocksy - 1);
				int diff = Math.abs(b1 - b2);
				if (diff > diffThreshold) { // the difference in a certain region has passed the threshold value of factorA
					log.debug("Difference found in "+y+" as "+diff);
					match = false;
					break;
				}
			}
			if (!match)
				break;
		}
		
		log.perfLog("Done comparing");
		
		return match;
	}
	
	private int getAverageBrightness(Raster r, int sampleStartX, int sampleStartY, int sampleWidth, int sampleHeight) {
		int total = 0;
		for (int y = 0; y < sampleHeight; y++) {
			for (int x = 0; x < sampleWidth; x++) {
				total += r.getSample(sampleStartX + x, sampleStartY + y, 0);
			}
		}
		return (int)(total * 10 / (r.getWidth() * r.getHeight()));
	}
	

	private static BufferedImage imageToBufferedImage(Image img) {
		BufferedImage bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = bi.createGraphics();
		g2.drawImage(img, null, null);
		return bi;
	}
	
	
	

	/* create a runable demo thing. */
	public static void main(String[] args) throws IOException {
		ConfigParser.instantiate();
		boolean isMatch = new ImageCompare().isMatch(ImageCompare.imageToBufferedImage(ImageIOUtils.loadJPG("D:\\tmp\\KT\\exp1.jpg")),
				ImageCompare.imageToBufferedImage(ImageIOUtils.loadJPG("D:\\tmp\\KT\\exp2.jpg")));
		// Display if these images are considered a match according to our parameters.
		System.out.println("Match: " + isMatch);
	}
	
}

