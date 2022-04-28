package com.projectdelta.naruto.data.repository

import com.projectdelta.naruto.data.remote.JutsuApi
import javax.inject.Inject

class JutsuRepository @Inject constructor(
	private val jutsuApi: JutsuApi
) {

	suspend fun getJutsuById(id: String) =
		jutsuApi.getJutsuById(id)
}
