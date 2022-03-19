package com.example.finnapp.screen.stockScreen.view.companyProfileView

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.finnapp.api.NetworkResult
import com.example.finnapp.api.model.stock.PriceUpdate
import com.example.finnapp.ui.theme.secondaryBackground

@Composable
fun PriceUpdateView(
   priceUpdate:NetworkResult<PriceUpdate>
) {
    when(priceUpdate){
        is NetworkResult.Error -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = priceUpdate.message.toString(),
                    color = Color.Red
                )
            }
        }
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
        is NetworkResult.Success -> {
            priceUpdate.data?.data?.let {
                Divider()
                Column(
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(text = "Last Price:")
                }

                LazyRow(content = {
                    items(it) { item ->
                        Text(
                            text = item.p.toString(),
                            modifier = Modifier.padding(5.dp)
                        )
                    }
                })
            }
            Divider()
        }
    }
}