package com.example.myapplication

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityControlBinding
import java.io.IOException
import java.lang.reflect.Method

class ControlActivity : AppCompatActivity(), ReceiveThread.Listener {
    private lateinit var binding: ActivityControlBinding
    private lateinit var actListLauncher: ActivityResultLauncher<Intent>
    private lateinit var btConnection: BtConnection
    private var listItem: ListItem? = null

    private lateinit var selectedDevice: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityControlBinding.inflate((layoutInflater))
        setContentView(binding.root)
        onBtListResult()
        init()

        binding.apply {

            b1.setOnCheckedChangeListener{_, isChecked ->
                val statusBTN1 = 10
                if(!isBTConnected()) {
                    b1.isChecked = false
                }
                else{
                    if(isChecked){
                        ButtonActivity.turnOn(statusBTN1)
                    }
                    else ButtonActivity.turnOff(statusBTN1)
                }
            }

            b2.setOnCheckedChangeListener{_, isChecked ->
                val statusBTN2 = 20
                if(!isBTConnected()) {
                    b2.isChecked = false
                }
                else{
                    if(isChecked){
                        ButtonActivity.turnOn(statusBTN2)
                    }
                    else ButtonActivity.turnOff(statusBTN2)
                }
            }

            b3.setOnCheckedChangeListener{_, isChecked ->
                val statusBTN3 = 30
                if(!isBTConnected()) {
                    b3.isChecked = false
                }
                else{
                    if(isChecked){
                        ButtonActivity.turnOn(statusBTN3)
                    }
                    else ButtonActivity.turnOff(statusBTN3)
                }
            }

            b4.setOnCheckedChangeListener {_,isChecked ->
                val statusBTN4 = 40
                if(!isBTConnected()) {
                    b4.isChecked = false
                }
                else{
                    if(isChecked){
                        ButtonActivity.turnOn(statusBTN4)
                    }
                    else ButtonActivity.turnOff(statusBTN4)
                }

            }

            b5.setOnCheckedChangeListener {_,isChecked ->
                val statusBTN5 = 50
                if(!isBTConnected()) {
                    b5.isChecked = false
                }
                else{
                    if(isChecked){
                        ButtonActivity.turnOn(statusBTN5)
                    }
                    else ButtonActivity.turnOff(statusBTN5)
                }

            }

            b6.setOnCheckedChangeListener {_,isChecked ->
                val statusBTN6 = 60
                if(!isBTConnected()) {
                    b6.isChecked = false
                }
                else{
                    if(isChecked){
                        ButtonActivity.turnOn(statusBTN6)
                    }
                    else ButtonActivity.turnOff(statusBTN6)
                }

            }

            bReset.setOnClickListener {
                clearBTN()
                ButtonActivity.reset()
                if (isBTConnected()) {
                    btConnection.sendMessage(0.to2ByteArray())
                }
            }
            bAdd2Q.setOnClickListener {
                if(timeText.text!!.isNotEmpty()) {
                    if (!ButtonActivity.add2Queue(timeText.text.toString())) {
                        Toast.makeText(this@ControlActivity,
                            "No command to add",
                            Toast.LENGTH_SHORT).show()
                    }
                    timeText.text?.clear()
                    showQueue.text = "Queue\n ${ButtonActivity.displayExeList()}"
                    addTable(counter, exeList)
                }
                else Toast.makeText(this@ControlActivity, "Input time", Toast.LENGTH_SHORT).show()
            }

            bExecute.setOnClickListener {
                if (isBTConnected()){
                    if (exeList.isNotEmpty()){
                        displayAlertDialog { sendExeList() }
                    }
                    else Toast.makeText(this@ControlActivity, "Empty command", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun init() {
        val btManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val btAdapter = btManager.adapter

        if(!btAdapter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, 1)
        }

        btConnection = BtConnection(btAdapter, this)
    }

    private fun addTable(int: Int, mutableList :MutableList<ByteArray>) {
        val tLayout = findViewById<TableLayout>(R.id.tLayout1)
        val tbRow = TableRow(this@ControlActivity)

        tbRow.weightSum = 8F
        tbRow.layoutParams = TableLayout.LayoutParams(tLayout.height, tLayout.width)

        val tText1 = TextView(this@ControlActivity)
        val tText2 = TextView(this@ControlActivity)

        tText1.text = "ON"
        tText1.layoutParams = TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        tText1.gravity = Gravity.CENTER_HORIZONTAL and Gravity.CENTER_VERTICAL

        tText2.text = "OFF"
        tText2.gravity = Gravity.CENTER_HORIZONTAL and Gravity.CENTER_VERTICAL
        tText2.layoutParams = TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)

        tbRow.addView(tText1)
        tbRow.addView(tText2)

        tLayout.addView(tbRow)



/*        var i = 0

        while(i<int){
            val tbRow = TableRow(this@ControlActivity)
            val auxArray: ByteArray = mutableList[i]
            for(num in auxArray){
                if (num<100){
                    if (num%10 == 0){
                        tText.text = "OFF"
                        tbRow.addView(tText)
                    }
                    else if (num%10 == 1) {
                        tText.text = "ON"
                        tbRow.addView(tText)
                    }
                }
                else{
                    val auxT = num - 100
                    tText.text = "$auxT"
                    tbRow.addView(tText)
                }
            }
            tLayout.addView(tbRow)
            i+=1
        }

 */
    }


    private fun clearBTN(){
        binding.apply {
            b1.isChecked = false
            b2.isChecked = false
            b3.isChecked = false
            b4.isChecked = false
            b5.isChecked = false
            b6.isChecked = false

            showQueue.text = null
        }
    }

    private fun sendExeList() {
        btConnection.sendMessage(counter.to2ByteArray())
        for (item in exeList) {
            btConnection.sendMessage(item)
        }
    }

    private fun displayAlertDialog(parameterFunction: () -> Unit){
        AlertDialog.Builder(this)
            .setTitle("Confirmation")
            .setMessage("Execute instructions?")
            .setPositiveButton("Yes") { _, _ -> parameterFunction() }
            .setNegativeButton("No") { _, _ -> }
            .show()
    }

    @SuppressLint("MissingPermission")
    private fun isBTConnected(): Boolean{
        val btManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val btAdapter = btManager.adapter
        val pairedDevices: Set<BluetoothDevice>? = btAdapter.bondedDevices
        fun isSelectedDeviceInitialized() = this::selectedDevice.isInitialized

        if (pairedDevices!=null && isSelectedDeviceInitialized()) {
            for (device in pairedDevices) {
                if (device.name == selectedDevice) {
                    return try {
                        val m: Method = device.javaClass.getMethod("isConnected")
                        m.invoke(device) as Boolean
                    }
                    catch (e: Exception) {
                        throw IllegalStateException(e)
                    }
                }
                Toast.makeText(this, "No device connected", Toast.LENGTH_SHORT).show()
                return false
            }
        }
        Toast.makeText(this, "No device connected", Toast.LENGTH_SHORT).show()
        return false
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.control_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.id_list) {
            actListLauncher.launch(Intent(this, BtListActivity::class.java))


        }
        else if(item.itemId == R.id.id_connect) {
            listItem.let { 
                if (it != null){
                    selectedDevice = it?.name!!
                    if (!isBTConnected()) {
                        Toast.makeText(this, "Connecting", Toast.LENGTH_SHORT).show()
                        btConnection.connect(it.mac)
                    }
                    else {
                        try {
                            btConnection.closeConnection()
                            Toast.makeText(this, "Disconnecting device", Toast.LENGTH_SHORT).show()
                        }
                        catch (e:IOException){
                            Toast.makeText(this, "No device connected", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                else Toast.makeText(this, "No device selected", Toast.LENGTH_SHORT).show()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun onBtListResult() {
        actListLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == RESULT_OK) {
               listItem = it.data?.getSerializableExtra(BtListActivity.DEVICE_KEY) as ListItem
            }
        }
    }


    override fun onReceive(message: String) {
        runOnUiThread{
            if(!listOf("connect", "device").any{ message.contains(it, ignoreCase = true) }) {
                val message2 = "Contador:$message"
                binding.showContador.text = message2
            }
            else binding.showContador.text = message
        }
    }
}