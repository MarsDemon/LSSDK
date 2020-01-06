package longse.com.herospeed.popupWindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import longse.com.herospeed.R;


/**
 * Created by LY on 2017/7/25.
 */

public class MainRightPopWindow extends PopupWindow {

    @BindView(R.id.add_friend_device)
    TextView addFriendDevice;
    @BindView(R.id.tv_name)
    TextView tvName;


    public MainRightPopWindow(Activity context) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mView = inflater.inflate(R.layout.pop_add_friend_layout, null);


        //设置PopupWindow的View
        this.setContentView(mView);
        //设置PopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.WRAP_CONTENT);
        //设置PopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        //设置PopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置PopupWindow点击外部消失
        this.setOutsideTouchable(true);
//        //设置SelectPicPopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.Animation);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        ButterKnife.bind(this, mView);
    }


    public void setAddFriendsAdnDeviceOnClickLicener(View.OnClickListener listener) {
        addFriendDevice.setOnClickListener(listener);
    }

    public void setSignOutOnClickLicener(View.OnClickListener listener) {
        tvName.setOnClickListener(listener);
    }




}

