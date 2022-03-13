package com.example.finnapp.screen.newsScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.example.finnapp.api.NetworkResult
import com.example.finnapp.api.model.news.News
import com.example.finnapp.di.component.DaggerAppComponent
import com.example.finnapp.screen.newsScreen.view.NewsExpandableCardView
import com.example.finnapp.screen.view.BaseErrorView
import com.example.finnapp.screen.view.animation.shimmer.BaseListShimmer
import com.example.finnapp.utils.Converters
import com.example.finnapp.utils.Converters.launchWhenCreated
import kotlinx.coroutines.flow.onEach

@ExperimentalMaterialApi
@Composable
fun NewsScreen(
    navController: NavController
) {
    val lifecycleScope = LocalLifecycleOwner.current.lifecycleScope

    val news:MutableState<NetworkResult<List<News>>> =
        remember { mutableStateOf(NetworkResult.Loading()) }

    LaunchedEffect(key1 = Unit, block = {
        val newsViewModel = DaggerAppComponent.create()
            .newsViewModel()

        newsViewModel.getMarkerNews()
        newsViewModel.responseNewsMarker.onEach {
            news.value = it
        }.launchWhenCreated(lifecycleScope)
    })

    LazyColumn(content = {
        when(news.value){
            is NetworkResult.Loading -> {
                items(30) {
                    BaseListShimmer()
                }
            }
            is NetworkResult.Error -> {
                item {
                    BaseErrorView(message = news.value.message.toString())
                }
            }
            is NetworkResult.Success -> {
                items(news.value.data!!){ item ->
                    Column {
                        NewsExpandableCardView(
                            navController = navController,
                            news = item
                        )
                        Divider()
                    }
                }
            }
        }
    })
}