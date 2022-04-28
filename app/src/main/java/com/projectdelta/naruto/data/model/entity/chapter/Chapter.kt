package com.projectdelta.naruto.data.model.entity.chapter

import com.projectdelta.naruto.data.model.entity.BaseModel
import java.io.Serializable

data class Chapter(
	override var id: String,
	val name: ChapterName? = null,
	val episode: ChapterEpisode? = null,
	val description: String? = null,
	val images: List<String>? = null,
	val arc: String? = null,
	val manga: ChapterManga? = null,
	val music: ChapterMusic? = null,
	val date: ChapterAirDate? = null,
) : BaseModel(), Serializable {

	companion object {
		private const val serialVersionUID = 1L

		const val MAX_EPISODE_NUMBER = 740

		const val SEASON_1_END = 220 // inclusive

	}

}
