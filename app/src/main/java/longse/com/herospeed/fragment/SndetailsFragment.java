package longse.com.herospeed.fragment;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputEditText;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.longse.freeipfunction.manager.RequestManager;
import com.xc.hdscreen.view.GLPlayView;
import com.xc.p2pVideo.NativeMediaPlayer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import longse.com.herospeed.R;
import longse.com.herospeed.base.ConstAction;
import longse.com.herospeed.base.Constants;
import longse.com.herospeed.bean.DeviceInfoBean;
import longse.com.herospeed.bean.JniResponseBean;
import longse.com.herospeed.eventbus.EventClientRequestManager;
import longse.com.herospeed.eventbus.EventFeiendsFragment;
import longse.com.herospeed.tools.BarTools;
import longse.com.herospeed.utils.JniRequestUtils;
import longse.com.herospeed.utils.LogUtil;
import longse.com.herospeed.utils.SharedUtils;
import longse.com.herospeed.widget.TitleView;

/**
 * Created by LY on 2017/10/25.
 */

public class SndetailsFragment extends BaseFragment {

    @BindView(R.id.friends_title)
    TitleView friendsTitle;
    @BindView(R.id.friends_bar_statue)
    View friendsBarStatue;
    @BindView(R.id.login_linear)
    LinearLayoutCompat loginLinear;
    @BindView(R.id.output_info)
    TextView outputInfo;
    @BindView(R.id.output_info_scrol)
    NestedScrollView outputInfoScrol;
    @BindView(R.id.serial_num)
    TextInputEditText serialEd;
    @BindView(R.id.serial_account)
    TextInputEditText accountEd;
    @BindView(R.id.serial_pwd)
    TextInputEditText pwdEd;
    @BindView(R.id.serail_complete)
    AppCompatButton login;
    @BindView(R.id.play_view)
    FrameLayout playView;


    private GLPlayView glPlay;

    private static final String TAG = "SndetailsFragment";

    private static SndetailsFragment friendsFragment;
    private String serial, account, pwd;
    private NativeMediaPlayer nativePlayer;
    private DeviceInfoBean infoBean;
    private MyHandler handler;

