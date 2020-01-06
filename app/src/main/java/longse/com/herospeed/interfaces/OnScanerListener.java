package longse.com.herospeed.interfaces;

import com.google.zxing.Result;

/**
 * Created by ly on 2017/10/22.
 */

public interface OnScanerListener {
    void onSuccess(String type, Result result);

    void onFail(String type, String message);
}
