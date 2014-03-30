package com.simplefootie.data;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.simplefootie.Resources;
import com.simplefootie.domain.Score;

public class ScoreSampleTest {
	
	private ScoreSample allScores;
	
	private List<Score> scores2;
	private List<Score> scores1;
	private List<Score> scoresHalf;
	private List<Score> scores0;
	
	@Before
	public void setUp() throws IOException {
		this.allScores = new ScoreSample(ScoresPersistence.read(Resources.getDataResource(Resources.TEST_SCORE_SAMPLES)));
	}
	
	@Test
	public void testFilterByCapacityRatio() {
		
		this.scores0 = this.allScores.filterByCapacity(0.25).getScores();
		this.scoresHalf = this.allScores.filterByCapacity(0.75).getScores();
		this.scores1 = this.allScores.filterByCapacity(1.5).getScores();
		this.scores2 = this.allScores.filterByCapacity(2.5).getScores();
		
		assertEquals(8, this.scores2.size());
		assertEquals(6, this.scores1.size());
		assertEquals(10, this.scoresHalf.size());
		assertEquals(7, this.scores0.size());
	}
}
