package com.android.opengles;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.fh.lib.FHSDK;

public class GLFrameSurface extends GLSurfaceView implements View.OnTouchListener, OnGestureListener, OnDoubleTapListener {
    private static final float SCALE_STEP = 0.1f;
    private GestureDetector detector;
    private GLFrameRenderer mFrameRender;
    private float hOffset = -1;
    private float hDegrees = -1;
    private float vDegrees = -1;
    private float baseValue = -1;
    private float[] hEyeDegrees = new float[4];
    private int curIndex = 0;
    private boolean isScaleMode = false;

    SensorManager mySensorManager;    //SensorManager��������
    Sensor myGyroscope;    //����������
    private static final float NS2S = 1.0f / 1000000000.0f;
    private float timestamp;
    private float angle[] = new float[3];

    private Context mContext;

    private int lastRot = -1;
    private Sensor aSensor;
    private Sensor mSensor;
    float[] accelerometerValues = new float[3];
    float[] magneticFieldValues = new float[3];
    private float startVDegrees = 0;
    private float startHDegrees = 0;

    private long flushCount = 0;
    private float lastAngleX = 0;
    private float lastAngleY = 0;

    public boolean isRun = false;


    public GLFrameSurface(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        mContext = context;
        mFrameRender = GLFrameRenderer.getInstance();
        detector = new GestureDetector(this);
        detector.setIsLongpressEnabled(true);
        detector.setOnDoubleTapListener(this);
        setOnTouchListener(this);

        mySensorManager = (SensorManager) context.getSystemService(context.SENSOR_SERVICE);
        myGyroscope = mySensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);


