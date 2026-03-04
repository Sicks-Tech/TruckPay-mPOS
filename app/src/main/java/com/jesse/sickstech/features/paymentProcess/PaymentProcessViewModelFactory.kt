package com.jesse.sickstech.features.paymentProcess

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jesse.sickstech.data.api.Api
import com.jesse.sickstech.data.local.dao.OrderDAO

class PaymentProcessViewModelFactory(
    private val api: Api,
    private val orderDao: OrderDAO
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PaymentProcessViewModel::class.java)) {
            return PaymentProcessViewModel(api, orderDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}