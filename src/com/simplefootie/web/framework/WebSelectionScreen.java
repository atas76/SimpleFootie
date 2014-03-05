package com.simplefootie.web.framework;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Andreas Tasoulas
 *
 */
public class WebSelectionScreen {

	private Map<String, String> options;
	private String servletName;
	private String genericName;
	private String context;
	
	/**
	 * Constructor
	 * 
	 * @param options a map containing the user options with the key the option identifier and value the label of the option in the view 
	 * @param servletName the name of the servlet which will process the option
	 * @param genericName a generic name used to identify the current workflow stage
	 * @param context used as an identifier for the current workflow stage from a domain perspective. Not to be confused with the servlet context.
	 */
	public WebSelectionScreen(Map<String, String> options, String servletName, String genericName, String context) {
		this.options = options;
		this.servletName = servletName;
		this.genericName = genericName;
		this.context = context;
	}
	
	/**
	 * Generates the html code for the current web selection page.
	 * 
	 * @return html code for the current page with dynamic parts filled by this object's fields values
	 */
	public String generateHTML() {
		
		StringBuilder html = new StringBuilder();
		
		html.append("<html>");
			
			html.append("<head>");
				html.append("<title>");
					html.append(genericName + " page");
				html.append("</title>");
			html.append("</head>");
			
			html.append("<body>");
				
				html.append("<form name='" + genericName + "Frm' action='./" + servletName + "' method='POST'>");
				
					html.append("<input type='hidden' name='optionSrc' value='" + context + "'/>");
				
				for (Map.Entry<String, String> option:options.entrySet()) {
					html.append("<label for='" + option.getKey() + "'>" + option.getValue() + "</label>" );
					html.append("<input type='radio' name='" + genericName + "' value='" + option.getKey() + "' id='" + option.getKey());
					html.append("'/>");
					html.append("<p/>");
				}
			
					html.append("<input type='submit' name='" + genericName + "Submit' value='Submit'/>");
				
				html.append("</form>");
			
			html.append("</body>");
		
		html.append("</html>");
				
		return html.toString();
	}
}
