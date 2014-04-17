package com.simplefootie.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Teams {
	
	private static Logger logger = Logger.getLogger("Teams");
	
	public static List<Team> getTeams() {
		
		logger.setLevel(Level.OFF);
		
		logger.info("getTeams()");
		
		List<Team> teams = new ArrayList<Team>();
		
		Document document = Environment.getDataDocument();
		
		NodeList teamElements = document.getElementsByTagName("Team");
		
		logger.info("Teams found: " + teamElements.getLength());
		
		for (int i = 0; i < teamElements.getLength(); i++) {
			
			Node teamNode = teamElements.item(i);
			
			logger.info("Processing team: " + teamNode.getAttributes().getNamedItem("name").getNodeValue());
			
			String currentReputation = teamNode.getAttributes().getNamedItem("reputation").getNodeValue();
			
			if (currentReputation.length() > 0) { // Some extra under-the-hood filtering
				teams.add(new Team(
						new Integer(teamNode.getAttributes().getNamedItem("id").getNodeValue()), 
						teamNode.getAttributes().getNamedItem("name").getNodeValue(),
						teamNode.getAttributes().getNamedItem("shortName").getNodeValue(),
						new Integer(teamNode.getAttributes().getNamedItem("reputation").getNodeValue())
						)
					);
			}
		}
		
		logger.info("Teams size: " + teams.size());
		
		return teams;
	}
	
	public static Team getTeamById(Integer id) {
		
		logger.info("getTeamById()");
	
		List<Team> teams = Teams.getTeams();
		
		logger.info("Number of teams: " + teams.size());
		
		for (Team team:teams) {
			if (team.getTeamId().equals(id)) {
				return team;
			}
		}
		return null;
	}
}
