package com.projectdelta.naruto.data.model.entity.jutsu

import java.io.Serializable

data class JutsuDebut(
	val manga: JutsuManga? = null,
	val anime: JutsuAnime? = null,
	val novel: String? = null,
	val movie: String? = null,
	val game: String? = null,
	val ova: String? = null,
	val appears: List<String>? = null,
) : Serializable
