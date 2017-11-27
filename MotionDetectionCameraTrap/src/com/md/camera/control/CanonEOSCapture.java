package com.md.camera.control;

import com.md.logger.LogFactory;
import com.md.logger.Logger;
import com.md.utils.ConfigParser;
import com.sun.jna.NativeLong;
import com.sun.jna.ptr.NativeLongByReference;

import edsdk.api.CanonCamera;
import edsdk.bindings.EdSdkLibrary;
import edsdk.bindings.EdSdkLibrary.EdsCameraListRef;
import edsdk.bindings.EdSdkLibrary.EdsCameraRef;
import edsdk.utils.CanonConstants.EdsShutterButton;
import edsdk.utils.CanonUtils;
import edsdk.utils.CanonConstants.EdsError;

public class CanonEOSCapture extends ImageCapture {

	private EdsCameraRef.ByReference camera;
	private final Logger log;

	public CanonEOSCapture() {
		super();
		this.log = LogFactory.getLogger(this.getClass());
	}

	@Override
	public void connect() throws Exception {
		

	}

	@Override
	public void disconnect() throws Exception {
		

	}
	
	private void createSession(){
		validateResult(CanonCamera.EDSDK.EdsInitializeSDK(), "initializing EOS SDK");

		EdsCameraListRef.ByReference cameraList = new EdsCameraListRef.ByReference();
		validateResult(CanonCamera.EDSDK.EdsGetCameraList(cameraList), "getting camera list");

		validateCameraConnection(cameraList);

		camera = new EdsCameraRef.ByReference();
		validateResult(CanonCamera.EDSDK.EdsGetChildAtIndex(cameraList.getValue(), new NativeLong(0), camera),
				"getting camera reference");

		validateResult(CanonCamera.EDSDK.EdsOpenSession(camera.getValue()), "opening session");
	}
	
	private void destroySession(){
		validateResult(CanonCamera.EDSDK.EdsCloseSession(camera.getValue()), "closing EOS session");
		validateResult(CanonCamera.EDSDK.EdsTerminateSDK(), "terminating SDK");
	}

	@Override
	public void captureInCamera() throws Exception {
		try{
			//Have to create session.  Otherwise camera is not responding
			createSession();
			if (isBurstEnabled()) {
				captureImageBurst();
			} else {
				captureSingleImages();
			}
		}finally{
			destroySession();
		}
		

	}

	private void captureSingleImages() throws InterruptedException {
		if (getMultImageCount() > 1) {
			for (int i = 0; i < getMultImageCount(); i++) {
				log.debug("Capturing image");
				CanonCamera.EDSDK.EdsSendCommand(camera.getValue(),
						new NativeLong(EdSdkLibrary.kEdsCameraCommand_TakePicture), new NativeLong(10));
				log.debug("Sleeping before capturing next image");
				Thread.sleep(getMultImageInterval());
			}
		} else {
			log.debug("Capturing single image");
			CanonCamera.EDSDK.EdsSendCommand(camera.getValue(),
					new NativeLong(EdSdkLibrary.kEdsCameraCommand_TakePicture), new NativeLong(10));
			log.debug("Image captured");
		}
	}

	private void captureImageBurst() throws InterruptedException {
		validateResult(
				CanonCamera.EDSDK.EdsSendCommand(camera.getValue(),
						new NativeLong(EdSdkLibrary.kEdsCameraCommand_PressShutterButton), new NativeLong(
								EdsShutterButton.kEdsCameraCommand_ShutterButton_Completely.value().longValue())),
				"pressing shutter button");

		Thread.sleep(getBurstDuration());

		validateResult(
				CanonCamera.EDSDK.EdsSendCommand(camera.getValue(),
						new NativeLong(EdSdkLibrary.kEdsCameraCommand_PressShutterButton),
						new NativeLong(EdsShutterButton.kEdsCameraCommand_ShutterButton_OFF.value().longValue())),
				"releasing shutter button");
	}

	private void validateResult(NativeLong commandOutput, String msg) {
		int result = commandOutput.intValue();

		if (result != EdSdkLibrary.EDS_ERR_OK) {
			EdsError err = CanonUtils.toEdsError(result);
			log.error("Error " + err.value() + ": " + err.name() + " - " + err.description());
			throw new ImageCaptureException("Error while " + msg + ". Error code: " + result);
		} else {
			log.debug(msg);
		}
	}

	private void validateCameraConnection(EdsCameraListRef.ByReference cameraList) {
		NativeLongByReference cameraCountRef = new NativeLongByReference();
		validateResult(CanonCamera.EDSDK.EdsGetChildCount(cameraList.getValue(), cameraCountRef),
				"getting connected camera count");
		long numCams = cameraCountRef.getValue().longValue();
		log.debug("Number of connected cameras: " + numCams);

		if (numCams == 0) {
			throw new ImageCaptureException("no EOS camera found");
		}
	}

	public static void main(String[] args) throws Exception {
		ConfigParser.instantiate();
		CanonEOSCapture capture = new CanonEOSCapture();
		capture.connect();

		capture.captureInCamera();
		System.out.println("Captured images");

		capture.disconnect();

	}

}
