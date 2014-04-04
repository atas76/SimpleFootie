package com.simplefootie.domain.tournament;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.simplefootie.domain.Team;

public class DrawTest {
	
	private Draw draw;
	
	private final int groupingSize = 2;
	private List<Team> teams = new ArrayList<Team>();
	
	@Before
	public void setUp() {
		
		// Hardcode a number of teams (we just limit the input to the bare minimum, instead of going all the way and loading the data from persistence)
		teams.add(new Team("Hamburger SV"));
		teams.add(new Team("Werder Bremen"));
		teams.add(new Team("Freiburg"));
		teams.add(new Team("Wolfsburg"));
		teams.add(new Team("Eintracht Frankfurt"));
		teams.add(new Team("Bayer Leverkusen"));
		teams.add(new Team("Borussia Dortmund"));
		teams.add(new Team("Mainz 05"));
		teams.add(new Team("Stuttgart"));
		teams.add(new Team("Hannover 96"));
		teams.add(new Team("Borussia Mönchengladbach"));
		teams.add(new Team("Bayern München"));
		
		this.draw = new Draw();
		this.draw.setGroupingSize(this.groupingSize);
		this.draw.setTeams(this.teams);
	}
	
	@Test
	public void testMake() {
		
		this.draw.make();
		
		int expectedNumberOfGroupings = this.teams.size() / this.groupingSize;
		assertEquals(expectedNumberOfGroupings, this.draw.getGroupings().size());
		
		List<String> drawnTeamNames = new ArrayList<String>();
		
		for (Grouping grouping:this.draw.getGroupings()) {	
			
			assertEquals(this.groupingSize, grouping.getTeams().size());
			
			for (Team team: grouping.getTeams()) {
				assertTrue(!drawnTeamNames.contains(team.getName()));
				drawnTeamNames.add(team.getName());
			}
		}
		// And finally display the draw
		System.out.println(this.draw);
	}
}
