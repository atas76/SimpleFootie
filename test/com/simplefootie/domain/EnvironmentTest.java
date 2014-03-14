package com.simplefootie.domain;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.simplefootie.domain.Competition;
import com.simplefootie.domain.Environment;
import com.simplefootie.domain.Team;

public class EnvironmentTest {
	
	@BeforeClass
	public static void setUp() throws SAXException, IOException, ParserConfigurationException {
		Environment.load();
	}
	
	@Test
	public void testFriendlyCompetitionSetup() {
		
		Competition friendlyCompetition = Environment.getCompetition(Environment.Competitions.FRIENDLY);
		
		List<Team> friendlyCompetitionTeams = friendlyCompetition.getTeams();
		
		int position = 1;
		
		for (Team team:friendlyCompetitionTeams) {
			System.out.println(position++ + " " + team.getName());
		}
	}

}
