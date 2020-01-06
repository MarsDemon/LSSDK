package longse.com.herospeed.manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;


import com.longse.freeipfunction.manager.RequestManager;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import longse.com.herospeed.base.ConstAction;
import longse.com.herospeed.base.Constants;
import longse.com.herospeed.bean.DeviceInfoBean;
import longse.com.herospeed.bean.PersonalBean;
import longse.com.herospeed.eventbus.EventClientRequestManager;
import longse.com.herospeed.utils.LogUtil;
import longse.com.herospeed.utils.SharedUtils;

import static com.longse.freeipfunction.context.Constant.CheckDeviceCodeSuccess;
import static com.longse.freeipfunction.context.Constant.FAILURE;
import static com.longse.freeipfunction.context.Constant.GetAllDeviceSuccess;
import static com.longse.freeipfunction.context.Constant.SNDeviceInfoByDidSuccess;
import static com.longse.freeipfunction.context.Constant.bindDeviceSuccess;
import static com.longse.freeipfunction.context.Constant.changePwdActionSuccess;
import static com.longse.freeipfunction.context.Constant.checkUsernameSuccess;
import static com.longse.freeipfunction.context.Constant.getCookieFromServerSuccess;
import static com.longse.freeipfunction.context.Constant.loginActionSuccess;
import static com.longse.freeipfunction.context.Constant.sendEmailSuccess;
import static com.longse.freeipfunction.context.Constant.submitResgisterInfoSuccess;

/**
 * Created by LY on 2017/11/14.
 */

public class ClientRequestManager {
    private static String TAG = "ClientRequestManager";

    private static ClientRequestManager clientRequestManager;
    private Bitmap bitmap;
    private String requestContent;
    private Context mContext;

    public static ClientRequestManager initClientRequestManager() {
        synchronized (ClientRequestManager.class) {
            if (clientRequestManager == null) {
                clientRequestManager = new ClientRequestManager();
            }
            return clientRequestManager;
        }
    }

    public void initRequestManager(Context context) {
        mContext = context;
        RequestManager.getInstance().initHandler(handler);
    }


    public void requestSendEmail(final String email) {
        if (TextUtils.isEmpty(SharedUtils.getString(mContext, Constants.CACHE_COOKIE, null))) {
            LogUtil.e(TAG, "不存在CooKie");
            RequestManager.getInstance().getCookieFromServer();
        } else {
            RequestManager.getInstance().sendEmail(email);
        }
    }

    public void requestLogin(final String phone, final String password) {
        if (TextUtils.isEmpty(SharedUtils.getString(mContext, Constants.CACHE_COOKIE, null))) {
            LogUtil.e(TAG, "不存在CooKie");
            RequestManager.getInstance().getCookieFromServer();
        } else {
            LogUtil.e(TAG, "测试登录");
            RequestManager.getInstance().loginAction(phone, password);
        }
    }

    public void requestDeviceStreamstatus(DeviceInfoBean deviceinfo){

        if(deviceinfo == null){
            return ;
        }
        Map<String,Object>  params = new HashMap<String,Object>();
        params.put("","");


    }

    public void Captcha() {
        LogUtil.e(TAG, "不存在CooKie==" + SharedUtils.getString(mContext, Constants.CACHE_COOKIE, null));
        if (TextUtils.isEmpty(SharedUtils.getString(mContext, Constants.CACHE_COOKIE, null))) {
            LogUtil.e(TAG, "不存在CooKie");
            RequestManager.getInstance().getCookieFromServer();
        } else {
            RequestManager.getInstance().getCaptcha();
        }
    }


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            LogUtil.e(TAG, "msg==" + msg);
            Bundle bundle = msg.getData();
            requestContent = bundle.getString("response"); //文本内容(返回的消息内容)
            bitmap = bundle.getParcelable("bitmap"); //图片内容（返回的图片内容）

            if (bitmap != null) {
                EventBus.getDefault().post(new EventClientRequestManager(ConstAction.ACTION_BITMAP, Constants.REQUEST_SUEESS, bitmap));
            }

