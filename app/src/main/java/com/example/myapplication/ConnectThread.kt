package com.example.myapplication

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import java.io.IOException
import java.util.*

@SuppressLint("MissingPermission")
class ConnectThread(private val device: BluetoothDevice, private val listener: ReceiveThread.Listener) : Thread() {
    val uuid = "00001101-0000-1000-8000-00805F9B34FB"
    var mSocket: BluetoothSocket? = null
    lateinit var  rThread: ReceiveThread

    init {
        try {
            mSocket = device.createRfcommSocketToServiceRecord(UUID.fromString(uuid))
        }
        catch (i: IOException) {

        }
    }
    override fun run() {
        try{
            listener.onReceive("Connecting")
            mSocket?.connect()
            listener.onReceive("Connected")
            rThread = ReceiveThread(mSocket!!, listener)
            rThread.start()
        }
        catch (i: IOException){
            listener.onReceive("Cannot connect to device")
            closeConnection()
        }
    }

    private fun closeConnection() {
        try{
            mSocket?.close()
        }
        catch (i: IOException){
            listener.onReceive("Cannot connect to device")
        }
    }
}