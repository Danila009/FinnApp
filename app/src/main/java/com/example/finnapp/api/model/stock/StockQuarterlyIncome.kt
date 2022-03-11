package com.example.finnapp.api.model.stock

data class StockQuarterlyIncome(
    val `actual`: Double,
    val estimate: Double,
    val period: String,
    val symbol: String
)