        aSensor = mySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensor = mySensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);


        rigisterListener();
    }

    public GLFrameSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onAttachedToWindow() {
        //Utils.LOGD("surface onAttachedToWindow()");
        super.onAttachedToWindow();

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mFrameRender.setvelocityX(0);
            mFrameRender.setvelocityY(0);
            baseValue = 0;
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (isScaleMode) {
                isScaleMode = false;
                return false;
            }
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (event.getPointerCount() == 1) {//防止不同时离开屏幕执行onScroll事件
                if (isScaleMode)
                    return false;
            }
            if (event.getPointerCount() == 2) {
                isScaleMode = true;

                float x = event.getX(0) - event.getX(1);
                float y = event.getY(0) - event.getY(1);
                float value = (float) Math.sqrt(x * x + y * y);// 计算两点的距离
                if (baseValue == 0) {
                    baseValue = value;
                } else {
                    float step = 0;
                    float scale = value / baseValue;// // // 当前两点间的距离除以手指落下时两点间的距离就是需要缩放的比例
                    if (scale > 1) {
                        step = SCALE_STEP;
                    } else if (scale < 1) {
                        step = -SCALE_STEP;
                    }
                    mFrameRender.depth = mFrameRender.depth + step;
                    if (mFrameRender.depth < FHSDK.getMaxZDepth(mFrameRender.hwin))
                        mFrameRender.depth = FHSDK.getMaxZDepth(mFrameRender.hwin);
                    else if (mFrameRender.depth > 0)
                        mFrameRender.depth = 0;
                }

                System.out.println("on touch...mFrameRender.depth == " + mFrameRender.depth);
                return false; // 缩放操作时不执行手势
            }
        }


        return detector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        // TODO Auto-generated method stub
        int i;

        //if (3 == mFrameRender.eyeMode)
        //{
        //	return false;
        //}

        hOffset = mFrameRender.hOffset;
        vDegrees = mFrameRender.vDegrees;
        hDegrees = mFrameRender.hDegrees;
        for (i = 0; i < 4; i++) {
            hEyeDegrees[i] = mFrameRender.hEyeDegrees[i];
        }
        if (e.getX() <= mFrameRender.mScreenWidth / 2 && e.getY() <= mFrameRender.mScreenHeight / 2)
            curIndex = 2;
        else if (e.getX() <= mFrameRender.mScreenWidth
                && e.getX() > mFrameRender.mScreenWidth / 2
                && e.getY() <= mFrameRender.mScreenHeight / 2)
            curIndex = 3;
        else if (e.getX() <= mFrameRender.mScreenWidth / 2
                && e.getY() <= mFrameRender.mScreenHeight
                && e.getY() > mFrameRender.mScreenHeight / 2)
            curIndex = 0;
        else if (e.getX() <= mFrameRender.mScreenWidth
                && e.getX() > mFrameRender.mScreenWidth / 2
                && e.getY() <= mFrameRender.mScreenHeight
                && e.getY() > mFrameRender.mScreenHeight / 2)
            curIndex = 1;

        mFrameRender.curIndex = curIndex;
        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        // TODO Auto-generated method stub

        //log.e("onFling: e1.getX() = " + e1.getX() + "  e2.getX() =" + e2.getX() + " velocityX =" + velocityX+ " velocityY =" + velocityY);
        if (Math.abs(velocityX) > 2000)//Ω‚æˆ¬˝ÀŸª¨∂Ø“¿»ª¥•∑¢onFlingµº÷¬µƒ ÷ ∆ª¨∂ØŒ Ã‚
        {

            if (0 == FHSDK.getDisplayType(mFrameRender.hwin))
                mFrameRender.setvelocityX(velocityX);
            else if (1 == FHSDK.getDisplayType(mFrameRender.hwin))
                mFrameRender.setvelocityX(-velocityX);

        }
        if (Math.abs(velocityY) > 2000)//Ω‚æˆ¬˝ÀŸª¨∂Ø“¿»ª¥•∑¢onFlingµº÷¬µƒ ÷ ∆ª¨∂ØŒ Ã‚
        {
            mFrameRender.setvelocityY(velocityY);
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        // TODO Auto-generated method stub
        //log.e("onLongPress");
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        // TODO Auto-generated method stub
        //log.e("onScroll: e1.getX() = " + e1.getX() + "  e2.getX() =" + e2.getX() + " distanceX =" + distanceX+ " distanceY =" + distanceY);
        if (0 == mFrameRender.displayMode || 6 == mFrameRender.displayMode) {
            float offsetX = (e2.getX() - e1.getX());
            float offsetY = (e2.getY() - e1.getY());
            if (Math.abs(offsetX) < 2 && Math.abs(offsetY) < 2) // 防止误操作  设大点会不灵敏
                return false;

            if (0 == mFrameRender.eyeMode || 1 == mFrameRender.eyeMode) {
                mFrameRender.vDegrees = vDegrees - offsetY / 10;
                mFrameRender.hDegrees = hDegrees - offsetX / 10;


                if (0 == FHSDK.getDisplayType(mFrameRender.hwin))
                    mFrameRender.hDegrees = hDegrees - offsetX / 10;
                else if (1 == FHSDK.getDisplayType(mFrameRender.hwin))
                    mFrameRender.hDegrees = hDegrees + offsetX / 10;

                if (6 == mFrameRender.displayMode) {
                    if (mFrameRender.hDegrees >= FHSDK.getMaxHDegress(mFrameRender.hwin))
                        mFrameRender.hDegrees = FHSDK.getMaxHDegress(mFrameRender.hwin);
                    if (mFrameRender.hDegrees <= FHSDK.getMinHDegress(mFrameRender.hwin))
                        mFrameRender.hDegrees = FHSDK.getMinHDegress(mFrameRender.hwin);
                }
                if (mFrameRender.vDegrees < FHSDK.getMaxVDegress(mFrameRender.hwin))
                    mFrameRender.vDegrees = FHSDK.getMaxVDegress(mFrameRender.hwin);
                else if (mFrameRender.vDegrees > FHSDK.getMinVDegress(mFrameRender.hwin))
                    mFrameRender.vDegrees = FHSDK.getMinVDegress(mFrameRender.hwin);
            } else if (2 == mFrameRender.eyeMode) {

                System.out.println("eyeMode==2..........................................offsetX==" + offsetX + "::::::::::::curIndex ==" + curIndex);
                mFrameRender.hEyeDegrees[curIndex] = hEyeDegrees[curIndex] - offsetX / 10;
            }
        } else {
            final float base = 500;
            float offset = (e2.getX() - e1.getX()) / base;
            mFrameRender.hOffset = hOffset + offset;
        }
        return false;
    }

    public void runSmooth() {

        System.out.println("111111111111111111111111");

        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO 自动生成的方法存根
                System.out.println("2222222222222222222222222");
                int currStep = 20;
                while (isRun) {

                    //System.out.println("start run...........................");
                    if (2 == mFrameRender.eyeMode) {
                        mFrameRender.hEyeDegrees[1] = hEyeDegrees[1] - 10;

                        hEyeDegrees[1] -= (currStep / mFrameRender.H_OFFSET_BASE) * 50;

                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        // TODO 自动生成的 catch 块
                        e.printStackTrace();
                    }

                }

            }
        }).start();
    }

    @Override
    public void onShowPress(MotionEvent e) {
        // TODO Auto-generated method stub
        //log.e("onShowPress");

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        // TODO Auto-generated method stub
        //log.e("onSingleTapUp");
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        // TODO Auto-generated method stub

        mFrameRender.isDoubleClick = true;

        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        // TODO Auto-generated method stub
        //log.e("onDoubleTapEvent");
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        // TODO Auto-generated method stub
        //log.e("onSingleTapConfirmed");
        return false;
    }

    private void calculateOrientation() {
        float[] values = new float[3];
        float[] R = new float[9];
        SensorManager.getRotationMatrix(R, null, accelerometerValues, magneticFieldValues);
        SensorManager.getOrientation(R, values);

        //mySensorManager.getOrientation(R, values);
        // 要经过一次数据格式的转换，转换为度  
        values[0] = (float) Math.toDegrees(values[0]);

        values[1] = (float) Math.toDegrees(values[1]);
        values[2] = (float) Math.toDegrees(values[2]);


        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        int uiRot = wm.getDefaultDisplay().getRotation();

        float anglex = 0, angley = 0, anglez = 0;

        //Log.i(TAG, values[0]+", " + values[1]+", "+ values[2]+"");  
        if (0 == startVDegrees) {
            if (0 == uiRot)
                startVDegrees = values[1];
            else if (1 == uiRot)
                startVDegrees = values[2];
            else if (2 == uiRot)
                startVDegrees = -values[1];
            else if (3 == uiRot)
                startVDegrees = -values[2];
        }
        if (0 == startHDegrees) {
            if (0 == uiRot)
                startHDegrees = -values[2];
            else if (1 == uiRot)
                startHDegrees = values[0];
            else if (2 == uiRot)
                startHDegrees = values[2];
            else if (3 == uiRot)
                startHDegrees = values[0] - 180;
        }


        if (null == myGyroscope) {
            if (0 == uiRot) {
                anglex = -values[1];
                angley = values[2];
            } else if (1 == uiRot) {
                anglex = -values[2];
                angley = -values[0];
            } else if (2 == uiRot) {
                //anglex = -values[1];
            } else if (3 == uiRot) {
                anglex = values[2];
                angley = -values[0];
            }

//        	if(lastAngleX == 0 && lastAngleY == 0)//init
//        	{
//        		update(0,0, anglex, angley);
//        		lastAngleX = anglex;
//        		lastAngleY = angley;
//        		return;
//        	}
            //Log.i(TAG, uiRot + ",  " + anglex+", " + angley+", " + Math.abs(lastAngleX- anglex)+", " + Math.abs(lastAngleY- angley));
            final float rangeLow = 0.0f;
            final float rangeHigh = 10.0f;
//        	if (Math.abs(lastAngleX- anglex) > rangeHigh || Math.abs(lastAngleY- angley) > rangeHigh)
//        	{
//        		//Log.i(TAG, uiRot + ",  " + anglex+", " + angley+", " + Math.abs(lastAngleX- anglex)+", " + Math.abs(lastAngleY- angley));  
//        		return;
//        	}
//        	if (Math.abs(lastAngleX- anglex) < rangeLow || Math.abs(lastAngleY- angley) < rangeLow)
//        	{
//        		//Log.i(TAG, uiRot + ",  " + anglex+", " + angley+", " + Math.abs(lastAngleX- anglex)+", " + Math.abs(lastAngleY- angley));  
//        		return;
//        	}
            //Log.e(TAG, uiRot + ",  " + anglex+", " + angley+", " + Math.abs(lastAngleX- anglex)+", " + Math.abs(lastAngleY- angley));
//        	if (++flushCount%5 == 0)
            {
                update(0, 0, anglex, angley);
            }
            lastAngleX = anglex;
            lastAngleY = angley;
        }
    }


    public void update(float startVDegrees, float startHDegrees, float anglex, float angley) {
        //log.e("startVDegrees = " + startVDegrees + " uiRot = " + uiRot + " x£∫"+anglex + "  y: " + angley + "  z: " + anglez);
        float offsetX = anglex;
        float offsetY = angley;
        //if (Math.abs(offsetX - lastOffsetX) < 0.1 && Math.abs(offsetY - lastOffsetY) < 0.1)
        {
            //	return;
        }
        if (3 == mFrameRender.eyeMode) {
            if (0 == mFrameRender.displayMode) {
                vDegrees = startVDegrees - offsetX;
                hDegrees = startHDegrees - offsetY;

                if (vDegrees < FHSDK.getMaxVDegress(mFrameRender.hwin))
                    vDegrees = FHSDK.getMaxVDegress(mFrameRender.hwin);
                else if (vDegrees > 0)
                    vDegrees = 0;

                mFrameRender.vDegrees = vDegrees;
                mFrameRender.hDegrees = hDegrees;

                //log.e("vDegrees = " + mFrameRender.vDegrees + "hDegrees = " + mFrameRender.hDegrees);

            } else if (6 == mFrameRender.displayMode) {
                vDegrees = startVDegrees - offsetX + 90;
                hDegrees = startHDegrees - offsetY;

                if (vDegrees < FHSDK.getMaxVDegress(mFrameRender.hwin))
                    vDegrees = FHSDK.getMaxVDegress(mFrameRender.hwin);
                else if (vDegrees > FHSDK.getMinVDegress(mFrameRender.hwin))
                    vDegrees = FHSDK.getMinVDegress(mFrameRender.hwin);

                mFrameRender.vDegrees = vDegrees;
                mFrameRender.hDegrees = hDegrees;

                //log.e("vDegrees = " + mFrameRender.vDegrees + "hDegrees = " + mFrameRender.hDegrees);
            }
        }
    }

    final SensorEventListener myListener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
                magneticFieldValues = sensorEvent.values;
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
                accelerometerValues = sensorEvent.values;

            calculateOrientation();
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    public void unRigisterListener() {
        try {

            mySensorManager.unregisterListener(mySensorListener, myGyroscope);
            mySensorManager.unregisterListener(myListener, aSensor);
            mySensorManager.unregisterListener(myListener, mSensor);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void rigisterListener() {
        try {
            mySensorManager.registerListener(
                    mySensorListener,        //添加监听
                    myGyroscope,        //传感器类型
                    SensorManager.SENSOR_DELAY_GAME    //传感器事件传递的频度
            );

            mySensorManager.registerListener(myListener, aSensor, SensorManager.SENSOR_DELAY_GAME);
            mySensorManager.registerListener(myListener, mSensor, SensorManager.SENSOR_DELAY_GAME);

        } catch (Exception e) {
            //e.printStackTrace();
        }


    }

    private SensorEventListener mySensorListener = new SensorEventListener() {//开发实现了SensorEventListener接口的传感器监听器
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

        @Override
        public void onSensorChanged(SensorEvent event) {

            WindowManager wm = (WindowManager) getContext()
                    .getSystemService(Context.WINDOW_SERVICE);
            int uiRot = wm.getDefaultDisplay().getRotation();
            if (lastRot != uiRot) {
                timestamp = 0;
                angle[0] = 0;
                angle[1] = 0;
                angle[2] = 0;
                lastRot = uiRot;
                //return;
                startVDegrees = 0;
                startHDegrees = 0;
            }

            float anglex = 0, angley = 0, anglez = 0;
            float[] values = event.values;//获取三个轴方向上的加速度值

            if (timestamp != 0) {
                // event.timesamp表示当前的时间，单位是纳秒（1百万分之一毫秒）
                final float dT = (event.timestamp - timestamp) * NS2S;
                angle[0] += event.values[0] * dT;
                angle[1] += event.values[1] * dT;
                angle[2] += event.values[2] * dT;


                anglex = (float) Math.toDegrees(angle[0]);
                angley = (float) Math.toDegrees(angle[1]);
                anglez = (float) Math.toDegrees(angle[2]);

            }
            timestamp = event.timestamp;

            if (1 == uiRot) {
                float tmp = anglex;
                anglex = -angley;
                angley = tmp;
            } else if (2 == uiRot) {
                anglex = -anglex;
                angley = -angley;
            } else if (3 == uiRot) {
                float tmp = anglex;
                anglex = angley;
                angley = -tmp;
            }

            float offsetX = anglex;
            float offsetY = angley;
            if (3 == mFrameRender.eyeMode) {
                if (0 == mFrameRender.displayMode) {
                    vDegrees = startVDegrees - offsetX;
                    hDegrees = startHDegrees - offsetY;

                    if (vDegrees < FHSDK.getMaxVDegress(mFrameRender.hwin))
                        vDegrees = FHSDK.getMaxVDegress(mFrameRender.hwin);
                    else if (vDegrees > 0)
                        vDegrees = 0;

                    mFrameRender.vDegrees = vDegrees;
                    mFrameRender.hDegrees = hDegrees;

                } else if (6 == mFrameRender.displayMode) {
                    vDegrees = startVDegrees - offsetX + 90;
                    hDegrees = startHDegrees - offsetY;

                    if (vDegrees < FHSDK.getMaxVDegress(mFrameRender.hwin))
                        vDegrees = FHSDK.getMaxVDegress(mFrameRender.hwin);
                    else if (vDegrees > FHSDK.getMinVDegress(mFrameRender.hwin))
                        vDegrees = FHSDK.getMinVDegress(mFrameRender.hwin);

                    mFrameRender.vDegrees = vDegrees;
                    mFrameRender.hDegrees = hDegrees;

                }
            }
        }
    };
}
