package com.example.finnapp.screen.stockScreen

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import com.example.finnapp.api.model.stock.PriceUpdate
import com.example.finnapp.api.model.stock.Stock
import com.example.finnapp.api.model.stock.StockLookup
import com.example.finnapp.api.model.stock.StockPriceQuote
import com.example.finnapp.navigation.navGraph.covid19NavGraph.constants.RouteScreenGovid19
import com.example.finnapp.screen.stockScreen.view.SearchView
import com.example.finnapp.screen.stockScreen.view.StockLookupView
import com.example.finnapp.screen.stockScreen.view.StockSymbolView
import com.example.finnapp.screen.view.animation.shimmer.BaseListShimmer
import com.example.finnapp.screen.stockScreen.viewModel.StockViewModel
import com.example.finnapp.screen.view.BaseErrorImage
import com.example.finnapp.screen.view.ErrorNoInternet
import com.example.finnapp.screen.view.ServerError
import com.example.finnapp.ui.theme.primaryBackground
import com.example.finnapp.ui.theme.secondaryBackground
import com.example.finnapp.utils.Constants.ERROR_NO_INTERNET
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

    val menuExpanded = remember { mutableStateOf(false) }

    val exchange = remember { mutableStateOf("US") }

    val exchanges = listOf(
        "US","BO","HK", "CA", "CN", "AX", "AT", "AS", "BA", "BC", "BR", "JK", "JO", "KQ", "HM", "V", "VN", "VS", "WA", "T", "SG"
    )

    val testing = false

    val a = listOf(
        "AAPL",
        "AMZN",
        "MSFT",
        "BINANCE:BTCUSDT",
        "IC MARKETS:1"
    )

    LaunchedEffect(key1 = Unit, block = {
        stockViewModel.responseStockSymbol.onEach {
            stockSymbol.value = it
        }.launchWhenCreated(lifecycleScope)

        stockViewModel.responseStockLookup.onEach {
            stockLookup.value = it
        }.launchWhenCreated(lifecycleScope)
    })

    LaunchedEffect(
        key1 = stockLookup.value,
        key2 = stockSymbol.value,
        block = {
            if (testing){
                val i = ArrayList<String>()
                a.forEach {
                    i.add(it)
                }
                stockViewModel.getPriceUpdate(
                    symbol = i
                )

            }else{
                if (search.value.isNotEmpty()){
                    val i = ArrayList<String>()
                    stockLookup.value.data?.result?.forEach {
                        i.add(it.symbol.toString())
                    }
                    stockViewModel.getPriceUpdate(
                        symbol = i
                    )
                }else{
                    val i = ArrayList<String>()
                    stockSymbol.value.data?.forEach {
                        i.add(it.symbol)
                    }
                    stockViewModel.getPriceUpdate(
                        symbol = i
                    )
                }
            }
        }
    )

    if (refreshing){
        stockViewModel.getStockSymbol(exchange = exchange.value)
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

    LaunchedEffect(key1 = exchange.value, block = {
        stockViewModel.getStockSymbol(exchange = exchange.value)
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
                    TextButton(onClick = { menuExpanded.value = true }) {
                        Text(
                            text = exchange.value,
                            color = secondaryBackground
                        )
                    }
                    Column {
                        DropdownMenu(
                            expanded = menuExpanded.value,
                            onDismissRequest = { menuExpanded.value = false },
                            content = {
                                exchanges.forEach { item ->
                                    DropdownMenuItem(onClick = {
                                        menuExpanded.value = false
                                        stockSymbol.value = NetworkResult.Loading()
                                        exchange.value = item
                                    }) {
                                        Text(text = item)
                                    }
                                }
                            }
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
                    if (testing){
                        LazyColumn(content = {
                            items(a) { item ->
                                StockSymbolView(
                                    stockViewModel = stockViewModel,
                                    lifecycleScope = lifecycleScope,
                                    navController = navController,
                                    item = Stock(
                                        displaySymbol = item,
                                        symbol = item,
                                        description = item
                                    )
                                )
                            }
                        })
                    }else{
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
                                            val i = stockSymbol.value.message.toString()
                                            when {
                                                i == ERROR_NO_INTERNET -> {
                                                    ErrorNoInternet()
                                                }
                                                i.contains("4") -> {
                                                    ServerError(
                                                        message = i
                                                    )
                                                }
                                                i.contains("5") -> {
                                                    ServerError(
                                                        message = i
                                                    )
                                                }
                                                else -> {
                                                    BaseErrorImage(
                                                        message = i
                                                    )
                                                }
                                            }
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
                                            val i = stockLookup.value.message.toString()
                                            when {
                                                i == ERROR_NO_INTERNET -> {
                                                    ErrorNoInternet()
                                                }
                                                i.contains("4") -> {
                                                    ServerError(
                                                        message = i
                                                    )
                                                }
                                                i.contains("5") -> {
                                                    ServerError(
                                                        message = i
                                                    )
                                                }
                                                else -> {
                                                    BaseErrorImage(
                                                        message = i
                                                    )
                                                }
                                            }
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
        }
    )
}