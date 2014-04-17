package com.simplefootie.domain;

public class League {
	
	private String name;
	private Integer leagueId;
	
	public League(Integer id, String name) {
		this.leagueId = id;
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public Integer getLeagueId() {
		return this.leagueId;
	}
}
