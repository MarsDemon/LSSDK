package longse.com.herospeed.tools.scanner;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.CountDownLatch;

import longse.com.herospeed.activity.SearchActivity;

/**
 *
 * 描述: 解码线程
 */
final class DecodeThread extends Thread {

    private final CountDownLatch handlerInitLatch;
    SearchActivity activity;
    private Handler handler;

	DecodeThread(SearchActivity activity) {
		this.activity = activity;
		handlerInitLatch = new CountDownLatch(1);
	}

	Handler getHandler() {
		try {
			handlerInitLatch.await();
		} catch (InterruptedException ie) {
			// continue?
		}
		return handler;
	}

	@Override
	public void run() {
		Looper.prepare();
		handler = new DecodeHandler(activity);
		handlerInitLatch.countDown();
		Looper.loop();
	}

}