            switch (msg.what) {
                case getCookieFromServerSuccess:
                    LogUtil.e(TAG, "getCookieFromServerSuccess");
                    try {
                        JSONObject json = new JSONObject(requestContent);
                        int code = json.getInt("code");
                        if (code == 0) {
                            String cookie = json.getString("data");
                            SharedUtils.putString(mContext, Constants.CACHE_COOKIE, cookie);
                            EventBus.getDefault().post(new EventClientRequestManager(ConstAction.ACTION_COOKIE, Constants.REQUEST_SUEESS));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        EventBus.getDefault().post(new EventClientRequestManager(ConstAction.ACTION_COOKIE, Constants.REQUEST_FAILURE));
                    }
                    break;

                case loginActionSuccess:
                    LogUtil.e(TAG, "requestContent==" + requestContent);
                    try {
                        JSONObject json = new JSONObject(requestContent);
                        int code = json.optInt("code");
                        if (code == 0) {
                            JSONObject jsonObject = json.getJSONObject("data");
                            SharedUtils.putString(mContext, Constants.CACHE_USERINF, jsonObject.toString());
                            EventBus.getDefault().post(new EventClientRequestManager(ConstAction.ACTION_LOGIN, Constants.REQUEST_SUEESS));
                            return;
                        } else {
                            String message = json.getString("msg");
                            EventBus.getDefault().post(new EventClientRequestManager(ConstAction.ACTION_LOGIN, Constants.REQUEST_FAILURE, message));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        EventBus.getDefault().post(new EventClientRequestManager(ConstAction.ACTION_LOGIN, Constants.REQUEST_NON));
                    }
                    break;

                case submitResgisterInfoSuccess:
                    try {
                        JSONObject json = new JSONObject(requestContent);
                        int code = json.optInt("code");
                        if (code == 0) {
                            EventBus.getDefault().post(new EventClientRequestManager(ConstAction.ACTION_REGISTER, Constants.REQUEST_SUEESS));
                            return;
                        } else {
                            String message = json.getString("msg");
                            EventBus.getDefault().post(new EventClientRequestManager(ConstAction.ACTION_REGISTER, Constants.REQUEST_FAILURE, message));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        EventBus.getDefault().post(new EventClientRequestManager(ConstAction.ACTION_REGISTER, Constants.REQUEST_NON));
                    }
                    break;

                case checkUsernameSuccess:
                    LogUtil.e(TAG, "checkUsernameSuccess==" + requestContent);
                    try {
                        JSONObject json = new JSONObject(requestContent);
                        int code = json.optInt("code");
                        if (code == 0) {
                            EventBus.getDefault().post(new EventClientRequestManager(ConstAction.ACTION_CHECKEMAIL, Constants.REQUEST_SUEESS));
                            return;
                        } else {
                            String message = json.getString("msg");
                            EventBus.getDefault().post(new EventClientRequestManager(ConstAction.ACTION_CHECKEMAIL, Constants.REQUEST_FAILURE, message));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        EventBus.getDefault().post(new EventClientRequestManager(ConstAction.ACTION_CHECKEMAIL, Constants.REQUEST_NON));
                    }
                    break;

                case sendEmailSuccess:
                    LogUtil.e(TAG, "sendEmailSuccess==" + requestContent);
                    try {
                        JSONObject json = new JSONObject(requestContent);
                        int code = json.optInt("code");
                        if (code == 0) {
                            JSONObject jo = json.getJSONObject("data");
                            String sessionId = jo.getString("session_id");
                            SharedUtils.putString(mContext, Constants.CACHE_SESSION, sessionId);
                            EventBus.getDefault().post(new EventClientRequestManager(ConstAction.ACTION_SENDCODE_EMAIL, Constants.REQUEST_SUEESS));
                            return;
                        } else {
                            String message = json.getString("msg");
                            EventBus.getDefault().post(new EventClientRequestManager(ConstAction.ACTION_SENDCODE_EMAIL, Constants.REQUEST_FAILURE, message));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        EventBus.getDefault().post(new EventClientRequestManager(ConstAction.ACTION_SENDCODE_EMAIL, Constants.REQUEST_NON));
                    }
                    break;

                case changePwdActionSuccess:
                    try {
                        JSONObject json = new JSONObject(requestContent);
                        int code = json.optInt("code");
                        if (code == 0) {
                            EventBus.getDefault().post(new EventClientRequestManager(ConstAction.ACTION_CHANGE_PWD, Constants.REQUEST_SUEESS));
                            return;
                        } else {
                            String message = json.getString("msg");
                            EventBus.getDefault().post(new EventClientRequestManager(ConstAction.ACTION_CHANGE_PWD, Constants.REQUEST_FAILURE, message));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        EventBus.getDefault().post(new EventClientRequestManager(ConstAction.ACTION_CHANGE_PWD, Constants.REQUEST_NON));
                    }
                    break;

                case GetAllDeviceSuccess:
                    try {
                        JSONObject json = new JSONObject(requestContent);
                        int code = json.optInt("code");
                        if (code == 0) {
                            JSONArray jsonArray = json.optJSONArray("data");
                            List<DeviceInfoBean> devicesList = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                devicesList.add(DeviceInfoBean.parse((JSONObject) jsonArray.get(i)));
                            }
                            EventBus.getDefault().post(new EventClientRequestManager(ConstAction.ACTION_ALLDEVICES, Constants.REQUEST_SUEESS, devicesList));
                            return;
                        } else {
                            String message = json.getString("msg");
                            EventBus.getDefault().post(new EventClientRequestManager(ConstAction.ACTION_ALLDEVICES, Constants.REQUEST_FAILURE, message));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        EventBus.getDefault().post(new EventClientRequestManager(ConstAction.ACTION_ALLDEVICES, Constants.REQUEST_NON));
                    }
                    break;
                case SNDeviceInfoByDidSuccess:
                    LogUtil.d(TAG, "SNDeviceInfoByDidSuccess" + requestContent);
                    try {
                        JSONObject json = new JSONObject(requestContent);
                        int code = json.optInt("code");
                        if(code ==0){
                            DeviceInfoBean info;
                            JSONObject obj = json.getJSONObject("data");
                            info = DeviceInfoBean.parse(obj);
                            EventBus.getDefault().post(new EventClientRequestManager(ConstAction.ACTION_SNLOGIN, Constants.REQUEST_SUEESS, info));
                            return;
                        }else {
                            String message = json.getString("msg");
                            EventBus.getDefault().post(new EventClientRequestManager(ConstAction.ACTION_SNLOGIN, Constants.REQUEST_FAILURE, message));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        EventBus.getDefault().post(new EventClientRequestManager(ConstAction.ACTION_SNLOGIN, Constants.REQUEST_NON));
                    }
                    break;

                case CheckDeviceCodeSuccess:
                    try {
                        JSONObject json = new JSONObject(requestContent);
                        int code = json.optInt("code");
                        if(code ==0){
                            boolean hasCode = json.optBoolean("data");
                            EventBus.getDefault().post(new EventClientRequestManager(ConstAction.ACTION_DEVCIEID_CHECK, Constants.REQUEST_SUEESS, hasCode));
                            return;
                        }else {
                            String message = json.getString("msg");
                            EventBus.getDefault().post(new EventClientRequestManager(ConstAction.ACTION_DEVCIEID_CHECK, Constants.REQUEST_FAILURE, message));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        EventBus.getDefault().post(new EventClientRequestManager(ConstAction.ACTION_DEVCIEID_CHECK, Constants.REQUEST_NON));
                    }
                    break;
                case bindDeviceSuccess:
                    try {
                        JSONObject json = new JSONObject(requestContent);
                        int code = json.optInt("code");
                        if(code ==0){
                            EventBus.getDefault().post(new EventClientRequestManager(ConstAction.ACTION_BIND_DEVICE, Constants.REQUEST_SUEESS));
                            return;
                        }else {
                            String message = json.getString("msg");
                            EventBus.getDefault().post(new EventClientRequestManager(ConstAction.ACTION_BIND_DEVICE, Constants.REQUEST_FAILURE, message));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        EventBus.getDefault().post(new EventClientRequestManager(ConstAction.ACTION_BIND_DEVICE, Constants.REQUEST_NON));
                    }
                    break;


                //请求失败
                case FAILURE:
                    LogUtil.e(TAG, "请求无响应");
                    EventBus.getDefault().post(new EventClientRequestManager(ConstAction.ACTION_LOGIN, Constants.REQUEST_NON));
                    EventBus.getDefault().post(new EventClientRequestManager(ConstAction.ACTION_ALLDEVICES, Constants.REQUEST_NON));
                    break;
            }
        }
    };
}
