package com.projectdelta.naruto.ui.main.episode.list

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.projectdelta.naruto.data.model.entity.chapter.Chapter
import com.projectdelta.naruto.data.preference.PreferenceManager
import com.projectdelta.naruto.data.repository.ChapterRepository
import com.projectdelta.naruto.util.PrefBus.EpisodeDataPrefBus
import com.projectdelta.naruto.util.networking.ApiConstants.SORT_ASC
import com.projectdelta.naruto.util.networking.ApiConstants.SORT_DESC
import com.projectdelta.naruto.widgets.ExtendedNavigationView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class EpisodeViewModel @Inject constructor(
	private val repository : ChapterRepository ,
	private val preferenceManager: PreferenceManager
) : ViewModel(){

	val rangeEpSt : Int
		get() = preferenceManager.rangeEpisodeStart()

	val rangeEpEd : Int
		get() = preferenceManager.rangeEpisodeEnd()

	private val cannon : Boolean
		get() = preferenceManager.filterCannon()

	private val currentDataPref = MutableLiveData(EpisodeDataPrefBus())

	init {
		onSortChanged()
		onFilterChanged()
		onRangeChanged()
	}

	var data = currentDataPref.switchMap { ( rangeL ,rangeR ,cannon ,sort ) ->
		getChapterRangedOrdered(rangeL, rangeR, cannon, sort)
			.cachedIn(viewModelScope)
			.asLiveData()
	}


	private fun getChapterRangedOrdered(
		rangeL : Int,
		rangeR : Int,
		cannon : Boolean,
		sort : Int
	): Flow<PagingData<Chapter>>{
		return repository
			.getChapterRangedOrdered(rangeL, rangeR, cannon, sort)
			.map { pagingData ->
				pagingData.map {
					it
				}
			}
			.cachedIn(viewModelScope)
	}

	fun updateEpisodeRange(values: List<Float>) {
		Timber.d(values.toMutableList().toString())
		preferenceManager.setRangeEpisodeStart( values.first().toInt() )
		preferenceManager.setRangeEpisodeEnd( values.last().toInt() )

		onRangeChanged()
	}

	private fun onRangeChanged() {
		val cur = currentDataPref.value
		cur?.rangeL = rangeEpSt
		cur?.rangeR = rangeEpEd
		currentDataPref.value = cur
	}

	fun onFilterChanged() {
		val cur = currentDataPref.value
		cur?.cannon = cannon
		currentDataPref.value = cur
	}

	fun onSortChanged() {
		val cur = currentDataPref.value
		cur?.sort = when( preferenceManager.sortAirDate() ){
			ExtendedNavigationView.Item.MultiSort.SORT_DESC -> SORT_DESC
			else -> SORT_ASC
		}
		currentDataPref.value = cur
	}

}