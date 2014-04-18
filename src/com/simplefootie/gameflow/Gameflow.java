package com.simplefootie.gameflow;

import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;

import com.simplefootie.data.DataException;
import com.simplefootie.domain.Competition;
import com.simplefootie.domain.Environment;
import com.simplefootie.domain.Environment.Competitions;
import com.simplefootie.domain.Ground;
import com.simplefootie.domain.Match;
import com.simplefootie.domain.exceptions.InvalidTeamRankingException;
import com.simplefootie.domain.exceptions.TeamNotFoundException;
import com.simplefootie.gui.Screens;
import com.simplefootie.screens.CompetitionSelection;
import com.simplefootie.screens.InitMatchScreen;
import com.simplefootie.screens.Main;
import com.simplefootie.screens.NationSelection;
import com.simplefootie.screens.TeamSelection;
import com.simplefootie.screens.VenueSelection;
import com.simplefootie.util.CmdLineParser;

/**
 * 
 * The class controlling the flow of the 'game'. There is a hardcoded constant for how many times a match is to be repeated, i.e. how many different results will be
 * generated.
 * 
 * @author Andreas Tasoulas
 *
 */
public class Gameflow {
	
	private static Logger logger; 
	private final static String usageMsg = "Usage: <team> <team> [-n] [--r=<number-of-repetitions>]";
	
	private static int REPETITIONS = 1;

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
		
		if (args.length == 0) {
			display(Screens.MAIN);
		} else {
			
			// logger = Logger.getLogger("Main");
			
			String homeTeamName = null;
			String awayTeamName = null;
			Ground venue = Ground.HOME_GROUND;
			
			for (String arg:args) {
				
				if (arg.startsWith("--")) {
					if (arg.charAt(2) == 'r') {
						
						CmdLineParser repetitionsParser = new CmdLineParser(arg);
						
						int equalsIndex = repetitionsParser.anticipate(3, '=');
						if (equalsIndex == -1) {
							// logger.info("Equals sign not found");
							System.out.println("Generic parse error");
							System.out.println(usageMsg);
							System.exit(1);
						}
						
						int repetitionsIndex = repetitionsParser.anticipateDigit(equalsIndex + 1);
						if (repetitionsIndex == -1) {
							// logger.info("Number not found");
							System.out.println("Generic parse error");
							System.out.println(usageMsg);
							System.exit(1);
						}
						
						try {
							REPETITIONS = Integer.parseInt(arg.substring(repetitionsIndex, arg.length()));
						} catch (NumberFormatException nfex) {
							System.out.println("Error parsing the number of repetitions");
							System.out.println(usageMsg);
							System.exit(2);
						}
					} else {
						System.out.println("Unknown switch");
						System.out.println(usageMsg);
						System.exit(3);
					}
				} else if (arg.startsWith("-")) {
					if (arg.charAt(1) == 'n') {
						venue = Ground.NEUTRAL_GROUND;
					} else {
						System.out.println("Unknown switch");
						System.out.println(usageMsg);
						System.exit(3);
					}
				} else {
					if (homeTeamName == null) {
						homeTeamName = arg;
					} else if (awayTeamName == null) {
						awayTeamName = arg;
					} else {
						System.out.println("Too many arguments");
						System.out.println(usageMsg);
						System.exit(4);
					}
				}
			}
			
			if (homeTeamName == null) {
				System.out.println("No teams specified");
				System.out.println(usageMsg);
				System.exit(5);
			}
			
			if (homeTeamName.equals(awayTeamName)) {
				System.out.println("Nice try, but a team playing against itself is not allowed. It might break the Universe.");
				System.exit(-1);
			}
			
			if (awayTeamName == null) {
				System.out.println("Only one team specified");
				System.out.println(usageMsg);
				System.exit(5);
			}
			
			try {
				InitMatchScreen.display(new Match(Environment.getCompetition(Competitions.FRIENDLY).getTeamByShortName(homeTeamName), 
										Environment.getCompetition(Competitions.FRIENDLY).getTeamByShortName(awayTeamName), 
										venue, 
										Environment.Competitions.FRIENDLY), REPETITIONS);
			} catch (DataException datex) {
				System.out.println("Statistical data required not found");
			} catch (InvalidTeamRankingException itrex) {
				System.out.println("Problem retrieving ranking of selected team. Probably team not found. Please consult the db.xml file for the correct team short name");
			} catch (TeamNotFoundException tnfex) {
				System.out.println("Error: team " + tnfex.getTeamShortName() + " not found");
			}
		}
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
			try {
				processMainMenu();
			} catch(InvalidUserInputException iuiex) {
				System.out.println(iuiex.getMessage());
				display(screen);
			}
			break;
		case SELECT_TEAM:
			try {
				try {
					InitMatchScreen.display(initMatch(Environment.Competitions.FRIENDLY), REPETITIONS);
				} catch(InvalidUserInputException iuiex) {
					System.out.println(iuiex.getMessage());
					display(screen);
				}
			} catch(InvalidTeamRankingException itrex) {
				System.out.println("Problem retrieving ranking of selected team. Probably team not found");
			} catch(DataException datex) {
				System.out.println("Statistical data required not found");
			}
			break;
		case SELECT_COMPETITION:
			System.out.println();
			Map<Integer, Competition> competitionSelection = CompetitionSelection.display();
			
