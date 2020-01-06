package longse.com.herospeed.bean;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import longse.com.herospeed.utils.LogUtil;

/**
 * Created by LY on 2017/11/15.
 */

public class DeviceInfoBean implements Serializable{
    public final static long serialVersionUID = 1L;

    private String equpId; // 设备ID
    private String ifOnLine;// 设备是否在线
    private String ifBind;// 是否被绑定
    private String equoModle;// �豸�ͺ�
    private String version;// 版本号
    private String checkStr;// ��֤��
    private String deviceName;// 设备名
    private String deviceType;// 设备类型、ipc,NVR
    private String sysVersion;// 系统版本号
    private String deviceDetatilType = ""; // 从设备端获取到的设备
    private String cateId;                 // 分组ID
    private String useCloudStorage;  // int  是否使用云存储 1：使用  0：没有
    private String canUseCloudStorage;  //是否能够使用云存储 1：可以  0：不可以

    private String cateName = "";
    private String deviceConnectServer;        //播放地址

    private String localPwd = "";
    private String localUser = "";
    private ArrayList<ChannelInfoEntity> infoEntitys;
    private String order_num;

    private String groupOrderNum;
    private String channelOrderNum;

    private boolean isLocalePlayer = false;//本地播放
    private String localeDeviceIp = "";
    private int localeDevicePort = 0;
    private boolean isDirect = false;
    private String PrivateServer = "";
    private int mode = 0;
    private int deviceStreams = 0;
    private int port = 0;

    private boolean remotePlay = false;//云存储录像

    public boolean isRemotePlay() {
        return remotePlay;
    }

    public void setRemotePlay(boolean remotePlay) {
        this.remotePlay = remotePlay;
    }

    public String getCanUseCloudStorage() {
        return canUseCloudStorage;
    }

    public void setCanUseCloudStorage(String canUseCloudStorage) {
        this.canUseCloudStorage = canUseCloudStorage;
    }

    public String getPrivateServer() {
        return PrivateServer;
    }

