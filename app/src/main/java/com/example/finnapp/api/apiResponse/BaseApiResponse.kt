package com.example.finnapp.api.apiResponse

import com.example.finnapp.api.NetworkResult
import retrofit2.Response

abstract class BaseApiResponse {
    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): NetworkResult<T> {
        try {
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    return NetworkResult.Success(body)
                }
            }
            return NetworkResult.Error("${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return NetworkResult.Error(e.message.toString())
        }
    }
}