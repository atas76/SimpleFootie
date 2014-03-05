package com.simplefootie.gameflow;

import com.simplefootie.data.DataException;
import com.simplefootie.domain.Competition;
import com.simplefootie.domain.Environment;
import com.simplefootie.domain.Ground;
import com.simplefootie.domain.Match;
import com.simplefootie.domain.exceptions.InvalidTeamRankingException;
import com.simplefootie.gui.Screens;
import com.simplefootie.screens.InitMatchScreen;
import com.simplefootie.screens.Main;
import com.simplefootie.screens.NationSelection;
import com.simplefootie.screens.TeamSelection;
import com.simplefootie.screens.VenueSelection;


/**
 * 
 * The class controlling the flow of the 'game'. There is a hardcoded constant for how many times a match is to be repeated, i.e. how many different results will be
 * generated.
 * 
 * @author Andreas Tasoulas
 *
 */
public class Gameflow {
	
	private static final int REPETITIONS = 1;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
			Environment.load();
		} catch (Exception ex) {
			System.out.println("Error loading environment");
			ex.printStackTrace();
			return;
		}
		
		display(Screens.MAIN);
	}
	
	/**
	 * Displaying specific data to the user, relating to the current gameflow stage.
	 * 
	 * @param screen the definition of data to be displayed
	 */
	private static void display(Screens screen) {
		
		if (screen == null) {
			return;
		}
		
		switch(screen) {
		case MAIN:
			
			Main.display();
			Main.Options userOption = Main.getUserInput();
			
			display(Screens.getScreenFromMenuSelection(userOption, Main.class));
			
			break;
		case SELECT_TEAM:
			
			try {
				InitMatchScreen.display(initMatch(Environment.Competitions.FRIENDLY), REPETITIONS);
			} catch(InvalidTeamRankingException itrex) {
				System.out.println("Problem retrieving ranking of selected team. Probably team not found");
			} catch(DataException datex) {
				System.out.println("Statistical data required not found");
			}
			
			// System.out.println("You selected team: " + teamName);
			
		default:
			return;
		}
		
	}

	/**
	 * Initialize match details. These details are retrieved from interaction with the user.
	 * 
	 * @param label A generic description of the match, e.g. "friendly match" 
	 * @return the initialized match object
	 */
	public static Match initMatch(String label) {
		
		NationSelection.display();
		String homeLeague = NationSelection.getUserInput();
		
		// System.out.println("You selected league: " + league);

		TeamSelection.display(homeLeague);
		String homeTeamName = TeamSelection.getUserInput();
		
		NationSelection.display();
		String awayLeague = NationSelection.getUserInput();
		
		String awayTeamName;
		
		do {
			TeamSelection.display(awayLeague);
			awayTeamName = TeamSelection.getUserInput();
			
			if (homeTeamName.equals(awayTeamName)) {
				System.out.println("A team cannot play itself. Please select another team");
			}
			
		} while (homeTeamName.equals(awayTeamName));
		
		VenueSelection.display();
		Ground venue = VenueSelection.getUserInput();
		
		return new Match(homeTeamName, awayTeamName, venue, label);
	}
}
