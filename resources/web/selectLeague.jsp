<%@ page contentType="text/html; charset=UTF-8" %>

<%@ page import="com.simplefootie.domain.League" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>

<html>
	<head>
		<title>Select league</title>
	</head>
	<body>
		<form name="selectLeagueFrm" method="POST" action="./selectLeague">
			<fieldset>
				<%
					List<League> leagues = (ArrayList<League>) request.getAttribute("leagues");
				
					for (League league: leagues) {
					
				%>
						<input type="radio" name="league" value="<%=league.getLeagueId()%>"><%=league.getName()%></input>
						<p/>
				<%
					}
				%>
		
				<input type="submit" name="leagueSubmit" value="Select League"/>
			</fieldset>
		</form>
	</body>
</html>