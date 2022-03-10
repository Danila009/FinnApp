package com.example.finnapp.api

import com.example.finnapp.api.model.news.News
import com.example.finnapp.api.model.stock.Stock
import com.example.finnapp.api.utils.ConstantsUrl.NEWS_URL
import com.example.finnapp.api.utils.ConstantsUrl.STOCK_SYMBOL_URL
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FinnApi {

    @GET(STOCK_SYMBOL_URL)
    suspend fun getStockSymbol(
        @Query("exchange") exchange:String = "US",
    ):Response<List<Stock>>

    @GET(NEWS_URL)
    suspend fun getNews(
        
    ):Response<List<News>>
}