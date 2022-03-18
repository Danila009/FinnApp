package com.example.finnapp.screen.stockScreen

import android.annotation.SuppressLint
import android.app.DatePickerDialog
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
import androidx.compose.ui.platform.LocalContext
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
import com.example.finnapp.api.model.stock.*
import com.example.finnapp.navigation.navGraph.stockNavGraph.constants.RouteScreenStock
import com.example.finnapp.screen.newsScreen.view.NewsExpandableCardView
import com.example.finnapp.screen.stockScreen.view.companyProfileView.NewsView
import com.example.finnapp.screen.stockScreen.viewModel.StockViewModel
import com.example.finnapp.screen.view.BaseErrorImage
import com.example.finnapp.screen.view.ErrorNoInternet
import com.example.finnapp.screen.view.ServerError
import com.example.finnapp.screen.view.animation.shimmer.ImageShimmer
import com.example.finnapp.ui.theme.primaryBackground
import com.example.finnapp.ui.theme.secondaryBackground
import com.example.finnapp.utils.Constants
import com.example.finnapp.utils.Converters.launchWhenCreated
import kotlinx.coroutines.flow.onEach
import java.util.*

@ExperimentalMaterialApi
@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
fun CompanyProfileScreen(
    stockViewModel:StockViewModel,
    navController: NavController,
    symbol:String
) {
    val context = LocalContext.current
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

    var priceUpdate:NetworkResult<PriceUpdate> by
        remember { mutableStateOf(NetworkResult.Loading()) }

    var dateDialogStart by remember { mutableStateOf("1900-01-01") }
    var dateDialogEnd by remember { mutableStateOf("2100-01-01") }

    val calendar = Calendar.getInstance()
    val startDialog = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, monthOfYear)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        var month = monthOfYear.toString()
        var day = dayOfMonth.toString()
        if (month.length == 1){
            month = "0$monthOfYear"
        }

        if (day.length == 1){
            day = "0$dayOfMonth"
        }

        dateDialogStart = "$year-$month-$day"
    }

    val endDialog = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, monthOfYear)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        var month = monthOfYear.toString()
        var day = dayOfMonth.toString()
        if (month.length == 1){
            month = "0$monthOfYear"
        }

        if (day.length == 1){
            day = "0$dayOfMonth"
        }

        dateDialogEnd = "$year-$month-$day"
    }

    LaunchedEffect(key1 = Unit, block = {
        stockViewModel.getCompanyProfile(symbol)
        stockViewModel.responseCompanyProfile.onEach {
            companyProfile.value = it
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

    LaunchedEffect(
        key1 = dateDialogStart,
        key2 = dateDialogEnd ,
        block = {
            stockViewModel.getNewsCompany(
                symbol,
                startData = dateDialogStart,
                endData = dateDialogEnd
            )
            stockViewModel.responseNewsCompany.onEach {
                newsCompany.value = it
            }.launchWhenCreated(lifecycleScope)
        }
    )

    stockViewModel.getPriceUpdateItem(symbol).onEach {
        priceUpdate = it
    }.launchWhenCreated(lifecycleScope)

    stockViewModel.getStockPriceQuote(symbol).onEach {
        stockPriceQuote = it
    }.launchWhenCreated(lifecycleScope)

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
                                val i = companyProfile.value.message.toString()
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
                                                        ImageShimmer(
                                                            sizeImage = 200.dp
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
                                }
                            }

                            when(priceUpdate){
                                is NetworkResult.Error -> {
                                    item {
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
                                }
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
                                        }
                                    }
                                }
                                is NetworkResult.Success -> {
                                    priceUpdate.data?.data?.let {
                                        item {
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
                                    }
                                    item {
                                        Divider()
                                    }
                                }
                            }

                            item {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
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

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        Text(
                                            text = companyProfile.value.data!!.ipo.toString(),
                                            modifier = Modifier.padding(5.dp)
                                        )
                                    }
                                    Divider()
                                }

                                companyProfile.value.data?.weburl?.let {
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
                                    Divider()
                                }
                            }
                        }
                    }
                    when(newsCompany.value){
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
                                NewsView(
                                    companyProfile = companyProfile.value,
                                    startDialog = startDialog,
                                    endDialog = endDialog,
                                    dateDialogStart = dateDialogStart,
                                    dateDialogEnd = dateDialogEnd,
                                    calendar = calendar
                                )
                            }

                            items(newsCompany.value.data!!){ item ->
                                Column {
                                    NewsExpandableCardView(
                                        navController = navController,
                                        news = item
                                    )
                                    Divider()
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