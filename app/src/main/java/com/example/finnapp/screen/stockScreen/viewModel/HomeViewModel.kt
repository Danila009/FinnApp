package com.example.finnapp.screen.stockScreen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finnapp.api.NetworkResult
import com.example.finnapp.api.model.stock.Stock
import com.example.finnapp.api.repository.ApiFinnRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val apiStockRepository: ApiFinnRepository
):ViewModel(){

    private val _autorResponse:MutableStateFlow<NetworkResult<List<Stock>>> = MutableStateFlow(NetworkResult.Loading())
    val autorApi:StateFlow<NetworkResult<List<Stock>>> = _autorResponse.asStateFlow()

    fun getStockSymbol(){
        viewModelScope.launch {
            _autorResponse.value = apiStockRepository.getStockSymbol()
        }
    }
}