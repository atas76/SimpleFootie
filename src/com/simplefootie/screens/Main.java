package com.simplefootie.screens;


import java.util.InputMismatchException;
import java.util.Scanner;

import com.simplefootie.gameflow.ErrorMessages;
import com.simplefootie.gameflow.InvalidUserInputException;
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
		
		PLAY_FRIENDLY("Play friendly (and exit)"),
		PLAY_COMPETITION("Play competition"),
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
		
		System.out.println();
		
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
	public static Options getUserInput() throws InvalidUserInputException {
		
		System.out.println();
		System.out.print("Select option: ");
		
		try {
			Scanner in = new Scanner(System.in);
			int userOption = in.nextInt();
			return Options.values()[userOption - 1];
		} catch (ArrayIndexOutOfBoundsException aiobex) {
			throw new InvalidUserInputException(ErrorMessages.MENU_OPTION_OUT_OF_BOUNDS);
		} catch (InputMismatchException imex) {
			throw new InvalidUserInputException(ErrorMessages.INVALID_MENU_OPTION);
		}
	}
}
