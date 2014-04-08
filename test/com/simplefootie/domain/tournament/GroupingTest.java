package com.simplefootie.domain.tournament;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.simplefootie.domain.Ground;
import com.simplefootie.domain.Match;
import com.simplefootie.domain.Team;

public class GroupingTest {
	
	private Grouping grouping;
	
	@Before
	public void setUp() {
		this.grouping = new Grouping(2);
		this.grouping.addTeam(new Team("Bayern"));
		this.grouping.addTeam(new Team("Dortmund"));
	}
	
	@Test
	public void testCreateScheduleDoubleMatch() {
		
		List<List<Match>> schedule = this.grouping.createSchedule(StageType.KNOCKOUT, PairingType.DOUBLE_MATCH);
		
		assertEquals(2, schedule.size());
		
		List<Match> firstLeg = schedule.get(0);
		List<Match> secondLeg = schedule.get(1);
		
		assertEquals(1, firstLeg.size());
		assertEquals(1, secondLeg.size());
		
		Match firstLegMatch = firstLeg.get(0);
		Match secondLegMatch = secondLeg.get(0);
		
		assertEquals("Bayern", firstLegMatch.getHomeTeamName());
		assertEquals("Dortmund", firstLegMatch.getAwayTeamName());
		assertEquals(Ground.HOME_GROUND, firstLegMatch.getVenue());
		
		assertEquals("Dortmund", secondLegMatch.getHomeTeamName());
		assertEquals("Bayern", secondLegMatch.getAwayTeamName());
		assertEquals(Ground.HOME_GROUND, secondLegMatch.getVenue());
	}
	
	@Test
	public void testCreateScheduleSingleMatchNeutral() {
		
		List<List<Match>> schedule = this.grouping.createSchedule(StageType.KNOCKOUT, PairingType.SINGLE_MATCH_NEUTRAL);
		assertEquals(1, schedule.size());
		
		List<Match> matchDay = schedule.get(0);
		assertEquals(1, matchDay.size());
		
		Match match = matchDay.get(0);
		
		assertEquals("Bayern", match.getHomeTeamName());
		assertEquals("Dortmund", match.getAwayTeamName());
		assertEquals(Ground.NEUTRAL_GROUND, match.getVenue());
	}
}
