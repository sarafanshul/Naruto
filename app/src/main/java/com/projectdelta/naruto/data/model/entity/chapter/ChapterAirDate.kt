package com.projectdelta.naruto.data.model.entity.chapter

import java.io.Serializable
import java.util.Date

data class ChapterAirDate(
	val japanese: Date? = null,
	val english: Date? = null
) : Serializable
