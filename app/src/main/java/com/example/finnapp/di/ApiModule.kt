package com.example.finnapp.di

import com.example.finnapp.api.FinnApi
import com.example.finnapp.api.repository.ApiFinnRepository
import com.example.finnapp.api.utils.ConstantsUrl.BASE_URL
import com.example.finnapp.utils.Constants.TOKEN_KEY
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class ApiModule {

    @Provides
    @Singleton
    fun providerStockApi(
        stockApi: FinnApi,
    ) = ApiFinnRepository(stockApi)

    @Provides
    @Singleton
    fun providerRetrofit(
        okHttpClient: OkHttpClient
    ):FinnApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(FinnApi::class.java)

    @Provides
    @Singleton
    fun providerOkHttpClient(

    ):OkHttpClient = OkHttpClient.Builder()
        .addInterceptor {
            val request = it.request()
                .newBuilder()
                .addHeader("X-Finnhub-Token", TOKEN_KEY)
                .build()
            it.proceed(request)
        }
        .build()
}