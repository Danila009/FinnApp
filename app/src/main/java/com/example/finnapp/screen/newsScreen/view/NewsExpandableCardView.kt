package com.example.finnapp.screen.newsScreen.view

import androidx.compose.material.MaterialTheme
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.example.finnapp.api.model.news.News
import com.example.finnapp.navigation.navGraph.stockNavGraph.constants.RouteScreenStock
import com.example.finnapp.screen.view.animation.shimmer.ImageShimmer
import com.example.finnapp.ui.theme.primaryBackground
import com.example.finnapp.utils.Converters

@ExperimentalMaterialApi
@Composable
fun NewsExpandableCardView(
    navController: NavController,
    news: News,
) {
    var expandedState by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(
        targetValue = if (expandedState) 180f else 0f
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            ),
        onClick = {
            expandedState = !expandedState
        }, backgroundColor = primaryBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                SubcomposeAsyncImage(
                    modifier = Modifier
                        .size(100.dp)
                        .padding(10.dp),
                    model = news.image,
                    contentDescription = null,
                    loading = {
                        val stateCoil = painter.state
                        if (stateCoil is AsyncImagePainter.State.Loading
                            || stateCoil is AsyncImagePainter.State.Error) {
                            ImageShimmer(sizeImage = 100.dp)
                        } else {
                            SubcomposeAsyncImageContent()
                        }
                    }
                )

                Text(
                    modifier = Modifier
                        .weight(6f),
                    text = Converters.replaceRange(
                        news.headline,
                        71
                    ),
                    fontSize = MaterialTheme.typography.h6.fontSize,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                IconButton(
                    modifier = Modifier
                        .weight(1f)
                        .alpha(ContentAlpha.medium)
                        .rotate(rotationState),
                    onClick = {
                        expandedState = !expandedState
                    }) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Drop-Down Arrow"
                    )
                }
            }
            if (expandedState) {
                Text(
                    text = news.headline,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(3.dp)
                )

                Text(
                    text = news.summary,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(3.dp)
                )

                TextButton(
                    modifier = Modifier.padding(3.dp),
                    onClick = { navController.navigate(RouteScreenStock.Web.argument(
                    url = news.url
                )) }) {
                    Text(
                        text = "Web ->",
                        color = Color.White
                    )
                }
            }
        }
    }
}