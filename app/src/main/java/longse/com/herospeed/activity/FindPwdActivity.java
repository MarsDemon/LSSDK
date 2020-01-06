package longse.com.herospeed.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.longse.freeipfunction.manager.RequestManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import longse.com.herospeed.R;
import longse.com.herospeed.base.ConstAction;
import longse.com.herospeed.base.Constants;
import longse.com.herospeed.eventbus.EventClientRequestManager;
import longse.com.herospeed.utils.LogUtil;
import longse.com.herospeed.utils.SharedUtils;
import longse.com.herospeed.widget.TitleView;

public class FindPwdActivity extends BaseActivity {

    @BindView(R.id.forget_pwd_title)
    TitleView forgetPwdTitle;
    @BindView(R.id.forget_email_show)
    AppCompatTextView forgetEmailShow;
    @BindView(R.id.forget_pwd)
    TextInputEditText forgetPwd;
    @BindView(R.id.forget_again_pwd)
    TextInputEditText forgetAgainPwd;
    @BindView(R.id.forget_code)
    EditText forgetCode;
    @BindView(R.id.forget_time)
    AppCompatTextView forgetTime;
    @BindView(R.id.forget_complete)
    AppCompatButton forgetComplete;


    private String email;
    private int time_start = 60;
    private int Time_tip = 0010;
    private Timer timer;
    private Handler handler;
    private String pwd;
    private String verifyCode;

    @Override
    protected void initBefore() {
        //注册eventBus
        EventBus.getDefault().register(this);

        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        LogUtil.e("FindPwdActivity==", "email==" + email);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_find_pwd;
    }

    @Override
    protected void initView() {
        super.initView();
        forgetPwdTitle.setLeftFinish(context);
        forgetPwdTitle.setRightIconVisibility(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initData() {
        super.initData();
        if (!TextUtils.isEmpty(email)) {
            forgetEmailShow.setText(String.format("验证码已发送至：%s", email));
            handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (msg.what == Time_tip) {
                        if (time_start == 0) {
                            timer.cancel();
                            if (forgetTime == null) return;
                            forgetTime.setEnabled(true);
                            forgetTime.setText("重新发送");
                        } else {
                            if (forgetTime == null) return;
                            forgetTime.setEnabled(false);
                            forgetTime.setText(timeTostr(--time_start));
                        }
                    }
                }
            };
            dealTimer();
        }
    }

    public String timeTostr(int time) {
        if ((time > 0) && (time < 60)) {
            return String.format("%ds可重新发送", time);
        }
        return "";
    }

    public void dealTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(Time_tip);
            }
        }, 0, 1000);
    }

    @OnClick({R.id.forget_time, R.id.forget_complete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.forget_time:
                showProgress();
                RequestManager.getInstance().sendEmail(email);
                break;
            case R.id.forget_complete:
                if (checkForm()) {
                    showProgress();
                    RequestManager.getInstance().changePwdAction(email, pwd, verifyCode,
                            SharedUtils.getString(context, Constants.CACHE_SESSION, "session_id"));
                }
                break;
        }
    }

    private boolean checkForm() {
        pwd = forgetPwd.getText().toString().trim();
        final String rePassword = forgetAgainPwd.getText().toString().trim();
        verifyCode = forgetCode.getText().toString().trim();

        boolean isPass = true;

        if (pwd.isEmpty() || pwd.length() < 6) {
            forgetPwd.setError("请填写至少6位数密码");
            isPass = false;
        } else {
            forgetPwd.setError(null);
        }

        if (rePassword.isEmpty() || rePassword.length() < 6 || !rePassword.equals(pwd)) {
            forgetAgainPwd.setError("两次输入的密码不一致,请检查");
            isPass = false;
        } else {
            forgetAgainPwd.setError(null);
        }

        if (verifyCode.isEmpty() || verifyCode.length() < 4) {
            Toast.makeText(context, "验证码为4位", Toast.LENGTH_SHORT).show();
            isPass = false;
        }

        return isPass;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void clientManager(EventClientRequestManager event) {
        LogUtil.e("SignUPActivity==", event.toString());
        if (event == null) return;
        switch (event.getMsg()) {
            case Constants.REQUEST_SUEESS:
                dismissProgress();
                if (event.getAction().equals(ConstAction.ACTION_SENDCODE_EMAIL)) {
                    time_start = 60;
                    dealTimer();
                } else if (event.getAction().equals(ConstAction.ACTION_CHANGE_PWD)) {
                    Toast.makeText(context, "密码修改成功", Toast.LENGTH_SHORT).show();
                    dismissProgress();
                    finish();
                }
                break;

            case Constants.REQUEST_FAILURE:
                dismissProgress();
                if (event.getAction().equals(ConstAction.ACTION_SENDCODE_EMAIL)) {
                    if (event.getObject() != null) {
                        Toast.makeText(context, (String) event.getObject(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(context, "验证码发送失败", Toast.LENGTH_SHORT).show();
                } else if (event.getAction().equals(ConstAction.ACTION_SENDCODE_EMAIL)) {
                    if (event.getObject() != null) {
                        Toast.makeText(context, (String) event.getObject(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(context, "重置密码失败", Toast.LENGTH_SHORT).show();
                }
                break;

            case Constants.REQUEST_NON:
                dismissProgress();
                Toast.makeText(context, "网络或服务器异常", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
