package com.projectdelta.naruto.data.model.entity.chapter

import java.io.Serializable

data class ChapterName(
	val english: String? = null,
	val kanji: String? = null,
	val romaji: String? = null,
) : Serializable