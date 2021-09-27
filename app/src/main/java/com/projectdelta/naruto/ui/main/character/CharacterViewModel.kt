package com.projectdelta.naruto.ui.main.character

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.projectdelta.naruto.data.model.entity.character.Character
import com.projectdelta.naruto.data.preference.PreferenceManager
import com.projectdelta.naruto.data.repository.CharacterRepository
import com.projectdelta.naruto.widgets.ExtendedNavigationView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


@HiltViewModel
class CharacterViewModel @Inject constructor(
	private val repository: CharacterRepository ,
	private val preferenceManager: PreferenceManager
):ViewModel() {

	private val currentDataPref = MutableLiveData( Triple(0 ,0 ,"") )
	init {
		getUpdatePrefDataSort()
		getFilterSort()
	}

	val data = currentDataPref.switchMap { (sort ,filters ,search) ->
		when (sort) {
			in 0 .. 99 -> { // alpha
				var query = Character.Companion.SortCharacter.BY_NAME_ASC
				if( sort%10 == ExtendedNavigationView.Item.MultiSort.SORT_DESC )
					query = Character.Companion.SortCharacter.BY_NAME_DESC
				characterDataPaged(query)
			}
			in 99..999 -> { // power
				var reverse = false
				if( sort%10 == ExtendedNavigationView.Item.MultiSort.SORT_DESC )
					reverse = true
				characterDataByPowerPaged(reverse)
			}
			else -> { // debut
				var query = Character.Companion.SortCharacter.BY_DEBUT_ASC
				if( sort%10 == ExtendedNavigationView.Item.MultiSort.SORT_DESC ){
					query = Character.Companion.SortCharacter.BY_DEBUT_DESC
				}
				characterDataPaged(query)
			}
		}.asLiveData().cachedIn(viewModelScope)
	}

	private var characterDataByPowerPagedData :  Flow<PagingData<Character>>? = null
	private var lastOrder : Boolean? = null
	fun characterDataByPowerPaged(reverse : Boolean):Flow<PagingData<Character>> {
		if( characterDataByPowerPagedData == null || (lastOrder != null && lastOrder != reverse))
			lastOrder = reverse
			characterDataByPowerPagedData = repository
				.getCharactersSortedByPowerPaged(reverse)
				.map { pagingData ->
					pagingData.map {
						it
					}
				}
				.cachedIn(viewModelScope)
		return characterDataByPowerPagedData!!
	}

	private var characterDataPaged : Flow<PagingData<Character>>? = null
	private var lastQuery : Character.Companion.SortCharacter?= null
	private fun characterDataPaged(sortParam : Character.Companion.SortCharacter): Flow<PagingData<Character>> {
		if( characterDataPaged == null || (lastQuery != null && lastQuery != sortParam) )
			lastQuery = sortParam
			characterDataPaged = repository
				.getCoreCharacters( sortParam )
				.map { pagingData ->
					pagingData.map {
						it
					}
				}
				.cachedIn(viewModelScope)
		return characterDataPaged!!
	}

	fun getUpdatePrefDataSort() {
		val cur = currentDataPref.value
		currentDataPref.value = when {
			// alpha
			preferenceManager.sortAlphabetically() != ExtendedNavigationView.Item.MultiSort.SORT_NONE -> {
				cur?.copy(first = 10 + preferenceManager.sortAlphabetically())
			}
			// power
			preferenceManager.sortPower() != ExtendedNavigationView.Item.MultiSort.SORT_NONE -> {
				cur?.copy(first = 100 + preferenceManager.sortPower())
			}
			// debut
			preferenceManager.sortDebut() != ExtendedNavigationView.Item.MultiSort.SORT_NONE -> {
				cur?.copy(first = 1000 + preferenceManager.sortDebut())
			}
			else -> throw IllegalStateException("WTF@ViewModel!")
		}
	}

	private fun getFilterSort() {

	}
}