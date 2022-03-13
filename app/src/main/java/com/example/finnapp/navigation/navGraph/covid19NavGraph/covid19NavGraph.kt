package com.example.finnapp.navigation.navGraph.covid19NavGraph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.finnapp.di.component.AppComponent
import com.example.finnapp.navigation.navGraph.covid19NavGraph.constants.RouteAndArgumentsGovid19.Route.COVID_19_ROUTE
import com.example.finnapp.navigation.navGraph.covid19NavGraph.constants.RouteScreenGovid19
import com.example.finnapp.screen.covid19Screen.Covid19Screen

fun NavGraphBuilder.covid19NavGraph(
    navController: NavController,
    appComponent: AppComponent
) {
    navigation(
        startDestination = RouteScreenGovid19.Covid19Screen.route,
        route = COVID_19_ROUTE,
        builder = {
            composable(RouteScreenGovid19.Covid19Screen.route){
                Covid19Screen(
                    navController = navController,
                    covidViewModel = appComponent.covidViewModel()
                )
            }
        }
    )
}