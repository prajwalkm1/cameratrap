package com.md.utils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageIOUtils {

	private ImageIOUtils() {
	}

	// read a jpeg file into a buffered image
	public static Image loadJPG(String filename) throws IOException {
		return ImageIO.read(new File(filename));
	}
	
	public static void saveImg(BufferedImage img, String filename) throws IOException{
		ImageIO.write(img, "JPEG", new File(filename));
	}

}
