package com.example.finnapp.api.repository

import com.example.finnapp.api.NetworkResult
import com.example.finnapp.api.FinnApi
import com.example.finnapp.api.apiResponse.BaseApiResponse
import com.example.finnapp.api.model.news.News
import com.example.finnapp.api.model.stock.Stock
import javax.inject.Inject

class ApiFinnRepository @Inject constructor(
    private val stockApi: FinnApi
):BaseApiResponse() {

    suspend fun getStockSymbol():NetworkResult<List<Stock>> = safeApiCall{stockApi.getStockSymbol()}

    suspend fun getNews():NetworkResult<List<News>> = safeApiCall { stockApi.getNews() }
}