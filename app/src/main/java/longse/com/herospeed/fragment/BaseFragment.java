package longse.com.herospeed.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import longse.com.herospeed.popupWindow.DialogLoading;

/**
 * Created by LY on 2017/11/15.
 */

public class BaseFragment extends Fragment {
    private DialogLoading dialogLoading;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initProgress();
    }

    private void initProgress() {
        dialogLoading = new DialogLoading(getContext());
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
}
