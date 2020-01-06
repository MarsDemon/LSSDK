package longse.com.herospeed.base;

/**
 * Created by LY on 2017/10/24.
 */

public class Constants {
    //侧滑菜单栏的五种fragment
    public static final int MENUFRIENDS = 1;
    public static final int MENUDEVICE = 2;
    public static final int MENUPUSH = 3;
    public static final int MENUCLOUD = 4;
    public static final int MENUSETING = 5;

    //显示菜单栏
    public static final int SHOWMENU = 0x000001;

    //存储信息
    public static final String CACHE_COOKIE = "cache_cookie";
    //存储登录状态
    public static final String CACHE_ISLOGIN = "cache_islogin";
    //存储用户信息
    public static final String CACHE_USERINF = "cache_userinfo";
    //存储Session
    public static final String CACHE_SESSION = "cache_SESSION";
    //存储SN登录名称
    public static final String CACHE_SN_ACCOUNT = "SN_LOGIN_ACCOUNT_KEY";
    //存储SN登录密码
    public static final String CACHE_SN_PWD = "SN_LOGIN_PASSWORD_KEY";
    //存储SN登录序列号
    public static final String CACHE_SN_DEVICE_ID = "SN_LOGIN_DEVICE_KEY";

    /**
     * 接口请求
     */
    public final static int REQUEST_SUEESS = 0X101;  //请求成功
    public final static int REQUEST_FAILURE = 0X102;  //请求失败
    public final static int REQUEST_NON= 0X103;  //请求无响应

    /**
     * JNI相关
     */
    public static final int JNIRESPONSECODE = 7827;
    public static final int DECODESUCC = 7650;
    public static final int CONNFILURE = 7651;
    public static final int GETRECORDTIME = 2759;
    public static final int NORECORDINFO = 2760;
    public static final int GETRECODINFOERROR = 2761;
    public static final int PLAYRECORDSTREAM = 2762;
    public static final int PLAYNEXTRECORD = 7824;
    public static final int GETRECORDOVER = 7825;
    public static final int POSTFRAMESIZE = 7826;
    public static final int PLAYNEXTVIDEO = 1414;
    public static final int GETCURRTIME = 4615;//回放时获取
    public static final int NOTSUPPORT = 4616; //设备不支持
}
