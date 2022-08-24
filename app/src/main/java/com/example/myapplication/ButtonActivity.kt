package com.example.myapplication

val offStateSwitches = byteArrayOf(10, 20, 30, 40, 50, 60, 0)
var switches = offStateSwitches.copyOf()
var counter: Int = 0
val exeList = mutableListOf<ByteArray>()
var sentence = ""

fun Int.to2ByteArray() : ByteArray = byteArrayOf(toByte())

object ButtonActivity{
    fun turnOn(num: Int){
        switches[(num/10)-1] = (num + 1).toByte()

    }

    fun turnOff(num: Int){
        switches[(num/10)-1] = num.toByte()
    }


    fun reset(){
        switches = offStateSwitches.copyOf()
        exeList.clear()
        counter = 0
        sentence = ""
    }

    fun add2Queue(text: String): Boolean{
        if (!switches.contentEquals(offStateSwitches) && text.toInt() <= 155 && text.toInt() != 0) {
            switches[6] = (text.toInt() + 100).toByte()
            exeList.add(switches.copyOf())
            counter = exeList.size
            return true
        }
        else return false
    }

    fun displayExeList(): String {
        for (item in exeList) {
            if(item.contentEquals(exeList.last())){
                val strArray = item.map { it.toUByte().toString() }.toTypedArray()
                for (i in strArray) {
                    if (sentence.isEmpty() || sentence.endsWith(" ")) {
                        sentence += i
                    }
                    else sentence += "; $i"
                    }

                sentence+="\n "
            }
        }
        return sentence
    }

}