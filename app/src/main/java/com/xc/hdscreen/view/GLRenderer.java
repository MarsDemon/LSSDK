package com.xc.hdscreen.view;

import android.annotation.SuppressLint;
import android.opengl.GLSurfaceView;
import android.os.Handler;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


@SuppressLint("ParserError")
public class GLRenderer implements GLSurfaceView.Renderer {

	private native int nativeInit(int winId);

	private native static int nativeSetup(int winId, int x, int y, int w, int h);

	private native void nativeDrawFrame(int playerId);

	private static int width;
	private static int height;
	private int winId;
	private String deviceId;
	private Handler handler;

	@SuppressWarnings("static-access")
	public GLRenderer(int winId, int weight, int height, Handler handler,
					  String deviceId) {
		this.winId = winId;
		this.width = weight;
		this.height = height;
		this.deviceId = deviceId;
		this.handler = handler;
	}

	public void onDrawFrame(GL10 arg0) {
		

	}
    
	public void onSurfaceChanged(GL10 arg0, int w, int h) {

	}

	public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {

	}

	private String formatTime(int t) {
		return t >= 10 ? "" + t : "0" + t;
	}

}
