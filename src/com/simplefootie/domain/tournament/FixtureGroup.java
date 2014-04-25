package com.simplefootie.domain.tournament;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.simplefootie.data.DataException;
import com.simplefootie.domain.Competition;
import com.simplefootie.domain.Match;
import com.simplefootie.domain.exceptions.InvalidTeamRankingException;

public class FixtureGroup {
	
	private String title;
	private List<Match> matches = new ArrayList<Match>();
	private boolean determinant;
	private Map<Match, Grouping> matchGroupings = new HashMap<Match, Grouping>(); 
	private boolean played;
	private boolean initialized;
	
	public FixtureGroup(String title, boolean determinant) {
		this.title = title;
		this.determinant = determinant;
		this.played = false;
		this.initialized = false;
	}
	
	public boolean isDeterminant() {
		return this.determinant;
	}
	
	public void setPlayed() {
		this.played = true;
	}
	
	public boolean isPlayed() {
		return this.played;
	}
	
	public void initialize() {
		this.initialized = true;
	}
	
	public boolean isInitialized() {
		return this.initialized;
	}
	
	public void addMatch(Match match, Grouping grouping) {
		this.matches.add(match);
		this.matchGroupings.put(match, grouping);
	}
	
	public List<Match> getMatches() {
		return this.matches;
	}
	
	public void calculateGroupingOutcome(Match match, List<TieBreaker> tieBreakers, StageType stageType, PairingType pairingType) {
		matchGroupings.get(match).calculateGroupingOutcome(match, tieBreakers, stageType, pairingType);
	}
	
	public String getView() {
		return this.getView(false);
	}
	
	/**
	 * 
	 * @param showAggregateScores This is one of maybe more future parameters, that will specify extra conditions about how to display match day scores
	 * @return
	 */
	public String getView(boolean showAggregateScores) {
		
		StringBuilder view = new StringBuilder();
		
		view.append("\n");
		if (this.title != null) {
			view.append(this.title);
			view.append("\n");
		}
		view.append("\n");
		
		for (Match match:this.matches) {
			view.append(match.toString());
			view.append("\n");
			if (showAggregateScores) {
				// Special case when we want to display aggregate scores, with the match day results. We will come back to it soon.
			}
		}
		
		view.append("\n");
		
		return view.toString();
	}
	
	public String proceed(Competition tournamentMetadata, CompetitionStage currentStage) throws DataException, InvalidTeamRankingException {
		
		// Don't proceed to actual match calculation before displaying the fixtures
		if (!this.initialized) {
			this.initialized = true;
			return getView();
		}
		
		for (Match match:getMatches()) {
			match.calculateResult(tournamentMetadata);
			// Update the competition status (we may not need this as the match is aggregated in the grouping object
			// currentFixtures.updateGrouping(match); // Match can be updated further: extra-time, penalty shoot-out, etc.
			if (isDeterminant()) {
				calculateGroupingOutcome(match, currentStage.getTieBreakers(), currentStage.getStageType(), currentStage.getPairingType());
			}
		}
		
		setPlayed();
		
		String currentView = getView(isDeterminant());
		
		if (isDeterminant() && currentStage.getPairingType().equals(PairingType.DOUBLE_MATCH)) { // Show the aggregate scores of current stage as well
			currentView += "\n" + currentStage.getAggregateResults();
		}
		return currentView;
	}
	
	@Deprecated
	public void display() {
		
		System.out.println();
		if (this.title != null) {
			System.out.println(this.title);
		}
		System.out.println();
		
		for (Match match:this.matches) {
			System.out.println(match);
		}
		
		System.out.println();
	}
}
