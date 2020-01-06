package longse.com.learing.script;

import android.webkit.JavascriptInterface;

/**
 * web 通知 APP 执行动作
 * 在该类中实现 JS 要调用的方法
 *
 *  1、APP 通知 Web 执行某项动作
 *  2、APP 主动从 Web 获取信息
 *  3、Web 通知 APP 执行某项动作
 *  4、Web 主从从 APP 获取信息
 *
 */
public class WebToScript {

    @JavascriptInterface
    public void showMsgFromAndroid(String msg) {

    }

    @JavascriptInterface
    public String getMsgFromAndroid(String msg) {
        return "" + msg;
    }

    /**
     * 需要调用 WebView 对象的 addJavaScriptInterface 方法， 给这个新类注册一实例，然后 JS 才能通过该实例名调用 APP 的方法
     * 注册实例代码： wv_js.addJavascriptInterface(new WebToScript(), "client");
     */

    /**
     * WebView 要调用setWebChromeClient 方法设置 JS 的解释客户端，从而避免 JS 中 alert 方法不弹框的问题，因为 JS 页面的渲染需要 WebChromeClient 实现。
     *
     */
}
