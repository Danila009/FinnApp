package com.example.finnapp.navigation.navGraph.covid19NavGraph.constants

sealed class RouteScreenGovid19(val route:String) {
    object Covid19Screen: RouteScreenGovid19("covid_19_screen")
}