package com.simplefootie.screens;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.simplefootie.domain.Leagues;


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
	public static String getUserInput() {
		
		System.out.println();
		System.out.print("Select league: ");
		
		Scanner in = new Scanner(System.in);
		int userOption = in.nextInt();
		
		// in.close();
		
		return optionMap.get(userOption);
	}
}
