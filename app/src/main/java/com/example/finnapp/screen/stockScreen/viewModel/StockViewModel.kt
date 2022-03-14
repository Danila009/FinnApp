package com.example.finnapp.screen.stockScreen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finnapp.api.NetworkResult
import com.example.finnapp.api.model.company.CompanyProfile
import com.example.finnapp.api.model.news.News
import com.example.finnapp.api.model.stock.*
import com.example.finnapp.api.repository.ApiFinnRepository
import com.example.finnapp.api.webSocketListener.SocketListenerUtil
import com.example.finnapp.utils.Converters
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class StockViewModel @Inject constructor(
    private val apiFinnRepository: ApiFinnRepository
):ViewModel(){

    private val _responseStockLookup:MutableStateFlow<NetworkResult<StockLookup>> =
        MutableStateFlow(NetworkResult.Loading())
    val responseStockLookup:StateFlow<NetworkResult<StockLookup>> = _responseStockLookup.asStateFlow()

    private val _responseStockSymbol:MutableStateFlow<NetworkResult<List<Stock>>> =
        MutableStateFlow(NetworkResult.Loading())
    val responseStockSymbol:StateFlow<NetworkResult<List<Stock>>> = _responseStockSymbol.asStateFlow()

    private val _responseNewsCompany:MutableStateFlow<NetworkResult<List<News>>> = MutableStateFlow(NetworkResult.Loading())
    val responseNewsCompany:StateFlow<NetworkResult<List<News>>> = _responseNewsCompany.asStateFlow()

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

    fun getStockLookup(
        search:String = ""
    ){
        viewModelScope.launch {
            _responseStockLookup.value = apiFinnRepository.getStockLookup(
                search = search
            )
        }
    }

    fun getStockSymbol(){
        viewModelScope.launch {
            _responseStockSymbol.value = apiFinnRepository.getStockSymbol()
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

    fun getPriceUpdate(symbol: String):StateFlow<NetworkResult<PriceUpdate>>{
        val responsePriceUpdate: MutableStateFlow<NetworkResult<PriceUpdate>> =
            MutableStateFlow(NetworkResult.Loading())
        viewModelScope.launch {
            try {
                SocketListenerUtil.connect(sendSymbol = symbol)
                val stockPriceQuote = Converters.decodeFromString<PriceUpdate>(SocketListenerUtil.mResponseStockPriceQuote)
                responsePriceUpdate.value = NetworkResult.Success(stockPriceQuote)
            }catch (e:Exception){
                responsePriceUpdate.value = NetworkResult.Error(e.toString())
            }
        }
        return responsePriceUpdate
    }

    fun getStockPriceQuote(symbol: String):StateFlow<NetworkResult<StockPriceQuote>>{
        val responseStockPriceQuote: MutableStateFlow<NetworkResult<StockPriceQuote>> =
            MutableStateFlow(NetworkResult.Loading())
        viewModelScope.launch {
            responseStockPriceQuote.value = apiFinnRepository.getStockPriceQuote(symbol)
        }
        return responseStockPriceQuote.asStateFlow()
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

    override fun onCleared() {
        super.onCleared()
        SocketListenerUtil.clear()
    }
}