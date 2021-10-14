package com.projectdelta.naruto.ui.main.character.detail

import androidx.lifecycle.MutableLiveData
import com.projectdelta.naruto.data.model.entity.chapter.Chapter
import com.projectdelta.naruto.data.model.entity.jutsu.Jutsu
import com.projectdelta.naruto.data.repository.CharacterRepository
import com.projectdelta.naruto.ui.base.BaseDetailViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
	private val repository: CharacterRepository
) : BaseDetailViewModel() {

	val jutsuList : MutableLiveData<List<Jutsu>> = MutableLiveData(listOf())
	suspend fun setJutsus( id : String ){
		if( jutsuList.value?.size == 0 ) {
			val result = repository.getCharacterJutsuFiltered(id).filterNotNull()
			jutsuList.postValue(result)
		}
	}

	val chapter : MutableLiveData<Chapter?> = MutableLiveData(null)
	suspend fun setChapter( id : String ){
		if( chapter.value == null ){
			val result = repository.getCharacterDebutChapter(id)
			if( ! result.isNullOrEmpty() )
				chapter.postValue(result.first())
		}
		Timber.d(chapter.value.toString())
	}

}