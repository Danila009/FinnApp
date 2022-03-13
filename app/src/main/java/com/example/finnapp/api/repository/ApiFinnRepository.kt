package com.example.finnapp.api.repository

import com.example.finnapp.api.NetworkResult
import com.example.finnapp.api.FinnApi
import com.example.finnapp.api.apiResponse.BaseApiResponse
import com.example.finnapp.api.model.company.CompanyProfile
import com.example.finnapp.api.model.covid19.Covid19
import com.example.finnapp.api.model.news.News
import com.example.finnapp.api.model.stock.*
import javax.inject.Inject

class ApiFinnRepository @Inject constructor(
    private val stockApi: FinnApi,
):BaseApiResponse() {

    suspend fun getStockSymbol():NetworkResult<List<Stock>> = safeApiCall{stockApi.getStockSymbol()}

    suspend fun getStockLookup(
        search:String
    ):NetworkResult<StockLookup> = safeApiCall { stockApi.getStockLookup(
        search = search
    ) }

    suspend fun getMarkerNews():NetworkResult<List<News>> = safeApiCall { stockApi.getNewsMarker() }

    suspend fun getNewsCompany(
        symbol: String
    ):NetworkResult<List<News>> = safeApiCall { stockApi.getNewsCompany(
        symbol = symbol
    ) }

    suspend fun getCompanyProfile(symbol:String):NetworkResult<CompanyProfile> = safeApiCall { stockApi.getCompanyProfile(
        symbol = symbol
    ) }

    suspend fun getStockPriceQuote(symbol: String):NetworkResult<StockPriceQuote> = safeApiCall { stockApi.getStockPriceQuote(
        symbol = symbol
    ) }

    suspend fun getStockMetric(symbol: String):NetworkResult<StockMetric> = safeApiCall { stockApi.getStockMetric(
        symbol = symbol
    ) }

    suspend fun getStockQuarterlyIncome(symbol: String):NetworkResult<List<StockQuarterlyIncome>> = safeApiCall { stockApi.getStockQuarterlyIncome(
        symbol = symbol
    ) }

    suspend fun getCovid19():NetworkResult<List<Covid19>> = safeApiCall { stockApi.getCovid19() }
}