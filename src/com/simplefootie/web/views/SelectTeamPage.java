package com.simplefootie.web.views;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.simplefootie.domain.Leagues;
import com.simplefootie.domain.Match;
import com.simplefootie.domain.Team;
import com.simplefootie.web.Navigation;
import com.simplefootie.web.Session;

public class SelectTeamPage extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		// Check preconditions
		
		if (request.getSession() == null || request.getSession().getAttribute(Session.USERNAME) == null) {
			response.sendRedirect(Navigation.LOGIN_PAGE);
			return;
		}
		
		Match currentMatch = (Match) request.getSession().getAttribute(Session.CURRENT_MATCH);
		
		if (currentMatch != null) {
			if (currentMatch.hasCompleteDetails()) {
				response.sendRedirect(Navigation.MATCH_PREVIEW_PAGE);
				return;
			}
		}
		
		Integer leagueId = (Integer) request.getAttribute("leagueId");
		
		List<Team> teams = Leagues.getTeamsByLeagueId(leagueId);
		
		// Remove the already selected team (this must be the current match's home team)
		
		if (currentMatch != null && currentMatch.getHomeTeam() != null) {
		
			String homeTeamName = currentMatch.getHomeTeam().getName(); 
					
			Team alreadySelectedTeam = null;
			
			for (Team team:teams) {
				if (team.getName().equals(homeTeamName)) {
					alreadySelectedTeam = team;
					break;
				}
			}
			
			// We are only interested to check this on the GUI side for user's-experience sake. 
			// We don't care about the backend, so this check can be bypassed with a web proxy.
			if (alreadySelectedTeam != null) { // It happens that one of the teams to be displayed is already selected, so don't display it again
				teams.remove(alreadySelectedTeam); 
			}
		}
		
		request.setAttribute("teams", teams);
		
		request.getRequestDispatcher(Navigation.SELECT_TEAM_VIEW).forward(request, response);
	}
	
	/*
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		// Check preconditions
		
		if (request.getSession() == null || request.getSession().getAttribute(Session.USERNAME) == null) {
			response.sendRedirect(Navigation.LOGIN_PAGE);
			return;
		}
		
		if (request.getSession().getAttribute(Session.CURRENT_MATCH) != null) {
			Match currentMatch = (Match) request.getSession().getAttribute(Session.CURRENT_MATCH);
			if (currentMatch.hasCompleteDetails()) {
				response.sendRedirect(Navigation.MATCH_PREVIEW_PAGE);
				return;
			}
		}
		
		Integer leagueId = (Integer) request.getAttribute("leagueId");
		
		List<Team> teams = Leagues.getTeamsByLeagueId(leagueId);
		
		request.setAttribute("teams", teams);
		
		request.getRequestDispatcher(Navigation.SELECT_TEAM_VIEW).forward(request, response);
	}*/
}
