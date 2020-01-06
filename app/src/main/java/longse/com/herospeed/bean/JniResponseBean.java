package longse.com.herospeed.bean;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JniResponseBean {
	
	private String type ;
	
	private String deviceId ;
	
	private int responseCode ;
	
	private int channelId;
	
	private int HD;
	
	private int BD;
	
	private int fluent ;
	
	private int percent;

	public static String jsonData;
	
	private List<RecordTimeBean> recordTimes = new ArrayList<RecordTimeBean>();

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public int getChannelId() {
		return channelId;
	}

	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}

	public int getHD() {
		return HD;
	}

	public void setHD(int hD) {
		HD = hD;
	}

	public int getBD() {
		return BD;
	}

	public void setBD(int bD) {
		BD = bD;
	}

	public int getFluent() {
		return fluent;
	}

	public void setFluent(int fluent) {
		this.fluent = fluent;
	}

	public int getPercent() {
		return percent;
	}

	public void setPercent(int percent) {
		this.percent = percent;
	}
	
	
	
	
	public List<RecordTimeBean> getRecordTimes() {
		return recordTimes;
	}

	public void setRecordTimes(List<RecordTimeBean> recordTimes) {
		this.recordTimes = recordTimes;
	}

	public JniResponseBean getPaserResponse(String jsonString){
		jsonData = jsonString;
		JniResponseBean bean = new JniResponseBean();
		try{
			JSONObject object = new JSONObject(jsonString);

			bean.setType(object.getString("type"));
			
			if(object.has("code")){
				bean.setResponseCode(object.getInt("code"));
			}
			
			if(object.has("device_id")){
				bean.setDeviceId(object.getString("device_id"));
			}
			if(object.has("channel_id")){
				bean.setChannelId(object.getInt("channel_id"));
			}
			if(object.has("HD")){
				bean.setHD(object.getInt("HD"));
			}
			if(object.has("BD")){
				bean.setBD(object.getInt("BD"));
			}
			if(object.has("fluent")){
				bean.setFluent(object.getInt("fluent"));
			}
			if(object.has("percent")){
				bean.setPercent(object.getInt("percent"));
			}
			if(object.has("data")){
				bean.setRecordTimes(RecordTimeBean.SyncRecordTime(object.getString("data")));
				System.out.println("record time size is :::::::::"+bean.getRecordTimes().size());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return bean ;
	}

	@Override
	public String toString() {
		return "JniResponseBean{" +
				"type='" + type + '\'' +
				", deviceId='" + deviceId + '\'' +
				", responseCode=" + responseCode +
				", channelId=" + channelId +
				", HD=" + HD +
				", BD=" + BD +
				", fluent=" + fluent +
				", percent=" + percent +
				", recordTimes=" + recordTimes +
				'}';
	}
}
