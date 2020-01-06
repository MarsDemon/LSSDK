package com.xc.p2pVideo;

import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Message;

import longse.com.herospeed.base.Constants;
import longse.com.herospeed.bean.FrameSize;
import longse.com.herospeed.bean.JniResponseBean;
import longse.com.herospeed.bean.PostCurrTime;
import longse.com.herospeed.utils.LogUtil;

public class NativeMediaPlayer {

    private static Handler handler;

    static {
        try {
            System.loadLibrary("avutil-55");
            System.loadLibrary("swresample-2");
            System.loadLibrary("avcodec-57");
            System.loadLibrary("avformat-57");
            System.loadLibrary("swscale-4");
            System.loadLibrary("gnustl_shared");
            System.loadLibrary("NewFreeipCloudAppSdk");
            System.out.println("Load Library suucess!!");

        } catch (Exception e) {

            System.out.println("NativeMediaPlayer==Load Library error!!");
        }
    }

    public NativeMediaPlayer(Handler handler) {
        /*
		 * Native setup requires a weak reference to our object. It's easier to
		 * create it here than in C++.
		 */
        NativeMediaPlayer.handler = handler;

        System.out.println("NativeMediaPlayer==class is " + handler.getClass());

        //mGLSurfaceView = glView;
    }

    // ?????????????
    public static int drawFrame(Object mGLSurfaceView) {
        // System.out.println(" draw frame!!");
        if (mGLSurfaceView == null) return 0;//出现 NullPointerException ；加了这行代码
        ((GLSurfaceView) mGLSurfaceView).requestRender();
        return 0;
    }

    public static native int JniInitClassToJni();

    public static native int JniAppInit(String regionServerip, String userName);

    public static native void JniAppExit();

    public static native int JniSendJsonReqToPlatform(String deviceId, int cmdId, String json_req);

    public static native int JniSendJsonReqToPlatformV2(String device_id, String json_req);

    public native static int JniCreateVideoPlayer(Object playerInstance_this, Object surfaceView, int dataMode, String rtspUrl, String id, int channelId, int maxChannelId, int windowId, int streamId, String userName, String privateName, String privatePwd, String priveteServer, int privatePort, int deviceType);

    public native static int JniVideoPlay(int playId, String reqjson);

    public static native int JniVideoStreamExchange(int videoPlayId, String closejson, String openjson);

    public native static int JniOpenAudio(int playId, String reqJson);

    public native static int JniCloseAudio(int playId, String reqJson);

    public native static int JniOpenVoiceTalk(int playId, String json);

    public native static int JniCloseVoiceTalk(int playId, String json);

    public native static int JniCloseVideoPlay(int playId, String reqjson);

    public native static int JniVideoPlayerStartRecord(int playId, String fileName);

    public native static int JniVideoPlayerStopRecord(int playId);

    public native static int JniToMp4File(String inFilePath, String outFilePath, int playerid);


    public native static int JniPTZCmdControl(String deviceId, String reqjson);//???????

    public native static int JniGetRecordSearchReq(String peerid, String reqJson); //jstring peerid, jint channelid,   int timeZoom,

    public native static String JniGetRecordSearchRsp(String peerid, int channelId, String userName, int playerId);

    public native static int JniPlayRecordStream(int playerId, String reqjson);//int timeZoom,

    public native static int JniSkipRecordStream(int playerId, String reqjson);//int timeZoom,

    public native static int JniStopRecordStream(int playId, String reqjson);

    public native static int JniGetDeviceStreamStates(String deviceId, String reqjson);

    public static native void JniAppClassExist();

    public native static int JniStartDirectVideo(int playerId);

    public native static int JniGetPrivateData(int playerId);

    public native static void JniWifiLinkSendOpen(String arg1, String arg2, String arg3);

    public native static void JniWifiLinkSendClose();

    public native static void JniWifiLinkSendStart();

    public native static void JniWifiLinkSendStop();

    public native static String JniGetLocalDevices();

