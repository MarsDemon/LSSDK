package longse.com.learing.utils;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;

public class PhoneSwitchUtils {

    private static final String TAG = "PhoneSwitchUtils";

    // 获取屏幕的自动旋转状态
    public static boolean getRotationStatus(Context context) {
        int status = 0;
        try {
            status = Settings.System.getInt(context.getContentResolver(),
                    Settings.System.ACCELEROMETER_ROTATION);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return status == 1;
    }

    // 设置屏幕的自动旋转开关
    public static void setRoutationStatus(Context context, boolean enabled) {
        int status = enabled ? 0 : 1;
        Uri uri = Settings.System.getUriFor("accelerometer_rotation");
        Settings.System.putInt(context.getContentResolver(), "accelerometer_rotation", status);
        context.getContentResolver().notifyChange(uri, null);
    }

    // 设置亮度自动调节的开关
    public static void setAutoBrightStatus(Context context, boolean enabled) {
        int screenMode = enabled ? Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC : Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL;
        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE,
                screenMode);
    }

    // 获取亮度自动调节状态
    public static boolean getAutoBrightStatus(Context context) {
        int screenMode = Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL;
        try {
            screenMode = Settings.System.getInt(context.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS_MODE);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return screenMode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
    }

    // 获取飞行模式的开关状态
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean getAirplaneStatus(Context context) {
        boolean status = Settings.System.getInt(context.getContentResolver(),
                Settings.Global.AIRPLANE_MODE_ON, 0) == 1;
        return status;
    }

    // 获取蓝牙的开关状态
    public static boolean getBlueToothStatus(Context context) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        boolean enabled = false;
        switch (bluetoothAdapter.getState()) {
            case BluetoothAdapter.STATE_ON:
            case BluetoothAdapter.STATE_TURNING_ON:
                enabled = true;
                break;
            case BluetoothAdapter.STATE_OFF:
            case BluetoothAdapter.STATE_TURNING_OFF:
            default:
                enabled = false;
                break;
        }
        return enabled;
    }

    // 获取 WiFi 开关状态
    public static boolean getWifiStatus(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        assert wifiManager != null;
        return wifiManager.isWifiEnabled();
    }

    private static Camera mCamera = null;

    //获取闪光灯/手电筒的开关状态
    public static boolean getFlashStatus(Context context) {
        if (mCamera == null) {
            mCamera = Camera.open();
        }
        Parameters parameters = mCamera.getParameters();
        String flashMode = parameters.getFlashMode();
        boolean enabled;
        if (flashMode.equals(Parameters.FLASH_MODE_TORCH)) {
            enabled = true;
        } else {
            enabled = false;
        }
        return enabled;
    }

    //打开或关闭闪光灯/手电筒
    public static void setFlashStatus(Context context, boolean enabled) {
        if (mCamera == null) {
            mCamera = Camera.open();
        }
        Parameters parameters = mCamera.getParameters();
        if (enabled == true) {
            parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);// 开启
            mCamera.setParameters(parameters);
        } else {
            parameters.setFlashMode(Parameters.FLASH_MODE_OFF);// 关闭
            mCamera.setParameters(parameters);
            mCamera.release();
            mCamera = null;
        }
    }


}
