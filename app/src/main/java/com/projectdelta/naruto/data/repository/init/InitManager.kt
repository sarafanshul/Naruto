package com.projectdelta.naruto.data.repository.init

import com.projectdelta.naruto.util.networking.ApiResult
import com.projectdelta.naruto.util.networking.Message
import com.projectdelta.naruto.util.networking.StateResource
import com.projectdelta.naruto.util.networking.Status

interface InitManager<T : Any> {
	/**
	 * returns a Status.Error according to an error
	 */
	private fun <T> manageEmptyOrErrorResponse(
		notSuccessfulResponse: ApiResult<T>?
	): StateResource =
		when (notSuccessfulResponse) {
			is ApiResult.Failure -> StateResource(
				status = Status.Error,
				message = Message.ServerError(notSuccessfulResponse.statusCode ?: 0)
			)
			ApiResult.NetworkError -> StateResource(
				status = Status.Error,
				message = Message.NetworkError
			)
			ApiResult.Empty -> StateResource(
				status = Status.Error,
				message = Message.EmptyResponse
			)
			else -> StateResource(
				status = Status.Error,
				message = Message.ServerError(0)
			)
		}
}