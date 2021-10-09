package com.projectdelta.naruto.ui.main.character.list

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.projectdelta.naruto.data.model.entity.character.Character
import com.projectdelta.naruto.data.preference.PreferenceManager
import com.projectdelta.naruto.data.repository.CharacterRepository
import com.projectdelta.naruto.util.DataPrefBus
import com.projectdelta.naruto.widgets.ExtendedNavigationView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class CharacterViewModel @Inject constructor(
	private val repository: CharacterRepository ,
	private val preferenceManager: PreferenceManager
):ViewModel() {

	private val currentDataPref = MutableLiveData( DataPrefBus(0 ,{true} ,"") )

	init {
		getUpdatePrefDataSort()
		triggerFilters()
	}

	@ExperimentalCoroutinesApi
	var data = currentDataPref.switchMap { (sort ,filters ,query) ->
		if( query.length > 2 ){
			characterLikePage(query)
				.cachedIn(viewModelScope).asLiveData()
		}
		else {
			when (sort) {
				in 0..99 -> { // alpha
					var sortParam = Character.Companion.SortCharacter.BY_NAME_ASC
					if (sort % 10 == ExtendedNavigationView.Item.MultiSort.SORT_DESC)
						sortParam = Character.Companion.SortCharacter.BY_NAME_DESC
					characterDataPaged(sortParam, filters)
				}
				in 99..999 -> { // power
					var reverse = false
					if (sort % 10 == ExtendedNavigationView.Item.MultiSort.SORT_DESC)
						reverse = true
					characterDataByPowerPaged(reverse, filters)
				}
				else -> { // debut
					var sortParam = Character.Companion.SortCharacter.BY_DEBUT_ASC
					if (sort % 10 == ExtendedNavigationView.Item.MultiSort.SORT_DESC) {
						sortParam = Character.Companion.SortCharacter.BY_DEBUT_DESC
					}
					characterDataPaged(sortParam, filters)
				}

			}.cachedIn(viewModelScope).asLiveData()
		}
	}

	fun characterDataByPowerPaged(
		reverse : Boolean ,
		filters : suspend (Character) -> Boolean
	):Flow<PagingData<Character>> {
		return repository
			.getCharactersSortedByPowerPaged(reverse ,filters)
			.map { pagingData ->
				pagingData.map {
					it
				}
			}
			.cachedIn(viewModelScope)
	}

	private fun characterDataPaged(
		sortParam : Character.Companion.SortCharacter,
		filters : suspend (Character) -> Boolean
	): Flow<PagingData<Character>> {
		return repository
			.getCoreCharacters( sortParam , filters)
			.map { pagingData ->
				pagingData.map {
					it
				}
			}
			.cachedIn(viewModelScope)
	}

	private fun characterLikePage(
		name : String ,
		sortParam: Character.Companion.SortCharacter = Character.Companion.SortCharacter.NA ,
		filters: suspend (Character) -> Boolean = {true}
	) : Flow<PagingData<Character>> {
		return repository
			.getCharacterLikePaged(name ,sortParam ,filters)
			.map { paginData ->
				paginData.map {
					it
				}
			}
			.cachedIn(viewModelScope)
	}

	fun getUpdatePrefDataSort() {
		val cur = currentDataPref.value
		cur?.sort = when {
			// alpha
			preferenceManager.sortAlphabetically() != ExtendedNavigationView.Item.MultiSort.SORT_NONE -> {
				10 + preferenceManager.sortAlphabetically()
			}
			// power
			preferenceManager.sortPower() != ExtendedNavigationView.Item.MultiSort.SORT_NONE -> {
				100 + preferenceManager.sortPower()
			}
			// debut
			preferenceManager.sortDebut() != ExtendedNavigationView.Item.MultiSort.SORT_NONE -> {
				1000 + preferenceManager.sortDebut()
			}
			else -> throw IllegalStateException("WTF@ViewModel!")
		}
		currentDataPref.value = cur
	}

	// naive function to trigger change in filters
	fun triggerFilters(){
		 val filters : suspend (it : Character) -> Boolean = {
			when(preferenceManager.filterAlive()){
				ExtendedNavigationView.Item.TriStateGroup.State.INCLUDE.value -> {
					it.personal?.status == Character.Companion.CharacterStatus.ALIVE.value
				}
				ExtendedNavigationView.Item.TriStateGroup.State.EXCLUDE.value -> {
					it.personal?.status != Character.Companion.CharacterStatus.ALIVE.value
				}
				else -> true
			} && when( preferenceManager.filterFemale() ){
				ExtendedNavigationView.Item.TriStateGroup.State.INCLUDE.value -> {
					it.personal?.sex == Character.Companion.CharacterSex.FEMALE.value
				}
				ExtendedNavigationView.Item.TriStateGroup.State.EXCLUDE.value -> {
					it.personal?.sex != Character.Companion.CharacterSex.FEMALE.value
				}
				else -> true
			}
		}
		val cur = currentDataPref.value
		cur?.filters = filters
		currentDataPref.value = cur
	}

	fun updateQuery( query : String ){
		if( (query.length <= 2 && query != "") || currentDataPref.value?.query == query) // not empty for resetting
			return
		val cur = currentDataPref.value
		cur?.query = query
		currentDataPref.value = cur
		Timber.d("new query = $query")
	}
}