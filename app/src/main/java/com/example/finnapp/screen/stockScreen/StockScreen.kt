package com.example.finnapp.screen.stockScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.example.finnapp.R
import com.example.finnapp.api.NetworkResult
import com.example.finnapp.api.model.stock.Stock
import com.example.finnapp.api.model.stock.StockLookup
import com.example.finnapp.navigation.navGraph.covid19NavGraph.constants.RouteScreenGovid19
import com.example.finnapp.screen.stockScreen.view.SearchView
import com.example.finnapp.screen.stockScreen.view.StockLookupView
import com.example.finnapp.screen.stockScreen.view.StockSymbolItemView
import com.example.finnapp.screen.stockScreen.view.StockSymbolView
import com.example.finnapp.screen.stockScreen.viewModel.StockViewModel
import com.example.finnapp.ui.theme.primaryBackground
import com.example.finnapp.ui.theme.secondaryBackground
import com.example.finnapp.utils.Constants.TESTING
import com.example.finnapp.utils.Converters.launchWhenCreated
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun StockScreen(
    navController: NavController,
    stockViewModel: StockViewModel? = null
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
        "US","BO","HK", "CA", "CN", "AX", "AT", "AS", "BA", "BC", "BR", "JK",
        "JO", "KQ", "HM", "V", "VN", "VS", "WA", "T", "SG"
    )

    val symbolTest = listOf(
        "AAPL",
        "AMZN",
        "MSFT",
        "BINANCE:BTCUSDT",
        "IC MARKETS:1"
    )

    val symbols = remember { mutableStateListOf("") }

    LaunchedEffect(key1 = Unit, block = {
        stockViewModel?.let {
            stockViewModel.responseStockSymbol.onEach {
                stockSymbol.value = it
            }.launchWhenCreated(lifecycleScope)

            stockViewModel.responseStockLookup.onEach {
                stockLookup.value = it
            }.launchWhenCreated(lifecycleScope)
        }
    })

    LaunchedEffect(
        key1 = symbols,
        block = {
            if (TESTING){
                symbols.removeRange(0,symbols.size)
                symbolTest.forEach {
                    symbols.add(it)
                }
                stockViewModel?.let {
                    stockViewModel.getPriceUpdate(
                        symbol = symbols
                    )
                }

            }else{
                if (search.value.isNotEmpty()){
                    stockViewModel?.let {
                        stockViewModel.getPriceUpdate(
                            symbol = symbols
                        )
                    }
                }else{
                    stockViewModel?.let {
                        stockViewModel.getPriceUpdate(
                            symbol = symbols
                        )
                    }
                }
            }
        }
    )

    if (refreshing){
        stockViewModel?.let {
            stockViewModel.getStockSymbol(exchange = exchange.value)
            stockViewModel.getStockLookup(search = search.value)
        }
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
        stockViewModel?.let {
            stockViewModel.getStockSymbol(exchange = exchange.value)
        }
    })

    LaunchedEffect(key1 = search.value, block = {
        stockViewModel?.let {
            stockViewModel.getStockLookup(search = search.value)
        }
    })

    Scaffold(
        topBar = {
            if (!TESTING){
                TopAppBar(
                    modifier = Modifier.testTag("BaseTopAppBar"),
                    backgroundColor = primaryBackground,
                    elevation = 8.dp,
                    title = {
                        SearchView(
                            search = search
                        )
                    },
                    actions = {
                        // Exchanges Button open menu
                        TextButton(
                            onClick = { menuExpanded.value = true },
                            modifier = Modifier.testTag("Text_Button_Open_Menu")
                        ) {
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
                        // Button open screen covid 19 information
                        IconButton(onClick = {
                            navController.navigate(RouteScreenGovid19.Covid19Screen.route)
                        }) {
                            Image(
                                painter = painterResource(id = R.drawable.covid),
                                contentDescription = null
                            )
                        }
                    }
                )
            }
        },
        content = {
            Surface(
                modifier = Modifier
                    .testTag("Base_Surface")
                    .fillMaxSize(),
                color = primaryBackground
            ) {
                SwipeRefresh(
                    state = rememberSwipeRefreshState(isRefreshing = refreshing),
                    onRefresh = {
                        refreshing = true
                    }
                ) {
                    if (TESTING){
                        LazyColumn(content = {
                            items(symbolTest) { item ->
                                stockViewModel?.let {
                                    StockSymbolItemView(
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
                            }
                        })
                    }else{
                        stockViewModel?.let {
                            StockSymbolView(
                                navController = navController,
                                lifecycleScope = lifecycleScope,
                                stockViewModel = stockViewModel,
                                search = search,
                                stockSymbol = stockSymbol.value,
                                symbols = symbols
                            )

                            StockLookupView(
                                navController = navController,
                                lifecycleScope = lifecycleScope,
                                stockViewModel = stockViewModel,
                                search = search,
                                stockLookup = stockLookup.value,
                                symbols = symbols
                            )
                        }
                    }
                }
            }
        }
    )
}