package com.projectdelta.naruto.util

import androidx.lifecycle.MutableLiveData
import com.projectdelta.naruto.data.model.entity.character.Character

/**
 * A Bus to store preference info.
 * - Use-case : data holder for [MutableLiveData] in viewModel.
 */
data class DataPrefBus(
	var sort : Int = 0,
	var filters : suspend (it : Character) -> Boolean,
	var query : String = "",
)
