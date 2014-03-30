package com.simplefootie.domain;

public class Score {
	
	private int homeTeamRanking;
	private int awayTeamRanking;
	private int homeScore;
	private int awayScore;
	
	public Score(int homeTeamRanking, int awayTeamRanking, int homeScore, int awayScore) {
		this.homeTeamRanking = homeTeamRanking;
		this.awayTeamRanking = awayTeamRanking;
		this.homeScore = homeScore;
		this.awayScore = awayScore;
	}
	
	public String toString() {
		return String.format("{%d,%d,%d,%d}", this.homeTeamRanking, this.awayTeamRanking, this.homeScore, this.awayScore);
	}
	
	public double getCapacityRatio() {
		return (double) this.homeTeamRanking / this.awayTeamRanking;
	}
	
	public int getHomeScore() {
		return this.homeScore;
	}
	
	public int getAwayScore() {
		return this.awayScore;
	}
}
