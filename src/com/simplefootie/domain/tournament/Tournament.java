package com.simplefootie.domain.tournament;

import java.util.ArrayList;
import java.util.List;

import com.simplefootie.data.DataException;
import com.simplefootie.domain.Competition;
import com.simplefootie.domain.Team;
import com.simplefootie.domain.exceptions.InvalidTeamRankingException;

/**
 * 
 * This class plays the role of a competition instance, where the competition class is a template holding the tournament metadata and ideally is immutable.
 * 
 * @author Andreas Tasoulas
 *
 */
public class Tournament {
	
	private Competition tournamentMetadata;
	
	// Instance specific attributes to be copied from the competition
	private List<Team> remainingTeams = new ArrayList<Team>();
	private List<CompetitionStage> stages = new ArrayList<CompetitionStage>();
	
	private int stageIndex;
	
	public Tournament(Competition competition) {
		
		this.tournamentMetadata = competition;
		
		this.stageIndex = 0;
		
		for (Team team: competition.getTeams()) {
			remainingTeams.add(team);
		}
		
		for (CompetitionStage stage:competition.getStages()) {
			this.stages.add(new CompetitionStage(stage));
		}
	}
	
	public Competition getMetadata() {
		return this.tournamentMetadata;
	}
	
	public String getPreview() {
		return this.tournamentMetadata.getPreview();
	}
	
	public CompetitionStage getCurrentStage() {
		return this.stages.get(this.stageIndex);
	}
	
	public int getStageIndex() {
		return this.stageIndex;
	}
	
	public int getMatchDayIndex() {
		return getCurrentStage().getCurrentMatchDayIndex();
	}
	
	public FixtureGroup getCurrentMatchDay() {
		return getCurrentStage().getCurrentMatchDay();
	}
	
	public boolean isOver() {
		return this.stageIndex >= stages.size();
	}
	
	public List<Team> getRemainingTeams() {
		return this.remainingTeams;
	}
	
	/**
	 * A tournament schedule can proceed with either going to the next stage or telling the current stage to proceed 
	 * 
	 */
	public String proceed() throws InvalidTeamRankingException, DataException {
		CompetitionStage currentStage = this.stages.get(this.stageIndex); // Get current stage
		String currentView = currentStage.proceed(this);
		if (currentStage.isOver()) {
			this.stageIndex++;
		}
		return currentView;
	}
	
	public String getCurrentStageName() {
		return this.stages.get(this.stageIndex).getName();
	}
	
	@Deprecated
	public String getCurrentView() {
		return this.stages.get(this.stageIndex).getCurrentView();
	}
	
	public void resetRemainingTeams(List<Team> teams) {
		this.remainingTeams = new ArrayList<Team>();
		for (Team team:teams) {
			this.remainingTeams.add(team);
		}
	}
	
	// TODO: Implement
	public Integer getNextDayStage(Integer stageId, Integer matchDayId) {
		return null;
	}
	
	// TODO: Implement
	public Integer getNextDayId(Integer stageId, Integer matchDayId) {
		return null;
	}
	
	// TODO: Implement
	public Integer getPreviousDayStage(Integer stageId, Integer matchDayId) {
		return null;
	}
	
	// TODO: Implement
	public Integer getPreviousDayId(Integer stageId, Integer matchDayId) {
		return null;
	}
}
