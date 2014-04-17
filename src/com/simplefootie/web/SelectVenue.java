package com.simplefootie.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.simplefootie.domain.Ground;
import com.simplefootie.domain.Match;

public class SelectVenue extends HttpServlet {
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		String groundSelection = request.getParameter("venueSelection");
		Match match = (Match) request.getSession().getAttribute("currentMatch");
		
		switch(groundSelection) {
		case "home":
			match.setVenue(Ground.HOME_GROUND);
			break;
		case "neutral":
			match.setVenue(Ground.NEUTRAL_GROUND);
			break;
		default:
			match.setVenue(Ground.HOME_GROUND); // This is the default anyway
			/*
			request.setAttribute("errorMessage", ErrorMessages.invalidInputValue);
			request.getRequestDispatcher(Navigation.ERROR_PAGE_DISPATCH).forward(request, response);
			return;
			*/
		}
		
		response.sendRedirect(Navigation.MATCH_PREVIEW_VIEW);
		// request.getRequestDispatcher(Navigation.MATCH_PREVIEW_DISPATCH).forward(request, response);
	}
}
