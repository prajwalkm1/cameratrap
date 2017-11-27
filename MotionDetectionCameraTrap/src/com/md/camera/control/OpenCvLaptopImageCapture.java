package com.md.camera.control;



import java.awt.image.BufferedImage;
import java.io.IOException;

import org.bytedeco.javacv.*;
import org.bytedeco.javacv.FrameGrabber.Exception;

import com.md.utils.ConfigParser;

import static org.bytedeco.javacpp.opencv_core.IplImage;
import static org.bytedeco.javacpp.opencv_core.cvFlip;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvSaveImage;



public class OpenCvLaptopImageCapture extends ImageCapture{
    private CanvasFrame canvas;
    private final boolean isDisplay;
    private final FrameGrabber grabber;
    
   
    public OpenCvLaptopImageCapture(){
    	this(false);
    }

    private OpenCvLaptopImageCapture(boolean isDisplay) {
        this.isDisplay = isDisplay;
        this.grabber = new VideoInputFrameGrabber(0); // 1 for next camera
        if (isDisplay){
        	canvas = new CanvasFrame("Web Cam");
        	canvas.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        }
        
    }
    
    public void connect() throws Exception{
    	grabber.start();
    }
    
    public void disconnect() throws Exception{
    	grabber.stop();
    }
    
    public BufferedImage capture() throws Exception{
         Frame frame = grabber.grab();
            
         if(isDisplay){
            displayAndSaveImg(frame);
         }
         return new Java2DFrameConverter().getBufferedImage(frame,1);
    }
    
    private void displayAndSaveImg(Frame frame) {
		OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
		IplImage img = converter.convert(frame);

		//the grabbed frame will be flipped, re-flip to make it right
		cvFlip(img, img, 1);// l-r = 90_degrees_steps_anti_clockwise
		//save
		String imgFileName = System.currentTimeMillis()+ "-aa.jpg";
		cvSaveImage(imgFileName, img);

		canvas.showImage(converter.convert(img));
	}


    public static void main(String[] args) throws IOException {
    	ConfigParser.instantiate();
    	final OpenCvLaptopImageCapture gs = new OpenCvLaptopImageCapture(true);
        //Thread th = new Thread(gs);
    	Thread th = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					gs.connect();
					while(true){
						    gs.capture();
						}
					
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
				}finally{
					try {
						gs.disconnect();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		});
    	th.start();
        
    }
}


