package longse.com.herospeed.fragment;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import longse.com.herospeed.R;
import longse.com.herospeed.base.Constants;
import longse.com.herospeed.base.SkipActivity;
import longse.com.herospeed.eventbus.EventFeiendsFragment;
import longse.com.herospeed.popupWindow.MainRightPopWindow;
import longse.com.herospeed.tools.BarTools;
import longse.com.herospeed.widget.TitleView;

/**
 * A simple {@link Fragment} subclass.
 */
public class CloudFragment extends BaseFragment {

    @BindView(R.id.cloud_title)
    TitleView cloudTitle;
    @BindView(R.id.friends_bar_statue)
    View friendsBarStatue;

    private MainRightPopWindow rightPopWindow;

    private static AlarmFragment alarmFragment;

    public static AlarmFragment newInstance() {
        if (alarmFragment == null) {
            alarmFragment = new AlarmFragment();
        }
        return alarmFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cloud, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
        initListener();
    }

    private void initView() {
        cloudTitle.setRightIcon(R.drawable.point);
        cloudTitle.setLeftIcon(R.drawable.menu_img);

        //取控件textView当前的布局参数 linearParams.height = 20;// 控件的高强制设成20
        RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) friendsBarStatue.getLayoutParams();
        // 控件的宽强制设成30
        linearParams.height = BarTools.getActionBarHeight(getActivity()) / 2;
        //使设置好的布局参数应用到控件
        friendsBarStatue.setLayoutParams(linearParams);

        rightPopWindow = new MainRightPopWindow(getActivity());
    }

    private void initListener() {
        cloudTitle.setLeftIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new EventFeiendsFragment(Constants.SHOWMENU));
            }
        });

        cloudTitle.setRightIconOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                rightPopWindow.showAtLocation(cloudTitle, Gravity.TOP | Gravity.RIGHT, 0, 0);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
