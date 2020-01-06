package longse.com.herospeed.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import longse.com.herospeed.R;
import longse.com.herospeed.interfaces.OnScanerListener;
import longse.com.herospeed.popupWindow.SearchPopWindow;
import longse.com.herospeed.tools.AnimationTool;
import longse.com.herospeed.tools.scanner.BitmapLuminanceSource;
import longse.com.herospeed.tools.scanner.CameraManager;
import longse.com.herospeed.tools.scanner.CaptureActivityHandler;
import longse.com.herospeed.tools.scanner.decoding.InactivityTimer;
import longse.com.herospeed.utils.LogUtil;
import longse.com.herospeed.widget.TitleView;

public class SearchActivity extends BaseActivity implements SurfaceHolder.Callback {

    @BindView(R.id.search_title)
    TitleView searchTitle;
    @BindView(R.id.activity_search)
    RelativeLayout activitySearch;
    @BindView(R.id.capture_scan_line)
    ImageView captureScanLine;
    @BindView(R.id.capture_crop_layout)
    RelativeLayout captureCropLayout;
    @BindView(R.id.right_mask)
    ImageView rightMask;
    @BindView(R.id.qr_flash)
    ImageView qrFlash;
    @BindView(R.id.qr_picture)
    ImageView qrPicture;
    @BindView(R.id.capture_preview)
    SurfaceView capturePreview;
    @BindView(R.id.function_bottom)
    RelativeLayout functionBottom;
    @BindView(R.id.top_mask)
    ImageView topMask;
    @BindView(R.id.left_mask)
    ImageView leftMask;

    private static final float BEEP_VOLUME = 0.50f;
    private final static String ALBUM_PATH = Environment.getExternalStorageDirectory() + File.separator + "fengci/";
    private static final long VIBRATE_DURATION = 200L;
    private static OnScanerListener mScanerListener;

    private CaptureActivityHandler handler;
    private SearchPopWindow searchPopWindow;
    private InactivityTimer inactivityTimer;

    //闪光灯标记
    boolean flag = true;
    private final int CHOOSE_PICTURE = 1003;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private boolean vibrate;
    private int x = 0;
    private int y = 0;
    private int mCropWidth = 0;
    private int mCropHeight = 0;
    private boolean hasSurface;

    //----------------------------------------------------------------------------------------------解析结果 及 后续处理 start
    private String mResult;

    public static void setScanerListener(OnScanerListener scanerListener) {
        mScanerListener = scanerListener;
    }

