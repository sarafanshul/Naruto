package com.projectdelta.naruto.ui.main.episode

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.projectdelta.naruto.data.model.entity.chapter.Chapter
import com.projectdelta.naruto.data.repository.ChapterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class EpisodeViewModel @Inject constructor(
	private val repository : ChapterRepository
) : ViewModel(){

	private var chapterDataSortedPaged : Flow<PagingData<Chapter>>? = null
	fun getChapterSortedPaged() : Flow<PagingData<Chapter>> {
		if( chapterDataSortedPaged == null )
			chapterDataSortedPaged = repository
				.getChapterSortedPaged()
				.map { pagingData ->
					pagingData.map {
						it
					}
				}
				.cachedIn(viewModelScope)
		return chapterDataSortedPaged!!
	}
}