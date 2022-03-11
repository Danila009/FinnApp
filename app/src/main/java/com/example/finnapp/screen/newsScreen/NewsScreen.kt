package com.example.finnapp.screen.newsScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.example.finnapp.api.NetworkResult
import com.example.finnapp.api.model.news.News
import com.example.finnapp.di.component.DaggerAppComponent
import com.example.finnapp.ui.theme.secondaryBackground
import com.example.finnapp.utils.Converters
import com.example.finnapp.utils.Converters.launchWhenCreated
import kotlinx.coroutines.flow.onEach

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
            //news.value = it
        }.launchWhenCreated(lifecycleScope)
    })

    LazyColumn(content = {
        when(news.value){
            is NetworkResult.Loading -> {
                item {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.padding(5.dp),
                            color = secondaryBackground
                        )
                        Text(
                            text = "Loading...",
                            modifier = Modifier.padding(5.dp),
                            color = secondaryBackground
                        )
                    }
                }
            }
            is NetworkResult.Error -> {
                item {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = news.value.message.toString(),
                            color = Color.Red
                        )
                    }
                }
            }
            is NetworkResult.Success -> {
                items(news.value.data!!){ item ->
                    Column {
                        Row {
                            SubcomposeAsyncImage(
                                modifier = Modifier
                                    .size(100.dp)
                                    .padding(10.dp),
                                model = item.image,
                                contentDescription = null,
                                loading = {
                                    val stateCoil = painter.state
                                    if (stateCoil is AsyncImagePainter.State.Loading
                                        || stateCoil is AsyncImagePainter.State.Error) {
                                        CircularProgressIndicator(
                                            color = secondaryBackground
                                        )
                                    } else {
                                        SubcomposeAsyncImageContent()
                                    }
                                }
                            )
                            Text(text = Converters.replaceRange(
                                item.headline,
                                101
                            ), modifier = Modifier.padding(5.dp))
                        }
                        Divider()
                    }
                }
            }
        }
    })
}