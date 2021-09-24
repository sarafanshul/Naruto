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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

/**
 * - 25-09 01:33 - this is an bare bone approach for sorting and filtering ,
 * 	DO NOT COPY , it'll be updated later , a way better version
 */
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
				var query = "_id"
				if( sort%10 == ExtendedNavigationView.Item.MultiSort.SORT_DESC )
					query += ",desc"
				characterDataPaged(query)
			}
			in 99..999 -> { // power
//				if( sort%10 == ExtendedNavigationView.Item.MultiSort.SORT_DESC )
				characterDataByPowerPaged()
			}
			else -> { // debut
				var q1 = "debut.anime.name,asc"
				var q2 = "debut.anime.episode,asc"
				if( sort%10 == ExtendedNavigationView.Item.MultiSort.SORT_DESC ){
					q1 = "debut.anime.name,desc"
					q2 = "debut.anime.episode,desc"
				}
				characterDataPaged(q1 ,q2)
			}
		}.asLiveData()
	}

	private var characterDataByPowerPagedData :  Flow<PagingData<Character>>? = null
	fun characterDataByPowerPaged():Flow<PagingData<Character>> {
		if( characterDataByPowerPagedData == null )
			characterDataByPowerPagedData = repository
				.getCharactersSortedByPowerPaged()
				.map { pagingData ->
					pagingData.map {
						it
					}
				}
				.cachedIn(viewModelScope)
		return characterDataByPowerPagedData!!
	}

	private var characterDataPaged : Flow<PagingData<Character>>? = null
	private var lastQuery : String = ""
	private fun characterDataPaged(sortParam1 : String, sortParam2: String = "" ): Flow<PagingData<Character>> {
		if( characterDataPaged == null || lastQuery != sortParam1 )
			lastQuery = sortParam1
			characterDataPaged = repository
				.getCharactersPaged( sortParam1 ,sortParam2)
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