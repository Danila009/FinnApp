package com.example.finnapp.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.finnapp.navigation.host.BaseHost
import com.example.finnapp.ui.theme.FinnAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FinnAppTheme {
                BaseHost(
                    navHostController = rememberNavController()
                )
            }
        }
    }
}