package com.simplefootie.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.simplefootie.Resources;
import com.simplefootie.data.DataException;
import com.simplefootie.data.ScoreSample;
import com.simplefootie.domain.Environment.Competitions;
import com.simplefootie.domain.exceptions.InvalidTeamRankingException;
import com.simplefootie.domain.exceptions.TeamNotFoundException;
import com.simplefootie.domain.tournament.CompetitionStage;
import com.simplefootie.domain.tournament.Grouping;

/**
 * Grouping of teams belonging to a particular competition. 
 * UPDATE: The above is a bit of restrictive use of competition. A competition is a lot more than the teams that participate in it. An update in the design needs to take
 * place.
 * <p/>  
 * A team in the environment should always be part of one or more competitions. This is good for two reasons: Firstly, grouping of teams and second calculating
 * the relative strengths of teams within the same group. So, a team does not have an "absolute" strength but its performance is calculated based on relative
 * strength to other teams in the same group of teams, identified by participation in a specific competition. For the time being, only friendly matches are 
 * supported, however even for that, a "friendly match" competition is used.
 * 
 * @author Andreas Tasoulas
 *
 */
public class Competition {
	
	private String name;
	
	private List<Team> parentRanking;
	
	private RankingMode rankingMode;
	private Map<RankingMode, List<Team>> rankings = new HashMap<RankingMode, List<Team>>();
	
	private List<Team> teams = new ArrayList<Team>();
	private List<CompetitionStage> stages;
	private ScoreSample scoreSample;
	
	private List<Team> remainingTeams = new ArrayList<Team>();
	
	/**
	 * Each competition must have a ranking mode, so that participating teams are ranked and sorted
	 * 
	 * @param rankingMode one of the various ranking modes supported
	 */
	public Competition(RankingMode rankingMode) {
		this.rankingMode = rankingMode;
	}
	
	public void setScoreSample(List<Score> sample) {
		this.scoreSample = new ScoreSample(sample);
	}
	
