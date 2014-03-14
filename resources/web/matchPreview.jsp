<html>
	<head>
		<title>Match preview</title>
	</head>
	<body>
		${competition}
		<p/>
		${currentMatch.homeTeamName} - ${currentMatch.awayTeamName}
		<p/>
		Venue: ${currentMatch.homeTeamName} ground
		<!-- <form name="mainMenuFrm" action="./getNextPage" method="POST"> -->
		<form name="showScoreFrm" action="./calculateScore" method="POST">
			<input type="submit" name="calculateScoreSubmit" value="Play match"/>
		</form>
	</body>
</html>