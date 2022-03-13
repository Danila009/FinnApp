package com.example.finnapp.screen.covid19Screen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finnapp.api.NetworkResult
import com.example.finnapp.api.model.covid19.Covid19
import com.example.finnapp.api.repository.ApiFinnRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class CovidViewModel @Inject constructor(
    private val apiFinnRepository: ApiFinnRepository
):ViewModel() {

    private val _responseCovid19:MutableStateFlow<NetworkResult<List<Covid19>>> =
        MutableStateFlow(NetworkResult.Loading())
    val responseCovid19:StateFlow<NetworkResult<List<Covid19>>> = _responseCovid19.asStateFlow()

    fun getCovid19(){
        viewModelScope.launch {
            _responseCovid19.value = apiFinnRepository.getCovid19()
        }
    }
}