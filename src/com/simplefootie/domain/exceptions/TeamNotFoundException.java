package com.simplefootie.domain.exceptions;

public class TeamNotFoundException extends Exception {
	
	private String shortName;
	
	public TeamNotFoundException(String shortName) {
		this.shortName = shortName;
	}
	
	public String getTeamShortName() {
		return this.shortName;
	}
}
