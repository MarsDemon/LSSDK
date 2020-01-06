package longse.com.herospeed.tools;

import android.app.Activity;
import android.app.Application;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import longse.com.herospeed.base.BaseApplication;

/**
 * Created by ly on 2017/11/16.
 *
 * @function 工具类 可以异步执行数据
 */

public class FactoryThread {

    //全局的线程池
    private final Executor defaultExecutor;

    private static final FactoryThread instance;

    static {
        instance = new FactoryThread();
    }


    public static Application app() {
        return BaseApplication.getInstance();
    }

    /**
     * 异步执行的方法
     *
     * @param runnable runnable
     */
    public static void runOnAsync(Runnable runnable) {
        //拿到单利  拿到线程池
        instance.defaultExecutor.execute(runnable);  //执行runnable
    }

    public static void runOnUiThread(Activity context){
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    public FactoryThread() {
        defaultExecutor = Executors.newFixedThreadPool(4);//新建一个线程池
    }

    public static void dispathPushMessage(){

    }

}
