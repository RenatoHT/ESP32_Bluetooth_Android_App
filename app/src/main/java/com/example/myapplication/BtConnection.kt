package com.example.myapplication

import android.bluetooth.BluetoothAdapter


class BtConnection(private val adapter: BluetoothAdapter, private val listener: ReceiveThread.Listener) {
    lateinit var cThread: ConnectThread
    fun connect(mac: String) {
        if (adapter.isEnabled && mac.isNotEmpty()) {
            val device = adapter.getRemoteDevice(mac)
            device.let {
                cThread = ConnectThread(it, listener)
                cThread.start()

            }
        }
    }


    fun sendMessage(message: ByteArray) {

        cThread.rThread.sendMessage(message)

    }

    fun closeConnection() {
        cThread.rThread.closeConnection()
    }

}