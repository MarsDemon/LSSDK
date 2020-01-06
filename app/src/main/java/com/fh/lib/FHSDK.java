package com.fh.lib;

public class FHSDK {
	
	static {
		
		try{
			  System.loadLibrary("FishEyeSdk");
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("load main error");
		}
      
    }

    public native static boolean init(int viewWidth, int viewHeight); 
	public native static boolean unInit();

	
	public native static int createBuffer(int bufferType);//bufferType: 0 YUV 1 RGB
	public native static int destroyBuffer(int hBuffer);

	public native static boolean bind(int hWin, int hBuffer);
	public native static boolean isBind(int hWin);
	public native static boolean unbind(int hWin);
	
	
    public native static int createWindow(int displayMode);
    public native static int getDisplayMode(int hWin);
    public native static boolean destroyWindow(int hWin);

    
    public native static boolean setDisplayType(int hWin, int displayType);
    public native static int getDisplayType(int hWin);

    
    
	public native static boolean setDebugMode(int hBuffer, byte[] rgbData, int width, int height);
	
	public native static boolean update(int hBuffer, byte[] data, int videoWidth, int videoHeight); // data: YUV420/RGB
	
    public native static boolean clear();
    public native static boolean viewport(int x, int y, int width, int height); // ��������
    public native static boolean draw(int hWin);
    

    
	public native static boolean eyeLookAt(int hWin, float vDegrees, float hDegrees, float depth);
	public native static boolean expandLookAt(int hWin,  float hOffset);
	public native static byte[] snapshot(int x, int y, int w, int h, boolean bTrans);
	public native static boolean frameParse(int hBuffer, byte[] frameData, int frameLen);

    public native static float getViewAngle(int hWin);       //�۲��ӽ�, default 60.0,�ݲ�֧�ָ���
    public native static float getMaxZDepth(int hWin);       //Զ�����, MaxZDepth(��)-��Զ, 0-���

    public native static float getMinVDegress(int hWin);     //�������, MaxVDeress(��)-����, MinVDegress(��)-����, 0-��
    public native static float getMaxVDegress(int hWin);     //�������, MaxVDeress(��)-����, MinVDegress(��)-����, 0-��

    public native static float getMinHDegress(int hWin);     //�������, [MinHFegress, MaxHDegress], [0,0]������
    public native static float getMaxHDegress(int hWin);     //�������, [MinHFegress, MaxHDegress], [0,0]������

	public native static int getFieldOfView(int hWin);
	public native static boolean setFieldOfView(int hWin, int fieldOfView);
    public native static boolean setStandardCircle(int hWin, float circleCenterX, float circleCenterY, float r);
    public native static boolean resetStandardCircle(int hWin);
    public native static boolean setImagingType(int hWin, int type);
    public native static int getImagingType(int hWin);
    public native static boolean resetEyeView(int hBuffer);
    public native static float getVerticalCutRatio(int hWin);
    public native static boolean setVerticalCutRatio(int hWin, float verticalCutRatio);
	
	

}
