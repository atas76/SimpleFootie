package com.simplefootie.util;

public class CmdLineParser {
	
	private String arg;
	
	public CmdLineParser(String arg) {
		this.arg = arg;
	}
	
	public int anticipate(int index, char ch) {
		for (int i = index; i < arg.length(); i++) {
			if (Character.isWhitespace(arg.charAt(i))) {
				continue;
			} else if (arg.charAt(i) == ch) {
				return i;
			} else {
				return -1;
			}
		}
		return -1;
	}
	
	public int anticipateDigit(int index) {
		for (int i = index; i < arg.length(); i++) {
			if (Character.isWhitespace(arg.charAt(i))) {
				continue;
			} else if (Character.isDigit(arg.charAt(i))) {
				return i;
			} else {
				return -1;
			}
		}
		return -1;
	}
}
