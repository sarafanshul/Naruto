package com.projectdelta.naruto.util.networking.page

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.projectdelta.naruto.data.model.entity.location.Village
import com.projectdelta.naruto.data.remote.VillageApi
import com.projectdelta.naruto.util.networking.ApiResult

/**
 * Auto Paging Implementation for paging response from server,
 * refer [this](https://medium.com/nerd-for-tech/pagination-in-android-with-paging-3-retrofit-and-kotlin-flow-2c2454ff776e)
 * and [that](https://medium.com/swlh/paging3-recyclerview-pagination-made-easy-333c7dfa8797)
 * for how to implement from scratch
 */
class VillagePagingSource (
	private val service : VillageApi
) : PagingSource<Int ,Village>() {
	companion object{
		const val DEFAULT_STARTING_PAGE_INDEX = 0
	}

	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Village> {
		val pageIndex = params.key ?: ChapterPagingSource.DEFAULT_STARTING_PAGE_INDEX

		val responsePageable = service.getVillagesPaged(pageIndex)

		val responseData = mutableListOf<Village>()
		val nextKey: Int?

		when( responsePageable ){
			is ApiResult.Success -> {
				nextKey = pageIndex + 1
				responseData.addAll(responsePageable.data.content.filterNotNull())
			}
			else -> {
				nextKey = null
			}
		}

		return LoadResult.Page(
			data = responseData ,
			prevKey = if( pageIndex == DEFAULT_STARTING_PAGE_INDEX ) null else pageIndex ,
			nextKey = nextKey
		)
	}

	/**
	 * The refresh key is used for subsequent calls to PagingSource.Load after the initial load.
	 */
	override fun getRefreshKey(state: PagingState<Int, Village>): Int? {
		// We need to get the previous key (or next key if previous is null) of the page
		// that was closest to the most recently accessed index.
		// Anchor position is the most recently accessed index.
		return state.anchorPosition?.let { anchorPosition ->
			state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
				?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
		}
	}

}