			try {
				startNextCompetition(competitionSelection);
			} catch (DataException datex) {
				System.out.println("Statistical data required not found");
			} catch (InvalidTeamRankingException itrex) {
				System.out.println("Problem retrieving ranking of selected team. Probably team not found");
			}
			try {
				processMainMenu();
			} catch(InvalidUserInputException iuiex) {
				System.out.println(iuiex.getMessage());
				display(Screens.MAIN); // Main is the current screen after competition is over, so we want to revert back to it in case of error
			}
		default:
			return;
		}
	}

	private static void startNextCompetition(Map<Integer, Competition> competitionSelection) throws InvalidTeamRankingException,DataException {
		try {
			int selectedCompetitionIndex = getUserInput("Select competition: ");
			Competition selectedCompetition = competitionSelection.get(selectedCompetitionIndex);
			selectedCompetition.play();
		} catch (InvalidUserInputException iuiex) {
			System.out.println(iuiex.getMessage());
			startNextCompetition(competitionSelection);
		} catch (NullPointerException npe) { // no competition was found (equivalent to array index out-of-bounds)
			System.out.println(ErrorMessages.MENU_OPTION_OUT_OF_BOUNDS);
			startNextCompetition(competitionSelection);
		}
	}

	private static void processMainMenu() throws InvalidUserInputException {
		Main.display();
		Main.Options userOption = Main.getUserInput();
		
		display(Screens.getScreenFromMenuSelection(userOption, Main.class));
	}
	
	public static int getUserInput(String prompt) throws InvalidUserInputException {
		
		System.out.println();
		System.out.print(prompt);
		
		Scanner in = new Scanner(System.in);
		
		try {
			int userOption = in.nextInt();
			return userOption;
		} catch (ArrayIndexOutOfBoundsException aiobex) {
			throw new InvalidUserInputException(ErrorMessages.MENU_OPTION_OUT_OF_BOUNDS);
		} catch (InputMismatchException imex) {
			throw new InvalidUserInputException(ErrorMessages.INVALID_MENU_OPTION);
		}
	}

	/**
	 * Initialize match details. These details are retrieved from interaction with the user.
	 * 
	 * @param label A generic description of the match, e.g. "friendly match" 
	 * @return the initialized match object
	 */
	public static Match initMatch(String label) throws InvalidUserInputException {
			
		String homeTeamName = selectTeam(null);
		String awayTeamName = selectTeam(homeTeamName);
		
		Ground venue = selectVenue();
		
		return new Match(homeTeamName, awayTeamName, venue, label);
	}

	private static Ground selectVenue() {
		try {
			VenueSelection.display();
			Ground venue = VenueSelection.getUserInput();
			return venue;
		} catch (InvalidUserInputException iuiex) {
			System.out.println(iuiex.getMessage());
			return selectVenue();
		}
	}
	
	private static String selectTeam(String opponent) {
		
		String league = selectTeamNation();
		String teamName = selectTeamFromLeague(league);
		
		while (opponent != null && opponent.equals(teamName)) {
			System.out.println("A team cannot play against itself. Please select another team");
			teamName = selectTeam(opponent);
		}	
		return teamName;
	}

	private static String selectTeamFromLeague(String league) {
		try {
			TeamSelection.display(league);
			String teamName = TeamSelection.getUserInput();
			return teamName;
		} catch (InvalidUserInputException iuiex) {
			System.out.println(iuiex.getMessage());
			return selectTeamFromLeague(league);
		}
	}

	private static String selectTeamNation() {
		try {
			NationSelection.display();
			String league = NationSelection.getUserInput();
			return league;
		} catch (InvalidUserInputException iuiex) {
			System.out.println(iuiex.getMessage());
			return selectTeamNation();
		}
	}
}
