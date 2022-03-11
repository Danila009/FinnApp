package com.example.finnapp.api.model.stock

data class Annual(
    val currentRatio: List<CurrentRatio>,
    val netMargin: List<NetMargin>,
    val salesPerShare: List<SalesPerShare>
)