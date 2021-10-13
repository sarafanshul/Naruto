package com.projectdelta.naruto.ui.main.location.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.projectdelta.naruto.data.model.entity.location.Village
import com.projectdelta.naruto.data.repository.VillageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
	private val repository: VillageRepository
) : ViewModel() {

	private var getLocationPagedData : LiveData<PagingData<Village>>? = null
	fun getLocationPaged() : LiveData<PagingData<Village>> {
		if( getLocationPagedData == null )
			getLocationPagedData = repository
				.getVillagePaged()
				.map { pagingData ->
					pagingData.map {
						it
					}
				}
				.cachedIn(viewModelScope)
				.asLiveData()
		return getLocationPagedData!!
	}

}