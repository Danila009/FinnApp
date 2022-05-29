package com.example.finnapp.screen.stockScreen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class StockScreenTestUi {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun before(){
        composeTestRule.setContent {
            StockScreen(
                navController = rememberNavController(),
                stockViewModel = null
            )
        }
    }

    @Test
    fun baseTopAppBar(){
        val baseTopAppBar =
            composeTestRule.onNode(hasTestTag("BaseTopAppBar"), useUnmergedTree = true)

        baseTopAppBar.assertIsDisplayed()
    }

    @Test
    fun searchTextField(){
        val searchTextField =
            composeTestRule.onNode(hasTestTag("SearchTextField"), useUnmergedTree = true)

        searchTextField.assertIsDisplayed()
    }

    @Test
    fun textButtonOpenMenu(){
        val textButtonOpenMenu =
            composeTestRule.onNode(hasTestTag("Text_Button_Open_Menu"), useUnmergedTree = true)

        textButtonOpenMenu.assertIsDisplayed()
        textButtonOpenMenu.performClick()
    }

    @Test
    fun baseSurface(){
        val baseSurface =
            composeTestRule.onNode(hasTestTag("Base_Surface"), useUnmergedTree = true)

        baseSurface.assertIsDisplayed()
    }
}