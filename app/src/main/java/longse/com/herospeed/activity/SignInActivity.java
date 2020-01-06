package longse.com.herospeed.activity;

import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Patterns;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import longse.com.herospeed.R;
import longse.com.herospeed.base.ConstAction;
import longse.com.herospeed.base.Constants;
import longse.com.herospeed.base.SkipActivity;
import longse.com.herospeed.eventbus.EventClientRequestManager;
import longse.com.herospeed.manager.ClientRequestManager;
import longse.com.herospeed.utils.LogUtil;
import longse.com.herospeed.utils.SharedUtils;
import longse.com.herospeed.widget.TitleView;

public class SignInActivity extends BaseActivity {

    @BindView(R.id.sign_in_title)
    TitleView signInTitle;
    @BindView(R.id.edit_sign_in_email)
    TextInputEditText editSignInEmail;
    @BindView(R.id.edit_sign_in_password)
    TextInputEditText editSignInPassword;
    @BindView(R.id.btn_sign_in)
    AppCompatButton btnSignIn;
    @BindView(R.id.tv_link_sign_up)
    AppCompatTextView tvLinkSignUp;
    @BindView(R.id.tv_link_forget_pwd)
    AppCompatTextView tvLinkForgetPwd;

    private String email;
    private String password;
    //判断是请求的那个接口做标记
    private static int requestID = -1;

    @Override
    protected void initBefore() {
        //注册eventBus
        EventBus.getDefault().register(this);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_sign_in;
    }

    @Override
    protected void initView() {
        super.initView();
        signInTitle.setLeftFinish(context);
        signInTitle.setRightIconVisibility(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.btn_sign_in)
    public void onClick() {
        if (checkForm()) {
            showProgress();
            requestID = 1;
            ClientRequestManager.initClientRequestManager().requestLogin(email, password);
        }
    }

    @OnClick(R.id.tv_link_sign_up)
    public void ontvClick() {
        SkipActivity.startSignUpActivity(context);
        finish();
    }

    @OnClick(R.id.tv_link_forget_pwd)
    public void onPwdClick() {
        if (checkEmailForm()) {
            requestID = 2;
            showProgress();
            ClientRequestManager.initClientRequestManager().requestSendEmail(email);
        }
    }

    private boolean checkForm() {
        email = editSignInEmail.getText().toString().trim();
        password = editSignInPassword.getText().toString().trim();

        boolean isPass = true;

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editSignInEmail.setError("Email error");
            isPass = false;
        } else {
            editSignInEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 6) {
            editSignInPassword.setError("Please fill out at least 6 digits password");
            isPass = false;
        } else {
            editSignInPassword.setError(null);
        }

        return isPass;
    }

    private boolean checkEmailForm() {
        email = editSignInEmail.getText().toString().trim();

        boolean isEmailPass = true;

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editSignInEmail.setError("Email error");
            isEmailPass = false;
        } else {
            editSignInEmail.setError(null);
        }

        return isEmailPass;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void clientManager(EventClientRequestManager event) {
        LogUtil.e("SignActivity==", event.toString());
        if (event == null) return;
        switch (event.getMsg()) {
            case Constants.REQUEST_SUEESS:
                if (event.getAction().equals(ConstAction.ACTION_COOKIE)) {
                    if (requestID == 1) {
                        ClientRequestManager.initClientRequestManager().requestLogin(email, password);
                    } else if (requestID == 2) {
                        ClientRequestManager.initClientRequestManager().requestSendEmail(email);
                    }
                } else if (event.getAction().equals(ConstAction.ACTION_LOGIN)) {
                    SharedUtils.putBoolean(context, Constants.CACHE_ISLOGIN, true);
                    dismissProgress();
                    Toast.makeText(context, "Sign in success", Toast.LENGTH_SHORT).show();
                    finish();
                } else if (event.getAction().equals(ConstAction.ACTION_SENDCODE_EMAIL)) {
                    dismissProgress();
                    SkipActivity.startFindPwdActivity(context, email);
                    finish();
                }
                break;

            case Constants.REQUEST_FAILURE:
                dismissProgress();
                if (event.getAction().equals(ConstAction.ACTION_COOKIE)) {
                    Toast.makeText(context, "CooKie get failure", Toast.LENGTH_SHORT).show();
                } else if (event.getAction().equals(ConstAction.ACTION_LOGIN) || event.getAction().equals(ConstAction.ACTION_SENDCODE_EMAIL)) {
                    if (event.getObject() != null) {
                        Toast.makeText(context, (String) event.getObject(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(context, "Sign in failure", Toast.LENGTH_SHORT).show();
                }
                break;

            case Constants.REQUEST_NON:
                dismissProgress();
                Toast.makeText(context, "The network or server error", Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
