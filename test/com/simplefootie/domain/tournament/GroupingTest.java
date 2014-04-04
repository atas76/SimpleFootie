package com.simplefootie.domain.tournament;

import org.junit.Before;
import org.junit.Test;

import com.simplefootie.domain.Team;

public class GroupingTest {
	
	private Grouping grouping;
	
	@Before
	public void setUp() {
		this.grouping.addTeam(new Team("Bayern"));
		this.grouping.addTeam(new Team("Dortmund"));
	}
	
	@Test
	public void testCreateScheduleDoubleMatch() {
		
	}
	
	@Test
	public void testCreateScheduleSingleMatch() {
		
	}

}
