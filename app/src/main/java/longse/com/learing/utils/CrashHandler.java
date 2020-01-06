package longse.com.learing.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Looper;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;

/**
 * @author LY
 */
public class CrashHandler implements UncaughtExceptionHandler {

    public static final String TAG = "CrashHandler";
    //是否开启日志输出，在Debug状态下开启
    public static final boolean DEBUG = true;
    //系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    //程序的Context对象
    private Context mContext;

    private static final String VERSION_NAME = "versionName";
    private static final String VERSION_CODE = "versionCode";
    private static final String STACK_TRACE = "STACK_TRACE";

    //CrashHandler实例
    private static CrashHandler INSTANCE;
    //获取CrashHandler实例，单例模式
    public static CrashHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CrashHandler();
        }
        return INSTANCE;
    }

    //获取系统默认的UncaughtException处理器，设置该CrashHandler为程序的默认处理器
    public void setCrashHandler(Context ctx) {
        Log.d(TAG, "CrashHandler setCrashHandler");
        mContext = ctx;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    //当UncaughtException发生时会转入该函数来处理
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Log.d(TAG, "CrashHandler uncaughtException");
        if (DEBUG) {
            ex.printStackTrace();
        }
        if (!handleException(ex) && mDefaultHandler != null) {
            Log.d(TAG, "mDefaultHandler.uncaughtException");
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            Log.d(TAG, "sleep and killProcess");
            try {
                Thread.sleep(2000);
            }catch (InterruptedException e) {
                Log.e(TAG, "Error : ", e);
            }
            Process.killProcess(Process.myPid());
            System.exit(10);
        }
    }

    private String getMsg(Throwable ex) {
        //若是空指针异常，getLocalizedMessage返回的是null
        String msg = ex.getLocalizedMessage();
        if (msg == null) {
//        	PrintStream err_msg = System.err.append(toString());
//        	msg = err_msg.toString();
            StackTraceElement[] stackArray = ex.getStackTrace();
            StackTraceElement element = stackArray[0];
            msg = element.toString();
        }
        return msg;
    }

    //自定义错误处理、收集错误信息、发送错误报告等操作均在此完成
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            Log.d(TAG, "handleException --- ex==null");
            return true;
        }
        final String msg = getMsg(ex);
        if(msg == null) {
            Log.d(TAG, "getMessage is null");
            return false;
        }
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "程序出错，即将退出:\n"+msg, Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();
        String file_name = String.format("crash-%s.log", System.currentTimeMillis());
        //收集设备信息
        collectCrashDeviceInfo(mContext);
        //保存错误报告文件
        saveCrashInfoToFile(ex);
        //保存错误信息
        //发送错误报告到服务器，若后台需要获取错误报告则打开
        //sendCrashReportsToServer(mContext);
        return true;
    }

    //保存错误信息到文件中
    private void saveCrashInfoToFile(Throwable ex) {
        Log.d(TAG, "saveCrashInfoToFile");
        Writer info = new StringWriter();
        PrintWriter printWriter = new PrintWriter(info);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        String result = info.toString();
        printWriter.close();
    }

    //收集程序崩溃的设备信息
    private void collectCrashDeviceInfo(Context ctx) {
        Log.d(TAG, "collectCrashDeviceInfo");
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
            }
        }catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Error while collect package info", e);
        }
        //使用反射来收集设备信息，例如：系统版本号、设备生产商等环境信息
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                if (DEBUG) {
                    Log.d(TAG, field.getName() + " : " + field.get(null));
                }
            }catch (Exception e) {
                Log.e(TAG, "Error while collect crash info", e);
            }
        }
    }

}
