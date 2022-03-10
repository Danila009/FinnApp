package com.example.finnapp.api.model.stock


import com.google.gson.annotations.SerializedName

data class Stock(
    @SerializedName("currency")
    val currency: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("displaySymbol")
    val displaySymbol: String = "",
    @SerializedName("figi")
    val figi: String = "",
    @SerializedName("mic")
    val mic: String = "",
    @SerializedName("symbol")
    val symbol: String = "",
    @SerializedName("type")
    val type: String = ""
)