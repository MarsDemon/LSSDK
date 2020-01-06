package longse.com.herospeed.base;

/**
 * Created by LY on 2017/10/24.
 */

public class ConstAction {
    public static final String JNIRequestKey = "cmd";

    public static final String JNIRequestStreamInfoCmd = "cmd_get_stream_info";
    public static final String JNIRequestRearchRecordCmd = "cmd_start_record_search";

    //事件消息
    private static final String BASE_ACTION = "com.longse.SDK";
    //获取CooKie
    public static final String ACTION_COOKIE = BASE_ACTION + ".cookieaction";
    //验证码图片
    public static final String ACTION_BITMAP = BASE_ACTION + ".bitmapaction";
    //登录
    public static final String ACTION_LOGIN = BASE_ACTION + ".loginaction";
    //注册
    public static final String ACTION_REGISTER = BASE_ACTION + ".resgisteraction";
    //验证邮箱是否被注册
    public static final String ACTION_CHECKEMAIL = BASE_ACTION + ".checkemail";
    //发送验证码到邮箱
    public static final String ACTION_SENDCODE_EMAIL = BASE_ACTION + ".sendcodetoemail";
    //修改密码
    public static final String ACTION_CHANGE_PWD = BASE_ACTION + ".changepwd";
    //获取所有设备
    public static final String ACTION_ALLDEVICES = BASE_ACTION + ".alldevices";
    //SN
    public static final String ACTION_SNLOGIN = BASE_ACTION + ".snlogin";
    //设备序列号验证
    public static final String ACTION_DEVCIEID_CHECK = BASE_ACTION + ".deviceidcheck";
    //绑定设备
    public static final String ACTION_BIND_DEVICE = BASE_ACTION + ".binddevice";

}
