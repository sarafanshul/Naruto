package com.projectdelta.naruto.data.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import com.projectdelta.naruto.data.model.entity.chapter.Chapter
import com.projectdelta.naruto.data.model.entity.character.Character
import com.projectdelta.naruto.data.model.entity.jutsu.Jutsu
import com.projectdelta.naruto.data.remote.CharacterApi
import com.projectdelta.naruto.data.repository.init.InitManager
import com.projectdelta.naruto.util.networking.ApiResult
import com.projectdelta.naruto.util.networking.page.PagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CharacterRepository @Inject constructor(
	private val characterApi: CharacterApi
) : InitManager<Character> {


	fun getCharactersSortedByPowerPaged(
		reverse: Boolean,
		filters: suspend (Character) -> Boolean
	): Flow<PagingData<Character>> {
		return Pager(
			config = PagingSource.defaultPagingConfig,
			pagingSourceFactory = {
				PagingSource(
					endPoint = loadMore@{ x: Int ->
						characterApi.getCharactersSortedByPower(x, reverse)
					},
					filters = filters
				)
			}
		).flow
	}

	fun getCoreCharacters(
		sortParam: Character.Companion.SortCharacter,
		filters: suspend (Character) -> Boolean
	): Flow<PagingData<Character>> {
		return Pager(
			config = PagingSource.defaultPagingConfig,
			pagingSourceFactory = {
				PagingSource(
					endPoint = loadMore@{ x: Int ->
						characterApi.getCoreCharacters(x, sortParam.value)
					},
					filters = filters
				)
			}
		).flow
	}

	fun getCharacterLikePaged(
		name: String, sortParam: Character.Companion.SortCharacter,
		filters: suspend (Character) -> Boolean
	): Flow<PagingData<Character>> {
		return Pager(
			config = PagingSource.defaultPagingConfig,
			pagingSourceFactory = {
				PagingSource(
					endPoint = loadMore@{ x: Int ->
						characterApi.getCharacterLikePaged(name, x, sortParam.value)
					},
					filters = filters
				)
			}
		).flow
	}

	suspend fun getCharacterJutsuFiltered(id: String): List<Jutsu?> {
		return when (val result: ApiResult<List<Jutsu?>> =
			characterApi.getCharacterJutsuFiltered(id)) {
			is ApiResult.Success -> result.data
			else -> emptyList()
		}
	}

	suspend fun getCharacterDebutChapter(id: String): List<Chapter?> {
		return when (val result: ApiResult<List<Chapter?>> =
			characterApi.getCharacterDebutChapter(id)) {
			is ApiResult.Success -> result.data
			else -> emptyList()
		}
	}

}
