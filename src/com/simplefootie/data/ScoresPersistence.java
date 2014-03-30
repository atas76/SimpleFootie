package com.simplefootie.data;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.simplefootie.domain.Score;

public class ScoresPersistence {
	
	/**
	 * Utility method for reading a binary scores file
	 * 
	 * @param filePath the path of the binary file
	 * @return a list of <code>Score</code> domain objects
	 */
	public static List<Score> read(String filePath) throws FileNotFoundException, IOException {
		
		List<Score> scores = new ArrayList<Score>();
		
		File file = new File(filePath);
		byte [] data = new byte[ (int) file.length()];
		
		BufferedInputStream input = new BufferedInputStream(new FileInputStream(file));
		input.read(data);
		scores = decodeScores(data);
		
		input.close();
		
		return scores;
	}
	
	private static List<Score> decodeScores(byte [] data) {
		
		List<Score> scores = new ArrayList<Score>();
		
		for (int i = 0; i < data.length; i+=4) {
			
			int homeTeamRanking = data[i];
			int awayTeamRanking = data[i+1];
			int homeScore = data[i+2];
			int awayScore = data[i+3];
			
			scores.add(new Score(homeTeamRanking, awayTeamRanking, homeScore, awayScore));
		}
		
		return scores;
	}
}
