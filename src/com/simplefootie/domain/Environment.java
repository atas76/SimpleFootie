package com.simplefootie.domain;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
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
import com.simplefootie.domain.tournament.CompetitionStage;
import com.simplefootie.domain.tournament.PairingType;
import com.simplefootie.domain.tournament.StageType;
import com.simplefootie.domain.tournament.TieBreaker;

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
		public final static String GERMAN_KNOCKOUT = "Bundesliga knockout tournament";
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
		
		loadData(path);
		
		// Universal 'friendly' competition
		Competition friendlyCompetition = createCompetition(Leagues.getAllTeams(environmentDocument), Resources.UNIVERSAL_SCORE_SAMPLES, RankingMode.UEFA, null);
		friendlyCompetition.setName(Competitions.FRIENDLY);
		
		// Create a rules template for knockout competitions
		List<TieBreaker> roundTieBreakers = new ArrayList<TieBreaker>(); 
		roundTieBreakers.add(TieBreaker.AWAY_GOALS);
		roundTieBreakers.add(TieBreaker.EXTRA_TIME);
		roundTieBreakers.add(TieBreaker.PENALTY_SHOOUTOUT);
		
		List<TieBreaker> finalTieBreakers = new ArrayList<TieBreaker>();
		finalTieBreakers.add(TieBreaker.EXTRA_TIME);
		finalTieBreakers.add(TieBreaker.PENALTY_SHOOUTOUT);
		
		// We hardcode here the current max number of teams per nation (Bundesliga)
		CompetitionStage germanPreliminaryRound = new CompetitionStage(StageType.KNOCKOUT, PairingType.DOUBLE_MATCH, roundTieBreakers, 12, "Preliminary round");
		CompetitionStage quarterFinals = new CompetitionStage(StageType.KNOCKOUT, PairingType.DOUBLE_MATCH, roundTieBreakers, 8, "Quarter finals");
		CompetitionStage semiFinals = new CompetitionStage(StageType.KNOCKOUT, PairingType.DOUBLE_MATCH, roundTieBreakers, 4, "Semi finals");
		CompetitionStage theFinal = new CompetitionStage(StageType.KNOCKOUT, PairingType.SINGLE_MATCH_NEUTRAL, finalTieBreakers, 2, "Final");
		
		List<CompetitionStage> knockoutCompetitionStages = new ArrayList<CompetitionStage>();
		knockoutCompetitionStages.add(quarterFinals);
		knockoutCompetitionStages.add(semiFinals);
		knockoutCompetitionStages.add(theFinal);
		
		List<CompetitionStage> germanKnockoutStages = new ArrayList<CompetitionStage>();
		germanKnockoutStages.add(germanPreliminaryRound);
		germanKnockoutStages.addAll(knockoutCompetitionStages);
		
		// Bundesliga
		Competition germanKnockout = createCompetition(Leagues.getTeams("Bundesliga"), Resources.UNIVERSAL_SCORE_SAMPLES, RankingMode.UEFA, null);
		germanKnockout.setStages(germanKnockoutStages);
		germanKnockout.setName(Competitions.GERMAN_KNOCKOUT);
		
		// Registering of competitions
		competitions.put(Competitions.FRIENDLY, friendlyCompetition);
		competitions.put(Competitions.GERMAN_KNOCKOUT, germanKnockout);
	}

	public static void loadData(String path)
			throws ParserConfigurationException, SAXException, IOException {
		
		DocumentBuilderFactory docBuildFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docBuildFactory.newDocumentBuilder();
		
		environmentDocument = docBuilder.parse(new File(path));
	}

	/**
	 * 
	 * @param teams
	 * @param resourceFile
	 * @param rankingMode
	 * @param parentRanking we use this ranking as a basis for deriving the score sample (by restriction of the initially specified)
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static Competition createCompetition(List<Team> teams, String resourceFile, RankingMode rankingMode, List<Team> parentRanking)
			throws FileNotFoundException, IOException {
		
		// Use this sorting by default (as a 'universal' one), but to be changed per competition 
		Collections.sort(teams);
		
		if (parentRanking != null) {
			// TODO: Filter parent score samples 
		}
		
		List<Score> scoreSample = ScoresPersistence.read(Resources.getDataResource(resourceFile));
		
		Competition competition = new Competition(rankingMode);
		competition.setTeams(teams);
		
		competition.setScoreSample(scoreSample);
		return competition;
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
	
	public static void loadData() throws IOException, ParserConfigurationException, SAXException {
		Environment.loadData(Resources.getDataResource(Resources.DB));
	}
	
	public static Document getDataDocument() {
		return environmentDocument;
	}
	
	public static Competition getCompetition(String name) {
		return competitions.get(name);
	}
	
	public static Map<String, Competition> getCompetitions() {
		return Environment.competitions;
	}
}
