package com.projectdelta.naruto.util

import androidx.lifecycle.MutableLiveData
import com.projectdelta.naruto.data.model.entity.chapter.Chapter.Companion.MAX_EPISODE_NUMBER
import com.projectdelta.naruto.data.model.entity.character.Character
import com.projectdelta.naruto.util.networking.ApiConstants.SORT_ASC

/**
 * A Bus to store preference info.
 * - Use-case : data holder for [MutableLiveData] in viewModel.
 */
object PrefBus {
	data class CharacterDataPrefBus(
		var sort: Int = 0,
		var filters: suspend (it: Character) -> Boolean,
		var query: String = "",
	)

	data class EpisodeDataPrefBus(
		var rangeL: Int = 0,
		var rangeR: Int = MAX_EPISODE_NUMBER,
		var cannon: Boolean = false,
		var sort: Int = SORT_ASC,
	)
}
