package com.projectdelta.naruto.ui.main.character

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.projectdelta.naruto.data.model.entity.character.Character
import com.projectdelta.naruto.data.repository.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel @Inject constructor(
	private val repository: CharacterRepository
):ViewModel() {

	fun characterData(pageNumber : Int) = flow{
		val response = repository.getCharactersSortedByPower(pageNumber)
		emit(response)
	}.flowOn(Dispatchers.IO)

	private var characterDataByPowerPagedData :  Flow<PagingData<Character>>? = null
	fun characterDataByPowerPaged():Flow<PagingData<Character>> {
		if( characterDataByPowerPagedData == null )
			characterDataByPowerPagedData = repository
				.getCharactersSortedByPowerPaged()
				.map { pagingData ->
					pagingData.map {
						it
					}
				}
				.cachedIn(viewModelScope)
		return characterDataByPowerPagedData!!
	}
}