package com.jesse.sickstech.data.model.order

import com.google.gson.annotations.SerializedName

data class OrderResponse(
    val id: String,
    val status: String,
    @SerializedName("status_detail")
    val statusDetail: String,
    @SerializedName("external_reference")
    val externalReference: String
)