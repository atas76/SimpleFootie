package com.simplefootie.screens;

import java.util.Scanner;

import com.simplefootie.domain.Ground;


/**
 * 
 * The venue selection screen. Two options are supported: either in the designated home team's ground or in a neutral ground, for the purpose of calculating home advantage.
 * No actual venue names are used.
 * 
 * @author Andreas Tasoulas
 * @see com.simplefootie.gui.Screens
 *
 */
public class VenueSelection {
	
	/**
	 * Display a list of possible venues for the current match to take place. 
	 * To keep things simple the user can either select the 'Home' ground (the ground of the 'home' team selected for the match) or a generic 'Neutral' ground.
	 */
	public static void display() {
		
		System.out.println();
		
		int count = 1;
		for (Ground groundOption:Ground.values()) {
			System.out.println(count++ + "." + groundOption.getName());
		}
	}
	
	/**
	 * Get input from the user for choosing the match's venue.
	 * 
	 * @return a value denoting either a 'home' or 'neutral' ground venue for the match
	 */
	public static Ground getUserInput() {
		
		System.out.println();
		System.out.print("Select venue: ");
		
		Scanner in = new Scanner(System.in);
		int userOption = in.nextInt();
		
		// in.close();
		
		return Ground.values()[userOption - 1];
	}
}
