package com.projectdelta.naruto.util.networking.page

import com.google.gson.annotations.SerializedName

data class PageResult<T>(
	@SerializedName("content") var content: List<T>,
	@SerializedName("pageable") var pageable: CustomResponsePageable,
	@SerializedName("totalPages") var totalPages: Int,
	@SerializedName("totalElements") var totalElements: Int,
	@SerializedName("last") var last: Boolean,
	@SerializedName("size") var size: Int,
	@SerializedName("number") var number: Int,
	@SerializedName("sort") var sort: CustomResponseSort,
	@SerializedName("numberOfElements") var numberOfElements: Int,
	@SerializedName("first") var first: Boolean,
	@SerializedName("empty") var empty: Boolean
)
