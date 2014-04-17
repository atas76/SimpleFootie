package com.simplefootie.web.views;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.simplefootie.web.Navigation;
import com.simplefootie.web.Session;

public class MainPage extends HttpServlet {
	
	/**
	 * Standard: Displays the main page. 
	 * 
	 * The functionality is exactly the same as that of the login view servlet, except that the default execution path is reversed.
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (request.getSession() == null || request.getSession().getAttribute(Session.USERNAME) == null) {
			response.sendRedirect(Navigation.LOGIN_VIEW);
		} else {
			response.sendRedirect(Navigation.MAIN_VIEW);
		}
	}
}
