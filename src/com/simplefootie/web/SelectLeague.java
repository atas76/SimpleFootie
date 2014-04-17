package com.simplefootie.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.simplefootie.domain.Match;
import com.simplefootie.web.backend.SelectTeam;
import com.simplefootie.web.framework.WebSelectionScreen;

/**
 * 
 * @author Andreas Tasoulas
 *
 */
public class SelectLeague extends HttpServlet {
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		/*
		if (request.getSession() == null || request.getSession().getAttribute("username") == null) {
			logger.info("No user in session");
			response.sendRedirect(Navigation.ROOT_REDIRECT);
			return;
		}
		
		Match currentMatch = (Match) request.getSession().getAttribute("currentMatch");
		
		if (currentMatch != null && currentMatch.hasCompleteDetails()) {
			logger.info("Match already loaded");
			response.sendRedirect(Navigation.MATCH_PREVIEW_REDIRECT);
			return;
		}
		*/
		
		// PrintWriter out = response.getWriter();
		
		/*
		String optionSrc = request.getParameter("optionSrc");
		String leagueSelected = request.getParameter("leagueSelection");
		
		logger.info("Option source: " + optionSrc);
		logger.info("League selected: " + leagueSelected);
		*/
		
		String leagueSelected = request.getParameter("league");
		
		logger.info("League Selection Controller");
		logger.info("League selected => " + leagueSelected);
		
		if (leagueSelected == null) { 
			response.sendRedirect(request.getContextPath() + Navigation.SELECT_LEAGUE_PAGE);
			return;
		}
		
		try {
		
			Integer leagueId = new Integer(leagueSelected);
			
			request.setAttribute("leagueId", leagueId);
			
			request.getRequestDispatcher(Navigation.SELECT_TEAM_PAGE).forward(request, response);
			
		} catch (NumberFormatException nfex) {
			response.sendRedirect(request.getContextPath() + Navigation.SELECT_LEAGUE_PAGE);
			return;
		}
	}
}
