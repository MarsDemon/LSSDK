package longse.com.learing.utils;

import android.hardware.Camera;

import java.util.List;

public class CameraUtils {

    public static List getCameraParams() {
        //CameraInfo.CAMERA_FACING_BACK //后置摄像头
        //CameraInfo.CAMERA_FACING_FRONT//前置摄像头
        Camera mCamera = Camera.open();
        Camera.Parameters parameters = mCamera.getParameters();

        List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();


        



        return sizes;
    }

}
