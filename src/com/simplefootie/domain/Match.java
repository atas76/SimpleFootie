package com.simplefootie.domain;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.simplefootie.data.DataException;
import com.simplefootie.data.MatchData;
import com.simplefootie.domain.exceptions.InvalidTeamRankingException;
import com.simplefootie.outcomes.StatsOutcome;


/**
 * Mainly used as a data structure to contain details of a specific match, and the method for calculating its result.
 * 
 * @author Andreas Tasoulas
 *
 */
public class Match {
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	private String label;
	
	private Team homeTeam;
	private Team awayTeam;
	
	private Ground venue;
	
	private int homeTeamScore = 0;
	private int awayTeamScore = 0;
	
	/**
	 * Initialize match, passing the team objects directly.
	 * 
	 * @param homeTeam the home team object
	 * @param awayTeam the away team object
	 * @param venue either the home team's venue or a neutral one
	 * @param label to be used as a description of the match, but most importantly used to lookup the teams' details by specifying the competition in which they belong
	 */
	public Match(Team homeTeam, Team awayTeam, Ground venue, String label) {
		this.homeTeam = homeTeam;
		this.awayTeam = awayTeam;
		this.venue = venue;
		this.label = label;
	}
	
	/**
	 * Match object initialization.
	 * 
	 * @param homeTeamName the name of the home team (or the team that is designated as 'home')
	 * @param awayTeamName the name of the away team
	 * @param venue either the home team's venue or a neutral one
	 * @param label to be used as a description of the match, but most importantly used to lookup the teams' details by specifying the competition in which they belong 
	 */
	public Match(String homeTeamName, String awayTeamName, Ground venue, String label) {
		this.homeTeam = Environment.getCompetition(label).getTeamByName(homeTeamName);
		this.awayTeam = Environment.getCompetition(label).getTeamByName(awayTeamName);
		this.venue = venue;
		this.label = label;
	}
	
	/**
	 * Default constructor. We use this default constructor for setting the match details later. Where possible the match object should be treated as immutable and 
	 * the other constructor should be used, however this is not the case for the web application of this project.
	 */
	public Match() {
	}

	public void setHomeTeam(String homeTeamName) {
		this.homeTeam = Environment.getCompetition(label).getTeamByName(homeTeamName);
	}
	
	public void setAwayTeam(String awayTeamName) {
		this.awayTeam = Environment.getCompetition(label).getTeamByName(awayTeamName);
	}
	
