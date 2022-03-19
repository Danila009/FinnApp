package com.example.finnapp.screen.stockScreen.view.companyProfileView

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.finnapp.api.NetworkResult
import com.example.finnapp.api.model.stock.StockPriceQuote
import com.example.finnapp.api.model.stock.StockQuarterlyIncome
import com.example.finnapp.ui.theme.secondaryBackground

@Composable
fun StockPriceQuoteView(
    stockPriceQuote:NetworkResult<StockPriceQuote>,
    stockQuarterlyIncome:NetworkResult<List<StockQuarterlyIncome>>
) {
    when(stockPriceQuote){
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Current price",
                    modifier = Modifier.padding(5.dp)
                )

                Text(
                    text = stockPriceQuote.data?.c.toString(),
                    modifier = Modifier.padding(5.dp)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "High price of the day",
                    modifier = Modifier.padding(5.dp)
                )

                Text(
                    text = stockPriceQuote.data?.h.toString(),
                    modifier = Modifier.padding(5.dp)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Low price of the day",
                    modifier = Modifier.padding(5.dp)
                )

                Text(
                    text = stockPriceQuote.data?.l.toString(),
                    modifier = Modifier.padding(5.dp)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Open price of the day",
                    modifier = Modifier.padding(5.dp)
                )

                Text(
                    text = stockPriceQuote.data?.o.toString(),
                    modifier = Modifier.padding(5.dp)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Previous close price",
                    modifier = Modifier.padding(5.dp)
                )

                Text(
                    text = stockPriceQuote.data?.pc.toString(),
                    modifier = Modifier.padding(5.dp)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Change",
                    modifier = Modifier.padding(5.dp)
                )

                Text(
                    text = stockPriceQuote.data?.t.toString(),
                    modifier = Modifier.padding(5.dp)
                )
            }
        }
    }

    stockQuarterlyIncome.data?.let {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Divider()
            Text(
                text = "Quarterly income:",
                modifier = Modifier.padding(5.dp)
            )
        }
    }
}