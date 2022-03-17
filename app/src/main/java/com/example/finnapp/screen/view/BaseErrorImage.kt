package com.example.finnapp.screen.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.finnapp.R

@Composable
fun BaseErrorImage(
    message:String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
        )

        Image(
            painter = painterResource(id = R.drawable.browser),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)
        )

        Text(
            text = message,
            modifier = Modifier.padding(5.dp),
            color = Color.Red
        )
    }
}