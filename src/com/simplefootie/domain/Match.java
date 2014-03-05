package com.simplefootie.domain;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
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
	
	public Match(Team homeTeam, Team awayTeam, Ground venue) {
		this.homeTeam = homeTeam;
		this.awayTeam = awayTeam;
		this.venue = venue;
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
		this.homeTeam = new Team(homeTeamName);
	}
	
	public void setAwayTeam(String awayTeamName) {
		this.awayTeam = new Team(awayTeamName);
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
		
		logger.config("Home team ranking: " + homeTeamRanking);
		logger.config("Away team ranking: " + awayTeamRanking);
		
		if (homeTeamRanking < 1 || awayTeamRanking < 1) {
			throw new InvalidTeamRankingException();
		}
		
		double capacityDivergence = (double) homeTeamRanking / (double) awayTeamRanking;
		
		logger.config("Capacity divergence: " + capacityDivergence);
		
		Map<Integer, Integer> goalDifferenceDistribution;
		Map<Integer, Integer> scoreEntropiesDistribution;
		
		if (capacityDivergence <= 0.5) {
			goalDifferenceDistribution = competition.getGoalDifferenceDistribution(0);
			scoreEntropiesDistribution = competition.getEntropiesDistribution(0);
		} else if (capacityDivergence <= 1) {
			goalDifferenceDistribution = competition.getGoalDifferenceDistribution(1);
			scoreEntropiesDistribution = competition.getEntropiesDistribution(1);
		} else if (capacityDivergence <= 2) {
			goalDifferenceDistribution = competition.getGoalDifferenceDistribution(2);
			scoreEntropiesDistribution = competition.getEntropiesDistribution(2);
		} else {
			goalDifferenceDistribution = competition.getGoalDifferenceDistribution(3);
			scoreEntropiesDistribution = competition.getEntropiesDistribution(3);
		}
		
		int goalDifference = StatsOutcome.getResultFromDistribution(goalDifferenceDistribution);
		int scoreEntropy = StatsOutcome.getResultFromDistribution(scoreEntropiesDistribution);
		
		logger.config("Goal difference: " + goalDifference);
		logger.config("Score entropy: " + scoreEntropy);
		
		int lowScore = 0;
		int highScore = Math.abs(goalDifference);
		
		lowScore += scoreEntropy;
		highScore += scoreEntropy;
		
		// Now for the interesting part: neutral ground filtering
		
		double homeGoalsAverageFactor = 1;
		double awayGoalsAverageFactor = 1;
		
		if (this.venue != null && this.venue.equals(Ground.NEUTRAL_GROUND)) {
			try {
			
				double homeTeamGoalsScored = MatchData.getVenueRelatedScoredRatio(this.homeTeam.getShortName());
				double awayTeamGoalsConceded = 1 - MatchData.getVenueRelatedConcededRatio(this.awayTeam.getShortName());
			
				double homeGoalsAverageRatio = (homeTeamGoalsScored + awayTeamGoalsConceded) / 2;
				homeGoalsAverageFactor = 0.5 / homeGoalsAverageRatio;
			
				double homeTeamGoalsConceded = MatchData.getVenueRelatedConcededRatio(this.homeTeam.getShortName());
				double awayTeamGoalsScored = 1 - MatchData.getVenueRelatedScoredRatio(this.awayTeam.getShortName());
			
				double awayGoalsAverageRatio = (homeTeamGoalsConceded + awayTeamGoalsScored) / 2;
				awayGoalsAverageFactor = 0.5 / awayGoalsAverageRatio;
				
			} catch (FileNotFoundException fnex) {
				throw new DataException(fnex);
			} catch (IOException ioex) {
				throw new DataException(ioex);
			}
		}
		
		if (lowScore < 0) {
			lowScore = 0;
		}
		
		if (highScore < 0) {
			highScore = 0;
		}
		
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
