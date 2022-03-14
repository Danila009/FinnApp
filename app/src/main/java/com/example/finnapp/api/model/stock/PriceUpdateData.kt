package com.example.finnapp.api.model.stock

import kotlinx.serialization.Serializable

@Serializable
data class PriceUpdateData(
    val c:List<String>? = null,
    val s:String? = null,
    val p:Double? = null,
    val t:Int? = null,
    val v:Double? = null
)
