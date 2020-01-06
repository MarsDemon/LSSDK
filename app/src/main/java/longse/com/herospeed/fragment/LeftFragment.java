package longse.com.herospeed.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import longse.com.herospeed.R;
import longse.com.herospeed.base.Constants;
import longse.com.herospeed.bean.PersonalBean;
import longse.com.herospeed.eventbus.EventLeftFragment;
import longse.com.herospeed.utils.LogUtil;
import longse.com.herospeed.utils.SharedUtils;
import longse.com.herospeed.widget.CircleImageView;

import static longse.com.herospeed.base.Constants.MENUCLOUD;
import static longse.com.herospeed.base.Constants.MENUDEVICE;
import static longse.com.herospeed.base.Constants.MENUFRIENDS;
import static longse.com.herospeed.base.Constants.MENUPUSH;
import static longse.com.herospeed.base.Constants.MENUSETING;

/**
 * Created by LY on 2017/10/24.
 */

public class LeftFragment extends BaseFragment {

    @BindView(R.id.login_img)
    CircleImageView loginImg;
    @BindView(R.id.login_statiu_tx)
    TextView loginStatiuTx;
    @BindView(R.id.login_ll)
    LinearLayout loginLl;
    @BindView(R.id.menu_friends)
    TextView menuFriends;
    @BindView(R.id.menu_device)
    TextView menuDevice;
    @BindView(R.id.menu_push)
    TextView menuPush;
    @BindView(R.id.menu_cloud)
    TextView menuCloud;
    @BindView(R.id.menu_setting)
    TextView menuSetting;

    private static LeftFragment leftFragment;

