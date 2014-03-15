package com.simplefootie.domain;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.simplefootie.Resources;

/**
 * 
 * The Environment class comprises a set of static methods, used for the "definition" of the environment. When the program starts running, the environment is loaded, 
 * which is the "realm" of the simulation, containing all the necessary details. At this point, the environment is the names of the teams and some statistical
 * hardcoded data used in match calculation. This class will be used mainly as a facade for managing the data retrieval classes, avoiding any hardcoded and
 * arbitrary values in the future.
 * 
 * @author Andreas Tasoulas
 *
 */
public class Environment {
	
	private static Document environmentDocument;
	
	private static Map<String, Competition> competitions = new HashMap<String, Competition>();

	public static class Competitions {
		public final static String FRIENDLY = "Friendly";
	}
	
	/**
	 * Parses the db.xml file containing the environment details, which for the time being are the teams' names and loads the details for access by subsequent classes.
	 * Sorts the teams according to their ranking.
	 * Creates a friendly competition instance and sets to it the loaded teams.
	 * Hardcoded distribution data are added for the competition, which are needed for scores calculation, and the competition is registered to the environment.
	 * 
	 * @param path the path of the xml file containing the environment details
	 * @throws ParserConfigurationException XML Parser configuration error
	 * @throws IOException Problem with reading the file
	 * @throws SAXException Problem parsing the file
	 */
	public static void load(String path) throws ParserConfigurationException, IOException, SAXException {
		
		DocumentBuilderFactory docBuildFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docBuildFactory.newDocumentBuilder();
		
		environmentDocument = docBuilder.parse(new File(path));
		
		List<Team> teams = Leagues.getAllTeams(environmentDocument);
		
		Collections.sort(teams);
		
		Competition friendlyCompetition = new Competition();
		friendlyCompetition.setTeams(teams);
		
		// Hardcode everything for the time being
		
		List<Map<Integer, Integer>> goalDifferences = new ArrayList<Map<Integer, Integer>>();
		List<Map<Integer, Integer>> scoreEntropies = new ArrayList<Map<Integer, Integer>>();
		
		Map<Integer, Integer> strongHomeGoalDifferences = new TreeMap<Integer, Integer>();
		Map<Integer, Integer> weakHomeGoalDifferences = new TreeMap<Integer, Integer>();
		Map<Integer, Integer> strongAwayDifferences = new TreeMap<Integer, Integer>();
		Map<Integer, Integer> weakAwayDifferences = new TreeMap<Integer, Integer>();
		
		strongHomeGoalDifferences.put(-2, 1);
		strongHomeGoalDifferences.put(-1, 2);
		strongHomeGoalDifferences.put(0, 10);
		strongHomeGoalDifferences.put(1, 8);
		strongHomeGoalDifferences.put(2, 8);
		strongHomeGoalDifferences.put(3, 5);
		strongHomeGoalDifferences.put(4, 5);
		strongHomeGoalDifferences.put(5, 4);
		
		goalDifferences.add(strongHomeGoalDifferences);
		
		weakHomeGoalDifferences.put(-2, 2);
		weakHomeGoalDifferences.put(-1, 4);
		weakHomeGoalDifferences.put(0, 8);
		weakHomeGoalDifferences.put(1, 8);
		weakHomeGoalDifferences.put(2, 5);
		weakHomeGoalDifferences.put(3, 2);
		weakHomeGoalDifferences.put(4, 2);
		
		goalDifferences.add(weakHomeGoalDifferences);
		
		weakAwayDifferences.put(-4, 1);
		weakAwayDifferences.put(-3, 1);
		weakAwayDifferences.put(-2, 1);
		weakAwayDifferences.put(-1, 8);
		weakAwayDifferences.put(0, 14);
		weakAwayDifferences.put(1, 11);
		weakAwayDifferences.put(2, 8);
		weakAwayDifferences.put(3, 8);
		
		goalDifferences.add(weakAwayDifferences);
		
		strongAwayDifferences.put(-5, 1);
		strongAwayDifferences.put(-4, 2);
		strongAwayDifferences.put(-3, 3);
		strongAwayDifferences.put(-2, 6);
		strongAwayDifferences.put(-1, 13);
		strongAwayDifferences.put(0, 11);
		strongAwayDifferences.put(1, 7);
		strongAwayDifferences.put(2, 2);
		
		goalDifferences.add(strongAwayDifferences);
		
		Map<Integer, Integer> strongHomeEntropies = new HashMap<Integer, Integer>();
		Map<Integer, Integer> weakHomeEntropies = new HashMap<Integer, Integer>();
		Map<Integer, Integer> strongAwayEntropies = new HashMap<Integer, Integer>();
		Map<Integer, Integer> weakAwayEntropies = new HashMap<Integer, Integer>();
		
		strongHomeEntropies.put(-2, 1);
		strongHomeEntropies.put(-1, 6);
		strongHomeEntropies.put(0, 18);
		strongHomeEntropies.put(1, 15);
		strongHomeEntropies.put(2, 2);
		strongHomeEntropies.put(3, 1);
		
		scoreEntropies.add(strongHomeEntropies);
		
		weakHomeEntropies.put(-1, 9);
		weakHomeEntropies.put(0, 17);
		weakHomeEntropies.put(1, 4);
		weakHomeEntropies.put(2, 1);
		
		scoreEntropies.add(weakHomeEntropies);
		
		weakAwayEntropies.put(-1, 14);
		weakAwayEntropies.put(0, 23);
		weakAwayEntropies.put(1, 8);
		weakAwayEntropies.put(2, 1);
		
		scoreEntropies.add(weakAwayEntropies);
		
		strongAwayEntropies.put(-1, 11);
		strongAwayEntropies.put(0, 24);
		strongAwayEntropies.put(1, 9);
		strongAwayEntropies.put(2, 1);
		
		scoreEntropies.add(strongAwayEntropies);
		
		friendlyCompetition.setExpectedGoalDifferences(goalDifferences);
		friendlyCompetition.setExpectedEntropies(scoreEntropies);
		
		competitions.put(Competitions.FRIENDLY, friendlyCompetition);
	}
	
	/**
	 * Loads the default xml file containing the environment details. This is assumed to be the hardcoded db.xml file for the time being.
	 * 
	 * @throws ParserConfigurationException See {@link #Environment()}.{@link #load(String)}
	 * @throws IOException See {@link #Environment()}.{@link #load(String)}
	 * @throws SAXException See {@link #Environment()}.{@link #load(String)}
	 * @see {@link #Environment()}.{@link #load(String)}
	 */
	public static void load() throws ParserConfigurationException, IOException, SAXException {
		Environment.load(Resources.getDataResource(Resources.DB));
	}
	
	public static Document getDataDocument() {
		return environmentDocument;
	}
	
	public static Competition getCompetition(String name) {
		return competitions.get(name);
	}
}
