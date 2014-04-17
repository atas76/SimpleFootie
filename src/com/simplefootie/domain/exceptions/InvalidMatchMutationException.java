package com.simplefootie.domain.exceptions;

public class InvalidMatchMutationException extends Exception {
	
	private String homeTeamName;
	private String awayTeamName;
	private String newTeamName;
	
	public InvalidMatchMutationException(String homeTeamName, String awayTeamName, String newTeamName) {
		this.homeTeamName = homeTeamName;
		this.awayTeamName = awayTeamName;
		this.newTeamName = newTeamName;
	}
	
	@Override
	public String getMessage() {
		return "Updating match with complete details: " + homeTeamName + " - " + awayTeamName + " new team: " + newTeamName;
	}
}
