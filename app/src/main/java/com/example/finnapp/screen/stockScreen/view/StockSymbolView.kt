package com.example.finnapp.screen.stockScreen.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.navigation.NavController
import com.example.finnapp.api.NetworkResult
import com.example.finnapp.api.model.stock.Stock
import com.example.finnapp.api.model.stock.StockPriceQuote
import com.example.finnapp.navigation.navGraph.stockNavGraph.constants.RouteScreenStock
import com.example.finnapp.screen.stockScreen.view.animation.shimmer.CurrentPriceShimmer
import com.example.finnapp.screen.stockScreen.viewModel.StockViewModel
import com.example.finnapp.screen.view.BaseErrorView
import com.example.finnapp.utils.Converters.launchWhenCreated
import kotlinx.coroutines.flow.onEach

@Composable
fun StockSymbolView(
    stockViewModel:StockViewModel,
    lifecycleScope:LifecycleCoroutineScope,
    navController:NavController,
    item:Stock
) {
    val stockPriceQuote: MutableState<NetworkResult<StockPriceQuote>> = remember {
        mutableStateOf(NetworkResult.Loading())
    }

    LaunchedEffect(key1 = Unit, block = {
        stockViewModel.getStockPriceQuote(item.symbol).onEach {
            stockPriceQuote.value = it
        }.launchWhenCreated(lifecycleScope)
    })

    Column {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    navController.navigate(
                        RouteScreenStock.CompanyProfile.argument(
                            symbol = item.symbol
                        )
                    )
                },
        ) {
            Text(
                text = item.description,
                modifier = Modifier.padding(5.dp)
            )

            when(stockPriceQuote.value){
                is NetworkResult.Loading -> {
                    CurrentPriceShimmer()
                }
                is NetworkResult.Error -> {
                    BaseErrorView(message = stockPriceQuote.value.message.toString())
                }
                is NetworkResult.Success -> {
                    Text(
                        text = "Current price: ${stockPriceQuote.value.data?.c}",
                        modifier = Modifier.padding(5.dp)
                    )
                }
            }
        }
        Divider()
    }
}