    public native static int JniSendLocalJsonReqToPlatform(String deviceId, int cmdid, String json_req);

    public native static int JniSendLocalJsonReqToPlatformV2(String deviceId, String json_req);

    public native static int JniLocalVideoPlay(int playerId, String json_req);

    public native static int JniLocalOpenAudio(int playerId, String reqjson);

    public native static int JniCloseLocalVideoPlayer(int playerId, String reqjson);

    public native static int JniGetLocalDeviceStreamStates(String peerId, String reqjson);

    public native static int JniPTZLocalCmdControl(String peerId, String reqjson);

    public native static int JniLocalCloseAudio(int playerId, String reqjson);

    public native static int JniDeleteAllLocalDevices();

    public static native int JniLocalVideoStreamExchange(int videoPlayId, String closejson, String openjson);

    public native static int JniSkipLocalRecordStream(int playerId, String reqjson);//int timeZoom,

    public native static int JniStopLocalRecordStream(int playId, String reqjson);

    public native static int JniLocalPlayRecordStream(int playerId, String reqjson);//int timeZoom,

    public native static int JniGetLocalRecordSearchReq(String peerid, String reqJson); //jstring peerid, jint channelid,   int timeZoom,

    public native static int JniOpenLocalVoiceTalk(int playId, String json);

    public native static int JniCloseLocalVoiceTalk(int playId, String json);

    public native static int JniDeleteDevLocalSession(String deviceId);

    public native static int JniDeleteDevSession(String deviceId);


    public static void NativePostEventString(int what, String arg, int winId) {
        switch (what) {
            case 906:

                PostCurrTime curTime = new PostCurrTime();
                curTime.setCurrentTime(arg);
                curTime.setPlayerId(winId);

                Message m = new Message();
                m.what = Constants.GETCURRTIME;
                m.obj = curTime;
                handler.sendMessage(m);

                break;

            case 907:

                System.out.println("NativeMediaPlayer==get 907........................." + arg);

                if (handler == null) {
                    System.out.println("NativeMediaPlayer==handler is null");
                }
                System.out.println("NativeMediaPlayer==get 907.........................end");
                break;

            case 910:

                if (arg == null) {
                    System.out.println("NativeMediaPlayer==get 910......arg is null.......");
                }

                JniResponseBean response = new JniResponseBean();
//
                response = response.getPaserResponse(arg);
                LogUtil.debugLog("NativeMediaPlayer==jni method.............msg = " + "    arg is " + arg);

                if (handler == null) {
                    LogUtil.debugLog("NativeMediaPlayer==handler is null......");
                }

                if (handler != null) {
                    Message me = new Message();
                    me.obj = response;
                    me.what = Constants.JNIRESPONSECODE;
                    handler.sendMessage(me);
//                    PlatformManager.getInstance().handlerMsg(me);

                    LogUtil.debugLog("NativeMediaPlayer==send handler......");
                }


                break;

            default:
                break;
        }
    }

