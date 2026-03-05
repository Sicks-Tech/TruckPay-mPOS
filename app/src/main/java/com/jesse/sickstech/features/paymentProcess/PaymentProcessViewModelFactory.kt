package com.jesse.sickstech.features.paymentProcess

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jesse.sickstech.core.util.BluetoothDeviceHelper
import com.jesse.sickstech.data.api.Api
import com.jesse.sickstech.data.local.dao.OrderDAO
import com.jesse.sickstech.data.printer.BluetoothPrinterManager

class PaymentProcessViewModelFactory(
    private val api: Api,
    private val orderDao: OrderDAO,
    private val bluetoothHelper: BluetoothDeviceHelper,
    private val printerManager: BluetoothPrinterManager
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PaymentProcessViewModel::class.java)) {
            return PaymentProcessViewModel(api, orderDao, bluetoothHelper, printerManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}