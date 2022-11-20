package com.example.openvpn.extal.bean

data class ConnectState(val ip:String,val port:Int, val code:Int,val time:Long,val type:Int,var startTime:Long) {}