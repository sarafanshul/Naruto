package com.projectdelta.naruto.util.networking.page

import com.google.gson.annotations.SerializedName

data class CustomResponsePageable(
	@SerializedName("sort") var sort: CustomResponseSort,
	@SerializedName("offset") var offset: Int,
	@SerializedName("pageNumber") var pageNumber: Int,
	@SerializedName("pageSize") var pageSize: Int,
	@SerializedName("unpaged") var unpaged: Boolean,
	@SerializedName("paged") var paged: Boolean
)
