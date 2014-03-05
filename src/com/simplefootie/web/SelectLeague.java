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
		
		PrintWriter out = response.getWriter();
		
		String optionSrc = request.getParameter("optionSrc");
		String leagueSelected = request.getParameter("leagueSelection");
		
		logger.info("Option source: " + optionSrc);
		logger.info("League selected: " + leagueSelected);
		
		if (leagueSelected == null) { // Do it all over again, because the user is messing around (helped by the clunky at the moment user interface)
			
			List<String> availableLeagues = com.simplefootie.web.backend.SelectLeague.getDynamicDisplay();
			
			if (availableLeagues == null) { // Something went wrong with data retrieval
				request.setAttribute("errorMessage", ErrorMessages.errorRetrievingData);
				request.getRequestDispatcher(Navigation.ERROR_PAGE_DISPATCH).forward(request, response);
				return;
			}
			
			Map<String, String> options = new HashMap<String,String>();
			
			for (String league:availableLeagues) {
				options.put(league, league);
			}
			
			WebSelectionScreen selectionScreen = new WebSelectionScreen(options, "selectLeague", "leagueSelection", optionSrc);
			
			out.write(selectionScreen.generateHTML());
			return;
		}
			
		List<String> teams  = SelectTeam.getDynamicDisplay(leagueSelected);
			
		Map<String, String> options = new HashMap<String, String>();
			
		for (String team:teams) {
			
			if (optionSrc.equals("awayFriendly") && team.equals(currentMatch.getHomeTeamName())) { // Don't display an already selected team!
				continue;
			}
			
			options.put(team, team);
		}
			
		WebSelectionScreen selectionScreen = new WebSelectionScreen(options, "selectTeam", "teamSelection", optionSrc);
			
		out.write(selectionScreen.generateHTML());
	}
}
