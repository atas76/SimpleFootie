package com.simplefootie.web.backend;

import java.util.List;

import com.simplefootie.domain.Leagues;


public class SelectTeam {
	
	/**
	 * Gets data for the team selection view for the user to select a team from.
	 * 
	 * @param league the league name of the team to be selected
	 * @return the set of available teams for a specific league
	 */
	public static List<String> getDynamicDisplay(String league) {
		return Leagues.getTeams(league);
	}
}
