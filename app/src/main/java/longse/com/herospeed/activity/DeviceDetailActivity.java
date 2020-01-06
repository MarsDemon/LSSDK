package longse.com.herospeed.activity;

import android.content.Intent;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import longse.com.herospeed.R;
import longse.com.herospeed.bean.DeviceInfoBean;
import longse.com.herospeed.eventbus.EventClientRequestManager;
import longse.com.herospeed.utils.LogUtil;
import longse.com.herospeed.widget.TitleView;

public class DeviceDetailActivity extends BaseActivity {
    @BindView(R.id.device_detail_title)
    TitleView deviceDetailTitle;
    private DeviceInfoBean deviceBean;

    @BindView(R.id.device_info)
    TextView deviceInfo;

    @Override
    protected void initBefore() {
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initView() {
        super.initView();
        deviceDetailTitle.setLeftFinish(this);
        deviceDetailTitle.setRightIconVisibility(false);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_device_detail;
    }

    @Override
    protected void initData() {
        super.initData();
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("devceinfo")) {
                deviceBean = (DeviceInfoBean) intent.getSerializableExtra("devceinfo");
                LogUtil.e("deviceBean------", String.valueOf(deviceBean));
            }
        }
        if (deviceBean == null) {
            finish();
            return;
        }
        deviceInfo.setText(deviceBean.toString());

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void clientManager(EventClientRequestManager event) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
