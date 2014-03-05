package com.simplefootie.web.backend;

import java.util.List;

import com.simplefootie.domain.Leagues;


public class SelectLeague {
	/**
	 * Gets the data for the league selection view.
	 * 
	 * @return all available leagues
	 */
	public static List<String> getDynamicDisplay() {
		return Leagues.getAllNames();
	}
}
