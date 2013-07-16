package cn.chdany.clean.tool;

import java.util.HashMap;
import java.util.Map;

/**
 * count the time cost for some specific action
 * @author chdany
 *
 */
public class TimeAnalysis {
	
	private static Map<String,Long> timeCostMap = new HashMap<String, Long>();

	private static boolean DEBUG = true;
	
	public static void startAnalysis(String tag){
		if(DEBUG ){
			long currentTimeMillis = System.currentTimeMillis();
			timeCostMap.put(tag, currentTimeMillis);
			Logger.log(tag + "  begins at ---------------------------" + currentTimeMillis);
			Logger.log("please wait ... ");
		}
	}
	
	public static void endAnalysis(String tag){
		if(DEBUG){
			long currentTimeMillis = System.currentTimeMillis();
			Long startTime = timeCostMap.get(tag);
			Logger.log(tag + "  ends at ---------------------------" + currentTimeMillis);
			Logger.log(tag + "  costs --------------------------- mills"
					+ (currentTimeMillis - startTime));
			timeCostMap.remove(tag);
		}
	}
	
	
}
