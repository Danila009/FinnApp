package com.example.finnapp.screen.view

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun BaseErrorView(
    message:String
) {
    Text(
        text = message,
        color = Color.Red
    )
}