package com.simplefootie.web.views;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.simplefootie.domain.League;
import com.simplefootie.domain.Leagues;
import com.simplefootie.domain.Match;
import com.simplefootie.web.Navigation;
import com.simplefootie.web.Session;

public class SelectLeaguePage extends HttpServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		// Check preconditions
		
		if (request.getSession() == null || request.getSession().getAttribute(Session.USERNAME) == null) {
			response.sendRedirect(Navigation.LOGIN_PAGE);
		}
		
		if (request.getSession().getAttribute(Session.CURRENT_MATCH) != null) {
			Match currentMatch = (Match) request.getSession().getAttribute(Session.CURRENT_MATCH);
			if (currentMatch.hasCompleteDetails()) {
				response.sendRedirect(Navigation.MATCH_PREVIEW_PAGE);
			}
		}
		
		//
		
		List<League> leagues = Leagues.getLeagues();
		
		request.setAttribute("leagues", leagues);
		
		request.getRequestDispatcher(Navigation.SELECT_LEAGUE_VIEW).forward(request, response);
	}
}
