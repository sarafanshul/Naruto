package com.projectdelta.naruto.util

@Suppress("unused")
object Constants {

	//Global Retrofit options
	const val CONNECTION_TIMEOUT = 10L
	const val READ_TIMEOUT = 10L
	const val WRITE_TIMEOUT = 10L

	// Drawables
	// threshold for when contents of collapsing toolbar is hidden
	const val COLLAPSING_TOOLBAR_VISIBILITY_THRESHOLD = -150
	const val CLICK_THRESHOLD = 150L // a click is considered 150ms or less
	const val CLICK_COLOR_CHANGE_TIME = 250L

	//Transition names
	const val TRANSITION_CHARACTER = "character_transition_"
	const val TRANSITION_LOCATION = "location_transition_"
	const val TRANSITION_EPISODE = "episode_transition_"

}
