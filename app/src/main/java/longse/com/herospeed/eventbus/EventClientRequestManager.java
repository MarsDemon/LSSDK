package longse.com.herospeed.eventbus;

import android.graphics.Bitmap;

import java.util.List;

import longse.com.herospeed.bean.DeviceInfoBean;

/**
 * Created by LY on 2017/11/14.
 */

public class EventClientRequestManager {
    private int mMsg;
    private Object mObject;
    private String mAction;
    private Bitmap mBitmap;
    private List<DeviceInfoBean> devicesList;

    public EventClientRequestManager(String action, int msg) {
        this.mAction = action;
        this.mMsg = msg;
    }

    public EventClientRequestManager(String action, int msg, Object object) {
        this.mAction = action;
        this.mMsg = msg;
        this.mObject = object;
    }

    public EventClientRequestManager(String action, int msg, List<DeviceInfoBean> deviceInfoList) {
        this.mAction = action;
        this.mMsg = msg;
        this.devicesList = deviceInfoList;
    }

    public EventClientRequestManager(String action, int msg, Bitmap bitmap) {
        this.mAction = action;
        this.mMsg = msg;
        this.mBitmap = bitmap;
    }

    public String getAction(){
        return mAction;
    }

    public int getMsg(){
        return mMsg;
    }

    public Object getObject(){
        return mObject;
    }

    public Bitmap getBitmap(){
        return mBitmap;
    }

    public List<DeviceInfoBean> getDevicesList(){
        return devicesList;
    }

}
