package com.jesse.sickstech.data.api

import com.jesse.sickstech.data.model.order.OrderRequest
import com.jesse.sickstech.data.model.order.OrderResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface Api {
    //fazer pedido
    @POST("v1/orders")
    suspend fun createOrder(
        @Body request: OrderRequest
    ): Response<OrderResponse>
}