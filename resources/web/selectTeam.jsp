<%@ page contentType="text/html; charset=UTF-8" %>

<%@ page import="com.simplefootie.domain.Team" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>

<html>
	<head>
		<title>Select team</title>
	</head>
	<body>
		<form name="selectTeamFrm" method="POST" action="./selectTeam">
			<fieldset>
			
				<%
					List<Team> teams = (ArrayList<Team>) request.getAttribute("teams");
				
					for (Team team: teams) {
					
				%>
						<input type="radio" name="team" value="<%=team.getTeamId()%>"><%=team.getName()%></input>
						<p/>
				<%
					}
				%>
		
				<input type="submit" name="teamSubmit" value="Select Team"/>
			</fieldset>
		</form>
	</body>
</html>