	public ScoreSample getScoreSample() {
		return this.scoreSample;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPreview() {
		
		StringBuilder competitionPreview = new StringBuilder();
		
		competitionPreview.append("\n");
		competitionPreview.append(this.name);
		competitionPreview.append("\n");
		competitionPreview.append("\n");
		competitionPreview.append("Participating teams: (" + teams.size() + ")");
		competitionPreview.append("\n");
		for (Team team:teams) {
			competitionPreview.append(team.getName());
			competitionPreview.append("\n");
		}
		competitionPreview.append("\n");
		
		return competitionPreview.toString();
	}
	
	/**
	 * This method being private and void is a bad side-effect of everything being handled by the competition object internally.
	 * But this must be changed and the competition object must be open to the world for reusing
	 */
	@Deprecated
	private void showPreview() {
		System.out.println();
		System.out.println(this.name);
		System.out.println();
		System.out.println("Participating teams: (" + teams.size() + ")");
		System.out.println();
		for (Team team:teams) {
			System.out.println(team.getName());
		}
		System.out.println();
	}
	
	/**
	 * Controller functionality in a model class. Not a good idea.
	 */
	@Deprecated
	private void promptNext() {
		System.out.print("Press enter to continue");
		Scanner in = new Scanner(System.in);
		in.nextLine();
	}
	
	/**
	 * This affects instance data and thus not relevant anymore for this metadata type
	 */
	@Deprecated
	private void initialize() {
		this.remainingTeams = new ArrayList<Team>();
		for (Team team:teams) {
			this.remainingTeams.add(team);
		}
		for (CompetitionStage stage:this.stages) {
			stage.reset();
		}
	}
	
	/**
	 * Start the competition gameflow
	 */
	@Deprecated
	public void play() throws InvalidTeamRankingException, DataException {
		
		initialize();
		
		showPreview();
		promptNext();
		
		// Initialize
		int currentStageIndex = 0;
		
		while (currentStageIndex < this.stages.size()) {
			
			CompetitionStage currentStage = this.stages.get(currentStageIndex);
			
			// This is useful for competitions, where teams enter the competition in advanced stages (as e.g. in the English FA Cup)
			currentStage.setQualifiedTeams(this.remainingTeams.subList(this.remainingTeams.size() - currentStage.getParticipantsNumber(), this.remainingTeams.size()));
			currentStage.addQualifyingTeams(this.remainingTeams.subList(0, this.remainingTeams.size() - currentStage.getParticipantsNumber())); // Teams receiving bye
			
			currentStage.draw();
			currentStage.createFixtures();
			
			System.out.println();
			System.out.println(currentStage.getName());
			
			while (!currentStage.isOver()) {
				
				System.out.println();
				currentStage.showFixtures();
				promptNext();
				
				currentStage.playFixtures(this); // Orientate the stage object in functional programming style
				
				System.out.println();
				currentStage.showResults();
				System.out.println();
				promptNext();
			}
			
			if (currentStage.showAggregateResults()) {
				promptNext();
			}
			
			// Finalize current stage
			currentStage.calculateQualifyingTeams();
			
			this.remainingTeams = new ArrayList<Team>(); // Reset
			for (Team team:currentStage.getQualifyingTeams()) {
				this.remainingTeams.add(team);
			}
			currentStageIndex++;
		}
	}
	
	/**
	 * 
	 * Setting the teams is not enough. We must created their ranking on the fly. 
	 * 
	 * @param teams The teams will remain 'sorted' for backwards compatibility.
	 */
	public void setTeams(List<Team> teams) {
		this.teams = teams;
		rankings.put(RankingMode.UEFA, teams);
	}
	
	public List<Team> getTeams() {
		return this.teams;
	}
	
	/**
	 * Returns a team ranking among of all the teams participating in the current competition. This method will be obsoleted by a corresponding method in specialized 
	 * data retrieval classes.
	 * 
	 * @param teamName the name of the team
	 * @return the ranking of a team in the competition. Should be a positive non-zero integer.
	 */
	public int getTeamRanking(String teamName) {
		
		List<Team> rankedTeams = this.rankings.get(this.rankingMode);
		
		for (Team team:rankedTeams) {
			if (team.getName().equals(teamName)) {
				return rankedTeams.indexOf(team) + 1; 
			}
		}
		return 0;
	}
	
	/**
	 * 
	 * We need this method, in case we want to combine rankings externally for calculating matches on a case-by-case basis
	 * 
	 * @param rankingMode We need to specify the ranking mode as it would normally be a 'custom' one: the competition ranking will be a mixed one
	 * @return a list of teams sorted according to the specified custom ranking
	 */
	public List<Team> getRanking(RankingMode rankingMode) {
		return this.getRanking(rankingMode);
	}
	
	/**
	 * Gets a team object based on the team's name from the teams associated with the current competition.
	 * 
	 * @param name the team's name
	 * @return the team object as contained in the current Competition object
	 */
	public Team getTeamByName(String name) {
		for (Team team:this.teams) {
			if (team.getName().equals(name)) {
				return team;
			}
		}
		return null;
	}
	
	/**
	 * Gets a team object based on the team's short name from the teams associated with the current competition.
	 * 
	 * @param shortName For simplicity, we usually use a designated 'short name' for teams, esp. where a human user is involved.
	 * @return the team object the team object as contained in the current Competition object
	 */
	public Team getTeamByShortName(String shortName) throws TeamNotFoundException {
		for (Team team:this.teams) {
			if (team.getShortName().equals(shortName)) {
				return team;
			}
		}
		// No team found
		throw new TeamNotFoundException(shortName);
	}
	
	public void setStages(List<CompetitionStage> stages) {
		this.stages = stages;
	}
	
	public List<CompetitionStage> getStages() {
		return this.stages;
	}
}
