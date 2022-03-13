package com.example.finnapp.navigation.navGraph.stockNavGraph.constants

sealed class RouteScreenStock(val route:String) {
    object Stock: RouteScreenStock("stock_screen")
    object CompanyProfile: RouteScreenStock("company_profile_screen?symbol={symbol}"){
        fun argument(
            symbol:String
        ):String = "company_profile_screen?symbol=$symbol"
    }
    object Web: RouteScreenStock("web_screen?url={url}"){
        fun argument(
            url:String
        ):String = "web_screen?url=$url"
    }
}