package com.simplefootie.web.views;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.simplefootie.web.Navigation;
import com.simplefootie.web.Session;

public class LoginPage extends HttpServlet {
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	/**
	 * Standard: Displays the login page
	 * 
	 * Error: If there is already an active user session, the user is redirected to MainPage
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (request.getSession() == null || request.getSession().getAttribute(Session.USERNAME) == null) {
			logger.info("Redirect to login page");
			response.sendRedirect(Navigation.LOGIN_VIEW);
		} else {
			logger.info("Redirect to main page");
			response.sendRedirect(Navigation.MAIN_VIEW);
		}
	}
}
