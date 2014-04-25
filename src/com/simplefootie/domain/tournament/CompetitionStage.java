package com.simplefootie.domain.tournament;

import java.util.ArrayList;
import java.util.List;

import com.simplefootie.data.DataException;
import com.simplefootie.domain.Competition;
import com.simplefootie.domain.Match;
import com.simplefootie.domain.Team;
import com.simplefootie.domain.exceptions.InvalidTeamRankingException;

public class CompetitionStage {
	
	private StageType stageType;
	private PairingType pairingType;
	private List<TieBreaker> tieBreakers = new ArrayList<TieBreaker>();
	private int participantsNumber;
	
	private List<Team> qualifyingTeams = new ArrayList<Team>();
	private List<Team> qualifiedTeams = new ArrayList<Team>();
	
	private Draw draw;
	private String name;
	
	private List<FixtureGroup> matchDays = new ArrayList<FixtureGroup>();
	
	private int currentMatchDay = 0;
	
	public List<TieBreaker> getTieBreakers() {
		return this.tieBreakers;
	}
	
	public PairingType getPairingType() {
		return this.pairingType;
	}
	
	public StageType getStageType() {
		return this.stageType;
	}
	
	public void reset() {
		this.qualifyingTeams = new ArrayList<Team>();
		this.qualifiedTeams = new ArrayList<Team>();
		this.matchDays = new ArrayList<FixtureGroup>();
		this.draw = null;
		this.currentMatchDay = 0;
	}
	
	public CompetitionStage(StageType stageType, PairingType pairingType, List<TieBreaker> tieBreakers, int participantsNumber, String name) {
		this.stageType = stageType;
		this.pairingType = pairingType;
		this.tieBreakers = tieBreakers;
		this.participantsNumber = participantsNumber;
		this.name = name;
	}
	
	public CompetitionStage(CompetitionStage competitionStage) {
		// Immutable type metadata
		this.stageType = competitionStage.stageType;
		this.pairingType = competitionStage.pairingType;
		this.tieBreakers = competitionStage.tieBreakers;
		this.participantsNumber = competitionStage.participantsNumber;
		this.name = competitionStage.name;
		
		// Instance data
		this.reset(); // Handy method for initializing all mutable instance data
	}
	
	
	public void setQualifiedTeams(List<Team> teams) {
		this.qualifiedTeams = teams;
	}
	
	public List<Team> getQualifyingTeams() {
		return this.qualifyingTeams;
	}
	
	public List<Grouping> getGroupings() {
		return this.draw.getGroupings();
	}
	
