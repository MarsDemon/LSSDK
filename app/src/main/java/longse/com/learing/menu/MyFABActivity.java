package longse.com.learing.menu;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import longse.com.herospeed.R;
import longse.com.herospeed.activity.BaseActivity;

public class MyFABActivity extends BaseActivity implements TextToSpeech.OnInitListener {

    private static final int CODE = 100;

    @BindView(R.id.text)
    TextView mText;
    private TextToSpeech mTts;

    @Override
    protected void initBefore() {

    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_my_fab;
    }

    @Override
    protected void initView() {
        super.initView();
        checkTTS();
    }

    @Override
    protected void initData() {
        super.initData();
        mTts = new TextToSpeech(this, this);
    }

    @OnClick(R.id.text)
    public void onClick() {
        initSpeech("Hello world  语音播报功能");
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = mTts.setLanguage(Locale.SIMPLIFIED_CHINESE);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                mText.setEnabled(false);
                Log.e("TAG", "Language is not available");
            } else {
                //TTS引擎已经成功初始化
                mText.setEnabled(true);
            }
        } else {
            // 初始化失败
            Log.e("TAG", "Could not initialize TextToSpeech.");
        }
    }

    /**
     * 检查TTS是否可以使用
     */
    private void checkTTS() {
        Intent in = new Intent();
        in.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(in, CODE);
    }

    public boolean isActivityCallable(String packageName, String className) {
        final Intent intent = new Intent();
        intent.setClassName(packageName, className);
        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    private void initSpeech( String contextStr ){
        if( mTts != null && !mTts.isSpeaking()){
            mTts.setPitch(1.0f); // 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
            mTts.speak( contextStr ,TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE) {
            System.out.println("MyFABActivity.onActivityResult == " + resultCode);
            switch (resultCode) {
                case TextToSpeech.Engine.CHECK_VOICE_DATA_PASS:
                    Toast.makeText(this, "恭喜您，TTS可用", Toast.LENGTH_SHORT).show();
                    mTts = new TextToSpeech(this, this);
                    break;
                case TextToSpeech.Engine.CHECK_VOICE_DATA_BAD_DATA:// 发音数据已经损坏
                case TextToSpeech.Engine.CHECK_VOICE_DATA_MISSING_DATA: // 需要的语音数据已丢失
                case TextToSpeech.Engine.CHECK_VOICE_DATA_MISSING_VOLUME: // 发音数据丢失
                    // 下载TTS对应的资源
                    Intent dataIntent = new Intent(
                            TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                    startActivity(dataIntent);
                    break;
                case TextToSpeech.Engine.CHECK_VOICE_DATA_FAIL:
                    // 发音失败
                    break;

            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTts != null) {
            // 停止TextToSpeech
            mTts.stop();
            //释放 TextToSpeech占用的资源
            mTts.shutdown();
        }

    }
}
