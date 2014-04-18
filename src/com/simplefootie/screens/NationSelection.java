package com.simplefootie.screens;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.simplefootie.domain.Leagues;
import com.simplefootie.gameflow.ErrorMessages;
import com.simplefootie.gameflow.InvalidUserInputException;


/**
 * 
 * The nation (= 'domestic league') selection screen: used to narrow down the eventual team selection.
 * 
 * @author Andreas Tasoulas
 * @see com.simplefootie.gui.Screens
 *
 */
public class NationSelection {
	
	private static Map<Integer, String> optionMap = new HashMap<Integer, String>();
	
	/**
	 * Display all the available national leagues. It also initializes the mappings between user choice and league name selected.
	 */
	public static void display() {
		
		optionMap = new HashMap<Integer, String>(); // Clear previous values (for not leaving old values out of the current range)
		
		System.out.println();
		
		List<String> leagueNames = Leagues.getAllNames();
		
		int count = 1;
		for (String leagueName:leagueNames) {
			
			optionMap.put(count, leagueName);
			
			System.out.println(count++ + ". " + leagueName);
		}
	}
	
	/**
	 * Get user's input as an integer.
	 * 
	 * @return a league name corresponding to the user's selection
	 */
	public static String getUserInput() throws InvalidUserInputException {
		
		System.out.println();
		System.out.print("Select league: ");
		
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
		
		// in.close();
	}
}
