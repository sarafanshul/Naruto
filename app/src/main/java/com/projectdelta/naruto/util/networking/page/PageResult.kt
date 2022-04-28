package com.projectdelta.naruto.util.networking.page

import com.google.gson.annotations.SerializedName
import com.projectdelta.naruto.data.model.entity.BaseModel

data class PageResult<T : BaseModel?>(
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
) {
	companion object {
		data class CustomResponsePageable(
			@SerializedName("sort") var sort: CustomResponseSort,
			@SerializedName("offset") var offset: Int,
			@SerializedName("pageNumber") var pageNumber: Int,
			@SerializedName("pageSize") var pageSize: Int,
			@SerializedName("unpaged") var unpaged: Boolean,
			@SerializedName("paged") var paged: Boolean
		)

		data class CustomResponseSort(
			@SerializedName("sorted") var sorted: Boolean,
			@SerializedName("unsorted") var unsorted: Boolean,
			@SerializedName("empty") var empty: Boolean
		)
	}

}
