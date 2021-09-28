package com.projectdelta.naruto.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.projectdelta.naruto.data.model.entity.character.Character
import com.projectdelta.naruto.data.remote.CharacterApi
import com.projectdelta.naruto.data.repository.init.InitManager
import com.projectdelta.naruto.util.networking.page.PagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CharacterRepository @Inject constructor(
	private val characterApi: CharacterApi
) : InitManager<Character> {

	companion object {
		const val DEFAULT_PAGE_SIZE = 20
	}

	fun getCharactersSortedByPowerPaged(reverse : Boolean): Flow<PagingData<Character>> {
		return Pager(
			config = PagingConfig(
				pageSize = DEFAULT_PAGE_SIZE,
				enablePlaceholders = false
			),
			pagingSourceFactory = {
				PagingSource(endPoint = loadMore@{ x: Int ->
					characterApi.getCharactersSortedByPower(x, reverse)
				})
			}
		).flow
	}


	@Deprecated("Uses Deprecated API, use getCoreCharacters")
	fun getCharactersPaged( sortParam1 : String , sortParam2: String = "" ) : Flow<PagingData<Character>> {
		return Pager(
			config = PagingConfig(
				pageSize = DEFAULT_PAGE_SIZE,
				enablePlaceholders = false
			),
			pagingSourceFactory = {
				PagingSource(endPoint = { x: Int ->
					@Suppress("DEPRECATION")
					characterApi.getCharacterPaged(x ,sortParam1 ,sortParam2)
				})
			}
		).flow
	}

	fun getCoreCharacters( sortParam: Character.Companion.SortCharacter ) : Flow<PagingData<Character>>{
		return Pager(
			config = PagingConfig(
				pageSize = DEFAULT_PAGE_SIZE,
				enablePlaceholders = false
			),
			pagingSourceFactory = {
				PagingSource(endPoint = loadMore@{ x: Int ->
					characterApi.getCoreCharacters(x ,sortParam.value)
				})
			}
		).flow
	}

	fun getCharacterLikePaged(name : String , sortParam: Character.Companion.SortCharacter) : Flow<PagingData<Character>>{
		return Pager(
			config = PagingConfig(
				pageSize = DEFAULT_PAGE_SIZE,
				enablePlaceholders = false
			),
			pagingSourceFactory = {
				PagingSource(endPoint = loadMore@{ x: Int ->
					characterApi.getCharacterLikePaged(name ,x ,sortParam.value)
				})
			}
		).flow
	}

}