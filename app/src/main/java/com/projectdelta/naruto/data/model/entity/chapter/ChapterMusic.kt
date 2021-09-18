package com.projectdelta.naruto.data.model.entity.chapter

import java.io.Serializable

data class ChapterMusic(
	val opening: String? = null,
	val ending: String? = null
) : Serializable