package com.projectdelta.naruto.util.networking.page

import com.google.gson.annotations.SerializedName

data class CustomResponseSort(
	@SerializedName("sorted") var sorted: Boolean,
	@SerializedName("unsorted") var unsorted: Boolean,
	@SerializedName("empty") var empty: Boolean
)
