package com.example.myapplication

import android.bluetooth.BluetoothSocket
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class ReceiveThread(val bSocket: BluetoothSocket, val listener: Listener): Thread() {
    var inStream: InputStream? = null
    var ouStream: OutputStream? = null
    init {
        try {
            inStream = bSocket.inputStream
        }
        catch (i: IOException) {

        }
        try {
            ouStream = bSocket.outputStream
        }
        catch (i: IOException) {

        }
    }

    override fun run() {
        val buf = ByteArray(2)
        while (true) {
            try{
                val size = inStream?.read(buf)
                val message = String(buf, 0, size!!)
                listener.onReceive(message)
            }
            catch (i: IOException) {
                listener.onReceive("Connection Lost")
                break
            }
        }
    }

    fun sendMessage(byteArray: ByteArray) {
        try {
            ouStream?.write(byteArray)
        }
        catch (i: IOException) {
            listener.onReceive("Device not found")
        }
    }

    fun closeConnection() {
        try {
            ouStream?.close()
        }
        catch (e: IOException){
            listener.onReceive("No device connected")
        }
        try {
            bSocket.close()
        }
        catch (e:IOException){
            listener.onReceive("No device connected")
        }

    }

    interface Listener {
        fun onReceive(message: String)

    }
}