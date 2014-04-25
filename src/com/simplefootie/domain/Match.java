package com.simplefootie.domain;

import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import com.simplefootie.data.DataException;
import com.simplefootie.domain.exceptions.InvalidMatchMutationException;
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
	
	private int homeTeamScoreAET;
	private int awayTeamScoreAET;
	
	private int homeTeamPenaltyShootOutScore = 0;
	private int awayTeamPenaltyShootOutScore = 0;
	
	private boolean extraTimePlayed;
	private boolean penaltyShootOutPlayed;
	
	private boolean matchPlayed;
	
	private Competition competition;
	
	private static Random rnd = new Random();
	
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
	
	@Override
	public boolean equals(Object object) {
		
		Match matchObject = (Match) object;
		
		return this.homeTeam.getName().equals(matchObject.getHomeTeamName()) && this.awayTeam.getName().equals(matchObject.getAwayTeamName());
	}
	
	@Override
	public int hashCode() {
		return this.homeTeam.getName().hashCode() * 2 + this.awayTeam.getName().hashCode();
	}
	
	public void setNextTeam(Team team) throws InvalidMatchMutationException {
		if (this.homeTeam == null) {
			this.homeTeam = team;
		} else if (this.awayTeam == null) {
			this.awayTeam = team;
		} else {
			throw new InvalidMatchMutationException(this.homeTeam.getName(), this.awayTeam.getName(), team.getName());
		}
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
	
	@Deprecated
	public String getHomeTeamName() {
		return this.homeTeam.getName();
	}
	
	@Deprecated
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
	
	public int getHomeTeamScoreAET() {
		return this.homeTeamScoreAET;
	}
	
	public int getAwayTeamScoreAET() {
		return this.awayTeamScoreAET;
	}
	
	public int getHomeTeamPenaltyShootOutScore() {
		return this.homeTeamPenaltyShootOutScore;
	}
	
	public int getAwayTeamPenaltyShootOutScore() {
		return this.awayTeamPenaltyShootOutScore;
	}
	
	@Override
	public String toString() {
		if (!this.matchPlayed) {
			return this.getFixture();
		} else {
			return this.getResult();
		}
	}
	
	public String getFixture() {
		return this.homeTeam.getName() + " - " + this.awayTeam.getName();
	}
	
	public String getResult() {
		
		String opponents = this.homeTeam.getName() + " - " + this.awayTeam.getName();
		String result =  homeTeamScore + "-" + awayTeamScore;
		
		if (this.extraTimePlayed) {
			String normalTimeResult = result;
			result = homeTeamScoreAET + " - " + awayTeamScoreAET + " (aet)" + ", Normal time: " + normalTimeResult;
		}
		
		if (this.penaltyShootOutPlayed) {
			String playingTimeResult = result;
			result = homeTeamPenaltyShootOutScore + " - " + awayTeamPenaltyShootOutScore + " (pens)" + ", " + playingTimeResult; 
		}
		
		return (opponents +  "   " + result);
	}
	
	/**
	 * Displays the result of the match: <home team name> - <away team name> <home score> - <away score>
	 */
	public void displayResult() {
		
		String opponents = this.homeTeam.getName() + " - " + this.awayTeam.getName();
		String result =  homeTeamScore + "-" + awayTeamScore;
		
		if (this.extraTimePlayed) {
			String normalTimeResult = result;
			result = homeTeamScoreAET + " - " + awayTeamScoreAET + " (aet)" + ", Normal time: " + normalTimeResult;
		}
		
		if (this.penaltyShootOutPlayed) {
			String playingTimeResult = result;
			result = homeTeamPenaltyShootOutScore + " - " + awayTeamPenaltyShootOutScore + " (pens)" + ", " + playingTimeResult; 
		}
		
		System.out.println(opponents +  "   " + result);
	}
	
	/**
	 * Utility method for validating that the match object has all the details necessary for generating its result.
	 * 
	 * @return True if both opponent team objects are not null, false otherwise (the match result cannot be generated at this point)
	 */
	public boolean hasCompleteDetails() {
		return (this.homeTeam != null && this.awayTeam != null);
	}
	
	public Team getHomeTeam() {
		return this.homeTeam;
	}
	
	public Team getAwayTeam() {
		return this.awayTeam;
	}
	
	private void shootPenalty(Team team) {
		
		double successFactor = 0.8; // A lot more work has been put into determining this than it seems :-)
		
		double outcome = rnd.nextDouble();
		
		if (outcome <= successFactor) {
			if (team.equals(this.homeTeam)) {
				this.homeTeamPenaltyShootOutScore++;
			}
			if (team.equals(this.awayTeam)) {
				this.awayTeamPenaltyShootOutScore++;
			}
		}
	}
	
	private boolean isPenaltyShootOutWinner(int currentTeamOrder, int penaltyOrder) {
		
		int difference = this.homeTeamPenaltyShootOutScore - this.awayTeamPenaltyShootOutScore;
		
		int homeTeamPenaltiesLeft = 5 - penaltyOrder;
		int awayTeamPenaltiesLeft = (currentTeamOrder == 2)?(5 - penaltyOrder):(5 - penaltyOrder + 1);
		
		if (difference > 0 && difference > awayTeamPenaltiesLeft) {
			return true;
		}
		if (difference < 0 && Math.abs(difference) > homeTeamPenaltiesLeft) {
			return true;
		}
		return false;
	}
	
	
	/**
	 * Calculate the outcome of the penalty shootout of a match. Use the 'universal competition' statistics for the time being.
	 */
	public void penaltyShootOut() {
		
		// Although it is not currently needed, we will carry on a full simulation of the penalty shoot-out
		// Let's make the home team shoot first for simplicity
		
		for (int i = 0; i < 5; i++) {
			shootPenalty(this.homeTeam);
			if (isPenaltyShootOutWinner(1, i + 1)) {
				this.penaltyShootOutPlayed = true;
				return;
			}
			shootPenalty(this.awayTeam);
			if (isPenaltyShootOutWinner(2, i + 1)) {
				this.penaltyShootOutPlayed = true;
				return;
			}
		}
		
		// 5 penalties shoot-out completed. Check if we have a winner and if not, go to tie breaker
		while (this.homeTeamPenaltyShootOutScore == this.awayTeamPenaltyShootOutScore) {
			shootPenalty(this.homeTeam);
			shootPenalty(this.awayTeam);
		}
		this.penaltyShootOutPlayed = true;
	}
	
	public void playExtraTime() {
		
		// Create a copy of the match and calculate the result
		Match extraTimeMatch = new Match(this.homeTeam, this.awayTeam, this.venue, "Extra time");
		
		try {
			
			extraTimeMatch.calculateResult(this.competition);
			
			// Extra time duration is 1/3 of the normal time match
			int homeTeamExtraTimeScore = extraTimeMatch.homeTeamScore / 3;
			int awayTeamExtraTimeScore = extraTimeMatch.awayTeamScore / 3;
			
			this.homeTeamScoreAET = this.homeTeamScore + homeTeamExtraTimeScore;
			this.awayTeamScoreAET = this.awayTeamScore + awayTeamExtraTimeScore;
			
			this.extraTimePlayed = true;
			
		} catch (Exception ex) {
			// And yet an exception is thrown. Print stack trace for debugging purposes
			logger.warning("Exception thrown when calculating extra time");
            ex.printStackTrace();
		}
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
		
		if (this.competition == null) {
			this.competition = competition; // We need the competition details for further processing, e.g. extra time
		}
		
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
			
			Score complementScore = reverseSampleScores.get(rnd.nextInt(reverseSampleScores.size()));
			
			this.homeTeamScore += complementScore.getAwayScore();
			this.awayTeamScore += complementScore.getHomeScore();
			
			homeGoalsAverageFactor = 0.5;
			awayGoalsAverageFactor = 0.5; 
		} 
		
		this.homeTeamScore *= homeGoalsAverageFactor;
		this.awayTeamScore *= awayGoalsAverageFactor;
		
		this.matchPlayed = true;
	}
}
