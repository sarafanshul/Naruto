package com.projectdelta.naruto.util.networking.page


import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.projectdelta.naruto.data.model.entity.BaseModel
import com.projectdelta.naruto.util.networking.ApiResult
import timber.log.Timber

/**
 * Auto Paging Implementation for paging response from server,
 * refer [this](https://medium.com/nerd-for-tech/pagination-in-android-with-paging-3-retrofit-and-kotlin-flow-2c2454ff776e)
 * and [that](https://medium.com/swlh/paging3-recyclerview-pagination-made-easy-333c7dfa8797)
 * for how to implement from scratch
 *
 * - *Update 20-09 00:42* - changed params from service to a particular endpoint, because **if it works**
 * we don't have to create a new PagingSource for every new page endpoint.
 *
 * - *Update 20-09 17:02* - changed the implementation for [PagingSource] to support generics hence
 * we don't have to define a custom paging source for different Documents , for now it supports all
 * subclasses of [BaseModel].
 *
 * - *Update 22-09 13:01* - now [PagingSource.load] returns a [PagingSource.LoadResult.Error] when encounters a [ApiResult.NetworkError] ,
 * [ApiResult.Failure] and [ApiResult.Empty] are still grouped together and returns `nextKey = null`
 * because we can get a empty page from service i.e [PageResult.content] can be null if [PageResult.last] is true
 *
 * - *Update 22-09 13:01* - **MAJOR UPDATE** as [PageResult] will return [ApiResult.Success] (200) on every page with empty [PageResult.content],
 * regardless it exists or not cause of spring paging controller issues this adapter will load infinite number of pages
 * mimicking a local DOS attack , now `nextKey` will be null if `content.size == 0`.
 *
 * @param T generic for type of document needed.
 * @param endPoint a suspend HTTP request endpoint
 */
class PagingSource<T: BaseModel> (
	private val endPoint : suspend (Int) -> ApiResult<PageResult<T?>>
) : PagingSource<Int ,T>() {

	companion object{
		const val DEFAULT_STARTING_PAGE_INDEX = 0
	}

	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
		Timber.d("load Called for : ${endPoint::class}")
		val pageIndex = params.key ?: DEFAULT_STARTING_PAGE_INDEX
		val responsePageable = endPoint(pageIndex)
		val responseData : MutableList<T> = mutableListOf()
		val nextKey: Int?
		when( responsePageable ) {
			is ApiResult.Success -> {
				if(responsePageable.data.content.isNotEmpty()) {
					nextKey = pageIndex + 1
					responseData.addAll(responsePageable.data.content.filterNotNull())
				}
				else {
					nextKey = null
				}
			}
			is ApiResult.NetworkError -> {
				return LoadResult.Error(
					Throwable("No network connection!")
				)
			}
			else -> {
				nextKey = null
			}
		}

		return LoadResult.Page(
			data = responseData ,
			prevKey = if(pageIndex == DEFAULT_STARTING_PAGE_INDEX) null else pageIndex ,
			nextKey = nextKey
		)
	}

	/**
	 * The refresh key is used for subsequent calls to PagingSource.Load after the initial load.
	 */
	override fun getRefreshKey(state: PagingState<Int, T>): Int? {
		// We need to get the previous key (or next key if previous is null) of the page
		// that was closest to the most recently accessed index.
		// Anchor position is the most recently accessed index.
		return state.anchorPosition?.let { anchorPosition ->
			state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
				?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
		}
	}

}