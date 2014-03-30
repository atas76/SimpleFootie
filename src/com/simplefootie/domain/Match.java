package com.simplefootie.domain;

import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import com.simplefootie.data.DataException;
import com.simplefootie.domain.exceptions.InvalidTeamRankingException;


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
		
		double capacityRatio = (double) homeTeamRanking / (double) awayTeamRanking;
		
		logger.config("Capacity divergence: " + capacityRatio);
		
		List<Score> sampleScores = competition.getScoreSample().filterByCapacity(capacityRatio).getScores();
		
		// Pick one random score from the sample and we are done
		
		Random rnd = new Random();
		
		Score matchedScore = sampleScores.get(rnd.nextInt(sampleScores.size()));
		
		this.homeTeamScore = matchedScore.getHomeScore();
		this.awayTeamScore = matchedScore.getAwayScore();
		
		// Now for the interesting part: neutral ground filtering
		double homeGoalsAverageFactor = 1;
		double awayGoalsAverageFactor = 1;
		
		if (this.venue != null && this.venue.equals(Ground.NEUTRAL_GROUND)) {
			
			double reverseCapacityRatio = 1 / capacityRatio;
			
			List<Score> reverseSampleScores = competition.getScoreSample().filterByCapacity(reverseCapacityRatio).getScores();
			
			Score complementScore = sampleScores.get(rnd.nextInt(reverseSampleScores.size()));
			
			this.homeTeamScore += complementScore.getAwayScore();
			this.awayTeamScore += complementScore.getHomeScore();
			
			homeGoalsAverageFactor = 0.5;
			awayGoalsAverageFactor = 0.5; 
		} 
		
		this.homeTeamScore *= homeGoalsAverageFactor;
		this.awayTeamScore *= awayGoalsAverageFactor;
	}
}