	public void setVenue(Ground venue) {
		this.venue = venue;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public String getHomeTeamName() {
		return this.homeTeam.getName();
	}
	
	public String getAwayTeamName() {
		return this.awayTeam.getName();
	}
	
	public Ground getVenue() {
		return this.venue;
	}
	
	public String getLabel() {
		return this.label;
	}
	
	public int getHomeTeamScore() {
		return this.homeTeamScore;
	}
	
	public int getAwayTeamScore() {
		return this.awayTeamScore;
	}
	
	/**
	 * Displays the result of the match: <home team name> - <away team name> <home score> - <away score>
	 */
	public void displayResult() {
		System.out.println(this.homeTeam.getName() + " - " + this.awayTeam.getName() + "   " + homeTeamScore + "-" + awayTeamScore);
	}
	
	/**
	 * Utility method for validating that the match object has all the details necessary for generating its result.
	 * 
	 * @return True if both opponent team objects are not null, false otherwise (the match result cannot be generated at this point)
	 */
	public boolean hasCompleteDetails() {
		return (this.homeTeam != null && this.awayTeam != null);
	}
	
	/**
	 * Calculates a single match result and saves the score internally. This means that a match result can be calculated many times, but only the last score is saved.
	 * 
	 * @param competition The competition for which the match takes place. A competition object can mean many things from a programmatic perspective, but in this method
	 * it carries a data model (relative strength of teams within the competition and mapping to a score distribution), based on which the result will be calculated.
	 * @throws InvalidTeamRankingException Thrown if the ranking of either of the teams is not a legitimate one (positive and non-zero).
	 * @throws DataException Thrown if there is a problem reading match-related data from the data source
	 */
	public void calculateResult(Competition competition) throws InvalidTeamRankingException, DataException {
		
		int homeTeamRanking = competition.getTeamRanking(this.homeTeam.getName());
		int awayTeamRanking = competition.getTeamRanking(this.awayTeam.getName());
		
		// logger.setLevel(Level.CONFIG);
		
		logger.config("Home team ranking: " + homeTeamRanking);
		logger.config("Away team ranking: " + awayTeamRanking);
		
		if (homeTeamRanking < 1 || awayTeamRanking < 1) {
			throw new InvalidTeamRankingException();
		}
		
		double capacityDivergence = (double) homeTeamRanking / (double) awayTeamRanking;
		
		logger.config("Capacity divergence: " + capacityDivergence);
		
		Map<Integer, Integer> goalDifferenceDistribution1;
		Map<Integer, Integer> scoreEntropiesDistribution1;
		
		Map<Integer, Integer> goalDifferenceDistribution2;
		Map<Integer, Integer> scoreEntropiesDistribution2;
		
		if (capacityDivergence <= 0.5) {
			
			goalDifferenceDistribution1 = competition.getGoalDifferenceDistribution(0);
			scoreEntropiesDistribution1 = competition.getEntropiesDistribution(0);
			
			goalDifferenceDistribution2 = competition.getGoalDifferenceDistribution(3);
			scoreEntropiesDistribution2 = competition.getEntropiesDistribution(3);
			
		} else if (capacityDivergence <= 1) {
			
			goalDifferenceDistribution1 = competition.getGoalDifferenceDistribution(1);
			scoreEntropiesDistribution1 = competition.getEntropiesDistribution(1);
			
			goalDifferenceDistribution2 = competition.getGoalDifferenceDistribution(2);
			scoreEntropiesDistribution2 = competition.getEntropiesDistribution(2);
			
		} else if (capacityDivergence <= 2) {
			
			goalDifferenceDistribution1 = competition.getGoalDifferenceDistribution(2);
			scoreEntropiesDistribution1 = competition.getEntropiesDistribution(2);
			
			goalDifferenceDistribution2 = competition.getGoalDifferenceDistribution(1);
			scoreEntropiesDistribution2 = competition.getGoalDifferenceDistribution(1);
			
		} else {
			
			goalDifferenceDistribution1 = competition.getGoalDifferenceDistribution(3);
			scoreEntropiesDistribution1 = competition.getEntropiesDistribution(3);
			
			goalDifferenceDistribution2 = competition.getGoalDifferenceDistribution(0);
			scoreEntropiesDistribution2 = competition.getGoalDifferenceDistribution(0);
		}
		
		int goalDifference;
		
		int goalDifference1 = StatsOutcome.getResultFromDistribution(goalDifferenceDistribution1);
		int scoreEntropy1 = StatsOutcome.getResultFromDistribution(scoreEntropiesDistribution1);
		
		int goalDifference2 = StatsOutcome.getResultFromDistribution(goalDifferenceDistribution2);
		int scoreEntropy2 = StatsOutcome.getResultFromDistribution(scoreEntropiesDistribution2);
		
		logger.config("Goal difference 1: " + goalDifference1);
		logger.config("Score entropy 1: " + scoreEntropy1);
		
		logger.config("Goal difference 2: " + goalDifference2);
		logger.config("Score entropy 2: " + scoreEntropy2);
		
		int lowScore = 0;
		int highScore = 0;
		
		int lowScore1 = 0;
		int highScore1 = Math.abs(goalDifference1);
		
		int lowScore2 = 0;
		int highScore2 = Math.abs(goalDifference2);
		
		lowScore1 += scoreEntropy1;
		highScore1 += scoreEntropy1;
		
		lowScore2 += scoreEntropy2;
		highScore2 += scoreEntropy2;
		
		// Now for the interesting part: neutral ground filtering
		
		double homeGoalsAverageFactor = 1;
		double awayGoalsAverageFactor = 1;
		
		logger.config("High score 1: " + highScore1);
		logger.config("High score 2: " + highScore2);
		
		logger.config("Low score 1: " + lowScore1);
		logger.config("Low score 2: " + lowScore2);
		
		if (this.venue != null && this.venue.equals(Ground.NEUTRAL_GROUND)) {
			
			goalDifference = goalDifference1 - goalDifference2;
			
			logger.config("Total goal difference: " + goalDifference);
			
			if (goalDifference1 >= 0 && goalDifference2 >= 0) {
				
				if (goalDifference1 >= goalDifference2) {
					highScore = highScore1 + lowScore2;
					lowScore = highScore2 + lowScore1;
				} else {
					highScore = highScore2 + lowScore1;
					lowScore = highScore1 + lowScore2;
				}
			} else if (goalDifference1 >= 0 && goalDifference2 < 0) {
				highScore = highScore1 + highScore2;
				lowScore = lowScore1 + lowScore2;
			} else if (goalDifference1 < 0 && goalDifference2 >= 0) {
				highScore = highScore1 + highScore2;
				lowScore = lowScore1 + lowScore2;
			} else if (goalDifference1 < 0 && goalDifference2 < 0) {
				if (goalDifference1 >= goalDifference2) {
					highScore = highScore1 + lowScore2;
					lowScore = highScore2 + lowScore1;
				} else {
					highScore = highScore2 + lowScore1;
					lowScore = highScore1 + lowScore2;
				}
			}
			
			homeGoalsAverageFactor = 0.5; // KISS principle
			awayGoalsAverageFactor = 0.5; // Ditto
			
		} else {
			goalDifference = goalDifference1;
			lowScore = lowScore1;
			highScore = highScore1;
		}
		
		if (lowScore < 0) {
			lowScore = 0;
		}
		
		if (highScore < 0) {
			highScore = 0;
		}
		
		logger.config("High score: " + highScore);
		logger.config("Low score: " + lowScore);
		
		logger.config("Home goals average factor: " + homeGoalsAverageFactor);
		logger.config("Away goals average factor: " + awayGoalsAverageFactor);
		
		if (goalDifference >= 0) {
			homeTeamScore = highScore;
			awayTeamScore = lowScore;
		} else {
			homeTeamScore = lowScore;
			awayTeamScore = highScore;
		}
		this.homeTeamScore *= homeGoalsAverageFactor;
		this.awayTeamScore *= awayGoalsAverageFactor;
	}
}
