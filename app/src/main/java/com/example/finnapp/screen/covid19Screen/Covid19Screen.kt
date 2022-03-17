package com.example.finnapp.screen.covid19Screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.example.finnapp.api.NetworkResult
import com.example.finnapp.api.model.covid19.Covid19
import com.example.finnapp.screen.covid19Screen.viewModel.CovidViewModel
import com.example.finnapp.screen.view.BaseErrorImage
import com.example.finnapp.screen.view.BaseErrorView
import com.example.finnapp.screen.view.ErrorNoInternet
import com.example.finnapp.screen.view.ServerError
import com.example.finnapp.screen.view.animation.shimmer.BaseListShimmer
import com.example.finnapp.ui.theme.primaryBackground
import com.example.finnapp.utils.Constants
import com.example.finnapp.utils.Converters.launchWhenCreated
import kotlinx.coroutines.flow.onEach

@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
fun Covid19Screen(
    navController: NavController,
    covidViewModel: CovidViewModel
) {
    val lifecycleScope = LocalLifecycleOwner.current.lifecycleScope

    var covid:NetworkResult<List<Covid19>> by remember {
        mutableStateOf(NetworkResult.Loading())
    }

    covidViewModel.getCovid19()
    covidViewModel.responseCovid19.onEach {
        covid = it
    }.launchWhenCreated(lifecycleScope)

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = primaryBackground,
                elevation = 8.dp,
                title = {
                    Text(text = "Covid-19")
                }, navigationIcon = {
                    IconButton(onClick = { navController.previousBackStackEntry?.destination?.route?.let {
                        navController.navigate(it)
                    } }) {
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
                    when(covid){
                        is NetworkResult.Error -> {
                            item{
                                val i = covid.message.toString()
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
                        is NetworkResult.Loading -> {
                            items(30) {
                                BaseListShimmer()
                            }
                        }
                        is NetworkResult.Success -> {
                            items(covid.data!!){ item ->
                                Column(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = item.state.toString(),
                                        modifier = Modifier.padding(5.dp)
                                    )

                                    Row {
                                        Text(
                                            text = "All:",
                                            modifier = Modifier.padding(5.dp)
                                        )
                                        Text(
                                            text = item.case.toString(),
                                            modifier = Modifier.padding(5.dp)
                                        )
                                    }

                                    Row {
                                        Text(
                                            text = "Death:",
                                            modifier = Modifier.padding(5.dp)
                                        )
                                        Text(
                                            text = item.death.toString(),
                                            modifier = Modifier.padding(5.dp)
                                        )
                                    }

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        Text(
                                            text = item.updated.toString(),
                                            modifier = Modifier.padding(5.dp)
                                        )
                                    }

                                    Divider()
                                }
                            }
                        }
                    }
                })
            }
        }
    )
}