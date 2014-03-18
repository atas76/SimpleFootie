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

import com.simplefootie.domain.Environment.Competitions;
import com.simplefootie.domain.Match;
import com.simplefootie.web.backend.SelectLeague;
import com.simplefootie.web.framework.WebSelectionScreen;


/**
 * 
 * @author Andreas Tasoulas
 *
 */
public class SelectTeam extends HttpServlet {
	
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
		String teamSelected = request.getParameter("teamSelection");
		
		Map<String, String> options = new HashMap<String,String>();
		
		if (teamSelected == null) { // Start all over again with league selection. Not the optimal solution, but the error case is not usual either
			
			List<String> availableLeagues = com.simplefootie.web.backend.SelectLeague.getDynamicDisplay();
			
			// Map<String, String> options = new HashMap<String,String>();
			
			for (String league:availableLeagues) {
				options.put(league, league);
			}
			
			WebSelectionScreen selectionScreen = new WebSelectionScreen(options, "selectLeague", "leagueSelection", optionSrc);
			
			out.write(selectionScreen.generateHTML());
			return;
		}
		
		// Give back to the domain model and start the workflow all over again
		
		if ("homeFriendly".equals(optionSrc)) {
			
			currentMatch = new Match();
			
			currentMatch.setLabel(Competitions.FRIENDLY);
			
			// We assume that a new match object should be created with a new home team selection. Otherwise we will refer to it as an existing object.
			request.getSession().setAttribute("currentMatch", currentMatch);
			
			currentMatch.setHomeTeam(teamSelected);
			
			List<String> availableLeagues = SelectLeague.getDynamicDisplay();
			
			for (String league:availableLeagues) {
				options.put(league, league);
			}
			
			WebSelectionScreen selectionScreen = new WebSelectionScreen(options, "selectLeague", "leagueSelection", "awayFriendly");
			
			out.write(selectionScreen.generateHTML());
			
			// out.print(((Match) req.getSession().getAttribute("currentMatch")).getHomeTeamName());
		}
		
		if ("awayFriendly".equals(optionSrc)) {
		
			// Match currentMatch = (Match) request.getSession().getAttribute("currentMatch");
			
			if (teamSelected.equals(currentMatch.getHomeTeamName())) {
			
				logger.info("A team cannot play against itself");
				
				// We reset the whole thing, as this is not a normal situation anyway. 
				// The GUI itself will prevent the same team selected as its opponent.
				request.getSession().invalidate();
				response.sendRedirect(Navigation.ROOT_REDIRECT);
				return;
				
			} else {
				currentMatch.setAwayTeam(teamSelected);
			}
			
			if (currentMatch == null || !currentMatch.hasCompleteDetails()) {
				logger.severe("Postcondition of match having complete details violated. Reset and start all over again");
				request.getSession().removeAttribute("currentMatch"); // Just in case it is in an unstable 'state'
				response.sendRedirect(Navigation.MAIN_REDIRECT);
				return;
			}
			
			String competition = "Friendly match";
			
			request.setAttribute("competition", competition);
			
			options = new HashMap<String,String>();
			options.put("home", "Home ground");
			options.put("neutral", "Neutral ground");
			
			WebSelectionScreen selectionScreen = new WebSelectionScreen(options, "selectGround", "groundSelection", "groundSelection");
			out.write(selectionScreen.generateHTML());
			return;
			
			// request.getRequestDispatcher(Navigation.MATCH_PREVIEW_DISPATCH).forward(request, response);
		}
	}
}
