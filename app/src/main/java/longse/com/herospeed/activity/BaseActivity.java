package longse.com.herospeed.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.bumptech.glide.Glide;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import longse.com.herospeed.popupWindow.DialogLoading;
import longse.com.herospeed.tools.BarTools;

/**
 * Created by LY on 2017/10/24.
 */

public abstract class BaseActivity extends FragmentActivity {

    private Unbinder mUnbinder;
    public BaseActivity context;
    private DialogLoading dialogLoading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //状态栏透明
        BarTools.setTransparentStatusBar(this);
        context = this;
        //在界面未初始化之前调用额初始化窗口
        Glide.get(this).clearMemory();


        if (initArgs(getIntent().getExtras())) {
            //设置界面layoutId
            setContentView(getContentLayoutId());

            initBefore();

            initView();

            initProgress();

            initListener();

            initData();

        } else {
            finish();
        }

    }

    protected abstract void initBefore();

    /**
     * 用于子类设置各种监听
     */
    protected void initListener() {

    }


    /**
     * 初始化窗口
     *
     * @param bundle 数据
     * @return 如果初始化成功  返回true
     */
    protected boolean initArgs(Bundle bundle) {
        return true;
    }

    /**
     * 当前控件id
     *
     * @return layoutId
     */
    protected abstract int getContentLayoutId();

    /**
     * 初始化控件
     */
    protected void initView() {
        mUnbinder = ButterKnife.bind(this);
    }


    /**
     * 初始化数据
     */
    protected void initData() {

    }

    private void initProgress() {
        dialogLoading = new DialogLoading(this);
    }

    public void showProgress() {
        if (dialogLoading != null) {
            dialogLoading.show();
        }
    }

    public void dismissProgress() {
        if (dialogLoading != null) {
            dialogLoading.dismiss();
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {//处理软键盘不会回收bug
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();

        Glide.get(this).clearMemory();
    }
}