    public static void NativePostEvent(int what, int arg, int winId) { // arg:
        switch (what) {
            case 900: // connect failured
                if (arg == 0) {
                    Message m = new Message();
                    m.what = Constants.CONNFILURE;
                    m.obj = winId;
                    System.out.println("NativeMediaPlayer==连接失败~~~" + winId);
                    handler.sendMessage(m);
                }
                break;
            case 901: // decode success
                if (arg == 1) {
                    Message mess = new Message();
                    mess.what = Constants.DECODESUCC;
                    mess.obj = winId;
                    System.out.println("NativeMediaPlayer==windowId解码成功~~~" + winId);
                    handler.sendMessage(mess);
                }
                break;
            case 902: //?л????????
                System.out.println("NativeMediaPlayer==?л??????????:" + arg);
                if (arg == 1) {  //change stream success

                } else { //change stream failure

                }
                break;
            case 903:
			/*StreamData streamData=new StreamData();
			streamData.setPlayerId(winId);
			streamData.setStreamData(arg);
			Message m=new Message();
			m.what=SystemConstants.STREAMDATA;
			m.obj=streamData;
			handler.sendMessage(m);*/
                break;
            case 904:
                if (arg == 0) {
                    System.out.println("NativeMediaPlayer==?????????????:" + arg);
                    Message msg = new Message();
                    msg.what = Constants.GETRECORDTIME;
                    msg.obj = winId;
                    handler.sendMessage(msg);
                } else if (arg == 1) {  //????????
                    Message msg = new Message();
                    msg.what = Constants.NORECORDINFO;
                    msg.obj = winId;
                    handler.sendMessage(msg);
                } else if (arg == 2) {
                    Message msg = new Message();
                    msg.what = Constants.GETRECODINFOERROR;
                    msg.obj = winId;
                    handler.sendMessage(msg);
                } else {
                    Message msg = new Message();
                    msg.what = Constants.PLAYRECORDSTREAM;
                    msg.obj = winId;
                    handler.sendMessage(msg);
                }
                break;

            case 905:

                if (arg == 0) {
                    Message message = new Message();
                    message.what = Constants.PLAYNEXTRECORD;
                    handler.sendMessage(message);
                } else if (arg == 1) {
                    Message message = new Message();
                    message.what = Constants.GETRECORDOVER;
                    message.obj = winId;
                    handler.sendMessage(message);
                }
                LogUtil.d("NativeMediaPlayer====NativeMediaPlayer===", arg + "");

                break;

            case 906:

                Message me = new Message();
                me.what = Constants.PLAYNEXTVIDEO;
                me.obj = winId;
                handler.sendMessage(me);

                System.out.println("NativeMediaPlayer==playnext vide playerId:::::::::" + winId);

                break;

            case 908:
                System.out.println("NativeMediaPlayer==not support device playerid ==" + winId);
                Message m = new Message();
                m.what = Constants.NOTSUPPORT;
                m.obj = winId;
                handler.sendMessage(m);

                break;

            case 909:

                FrameSize frame = new FrameSize();
                frame.setFrameWidth(arg);
                frame.setFrameHeight(winId);

                Message mes = new Message();
                mes.what = Constants.POSTFRAMESIZE;
                mes.obj = frame;
                handler.sendMessage(mes);
                System.out.println("NativeMediaPlayer==............frameHeight ==" + winId + "::::frameWidth==" + arg);

                break;

            case 911:  // need reconnect

		         System.out.println("NativeMediaPlayer==player##connect###" + "player id is :::"+winId +"arg is "+arg);


                if (handler != null) {
                    Message reconn = Message.obtain();
                    reconn.what = 4040;
                    reconn.arg1 = winId;
                    handler.sendMessage(reconn);
                }

                //play end
                if (handler != null && arg == 2) {
                    Message playEnd = Message.obtain();
                    playEnd.what = 20171102;
                    handler.sendMessage(playEnd);
                }

                break;

        }

    }

    public static void UpdataFrameSize(int frameWidth, int frameHeight, int playerId) {

        System.out.println("............frameHeight ==" + frameHeight + "::::frameWidth==" + frameWidth + ":::playerId:==" + playerId);

        FrameSize frameSize = new FrameSize();

        frameSize.setFrameHeight(frameHeight);
        frameSize.setFrameWidth(frameWidth);
        frameSize.setPlayerId(playerId);

        Message m = new Message();
        m.what = Constants.POSTFRAMESIZE;
        m.obj = frameSize;
        handler.sendMessage(m);

        System.out.println("--------------------------------------------");
    }

    public int NativeCreateMediaPlayer(GLSurfaceView glView, int dataMode, String rtspUrl, String id, int channelId, int maxChannelId, int windowId, int streamId, String userName, String privateName, String privatePwd, String privateServer, int privatePort, int deviceType) {
        return JniCreateVideoPlayer(this, glView, dataMode, rtspUrl, id,
                channelId, maxChannelId, windowId, streamId, userName, privateName, privatePwd, privateServer, privatePort, deviceType);
    }

}
