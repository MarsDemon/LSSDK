package longse.com.herospeed.tools.scanner;

import android.os.Handler;
import android.os.Message;

import com.google.zxing.Result;

import longse.com.herospeed.activity.SearchActivity;


/**
 * Author: Vondear
 * 描述: 扫描消息转发
 */
public final class CaptureActivityHandler extends Handler {

    DecodeThread decodeThread = null;
    SearchActivity activity = null;
    private State state;

    public CaptureActivityHandler(SearchActivity activity) {
        this.activity = activity;
        decodeThread = new DecodeThread(activity);
        decodeThread.start();
        state = State.SUCCESS;
        CameraManager.get().startPreview();
        restartPreviewAndDecode();
    }

    @Override
    public void handleMessage(Message message) {
        if (message.what == longse.com.herospeed.R.id.auto_focus) {
            if (state == State.PREVIEW) {
                CameraManager.get().requestAutoFocus(this, longse.com.herospeed.R.id.auto_focus);
            }
        } else if (message.what == longse.com.herospeed.R.id.restart_preview) {
            restartPreviewAndDecode();
        } else if (message.what == longse.com.herospeed.R.id.decode_succeeded) {
            state = State.SUCCESS;
            activity.handleDecode((Result) message.obj);// 解析成功，回调
        } else if (message.what == longse.com.herospeed.R.id.decode_failed) {
            state = State.PREVIEW;
            CameraManager.get().requestPreviewFrame(decodeThread.getHandler(), longse.com.herospeed.R.id.decode);
        }
    }

    public void quitSynchronously() {
        state = State.DONE;
        CameraManager.get().stopPreview();
        removeMessages(longse.com.herospeed.R.id.decode_succeeded);
        removeMessages(longse.com.herospeed.R.id.decode_failed);
        removeMessages(longse.com.herospeed.R.id.decode);
        removeMessages(longse.com.herospeed.R.id.auto_focus);
    }

    private void restartPreviewAndDecode() {
        if (state == State.SUCCESS) {
            state = State.PREVIEW;
            CameraManager.get().requestPreviewFrame(decodeThread.getHandler(), longse.com.herospeed.R.id.decode);
            CameraManager.get().requestAutoFocus(this, longse.com.herospeed.R.id.auto_focus);
        }
    }

    private enum State {
        PREVIEW, SUCCESS, DONE
    }

}
