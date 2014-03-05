package com.simplefootie.outcomes;

import java.util.Map;

import java.util.Random;
import java.util.logging.Logger;

/**
 * 
 * A generic class for keeping statistical calculations.
 * 
 * @author Andreas Tasoulas
 *
 */
public class StatsOutcome {
	
	private static Random rnd = new Random();
	private static Logger log = Logger.getLogger("outcomes.StatsOutcome");
	
	/**
	 * Calculates a random integer weighted by a distribution map.
	 * 
	 * @param distribution a map between an integer (denoting for example goal difference) and cardinality in a sample of matches
	 * @return a random key of the distribution map weighted by its corresponding value 
	 */
	public static int getResultFromDistribution(Map<Integer, Integer> distribution) {
		
		// log.setLevel(Level.FINE);
		
		int sampleSize = 0;
		
		for (Integer samplePartialSize:distribution.values()) {
			sampleSize += samplePartialSize;
		}
		
		log.fine("Sample size: " + sampleSize);
		
		int rawResult = rnd.nextInt(sampleSize);
		
		log.fine("Raw result: " + rawResult);
		
		int currentResult = 0;
		int result = 0;
		for (Map.Entry<Integer, Integer> distributionAllocation:distribution.entrySet()) {
			
			currentResult += distributionAllocation.getValue();
			
			if (rawResult < currentResult) {
				result = distributionAllocation.getKey();
				break;
			}
		}
		
		return result;
	}

}
