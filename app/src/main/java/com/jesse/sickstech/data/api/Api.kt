package com.jesse.sickstech.data.api

import com.jesse.sickstech.data.model.order.OrderRequest
import com.jesse.sickstech.data.model.order.OrderResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface Api {
    //fazer pedido
    @POST("v1/orders")
    suspend fun createOrder(
        @Body request: OrderRequest
    ): Response<OrderResponse>

    @GET("v1/orders/{id}")
    suspend fun getOrder(
        @Path("id") orderId: String
    ): Response<OrderResponse>

}