package com.projectdelta.naruto.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.projectdelta.naruto.data.model.entity.character.Character
import com.projectdelta.naruto.data.remote.CharacterApi
import com.projectdelta.naruto.data.repository.init.InitManager
import com.projectdelta.naruto.util.networking.ApiResult
import com.projectdelta.naruto.util.networking.page.CharacterPagingSource
import com.projectdelta.naruto.util.networking.page.PageResult
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

class CharacterRepository @Inject constructor(
	private val characterApi: CharacterApi
) : InitManager<Character> {

	companion object{
		const val DEFAULT_PAGE_SIZE = 20
	}

	/**
	 * Bare bone impl of get request
	 */
	suspend fun getCharactersSortedByPower(pageNumber : Int): ApiResult<PageResult<Character?>>{
		Timber.d("fetching paged character by power page:$pageNumber")
		return characterApi.getCharactersSortedByPower(pageNumber)
	}

	fun getCharactersSortedByPowerPaged(): Flow<PagingData<Character>> {
		return Pager(
			config = PagingConfig(
				pageSize = DEFAULT_PAGE_SIZE,
				enablePlaceholders = false
			) ,
			pagingSourceFactory = {
				CharacterPagingSource(service = characterApi)
			}
		).flow
	}

}