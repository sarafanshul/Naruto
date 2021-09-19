package com.projectdelta.naruto.ui.main.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.projectdelta.naruto.data.repository.VillageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
	private val repository: VillageRepository
) : ViewModel() {

	fun getLocationPaged() =
		repository
			.getVillagePaged()
			.map { pagingData ->
				pagingData.map {
					it
				}
			}
			.cachedIn(viewModelScope)

}