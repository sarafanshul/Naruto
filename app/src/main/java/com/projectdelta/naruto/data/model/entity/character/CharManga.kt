package com.projectdelta.naruto.data.model.entity.character

import java.io.Serializable

data class CharManga(
	val name: String? = null,
	val volume: Double? = null,
	val chapter: Double? = null,
) : Serializable
