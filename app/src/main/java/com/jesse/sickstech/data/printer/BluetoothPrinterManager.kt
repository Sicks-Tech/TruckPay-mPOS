package com.jesse.sickstech.data.printer

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

class BluetoothPrinterManager(private val context: Context) {

    suspend fun printData(device: BluetoothDevice, imageBytes: ByteArray): Boolean = withContext(Dispatchers.IO) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            return@withContext false
        }

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            Log.e("PRINTER", "Permissão de Bluetooth negada no Manager!")
            return@withContext false
        }

        var socket: BluetoothSocket? = null
        val uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

        try {
             @SuppressLint("MissingPermission")
            socket = device.createRfcommSocketToServiceRecord(uuid)
            socket.connect()
        } catch (e: SecurityException) {
            // O próprio Android sugeriu tratar o SecurityException, então fazemos isso!
            Log.e("PRINTER", "Erro de segurança (permissão) ao conectar", e)
            return@withContext false
        } catch (e: Exception) {
            Log.e("PRINTER", "Falha no método padrão, tentando reflection", e)

            try {
                val m = device.javaClass.getMethod("createRfcommSocket", Int::class.javaPrimitiveType)
                socket = m.invoke(device, 1) as BluetoothSocket

                // @SuppressLint("MissingPermission") se necessário
                socket.connect()
            } catch (e2: Exception) {
                Log.e("PRINTER", "Falha no reflection", e2)
                return@withContext false
            }
        }

        return@withContext try {
            socket?.let { s ->
                val out = s.outputStream

                val init = byteArrayOf(0x1B, 0x40)
                val alinharCentro = byteArrayOf(0x1B, 0x61, 0x01)

                out.write(init)
                out.write(alinharCentro)
                out.write(imageBytes)
                out.flush()

                Thread.sleep(1000)
                s.close()
                Log.d("PRINTER", "Socket Fechado com sucesso")
                true
            } ?: false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}