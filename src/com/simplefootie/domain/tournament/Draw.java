package com.simplefootie.domain.tournament;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.simplefootie.domain.Team;

public class Draw {

	// Properties
	private int groupingSize;
	private List<Grouping> groupings = new ArrayList<Grouping>();
	private List<Team> teams;
	
	// Closure variables surrogates
	private List<Team> remainingTeams = new ArrayList<Team>();
	
	private static Random rnd = new Random();
	
	public void setTeams(List<Team> teams) {
		this.teams = teams;
	}

	public void setGroupingSize(int size) {
		this.groupingSize = size;
	}
	public int getGroupingSize() {
		return this.groupingSize;
	}
	
	public List<Grouping> getGroupings() {
		return this.groupings;
	}
	
	/**
	 * Utility method for getting the next team in the draw
	 * 
	 * @return the object of the next team.
	 */
	private Team getNextTeam() {
		
		int index = rnd.nextInt(remainingTeams.size());
		
		Team nextTeam = remainingTeams.get(index);
		
		for (Team team:this.teams) {
			if (nextTeam.getName().equals(team.getName())) {
				nextTeam = team;
				break;
			}
		}
		remainingTeams.remove(index);
		return nextTeam;
	}
	
	/**
	 * We introduce the concept of grouping. Depending on how the competition stage will be played, grouping is a generalization of a two teams draw or a group and 
	 * includes the rules of how the teams will advance from the current stage
	 * 
	 * @return So, a draw is just a collection of groupings, that being a pair of teams or a group. A whole grouping is returned from this method.
	 */
	private Grouping getNextGrouping() {
		
		Grouping grouping = new Grouping(this.groupingSize);
		
		for (int i = 0; i < this.groupingSize; i++) {
			grouping.addTeam(getNextTeam());
		}
		return grouping;
	}
	
	/**
	 * Creation of the draw.
	 * 
	 */
	public void make() {
		// Initialization
		for (Team team:this.teams) {
			this.remainingTeams.add(new Team(team.getName())); // We only need the team name as reference
		}
		// Actual draw making
		while (remainingTeams.size() > 0) {
			this.groupings.add(getNextGrouping());
		}
	}
	
	@Override
	public String toString(){
		
		StringBuilder drawStr = new StringBuilder();
		
		for (Grouping grouping:this.groupings) {
			drawStr.append(grouping.toString());
			drawStr.append("\n");
		}
		
		return drawStr.toString();
	}
}
