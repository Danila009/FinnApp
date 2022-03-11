package com.example.finnapp.api.model.stock

data class StockMetric(
    val series: Series,
    val metricType:String,
    val symbol:String,
    val metric: Metric
)