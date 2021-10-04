package com.projectdelta.naruto.ui.main.character.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.projectdelta.naruto.data.model.entity.jutsu.Jutsu
import com.projectdelta.naruto.data.repository.JutsuRepository
import com.projectdelta.naruto.util.CollapsingToolbarState
import com.projectdelta.naruto.util.networking.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@Suppress("unused" , "MemberVisibilityCanBePrivate")
@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
	private val repository: JutsuRepository
) : ViewModel() {

	private val interactionManager = CharacterDetailInteractionManager()

	suspend fun getJutsuById(id:String)=
		repository.getJutsuById(id)

	var jutsuList : MutableLiveData<List<Jutsu>> = MutableLiveData(listOf())
	suspend fun setJutsus( items : List<String> ){
		if( jutsuList.value?.size == 0 )
			jutsuList.value = items.mapNotNull {
				val response : ApiResult<Jutsu?> = getJutsuById(
					it.split(" ").joinToString("_") { sss -> sss }
				)
				when(response){
					is ApiResult.Success<Jutsu?> -> response.data
					else -> null
				}
			}
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