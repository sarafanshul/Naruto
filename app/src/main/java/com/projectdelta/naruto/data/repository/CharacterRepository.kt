package com.projectdelta.naruto.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.projectdelta.naruto.data.model.entity.character.Character
import com.projectdelta.naruto.data.remote.CharacterApi
import com.projectdelta.naruto.data.repository.init.InitManager
import com.projectdelta.naruto.util.networking.ApiResult
import com.projectdelta.naruto.util.networking.page.PageResult
import com.projectdelta.naruto.util.networking.page.PagingSource
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

class CharacterRepository @Inject constructor(
	private val characterApi: CharacterApi
) : InitManager<Character> {

	companion object {
		const val DEFAULT_PAGE_SIZE = 20
	}

	fun getCharactersSortedByPowerPaged(): Flow<PagingData<Character>> {
		return Pager(
			config = PagingConfig(
				pageSize = DEFAULT_PAGE_SIZE,
				enablePlaceholders = false
			),
			pagingSourceFactory = {
				PagingSource(endPoint = { x: Int ->
					characterApi.getCharactersSortedByPower(x)
				})
			}
		).flow
	}

	fun getCharactersPaged( sortParam1 : String , sortParam2: String = "" ) : Flow<PagingData<Character>> {
		return Pager(
			config = PagingConfig(
				pageSize = DEFAULT_PAGE_SIZE,
				enablePlaceholders = false
			),
			pagingSourceFactory = {
				PagingSource(endPoint = { x: Int ->
					characterApi.getCharacterPaged(x ,sortParam1 ,sortParam2)
				})
			}
		).flow
	}

}