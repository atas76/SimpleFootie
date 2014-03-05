package com.simplefootie.domain;

/**
 * Used in match calculation for whether to give advantage to the home team or not.
 * 
 * @author Andreas Tasoulas
 *
 */
public enum Ground {
	
	HOME_GROUND("Home team ground"),
	NEUTRAL_GROUND("Neutral ground");
	
	private String name;
	
	private Ground(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
}
