package longse.com.learing.audioandmedia.audio;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import longse.com.herospeed.utils.LogUtil;

/**
 * 采集 mic 音频数据
 * 调用方式：AudioCapture audioCapture = new AudioCapture();
 *          audioCapture.startRecord();
 */
public class AudioCapture {

    private static final String TAG = AudioCapture.class.getName();

    // 麦克风
    private final int DEFAULT_SOURCE = MediaRecorder.AudioSource.MIC;
    // 采样率
    private final int DEFAULT_RATE = 44100;
    // 双通道（左右通道）
    private final int DEFAULT_CHANNEL = AudioFormat.CHANNEL_IN_STEREO;
    // 数据位宽16位
    private final int DEFAULT_FORMAT = AudioFormat.ENCODING_PCM_16BIT;

    private AudioRecord mAudioRecord;
    private int mMinBufferSize;
    private onAudioFrameCaptureListener mOnAudioFrameCaptureListener;

    private boolean isRecording = false;


    public void startRecord() {

    }

    public void startRecord(int audioSource, int sampleRateInHz, int channelConfig, int audioFormat) {
        mMinBufferSize = AudioRecord.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);
        if (mMinBufferSize == AudioRecord.ERROR_BAD_VALUE) {
            LogUtil.d(TAG, "Invalid parameter");
            return;
        }

        mAudioRecord = new AudioRecord(audioSource, sampleRateInHz, channelConfig, audioFormat, mMinBufferSize);

        if (mAudioRecord.getState() == AudioRecord.STATE_UNINITIALIZED) {
            LogUtil.d(TAG, "AudioRecord initialize fail");
            return;
        }

        mAudioRecord.startRecording();
        isRecording = true;
        CaptureThread t = new CaptureThread();
        t.start();
        LogUtil.d(TAG, "AudioRecord Start");
    }

    public void stopRecord() {
        isRecording = false;
        if (mAudioRecord.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING) {
            mAudioRecord.stop();
        }
        mAudioRecord.release();
        mOnAudioFrameCaptureListener = null;
        LogUtil.d(TAG, "AudioRecord Stop");
    }

    private class CaptureThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (isRecording) {
                byte[] buffer = new byte[mMinBufferSize];
                int result = mAudioRecord.read(buffer, 0, buffer.length);
                LogUtil.d(TAG, "Capture  " + result + "  byte");
                if (mOnAudioFrameCaptureListener != null) {
                    mOnAudioFrameCaptureListener.onAudioFrameCapture(buffer);
                }
            }
        }
    }

    public interface onAudioFrameCaptureListener {
        void onAudioFrameCapture(byte[] audioData);
    }

    public void setOnAudioFrameCaptureListener(onAudioFrameCaptureListener listener) {
        mOnAudioFrameCaptureListener = listener;
    }

}
