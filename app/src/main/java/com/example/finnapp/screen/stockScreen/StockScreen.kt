package com.example.finnapp.screen.stockScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.example.finnapp.api.NetworkResult
import com.example.finnapp.api.model.stock.Stock
import com.example.finnapp.di.component.DaggerAppComponent
import com.example.finnapp.ui.theme.primaryBackground
import com.example.finnapp.ui.theme.secondaryBackground
import com.example.finnapp.utils.Converters.launchWhenCreated
import kotlinx.coroutines.flow.onEach

@Composable
fun StockScreen(
    navController: NavController
) {
    val lifecycleScope = LocalLifecycleOwner.current.lifecycleScope
    val stock:MutableState<NetworkResult<List<Stock>>> =
        remember { mutableStateOf(NetworkResult.Loading()) }

    LaunchedEffect(key1 = Unit, block = {
        val stockViewModel = DaggerAppComponent.create()
            .stockViewModel()

        stockViewModel.getStockSymbol()
        stockViewModel.autorApi.onEach {
            stock.value = it
        }.launchWhenCreated(lifecycleScope)

    })

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = primaryBackground,
                elevation = 8.dp,
                title = {},
                actions = {
                    TextButton(onClick = { /*TODO*/ }) {
                        Text(
                            text = "USD",
                            color = secondaryBackground
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
                LazyColumn(content = {
                    when(stock.value){
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
                                        text = stock.value.message.toString(),
                                        color = Color.Red
                                    )
                                }
                            }
                        }
                        is NetworkResult.Success -> {
                            items(stock.value.data!!){ item ->
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = item.description,
                                            modifier = Modifier.padding(5.dp)
                                        )

                                        Text(
                                            text = item.displaySymbol,
                                            modifier = Modifier.padding(5.dp)
                                        )
                                    }
                                    Divider()
                                }
                            }
                        }
                    }
                })
            }
        }
    )
}