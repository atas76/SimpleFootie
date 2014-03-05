package com.simplefootie.screens;

import java.util.Scanner;

import com.simplefootie.data.DataException;
import com.simplefootie.domain.Environment;
import com.simplefootie.domain.Ground;
import com.simplefootie.domain.Match;
import com.simplefootie.domain.exceptions.InvalidTeamRankingException;


/**
 * 
 * The match preview screen.
 * 
 * @author Andreas Tasoulas
 * @see com.simplefootie.gui.Screens
 *
 */
public class InitMatchScreen {
	
	/**
	 * Displays the match details as a preview before generating the result. 
	 * Upon the user initiation it also sends a message to the match object for calculating the result(s), so it also serves as an MVC controller.
	 * 
	 * @param matchDetails the match object, which contains all the details related to the match for displaying and calculating the result(s)
	 * @param repetitions the number of results that will be generated for the current match
	 * @throws InvalidTeamRankingException Thrown during match calculation
	 * @throws DataException Thrown during match calculation
	 */
	public static void display(Match matchDetails, int repetitions) throws InvalidTeamRankingException, DataException {
		
		System.out.println();
		System.out.println(matchDetails.getLabel());
		
		System.out.print(matchDetails.getHomeTeamName());
		System.out.print(" - ");
		System.out.println(matchDetails.getAwayTeamName());
		
		System.out.println();
		
		System.out.println("Venue: " + (matchDetails.getVenue().equals(Ground.HOME_GROUND)?matchDetails.getHomeTeamName() + " ground":"Neutral"));
		System.out.println();
		System.out.println("Press enter to continue");
		
		Scanner in = new Scanner(System.in);
		in.nextLine();
		
		// System.out.println("Match should be started");
		
		for (int i = 0; i < repetitions; i++) {
			matchDetails.calculateResult(Environment.getCompetition(Environment.Competitions.FRIENDLY));
			matchDetails.displayResult();
		}
	}
}
