package com.example.finnapp.screen.newsScreen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finnapp.api.NetworkResult
import com.example.finnapp.api.model.news.News
import com.example.finnapp.api.repository.ApiFinnRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewsViewModel @Inject constructor(
    private val apiFinnRepository: ApiFinnRepository
):ViewModel() {

    private val _responseNewsMarker:MutableStateFlow<NetworkResult<List<News>>> =
        MutableStateFlow(NetworkResult.Loading())
    val responseNewsMarker:StateFlow<NetworkResult<List<News>>> = _responseNewsMarker.asStateFlow()

    fun getMarkerNews(){
        viewModelScope.launch {
            _responseNewsMarker.value = apiFinnRepository.getMarkerNews()
        }
    }
}