package com.example.finnapp.navigation.navGraph.stockNavGraph.constants

sealed class RouteScreenStock(val route:String) {
    object Stock: RouteScreenStock("stock_screen")
}