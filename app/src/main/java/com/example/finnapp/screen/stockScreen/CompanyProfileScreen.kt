package com.example.finnapp.screen.stockScreen

import android.os.CountDownTimer
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.example.finnapp.api.NetworkResult
import com.example.finnapp.api.model.company.CompanyProfile
import com.example.finnapp.api.model.news.News
import com.example.finnapp.api.model.stock.StockMetric
import com.example.finnapp.api.model.stock.StockPriceQuote
import com.example.finnapp.api.model.stock.StockQuarterlyIncome
import com.example.finnapp.navigation.navGraph.stockNavGraph.constants.RouteScreenStock
import com.example.finnapp.screen.stockScreen.viewModel.StockViewModel
import com.example.finnapp.ui.theme.primaryBackground
import com.example.finnapp.ui.theme.secondaryBackground
import com.example.finnapp.utils.Converters
import com.example.finnapp.utils.Converters.launchWhenCreated
import kotlinx.coroutines.flow.onEach

@Composable
fun CompanyProfileScreen(
    stockViewModel:StockViewModel,
    navController: NavController,
    symbol:String
) {
    val lifecycleScope = LocalLifecycleOwner.current.lifecycleScope

    val companyProfile:MutableState<NetworkResult<CompanyProfile>>
    = remember { mutableStateOf(NetworkResult.Loading()) }

    val newsCompany:MutableState<NetworkResult<List<News>>> =
        remember { mutableStateOf(NetworkResult.Loading()) }

    var stockMetric:NetworkResult<StockMetric> by
        remember { mutableStateOf(NetworkResult.Loading()) }

    var stockQuarterlyIncome:NetworkResult<List<StockQuarterlyIncome>> by
        remember { mutableStateOf(NetworkResult.Loading()) }

    var stockPriceQuote:NetworkResult<StockPriceQuote> by
    remember { mutableStateOf(NetworkResult.Loading()) }

    var timeCheck by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit, block = {

        stockViewModel.getCompanyProfile(symbol)
        stockViewModel.responseCompanyProfile.onEach {
            companyProfile.value = it
        }.launchWhenCreated(lifecycleScope)

        stockViewModel.getNewsCompany(symbol)
        stockViewModel.responseNewsCompany.onEach {
            newsCompany.value = it
        }.launchWhenCreated(lifecycleScope)

        stockViewModel.getStockMetric(symbol)
        stockViewModel.responseStockMetric.onEach {
            stockMetric = it
        }.launchWhenCreated(lifecycleScope)

        stockViewModel.getStockQuarterlyIncome(symbol)
        stockViewModel.responseStockQuarterlyIncome.onEach {
            stockQuarterlyIncome = it
        }.launchWhenCreated(lifecycleScope)
    })

    val time = object : CountDownTimer(100,100){
        override fun onTick(p0: Long) = Unit

        override fun onFinish() {
            stockViewModel.getStockPriceQuote(symbol)
            stockViewModel.responseStockPriceQuote.onEach {
                stockPriceQuote = it
                timeCheck = true
            }.launchWhenCreated(lifecycleScope)
        }
    }

    if (timeCheck){

    }else{
        time.start()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = primaryBackground,
                title = {
                    Text(text = companyProfile.value.data?.name.toString())
                }, navigationIcon = {
                    IconButton(onClick = {
                        navController.previousBackStackEntry?.destination?.route?.let {
                            navController.navigate(it)
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = null
                        )
                    }
                }
            )
        }, content = {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = primaryBackground
            ) {
                LazyColumn(content = {
                    when(companyProfile.value){
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
                                        text = companyProfile.value.message.toString(),
                                        color = Color.Red
                                    )
                                }
                            }
                        }
                        is NetworkResult.Success -> {
                            item {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    companyProfile.value.data?.logo?.let {
                                        if (it.isNotEmpty()){
                                            SubcomposeAsyncImage(
                                                modifier = Modifier
                                                    .size(200.dp)
                                                    .padding(10.dp),
                                                model = companyProfile.value.data?.logo!!,
                                                contentDescription = null,
                                                loading = {
                                                    val stateCoil = painter.state
                                                    if (stateCoil is AsyncImagePainter.State.Loading
                                                        || stateCoil is AsyncImagePainter.State.Error) {
                                                        CircularProgressIndicator(
                                                            color = secondaryBackground
                                                        )
                                                    } else {
                                                        SubcomposeAsyncImageContent()
                                                    }
                                                }
                                            )
                                        }
                                    }

                                    Row {
                                        Text(
                                            text = "Рыночная капитализация: ",
                                            modifier = Modifier.padding(5.dp)
                                        )

                                        Text(
                                            text = companyProfile.value.data?.marketCapitalization.toString(),
                                            modifier = Modifier.padding(5.dp)
                                        )
                                    }

                                    stockPriceQuote.data?.let {
                                        Column(
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Divider()
                                            Text(
                                                text = "Stock price:",
                                                modifier = Modifier.padding(5.dp)
                                            )
                                        }
                                    }

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

                                    when(newsCompany.value){
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
                                                Text(
                                                    text = "Loading...",
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
                                                    text = companyProfile.value.message.toString(),
                                                    color = Color.Red
                                                )
                                            }
                                        }
                                        is NetworkResult.Success -> {
                                            LazyRow(content = {
                                                items(newsCompany.value.data!!){ item ->
                                                    Card(
                                                        backgroundColor = primaryBackground,
                                                        elevation = 8.dp,
                                                        shape = AbsoluteRoundedCornerShape(15.dp),
                                                        modifier = Modifier
                                                            .padding(5.dp)
                                                            .width(250.dp)
                                                            .height(150.dp),
                                                    ) {
                                                        Row {
                                                            item.image.let {
                                                                if (it.isNotEmpty()){
                                                                    SubcomposeAsyncImage(
                                                                        modifier = Modifier
                                                                            .size(100.dp)
                                                                            .padding(10.dp),
                                                                        model = it,
                                                                        contentDescription = null,
                                                                        loading = {
                                                                            val stateCoil = painter.state
                                                                            if (stateCoil is AsyncImagePainter.State.Loading
                                                                                || stateCoil is AsyncImagePainter.State.Error) {
                                                                                CircularProgressIndicator(
                                                                                    color = secondaryBackground
                                                                                )
                                                                            } else {
                                                                                SubcomposeAsyncImageContent()
                                                                            }
                                                                        }
                                                                    )
                                                                }
                                                            }
                                                            Text(
                                                                text = Converters.replaceRange(
                                                                    item.headline, 51),
                                                                modifier = Modifier.padding(5.dp)
                                                            )
                                                        }
                                                    }
                                                }
                                            })
                                        }
                                    }

                                    Divider()

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        Text(
                                            text = companyProfile.value.data!!.ipo.toString(),
                                            modifier = Modifier.padding(5.dp)
                                        )
                                    }
                                }

                                companyProfile.value.data?.weburl?.let {
                                    Divider()
                                    TextButton(
                                        modifier = Modifier.padding(5.dp),
                                        onClick = { navController.navigate(RouteScreenStock.Web.argument(
                                        url = it
                                    )) }) {
                                        Text(
                                            text = "Web ->",
                                            color = secondaryBackground
                                        )
                                    }
                                }
                            }
                        }
                    }
                    item {
                        Spacer(modifier = Modifier
                            .fillMaxWidth()
                            .height(70.dp)
                        )
                    }
                })
            }
        }
    )
}