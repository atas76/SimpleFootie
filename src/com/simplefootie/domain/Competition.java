package com.simplefootie.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.simplefootie.domain.exceptions.TeamNotFoundException;

/**
 * Grouping of teams belonging to a particular competition. 
 * A team in the environment should always be part of one or more competitions. This is good for two reasons: Firstly, grouping of teams and second calculating
 * the relative strengths of teams within the same group. So, a team does not have an "absolute" strength but its performance is calculated based on relative
 * strength to other teams in the same group of teams, identified by participation in a specific competition. For the time being, only friendly matches are 
 * supported, however even for that, a "friendly match" competition is used.
 * 
 * @author Andreas Tasoulas
 *
 */
public class Competition {
	
	private List<Team> teams;
	private List<Map<Integer, Integer>> expectedGoalDifferences = new ArrayList<Map<Integer, Integer>>();
	private List<Map<Integer, Integer>> expectedEntropies = new ArrayList<Map<Integer, Integer>>();
	
	public void setTeams(List<Team> teams) {
		this.teams = teams;
	}
	
	public List<Team> getTeams() {
		return this.teams;
	}
	
	public void setExpectedGoalDifferences(List<Map<Integer, Integer>> expectedGoalDifferences) {
		this.expectedGoalDifferences = expectedGoalDifferences;
	}
	
	public void setExpectedEntropies(List<Map<Integer, Integer>> expectedEntropies) {
		this.expectedEntropies = expectedEntropies;
	}
	
	/**
	 * Getter for a map between goal difference and cardinality of matches having a specific goal difference in the sample.
	 * 
	 * @param index an index denoting a grouping of matches based on opponent teams' ranking ratios
	 * @return map of goal differences to number of matches having this goal difference in the sample. Away team wins are represented as negative goal difference.
	 */
	public Map<Integer, Integer> getGoalDifferenceDistribution(int index) {
		return expectedGoalDifferences.get(index);
	}
	
	/**
	 * Getter for a map between score "entropies" and cardinality of matches with this specific "entropy" in the sample. 
	 * The concept is used for calculating number of goals scored on top of calculated goal difference (2-0, 3-1, 4-2, etc.), but it will likely be
	 * dropped from future logic as it adds unnecessary complexity (or probably replaced by much simpler and clearer logic).
	 *   
	 * @param index An index denoting a grouping of matches based on opponent teams' ranking ratios
	 * @return a number specific to a distribution of scores, for calculating the total number of goals scored on top of goal difference
	 */
	public Map<Integer, Integer> getEntropiesDistribution(int index) {
		return expectedEntropies.get(index);
	}
	
	/**
	 * Returns a team ranking among of all the teams participating in the current competition. This method will be obsoleted by a corresponding method in specialized 
	 * data retrieval classes.
	 * 
	 * @param teamName the name of the team
	 * @return the ranking of a team in the competition. Should be a positive non-zero integer.
	 */
	public int getTeamRanking(String teamName) {
		for (Team team:this.teams) {
			if (team.getName().equals(teamName)) {
				return teams.indexOf(team) + 1; 
			}
		}
		return 0;
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
}
