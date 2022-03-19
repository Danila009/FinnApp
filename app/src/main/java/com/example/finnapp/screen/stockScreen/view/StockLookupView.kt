package com.example.finnapp.screen.stockScreen.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.navigation.NavController
import com.example.finnapp.api.NetworkResult
import com.example.finnapp.api.model.stock.StockLookup
import com.example.finnapp.screen.stockScreen.viewModel.StockViewModel
import com.example.finnapp.screen.view.BaseErrorImage
import com.example.finnapp.screen.view.ErrorNoInternet
import com.example.finnapp.screen.view.ServerError
import com.example.finnapp.screen.view.animation.shimmer.BaseListShimmer
import com.example.finnapp.ui.theme.secondaryBackground
import com.example.finnapp.utils.Constants

@Composable
fun StockLookupView(
    navController:NavController,
    lifecycleScope:LifecycleCoroutineScope,
    stockViewModel:StockViewModel,
    search:MutableState<String>,
    stockLookup:NetworkResult<StockLookup>,
    symbols: SnapshotStateList<String>
) {
    AnimatedVisibility(
        visible = search.value.isNotEmpty(),
        enter = expandHorizontally(),
        exit = shrinkHorizontally()
    ) {
        symbols.removeRange(0,symbols.size)
        LazyColumn(content = {
            when(stockLookup){
                is NetworkResult.Loading -> {
                    items(30) {
                        BaseListShimmer()
                    }
                }
                is NetworkResult.Error -> {
                    item {
                        val i = stockLookup.message.toString()
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
                    item {
                        Text(
                            text = "Result: ${stockLookup.data?.count}",
                            modifier = Modifier.padding(5.dp),
                            fontFamily = FontFamily.Cursive,
                            fontWeight = FontWeight.Bold,
                            color = secondaryBackground,
                            fontSize = 22.sp
                        )
                    }

                    items(stockLookup.data?.result!!){ item ->
                        symbols.add(item.symbol.toString())
                        StockLookupItemView(
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