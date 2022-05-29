package com.example.finnapp.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.navigation.compose.rememberNavController
import com.example.finnapp.api.webSocketListener.SocketListenerUtil
import com.example.finnapp.navigation.host.BaseHost
import com.example.finnapp.ui.theme.FinnAppTheme

@ExperimentalMaterialApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Web socket connection
        SocketListenerUtil.connect()

        setContent {
            //Base Theme
            FinnAppTheme {
                //Base Nav Host
                BaseHost(
                    navHostController = rememberNavController()
                )
            }
        }
    }
}