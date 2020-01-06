package longse.com.learing.welcomelunchmedia;

import android.media.MediaPlayer;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import butterknife.BindView;
import butterknife.OnClick;
import longse.com.herospeed.R;
import longse.com.herospeed.activity.BaseActivity;

public class WelcomeActivity extends BaseActivity {

    @BindView(R.id.welcome_videoview)
    VideoView mWelcomeVideoview;
    @BindView(R.id.welcome_button)
    Button mWelcomeButton;

    @Override
    protected void initBefore() {

    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void initData() {
        super.initData();
        mWelcomeVideoview.setVideoURI(Uri.parse("android.resource://"+this.getPackageName()+"/"+R.raw.kr36));
        mWelcomeVideoview.start();
        mWelcomeVideoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mWelcomeVideoview.start();
            }
        });
        mWelcomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mWelcomeVideoview.isPlaying()){
                    mWelcomeVideoview.stopPlayback();
                    mWelcomeVideoview=null;
                }
            }
        });
    }

    @OnClick(R.id.welcome_button)
    public void onViewClicked() {
    }
}
