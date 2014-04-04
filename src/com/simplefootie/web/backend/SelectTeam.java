package com.simplefootie.web.backend;

import java.util.ArrayList;
import java.util.List;

import com.simplefootie.domain.Leagues;
import com.simplefootie.domain.Team;


public class SelectTeam {
	
	/**
	 * Gets data for the team selection view for the user to select a team from.
	 * 
	 * @param league the league name of the team to be selected
	 * @return the set of available teams for a specific league
	 */
	public static List<String> getDynamicDisplay(String league) {
		
		List<String> teamNames = new ArrayList<String>();
		
		for (Team team: Leagues.getTeams(league)) {
			teamNames.add(team.getName());
		}
		
		return teamNames;
	}
}
