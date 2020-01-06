package longse.com.herospeed.utils;

import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class JniRequestUtils {
	
	public static String getRequestToJni(Map<String, Object> param){
		
		return getPostNameParma(param);
		
	}
	/**
	 * 解析参数
	 * @param params
	 * @return
	 */
	public static String getPostNameParma(Map<String, Object> params){
		try{
		JSONObject jobject = new JSONObject();
		
		
		Set<Entry<String, Object>> set = params.entrySet();
		Iterator<Entry<String, Object>> it = set.iterator();
		
		  while(it.hasNext()){						
				Entry<String, Object> entry = it.next();
				jobject.put(entry.getKey(), entry.getValue());	
		  }
		  return jobject.toString();
		}catch(Exception e){
			e.printStackTrace();
			
			return "";
		}			
	}

}
