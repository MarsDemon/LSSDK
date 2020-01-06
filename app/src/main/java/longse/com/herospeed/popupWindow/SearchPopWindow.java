package longse.com.herospeed.popupWindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import longse.com.herospeed.R;

import static android.content.Context.INPUT_METHOD_SERVICE;


/**
 * Created by LY on 2017/7/25.
 */

public class SearchPopWindow extends PopupWindow {


    @BindView(R.id.search_back)
    ImageView searchBack;
    @BindView(R.id.search_content_et)
    EditText searchContentEt;
    @BindView(R.id.search_del_text)
    ImageView searchDelText;
    @BindView(R.id.pop_search_friends)
    TextView popSearchFriends;
    @BindView(R.id.pop_search_friends_ll)
    LinearLayout popSearchFriendsLl;
    @BindView(R.id.pop_search_device)
    TextView popSearchDevice;
    @BindView(R.id.pop_search_device_ll)
    LinearLayout popSearchDeviceLl;
    @BindView(R.id.shadow)
    View shadow;

    public SearchPopWindow(Activity context) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mView = inflater.inflate(R.layout.pop_search_layout, null);

        //设置PopupWindow的View
        this.setContentView(mView);
        //设置PopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        //设置PopupWindow弹出窗体的高
        this.setHeight(LayoutParams.MATCH_PARENT);
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
        searchContentEtListener();
        changeKey(context);
    }

    private void changeKey(final Activity mContext) {
        searchContentEt.setOnKeyListener(new View.OnKeyListener() {

            @Override

            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    // 先隐藏键盘
                    ((InputMethodManager) mContext.getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(mContext.getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    //进行搜索操作的方法，在该方法中可以加入mEditSearchUser的非空判断
//                    search();
                }
                return false;
            }
        });
    }

    private void searchContentEtListener() {
        searchContentEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (TextUtils.isEmpty(s)) {
                    searchDelText.setVisibility(View.INVISIBLE);
                    return;
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                popSearchFriends.setText(s);
                popSearchDevice.setText(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    searchDelText.setVisibility(View.INVISIBLE);
                } else {
                    searchDelText.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    @OnClick({R.id.search_back, R.id.search_del_text, R.id.pop_search_friends_ll, R.id.pop_search_device_ll, R.id.shadow})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_back:
                this.dismiss();
                break;
            case R.id.search_del_text:
                searchContentEt.setText("");
                popSearchFriends.setText("");
                popSearchDevice.setText("");
                break;
            case R.id.pop_search_friends_ll:
                break;
            case R.id.pop_search_device_ll:
                break;

            case R.id.shadow:
                this.dismiss();
                break;
        }
    }

}

