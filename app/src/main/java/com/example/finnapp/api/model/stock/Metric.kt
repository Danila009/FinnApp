package com.example.finnapp.api.model.stock

import com.google.gson.annotations.SerializedName

data class Metric(
    @SerializedName("10DayAverageTradingVolume")
    val a10DayAverageTradingVolumeval:Double,
    @SerializedName("52WeekHigh")
    val a52WeekHigh:Double,
    @SerializedName("52WeekLow")
    val a52WeekLow:Double,
    @SerializedName("52WeekLowDate")
    val a52WeekLowDate:String,
    @SerializedName("52WeekPriceReturnDaily")
    val a52WeekPriceReturnDaily:Double,
    val beta:String
)