    public static SndetailsFragment newInstance() {
        if (friendsFragment == null) {
            friendsFragment = new SndetailsFragment();
        }
        return friendsFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, null);
        ButterKnife.bind(this, view);
        //注册eventBus
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initListener();
    }

    private void initView() {
        handler = new MyHandler(Looper.myLooper());
        nativePlayer = new NativeMediaPlayer(handler);

        serialEd.setText(SharedUtils.getString(getContext(), Constants.CACHE_SN_DEVICE_ID, ""));
        accountEd.setText(SharedUtils.getString(getContext(), Constants.CACHE_SN_ACCOUNT, ""));
        pwdEd.setText(SharedUtils.getString(getContext(), Constants.CACHE_SN_PWD, ""));

        friendsTitle.setRightIcon(R.drawable.change_mode);
        friendsTitle.setLeftIcon(R.drawable.menu_img);
        friendsTitle.setRightIconVisibility(false);

        //取控件textView当前的布局参数 linearParams.height = 20;// 控件的高强制设成20
        RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) friendsBarStatue.getLayoutParams();
        // 控件的宽强制设成30
        linearParams.height = BarTools.getActionBarHeight(getActivity()) / 2;
        //使设置好的布局参数应用到控件
        friendsBarStatue.setLayoutParams(linearParams);
    }

    private void initListener() {
        friendsTitle.setLeftIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new EventFeiendsFragment(Constants.SHOWMENU));
            }
        });

        friendsTitle.setRightIconOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if (friendsTitle.getTitle().equals(getString(R.string.leftsn))) {
                    friendsTitle.setTitle(getString(R.string.play_back));
                    //Sn回放
                    showProgress();
                    creatVideoPlayer();
                    loadDeviceRecordStream(infoBean);
                    outputInfo.setText("");
                } else {
                    //Sn登录
                    friendsTitle.setTitle(getString(R.string.leftsn));
                    snLogin();
                }
            }
        });
    }

    private void creatVideoPlayer() {
        glPlay = new GLPlayView(getContext(), 1, 10, 10, handler,
                infoBean.getEqupId());
        playView.addView(glPlay);
        nativePlayer.NativeCreateMediaPlayer(glPlay, 2, "rtspurl",
                infoBean.getEqupId(), 0, infoBean.getChannelSum(),
                1, 1, infoBean.getEqupId(),
                "privateName", "privatePwd", "privateServer", 0,
                0);
    }

    /**
     * 加载码流信息
     *
     * @param info 设备信息
     */
    private void loadDeviceStream(DeviceInfoBean info) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put(ConstAction.JNIRequestKey, ConstAction.JNIRequestStreamInfoCmd);
        param.put("device_id", info.getEqupId());
        param.put("channel_id", "0");
        param.put("dev_username", info.getLocalUser());
        param.put("dev_passwd", info.getLocalPwd());
        NativeMediaPlayer.JniGetDeviceStreamStates(info.getEqupId(), JniRequestUtils.getRequestToJni(param));
    }

    private void loadDeviceRecordStream(DeviceInfoBean info) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(ConstAction.JNIRequestKey, ConstAction.JNIRequestRearchRecordCmd);
        params.put("device_id", info.getEqupId());
        params.put("channel_id", "0");
        params.put("file_type", 4);
        params.put("time_zone", getTimeZone());
        params.put("start_time", getTimesmorning());
        params.put("dev_username", info.getLocalUser());
        params.put("dev_passwd", info.getLocalPwd());
        params.put("end_time", getTimesnight());

        NativeMediaPlayer.JniGetRecordSearchReq(info.getEqupId(), JniRequestUtils.getRequestToJni(params));
    }

    //获得当天0点时间
    public static long getTimesmorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        LogUtil.d("SndetailsFragment==getTimesmorning", String.valueOf(cal.getTimeInMillis()));
        return cal.getTimeInMillis();
    }

    //获得当天24点时间
    public static long getTimesnight() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 24);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        LogUtil.d("SndetailsFragment==getTimesnight", String.valueOf(cal.getTimeInMillis()));
        return cal.getTimeInMillis() - 1000;
    }

    //获取当前失去时间
    public int getTimeZone() {
        Calendar calendar = new GregorianCalendar();
        int zoneOffset = calendar.get(Calendar.ZONE_OFFSET);
        LogUtil.d("SndetailsFragment==getTimeZone", String.valueOf(zoneOffset / 1000));
        return zoneOffset / 1000;
    }

    private void snLogin() {
        showProgress();
        RequestManager.getInstance().SNDeviceInfoByDid(serial);
    }

    @OnClick(R.id.serail_complete)
    public void complete() {
        if (checkForm()) {
            //sn登录
            snLogin();
        }
    }

    private boolean checkForm() {
        serial = serialEd.getText().toString().trim();
        account = accountEd.getText().toString().trim();
        pwd = pwdEd.getText().toString().trim();

        boolean isPass = true;
        if (TextUtils.isEmpty(serial)) {
            serialEd.setError("Serial cannot be empty");
            isPass = false;
        } else {
            serialEd.setError(null);
        }
        if (TextUtils.isEmpty(account)) {
            serialEd.setError("account cannot be empty");
            isPass = false;
        } else {
            serialEd.setError(null);
        }
        if (TextUtils.isEmpty(pwd)) {
            serialEd.setError("password cannot be empty");
            isPass = false;
        } else {
            serialEd.setError(null);
        }
        return isPass;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    class MyHandler extends Handler {
        private MyHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            if (msg.what == Constants.JNIRESPONSECODE && msg.obj != null && msg.obj instanceof JniResponseBean) {
                JniResponseBean jniResponseBean = (JniResponseBean) msg.obj;
                if (jniResponseBean == null) return;

                LogUtil.e("SndetailsFragment==", jniResponseBean.getType());
                if ("session_status".equals(jniResponseBean.getType())) {
                    loadDeviceStream(infoBean);
                } else if ("stream_info".equals(jniResponseBean.getType())) {
                    dismissProgress();
                    LogUtil.e("SndetailsFragment==", jniResponseBean.toString());
                    outputInfo.setText(jniResponseBean.toString());
                    friendsTitle.setRightIconVisibility(true);
                } else if ("record_search".equals(jniResponseBean.getType())) {
                    LogUtil.e("SndetailsFragment==", jniResponseBean.toString());
                    dismissProgress();
                    outputInfo.setText(jniResponseBean.toString());
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void clientManager(EventClientRequestManager event) {
        LogUtil.e("SndetailsFragment==", event.toString());
        if (event == null) return;
        switch (event.getMsg()) {
            case Constants.REQUEST_SUEESS:
                if (event.getAction().equals(ConstAction.ACTION_SNLOGIN)) {
                    infoBean = (DeviceInfoBean) event.getObject();
                    if (infoBean == null) return;
                    SharedUtils.putString(getContext(), Constants.CACHE_SN_DEVICE_ID, serial);
                    SharedUtils.putString(getContext(), Constants.CACHE_SN_ACCOUNT, account);
                    SharedUtils.putString(getContext(), Constants.CACHE_SN_PWD, pwd);
                    if (!"1".equals(infoBean.getIfOnLine())) {
                        Toast.makeText(getActivity(), R.string.device_unline, Toast.LENGTH_SHORT).show();
                        break;
                    }
                    infoBean.setLocalUser(account);
                    infoBean.setLocalPwd(pwd);
                    loginLinear.setVisibility(View.GONE);
                    outputInfoScrol.setVisibility(View.VISIBLE);
                    loadDeviceStream(infoBean);
                }
                break;
            case Constants.REQUEST_FAILURE:
                dismissProgress();
                if (event.getAction().equals(ConstAction.ACTION_SNLOGIN)) {
                    if (event.getObject() != null) {
                        Toast.makeText(getContext(), (String) event.getObject(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(getContext(), "SN sign in failed", Toast.LENGTH_SHORT).show();
                }
                break;
            case Constants.REQUEST_NON:
                dismissProgress();
                Toast.makeText(getContext(), "The network or server error", Toast.LENGTH_SHORT).show();
                break;
        }

    }

}
