package com.simplefootie.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.simplefootie.domain.Environment;
import com.simplefootie.domain.Leagues;

public class LeaguesTest {
	
	@BeforeClass
	public static void setUp() throws Exception {
		Environment.load();
	}
	
	@Test
	public void testGetAllNames() {
		
		List<String> leagueNames = Leagues.getAllNames();
		
		assertEquals(3, leagueNames.size());
		
		assertTrue(leagueNames.contains("Bundesliga"));
		assertTrue(leagueNames.contains("Premier League"));
		assertTrue(leagueNames.contains("La Liga"));
	}
	
	@Test
	public void testGetLeagueTeams() {
		
		List<String> germanTeams = Leagues.getTeams("Bundesliga");
		List<String> englishTeams = Leagues.getTeams("Premier League");
		List<String> spanishTeams = Leagues.getTeams("La Liga");
		
		assertEquals(18, germanTeams.size());
		assertEquals(12, englishTeams.size());
		assertEquals(12, spanishTeams.size());
		
		assertTrue(germanTeams.contains("Mainz 05"));
		assertTrue(englishTeams.contains("Newcastle United"));
		assertTrue(spanishTeams.contains("Sevilla"));
		
	}
}
