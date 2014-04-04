package com.simplefootie.domain.tournament;

import java.util.ArrayList;
import java.util.List;

import com.simplefootie.data.DataException;
import com.simplefootie.domain.Competition;
import com.simplefootie.domain.Ground;
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
	
	public void reset() {
		this.qualifyingTeams = new ArrayList<Team>();
		this.draw = null;
	}
	
	public CompetitionStage(StageType stageType, PairingType pairingType, List<TieBreaker> tieBreakers, int participantsNumber, String name) {
		this.stageType = stageType;
		this.pairingType = pairingType;
		this.tieBreakers = tieBreakers;
		this.participantsNumber = participantsNumber;
		this.name = name;
	}
	
	public void setQualifiedTeams(List<Team> teams) {
		this.qualifiedTeams = teams;
	}
	
	public List<Team> getQualifyingTeams() {
		return this.qualifyingTeams;
	}
	
	public int getParticipantsNumber() {
		return this.participantsNumber;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isOver() {
		return this.currentMatchDay >= this.matchDays.size();
	}
	
	public void addQualifyingTeams(List<Team> teams) {
		this.qualifyingTeams.addAll(teams);
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
				this.matchDays.add(new FixtureGroup("Final", true));
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
	
	public void showFixtures() {
		this.matchDays.get(this.currentMatchDay).display();
	}
	
	public void showResults() {
		// Upon showing the results, match day is officially over.
		for (Match match:this.matchDays.get(this.currentMatchDay++).getMatches()) {
			match.displayResult();
		}
	}
	
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
	}
}
