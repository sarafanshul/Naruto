package com.projectdelta.naruto.data.model.entity.character

import java.io.Serializable

data class CharDebut(
	val manga: CharManga? = null,
	val anime: CharAnime? = null,
	val novel: String? = null,
	val movie: String? = null,
	val game: String? = null,
	val ova: String? = null,
	val appears: List<String>? = null,
) : Serializable