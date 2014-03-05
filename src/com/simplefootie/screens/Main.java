package com.simplefootie.screens;


import java.util.Scanner;

import com.simplefootie.gui.MenuOptions;

/**
 * 
 * The "main menu" screen.
 * 
 * @author Andreas Tasoulas
 * @see com.simplefootie.gui.Screens
 *
 */
public class Main  {
	
	public static enum Options implements MenuOptions {
		
		PLAY_FRIENDLY("Play friendly"),
		EXIT("Exit");
		
		private String text;
		
		Options(String text) {
			this.text = text;
		}
	}
	
	/**
	 * Displays the options for the user to choose from
	 */
	public static void display() {
		int count = 1;
		for (Options menuOption: Options.values()) {
			System.out.println(count++ + ". " + menuOption.text);
		}
	}
	
	/**
	 * Gets from standard input a number to correspond to a user's selected option.
	 * 
	 * @return the current option selected from the user
	 */
	public static Options getUserInput() {
		
		System.out.println();
		System.out.print("Select option: ");
		
		Scanner in = new Scanner(System.in);
		int userOption = in.nextInt();
		
		// System.out.println("The user has selected: " + i);
		
		// in.close();
		
		return Options.values()[userOption - 1];
	}
}
