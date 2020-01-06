package longse.com.herospeed.activity;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.View;
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
import longse.com.herospeed.eventbus.EventClientRequestManager;
import longse.com.herospeed.utils.LogUtil;
import longse.com.herospeed.widget.TitleView;

public class BindDeviceActivity extends BaseActivity {

    @BindView(R.id.bind_device_title)
    TitleView bindDeviceTitle;
    @BindView(R.id.edit_device_id)
    TextInputEditText editDeviceId;
    @BindView(R.id.edit_device_account)
    TextInputEditText editDeviceAccount;
    @BindView(R.id.edit_device_password)
    TextInputEditText editDevicePassword;
    @BindView(R.id.edit_device_verify)
    TextInputEditText editDeviceVerify;
    @BindView(R.id.btn_bind)
    AppCompatButton btnBind;
    @BindView(R.id.til_account)
    TextInputLayout tilAccount;
    @BindView(R.id.til_password)
    TextInputLayout tilPassword;
    @BindView(R.id.til_verify)
    TextInputLayout tilVerify;

    private String deviceId;
    private String deviceAccount;
    private String devicePassword;
    private String deviceVerify;
    private boolean hasVerify = false;

    @Override
    protected void initBefore() {
        //注册eventBus
        EventBus.getDefault().register(this);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_bind_device;
    }

    @Override
    protected void initView() {
        super.initView();
        bindDeviceTitle.setLeftFinish(this);
        bindDeviceTitle.setRightIconVisibility(false);
    }

    @OnClick(R.id.btn_bind)
    public void onClick() {
        if (tilAccount.getVisibility() == View.GONE) {
            checkDevcie();
        } else {
            checkForm();
            showProgress();
            RequestManager.getInstance().bindDevice(deviceId, deviceAccount, devicePassword, deviceVerify);
        }
    }

    private void checkDevcie() {
        deviceId = editDeviceId.getText().toString().trim();
        if (!TextUtils.isEmpty(deviceId) || deviceId.length() < 13) {
            editDeviceId.setError("Wrong device id format");
            return;
        } else {
            editDeviceId.setError(null);
        }
        showProgress();
        RequestManager.getInstance().CheckDeviceCode(deviceId);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void clientManager(EventClientRequestManager event) {
        LogUtil.e("BindDeviceActivity==", event.toString());
        if (event == null) return;
        switch (event.getMsg()) {
            case Constants.REQUEST_SUEESS:
                dismissProgress();
                if (event.getAction().equals(ConstAction.ACTION_DEVCIEID_CHECK)) {
                    hasVerify = (boolean) event.getObject();
                    editDeviceId.setFocusable(false);
                    btnBind.setText("Bind");
                    if (hasVerify) {
                        tilAccount.setVisibility(View.VISIBLE);
                        tilPassword.setVisibility(View.VISIBLE);
                        tilVerify.setVisibility(View.VISIBLE);
                    } else {
                        tilVerify.setVisibility(View.GONE);
                    }
                } else if (event.getAction().equals(ConstAction.ACTION_BIND_DEVICE)) {
                    Toast.makeText(context, "Bind device success", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;

            case Constants.REQUEST_FAILURE:
                dismissProgress();
                if (event.getAction().equals(ConstAction.ACTION_DEVCIEID_CHECK)) {
                    if (event.getObject() != null) {
                        Toast.makeText(context, (String) event.getObject(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(context, "The network or server error", Toast.LENGTH_SHORT).show();
                } else if (event.getAction().equals(ConstAction.ACTION_DEVCIEID_CHECK)) {
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


    private boolean checkForm() {
        deviceAccount = editDeviceAccount.getText().toString().trim();
        devicePassword = editDevicePassword.getText().toString().trim();
        deviceVerify = editDeviceVerify.getText().toString().trim();

        boolean isPass = true;

        if (deviceAccount.isEmpty() || deviceAccount.length() < 6) {
            editDeviceAccount.setError("Wrong device account address format");
            isPass = false;
        } else {
            editDeviceAccount.setError(null);
        }

        if (devicePassword.isEmpty() || devicePassword.length() < 6) {
            editDevicePassword.setError("Please fill out at least 6 digits password");
            isPass = false;
        } else {
            editDevicePassword.setError(null);
        }
        if (hasVerify) {
            if (deviceVerify.isEmpty() || deviceVerify.length() < 4) {
                Toast.makeText(context, "Verification code for four", Toast.LENGTH_SHORT).show();
                isPass = false;
            }
        }
        return isPass;
    }

}
