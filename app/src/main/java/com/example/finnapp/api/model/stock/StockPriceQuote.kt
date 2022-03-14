package com.example.finnapp.api.model.stock

import kotlinx.serialization.Serializable

@Serializable
data class StockPriceQuote(
    val c: Double? = 0.1,
    val h: Double? = 0.1,
    val l: Double? = 0.1,
    val o: Double? = 0.1,
    val pc: Double? = 0.1,
    val t: Double? = 0.1
)