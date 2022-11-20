package com.example

import androidx.lifecycle.MutableLiveData
import com.example.bean.VpsBeanItem
import com.example.openvpn.core.Connection
import com.example.openvpn.extal.OpenConnector
import com.example.openvpn.extal.VPN

object VpsFactory {
    val currentVps = MutableLiveData<VpsBeanItem>()
    val fastVps = MutableLiveData<VpsBeanItem>()
    suspend fun connect() {
        VPN.setConnectionList(mutableListOf<Connection>(Connection().apply {
            mServerName = currentVps.value?.ip
            mServerPort = "1194"
            mUseUdp = true
            mConnectTimeout = 15
            country = "US"
        }))
        OpenConnector.connect("", "")


    }

    fun initVps() {
        if (App.data.isNotEmpty()){
            val item=App.data.random()
            fastVps.postValue(item)
            currentVps.postValue(item)
        }
    }
}