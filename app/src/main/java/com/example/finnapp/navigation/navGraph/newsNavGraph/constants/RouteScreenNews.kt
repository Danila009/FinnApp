package com.example.finnapp.navigation.navGraph.newsNavGraph.constants

sealed class RouteScreenNews(val route:String) {
    object News: RouteScreenNews("news_screen")
}