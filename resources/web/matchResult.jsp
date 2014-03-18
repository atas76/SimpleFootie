<%@ page import="com.simplefootie.domain.Match" %>
<%@ page import="com.simplefootie.domain.Ground" %>

<html>
	<head>
		<title>Match preview</title>
	</head>
	<body>
		${competition}
		<p/>
		${currentMatch.homeTeamName} - ${currentMatch.awayTeamName} ${currentMatch.homeTeamScore} - ${currentMatch.awayTeamScore}
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
		<form name="postMatchFrm" action="./postMatchNext" method="POST">
			<input type="submit" name="replayMatchSubmit" value="Replay match"/>
			<input type="submit" name="returnHomeSubmit" value="Return to main"/>
		</form>
	</body>
</html>