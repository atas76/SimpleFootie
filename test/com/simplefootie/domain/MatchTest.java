package com.simplefootie.domain;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.simplefootie.data.DataException;
import com.simplefootie.domain.Environment;
import com.simplefootie.domain.Ground;
import com.simplefootie.domain.Match;
import com.simplefootie.domain.Team;
import com.simplefootie.domain.exceptions.InvalidTeamRankingException;


public class MatchTest {
	
	private Match match;
	
	@BeforeClass
	public static void setUp() throws ParserConfigurationException, IOException, SAXException {
		
		Environment.load();
		
	}
	
	@Test
	public void testResult() throws InvalidTeamRankingException, DataException {
		
		Team homeTeam = new Team("Freiburg");
		Team awayTeam = new Team("Wolfsburg");
		
		match = new Match(homeTeam, awayTeam, Ground.HOME_GROUND);
		
		match.calculateResult(Environment.getCompetition(Environment.Competitions.FRIENDLY));
		
		System.out.println("Result: ");
		System.out.print(homeTeam.getName() + " - " + awayTeam.getName() + " " + match.getHomeTeamScore() + "-" + match.getAwayTeamScore());
	}
}
