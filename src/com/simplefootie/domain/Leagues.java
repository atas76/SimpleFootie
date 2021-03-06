package com.simplefootie.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Used for retrieving league details. The data are loaded from the data source and accessed through the Environment class.
 * 
 * @author Andreas Tasoulas
 *
 */
public class Leagues {
	
	private static Logger logger = Logger.getLogger("domain.Leagues");
	
	public static List<League> getLeagues() {
		
		List<League> leagues = new ArrayList<League>();
		
		Document document = Environment.getDataDocument();
		
		NodeList leagueElements = document.getElementsByTagName("League");
		
		for (int i = 0; i < leagueElements.getLength(); i++) {
			
			Node leagueNode = leagueElements.item(i);
			
			leagues.add(new League(new Integer(leagueNode.getAttributes().getNamedItem("id").getNodeValue()), 
					leagueNode.getAttributes().getNamedItem("name").getNodeValue()));
		}
		
		return leagues;
	}
	
	public static League getLeagueById(Integer leagueId) {
		
		List<League> leagues = getLeagues();
		
		for (League league: leagues) {
			if (league.getLeagueId().equals(leagueId)) {
				return league;
			}
		}
		return null;
	}
	
	/**
	 * Getter of all teams' names irrespective of the league they belong to.
	 * 
	 * @return all the available national leagues' names as read from the data source
	 */
	public static List<String> getAllNames() {
		
		List<String> retVal = new ArrayList<String>();
		
		Document document = Environment.getDataDocument();
		
		NodeList leagueElements = document.getElementsByTagName("League");
		
		for (int i = 0; i < leagueElements.getLength(); i++) {
			
			Node leagueNode = leagueElements.item(i);
			
			retVal.add(leagueNode.getAttributes().getNamedItem("name").getNodeValue());
		}
		
		return retVal;
	}
	
	/**
	 * Get all team objects irrespective of the league they belong to.
	 * 
	 * @return All available teams from the data source.
	 */
	public static List<Team> getAllTeams(Document document) {
		
		List<Team> retVal = new ArrayList<Team>();
		
		NodeList leagueElements = document.getElementsByTagName("League");
		
		for (int i = 0; i < leagueElements.getLength(); i++) {
			
			Node leagueNode = leagueElements.item(i);
			
			NodeList teamList = leagueNode.getChildNodes();
			
			for (int j = 0; j < teamList.getLength(); j++) {
				
				Node teamNode = teamList.item(j);
				
				/*
				assert(teamNode != null);
				assert(teamNode.getAttributes() != null);
				assert(teamNode.getAttributes().getNamedItem("reputation") != null);
				*/
				
				if (teamNode.getNodeName().equals("Team")) {
				
					String currentReputation = teamNode.getAttributes().getNamedItem("reputation").getNodeValue();
					
					// System.out.println(currentReputation);
			
					if (currentReputation.length() > 0) {
				
						String currentTeamName = teamNode.getAttributes().getNamedItem("name").getNodeValue();
						String shortName = teamNode.getAttributes().getNamedItem("shortName").getNodeValue();
						
						logger.config("Loading team: " + currentTeamName + " with short name: " + shortName);
				
						retVal.add(new Team(currentTeamName, shortName, new Integer(currentReputation).intValue()));
					}
				}
			}
		}
		
		return retVal;
		
	}
	
	public static List<Team> getTeamsByLeagueId(Integer leagueId) {
		
		List<League> leagues = Leagues.getLeagues();
		String leagueName = "";
		
		for (League league:leagues) {
			if (league.getLeagueId().equals(leagueId)) {
				leagueName = league.getName();
				break;
			}
		}
		
		return Leagues.getTeams(leagueName);
	}
	
	/**
	 * Get teams of a specified league from the data source.
	 * 
	 * @param leagueName the specified league
	 * @return The list of available teams in the specified league
	 */
	public static List<Team> getTeams(String leagueName) {
		
		List<Team> retVal = new ArrayList<Team>();
		
		Document document = Environment.getDataDocument();
		
		NodeList leagueElements = document.getElementsByTagName("League");
		
		for (int i = 0; i < leagueElements.getLength(); i++) {
			
			Node leagueNode = leagueElements.item(i);
			String currentLeagueName = leagueNode.getAttributes().getNamedItem("name").getNodeValue();
			
			if (currentLeagueName.equals(leagueName)) {
				
				NodeList teamList = leagueNode.getChildNodes();
				
				for (int j = 0; j < teamList.getLength(); j++) {
					
					Node teamNode = teamList.item(j);
					
					if (teamNode.getNodeName().equals("Team")) {
					
						// assert(teamNode.getAttributes() != null);
						// assert(teamNode.getAttributes().getNamedItem("reputation") != null);
					
						String currentReputation = teamNode.getAttributes().getNamedItem("reputation").getNodeValue();
						
						// System.out.println(currentReputation);
					
						if (currentReputation.length() > 0) {
						
							String currentTeamName = teamNode.getAttributes().getNamedItem("name").getNodeValue();
							String shortName = teamNode.getAttributes().getNamedItem("shortName").getNodeValue();
							String teamId = teamNode.getAttributes().getNamedItem("id").getNodeValue();
							
							logger.config("Loading team: " + currentTeamName + " with short name: " + shortName);
					
							retVal.add(new Team(new Integer(teamId), currentTeamName, shortName, new Integer(currentReputation).intValue()));
						}
					}
				}
				break;
			}
			
		}
		return retVal;
	}
}
