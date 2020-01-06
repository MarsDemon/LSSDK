package com.android.opengles;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.fh.lib.FHSDK;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.Calendar;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLFrameRenderer implements Renderer{
	public static final float H_OFFSET_BASE = 1000.0f;
	private static final float STEP_BASE_FAST = 200.0f;
	private static final float STEP_BASE_SLOW = 100.0f;
	private static final float STEP_OFFSET = 3.0f;
	
	private static final int DISPLAY_TYPE_YUV = 0;
	private static final int DISPLAY_TYPE_RGB = 1;

    private GLSurfaceView mTargetSurface;
    public static int mScreenWidth, mScreenHeight;
    public static int mDrawWidth, mDrawHeight;
    private int view_x, view_y, view_w, view_h;
    private static int mVideoWidth =0, mVideoHeight = 0;
  
	static byte[] yArray = new byte[1024 * 1024 * 24];
	static byte[] uArray = new byte[1024 * 1024 * 2];
	static byte[] vArray = new byte[1024 * 1024 * 2];
    
    private boolean bSurfaceCreate = false;
	private boolean bSurfaceChanged = false;
	//private int lastShowMode = -1;
	private Handler mHandler = null;
	private Context mContext;
	
	public static boolean  bMixMode = false;
	
	public static GLFrameRenderer instance;
	
	private byte[] frameBuf = null;
	public static boolean isDebugMode = false; 
	public static int displayMode = 9;   //3:单展 4:双展 5:圆筒 6:壁挂 7:挂壁矫正
	
	public static float vDegrees = 0;
	public static float hDegrees = 0;
	public static float depth = 0;
		
	public static float hOffset = 0;
	private static float velocityX = 0;
	private static float velocityY = 0;
	public static float scrollStep = 0;
	public static int modeOffset = 0;
	public static int eyeMode = 0; //  0 default, 1 four screen 2 顶视四屏 3 顶视vr
	public static float[] hEyeDegrees = new float[]{0, 90, 180, 270};
	
	public static int curIndex = 0;
	public static boolean isDoubleClick = false;
	public static boolean isZoomIn = false;
	//private VRFragment vrFragment;
	public static int hwin= 0,hBuffer = 0;
	
	public static int hWinMixMode[]  = new int[4];
	public static int hBufferMixMode[]  = new int[4];
	private boolean bUpdated = false;

	public static int ctrlIndex = 0;
	public static boolean resChanged = false;
	private float lastVDegrees = -1;
	private float lastHDegrees = -1;
	private float lastDepth = -1;
	private float lastHOffset = -1;
	
	public static boolean isPlayer = false;
	
	private Handler mhandler;
	
	//,VRFragment vrContext
    public GLFrameRenderer(Context context, GLSurfaceView surface, int width, int height, Handler handler, int mFrameWidth, int mFrameHeight) {
    	mContext = context;
        mTargetSurface = surface;
        mScreenWidth =width;
        mScreenHeight =height;
        mVideoWidth =  mFrameWidth; //mFrameWidth
        mVideoHeight =  mFrameHeight; //mFrameHeight
        mhandler = handler;
		mHandler = new Handler();
		mHandler.post(requestRender);
      
    }
    public GLFrameRenderer()
    {
    	
    }

    public void refreshFrameSize(int frameWidth,int frameHeight){
		mVideoWidth = frameWidth;
		mVideoHeight = frameHeight;
	}
 
    public static GLFrameRenderer getInstance()
    {
        if(null == instance)
        {
            instance = new GLFrameRenderer();
        }
        return instance;             
    }
    Runnable scaleView = new Runnable()
	{
		@Override
		public void run() {
			
			final float totalTime = 2*100;
			float vDegreesStep = Math.abs((FHSDK.getMaxVDegress(hwin) - FHSDK.getMinVDegress(hwin)) /totalTime);//Math.abs(FHSDK.getMaxVDegress(hWin)/totalTime);
			float depthStep = Math.abs(FHSDK.getMaxZDepth(hwin)/totalTime);
			float hDegreesStep = 90/totalTime;
			
			if (isZoomIn)
			{
				if(0 == FHSDK.getDisplayType(hwin))
					vDegrees -= vDegreesStep;
				else if(1 == FHSDK.getDisplayType(hwin))
					vDegrees += vDegreesStep;
				
				hDegrees += hDegreesStep;
				depth += depthStep;
			}
			else
			{
				float speed = 4.0f;
				
				if(0 == FHSDK.getDisplayType(hwin))
					vDegrees += vDegreesStep*speed;
				else if(1 == FHSDK.getDisplayType(hwin))
					vDegrees -= vDegreesStep*speed;
								
				hDegrees -= hDegreesStep*speed;
				depth -= depthStep*speed;
			}
			
			if (vDegrees < FHSDK.getMaxVDegress(hwin))
				vDegrees = FHSDK.getMaxVDegress(hwin);
			else if (vDegrees > FHSDK.getMinVDegress(hwin))
				vDegrees = FHSDK.getMinVDegress(hwin);


			if (depth < FHSDK.getMaxZDepth(hwin))
				depth = FHSDK.getMaxZDepth(hwin);
			else if (depth > 0)
				depth = 0;

			if ((isZoomIn && depth != 0)||  (!isZoomIn && depth != FHSDK.getMaxZDepth(hwin)))
				mHandler.postDelayed(scaleView, 10);
		}
	};
	
    Runnable requestRender = new Runnable()
	{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			if (0 != displayMode&&6!=displayMode)
				eyeMode = 0;
			
			if (isDoubleClick && ((0 == displayMode && 0 == eyeMode) ||6 == displayMode))
			{
				isDoubleClick = false;
				
				if (depth != FHSDK.getMaxZDepth(hwin))
				{
					isZoomIn = false;
				}
				else
				{
					isZoomIn = true;
				}
				mHandler.post(scaleView);
			}
			
			if (velocityX > 0)
			{
				velocityX -= scrollStep;
				if (velocityX < 0 || scrollStep/H_OFFSET_BASE < 0.005f)
					velocityX = 0;
					
				if (0 == displayMode|| 6 == displayMode)
				{
					if (0 == eyeMode || 1 == eyeMode)
					{
						hDegrees -= (scrollStep/H_OFFSET_BASE)*50;
					}
					else if (2 == eyeMode)
					{
						
						hEyeDegrees[curIndex] -= (scrollStep/H_OFFSET_BASE)*50;
												
					}
				}
				else
					hOffset -= scrollStep/H_OFFSET_BASE;

			}
			else if (velocityX < 0)
			{
				velocityX += scrollStep;
				if (velocityX > 0 || scrollStep/H_OFFSET_BASE < 0.005f)
					velocityX = 0;
				
				if (0 == displayMode  || 6 == displayMode)
				{
					if (0 == eyeMode || 1 == eyeMode)
					{
						hDegrees += (scrollStep/H_OFFSET_BASE)*50;
					}
					else if (2 == eyeMode)
					{
						hEyeDegrees[curIndex] += (scrollStep/H_OFFSET_BASE)*50;
					}
				}
				else
					hOffset += scrollStep/H_OFFSET_BASE;
				
			}
			
			if (velocityY > 0)
			{
				velocityY -= scrollStep;
				if (velocityY < 0 || scrollStep/H_OFFSET_BASE < 0.005f)
					velocityY = 0;
					
				if (6 == displayMode||0 == displayMode )
				{
					vDegrees -= (scrollStep/H_OFFSET_BASE)*50;
					
					if (vDegrees < FHSDK.getMaxVDegress(hwin))
						vDegrees = FHSDK.getMaxVDegress(hwin);
					else if (vDegrees > FHSDK.getMinVDegress(hwin))
						vDegrees = FHSDK.getMinVDegress(hwin);
				}
				else
					hOffset -= scrollStep/H_OFFSET_BASE;

			}
			else if (velocityY < 0)
			{
				velocityY += scrollStep;
				if (velocityY > 0 || scrollStep/H_OFFSET_BASE < 0.005f)
					velocityY = 0;
				
				if (6 == displayMode)
				{
					vDegrees += (scrollStep/H_OFFSET_BASE)*50;
				}
				else
					hOffset += scrollStep/H_OFFSET_BASE;
			}
			
			if (scrollStep > 0)
				scrollStep -= STEP_OFFSET;
			
			if(!isPlayer)
				return ;
			
			mHandler.postDelayed(requestRender, 10);
			
		}
    	
	};
    
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
    	System.out.println("GLFrameRenderer :: onSurfaceCreated");
         
        bSurfaceCreate = true;
        displayMode = 9;  //0
        
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
    	System.out.println("GLFrameRenderer :: onSurfaceChanged()(" + width + ","  + height + ")");

    	mScreenWidth = width;
		mScreenHeight = height;
		
		if (mScreenWidth < mScreenHeight)
		{
			mDrawWidth = mDrawHeight = mScreenWidth;
		}
		else
		{
			mDrawWidth = mScreenWidth;
			mDrawHeight = mScreenHeight; 
		}

		
		bSurfaceChanged = true;
		
    	view_x = (mScreenWidth - mDrawWidth)/2;
    	view_y = (mScreenHeight - mDrawHeight)/2;
    	view_w = mDrawWidth;
    	view_h = mDrawHeight;
    	
    	FHSDK.init(mDrawWidth, mDrawHeight);
    	
    	//System.out.println("DEBUG:: mDrawWidth =="+mDrawWidth+"; mDrawHeight ="+mDrawHeight);
    	
    	if (0 != hBuffer)
    	{
    		FHSDK.destroyBuffer(hBuffer);
    	}
    	hBuffer = FHSDK.createBuffer(DISPLAY_TYPE_YUV);
    	
    	if (0 != hwin)
    	{
    		FHSDK.destroyWindow(hwin);
    	}

    	hwin = FHSDK.createWindow(displayMode);
    	
    	FHSDK.unbind(hwin);
		FHSDK.bind(hwin, hBuffer);
				
		depth = FHSDK.getMaxZDepth(hwin);
		
    }

    
	public String getCurrentTime() {
		Calendar c = Calendar.getInstance();
		String time = c.get(Calendar.YEAR) + "-" + // �õ���
				formatTime(c.get(Calendar.MONTH) + 1) + "-" + // month��һ //��
				formatTime(c.get(Calendar.DAY_OF_MONTH)) + "!" + // ��
				formatTime(c.get(Calendar.HOUR_OF_DAY)) + "," + // ʱ
				formatTime(c.get(Calendar.MINUTE)) + "," + // ��
				formatTime(c.get(Calendar.SECOND)); // ��
		return time;
	}

	private String formatTime(int t) {
		return t >= 10 ? "" + t : "0" + t;// ��Ԫ����� t>10ʱȡ ""+t
	}
	 int count = 0;
    @Override
    public void onDrawFrame(GL10 gl) {
        synchronized (this) {
			int i;
			if (!isPlayer) {
				System.out.println("device is close .............................");
				return;
			}

			if (displayMode != FHSDK.getDisplayMode(hwin)) {
				FHSDK.unbind(hwin);
				FHSDK.destroyWindow(hwin);
				System.out.println("displayMode == "+displayMode);
				hwin = FHSDK.createWindow(displayMode);
				FHSDK.bind(hwin, hBuffer);
			}
			//System.out.println("displayMode == "+displayMode +"hWin =="+hwin);
			//FHSDK.setStandardCircle(hwin, mVideoWidth / 2, mVideoHeight / 2, mVideoHeight / 2 - 20);

			if (!isDebugMode && yArray != null) {

				FHSDK.update(hBuffer, yArray, mVideoWidth, mVideoHeight);
			} else {
				System.out.println("y is null.........................");
				return;
			}
			FHSDK.clear();
			if (0 == displayMode || 6 == displayMode) {
				if (0 == eyeMode) {
					FHSDK.viewport(view_x, view_y, view_w, view_h);
					//if (lastVDegrees != vDegrees || lastHDegrees != hDegrees || lastDepth != depth)					
					FHSDK.eyeLookAt(hwin, vDegrees, hDegrees, depth);
					FHSDK.draw(hwin);
					//System.out.println("DEBUG:: mVideoWidth =="+mVideoWidth+"; mVideoHeight ="+mVideoHeight +"; displayMode"+displayMode);
				} else if (1 == eyeMode) {
					int[] x = {view_x, view_x + view_w / 2, view_x, view_x + view_w / 2};
					int[] y = {view_y, view_y, view_y + view_h / 2, view_y + view_h / 2};
					for (i = 0; i < 4; i++) {
						gl.glViewport(x[i], y[i], view_w / 2, view_h / 2);//GLES20
						FHSDK.eyeLookAt(hwin, FHSDK.getMaxVDegress(hwin), hDegrees + 90 * i, 0);
						FHSDK.draw(hwin);
					}
				} else if (2 == eyeMode) {
					int[] x = {view_x, view_x + view_w / 2, view_x, view_x + view_w / 2};
					int[] y = {view_y, view_y, view_y + view_h / 2, view_y + view_h / 2};
					for (i = 0; i < 4; i++) {
						FHSDK.viewport(x[i], y[i], view_w / 2, view_h / 2);
						FHSDK.eyeLookAt(hwin, FHSDK.getMaxVDegress(hwin), hEyeDegrees[i], 0);
						FHSDK.draw(hwin);
					}

				} else if (3 == eyeMode) {
					int[] x = {view_x, view_x + view_w / 2};
					int[] y = {view_y, view_y};
					for (i = 0; i < 2; i++) {
						FHSDK.viewport(x[i], y[i], view_w / 2, view_h);
						//if (lastVDegrees != vDegrees || lastHDegrees != hDegrees)

						FHSDK.eyeLookAt(hwin, vDegrees, hDegrees, 0);
						lastVDegrees = vDegrees;
						lastHDegrees = hDegrees;

						FHSDK.draw(hwin);
					}
				}
			} else {

				//System.out.println("viewport:::view_x ="+view_x+" view_y="+view_y+" view_w"+view_w+" view_h"+view_h);

				FHSDK.viewport(view_x, view_y, view_w, view_h);
				FHSDK.expandLookAt(hwin, hOffset);
				FHSDK.draw(hwin);
			}
		}
    }
	
	 public void setvelocityY(float velocityY)
	    {
	    	this.velocityY = velocityY;
	    	if (Math.abs(velocityY) > 3000)
	    		scrollStep = STEP_BASE_FAST;
	    	else
	    		scrollStep = STEP_BASE_SLOW;
	    }


    public void setvelocityX(float velocityX)
    {
    	this.velocityX = velocityX;
    	if (Math.abs(velocityX) > 3000)
    		scrollStep = STEP_BASE_FAST;
    	else
    		scrollStep = STEP_BASE_SLOW;
    }

}