    public static LeftFragment newInstance() {
        if (leftFragment == null) {
            leftFragment = new LeftFragment();
        }
        return leftFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_left, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        PersonalBean personalBean = PersonalBean.parse(SharedUtils.getString(getContext(), Constants.CACHE_USERINF, ""));
        if (personalBean != null) {
            loginStatiuTx.setText(personalBean.getNickName());
            Glide.with(getActivity()).
                    load(personalBean.getUserImg()).
                    diskCacheStrategy(DiskCacheStrategy.RESULT).
                    thumbnail(0.5f).
                    priority(Priority.HIGH).
                    placeholder(R.drawable.pikachu_sit).
                    error(R.drawable.pikachu_sit).
                    fallback(R.drawable.pikachu_sit)
                    .into(loginImg);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        menuFriends.setBackgroundResource(R.color.c);
        menuFriends.setTextColor(getResources().getColor(R.color.red));
        menuFriends.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.me_choose), null, null, null);

    }

    @OnClick({R.id.login_img, R.id.login_statiu_tx, R.id.login_ll, R.id.menu_friends, R.id.menu_device, R.id.menu_push, R.id.menu_cloud, R.id.menu_setting})
    public void onClick(View view) {
        LogUtil.e("点击事件", String.valueOf(view.getId()));
        switch (view.getId()) {
            case R.id.login_img://头像
                break;
            case R.id.login_statiu_tx://状态text
                break;
            case R.id.login_ll://头像和昵称
                break;
            case R.id.menu_friends://好友
                changeUiColorAndFragment(MENUFRIENDS);
                break;
            case R.id.menu_device://设备
                changeUiColorAndFragment(MENUDEVICE);
                break;
            case R.id.menu_push://推送
                changeUiColorAndFragment(MENUPUSH);
                break;
            case R.id.menu_cloud://云存储
                changeUiColorAndFragment(MENUCLOUD);
                break;
            case R.id.menu_setting://设置
                changeUiColorAndFragment(MENUSETING);
                break;
        }
    }

    private void changeUiColorAndFragment(int menu) {
        EventBus.getDefault().post(new EventLeftFragment(menu));
        switch (menu) {
            case MENUFRIENDS:
                menuFriends.setBackgroundResource(R.color.c);
                menuFriends.setTextColor(getResources().getColor(R.color.red));
                menuFriends.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.me_choose), null, null, null);

                menuDevice.setBackgroundResource(R.color.transparent);
                menuDevice.setTextColor(getResources().getColor(R.color.black));
                menuDevice.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.video), null, null, null);

                menuPush.setBackgroundResource(R.color.transparent);
                menuPush.setTextColor(getResources().getColor(R.color.black));
                menuPush.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.push), null, null, null);

                menuCloud.setBackgroundResource(R.color.transparent);
                menuCloud.setTextColor(getResources().getColor(R.color.black));
                menuCloud.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.cloud), null, null, null);

                menuSetting.setBackgroundResource(R.color.transparent);
                menuSetting.setTextColor(getResources().getColor(R.color.black));
                menuSetting.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.setting), null, null, null);
                break;
            case MENUDEVICE:
                menuDevice.setBackgroundResource(R.color.c);
                menuDevice.setTextColor(getResources().getColor(R.color.red));
                menuDevice.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.video_choose), null, null, null);

                menuFriends.setBackgroundResource(R.color.transparent);
                menuFriends.setTextColor(getResources().getColor(R.color.black));
                menuFriends.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.me), null, null, null);

                menuPush.setBackgroundResource(R.color.transparent);
                menuPush.setTextColor(getResources().getColor(R.color.black));
                menuPush.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.push), null, null, null);

                menuCloud.setBackgroundResource(R.color.transparent);
                menuCloud.setTextColor(getResources().getColor(R.color.black));
                menuCloud.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.cloud), null, null, null);

                menuSetting.setBackgroundResource(R.color.transparent);
                menuSetting.setTextColor(getResources().getColor(R.color.black));
                menuSetting.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.setting), null, null, null);
                break;
            case MENUPUSH:
                menuPush.setBackgroundResource(R.color.c);
                menuPush.setTextColor(getResources().getColor(R.color.red));
                menuPush.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.push_choose), null, null, null);

                menuFriends.setBackgroundResource(R.color.transparent);
                menuFriends.setTextColor(getResources().getColor(R.color.black));
                menuFriends.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.me), null, null, null);

                menuDevice.setBackgroundResource(R.color.transparent);
                menuDevice.setTextColor(getResources().getColor(R.color.black));
                menuDevice.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.video), null, null, null);

                menuCloud.setBackgroundResource(R.color.transparent);
                menuCloud.setTextColor(getResources().getColor(R.color.black));
                menuCloud.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.cloud), null, null, null);

                menuSetting.setBackgroundResource(R.color.transparent);
                menuSetting.setTextColor(getResources().getColor(R.color.black));
                menuSetting.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.setting), null, null, null);
                break;
            case MENUCLOUD:
                menuCloud.setBackgroundResource(R.color.c);
                menuCloud.setTextColor(getResources().getColor(R.color.red));
                menuCloud.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.cloud_choose), null, null, null);

                menuFriends.setBackgroundResource(R.color.transparent);
                menuFriends.setTextColor(getResources().getColor(R.color.black));
                menuFriends.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.me), null, null, null);

                menuDevice.setBackgroundResource(R.color.transparent);
                menuDevice.setTextColor(getResources().getColor(R.color.black));
                menuDevice.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.video), null, null, null);

                menuPush.setBackgroundResource(R.color.transparent);
                menuPush.setTextColor(getResources().getColor(R.color.black));
                menuPush.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.push), null, null, null);

                menuSetting.setBackgroundResource(R.color.transparent);
                menuSetting.setTextColor(getResources().getColor(R.color.black));
                menuSetting.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.setting), null, null, null);
                break;

            case MENUSETING:

                menuSetting.setBackgroundResource(R.color.c);
                menuSetting.setTextColor(getResources().getColor(R.color.red));
                menuSetting.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.setting), null, null, null);

                menuFriends.setBackgroundResource(R.color.transparent);
                menuFriends.setTextColor(getResources().getColor(R.color.black));
                menuFriends.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.me), null, null, null);

                menuDevice.setBackgroundResource(R.color.transparent);
                menuDevice.setTextColor(getResources().getColor(R.color.black));
                menuDevice.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.video), null, null, null);

                menuPush.setBackgroundResource(R.color.transparent);
                menuPush.setTextColor(getResources().getColor(R.color.black));
                menuPush.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.push), null, null, null);

                menuCloud.setBackgroundResource(R.color.transparent);
                menuCloud.setTextColor(getResources().getColor(R.color.black));
                menuCloud.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.cloud), null, null, null);



                break;
        }
    }


}
