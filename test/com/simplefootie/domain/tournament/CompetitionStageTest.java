package com.simplefootie.domain.tournament;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.simplefootie.Resources;
import com.simplefootie.data.DataException;
import com.simplefootie.domain.Competition;
import com.simplefootie.domain.Environment;
import com.simplefootie.domain.Leagues;
import com.simplefootie.domain.RankingMode;
import com.simplefootie.domain.Team;
import com.simplefootie.domain.exceptions.InvalidTeamRankingException;

public class CompetitionStageTest {
	
	private List<TieBreaker> tieBreakers = new ArrayList<TieBreaker>();
	private CompetitionStage competitionStage;
	private Competition hostCompetition;
	
	@Before
	public void setUp() throws IOException, SAXException, ParserConfigurationException {
		
		Environment.loadData(); // Just load the bare data without creating the standard competitions
		
		this.tieBreakers.add(TieBreaker.AWAY_GOALS);
		this.tieBreakers.add(TieBreaker.EXTRA_TIME);
		this.tieBreakers.add(TieBreaker.PENALTY_SHOOUTOUT);
		
		List<Team> teams = new ArrayList<Team>();
		
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
		
		this.competitionStage = new CompetitionStage(StageType.KNOCKOUT, PairingType.DOUBLE_MATCH, this.tieBreakers, 12, "Preliminary stage");
		this.competitionStage.setQualifiedTeams(teams);
		this.competitionStage.draw();
		this.competitionStage.createFixtures();
		
		List<CompetitionStage> stages = new ArrayList<CompetitionStage>(); // We will add just the first stage
		stages.add(this.competitionStage);
		
		this.hostCompetition = Environment.createCompetition(Leagues.getTeams("Bundesliga"), Resources.UNIVERSAL_SCORE_SAMPLES, RankingMode.UEFA, null);
		this.hostCompetition.setStages(stages);
		this.hostCompetition.setName("German knockout demo");
	}
	
	@Test
	public void playFixturesTest() throws InvalidTeamRankingException, DataException {
		while (!this.competitionStage.isOver()) {
			this.competitionStage.showFixtures(); // Monitoring
			this.competitionStage.playFixtures(this.hostCompetition);
			this.competitionStage.showResults(); // Monitoring
		}
		System.out.println();
		for (Grouping grouping:this.competitionStage.getGroupings()) {
			assertEquals(1, grouping.getWinners().size());
			System.out.println(grouping.getAggregateScore());
			System.out.println("Winners: " + grouping.getWinners().get(0).getName() + 
					(grouping.getTieBreaker() != null?" on " + grouping.getTieBreaker().getDescription():""));
			System.out.println();
		}
	}
}
