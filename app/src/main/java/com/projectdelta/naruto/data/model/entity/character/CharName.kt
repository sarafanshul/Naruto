package com.projectdelta.naruto.data.model.entity.character

import java.io.Serializable

data class CharName(
	val english: String? = null,
	val kanji: String? = null,
	val romaji: String? = null,
	val others: List<String>? = null,
) : Serializable