    @Override
    protected void initBefore() {

        //请求Camera权限 与 文件读写 权限
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        CameraManager.init(context);//初始化 CameraManager
        inactivityTimer = new InactivityTimer(this);
        hasSurface = false;

    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_search;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.v("ServiceActivityon==", "onResume");
        CameraManager.get();
        LogUtil.v("ServiceActivityon==", String.valueOf(hasSurface));
        SurfaceHolder surfaceHolder = capturePreview.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);//Camera初始化
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
    }


    @Override
    protected void onPause() {
        LogUtil.v("ServiceActivityon==", "onPause");
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            CameraManager.get().closeDriver();
            handler = null;
        }
    }

    @Override
    protected void onDestroy() {
        LogUtil.v("ServiceActivityon==", "onDestroy");
        inactivityTimer.shutdown();
        mScanerListener = null;
        super.onDestroy();
    }

    @Override
    protected void initView() {
        super.initView();
        searchPopWindow = new SearchPopWindow(context);
        searchTitle.setLeftFinish(context);
        searchTitle.setRightIcon(R.drawable.search_img);

        initScanerAnimation();//扫描动画初始化
        searchTitle.setRightIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchPopWindow.showAtLocation(activitySearch, Gravity.TOP, 0, 0);
            }
        });
    }

    @OnClick({R.id.qr_flash, R.id.qr_picture})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.qr_flash:
                light();
                break;
            case R.id.qr_picture:
                getPicture();
                break;
        }
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);

            Point point = CameraManager.get().getCameraResolution();
            int width = point.y;
            int height = point.x;
            int x = captureCropLayout.getLeft() * width / activitySearch.getWidth();
            int y = captureCropLayout.getTop() * height / activitySearch.getHeight();
            int cropWidth = captureCropLayout.getWidth() * width
                    / activitySearch.getWidth();
            int cropHeight = captureCropLayout.getHeight() * height
                    / activitySearch.getHeight();
            setX(x);
            setY(y);
            setCropWidth(cropWidth);
            setCropHeight(cropHeight);
        } catch (IOException | RuntimeException ioe) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(SearchActivity.this);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        LogUtil.v("ServiceActivityon==", "surfaceChanged");
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        LogUtil.v("ServiceActivityon==", "surfaceCreated");
        LogUtil.v("ServiceActivityon==surfaceCreated", String.valueOf(hasSurface));
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        LogUtil.v("ServiceActivityon==", "surfaceDestroyed");
        hasSurface = false;

    }

    public Handler getHandler() {
        return handler;
    }

    private void initScanerAnimation() {
        AnimationTool.ScaleUpDowm(captureScanLine);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getCropWidth() {
        return mCropWidth;
    }

    public void setCropWidth(int cropWidth) {
        this.mCropWidth = cropWidth;
        CameraManager.FRAME_WIDTH = mCropWidth;
    }

    public int getCropHeight() {
        return mCropHeight;
    }

    public void setCropHeight(int cropHeight) {
        this.mCropHeight = cropHeight;
        CameraManager.FRAME_HEIGHT = mCropHeight;
    }

    private void light() {
        if (flag) {
            flag = false;
            // 开闪光灯
            CameraManager.get().openLight();
        } else {
            flag = true;
            // 关闪光灯
            CameraManager.get().offLight();
        }
    }

    //==============================================================================================解析结果 及 后续处理 end
    public void handleDecode(Result result) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();

        this.mResult = result.getText();
        LogUtil.v("二维码/条形码 扫描结果", mResult);
        if (mScanerListener == null) {
            Toast.makeText(context, mResult, Toast.LENGTH_SHORT).show();
        } else {
            mScanerListener.onSuccess("From to Camera", result);
        }
    }

    /***
     * 调用系统相册
     */
    private void getPicture() {
        Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
        openAlbumIntent.setType("image/*");
        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
    }

    //==============================================================================================打开本地图片识别二维码 end

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            ContentResolver resolver = getContentResolver();
            // 照片的原始资源地址
            Uri originalUri = data.getData();
            try {
                // 使用ContentProvider通过URI获取原始图片
                Bitmap photo = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                if (photo != null) {
                    Bitmap smallBitmap = zoomBitmap(photo, photo.getWidth() / 2, photo.getHeight() / 2);// 为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
                    photo.recycle(); // 释放原始图片占用的内存，防止out of memory异常发生
//                    String bitmappath = saveFile(smallBitmap, setImageName());

                    MultiFormatReader multiFormatReader = new MultiFormatReader();

                    // 解码的参数
                    Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>(2);
                    // 可以解析的编码类型
                    Vector<BarcodeFormat> decodeFormats = new Vector<BarcodeFormat>();
                    if (decodeFormats == null || decodeFormats.isEmpty()) {
                        decodeFormats = new Vector<BarcodeFormat>();

                        Vector<BarcodeFormat> PRODUCT_FORMATS = new Vector<BarcodeFormat>(5);
                        PRODUCT_FORMATS.add(BarcodeFormat.UPC_A);
                        PRODUCT_FORMATS.add(BarcodeFormat.UPC_E);
                        PRODUCT_FORMATS.add(BarcodeFormat.EAN_13);
                        PRODUCT_FORMATS.add(BarcodeFormat.EAN_8);
                        // PRODUCT_FORMATS.add(BarcodeFormat.RSS14);
                        Vector<BarcodeFormat> ONE_D_FORMATS = new Vector<BarcodeFormat>(PRODUCT_FORMATS.size() + 4);
                        ONE_D_FORMATS.addAll(PRODUCT_FORMATS);
                        ONE_D_FORMATS.add(BarcodeFormat.CODE_39);
                        ONE_D_FORMATS.add(BarcodeFormat.CODE_93);
                        ONE_D_FORMATS.add(BarcodeFormat.CODE_128);
                        ONE_D_FORMATS.add(BarcodeFormat.ITF);
                        Vector<BarcodeFormat> QR_CODE_FORMATS = new Vector<BarcodeFormat>(1);
                        QR_CODE_FORMATS.add(BarcodeFormat.QR_CODE);
                        Vector<BarcodeFormat> DATA_MATRIX_FORMATS = new Vector<BarcodeFormat>(1);
                        DATA_MATRIX_FORMATS.add(BarcodeFormat.DATA_MATRIX);

                        // 这里设置可扫描的类型，我这里选择了都支持
                        decodeFormats.addAll(ONE_D_FORMATS);
                        decodeFormats.addAll(QR_CODE_FORMATS);
                        decodeFormats.addAll(DATA_MATRIX_FORMATS);
                    }
                    hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);
                    // 设置继续的字符编码格式为UTF8
                    // hints.put(DecodeHintType.CHARACTER_SET, "UTF8");
                    // 设置解析配置参数
                    multiFormatReader.setHints(hints);

                    // 开始对图像资源解码
                    Result rawResult = null;
                    try {
                        rawResult = multiFormatReader.decodeWithState(new BinaryBitmap(new HybridBinarizer(new BitmapLuminanceSource(smallBitmap))));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (rawResult != null) {
                        if (mScanerListener == null) {
                            Toast.makeText(context, rawResult.toString(), Toast.LENGTH_SHORT).show();

//                            dialogShow(rawResult);
                        } else {
                            mScanerListener.onSuccess("From to Picture", rawResult);
                        }
                    } else {
                        if (mScanerListener == null) {
                            Toast.makeText(context, "图片识别失败.", Toast.LENGTH_SHORT).show();
//                            RxToast.error("图片识别失败.");
                        } else {
                            mScanerListener.onFail("From to Picture", "图片识别失败");
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Resize the bitmap
     *
     * @param bitmap
     * @param width
     * @param height
     * @return
     */
    public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) width / w);
        float scaleHeight = ((float) height / h);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        return newbmp;
    }


    //----------------------------------------------------------------------------------------------扫描成功之后的振动与声音提示 start
    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.seekTo(0);
                }
            });

            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }
    //==============================================================================================扫描成功之后的振动与声音提示 end

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
