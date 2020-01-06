package longse.com.herospeed.base;

import android.content.Context;
import android.content.Intent;

import longse.com.herospeed.activity.BindDeviceActivity;
import longse.com.herospeed.activity.DeviceDetailActivity;
import longse.com.herospeed.activity.FindPwdActivity;
import longse.com.herospeed.activity.SearchActivity;
import longse.com.herospeed.activity.SignInActivity;
import longse.com.herospeed.activity.SignUpActivity;
import longse.com.herospeed.bean.DeviceInfoBean;

/**
 * Created by LY on 2017/10/25.
 */

public class SkipActivity {

    public static void startSignInActivity(Context context) {
        Intent i = new Intent(context, SignInActivity.class);
        context.startActivity(i);
    }

    public static void startSignUpActivity(Context context) {
        Intent i = new Intent(context, SignUpActivity.class);
        context.startActivity(i);
    }

    public static void startSearchActivity(Context context) {
        Intent i = new Intent(context, SearchActivity.class);
        context.startActivity(i);
    }

    public static void startFindPwdActivity(Context context, String email) {
        Intent i = new Intent(context, FindPwdActivity.class);
        i.putExtra("email", email);
        context.startActivity(i);
    }
    public static void startDeviceDetailActivity(Context context, DeviceInfoBean bean) {
        Intent i = new Intent(context, DeviceDetailActivity.class);
        i.putExtra("devceinfo", bean);
        context.startActivity(i);
    }

    public static void startBindDeviceActivity(Context context) {
        Intent i = new Intent(context, BindDeviceActivity.class);
        context.startActivity(i);
    }
}
