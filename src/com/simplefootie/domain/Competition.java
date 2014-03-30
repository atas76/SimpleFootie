package com.simplefootie.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.simplefootie.data.ScoreSample;
import com.simplefootie.domain.exceptions.TeamNotFoundException;
import com.simplefootie.domain.tournament.CompetitionStage;

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
	
	private RankingMode rankingMode;
	private Map<RankingMode, List<Team>> rankings = new HashMap<RankingMode, List<Team>>();
	
	private List<Team> teams;
	private List<CompetitionStage> stage;
	private ScoreSample scoreSample;
	
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
	
	/**
	 * 
	 * Setting the teams is not enough. We must created their ranking on the fly. 
	 * 
	 * @param teams The teams will remain 'sorted' for backwards compatibility.
	 */
	public void setTeams(List<Team> teams) {
		// For the time being we support only UEFA ranking by default
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
}
