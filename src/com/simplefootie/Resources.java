package com.simplefootie;

import java.util.HashMap;
import java.util.Map;

public class Resources {
	
	private static String resourcesDataRoot = "./resources/data";
	
	public static void setResourcesDataRoot(String resourcesDataRoot) {
		Resources.resourcesDataRoot = resourcesDataRoot;
	}
	
	public static final String DB = "db";
	public static final String UNIVERSAL_SCORE_SAMPLES = "uniScoreSamples";
	public static final String TEST_SCORE_SAMPLES = "testScoreSamples";
	
	private static final Map<String, String> dataResources = new HashMap<String, String>();
	
	static {
		dataResources.put(DB, "db.xml");
		dataResources.put(UNIVERSAL_SCORE_SAMPLES, "universal.sco");
		dataResources.put(TEST_SCORE_SAMPLES, "test.sco");
	}
	
	public static String getDataResource(String resourceName) {
		return resourcesDataRoot + "/" + dataResources.get(resourceName); 
	}
}
