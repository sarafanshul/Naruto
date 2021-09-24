package com.projectdelta.naruto.data.preference

import android.content.Context
import android.content.SharedPreferences
import com.projectdelta.naruto.data.preference.PreferenceKeys as Keys
import com.projectdelta.naruto.widgets.ExtendedNavigationView.Item

class PreferenceManager(
	private val context: Context,
	private val preferences: SharedPreferences
) {

	// gender filters
	fun filterMale() = preferences.getInt(Keys.FILTER_MALE , Item.TriStateGroup.State.IGNORE.value)
	fun setFilterMale( newValue: Int ) = preferences.edit().putInt(Keys.FILTER_MALE ,newValue).apply()

	fun filterFemale() = preferences.getInt(Keys.FILTER_FEMALE , Item.TriStateGroup.State.IGNORE.value)
	fun setFilterFemale( newValue: Int ) = preferences.edit().putInt(Keys.FILTER_FEMALE ,newValue).apply()

	// orochimaru
	fun filterBi() = preferences.getInt(Keys.FILTER_BI , Item.TriStateGroup.State.IGNORE.value)
	fun setFilterBi( newValue: Int ) = preferences.edit().putInt(Keys.FILTER_BI ,newValue).apply()

	// status filters
	fun filterAlive() = preferences.getInt(Keys.FILTER_ALIVE , Item.TriStateGroup.State.IGNORE.value)
	fun setFilterAlive( newValue: Int ) = preferences.edit().putInt(Keys.FILTER_ALIVE ,newValue).apply()

	fun filterDead() = preferences.getInt(Keys.FILTER_DEAD , Item.TriStateGroup.State.IGNORE.value)
	fun setFilterDead( newValue: Int ) = preferences.edit().putInt(Keys.FILTER_DEAD ,newValue).apply()


	fun resetFilters() {
		preferences.edit().putInt(Keys.FILTER_MALE ,Item.TriStateGroup.State.IGNORE.value).apply()
		preferences.edit().putInt(Keys.FILTER_FEMALE ,Item.TriStateGroup.State.IGNORE.value).apply()
		preferences.edit().putInt(Keys.FILTER_BI ,Item.TriStateGroup.State.IGNORE.value).apply()
		preferences.edit().putInt(Keys.FILTER_ALIVE ,Item.TriStateGroup.State.IGNORE.value).apply()
		preferences.edit().putInt(Keys.FILTER_DEAD ,Item.TriStateGroup.State.IGNORE.value).apply()
	}

	fun sortAlphabetically() = preferences.getInt(Keys.SORT_ALPHA ,Item.MultiSort.SORT_NONE)
	fun setSortAlphabetically( newValue: Int ) = preferences.edit().putInt(Keys.SORT_ALPHA ,newValue).apply()

	fun sortPower() = preferences.getInt(Keys.SORT_POWER ,Item.MultiSort.SORT_DESC)
	fun setSortPower( newValue: Int ) = preferences.edit().putInt(Keys.SORT_POWER ,newValue).apply()

	fun sortDebut() = preferences.getInt(Keys.SORT_DEBUT ,Item.MultiSort.SORT_NONE)
	fun setSortDebut( newValue: Int ) = preferences.edit().putInt(Keys.SORT_DEBUT ,newValue).apply()

	fun resetSorting(){
		preferences.edit().putInt(Keys.SORT_ALPHA ,Item.MultiSort.SORT_NONE).apply()
		preferences.edit().putInt(Keys.SORT_POWER ,Item.MultiSort.SORT_NONE).apply()
		preferences.edit().putInt(Keys.SORT_DEBUT ,Item.MultiSort.SORT_NONE).apply()
	}

}