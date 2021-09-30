package com.projectdelta.naruto.data.remote

import com.projectdelta.naruto.data.model.entity.character.Character
import com.projectdelta.naruto.util.networking.ApiConstants.CHARACTER_URL
import com.projectdelta.naruto.util.networking.ApiConstants.QUERY_NAME
import com.projectdelta.naruto.util.networking.ApiConstants.QUERY_PAGE
import com.projectdelta.naruto.util.networking.ApiConstants.QUERY_REVERSE
import com.projectdelta.naruto.util.networking.ApiConstants.QUERY_SORT
import com.projectdelta.naruto.util.networking.ApiConstants.SUB_URL_CORE
import com.projectdelta.naruto.util.networking.ApiConstants.SUB_URL_LIKE_PAGED
import com.projectdelta.naruto.util.networking.ApiConstants.SUB_URL_PAGE
import com.projectdelta.naruto.util.networking.ApiConstants.SUB_URL_POWER
import com.projectdelta.naruto.util.networking.ApiResult
import com.projectdelta.naruto.util.networking.page.PageResult
import retrofit2.http.GET
import retrofit2.http.Query

interface CharacterApi {

	/**
	 * Paged Result of Characters W.R.T Characters::jutsus::length , order DESC
	 * <br>
	 * Usage
	 * <br>
	 * <i>character/power/<b>page=2&size=10</b></i>
	 */
	@GET(CHARACTER_URL + SUB_URL_POWER)
	suspend fun getCharactersSortedByPower(
		@Query(QUERY_PAGE) pageNumber: Int ,
		@Query(QUERY_REVERSE) reverse: Boolean
	):ApiResult<PageResult<Character?>>

	/**
	 * Page Api just to get work done , TODO (Update Later)
	 *  [ex1](http://localhost:8080/character/page/?sort=_id,desc)
	 *  [ex2](http://localhost:8080/character/page/?sort=_id)
	 *  [ex3](http://localhost:8080/character/page/?page=22&sort=debut.anime.name,asc&sort=debut.anime.episode,asc)
	 */
	@Deprecated("Use getCoreCharacters endPoint")
	@GET(CHARACTER_URL + SUB_URL_PAGE)
	suspend fun getCharacterPaged(
		@Query(QUERY_PAGE) pageNumber: Int ,
		@Query(QUERY_SORT) sort : String ,
		@Query(QUERY_SORT) sort1 : String = ""
	):ApiResult<PageResult<Character?>>

	/**
	 * Gets cannon characters with null check on name and images.
	 * @param pageNumber let PagingSource handle it
	 * @param sortParam use [Character.SortCharacter] values for this query
	 */
	@GET(CHARACTER_URL + SUB_URL_CORE)
	suspend fun getCoreCharacters(
		@Query(QUERY_PAGE) pageNumber: Int ,
		@Query(QUERY_SORT) sortParam : String
	):ApiResult<PageResult<Character?>>

	/**
	 * Gets characters with regex match.
	 * @param pageNumber let PagingSource handle it
	 * @param sortParam use [Character.SortCharacter] values for this query
	 */
	@GET(CHARACTER_URL + SUB_URL_LIKE_PAGED)
	suspend fun getCharacterLikePaged(
		@Query(QUERY_NAME) name : String ,
		@Query(QUERY_PAGE) pageNumber: Int ,
		@Query(QUERY_SORT) sortParam: String
	):ApiResult<PageResult<Character?>>
}