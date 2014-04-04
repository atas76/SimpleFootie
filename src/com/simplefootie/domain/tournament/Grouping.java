package com.simplefootie.domain.tournament;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.simplefootie.domain.Ground;
import com.simplefootie.domain.Match;
import com.simplefootie.domain.Team;

public class Grouping {
	
	private List<Team> teams;
	private List<Team> winners;
	
	private List<List<Match>> schedule;
	
	public Grouping(int size) {
		this.teams = new ArrayList<Team>(size);
	}
	
	public void addTeam(Team team) {
		this.teams.add(team);
	}
	
	public Team getTeam(int index) {
		return this.teams.get(index);
	}
	
	public List<Team> getTeams() {
		return this.teams;
	}
	
	public List<Team> getWinners() {
		return this.winners;
	}
	
	private static Random rnd = new Random();
	
	/**
	 * Creates the schedule for the teams involved in the 'grouping'
	 * 
	 * @return list of list of matches. Each list of matches will belong to a 'level' corresponding to match day, so that the actual calendar schedule can be deducted
	 */
	public List<List<Match>> createSchedule(StageType type, PairingType pairingType) {
		
		// Just setting the scene; they are the only options we support currently
		if (type.equals(StageType.KNOCKOUT)) {
			if (type.equals(PairingType.DOUBLE_MATCH)) {
				// We create two levels of matches/match days, each corresponding to a double match tie leg
				// Use the label for grouping the fixtures in the view, instead of using a generic label for all matches (it will make a difference when we will have groups)
				Match firstLeg = new Match(this.teams.get(0), this.teams.get(1), Ground.HOME_GROUND, "First leg");
				Match secondLeg = new Match(this.teams.get(1), this.teams.get(0), Ground.HOME_GROUND, "Second leg");
				
				List<Match> firstLegMatches = new ArrayList<Match>();
				List<Match> secondLegMatches = new ArrayList<Match>();
				
				firstLegMatches.add(firstLeg);
				secondLegMatches.add(secondLeg);
				
				schedule.add(firstLegMatches);
				schedule.add(secondLegMatches);
			} else if (type.equals(PairingType.SINGLE_MATCH_NEUTRAL)) {
				Match singleMatch = new Match(this.teams.get(0), this.teams.get(1), Ground.NEUTRAL_GROUND, "Single match");
				List<Match> matches = new ArrayList<Match>();
				matches.add(singleMatch);
				schedule.add(matches);
			}
		}
		return this.schedule;
	}
	
	private boolean applyTieBreakers(TieBreaker tieBreaker, Match decidingMatch) {
		
		if (tieBreaker.equals(TieBreaker.AWAY_GOALS)) {
				
			int homeTeamAwayGoals = this.schedule.get(1).get(0).getAwayTeamScore();
			int awayTeamAwayGoals = this.schedule.get(0).get(0).getAwayTeamScore();
				
			if (homeTeamAwayGoals > awayTeamAwayGoals) {
				this.winners.add(this.teams.get(0));
				return true;
			} else if (awayTeamAwayGoals > homeTeamAwayGoals) {
				this.winners.add(this.teams.get(1));
				return true;
			} else if (homeTeamAwayGoals == awayTeamAwayGoals) {
				return false;
			}
				
		} else if (tieBreaker.equals(TieBreaker.EXTRA_TIME)) {
				
			decidingMatch.playExtraTime();
			// Don't go back to the generic grouping calculation, just peek at the deciding match result
			// Careful now with the home team definition: deciding match home team <> grouping home team
			int homeTeamScore = decidingMatch.getHomeTeamScoreAET();
			int awayTeamScore = decidingMatch.getAwayTeamScoreAET();
				
			if (homeTeamScore > awayTeamScore) {
				this.winners.add(decidingMatch.getHomeTeam());
				return true;
			} else if (awayTeamScore > homeTeamScore) {
				this.winners.add(decidingMatch.getAwayTeam());
				return true;
			} else if (homeTeamScore == awayTeamScore) {
				return false;
			}
		} else if (tieBreaker.equals(TieBreaker.PENALTY_SHOOUTOUT)) {
				
			decidingMatch.penaltyShootOut();
			int homeTeamPenaltyShootOutScore = decidingMatch.getHomeTeamPenaltyShootOutScore();
			int awayTeamPenaltyShootOutScore = decidingMatch.getAwayTeamPenaltyShootOutScore();
				
			if (homeTeamPenaltyShootOutScore > awayTeamPenaltyShootOutScore) {
				this.winners.add(decidingMatch.getHomeTeam());
			} else if (awayTeamPenaltyShootOutScore > homeTeamPenaltyShootOutScore) {
				this.winners.add(decidingMatch.getAwayTeam());
			}
			return true;
		}
		boolean coinTossOutcome = rnd.nextBoolean();
		if (coinTossOutcome) {
			this.winners.add(decidingMatch.getHomeTeam());
		} else {
			this.winners.add(decidingMatch.getAwayTeam());
		}
		return true;
	}
	
	public void calculateGroupingOutcome(Match match, List<TieBreaker> tieBreakers, StageType stageType, PairingType pairingType) {
		if (stageType.equals(StageType.KNOCKOUT)) {
			if (pairingType.equals(PairingType.DOUBLE_MATCH)) { 
				// As a convention, 'home team' of the grouping is the home team of the first leg 
				int homeTeamScore = this.schedule.get(0).get(0).getHomeTeamScore() + this.schedule.get(1).get(0).getAwayTeamScore();
				int awayTeamScore = this.schedule.get(0).get(0).getAwayTeamScore() + this.schedule.get(1).get(0).getHomeTeamScore();
				
				if (homeTeamScore > awayTeamScore) {
					winners.add(this.teams.get(0));
				} else if (awayTeamScore > homeTeamScore) {
					winners.add(this.teams.get(1));
				} else if (homeTeamScore == awayTeamScore) {
					
					boolean tieBroken = false;
					
					int tieBreakerIndex = 0;
					while (!tieBroken) {
						tieBroken = applyTieBreakers(tieBreakers.get(tieBreakerIndex++), match);
					}
				}
			} else if (pairingType.equals(PairingType.SINGLE_MATCH_NEUTRAL)) {
				
				int homeTeamScore = this.schedule.get(0).get(0).getHomeTeamScore();
				int awayTeamScore = this.schedule.get(0).get(0).getAwayTeamScore();
				
				if (homeTeamScore > awayTeamScore) {
					winners.add(this.teams.get(0));
				} else if (homeTeamScore < awayTeamScore) {
					winners.add(this.teams.get(1));
				} else if (homeTeamScore == awayTeamScore) {
					
					boolean tieBroken = false;
					
					int tieBreakerIndex = 0;
					while (!tieBroken) {
						tieBroken = applyTieBreakers(tieBreakers.get(tieBreakerIndex++), match);
					}
				}
			}
		}
	}
	
	@Override
	public String toString() {
		if (this.teams.size() == 2) {
			return this.teams.get(0).getName() + " - " + this.teams.get(1).getName();
		}
		return "undefined (support only for team pairs at the moment)";
	}
	
}
