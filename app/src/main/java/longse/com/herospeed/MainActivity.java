package longse.com.herospeed;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.xc.p2pVideo.NativeMediaPlayer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import longse.com.herospeed.activity.BaseActivity;
import longse.com.herospeed.base.Constants;
import longse.com.herospeed.base.MyApplication;
import longse.com.herospeed.base.SkipActivity;
import longse.com.herospeed.eventbus.EventFeiendsFragment;
import longse.com.herospeed.eventbus.EventLeftFragment;
import longse.com.herospeed.fragment.AlarmFragment;
import longse.com.herospeed.fragment.CloudFragment;
import longse.com.herospeed.fragment.DevicesFragment;
import longse.com.herospeed.fragment.LeftFragment;
import longse.com.herospeed.fragment.SettingFragment;
import longse.com.herospeed.fragment.SndetailsFragment;
import longse.com.herospeed.utils.LogUtil;
import longse.com.herospeed.utils.SharedUtils;


public class MainActivity extends BaseActivity {

    @BindView(R.id.main_content)
    FrameLayout mainContent;
    @BindView(R.id.main_menu_left)
    FrameLayout mainMenuLeft;
    @BindView(R.id.main_drawerlayout)
    DrawerLayout mainDrawerlayout;
    @BindView(R.id.main_fab)
    FloatingActionButton mainFab;

    private FragmentTransaction ft;
    private long mBackPressed;
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.

    @Override
    protected void initBefore() {
        //注册eventBus
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initView() {
        super.initView();
        ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.main_content, DevicesFragment.newInstance());
        ft.add(R.id.main_content, SndetailsFragment.newInstance());
        ft.add(R.id.main_menu_left, LeftFragment.newInstance());
        ft.commit();
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SharedUtils.getBoolean(context, Constants.CACHE_ISLOGIN, false)) {
            mainFab.setImageResource(R.drawable.add_device);
        } else {
            mainFab.setImageResource(R.drawable.top_img);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.d("mainactivity==onDestroy");
        EventBus.getDefault().unregister(this);
        //清空登录存储状态
        SharedUtils.deleShare(context, Constants.CACHE_COOKIE);
        SharedUtils.deleShare(context, Constants.CACHE_USERINF);
        SharedUtils.putBoolean(context, Constants.CACHE_ISLOGIN, false);
        NativeMediaPlayer.JniAppExit();
    }

    @OnClick(R.id.main_fab)
    public void onClick() {
        if (SharedUtils.getBoolean(context, Constants.CACHE_ISLOGIN, false)) {
            SkipActivity.startBindDeviceActivity(context);
        } else {
            SkipActivity.startSignInActivity(context);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(EventFeiendsFragment event) {
        if (event.getMsg() == Constants.SHOWMENU) {
            mainDrawerlayout.openDrawer(mainMenuLeft);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Left(EventLeftFragment event) {
        switch (event.getMsg()) {
            case Constants.MENUFRIENDS:
                ft = getSupportFragmentManager().beginTransaction();
                LogUtil.d("fragment===", String.valueOf(getSupportFragmentManager().getFragments()));
                LogUtil.d("fragment===", String.valueOf(SndetailsFragment.newInstance()));
                ft.replace(R.id.main_content, SndetailsFragment.newInstance());
                ft.commit();
                mainDrawerlayout.closeDrawers();
                break;
            case Constants.MENUDEVICE:
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.main_content, DevicesFragment.newInstance());
                ft.commit();
                mainDrawerlayout.closeDrawers();
                break;
            case Constants.MENUPUSH:
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.main_content, AlarmFragment.newInstance());
                ft.commit();
                mainDrawerlayout.closeDrawers();
                break;
            case Constants.MENUCLOUD:
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.main_content, CloudFragment.newInstance());
                ft.commit();
                mainDrawerlayout.closeDrawers();
                break;

            case Constants.MENUSETING:

                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.main_content, SettingFragment.newInstance());
                ft.commit();
                mainDrawerlayout.closeDrawers();

                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            MyApplication app = (MyApplication) getApplication();
            app.finishAll(context);
            return;
        } else {
            Toast.makeText(getBaseContext(), "Click again on the return key to exit", Toast.LENGTH_SHORT).show();
        }
        mBackPressed = System.currentTimeMillis();
    }
}
