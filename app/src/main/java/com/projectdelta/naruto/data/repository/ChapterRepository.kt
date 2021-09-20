package com.projectdelta.naruto.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.projectdelta.naruto.data.model.entity.chapter.Chapter
import com.projectdelta.naruto.data.model.entity.character.Character
import com.projectdelta.naruto.data.remote.ChapterApi
import com.projectdelta.naruto.data.repository.init.InitManager
import com.projectdelta.naruto.util.networking.ApiResult
import com.projectdelta.naruto.util.networking.page.PageResult
import com.projectdelta.naruto.util.networking.page.PagingSource
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

class ChapterRepository @Inject constructor(
	private val chapterApi: ChapterApi
) : InitManager<Character> {

	companion object{
		const val DEFAULT_PAGE_SIZE = 20
	}

	suspend fun getChapterSorted(pageNumber : Int): ApiResult<PageResult<Chapter?>>{
		Timber.d("fetching paged chapter by power page:$pageNumber")
		return chapterApi.getChapterSortedPaged(pageNumber)
	}

	fun getChapterSortedPaged() : Flow<PagingData<Chapter>> {
		return Pager(
			config = PagingConfig(
				pageSize = DEFAULT_PAGE_SIZE,
				enablePlaceholders = false
			) ,
			pagingSourceFactory = {
				PagingSource(endPoint = { x : Int ->
					chapterApi.getChapterSortedPaged(x)
				})
			}
		).flow
	}

}