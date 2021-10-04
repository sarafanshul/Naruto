package com.projectdelta.naruto.data.remote

import com.projectdelta.naruto.data.model.entity.jutsu.Jutsu
import com.projectdelta.naruto.util.networking.ApiConstants.JUTSU_URL
import com.projectdelta.naruto.util.networking.ApiConstants.SUB_URL_ID
import com.projectdelta.naruto.util.networking.ApiResult
import retrofit2.http.GET
import retrofit2.http.Path


interface JutsuApi {

	@Suppress("ConvertToStringTemplate")
	@GET(JUTSU_URL + SUB_URL_ID + "/{id}")
	suspend fun getJutsuById(
		@Path("id") id : String
	) : ApiResult<Jutsu?>

}