	public int getParticipantsNumber() {
		return this.participantsNumber;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public boolean isOver() {
		return this.currentMatchDay >= this.matchDays.size();
	}
	
	public boolean isInitialized() {
		return (this.draw != null);
	}
	
	public void addQualifyingTeams(List<Team> teams) {
		this.qualifyingTeams.addAll(teams);
	}
	
	public int getCurrentMatchDayIndex() {
		return this.currentMatchDay;
	}
	
	public String proceed(Tournament tournament) throws InvalidTeamRankingException, DataException {
		
		if (!this.isInitialized()) {	
			// Initialize stage with tournament details
			setQualifiedTeams(tournament.getRemainingTeams().subList(tournament.getRemainingTeams().size() - getParticipantsNumber(), tournament.getRemainingTeams().size()));
			addQualifyingTeams(tournament.getRemainingTeams().subList(0, tournament.getRemainingTeams().size() - getParticipantsNumber())); // Teams receiving bye
		
			draw();
			createFixtures();
		}
		
		FixtureGroup currentMatchDayFixtures = this.matchDays.get(this.currentMatchDay);
		
		String currentView = currentMatchDayFixtures.proceed(tournament.getMetadata(), this);
		
		String stageHeader = "";
		
		// First match day of the stage. Display stage title as well.
		if (this.currentMatchDay == 0) {
			stageHeader = "\n" + getName() + "\n";
		} 
		
		currentView = stageHeader + currentView;
		
		if (currentMatchDayFixtures.isPlayed()) {
			this.currentMatchDay++;
		}
		
		if (this.isOver()) {
			calculateQualifyingTeams();
			tournament.resetRemainingTeams(this.getQualifyingTeams());
		}
		
		return currentView;
	}
	
	@Deprecated
	public String getCurrentView() {
		
		if (this.isOver()) {
			return getAggregateResults();
		}
		
		String stageHeader = "";
		
		// First match day of the stage. Display stage title as well.
		if (this.currentMatchDay == 0) {
			stageHeader = "\n" + getName() + "\n";
		}
		
		return stageHeader + getMatchDayView();
		
		/*
		if (this.matchDays.get(this.currentMatchDay).isPlayed()) {
			if (!this.matchDays.get(this.currentMatchDay).isDeterminant()) {
				return getMatchDayView();
			} else {
				return getAggregateResults();
			}
		} else { // Fixtures display. Display conditionally the stage name as well.
			return stageHeader + getMatchDayView();
		}
		*/
	}
	
	/**
	 * A utility method for grouping the qualifying teams of the stage
	 */
	public void calculateQualifyingTeams() {
		for (Grouping grouping:draw.getGroupings()) {
			for (Team qualifyingTeam: grouping.getWinners()) {
				this.qualifyingTeams.add(qualifyingTeam);
			}
		}
	}
	
	public void draw() {
		
		this.draw = new Draw();
		
		List<Team> drawTeams = new ArrayList<Team>();
		for (Team team:this.qualifiedTeams) {
			drawTeams.add(team);
		}
		
		if (stageType.equals(StageType.KNOCKOUT)) {
			draw.setGroupingSize(2);
			draw.setTeams(drawTeams);
			draw.make();
		}
	}
	
	public void createFixtures() {
		
		// Initialize schedule
		if (this.stageType.equals(StageType.KNOCKOUT)) {
			if (this.pairingType.equals(PairingType.DOUBLE_MATCH)) {
				this.matchDays.add(new FixtureGroup("First Leg", false));
				this.matchDays.add(new FixtureGroup("Second Leg", true));
			} else {
				this.matchDays.add(new FixtureGroup(null, true)); // The match day title will be inherited from the stage title
			}
			for (Grouping grouping: draw.getGroupings()) {	
				List<List<Match>> matches = grouping.createSchedule(this.stageType, this.pairingType);
				for (int i = 0; i < this.matchDays.size(); i++) {
					for (Match match:matches.get(i)) {
						this.matchDays.get(i).addMatch(match, grouping);
					}
				}
			}
		}
	}
	
	@Deprecated
	public boolean showAggregateResults() {
		// Display aggregate scores
		
		if (!this.stageType.equals(StageType.KNOCKOUT)) {
			return false;
		}
		
		if (!this.pairingType.equals(PairingType.DOUBLE_MATCH)) {
			return false;
		}
		
		System.out.println();
		System.out.println("Aggregate scores for " + this.name);
		System.out.println();
		
		for (Grouping grouping:this.getGroupings()) {
			System.out.println(grouping.getAggregateScore());
			System.out.println("Winners: " + grouping.getWinners().get(0).getName() + 
					(grouping.getTieBreaker() != null?" on " + grouping.getTieBreaker().getDescription():""));
			System.out.println();
		}
		return true;
	}
	
	public String getAggregateResults() {
		
		StringBuilder aggregateResults = new StringBuilder();
		
		aggregateResults.append("\n");
		aggregateResults.append("Aggregate scores for " + this.name);
		aggregateResults.append("\n");
		aggregateResults.append("\n");
		
		for (Grouping grouping:this.getGroupings()) {
			aggregateResults.append(grouping.getAggregateScore());
			aggregateResults.append("\n");
			aggregateResults.append("Winners: " + grouping.getWinners().get(0).getName() + 
					(grouping.getTieBreaker() != null?" on " + grouping.getTieBreaker().getDescription():""));
			aggregateResults.append("\n");
			aggregateResults.append("\n");
		}
		
		return aggregateResults.toString();
	}
	
	@Deprecated
	public void showFixtures() {
		this.matchDays.get(this.currentMatchDay).display();
	}
	
	
	public FixtureGroup getCurrentMatchDay() {
		return this.matchDays.get(this.currentMatchDay);
	}
	
	/**
	 * Both updating of the view and the model with proceeding to the next stage. Awesome.
	 */
	@Deprecated
	public void showResults() {
		// Upon showing the results, match day is officially over.
		for (Match match:this.matchDays.get(this.currentMatchDay++).getMatches()) {
			match.displayResult();
		}
		System.out.println();
	}
	
	public String getMatchDayView() {
		return this.matchDays.get(this.currentMatchDay).getView();
	}
	
	/**
	 * 
	 * The stage should not dictate the match day phase
	 * 
	 * @param competition
	 * @throws DataException
	 * @throws InvalidTeamRankingException
	 */
	@Deprecated
	public void playFixtures(Competition competition) throws DataException, InvalidTeamRankingException {
		
		FixtureGroup currentFixtures = this.matchDays.get(this.currentMatchDay);
		
		for (Match match:currentFixtures.getMatches()) {
			match.calculateResult(competition);
			// Update the competition status (we may not need this as the match is aggregated in the grouping object
			// currentFixtures.updateGrouping(match); // Match can be updated further: extra-time, penalty shoot-out, etc.
			if (currentFixtures.isDeterminant()) {
				currentFixtures.calculateGroupingOutcome(match, this.tieBreakers, this.stageType, this.pairingType);
			}
		}
		currentFixtures.setPlayed();
	}
}
