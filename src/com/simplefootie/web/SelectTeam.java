package com.simplefootie.web;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.simplefootie.domain.Match;
import com.simplefootie.domain.Team;
import com.simplefootie.domain.Teams;
import com.simplefootie.domain.exceptions.InvalidMatchMutationException;


/**
 * 
 * @author Andreas Tasoulas
 *
 */
public class SelectTeam extends HttpServlet {
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		String teamSelected = request.getParameter("team");
		
		if (teamSelected == null) {
			logger.warning("No team parameter found");
			request.getRequestDispatcher(Navigation.SELECT_TEAM_PAGE).forward(request, response);
			return;
		}
		
		try  {
			
			Integer teamId = new Integer(teamSelected);
			
			logger.info("Team id: " + teamId);
			
			Team team = Teams.getTeamById(teamId);
			
			if (team == null) {
				logger.info("No team found");
				request.getRequestDispatcher(Navigation.SELECT_TEAM_PAGE).forward(request, response);
				return;
			}
			
			logger.info("Team name: " + team.getName());
			
			Match currentMatch = (Match) request.getSession().getAttribute(Session.CURRENT_MATCH);
			
			if (currentMatch == null) {
				currentMatch = new Match();
				request.getSession().setAttribute(Session.CURRENT_MATCH, currentMatch);
			}
			
			currentMatch.setNextTeam(team);
			
			if (currentMatch.hasCompleteDetails()) {
				response.sendRedirect(request.getContextPath() + Navigation.VENUE_SELECTION_PAGE);
			} else {
				logger.info("Match details not complete yet");
				response.sendRedirect(request.getContextPath() + Navigation.SELECT_LEAGUE_PAGE);
			}
			
		} catch (NumberFormatException nfex) {
			request.getRequestDispatcher(Navigation.SELECT_TEAM_PAGE).forward(request, response);
		} catch (InvalidMatchMutationException immex) {
			response.sendRedirect(request.getContextPath() + Navigation.MATCH_PREVIEW_PAGE);
		}
	}
}