    public void setPrivateServer(String privateServer) {
        PrivateServer = privateServer;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getDeviceStreams() {
        return deviceStreams;
    }

    public void setDeviceStreams(int deviceStreams) {
        this.deviceStreams = deviceStreams;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isDirect() {
        return isDirect;
    }

    public void setDirect(boolean direct) {
        isDirect = direct;
    }

    public boolean isLocalePlayer() {
        return isLocalePlayer;
    }

    public void setLocalePlayer(boolean localePlayer) {
        isLocalePlayer = localePlayer;
    }

    public String getLocaleDeviceIp() {
        return localeDeviceIp;
    }

    public void setLocaleDeviceIp(String localeDeviceIp) {
        this.localeDeviceIp = localeDeviceIp;
    }

    public int getLocaleDevicePort() {
        return localeDevicePort;
    }

    public void setLocaleDevicePort(int localeDevicePort) {
        this.localeDevicePort = localeDevicePort;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        try {
            json.put("device_id", equpId);
            json.put("device_status", ifOnLine);
            json.put("bind_status", ifBind);
            json.put("device_model", deviceDetatilType);
            json.put("use_cloud_storage", useCloudStorage);
            json.put("can_use_cloud_storage", canUseCloudStorage);
            json.put("device_sdkver", version);
            json.put("device_verify", checkStr);
            json.put("device_name", deviceName);
            json.put("device_type", deviceType);
            json.put("device_firmware_ver", sysVersion);
            json.put("server_ip", deviceConnectServer);
            json.put("cate_id", cateId);
            json.put("local_pwd", localPwd);
            json.put("local_user", localUser);
            json.put("order_num", order_num);
            if(infoEntitys != null){
                JSONArray array = new JSONArray();
                for (ChannelInfoEntity c : infoEntitys){
                    array.put(c.toJson());
                }
                json.put("channelList",array);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static DeviceInfoBean parseLocaleSharedData(JSONObject object){
        return parse(object);
//		DeviceInfoBean info = new DeviceInfoBean();
//		if(object == null)return info;
//		info.setEqupId(object.optString("device_id"));
//		info.setIfOnLine(object.optString("device_status"));
//		info.setIfBind(object.optString("bind_status"));
//		info.setEquoModle(object.optString("device_model"));
//		info.setDeviceDetatilType(object.optString("device_model"));
//		info.setVersion(object.optString("device_sdkver"));
//		info.setCheckStr(object.optString("device_verify"));
//		info.setDeviceName(object.optString("device_name"));
//		info.setDeviceType(object.optString("device_type"));
//		info.setSysVersion(object.optString("device_firmware_ver"));
//		info.setDeviceConnectServer(object.optString("server_ip"));
//		info.setCateId(object.optString("cate_id"));
//		info.setLocalPwd(object.optString("local_pwd"));
//		info.setLocalUser(object.optString("local_user"));
//		JSONArray array = object.optJSONArray("channelList");
////		if(array != null){
////			ChannelInfoEntity infoEntity = null;
////			int length = array.length();
////			for(int i = 0; i < )
////		}
////		info.setInfoEntitys(ChannelInfoEntity.parseLocaleSharedData(object.optJSONObject("channelList")));
//		return info;
    }

    public String getGroupOrderNum() {
        return groupOrderNum;
    }

    public void setGroupOrderNum(String groupOrderNum) {
        this.groupOrderNum = groupOrderNum;
    }

    public String getChannelOrderNum(int  channel) {
        if(infoEntitys == null)return "1";
        LogUtil.debugLog("ChannelInfoEntity##getorder channel start");
        for (ChannelInfoEntity channelInfoEntity : infoEntitys){
            LogUtil.debugLog("ChannelInfoEntity##getorder channel = %s",channelInfoEntity.toString());
            if(channelInfoEntity.getChannel() == channel){
                LogUtil.debugLog("ChannelInfoEntity##getorder channel stop num = %d",channelInfoEntity.getOrder_num());
                return String.valueOf(channelInfoEntity.getOrder_num());
            }
        }
        LogUtil.debugLog("ChannelInfoEntity##getorder channel stop error");
        return channelOrderNum;
    }

    public String getChannelOrderName(int  channel) {
        if(infoEntitys == null)return "channel " + String.valueOf(channel);
        LogUtil.debugLog("ChannelInfoEntity##getorder channel start");
        for (ChannelInfoEntity channelInfoEntity : infoEntitys){
            LogUtil.debugLog("ChannelInfoEntity##getorder channel = %s",channelInfoEntity.toString());
            if(channelInfoEntity.getChannel() == channel){
                LogUtil.debugLog("ChannelInfoEntity##getorder channel stop num = %d",channelInfoEntity.getOrder_num());
                String name = channelInfoEntity.getChannel_name();
                if(TextUtils.isEmpty(name) || String.valueOf(channel).equals(name)){
                    return "channel " + String.valueOf(channel);
                } else {
                    return name;
                }
            }
        }
        LogUtil.debugLog("ChannelInfoEntity##getorder channel stop error");
        return "channel " + String.valueOf(channel);
    }

    public String getChannelOrderNamePlay(int  channel) {
        if(infoEntitys == null)return "channel " + String.valueOf(channel);
        LogUtil.debugLog("ChannelInfoEntity##getorder channel start");
        for (ChannelInfoEntity channelInfoEntity : infoEntitys){
            LogUtil.debugLog("ChannelInfoEntity##getorder channel = %s",channelInfoEntity.toString());
            if(channelInfoEntity.getChannel() == channel){
                LogUtil.debugLog("ChannelInfoEntity##getorder channel stop num = %d",channelInfoEntity.getOrder_num());
                String name = channelInfoEntity.getChannel_name();
                if(TextUtils.isEmpty(name) || String.valueOf(channel).equals(name)){
                    return "CH" + String.valueOf(channel);
                } else {
                    return name;
                }
            }
        }
        LogUtil.debugLog("ChannelInfoEntity##getorder channel stop error");
        return "CH" + String.valueOf(channel);
    }

    public void setChannelOrderNum(String channelOrderNum) {
        this.channelOrderNum = channelOrderNum;
    }

    public String getOrder_num() {
        return order_num;
    }

    public void setOrder_num(String order_num) {
        this.order_num = order_num;
    }

    public List<ChannelInfoEntity> getInfoEntitys() {
        return infoEntitys;
    }

    public void setInfoEntitys(ArrayList<ChannelInfoEntity> infoEntitys) {
        this.infoEntitys = infoEntitys;
    }

    public String getLocalPwd() {
        return localPwd;
    }

    public void setLocalPwd(String localPwd) {
        this.localPwd = localPwd;
    }

    public String getLocalUser() {
        return localUser;
    }

    public void setLocalUser(String localUser) {
        this.localUser = localUser;
    }

    public String getDeviceDetatilType() {
        return deviceDetatilType;
    }

    public void setDeviceDetatilType(String deviceDetatilType) {
        this.deviceDetatilType = deviceDetatilType;
    }

    public String getCateName() {
        return cateName;
    }

    public void setCateName(String cateName) {
        this.cateName = cateName;
    }

    public String getDeviceConnectServer() {
        return deviceConnectServer;
    }

    public void setDeviceConnectServer(String deviceConnectServer) {
        this.deviceConnectServer = deviceConnectServer;
    }

    public String getSysVersion() {
        return sysVersion;
    }

    public void setSysVersion(String sysVersion) {
        this.sysVersion = sysVersion;
    }

    public String getEqupId() {
        return equpId;
    }

    public void setEqupId(String equpId) {
        this.equpId = equpId;
    }

    public String getIfOnLine() {
        return ifOnLine;
    }

    public void setIfOnLine(String ifOnLine) {
        this.ifOnLine = ifOnLine;
    }

    public String getIfBind() {
        return ifBind;
    }

    public void setIfBind(String ifBind) {
        this.ifBind = ifBind;
    }

    public String getUseCloudStorage() {
        return useCloudStorage;
    }

    public void setUseCloudStorage(String useCloudStorage) {
        this.useCloudStorage = useCloudStorage;
    }

    public String getEquoModle() {
        return equoModle;
    }

    public String getEquoModleStr() {
        String result = "IPC";
        try{
            if (getEquoModle().equals("") || getEquoModle() == null) {
                return "IPC";
            }
            int type = Integer.parseInt(getEquoModle());
            if (type < 2000) {// IPC
                result = "IPC";
            } else if (type > 2000 && type < 2101) {// DVR��ͨ��
                result = "DVR-4";
            } else if (type > 2100 && type < 2201) {// DVR8ͨ��
                result = "DVR-8";
            } else if (type > 2200 && type < 2301) {// DVR16ͨ��
                result = "DVR-16";
            } else if (type > 2300 && type < 2401) {// DVR24ͨ��
                result = "DVR-25";
            } else if (type > 2400 && type < 2501) {// DVR32ͨ��
                result = "DVR-32";
            } else if (type > 4000 && type < 4101) {// NVR4ͨ��
                result = "NVR-4";
            } else if (type > 4100 && type < 4201) {// NVR8ͨ��
                result = "NVR-8";
            } else if (type > 4200 && type < 4301) {// NVR16ͨ��
                result = "NVR-16";
            } else if (type > 4300 && type < 4401) {// NVR24ͨ��
                result = "NVR-25";
            } else if (type > 4400 && type < 4501) {// NVR32ͨ��
                result = "NVR-32";
            } else if (type > 4500 && type < 4601) { // ����nvr-25
                result = "NVR-25";
            } else if (type > 4600 && type < 4701) { // ����nvr-9
                result = "NVR-9";
            } else if (type > 4700 && type < 4801) {
                result = "NVR-36";
            } else if (type > 4800 && type < 4901) {
                result = "NVR-64";
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public void setEquoModle(String equoModle) {
        this.equoModle = equoModle;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCheckStr() {
        return checkStr;
    }

    public void setCheckStr(String checkStr) {
        this.checkStr = checkStr;
    }

    public String getDeviceName() {
        if(TextUtils.isEmpty(deviceName)){
            return equpId;
        }
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getCateId() {
        return cateId;
    }

    public void setCateId(String cateId) {
        this.cateId = cateId;
    }

    public static DeviceInfoBean parse(String str) {
        if (TextUtils.isEmpty(str))
            return null;
        try {
            JSONObject array = new JSONObject(str);
            if (array != null) {
                return parse(array);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static DeviceInfoBean parse(JSONObject object) {
        if (object == null)
            return null;
        LogUtil.e("DeviceInfoBean==", String.valueOf(object));
        DeviceInfoBean info = new DeviceInfoBean();
        info.setEqupId(object.optString("device_id"));
        info.setIfOnLine(object.optString("device_status"));
        info.setIfBind(object.optString("bind_status"));
        info.setEquoModle(SynscDeviceModle(object.optString("device_model")));
        info.setDeviceDetatilType(object.optString("device_model"));
        info.setVersion(object.optString("device_sdkver"));
        info.setCheckStr(object.optString("device_verify"));
        info.setDeviceName(object.optString("device_name"));
        info.setDeviceType(object.optString("device_type"));
        info.setSysVersion(object.optString("device_firmware_ver"));
        info.setDeviceConnectServer(object.optString("server_ip"));
        info.setCateId(object.optString("cate_id")); // ����cateId
        info.setLocalPwd(object.optString("local_pwd"));
        info.setLocalUser(object.optString("local_user"));
        info.setOrder_num(object.optString("order_num"));
        info.setUseCloudStorage(object.optString("use_cloud_storage"));
        info.setCanUseCloudStorage(object.optString("can_use_cloud_storage"));
        JSONArray array = object.optJSONArray("channelList");
        if(array != null){
            ArrayList<ChannelInfoEntity> list = new ArrayList<>();
            ChannelInfoEntity infoEntity = null;
            int length = array.length();
            for (int i = 0; i < length; i++){
                infoEntity = ChannelInfoEntity.parse(array.optJSONObject(i));
                if(infoEntity != null){
//					LogUtil.debugLog("ChannelInfoEntity##str = %s",infoEntity.toString());
                    list.add(infoEntity);
                }
            }
            info.setInfoEntitys(list);
        }
        return info;
    }

    /**
     * 返回结果做了修改 为了兼容之前的UI层 在此做数据转换
     *
     * @param deviceModle
     * @return
     */
    public static String SynscDeviceModle(String deviceModle) {
        String modle = "";
        if (deviceModle == null)
            return "1";
        if (deviceModle.startsWith("IPC")) {
            modle = "1000";
        } else if (deviceModle.contains("_")) {
            if (deviceModle.split("_")[0].equals("DVR")) {
                if (Integer.parseInt(deviceModle.split("_")[1]) == 4) {
                    modle = "2010";
                } else if (Integer.parseInt(deviceModle.split("_")[1]) == 8) {
                    modle = "2110";
                } else if (Integer.parseInt(deviceModle.split("_")[1]) == 16) {
                    modle = "2210";
                } else if (Integer.parseInt(deviceModle.split("_")[1]) == 25) {
                    modle = "2310";
                } else if (Integer.parseInt(deviceModle.split("_")[1]) == 32) {
                    modle = "2410";
                } else { // 无法识别的路数 默认为4
                    modle = "2010";
                }
            } else { // 其他统称为NVR
                if (Integer.parseInt(deviceModle.split("_")[1]) == 4) {
                    modle = "4010";
                } else if (Integer.parseInt(deviceModle.split("_")[1]) == 8) {
                    modle = "4110";
                } else if (Integer.parseInt(deviceModle.split("_")[1]) == 16) {
                    modle = "4210";
                } else if (Integer.parseInt(deviceModle.split("_")[1]) == 25) {
                    modle = "4310";
                } else if (Integer.parseInt(deviceModle.split("_")[1]) == 32) {
                    modle = "4410";
                } else if (Integer.parseInt(deviceModle.split("_")[1]) == 9) {
                    modle = "4610";
                } else if (Integer.parseInt(deviceModle.split("_")[1]) == 36) {
                    modle = "4710";
                } else if (Integer.parseInt(deviceModle.split("_")[1]) == 64) {
                    modle = "4810";
                } else { // 无法识别的路数 默认为4
                    modle = "4010";
                }
            }
        } else if (deviceModle.equals("")) {
            modle = "1";
        }

        return modle;
    }

    public int getChannelSum(){
        return createTotalChannel(getEquoModle());
    }

    private int createTotalChannel(String deviceModle) {
        int modle = Integer.parseInt(deviceModle);
        int chann = -1;
        if ((modle > 2000 && modle < 2101) || (modle > 4000 && modle < 4101)) {
            chann = 4;
        } else if ((modle > 2100 && modle < 2201) || (modle > 4100 && modle < 4201)) {
            chann = 8;
        } else if ((modle > 2200 && modle < 2301) || (modle > 4200 && modle < 4301)) {
            chann = 16;
        } else if ((modle > 2300 && modle < 2401)) {
            chann = 24;
        } else if ((modle > 2400 && modle < 2501) || (modle > 4400 && modle < 4501)) {
            chann = 32;
        } else if ((modle > 4500 && modle < 4601) || (modle > 4300 && modle < 4401)) {
            chann = 25;
        } else if ((modle > 4600 && modle < 4701)) {
            chann = 9;
        } else if ((modle > 4700 && modle < 4801)) {
            chann = 36;
        } else if ((modle > 4800 && modle < 4901)) {
            chann = 64;
        } else {
            chann = 4;
        }

        return chann;
    }

    @Override
    public String toString() {
        return "DeviceInfoBean{" +
                "\n equpId='" + equpId + '\'' +
                ",\n ifOnLine='" + ifOnLine + '\'' +
                ",\n ifBind='" + ifBind + '\'' +
                ",\n equoModle='" + equoModle + '\'' +
                ",\n version='" + version + '\'' +
                ",\n checkStr='" + checkStr + '\'' +
                ",\n deviceName='" + deviceName + '\'' +
                ",\n deviceType='" + deviceType + '\'' +
                ",\n sysVersion='" + sysVersion + '\'' +
                ",\n deviceDetatilType='" + deviceDetatilType + '\'' +
                ",\n cateId='" + cateId + '\'' +
                ",\n useCloudStorage='" + useCloudStorage + '\'' +
                ",\n canUseCloudStorage='" + canUseCloudStorage + '\'' +
                ",\n cateName='" + cateName + '\'' +
                ",\n deviceConnectServer='" + deviceConnectServer + '\'' +
                ",\n localPwd='" + localPwd + '\'' +
                ",\n localUser='" + localUser + '\'' +
                ",\n infoEntitys=" + infoEntitys +
                ",\n order_num='" + order_num + '\'' +
                ",\n groupOrderNum='" + groupOrderNum + '\'' +
                ",\n channelOrderNum='" + channelOrderNum + '\'' +
                ",\n isLocalePlayer=" + isLocalePlayer +
                ",\n localeDeviceIp='" + localeDeviceIp + '\'' +
                ",\n localeDevicePort=" + localeDevicePort +
                ",\n isDirect=" + isDirect +
                ",\n PrivateServer='" + PrivateServer + '\'' +
                ",\n mode=" + mode +
                ",\n deviceStreams=" + deviceStreams +
                ",\n port=" + port +
                '}';
    }

}
