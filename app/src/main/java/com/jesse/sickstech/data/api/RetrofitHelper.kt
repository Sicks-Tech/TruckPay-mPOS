package com.jesse.sickstech.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {

    val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.15.10:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api = retrofit.create(Api::class.java)
}