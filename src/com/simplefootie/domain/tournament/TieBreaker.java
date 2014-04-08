package com.simplefootie.domain.tournament;

public enum TieBreaker {
	
	REPLAY("replay"),
	EXTRA_TIME("extra time"),
	PENALTY_SHOOUTOUT("penalties"),
	AWAY_GOALS("away goals"),
	BETWEEN_MATCHES("between matches"),
	GOAL_DIFFERENCE("goal difference"),
	GOALS_SCORED("goals scored"),
	SEEDING("seeding");
	
	private String description;
	
	private TieBreaker(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return this.description;
	}
}
