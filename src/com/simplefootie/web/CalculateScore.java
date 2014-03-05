package com.simplefootie.web;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.simplefootie.data.DataException;
import com.simplefootie.domain.Environment;
import com.simplefootie.domain.Match;
import com.simplefootie.domain.exceptions.InvalidTeamRankingException;


/**
 * 
 * @author Andreas Tasoulas
 *
 */
public class CalculateScore extends HttpServlet {
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		Match currentMatch = (Match) request.getSession().getAttribute("currentMatch");
		
		if (currentMatch == null || !currentMatch.hasCompleteDetails()) {
			logger.severe("Current match object not found or without complete details. Reset and start all over again");
			request.getSession().removeAttribute("currentMatch"); // Just in case it is in an unstable 'state'
			response.sendRedirect(Navigation.MAIN_REDIRECT);
			return;
		}
		
		try {
			currentMatch.calculateResult(Environment.getCompetition(Environment.Competitions.FRIENDLY));
		} catch (InvalidTeamRankingException iteEx) {
			// Send to error page
			request.setAttribute("errorMessage", "Weird error evaluating team ranking");
			request.getRequestDispatcher(Navigation.ERROR_PAGE_DISPATCH).forward(request, response);
			return;
		} catch (DataException dataEx) {
			// Send to error page
			request.setAttribute("errorMessage", "Error retrieving statistical data");
			request.getRequestDispatcher(Navigation.ERROR_PAGE_DISPATCH).forward(request, response);
			return;
		}
		
		request.getRequestDispatcher(Navigation.MATCH_RESULT_DISPATCH).forward(request, response);
	}
}
