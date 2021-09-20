package com.projectdelta.naruto.ui.main.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.projectdelta.naruto.data.model.entity.location.Village
import com.projectdelta.naruto.data.repository.VillageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
	private val repository: VillageRepository
) : ViewModel() {

	private var getLocationPagedData : Flow<PagingData<Village>>? = null
	fun getLocationPaged() : Flow<PagingData<Village>> {
		if( getLocationPagedData == null )
			getLocationPagedData = repository
				.getVillagePaged()
				.map { pagingData ->
					pagingData.map {
						it
					}
				}
				.cachedIn(viewModelScope)
		return getLocationPagedData!!
	}

}