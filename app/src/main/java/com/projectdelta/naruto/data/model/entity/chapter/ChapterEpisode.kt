package com.projectdelta.naruto.data.model.entity.chapter

import java.io.Serializable

data class ChapterEpisode(
	val series: String? = null,
	val episode: Double? = null,
	val previous: String? = null,
	val next: String? = null,
	val season: Int? = null,
	val absoluteEpisodeNumber: Double? = null,
) : Serializable
