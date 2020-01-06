package longse.com.herospeed.activity;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.longse.freeipfunction.manager.RequestManager;

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
import longse.com.herospeed.widget.TitleView;

public class SignUpActivity extends BaseActivity {

    @BindView(R.id.sign_up_title)
    TitleView signUpTitle;
    @BindView(R.id.edit_sign_up_email)
    TextInputEditText editSignUpEmail;
    @BindView(R.id.edit_sign_up_password)
    TextInputEditText editSignUpPassword;
    @BindView(R.id.edit_sign_up_re_password)
    TextInputEditText editSignUpRePassword;
    @BindView(R.id.btn_sign_up)
    AppCompatButton btnSignUp;
    @BindView(R.id.tv_link_sign_in)
    AppCompatTextView tvLinkSignIn;
    @BindView(R.id.activity_sign_up)
    LinearLayoutCompat activitySignUp;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.img_code)
    ImageView imgCode;
    private String email;
    private String password;
    private String verifyCode;

    @Override
    protected void initBefore() {
        //注册eventBus
        EventBus.getDefault().register(this);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_sign_up;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initView() {
        super.initView();
        signUpTitle.setLeftFinish(context);
        signUpTitle.setRightIconVisibility(false);
    }

    @Override
    protected void initData() {
        super.initData();
        //初始化验证码
        ClientRequestManager.initClientRequestManager().Captcha();
    }

    @OnClick({R.id.btn_sign_up, R.id.tv_link_sign_in, R.id.img_code})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sign_up:
                if (checkForm()) {
                    showProgress();
                    RequestManager.getInstance().submitResgisterInfo(email, password, verifyCode);
                }
                break;

            case R.id.img_code:
                ClientRequestManager.initClientRequestManager().Captcha();
                break;

            case R.id.tv_link_sign_in:
                SkipActivity.startSignInActivity(context);
                finish();
                break;


        }
    }


    private boolean checkForm() {
        email = editSignUpEmail.getText().toString().trim();
        password = editSignUpPassword.getText().toString().trim();
        final String rePassword = editSignUpRePassword.getText().toString().trim();
        verifyCode = etCode.getText().toString().trim();

        boolean isPass = true;

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editSignUpEmail.setError("Wrong email address format");
            isPass = false;
        } else {
            editSignUpEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 6) {
            editSignUpPassword.setError("Please fill out at least 6 digits password");
            isPass = false;
        } else {
            editSignUpPassword.setError(null);
        }

        if (rePassword.isEmpty() || rePassword.length() < 6 || !rePassword.equals(password)) {
            editSignUpRePassword.setError("Two input password is not consistent, please check");
            isPass = false;
        } else {
            editSignUpRePassword.setError(null);
        }

        if (verifyCode.isEmpty() || verifyCode.length() < 4) {
            Toast.makeText(context, "Verification code for four", Toast.LENGTH_SHORT).show();
            isPass = false;
        }

        return isPass;
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void clientManager(EventClientRequestManager event) {
        if (event == null) return;
        LogUtil.e("SignUPActivity==", event.toString());
        switch (event.getMsg()) {
            case Constants.REQUEST_SUEESS:
                if (event.getAction().equals(ConstAction.ACTION_COOKIE)) {
                    ClientRequestManager.initClientRequestManager().Captcha();
                } else if (event.getAction().equals(ConstAction.ACTION_BITMAP)) {
                    if (event.getBitmap() != null) {
                        imgCode.setImageBitmap(event.getBitmap());
                    }
                } else if (event.getAction().equals(ConstAction.ACTION_REGISTER)) {
                    dismissProgress();
                    Toast.makeText(context, "Sign up success", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;

            case Constants.REQUEST_FAILURE:
                dismissProgress();
                if (event.getAction().equals(ConstAction.ACTION_COOKIE)) {
                    Toast.makeText(context, "CooKie fail to get", Toast.LENGTH_SHORT).show();
                } else if (event.getAction().equals(ConstAction.ACTION_BITMAP)) {
                    if (event.getObject() != null) {
                        Toast.makeText(context, (String) event.getObject(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(context, "Verification code for failure", Toast.LENGTH_SHORT).show();
                } else if (event.getAction().equals(ConstAction.ACTION_REGISTER)) {
                    if (event.getObject() != null) {
                        Toast.makeText(context, (String) event.getObject(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(context, "The network or server error", Toast.LENGTH_SHORT).show();
                }
                break;

            case Constants.REQUEST_NON:
                dismissProgress();
                Toast.makeText(context, "The network or server error", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
