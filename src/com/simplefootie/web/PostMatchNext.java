package com.simplefootie.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author Andreas Tasoulas
 *
 */
public class PostMatchNext extends HttpServlet {
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		if (request.getParameter("replayMatchSubmit") != null) {
			request.getRequestDispatcher(Navigation.CALCULATE_SCORE_DISPATCH).forward(request, response);
		} else if (request.getParameter("returnHomeSubmit") != null) {
			request.getSession().removeAttribute("currentMatch");
			response.sendRedirect(Navigation.MAIN_REDIRECT);
		}
	}
}
