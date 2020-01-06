package longse.com.herospeed.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import longse.com.herospeed.activity.BaseActivity;
import longse.com.herospeed.utils.LogUtil;

import static longse.com.herospeed.fragment.SettingFragment.totalURL;

/**
 * Created by LY on 2017/10/24.
 */

public class BaseApplication extends com.longse.freeipfunction.context.MyApplication {


    protected static Application instance;

    private List<BaseActivity> mActivities = new ArrayList<>();

    /**
     * 改变域名
     *
     * @return
     */
    @Override
    public String getBaseUrl() {
        if (totalURL!=null){
            LogUtil.e("getBaseUrl------",totalURL);
            return totalURL;
        }else {
            return null;
        }
    }

    /**
     * 获取IP
     * @return
     */
    @Override
    public String getIPUrl() {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        instance.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                if (activity instanceof BaseActivity)
                    mActivities.add((BaseActivity) activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                mActivities.remove(activity);
            }
        });
    }

    /**
     * 移除所有的activity
     *
     * @param context 上下文
     */
    public void finishAll(Context context) {
        for (BaseActivity activity : mActivities) {
            LogUtil.e("Acriviry===", activity.toString());
            activity.finish();
        }
    }

    public static Application getInstance() {
        return instance;
    }

}
