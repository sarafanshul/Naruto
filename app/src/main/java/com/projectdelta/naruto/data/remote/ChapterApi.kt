package com.projectdelta.naruto.data.remote

import com.projectdelta.naruto.data.model.entity.chapter.Chapter
import com.projectdelta.naruto.util.networking.ApiConstants.CHAPTER_URL
import com.projectdelta.naruto.util.networking.ApiConstants.QUERY_CANNON
import com.projectdelta.naruto.util.networking.ApiConstants.QUERY_PAGE
import com.projectdelta.naruto.util.networking.ApiConstants.QUERY_RANGE_L
import com.projectdelta.naruto.util.networking.ApiConstants.QUERY_RANGE_R
import com.projectdelta.naruto.util.networking.ApiConstants.QUERY_SORT
import com.projectdelta.naruto.util.networking.ApiConstants.SUB_URL_IZANAMI
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

	@GET(CHAPTER_URL + SUB_URL_IZANAMI)
	suspend fun getChapterRangedOrdered(
		@Query(QUERY_RANGE_L) rangeL : Int ,
		@Query(QUERY_RANGE_R) rangeR : Int ,
		@Query(QUERY_CANNON) cannon : Boolean ,
		@Query(QUERY_SORT) sort : Int ,
		@Query(QUERY_PAGE) pageNumber: Int
	) : ApiResult< PageResult< Chapter? > >
}