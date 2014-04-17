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
		
		// System.out.println("User choice: " + option);
		
		switch(option) {
		case "friendly":
			
			/*
			PrintWriter out = response.getWriter();
			
			Map<String, String> options = new HashMap<String,String>();
			
			for (String league:availableLeagues) {
				options.put(league, league);
			}
			
			WebSelectionScreen selectionScreen = new WebSelectionScreen(options, "selectLeague", "leagueSelection", "homeFriendly");
			
			out.write(selectionScreen.generateHTML());
			*/
			
			response.sendRedirect(request.getContextPath() + Navigation.SELECT_LEAGUE_PAGE);
			
			break;
		case "logout":
			request.getSession().invalidate();
			response.sendRedirect(Navigation.LOGIN_VIEW);
			break;
		default:
			logger.info("Invalid input option");
			response.sendRedirect(request.getContextPath() + Navigation.MAIN_PAGE);
		}
	}
}
