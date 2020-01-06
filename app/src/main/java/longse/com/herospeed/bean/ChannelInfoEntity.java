package longse.com.herospeed.bean;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by lw on 2017/6/8.
 */

public class ChannelInfoEntity implements Serializable {
    public final static long serialVersionUID = 2L;

    private int dc_id;
    private String device_id;
    private String channel_name;
    private int channel;
    private int order_num;

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        try{
            json.put("dc_id",dc_id);
            json.put("device_id",device_id);
            json.put("channel_name",channel_name);
            json.put("channel",channel);
            json.put("order_num",order_num);
        }catch (Exception e){
            e.printStackTrace();
        }
        return json;
    }

    public static ChannelInfoEntity parseLocaleSharedData(JSONObject object){
        return parse(object);
    }

    public static ChannelInfoEntity parse(String str){
        if(!TextUtils.isEmpty(str)){
            try{
                JSONObject jsonObject = new JSONObject(str);
                if(jsonObject != null){
                    return parse(jsonObject);
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * dc_id : number        关系主键
     device_id : number      设备ID
     channel_name : string   通道备注名
     channel : number        通道号
     order_num : number      排序位置
     */
    public static ChannelInfoEntity parse(JSONObject jsonObject){
        if(jsonObject == null)return null;
        ChannelInfoEntity infoEntity = new ChannelInfoEntity();
        infoEntity.setDc_id(jsonObject.optInt("dc_id"));
        infoEntity.setDevice_id(jsonObject.optString("device_id"));
        infoEntity.setChannel_name(jsonObject.optString("channel_name"));
        infoEntity.setChannel(jsonObject.optInt("channel"));
        infoEntity.setOrder_num(jsonObject.optInt("order_num"));
        return infoEntity;
    }

    public int getDc_id() {
        return dc_id;
    }

    public void setDc_id(int dc_id) {
        this.dc_id = dc_id;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getChannel_name() {
        return channel_name;
    }

    public void setChannel_name(String channel_name) {
        this.channel_name = channel_name;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public int getOrder_num() {
        return order_num;
    }

    public void setOrder_num(int order_num) {
        this.order_num = order_num;
    }

    @Override
    public String toString() {
        return "\n ChannelInfoEntity{" +
                "\n dc_id=" + dc_id +
                ",\n device_id='" + device_id + '\'' +
                ",\n channel_name='" + channel_name + '\'' +
                ",\n channel=" + channel +
                ",\n order_num=" + order_num +
                '}';
    }
}
