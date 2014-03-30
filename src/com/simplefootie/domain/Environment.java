package com.simplefootie.domain;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.simplefootie.Resources;
import com.simplefootie.data.ScoresPersistence;

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
		
		// Use this sorting by default (as a 'universal' one), but to be changed per competition 
		Collections.sort(teams);
		
		List<Score> scoreSample = ScoresPersistence.read(Resources.getDataResource(Resources.UNIVERSAL_SCORE_SAMPLES));
		
		// Friendly competition
		Competition friendlyCompetition = new Competition(RankingMode.UEFA);
		friendlyCompetition.setTeams(teams);
		
		// In the future we will provide for filtering options of the sample scores
		friendlyCompetition.setScoreSample(scoreSample);
	
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
