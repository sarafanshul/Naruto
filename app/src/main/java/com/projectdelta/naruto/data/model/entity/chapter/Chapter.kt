package com.projectdelta.naruto.data.model.entity.chapter

import java.io.Serializable

data class Chapter(
	val id: String? = null,
	val name: ChapterName? = null,
	val episode: ChapterEpisode? = null,
	val description: String? = null,
	val images: List<String>? = null,
	val arc: String? = null,
	val manga: ChapterManga? = null,
	val music: ChapterMusic? = null,
	val date: ChapterAirDate? = null,
) : Serializable {

	companion object {
		private const val serialVersionUID = 1L
	}

}