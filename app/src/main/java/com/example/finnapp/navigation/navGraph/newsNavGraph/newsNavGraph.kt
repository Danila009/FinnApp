package com.example.finnapp.navigation.navGraph.newsNavGraph

import androidx.compose.material.ExperimentalMaterialApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.finnapp.di.component.AppComponent
import com.example.finnapp.di.component.DaggerAppComponent
import com.example.finnapp.navigation.navGraph.newsNavGraph.constants.RouteAndArgumentsNews.Route.NEWS_ROUTE
import com.example.finnapp.navigation.navGraph.newsNavGraph.constants.RouteScreenNews
import com.example.finnapp.screen.newsScreen.NewsScreen

@ExperimentalMaterialApi
fun NavGraphBuilder.newsNavGraph(
    navController: NavController,
    appComponent: AppComponent
) {
    navigation(
        startDestination = RouteScreenNews.News.route,
        route = NEWS_ROUTE,
        builder = {
            composable(RouteScreenNews.News.route){
                NewsScreen(
                    navController = navController,
                    newsViewModel = appComponent.newsViewModel()
                )
            }
        }
    )
}