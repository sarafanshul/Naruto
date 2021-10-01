package com.projectdelta.naruto.ui.main.character.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.projectdelta.naruto.util.CollapsingToolbarState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@Suppress("unused" , "MemberVisibilityCanBePrivate")
@HiltViewModel
class CharacterDetailViewModel @Inject constructor() : ViewModel() {

	private val interactionManager = CharacterDetailInteractionManager()

	val collapsingToolbarState : LiveData<CollapsingToolbarState>
		get() = interactionManager.collapsingToolbarState

	fun setCollapsingToolbarState(state: CollapsingToolbarState) {
		interactionManager.setCollapsingToolbarState(state)
	}

	fun isToolbarCollapsed() =
		collapsingToolbarState.value is CollapsingToolbarState.Collapsed

	fun isToolbarExpanded() =
		collapsingToolbarState.value is CollapsingToolbarState.Expanded

}