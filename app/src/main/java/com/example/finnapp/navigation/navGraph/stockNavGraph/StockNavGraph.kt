package com.example.finnapp.navigation.navGraph.stockNavGraph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.finnapp.navigation.navGraph.stockNavGraph.constants.RouteAndArgumentsStock.Route.STOCK_ROUTE
import com.example.finnapp.navigation.navGraph.stockNavGraph.constants.RouteScreenStock
import com.example.finnapp.screen.stockScreen.StockScreen

fun NavGraphBuilder.stockNavGraph(
    navController: NavController
) {
    navigation(
        startDestination = RouteScreenStock.Stock.route,
        route = STOCK_ROUTE,
        builder = {
            composable(RouteScreenStock.Stock.route){
                StockScreen(
                    navController = navController
                )
            }
        }
    )
}