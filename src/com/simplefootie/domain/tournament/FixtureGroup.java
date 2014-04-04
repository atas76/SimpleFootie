package com.simplefootie.domain.tournament;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.simplefootie.domain.Match;
import com.simplefootie.domain.Team;

public class FixtureGroup {
	
	private String title;
	private List<Match> matches = new ArrayList<Match>();
	private boolean determinant;
	private Map<Match, Grouping> matchGroupings = new HashMap<Match, Grouping>(); 
	
	public FixtureGroup(String title, boolean determinant) {
		this.title = title;
		this.determinant = determinant;
	}
	
	public boolean isDeterminant() {
		return this.determinant;
	}
	
	public void addMatch(Match match, Grouping grouping) {
		this.matches.add(match);
		this.matchGroupings.put(match, grouping);
	}
	
	public List<Match> getMatches() {
		return this.matches;
	}
	
	public void calculateGroupingOutcome(Match match, List<TieBreaker> tieBreakers, StageType stageType, PairingType pairingType) {
		matchGroupings.get(match).calculateGroupingOutcome(match, tieBreakers, stageType, pairingType);
	}
	
	public void display() {
		
		System.out.println();
		System.out.println(this.title);
		System.out.println();
		
		for (Match match:this.matches) {
			System.out.println(match);
		}
		
		System.out.println();
	}
}
