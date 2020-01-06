package longse.com.herospeed.fragment;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import longse.com.herospeed.R;
import longse.com.herospeed.base.Constants;
import longse.com.herospeed.base.SkipActivity;
import longse.com.herospeed.eventbus.EventClientRequestManager;
import longse.com.herospeed.eventbus.EventFeiendsFragment;
import longse.com.herospeed.popupWindow.MainRightPopWindow;
import longse.com.herospeed.tools.BarTools;
import longse.com.herospeed.utils.LogUtil;
import longse.com.herospeed.utils.SharedUtils;
import longse.com.herospeed.widget.TitleView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends BaseFragment {

    @BindView(R.id.setting_title)
    TitleView settingTitle;
    @BindView(R.id.friends_bar_statue)
    View friendsBarStatue;
    @BindView(R.id.web_select)
    Spinner webSelect;
    @BindView(R.id.plat_select)
    Spinner platSelect;
    @BindView(R.id.setting_complete)
    AppCompatButton settingComplete;

    private MainRightPopWindow rightPopWindow;

    private static SettingFragment settingFragment;
    private String webName, platName;
    public static String totalURL;

    public static SettingFragment newInstance() {
        if (settingFragment == null) {
            settingFragment = new SettingFragment();
        }
        return settingFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, null);
        ButterKnife.bind(this, view);
        //注册eventBus
        EventBus.getDefault().register(this);
        webSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] languages = getResources().getStringArray(R.array.service_type);
//                Toast.makeText(getContext(), languages[position], Toast.LENGTH_SHORT).show();
                platName = languages[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        platSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] languages = getResources().getStringArray(R.array.service_type);
//                Toast.makeText(getContext(), languages[position], Toast.LENGTH_SHORT).show();
                webName = languages[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
        initListener();
    }

    private void initView() {
        settingTitle.setRightIcon(R.drawable.point);
        settingTitle.setLeftIcon(R.drawable.menu_img);

        //取控件textView当前的布局参数 linearParams.height = 20;// 控件的高强制设成20
        RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) friendsBarStatue.getLayoutParams();
        // 控件的宽强制设成30
        linearParams.height = BarTools.getActionBarHeight(getActivity()) / 2;
        //使设置好的布局参数应用到控件
        friendsBarStatue.setLayoutParams(linearParams);

        rightPopWindow = new MainRightPopWindow(getActivity());
    }

    private void initListener() {
        settingTitle.setLeftIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new EventFeiendsFragment(Constants.SHOWMENU));
            }
        });

        settingTitle.setRightIconOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                rightPopWindow.showAtLocation(settingTitle, Gravity.TOP | Gravity.RIGHT, 0, 0);
            }
        });

        rightPopWindow.setAddFriendsAdnDeviceOnClickLicener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rightPopWindow.isShowing()) {
                    rightPopWindow.dismiss();
                }
                SkipActivity.startSearchActivity(getActivity());
            }
        });

        rightPopWindow.setSignOutOnClickLicener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rightPopWindow.isShowing()) {
                    rightPopWindow.dismiss();
                }
            }
        });
    }
    //加州：52.52.7.160    //52.28.117.1 法兰克福    //52.220.173.92  新加坡   //52.80.27.63   北京

    @OnClick(R.id.setting_complete)
    public void onViewClicked() {
        checkData();
    }

    private void checkData() {
        if (TextUtils.isEmpty(platName) || TextUtils.isEmpty(webName)) {
            Toast.makeText(getContext(), "请选择服务器地址", Toast.LENGTH_SHORT).show();
            return;
        }
        selectUrl(platName, webName);
    }

    private void selectUrl(String name, String name2) {
        if (TextUtils.isEmpty(name)) {
            totalURL = null;
        }
        LogUtil.e("name--------", name);
        if (name.equals("Beijing")) {
            totalURL = "http://52.80.27.63/ys/index.php?r=common/";
        } else if (name.equals("Singapore")) {
            totalURL = "http://52.220.173.92/ys/index.php?r=common/";
        } else if (name.equals("California")) {
            totalURL = "http://52.52.7.160/ys/index.php?r=common/";
        } else if (name.equals("Frankfurt")) {
            totalURL = "http://52.28.117.1/ys/index.php?r=common/";
        }
        LogUtil.e("name2--------", name2);
        if (name2.equals("Beijing")) {
            //设置webService
        }
        LogUtil.e("selectUrl------", totalURL);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void clientManager(EventClientRequestManager event) {
        LogUtil.e("SndetailsFragment==", event.toString());
        if (event == null) return;
        switch (event.getMsg()) {
            case Constants.REQUEST_SUEESS:
                break;
            case Constants.REQUEST_FAILURE:
                break;
            case Constants.REQUEST_NON:
                dismissProgress();
                Toast.makeText(getContext(), "网络或服务器异常", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
