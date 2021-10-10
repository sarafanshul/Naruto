package com.projectdelta.naruto.data.remote

import com.projectdelta.naruto.data.model.entity.chapter.Chapter
import com.projectdelta.naruto.util.networking.ApiConstants.CHAPTER_URL
import com.projectdelta.naruto.util.networking.ApiConstants.QUERY_PAGE
import com.projectdelta.naruto.util.networking.ApiConstants.SUB_URL_SORTED
import com.projectdelta.naruto.util.networking.ApiResult
import com.projectdelta.naruto.util.networking.page.PageResult
import retrofit2.http.GET
import retrofit2.http.Query

interface ChapterApi {

	@GET(CHAPTER_URL + SUB_URL_SORTED)
	suspend fun getChapterSortedPaged(
		@Query(QUERY_PAGE) pageNumber: Int
	): ApiResult<PageResult<Chapter?>>

	
}