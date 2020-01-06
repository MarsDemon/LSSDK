package longse.com.herospeed.activity;

import org.greenrobot.eventbus.EventBus;

import longse.com.herospeed.R;

public class ScreenChangeActivity extends BaseActivity {

    @Override
    protected void initBefore() {
        EventBus.getDefault().register(this);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_screen_change;
    }
}
