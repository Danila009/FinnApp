package com.example.finnapp.screen.stockScreen.view.companyProfileView

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.finnapp.api.NetworkResult
import com.example.finnapp.api.model.stock.StockMetric
import com.example.finnapp.ui.theme.primaryBackground
import com.example.finnapp.ui.theme.secondaryBackground

@Composable
fun StockMetricView(
    stockMetric:NetworkResult<StockMetric>
) {
    when(stockMetric){
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
                    text = stockMetric.message.toString(),
                    color = Color.Red
                )
            }
        }
        is NetworkResult.Success -> {
            Column {
                Divider()
                Text(
                    text = "Metric:",
                    modifier = Modifier.padding(5.dp),
                    fontWeight = FontWeight.Bold
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "10 Day Average Trading Volumeval",
                        modifier = Modifier.padding(5.dp)                                                    )

                    Text(
                        text = stockMetric.data?.metric?.a10DayAverageTradingVolumeval.toString(),
                        modifier = Modifier.padding(5.dp)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "52 Week High",
                        modifier = Modifier.padding(5.dp)                                                    )

                    Text(
                        text = stockMetric.data?.metric?.a52WeekHigh.toString(),
                        modifier = Modifier.padding(5.dp)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "52 Week Low",
                        modifier = Modifier.padding(5.dp)                                                    )

                    Text(
                        text = stockMetric.data?.metric?.a52WeekLow.toString(),
                        modifier = Modifier.padding(5.dp)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "52 Week Low Date",
                        modifier = Modifier.padding(5.dp)                                                    )

                    Text(
                        text = stockMetric.data?.metric?.a52WeekLowDate.toString(),
                        modifier = Modifier.padding(5.dp)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "52 Week Price Return Daily",
                        modifier = Modifier.padding(5.dp)                                                    )

                    Text(
                        text = stockMetric.data?.metric?.a52WeekPriceReturnDaily.toString(),
                        modifier = Modifier.padding(5.dp)
                    )
                }

                stockMetric.data?.series?.annual?.salesPerShare?.let {
                    Divider()
                    Text(
                        text = "sales Per Share:",
                        modifier = Modifier.padding(5.dp)
                    )

                    LazyRow(content = {
                        items(it){ item ->
                            Card(
                                modifier = Modifier
                                    .padding(5.dp),
                                shape = AbsoluteRoundedCornerShape(10.dp),
                                elevation = 8.dp,
                                backgroundColor = primaryBackground
                            ) {
                                Column {
                                    Text(
                                        text = item.period,
                                        modifier = Modifier.padding(
                                            5.dp
                                        )
                                    )
                                    Text(
                                        text = item.v.toString(),
                                        modifier = Modifier.padding(
                                            5.dp
                                        )
                                    )
                                }
                            }
                        }
                    })
                }

                stockMetric.data?.series?.annual?.currentRatio?.let {
                    Divider()
                    Text(
                        text = "Current ratio:",
                        modifier = Modifier.padding(5.dp)
                    )

                    LazyRow(content = {
                        items(it){ item ->
                            Card(
                                modifier = Modifier
                                    .padding(5.dp),
                                shape = AbsoluteRoundedCornerShape(10.dp),
                                elevation = 8.dp,
                                backgroundColor = primaryBackground
                            ) {
                                Column {
                                    Text(
                                        text = item.period,
                                        modifier = Modifier.padding(
                                            5.dp
                                        )
                                    )
                                    Text(
                                        text = item.v.toString(),
                                        modifier = Modifier.padding(
                                            5.dp
                                        )
                                    )
                                }
                            }
                        }
                    })
                }

                stockMetric.data?.series?.annual?.netMargin?.let {
                    Divider()
                    Text(
                        text = "Net margin:",
                        modifier = Modifier.padding(5.dp)
                    )

                    LazyRow(content = {
                        items(it){ item ->
                            Card(
                                modifier = Modifier
                                    .padding(5.dp),
                                shape = AbsoluteRoundedCornerShape(10.dp),
                                elevation = 8.dp,
                                backgroundColor = primaryBackground
                            ) {
                                Column {
                                    Text(
                                        text = item.period,
                                        modifier = Modifier.padding(
                                            5.dp
                                        )
                                    )
                                    Text(
                                        text = item.v.toString(),
                                        modifier = Modifier.padding(
                                            5.dp
                                        )
                                    )
                                }
                            }
                        }
                    })
                }

                Divider()
            }
        }
    }
}