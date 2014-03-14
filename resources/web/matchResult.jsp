<html>
	<head>
		<title>Match preview</title>
	</head>
	<body>
		${competition}
		<p/>
		${currentMatch.homeTeamName} - ${currentMatch.awayTeamName} ${currentMatch.homeTeamScore} - ${currentMatch.awayTeamScore}
		<p/>
		Venue: ${currentMatch.homeTeamName} ground
		<!-- <form name="mainMenuFrm" action="./getNextPage" method="POST"> -->
		<form name="postMatchFrm" action="./postMatchNext" method="POST">
			<input type="submit" name="replayMatchSubmit" value="Replay match"/>
			<input type="submit" name="returnHomeSubmit" value="Return to main"/>
		</form>
	</body>
</html>