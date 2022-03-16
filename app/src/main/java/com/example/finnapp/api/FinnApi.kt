package com.example.finnapp.api

import com.example.finnapp.api.model.company.CompanyProfile
import com.example.finnapp.api.model.covid19.Covid19
import com.example.finnapp.api.model.news.News
import com.example.finnapp.api.model.stock.*
import com.example.finnapp.api.utils.ConstantsUrl.COMPANY_PROFILE_URL
import com.example.finnapp.api.utils.ConstantsUrl.COVID_19_URL
import com.example.finnapp.api.utils.ConstantsUrl.NEWS_COMPANY_URL
import com.example.finnapp.api.utils.ConstantsUrl.NEWS_MARKER_URL
import com.example.finnapp.api.utils.ConstantsUrl.STOCK_LOOKUP_URL
import com.example.finnapp.api.utils.ConstantsUrl.STOCK_METRIC_URL
import com.example.finnapp.api.utils.ConstantsUrl.STOCK_PRICE_QUOTE_URL
import com.example.finnapp.api.utils.ConstantsUrl.STOCK_QUARTERLY_INCOME_URL
import com.example.finnapp.api.utils.ConstantsUrl.STOCK_SYMBOL_URL
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FinnApi {

    @GET(STOCK_SYMBOL_URL)
    suspend fun getStockSymbol(
        @Query("exchange") exchange:String,
    ):Response<List<Stock>>

    @GET(STOCK_LOOKUP_URL)
    suspend fun getStockLookup(
        @Query("q") search:String
    ):Response<StockLookup>

    @GET(NEWS_MARKER_URL)
    suspend fun getNewsMarker(
        @Query("category") category:String
    ):Response<List<News>>

    @GET(NEWS_COMPANY_URL)
    suspend fun getNewsCompany(
        @Query("symbol") symbol:String,
        @Query("from") fromDate:String,
        @Query("to") toDate:String
    ):Response<List<News>>

    @GET(COMPANY_PROFILE_URL)
    suspend fun getCompanyProfile(
        @Query("symbol") symbol:String
    ):Response<CompanyProfile>

    @GET(STOCK_PRICE_QUOTE_URL)
    suspend fun getStockPriceQuote(
        @Query("symbol") symbol:String
    ):Response<StockPriceQuote>

    @GET(STOCK_METRIC_URL)
    suspend fun getStockMetric(
        @Query("symbol") symbol:String
    ):Response<StockMetric>

    @GET(STOCK_QUARTERLY_INCOME_URL)
    suspend fun getStockQuarterlyIncome(
        @Query("symbol") symbol:String
    ):Response<List<StockQuarterlyIncome>>

    @GET(COVID_19_URL)
    suspend fun getCovid19():Response<List<Covid19>>
}