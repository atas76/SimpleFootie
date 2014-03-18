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

import com.simplefootie.Resources;
import com.simplefootie.domain.Environment;
import com.simplefootie.web.backend.SelectLeague;
import com.simplefootie.web.framework.WebSelectionScreen;


/**
 * 
 * @author Andreas Tasoulas
 *
 */
public class Main extends HttpServlet {
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		// System.out.println("Test: Main");
		
		try {
			
			// runs as a web application
			Resources.setResourcesDataRoot("../webapps/" + getServletContext().getContextPath() + "/WEB-INF/data");
			
			Environment.load(Resources.getDataResource(Resources.DB));
			
		} catch (Exception ex) {
			logger.severe("Problem loading main data");
			ex.printStackTrace();
			request.setAttribute("errorMessage", ErrorMessages.errorRetrievingData);
			request.getRequestDispatcher(Navigation.ERROR_PAGE_DISPATCH).forward(request, response);
			return;
		}
		
		String option = request.getParameter("main");
		
		logger.info("User choice: " + option);
		
		// System.out.println("User choice: " + option);
		
		String nextPageName = Pages.get("main", option);
		
		if (nextPageName == null) {
			logger.info("Invalid input option: null");
			response.sendRedirect(Navigation.MAIN_REDIRECT);
			return;
		}
		
		switch(nextPageName) {
		case "SelectLeague":
			if (request.getSession() == null || request.getSession().getAttribute("username") == null) {
				logger.info("No user in session");
				response.sendRedirect(Navigation.ROOT_REDIRECT);
				return;
			}
			
			if (request.getSession().getAttribute("currentMatch") != null) {
				logger.info("Match already loaded");
				response.sendRedirect(Navigation.MATCH_PREVIEW_REDIRECT);
				return;
				// req.getRequestDispatcher("/matchPreview.jsp").forward(req, resp);
			}
			
			List<String> availableLeagues = SelectLeague.getDynamicDisplay();
			
			if (availableLeagues == null) {
				request.setAttribute("errorMessage", ErrorMessages.errorRetrievingData);
				request.getRequestDispatcher(Navigation.ERROR_PAGE_DISPATCH).forward(request, response);
				return;
			}
			
			PrintWriter out = response.getWriter();
			
			Map<String, String> options = new HashMap<String,String>();
			
			for (String league:availableLeagues) {
				options.put(league, league);
			}
			
			WebSelectionScreen selectionScreen = new WebSelectionScreen(options, "selectLeague", "leagueSelection", "homeFriendly");
			
			out.write(selectionScreen.generateHTML());
			
			break;
		case "Logout":
			request.getSession().invalidate();
			response.sendRedirect(Navigation.ROOT_REDIRECT);
			break;
		default:
			logger.info("Invalid input option");
			response.sendRedirect(Navigation.ROOT_REDIRECT);
		}
	}
}
