package com.projectdelta.naruto.ui.main.character.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.projectdelta.naruto.util.CollapsingToolbarState

class CharacterDetailInteractionManager {

	private val _collapsingToolbarState: MutableLiveData<CollapsingToolbarState> =
		MutableLiveData(CollapsingToolbarState.Expanded()) // start as Expanded

	val collapsingToolbarState: LiveData<CollapsingToolbarState>
		get() = _collapsingToolbarState

	fun setCollapsingToolbarState(state: CollapsingToolbarState) {
		if (state.toString() != _collapsingToolbarState.value.toString())
			_collapsingToolbarState.value = state
	}
}