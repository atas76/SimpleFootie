package com.simplefootie.data;

import java.util.ArrayList;
import java.util.List;

import com.simplefootie.domain.Score;

public class ScoreSample {
	
	private List<Score> scores;
	
	public ScoreSample(List<Score> scores) {
		this.scores = scores;
	}
	
	public List<Score> getScores() {
		return this.scores;
	}
	
	public ScoreSample filterByCapacity(double capacityDivergence) {
		
		double minRange = 0.0;
		double maxRange = Double.MAX_VALUE;
		
		if (capacityDivergence >= 2.0) {
			minRange = 2.0;
			maxRange = Double.MAX_VALUE;
		} else if (capacityDivergence >= 1.0) {
			minRange = 1.0;
			maxRange = 2.0;
		} else if (capacityDivergence >= 0.5) {
			minRange = 0.5;
			maxRange = 1.0;
		} else {
			minRange = 0.0;
			maxRange = 0.5;
		}
		
		List<Score> filteredScores = new ArrayList<Score>();
		
		for (Score score:this.scores) {
			if ((minRange <= score.getCapacityRatio()) && (score.getCapacityRatio() <= maxRange)) {
				filteredScores.add(score);
			}
		}
		
		return new ScoreSample(filteredScores);
	}
}
