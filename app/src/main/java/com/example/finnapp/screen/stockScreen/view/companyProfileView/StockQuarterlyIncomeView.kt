package com.example.finnapp.screen.stockScreen.view.companyProfileView

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.finnapp.api.NetworkResult
import com.example.finnapp.api.model.stock.StockQuarterlyIncome
import com.example.finnapp.ui.theme.primaryBackground
import com.example.finnapp.ui.theme.secondaryBackground

@Composable
fun StockQuarterlyIncomeView(
    stockQuarterlyIncome:NetworkResult<List<StockQuarterlyIncome>>
) {
    when(stockQuarterlyIncome){
        is NetworkResult.Loading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(5.dp),
                    color = secondaryBackground
                )
            }
        }
        is NetworkResult.Error -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stockQuarterlyIncome.message.toString(),
                    color = Color.Red
                )
            }
        }
        is NetworkResult.Success -> {
            LazyRow(content = {
                items(stockQuarterlyIncome.data!!){ item ->
                    Card(
                        modifier = Modifier
                            .padding(5.dp),
                        shape = AbsoluteRoundedCornerShape(10.dp),
                        elevation = 8.dp,
                        backgroundColor = primaryBackground
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = item.actual.toString(),
                                modifier = Modifier.padding(5.dp)
                            )

                            Text(
                                text = item.estimate.toString(),
                                modifier = Modifier.padding(5.dp)
                            )

                            Text(
                                text = item.period,
                                modifier = Modifier.padding(5.dp)
                            )
                        }
                    }
                }
            })
        }
    }
}