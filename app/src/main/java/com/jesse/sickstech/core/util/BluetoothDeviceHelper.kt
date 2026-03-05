package com.jesse.sickstech.core.util

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import androidx.annotation.RequiresPermission

class BluetoothDeviceHelper(context: Context) {

    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun getImpressoraPareada(): BluetoothDevice? {
        if (bluetoothAdapter?.isEnabled == false) return null

        @SuppressLint("MissingPermission")
        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices

        @SuppressLint("MissingPermission")
        return pairedDevices?.find { device ->
            val nome = device.name ?: ""
            nome.contains("MPT-II", ignoreCase = true) ||
                    nome.contains("3010") ||
                    nome.contains("MPT", ignoreCase = true)
        }
    }
}