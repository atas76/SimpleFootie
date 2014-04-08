package com.simplefootie.screens;

import java.util.HashMap;
import java.util.Map;

import com.simplefootie.domain.Competition;
import com.simplefootie.domain.Environment;
import com.simplefootie.domain.Environment.Competitions;

public class CompetitionSelection {
	
	public static Map<Integer, Competition> display() {
	
		// Get registered competitions
		Map<String, Competition> registeredCompetitions = Environment.getCompetitions();
		Map<Integer, Competition> competitionSelection = new HashMap<Integer, Competition>();
		
		int count = 1;
		for (Map.Entry<String, Competition> competitionEntry: registeredCompetitions.entrySet()) {
			if (competitionEntry.getKey().equals(Competitions.FRIENDLY)) {
				continue; // Friendly match option ('competition') has its own menu option
			}
			System.out.println(count + ". " + competitionEntry.getKey());
			competitionSelection.put(count++, competitionEntry.getValue());
		}
		return competitionSelection;
	}
}
