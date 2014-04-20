package com.simplefootie.screens;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.simplefootie.domain.Leagues;
import com.simplefootie.domain.Team;
import com.simplefootie.gameflow.ErrorMessages;
import com.simplefootie.gameflow.InvalidUserInputException;


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
	 * This is the default overloading of the method (and the initial one), although it is unlikely to ever be called, because we need to take the opponent into account
	 * anyway in different levels. 
	 * 
	 * @param leagueName the specified league
	 */
	public static void display(String leagueName) {
		display(leagueName, null);
	}
	
	public static void display(String leagueName, String opponent) {
		
		List<Team> teams = Leagues.getTeams(leagueName);
		
		Team alreadySelectedTeam = null;
		
		if (opponent != null) {
			for (Team team:teams) {
				if (team.getName().equals(opponent)) {
					alreadySelectedTeam = team;
					break;
				}
			}
			if (alreadySelectedTeam != null) {
				teams.remove(alreadySelectedTeam);
			}
		}
		
		displayMenuOptionsFromTeams(teams);
	}

	private static void displayMenuOptionsFromTeams(List<Team> teams) {
		
		System.out.println();
		
		optionMap = new HashMap<Integer, String>(); // Clear previous values (for not leaving old values out of the current range)
		
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
	public static String getUserInput() throws InvalidUserInputException {
		
		System.out.println();
		System.out.print("Select team: ");
		
		try {
			Scanner in = new Scanner(System.in);
			int userOption = in.nextInt();
			
			String retOption = optionMap.get(userOption);
			
			if (retOption == null) {
				throw new InvalidUserInputException(ErrorMessages.MENU_OPTION_OUT_OF_BOUNDS);
			}
			
			return retOption;
			
		} catch (ArrayIndexOutOfBoundsException aiobex) {
			throw new InvalidUserInputException(ErrorMessages.MENU_OPTION_OUT_OF_BOUNDS);
		} catch (InputMismatchException imex) {
			throw new InvalidUserInputException(ErrorMessages.INVALID_MENU_OPTION);
		}
	}
}
