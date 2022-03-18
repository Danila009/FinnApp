package com.example.finnapp.api.model.stock

import kotlinx.serialization.Serializable

@Serializable
data class PriceUpdateData(
    val p:Double? = null,
    val s:String? = null
)
