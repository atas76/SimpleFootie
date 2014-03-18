package com.simplefootie.data;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.sjon.core.SjonFileReader;
import org.sjon.core.SjonRecord;

import com.simplefootie.Resources;

/**
 * This class is a surrogate for a future class/set of classes or even API that will be responsible for retrieving statistical data related to match calculation.
 * For the time being, the only data loaded from a file (i.e. not hardcoded) are ratios for goals scored/conceded at home to away grounds for each team, used to
 * calculate performance of teams in neutral grounds.
 * 
 * @author Andreas Tasoulas
 *
 */
public class MatchData {
	
	private static SjonFileReader scoredAveragesFile; 
	private static SjonFileReader concededAveragesFile;
	
	private static Map<String, Double> scoredAverages = new HashMap<String, Double>();
	private static Map<String, Double> concededAverages = new HashMap<String, Double>();
	
	private static Logger log = Logger.getLogger("MatchData");
	
	/**
	 * Loads the necessary data from a specified data source, defined in the properties file. The data currently are the ratios of home scored to away scored goals 
	 * and home conceded to away conceded goals.
	 * 
	 * @throws FileNotFoundException One of the data source files is not found
	 * @throws IOException Problem with reading one of the data source files
	 */
	private static void loadData() throws FileNotFoundException, IOException {
		try {
			
			scoredAveragesFile = new SjonFileReader(Resources.getDataResource(Resources.HOME_SCORED_AVG));
			concededAveragesFile = new SjonFileReader(Resources.getDataResource(Resources.HOME_CONCEDED_AVG));
			
			List<SjonRecord> scoredAveragesData = scoredAveragesFile.getData();
			List<SjonRecord> concededAveragesData = concededAveragesFile.getData();
			
			log.config("Averages read from file");
			
			for (SjonRecord teamScoredAverage: scoredAveragesData) {
				String teamName = teamScoredAverage.getString(0);
				Double scoredAverage = teamScoredAverage.getDouble(1);
				scoredAverages.put(teamName, scoredAverage);
			}
			
			for (SjonRecord concededScoredAverage: concededAveragesData) {
				String teamName = concededScoredAverage.getString(0);
				Double concededAverage = concededScoredAverage.getDouble(1);
				concededAverages.put(teamName, concededAverage);
			}
			
			log.config("Averages loaded");
			
		} catch (FileNotFoundException fnex) {
			throw fnex;
		} catch (IOException ioex) {
			throw ioex;
		}
	}
	
	private static void init() throws FileNotFoundException, IOException {
		if (MatchData.scoredAveragesFile == null || MatchData.concededAveragesFile == null) {
			
			log.config("Loading data...");
			
			MatchData.loadData();
		}
	}
	
	/**
	 * Getter for the ratio of home goals vs. away goals scored for a specific team in a set of matches loaded from a data sample (defined in properties file).
	 * 
	 * @param teamName the name of the team, used as an identifier
	 * @return the percentage of goals scored at home ground. It will be used in calculation of team performance in neutral grounds.
	 * The source data are generated independently of the current project and are loaded directly from a data source.
	 * @throws FileNotFoundException Thrown if one of data source files is not found.
	 * @throws IOException Thrown if there is a problem reading from the data source file
	 */
	public static double getVenueRelatedScoredRatio(String teamName) throws FileNotFoundException, IOException {
		
		log.setLevel(Level.CONFIG);
		
		init();
		
		log.config("init() passed");
		log.config("Team name: " + teamName);
		
		return scoredAverages.get(teamName); 
	}
	
	/**
	 * Returns the ratio of home goals vs. away goals conceded for a specific team in a set of matches loaded from a data sample (defined in properties file).
	 * 
	 * @param teamName the name of the team, used as an identifier
	 * @return the percentage of goals conceded at home ground. It will be used in calculation of team performance in neutral grounds.
	 * @throws FileNotFoundException Thrown if one of data source files is not found
	 * @throws IOException Thrown if there is a problem reading from the data source file
	 */
	public static double getVenueRelatedConcededRatio(String teamName) throws FileNotFoundException, IOException {
		init();
		return concededAverages.get(teamName);
	}
}
