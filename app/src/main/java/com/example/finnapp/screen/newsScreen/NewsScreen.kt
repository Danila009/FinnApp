package com.example.finnapp.screen.newsScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.example.finnapp.api.NetworkResult
import com.example.finnapp.api.model.news.News
import com.example.finnapp.screen.newsScreen.view.NewsExpandableCardView
import com.example.finnapp.screen.newsScreen.viewModel.NewsViewModel
import com.example.finnapp.screen.view.BaseErrorView
import com.example.finnapp.screen.view.animation.shimmer.BaseListShimmer
import com.example.finnapp.ui.theme.primaryBackground
import com.example.finnapp.ui.theme.secondaryBackground
import com.example.finnapp.utils.Converters.launchWhenCreated
import kotlinx.coroutines.flow.onEach

@SuppressLint("FlowOperatorInvokedInComposition")
@ExperimentalMaterialApi
@Composable
fun NewsScreen(
    newsViewModel: NewsViewModel,
    navController: NavController
) {
    val lifecycleScope = LocalLifecycleOwner.current.lifecycleScope

    val menuExpanded = remember { mutableStateOf(false) }
    val category = remember { mutableStateOf("All") }

    val news:MutableState<NetworkResult<List<News>>> =
        remember { mutableStateOf(NetworkResult.Loading()) }

    LaunchedEffect(key1 = category.value, block = {
        if (category.value == "All"){
            newsViewModel.getMarkerNews(
                ""
            )
        }else{
            newsViewModel.getMarkerNews(
                category.value
            )
        }
    })
    newsViewModel.responseNewsMarker.onEach {
        news.value = it
    }.launchWhenCreated(lifecycleScope)

    Column {
        LazyColumn(content = {
            item {
                TopAppBar(
                    backgroundColor = primaryBackground,
                    elevation = 8.dp,
                    title = {
                        Text(text = "Category:")
                    }, actions = {
                        TextButton(
                            onClick = {
                                menuExpanded.value = true
                            }
                        ) {
                            Text(
                                text = category.value,
                                color = secondaryBackground
                            )
                        }
                        Column {
                            DropdownMenu(
                                expanded = menuExpanded.value,
                                onDismissRequest = { menuExpanded.value = false },
                                content = {
                                    DropdownMenuItem(
                                        onClick = {
                                            category.value = "All"
                                            menuExpanded.value = false
                                        },
                                        content = {
                                            Text(text = "All")
                                        }
                                    )
                                    DropdownMenuItem(
                                        onClick = {
                                            category.value = "general"
                                            menuExpanded.value = false
                                        },
                                        content = {
                                            Text(text = "General")
                                        }
                                    )
                                    DropdownMenuItem(
                                        onClick = {
                                            category.value = "forex"
                                            menuExpanded.value = false
                                        },
                                        content = {
                                            Text(text = "Forex")
                                        }
                                    )
                                    DropdownMenuItem(
                                        onClick = {
                                            category.value = "crypto"
                                            menuExpanded.value = false
                                        },
                                        content = {
                                            Text(text = "Crypto")
                                        }
                                    )
                                    DropdownMenuItem(
                                        onClick = {
                                            category.value = "merger"
                                            menuExpanded.value = false
                                        },
                                        content = {
                                            Text(text = "Merger")
                                        }
                                    )
                                }
                            )
                        }
                    }
                )
            }
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
}