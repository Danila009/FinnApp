package com.example.finnapp.screen.stockScreen.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.navigation.NavController
import com.example.finnapp.api.NetworkResult
import com.example.finnapp.api.model.stock.Stock
import com.example.finnapp.screen.stockScreen.viewModel.StockViewModel
import com.example.finnapp.screen.view.BaseErrorImage
import com.example.finnapp.screen.view.ErrorNoInternet
import com.example.finnapp.screen.view.ServerError
import com.example.finnapp.screen.view.animation.shimmer.BaseListShimmer
import com.example.finnapp.utils.Constants

@Composable
fun StockSymbolView(
    navController: NavController,
    lifecycleScope: LifecycleCoroutineScope,
    stockViewModel: StockViewModel,
    search: MutableState<String>,
    stockSymbol:NetworkResult<List<Stock>>,
    symbols: SnapshotStateList<String>
) {
    AnimatedVisibility(
        visible = search.value.isEmpty(),
        enter = expandHorizontally(),
        exit = shrinkHorizontally()
    ) {
        symbols.removeRange(0,symbols.size)
        LazyColumn(content = {
            when(stockSymbol){
                is NetworkResult.Loading -> {
                    items(30) {
                        BaseListShimmer()
                    }
                }
                is NetworkResult.Error -> {
                    item {
                        val i = stockSymbol.message.toString()
                        when {
                            i == Constants.ERROR_NO_INTERNET -> {
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
                    items(stockSymbol.data!!){ item ->
                        symbols.add(item.symbol)
                        StockSymbolItemView(
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