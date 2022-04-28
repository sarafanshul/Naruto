package com.projectdelta.naruto.data.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import com.projectdelta.naruto.data.model.entity.chapter.Chapter
import com.projectdelta.naruto.data.model.entity.chapter.Chapter.Companion.MAX_EPISODE_NUMBER
import com.projectdelta.naruto.data.model.entity.character.Character
import com.projectdelta.naruto.data.remote.ChapterApi
import com.projectdelta.naruto.data.repository.init.InitManager
import com.projectdelta.naruto.util.networking.ApiConstants.SORT_ASC
import com.projectdelta.naruto.util.networking.page.PagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChapterRepository @Inject constructor(
	private val chapterApi: ChapterApi
) : InitManager<Character> {


	fun getChapterSortedPaged(): Flow<PagingData<Chapter>> {
		return Pager(
			config = PagingSource.defaultPagingConfig,
			pagingSourceFactory = {
				PagingSource(endPoint = { x: Int ->
					chapterApi.getChapterSortedPaged(x)
				},
					filters = { true })
			}
		).flow
	}

	fun getChapterRangedOrdered(
		rangeL: Int = 0,
		rangeR: Int = MAX_EPISODE_NUMBER,
		cannon: Boolean = false,
		sort: Int = SORT_ASC
	): Flow<PagingData<Chapter>> {
		return Pager(
			config = PagingSource.defaultPagingConfig,
			pagingSourceFactory = {
				PagingSource(
					endPoint = { pageNumber: Int ->
						chapterApi.getChapterRangedOrdered(rangeL, rangeR, cannon, sort, pageNumber)
					},
					filters = { true }
				)
			}
		).flow
	}

}
