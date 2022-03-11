package com.example.finnapp.screen.stockScreen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finnapp.api.NetworkResult
import com.example.finnapp.api.model.company.CompanyProfile
import com.example.finnapp.api.model.news.News
import com.example.finnapp.api.model.stock.Stock
import com.example.finnapp.api.model.stock.StockMetric
import com.example.finnapp.api.model.stock.StockPriceQuote
import com.example.finnapp.api.model.stock.StockQuarterlyIncome
import com.example.finnapp.api.repository.ApiFinnRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class StockViewModel @Inject constructor(
    private val apiFinnRepository: ApiFinnRepository
):ViewModel(){

    private val _responseStock:MutableStateFlow<NetworkResult<List<Stock>>> = MutableStateFlow(NetworkResult.Loading())
    val responseStock:StateFlow<NetworkResult<List<Stock>>> = _responseStock.asStateFlow()

    private val _responseNewsCompany:MutableStateFlow<NetworkResult<List<News>>> = MutableStateFlow(NetworkResult.Loading())
    val responseNewsCompany:StateFlow<NetworkResult<List<News>>> = _responseNewsCompany.asStateFlow()

    private val _responseStockPriceQuote:MutableStateFlow<NetworkResult<StockPriceQuote>> = MutableStateFlow(NetworkResult.Loading())
    val responseStockPriceQuote: StateFlow<NetworkResult<StockPriceQuote>> = _responseStockPriceQuote.asStateFlow()

    private val _responseCompanyProfile:MutableStateFlow<NetworkResult<CompanyProfile>> =
        MutableStateFlow(NetworkResult.Loading())
    val responseCompanyProfile:StateFlow<NetworkResult<CompanyProfile>> =
        _responseCompanyProfile.asStateFlow()

    private val _responseStockMetric:MutableStateFlow<NetworkResult<StockMetric>> =
        MutableStateFlow(NetworkResult.Loading())
    val responseStockMetric:StateFlow<NetworkResult<StockMetric>> =
        _responseStockMetric.asStateFlow()

    private val _responseStockQuarterlyIncome:MutableStateFlow<NetworkResult<List<StockQuarterlyIncome>>> =
        MutableStateFlow(NetworkResult.Loading())
    val responseStockQuarterlyIncome:StateFlow<NetworkResult<List<StockQuarterlyIncome>>> =
        _responseStockQuarterlyIncome.asStateFlow()

    fun getStockSymbol(){
        viewModelScope.launch {
            _responseStock.value = apiFinnRepository.getStockSymbol()
        }
    }

    fun getCompanyProfile(symbol:String){
        viewModelScope.launch {
            _responseCompanyProfile.value = apiFinnRepository.getCompanyProfile(symbol = symbol)
        }
    }

    fun getNewsCompany(symbol: String){
        viewModelScope.launch {
            _responseNewsCompany.value = apiFinnRepository.getNewsCompany(symbol = symbol)
        }
    }

    fun getStockPriceQuote(symbol: String){
        viewModelScope.launch {
            _responseStockPriceQuote.value = apiFinnRepository.getStockPriceQuote(symbol = symbol)
        }
    }

    fun getStockMetric(symbol: String){
        viewModelScope.launch {
            _responseStockMetric.value = apiFinnRepository.getStockMetric(symbol = symbol)
        }
    }

    fun getStockQuarterlyIncome(symbol: String){
        viewModelScope.launch {
            _responseStockQuarterlyIncome.value = apiFinnRepository.getStockQuarterlyIncome(symbol = symbol)
        }
    }
}