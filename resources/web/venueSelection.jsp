<%@ page contentType="text/html; charset=UTF-8" %>

<html>

	<form name="venueSelectionFrm" method="POST" action="./selectVenue">
		<fieldset>
			<input type="radio" name="venueSelection" value="home">Home ground</input>
			<input type="radio" name="venueSelection" value="neutral">Neutral ground</input>
		</fieldset>
		<p/>
		<input type="submit" value="Select Venue">
	</form>