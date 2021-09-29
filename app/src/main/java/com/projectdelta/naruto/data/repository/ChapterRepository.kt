package com.projectdelta.naruto.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.projectdelta.naruto.data.model.entity.chapter.Chapter
import com.projectdelta.naruto.data.model.entity.character.Character
import com.projectdelta.naruto.data.remote.ChapterApi
import com.projectdelta.naruto.data.repository.init.InitManager
import com.projectdelta.naruto.util.networking.page.PagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChapterRepository @Inject constructor(
	private val chapterApi: ChapterApi
) : InitManager<Character> {

	companion object {
		const val DEFAULT_PAGE_SIZE = 20
	}

	fun getChapterSortedPaged(): Flow<PagingData<Chapter>> {
		return Pager(
			config = PagingConfig(
				pageSize = DEFAULT_PAGE_SIZE,
				enablePlaceholders = false
			),
			pagingSourceFactory = {
				PagingSource(endPoint = { x: Int ->
					chapterApi.getChapterSortedPaged(x)
				},
					filters = { true })
			}
		).flow
	}

}