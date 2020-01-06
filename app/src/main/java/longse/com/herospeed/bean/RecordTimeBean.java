package longse.com.herospeed.bean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RecordTimeBean {

	private int fileType;
	
	private long startTime;
	
	private long endTime;
	
	private int totalTime;

	public int getFileType() {
		return fileType;
	}

	public void setFileType(int fileType) {
		this.fileType = fileType;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public int getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(int totalTime) {
		this.totalTime = totalTime;
	}

	public static List<RecordTimeBean> SyncRecordTime(String jsonString){
		List<RecordTimeBean> recordTimes = new ArrayList<RecordTimeBean>();
		try{
			JSONArray array = new JSONArray(jsonString);
			System.out.println("array length is :::"+array.length());
			for(int i =0;i<array.length();i++ ){
				RecordTimeBean recordTime = new RecordTimeBean();
				JSONObject object = array.getJSONObject(i);
				recordTime.setEndTime(object.getLong("ET"));
				recordTime.setStartTime(object.getLong("ST"));
				recordTime.setTotalTime(object.getInt("TT"));
				recordTime.setFileType(object.optInt("file_type",4));
				if(recordTime.getStartTime() == 0){
					System.out.println("starttime is 0");
				}else {
				
				  recordTimes.add(recordTime);
				}
			}
		}catch(Exception e){
			
			e.printStackTrace();
		}
		
		return recordTimes;
		
	}

	@Override
	public String toString() {
		return "RecordTimeBean{" +
				"fileType=" + fileType +
				"startTime=" + startTime +
				", endTime=" + endTime +
				", totalTime=" + totalTime +
				'}';
	}
}
