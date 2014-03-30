package com.simplefootie.data;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.simplefootie.Resources;
import com.simplefootie.domain.Score;

public class ScorePersistenceTest {
	
	private static String scoreFilePath;
	private static List<Score> scores;
	
	@Before
	public void setUp() {
		scoreFilePath = Resources.getDataResource(Resources.UNIVERSAL_SCORE_SAMPLES);
	}
	
	@Test
	public void testRead() throws IOException {
		
		scores = ScoresPersistence.read(scoreFilePath);
		
		assertEquals(447, scores.size());
		
		Score score = scores.get(262);
		assertEquals("{6,29,0,0}", score.toString());
		
		score = scores.get(277);
		assertEquals("{4,11,2,2}", score.toString());
	}
}
