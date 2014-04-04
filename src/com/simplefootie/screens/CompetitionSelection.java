package com.simplefootie.screens;

import java.util.HashMap;
import java.util.Map;

import com.simplefootie.domain.Competition;
import com.simplefootie.domain.Environment;

public class CompetitionSelection {
	
	public static Map<Integer, Competition> display() {
	
		// Get registered competitions
		Map<String, Competition> registeredCompetitions = Environment.getCompetitions();
		Map<Integer, Competition> competitionSelection = new HashMap<Integer, Competition>();
		
		int count = 1;
		for (Map.Entry<String, Competition> competitionEntry: registeredCompetitions.entrySet()) {
			System.out.println(count + ". " + competitionEntry.getKey());
			competitionSelection.put(count++, competitionEntry.getValue());
		}
		return competitionSelection;
	}
}
