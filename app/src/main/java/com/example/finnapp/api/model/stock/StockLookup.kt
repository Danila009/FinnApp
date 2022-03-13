package com.example.finnapp.api.model.stock

data class StockLookup(
    val count:Int,
    val result:List<StockLookupResult> = listOf()
)
