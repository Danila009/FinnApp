package com.example.finnapp.screen.stockScreen

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.example.finnapp.R
import com.example.finnapp.api.NetworkResult
import com.example.finnapp.api.model.stock.Stock
import com.example.finnapp.api.model.stock.StockLookup
import com.example.finnapp.navigation.navGraph.covid19NavGraph.constants.RouteScreenGovid19
import com.example.finnapp.screen.stockScreen.view.SearchView
import com.example.finnapp.screen.stockScreen.view.StockLookupView
import com.example.finnapp.screen.stockScreen.view.StockSymbolView
import com.example.finnapp.screen.view.animation.shimmer.BaseListShimmer
import com.example.finnapp.screen.stockScreen.viewModel.StockViewModel
import com.example.finnapp.screen.view.BaseErrorView
import com.example.finnapp.ui.theme.primaryBackground
import com.example.finnapp.ui.theme.secondaryBackground
import com.example.finnapp.utils.Converters.launchWhenCreated
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@SuppressLint("FlowOperatorInvokedInComposition", "CoroutineCreationDuringComposition")
@Composable
fun StockScreen(
    navController: NavController,
    stockViewModel: StockViewModel
) {
    val lifecycleScope = LocalLifecycleOwner.current.lifecycleScope
    val scope = rememberCoroutineScope()
    val search = remember { mutableStateOf("") }
    val stockLookup:MutableState<NetworkResult<StockLookup>> =
        remember { mutableStateOf(NetworkResult.Loading()) }

    val stockSymbol:MutableState<NetworkResult<List<Stock>>> =
        remember { mutableStateOf(NetworkResult.Loading()) }

    var refreshing by remember { mutableStateOf(false) }

    stockViewModel.responseStockSymbol.onEach {
        stockSymbol.value = it
    }.launchWhenCreated(lifecycleScope)

    stockViewModel.responseStockLookup.onEach {
        stockLookup.value = it
    }.launchWhenCreated(lifecycleScope)

    if (refreshing){
        stockViewModel.getStockSymbol()
        stockViewModel.getStockLookup(search = search.value)
        scope.launch {
            delay(1000L)
            if (
                stockLookup.value !is NetworkResult.Success
                || stockSymbol.value !is NetworkResult.Success
            )
                refreshing = false
        }
    }

    LaunchedEffect(key1 = Unit, block = {
        stockViewModel.getStockSymbol()
    })

    LaunchedEffect(key1 = search.value, block = {
        stockViewModel.getStockLookup(search = search.value)
    })

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = primaryBackground,
                elevation = 8.dp,
                title = {
                    SearchView(
                        search = search
                    )
                },
                actions = {
                    TextButton(onClick = { /*TODO*/ }) {
                        Text(
                            text = "USD",
                            color = secondaryBackground
                        )
                    }
                }, navigationIcon = {
                    IconButton(onClick = { navController.navigate(RouteScreenGovid19.Covid19Screen.route) }) {
                        Image(
                            painter = painterResource(id = R.drawable.covid),
                            contentDescription = null
                        )
                    }
                }
            )
        },
        content = {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = primaryBackground
            ) {
                SwipeRefresh(
                    state = rememberSwipeRefreshState(isRefreshing = refreshing),
                    onRefresh = {
                        refreshing = true
                    }
                ) {
                    AnimatedVisibility(
                        visible = search.value.isEmpty(),
                        enter = expandHorizontally(),
                        exit = shrinkHorizontally()
                    ) {
                        LazyColumn(content = {
                            when(stockSymbol.value){
                                is NetworkResult.Loading -> {
                                    items(30) {
                                        BaseListShimmer()
                                    }
                                }
                                is NetworkResult.Error -> {
                                    item {
                                        BaseErrorView(
                                            message = stockSymbol.value.message.toString()
                                        )
                                    }
                                }
                                is NetworkResult.Success -> {
                                    items(stockSymbol.value.data!!){ item ->
                                        StockSymbolView(
                                            stockViewModel = stockViewModel,
                                            lifecycleScope = lifecycleScope,
                                            navController = navController,
                                            item = item
                                        )
                                    }
                                }
                            }
                        })
                    }

                    AnimatedVisibility(
                        visible = search.value.isNotEmpty(),
                        enter = expandHorizontally(),
                        exit = shrinkHorizontally()
                    ) {
                        LazyColumn(content = {
                            when(stockLookup.value){
                                is NetworkResult.Loading -> {
                                    items(30) {
                                        BaseListShimmer()
                                    }
                                }
                                is NetworkResult.Error -> {
                                    item {
                                        BaseErrorView(message = stockLookup.value.message.toString())
                                    }
                                }
                                is NetworkResult.Success -> {
                                    item {
                                        Text(
                                            text = "Result: ${stockLookup.value.data?.count}",
                                            modifier = Modifier.padding(5.dp),
                                            fontFamily = FontFamily.Cursive,
                                            fontWeight = FontWeight.Bold,
                                            color = secondaryBackground,
                                            fontSize = 22.sp
                                        )
                                    }

                                    items(stockLookup.value.data?.result!!){ item ->
                                        StockLookupView(
                                            stockViewModel = stockViewModel,
                                            lifecycleScope = lifecycleScope,
                                            navController = navController,
                                            item = item
                                        )
                                    }
                                }
                            }
                        })
                    }
                }
            }
        }
    )
}