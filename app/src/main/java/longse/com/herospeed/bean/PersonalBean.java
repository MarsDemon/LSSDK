package longse.com.herospeed.bean;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import longse.com.herospeed.utils.LogUtil;

/**
 * Created by ly on 2017/11/16.
 *
 * @function 用户信息
 */
public class PersonalBean {
    private String emailAddr;
    private String emailCheck;
    private String phoneNumber;
    private String realName;
    private String nickName;
    private String userImg;
    private String userId;

    public PersonalBean() {
    }

    public PersonalBean(String userId, String phoneNumber, String realName, String nickName, String userImg, String emailAddr, String emailCheck) {
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.realName = realName;
        this.nickName = nickName;
        this.userImg = userImg;
        this.emailAddr = emailAddr;
        this.emailCheck = emailCheck;
    }

    public static PersonalBean parse(String str) {
        if (TextUtils.isEmpty(str))
            return null;
        try {
            JSONObject Object = new JSONObject(str);
            return parse(Object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PersonalBean parse(JSONObject object) {
        if (object == null)
            return null;
        LogUtil.e("PersonalBean==", String.valueOf(object));
        PersonalBean info = new PersonalBean();
        try {
            info.setEmailAddr(object.getString("email_addr"));
            info.setEmailCheck(object.getString("email_check"));
            info.setPhoneNumber(object.getString("phone_number"));
            info.setRealName(object.getString("real_name"));
            info.setNickName(object.getString("nickname"));
            info.setUserImg(object.getString("user_img"));
            info.setUserId(object.getString("user_id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return info;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmailAddr() {
        return emailAddr;
    }

    public void setEmailAddr(String emailAddr) {
        this.emailAddr = emailAddr;
    }

    public String getEmailCheck() {
        return emailCheck;
    }

    public void setEmailCheck(String emailCheck) {
        this.emailCheck = emailCheck;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }
}
