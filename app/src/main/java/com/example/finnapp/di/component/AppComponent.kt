package com.example.finnapp.di.component

import com.example.finnapp.di.ApiModule
import com.example.finnapp.screen.newsScreen.viewModel.NewsViewModel
import com.example.finnapp.screen.stockScreen.viewModel.StockViewModel
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [
        ApiModule::class
    ]
)
@Singleton
interface AppComponent {

    fun stockViewModel():StockViewModel
    fun newsViewModel():NewsViewModel
}