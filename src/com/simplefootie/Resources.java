package com.simplefootie;

import java.util.HashMap;
import java.util.Map;

public class Resources {
	
	private static String resourcesDataRoot = "./resources/data";
	
	public static void setResourcesDataRoot(String resourcesDataRoot) {
		Resources.resourcesDataRoot = resourcesDataRoot;
	}
	
	public static final String DB = "db";
	public static final String HOME_SCORED_AVG = "homeScoredAvg";
	public static final String HOME_CONCEDED_AVG = "homeConcededAvg";
	
	private static final Map<String, String> dataResources = new HashMap<String, String>();
	
	static {
		dataResources.put(DB, "db.xml");
		dataResources.put(HOME_SCORED_AVG, "scored averages.sjon");
		dataResources.put(HOME_CONCEDED_AVG, "conceded averages.sjon");
	}
	
	public static String getDataResource(String resourceName) {
		return resourcesDataRoot + "/" + dataResources.get(resourceName); 
	}
}
