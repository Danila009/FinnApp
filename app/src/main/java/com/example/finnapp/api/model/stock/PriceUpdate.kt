package com.example.finnapp.api.model.stock

import kotlinx.serialization.Serializable

@Serializable
data class PriceUpdate(
    val type:String? = null,
    val data:List<PriceUpdateData>? = null
)
