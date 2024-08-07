package com.example.cinema.ui.data.model

import com.google.gson.annotations.SerializedName

data class ActionFavoriteResponse(
    @SerializedName("status_code")
    val statusCode: Int,
    @SerializedName("status_message")
    val statusMessage: String
)