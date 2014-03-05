package com.simplefootie.gui;

import com.simplefootie.screens.Main;

/**
 * 
 * Used for handling navigation details between 'screens'. We use the concept of screens for navigation through the application as an attempt at having a technology
 * agnostic way of navigation, to be used reused (at least conceptually) among porting to different platforms. Well, so far this didn't seem to work too good with the
 * servlet/jsp/custom framework-based GUI, which is part of this project. Anyway, even so it is still a good enough abstraction for a console-based version.
 * 
 * @author Andreas Tasoulas
 *
 */
public enum Screens {
	
	MAIN, SELECT_TEAM;
	
	/**
	 * Mapping method between the user's menu selection and the next screen to navigate to
	 * 
	 * @param menuOption the value representing user's current option
	 * @param hostScreenClass Denotes the current screen
	 * @return the next screen, as this enumerator's value
	 */
	public static Screens getScreenFromMenuSelection(MenuOptions menuOption, Class hostScreenClass) {
		
		if (hostScreenClass.equals(Main.class)) {
		
			switch((Main.Options) menuOption) {
				case PLAY_FRIENDLY:
					return SELECT_TEAM;	
				case EXIT:
					return null;
			}
		}
		
		return null;
	}
}
