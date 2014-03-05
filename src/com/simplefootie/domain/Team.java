package com.simplefootie.domain;

/**
 * Team object data structure.
 * 
 * @author Andreas Tasoulas
 *
 */
public class Team implements Comparable {
	
	private String name;
	private String shortName;
	private int reputation;
	
	public Team(String name, String shortName, int reputation) {
		this.name = name;
		this.reputation = reputation;
		this.shortName = shortName;
	}
	
	public Team(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getShortName() {
		return this.shortName;
	}
	
	public int compareTo(Object teamObject) {
		
		Team team = (Team) teamObject;
		
		return (team.reputation - this.reputation);
	}
}
