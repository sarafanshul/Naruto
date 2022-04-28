package com.projectdelta.naruto.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.projectdelta.naruto.util.CollapsingToolbarState

@Suppress("unused", "MemberVisibilityCanBePrivate")
abstract class BaseDetailViewModel : ViewModel() {

	private val interactionManager = BaseDetailInteractionManager()

	val collapsingToolbarState: LiveData<CollapsingToolbarState>
		get() = interactionManager.collapsingToolbarState

	fun setCollapsingToolbarState(state: CollapsingToolbarState) {
		interactionManager.setCollapsingToolbarState(state)
	}

	fun isToolbarCollapsed() =
		collapsingToolbarState.value is CollapsingToolbarState.Collapsed

	fun isToolbarExpanded() =
		collapsingToolbarState.value is CollapsingToolbarState.Expanded
}
