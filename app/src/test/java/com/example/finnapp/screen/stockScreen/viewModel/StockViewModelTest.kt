package com.example.finnapp.screen.stockScreen.viewModel

import com.example.finnapp.api.FinnApi
import com.example.finnapp.api.model.stock.Stock
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.mockito.Mockito
import org.mockito.kotlin.mock
import retrofit2.Response

class StockViewModelTest {

    private val apiFinn = mock<FinnApi>()

    @Test
   fun `test get stock symbol return Stock list`():Unit = runBlocking {

        val stockListTest = listOf(
            Stock(), Stock(), Stock()
        )

        Mockito.`when`(apiFinn.getStockSymbol("US"))
           .thenReturn(Response.success(stockListTest))

       Assertions.assertEquals(
           stockListTest,
           apiFinn.getStockSymbol("US").body()
       )
   }
}