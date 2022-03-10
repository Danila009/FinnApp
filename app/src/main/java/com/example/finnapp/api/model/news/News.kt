package com.example.finnapp.api.model.news


import com.google.gson.annotations.SerializedName

data class News(
    @SerializedName("category")
    val category: String= "",
    @SerializedName("datetime")
    val datetime: Int = 0,
    @SerializedName("headline")
    val headline: String = "",
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("image")
    val image: String = "",
    @SerializedName("related")
    val related: String = "",
    @SerializedName("source")
    val source: String = "",
    @SerializedName("summary")
    val summary: String = "",
    @SerializedName("url")
    val url: String = ""
)