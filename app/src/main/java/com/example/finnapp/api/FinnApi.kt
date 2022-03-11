package com.example.finnapp.api

import com.example.finnapp.api.model.company.CompanyProfile
import com.example.finnapp.api.model.news.News
import com.example.finnapp.api.model.stock.Stock
import com.example.finnapp.api.model.stock.StockMetric
import com.example.finnapp.api.model.stock.StockPriceQuote
import com.example.finnapp.api.model.stock.StockQuarterlyIncome
import com.example.finnapp.api.utils.ConstantsUrl.COMPANY_PROFILE_URL
import com.example.finnapp.api.utils.ConstantsUrl.NEWS_COMPANY_URL
import com.example.finnapp.api.utils.ConstantsUrl.NEWS_MARKER_URL
import com.example.finnapp.api.utils.ConstantsUrl.STOCK_METRIC
import com.example.finnapp.api.utils.ConstantsUrl.STOCK_PRICE_QUOTE
import com.example.finnapp.api.utils.ConstantsUrl.STOCK_QUARTERLY_INCOME
import com.example.finnapp.api.utils.ConstantsUrl.STOCK_SYMBOL_URL
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FinnApi {

    @GET(STOCK_SYMBOL_URL)
    suspend fun getStockSymbol(
        @Query("exchange") exchange:String = "US",
    ):Response<List<Stock>>

    @GET(NEWS_MARKER_URL)
    suspend fun getNewsMarker(
        
    ):Response<List<News>>

    @GET(NEWS_COMPANY_URL)
    suspend fun getNewsCompany(
        @Query("symbol") symbol:String,
        @Query("from") fromDate:String = "1900-01-01",
        @Query("to") toDate:String = "2100-01-01"
    ):Response<List<News>>

    @GET(COMPANY_PROFILE_URL)
    suspend fun getCompanyProfile(
        @Query("symbol") symbol:String
    ):Response<CompanyProfile>

    @GET(STOCK_PRICE_QUOTE)
    suspend fun getStockPriceQuote(
        @Query("symbol") symbol:String
    ):Response<StockPriceQuote>

    @GET(STOCK_METRIC)
    suspend fun getStockMetric(
        @Query("symbol") symbol:String
    ):Response<StockMetric>

    @GET(STOCK_QUARTERLY_INCOME)
    suspend fun getStockQuarterlyIncome(
        @Query("symbol") symbol:String
    ):Response<List<StockQuarterlyIncome>>

}