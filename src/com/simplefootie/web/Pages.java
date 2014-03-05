package com.simplefootie.web;

public class Pages {
	
	/**
	 * A bit of a baroque attempt to imitate the desktop navigation management. Doesn't quite well work with web and abandoned for subsequent workflow.
	 * It is supposed to return the name of the next page in the workflow, based on the current page and the user's selection.
	 * 
	 * @param page The name of the current page
	 * @param option A string denoting the user's option
	 * @return the name of the next page
	 */
	public static String get(String page, String option) {
		
		if (option == null) {
			return null;
		}

		switch(page) {
		case "main":
			switch(option) {
			case "friendly":
					return "SelectLeague";
			case "logout":
					return "Logout";
			}
		}
		
		return null;
	}

}
