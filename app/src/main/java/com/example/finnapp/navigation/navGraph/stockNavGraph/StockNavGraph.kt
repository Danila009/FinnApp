package com.example.finnapp.navigation.navGraph.stockNavGraph

import androidx.navigation.*
import androidx.navigation.compose.composable
import com.example.finnapp.di.component.DaggerAppComponent
import com.example.finnapp.navigation.navGraph.stockNavGraph.constants.RouteAndArgumentsStock.Argument.SYMBOL
import com.example.finnapp.navigation.navGraph.stockNavGraph.constants.RouteAndArgumentsStock.Argument.WEB_URL
import com.example.finnapp.navigation.navGraph.stockNavGraph.constants.RouteAndArgumentsStock.Route.STOCK_ROUTE
import com.example.finnapp.navigation.navGraph.stockNavGraph.constants.RouteScreenStock
import com.example.finnapp.screen.stockScreen.CompanyProfileScreen
import com.example.finnapp.screen.stockScreen.StockScreen
import com.example.finnapp.screen.stockScreen.WebScreen

fun NavGraphBuilder.stockNavGraph(
    navController: NavController
) {
    val stockViewModel = DaggerAppComponent.create()
        .stockViewModel()
    navigation(
        startDestination = RouteScreenStock.Stock.route,
        route = STOCK_ROUTE,
        builder = {
            composable(RouteScreenStock.Stock.route){
                StockScreen(
                    navController = navController,
                    stockViewModel = stockViewModel
                )
            }
            composable(
                RouteScreenStock.CompanyProfile.route,
                arguments = listOf(
                    navArgument(SYMBOL){
                        type = NavType.StringType
                    }
                )
            ){
                CompanyProfileScreen(
                    symbol = it.arguments?.getString(SYMBOL).toString(),
                    navController = navController,
                    stockViewModel = stockViewModel
                )
            }
            composable(
                RouteScreenStock.Web.route,
                arguments = listOf(
                    navArgument(WEB_URL){
                        type = NavType.StringType
                    }
                )
            ){
                WebScreen(
                    url = it.arguments?.getString(WEB_URL).toString()
                )
            }
        }
    )
}