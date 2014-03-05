package com.simplefootie.web.framework;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author Andreas Tasoulas
 *
 */
public class WebDisplayScreen {
	
	private String filename;
	private Map<String, String> params;
	
	public WebDisplayScreen(String filename, Map<String, String> params) {
		this.filename = filename;
		this.params = params;
	}
	
	public String generateHTML() throws FileNotFoundException, IOException {
		
		StringBuilder html = new StringBuilder();
		
		BufferedReader bufferedReader = new BufferedReader(new FileReader("data/" + filename + ".csp"));
		
		Pattern pattern = Pattern.compile("<$[A-Za-z][A-Za-z0-9]*>");
		
		String currentLine = null;
		while ((currentLine = bufferedReader.readLine()) != null) {
			
			Matcher matcher = pattern.matcher(currentLine);
			
			Map<Integer, String> variableTokens = new TreeMap<Integer, String>();
			
			while (matcher.find()) {
				variableTokens.put(matcher.start(), matcher.group());
			}
			
			StringBuilder outputLineBuilder = new StringBuilder();
			String outputLine;
			
			if (variableTokens.size() == 0) {
				outputLine = currentLine;
			} else {
				int currentIndex = 0;
				for (Map.Entry<Integer, String> variableToken:variableTokens.entrySet()) {
					outputLineBuilder.append(currentLine.substring(currentIndex, variableToken.getKey()));
					outputLineBuilder.append(params.get(variableToken.getValue().substring(2, variableToken.getValue().length() - 1)));
					currentIndex = variableToken.getKey() + variableToken.getValue().length();
				}
				outputLineBuilder.append(currentLine.substring(currentIndex));
				outputLine = outputLineBuilder.toString();
			}
			
			html.append(outputLine);
		}
		
		return html.toString();
	}

}
