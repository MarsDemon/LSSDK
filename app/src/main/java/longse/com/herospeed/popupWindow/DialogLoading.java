package longse.com.herospeed.popupWindow;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import longse.com.herospeed.R;

/**
 * Created by LY on 2017/11/14.
 */

public class DialogLoading extends BaseDialog{

    private ProgressBar mLoadingView;
    private View mDialogContentView;
    private TextView mTextView;

    public DialogLoading(Context context, int themeResId) {
        super(context, themeResId);
        initView(context);
    }

    public DialogLoading(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView(context);
    }

    public DialogLoading(Context context) {
        super(context);
        initView(context);
    }

    public DialogLoading(Activity context) {
        super(context);
        initView(context);
    }

    public DialogLoading(Context context, float alpha, int gravity) {
        super(context, alpha, gravity);
        initView(context);
    }

    private void initView(Context context) {
        mDialogContentView = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        mLoadingView = (ProgressBar) mDialogContentView.findViewById(R.id.spin_kit);
        mTextView = (TextView) mDialogContentView.findViewById(R.id.name);
        setContentView(mDialogContentView);
    }

    //设置加载字体
    public void setLoadingText(CharSequence charSequence) {
        mTextView.setText(charSequence);
    }

    //设置加载颜色
    public void setLoadingColor(int color){
        mLoadingView.setBackgroundColor(color);
    }

    public void cancel(CancelType code, String str) {
        cancel();
        switch (code) {
            case normal:

                break;
            case error:

                break;
            case success:

                break;
            case info:

                break;
            default:

                break;
        }
    }

    public void cancel(String str) {
        cancel();
    }
    public View getDialogContentView() {
        return mDialogContentView;
    }

    public TextView getTextView() {
        return mTextView;
    }

    public enum CancelType {normal, error, success, info}
}
