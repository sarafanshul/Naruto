package com.projectdelta.naruto.data.remote

import com.projectdelta.naruto.data.model.entity.location.Village
import com.projectdelta.naruto.util.networking.ApiConstants.QUERY_PAGE
import com.projectdelta.naruto.util.networking.ApiConstants.SUB_URL_PAGE
import com.projectdelta.naruto.util.networking.ApiConstants.VILLAGE_URL
import com.projectdelta.naruto.util.networking.ApiResult
import com.projectdelta.naruto.util.networking.page.PageResult
import retrofit2.http.GET
import retrofit2.http.Query

interface VillageApi {

	@GET(VILLAGE_URL + SUB_URL_PAGE)
	suspend fun getVillagesPaged(
		@Query(QUERY_PAGE) pageNumber: Int
	): ApiResult<PageResult<Village?>>
}
