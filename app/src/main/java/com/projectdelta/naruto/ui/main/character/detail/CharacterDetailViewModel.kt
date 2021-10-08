package com.projectdelta.naruto.ui.main.character.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.projectdelta.naruto.data.model.entity.jutsu.Jutsu
import com.projectdelta.naruto.data.repository.CharacterRepository
import com.projectdelta.naruto.util.CollapsingToolbarState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@Suppress("unused" , "MemberVisibilityCanBePrivate")
@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
	private val repository: CharacterRepository
) : ViewModel() {

	private val interactionManager = CharacterDetailInteractionManager()

	var jutsuList : MutableLiveData<List<Jutsu>> = MutableLiveData(listOf())
	suspend fun setJutsus( id : String ){
		if( jutsuList.value?.size == 0 )
			jutsuList.value = repository.getCharacterJutsuFiltered(id).filterNotNull()
	}

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