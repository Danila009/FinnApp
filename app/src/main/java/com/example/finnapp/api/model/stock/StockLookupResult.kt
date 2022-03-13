package com.example.finnapp.api.model.stock

data class StockLookupResult(
    val description: String?,
    val displaySymbol: String?,
    val symbol: String?,
    val type: String?
)