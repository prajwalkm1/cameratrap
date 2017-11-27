package com.md.camera.control;

import java.awt.image.BufferedImage;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public abstract class ImageCapture {
	
	private boolean isBurstEnabled;
	private int multImageCount;
	private int multImageInterval;
	private int burstDuration;
	
	public abstract void connect() throws Exception;
	public abstract void disconnect() throws Exception;
	
	public BufferedImage capture() throws Exception{
		throw new NotImplementedException();
	}
	
	public void captureInCamera() throws Exception{
		throw new NotImplementedException();
	}
	
	public boolean isBurstEnabled() {
		return isBurstEnabled;
	}
	public void setBurstEnabled(boolean isBurstEnabled) {
		this.isBurstEnabled = isBurstEnabled;
	}
	public int getMultImageCount() {
		return multImageCount;
	}
	public void setMultImageCount(int multImageCount) {
		this.multImageCount = multImageCount;
	}
	public int getMultImageInterval() {
		return multImageInterval;
	}
	public void setMultImageInterval(int multImageInterval) {
		this.multImageInterval = multImageInterval;
	}
	public int getBurstDuration() {
		return burstDuration;
	}
	public void setBurstDuration(int burstDuration) {
		this.burstDuration = burstDuration;
	}
	
	

}
