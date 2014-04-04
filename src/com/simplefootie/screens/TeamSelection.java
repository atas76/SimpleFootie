package com.simplefootie.screens;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.simplefootie.domain.Leagues;
import com.simplefootie.domain.Team;


/**
 * 
 * The team selection screen. This is presented twice in each run of the program, one for each opponent team.
 * 
 * @author Andreas Tasoulas
 * @see com.simplefootie.gui.Screens
 *
 */
public class TeamSelection {
	
	private static Map<Integer, String> optionMap = new HashMap<Integer, String>();
	
	/**
	 * Displays the available teams for the user to select from a specified league. It also initializes the mappings between user choice and team name selected.
	 * 
	 * @param leagueName the specified league
	 */
	public static void display(String leagueName) {
		
		System.out.println();
		
		List<Team> teams = Leagues.getTeams(leagueName);
		
		int count = 1;
		for (Team team:teams) {
			
			optionMap.put(count, team.getName());
			
			System.out.println(count++ + ". " + team.getName());
		}
	}
	
	/**
	 * Get user input as integer and return the team matching the user's choice.
	 * 
	 * @return the name of team selected
	 */
	public static String getUserInput() {
		
		System.out.println();
		System.out.print("Select team: ");
		
		Scanner in = new Scanner(System.in);
		int userOption = in.nextInt();
		
		return optionMap.get(userOption);
	}
}
