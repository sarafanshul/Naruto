package com.projectdelta.naruto.data.model.entity.jutsu

import java.io.Serializable

data class JutsuName(
	val english: String? = null,
	val kanji: String? = null,
	val romaji: String? = null,
	val other: String? = null,
): Serializable
