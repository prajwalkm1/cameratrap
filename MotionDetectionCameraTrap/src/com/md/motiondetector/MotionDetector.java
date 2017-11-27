package com.md.motiondetector;

public interface MotionDetector {
	public void initialize() throws Exception;
	public void destroy() throws Exception;
	public boolean isMotionDetected() throws Exception;
}
