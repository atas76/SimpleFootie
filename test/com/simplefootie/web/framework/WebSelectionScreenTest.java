package com.simplefootie.web.framework;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import com.simplefootie.web.framework.WebSelectionScreen;

public class WebSelectionScreenTest {
	
	private static WebSelectionScreen webSelectionScreen;
	
	@BeforeClass
	public static void setUp() {
		
		Map<String, String> options = new HashMap<String, String>();
		
		options.put("Bundesliga", "Bundesliga");
		options.put("Premier League", "Premier League");
		options.put("La Liga", "La Liga");
		
		webSelectionScreen = new WebSelectionScreen(options, "selectLeague", "leagueSelection", "homeFriendly");
	}
	
	@Test
	public void testGenerateHTML() {
		System.out.println(webSelectionScreen.generateHTML());
	}
}
