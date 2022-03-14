package com.example.finnapp.navigation.host

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.finnapp.di.component.AppComponent
import com.example.finnapp.di.component.DaggerAppComponent
import com.example.finnapp.ui.theme.primaryBackground
import com.example.finnapp.ui.theme.secondaryBackground
import com.example.finnapp.navigation.host.buttonBar.ButtonBarDate
import com.example.finnapp.navigation.navGraph.stockNavGraph.stockNavGraph
import com.example.finnapp.navigation.navGraph.newsNavGraph.constants.RouteAndArgumentsNews
import com.example.finnapp.navigation.navGraph.newsNavGraph.newsNavGraph
import com.example.finnapp.navigation.navGraph.stockNavGraph.constants.RouteAndArgumentsStock.Route.STOCK_ROUTE

@ExperimentalMaterialApi
@Composable
fun BaseHost(
    navHostController: NavHostController,
    appComponent: AppComponent = DaggerAppComponent.create()
) {
    val idButtonBar = remember { mutableStateOf(ButtonBarDate.Stock) }

    Scaffold(
        bottomBar = {
            BottomNavigation(
                backgroundColor = primaryBackground,
                elevation = 8.dp
            ) {
                ButtonBarDate.values().forEach { item ->
                    BottomNavigationItem(
                        selected = idButtonBar.value == item,
                        onClick = {
                            idButtonBar.value = item

                            when(idButtonBar.value){
                                ButtonBarDate.Stock -> navHostController.navigate(STOCK_ROUTE)
                                ButtonBarDate.News -> navHostController.navigate(
                                    RouteAndArgumentsNews.Route.NEWS_ROUTE)
                            }
                        },
                        label = {
                            Text(text = item.name)
                        },
                        icon = {
                            Icon(
                                painter = painterResource(id = item.icon),
                                contentDescription = item.name,
                                modifier = Modifier.size(28.dp)
                            )
                        }, selectedContentColor = secondaryBackground,
                        unselectedContentColor = Color.Gray
                    )
                }
            }
        }
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = primaryBackground
        ) {
            NavHost(
                navController = navHostController,
                startDestination = STOCK_ROUTE,
                route = "route",
                builder = {
                    stockNavGraph(
                        navController = navHostController
                    )
                    newsNavGraph(
                        navController = navHostController,
                        appComponent = appComponent
                    )
                }
            )
        }
    }
}