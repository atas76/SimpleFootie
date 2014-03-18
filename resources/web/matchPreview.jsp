<%@ page import="com.simplefootie.domain.Match" %>
<%@ page import="com.simplefootie.domain.Ground" %>

<html>
	<head>
		<title>Match preview</title>
	</head>
	<body>
		${competition}
		<p/>
		${currentMatch.homeTeamName} - ${currentMatch.awayTeamName}
		<p/>
		
		<%
		
			Match currentMatch = (Match) session.getAttribute("currentMatch");
		
			if (currentMatch.getVenue() == null || currentMatch.getVenue().equals(Ground.HOME_GROUND)) {
			
		%>
			Venue: ${currentMatch.homeTeamName} ground
			
		<%
			} else {
		%>
		
			Venue: Neutral ground
		
		<%
		}
		%>
		
		<!-- <form name="mainMenuFrm" action="./getNextPage" method="POST"> -->
		<form name="showScoreFrm" action="./calculateScore" method="POST">
			<input type="submit" name="calculateScoreSubmit" value="Play match"/>
		</form>
	</body>
</html>