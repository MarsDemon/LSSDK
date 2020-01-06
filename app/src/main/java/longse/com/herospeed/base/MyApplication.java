package longse.com.herospeed.base;

import com.xc.p2pVideo.NativeMediaPlayer;

import org.greenrobot.greendao.AbstractDaoSession;

import longse.com.herospeed.manager.ClientRequestManager;

/**
 * Created by LY on 2017/10/24.
 */

public class MyApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        ClientRequestManager.initClientRequestManager().initRequestManager(this);
        NativeMediaPlayer.JniInitClassToJni();
        NativeMediaPlayer.JniAppInit("52.220.173.92", "123465");
    }
}
