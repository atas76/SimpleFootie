package com.simplefootie.web.views;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.simplefootie.domain.Match;
import com.simplefootie.web.Navigation;
import com.simplefootie.web.Session;

public class MatchPreviewPage extends HttpServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		if (request.getSession() == null || request.getSession().getAttribute(Session.USERNAME) == null) {
			response.sendRedirect(request.getContextPath() + Navigation.LOGIN_PAGE);
			return;
		}
		
		Match currentMatch = (Match) request.getSession().getAttribute(Session.CURRENT_MATCH);
		
		if (currentMatch == null || !currentMatch.hasCompleteDetails()) {
			response.sendRedirect(request.getContextPath() + Navigation.SELECT_LEAGUE_PAGE);
			return;
		}
		
		response.sendRedirect(Navigation.MATCH_PREVIEW_VIEW);
	}
}
