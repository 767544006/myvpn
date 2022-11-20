package com.example.openvpn.extal.impl

import android.content.Context
import com.example.openvpn.VpnProfile

interface IVpn{
    suspend fun connect(user:String,pass:String)
    suspend fun disConnect()
    fun isActive():Boolean
}