package com.example.finnapp.screen.stockScreen

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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.example.finnapp.api.NetworkResult
import com.example.finnapp.api.model.company.CompanyProfile
import com.example.finnapp.api.model.news.News
import com.example.finnapp.di.component.DaggerAppComponent
import com.example.finnapp.ui.theme.primaryBackground
import com.example.finnapp.ui.theme.secondaryBackground
import com.example.finnapp.utils.Converters
import com.example.finnapp.utils.Converters.launchWhenCreated
import kotlinx.coroutines.flow.onEach

@Composable
fun CompanyProfileScreen(
    navController: NavController,
    symbol:String
) {
    val lifecycleScope = LocalLifecycleOwner.current.lifecycleScope

    val companyProfile:MutableState<NetworkResult<CompanyProfile>>
    = remember { mutableStateOf(NetworkResult.Loading()) }

    val newsCompany:MutableState<NetworkResult<List<News>>> =
        remember { mutableStateOf(NetworkResult.Loading()) }

    LaunchedEffect(key1 = Unit, block = {
        val stockViewModel = DaggerAppComponent.create()
            .stockViewModel()

        stockViewModel.getCompanyProfile(symbol)
        stockViewModel.responseCompanyProfile.onEach {
            companyProfile.value = it
        }.launchWhenCreated(lifecycleScope)

        stockViewModel.getNewsCompany(symbol)
        stockViewModel.responseNewsCompany.onEach {
            newsCompany.value = it
        }.launchWhenCreated(lifecycleScope)
    })

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
                            }
                        }
                    }
                })
            }
        }
    )
}