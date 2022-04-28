package com.projectdelta.naruto.data.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import com.projectdelta.naruto.data.model.entity.location.Village
import com.projectdelta.naruto.data.remote.VillageApi
import com.projectdelta.naruto.data.repository.init.InitManager
import com.projectdelta.naruto.util.networking.page.PagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class VillageRepository @Inject constructor(
	private val villageApi: VillageApi
) : InitManager<Village> {


	fun getVillagePaged(): Flow<PagingData<Village>> {
		return Pager(
			config = PagingSource.defaultPagingConfig,
			pagingSourceFactory = {
				PagingSource(endPoint = { x: Int ->
					villageApi.getVillagesPaged(x)
				},
					filters = { true })
			}
		).flow
	}
}
