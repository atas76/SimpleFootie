package com.simplefootie.web;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author Andreas Tasoulas
 *
 */
public class SignUpIn extends HttpServlet {
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		logger.info("Entering SignUpIn servlet");
		logger.info("Parameter: username => " + request.getParameter("username"));
		
		if (request.getSession() != null && request.getSession().getAttribute("username") != null) {
			logger.info("You cannot sign in on top of an existing user session. Please logout first");
			response.sendRedirect(Navigation.MAIN_REDIRECT);
			return;
		}
		
		String username = request.getParameter("username");
		
		if (!validateUsername(username)) {
			logger.info("Invalid username");
			response.sendRedirect(Navigation.ROOT_REDIRECT);
			return;
		}
		
		request.getSession(true).setAttribute("username", username);
		
		response.sendRedirect(Navigation.MAIN_REDIRECT);
	}
	
	/**
	 * A simple method for basic username validation: a non-empty string with alphanumeric characters beginning with an alphabetic character.
	 * 
	 * @param username The username string to validate
	 * @return true if username passes the validation, false otherwise
	 */
	private boolean validateUsername(String username) {
		return username.matches("^[A-Za-z]+[A-Za-z0-9]*");
	}
}
