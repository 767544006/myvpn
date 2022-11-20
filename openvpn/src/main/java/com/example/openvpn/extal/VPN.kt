package com.example.openvpn.extal

import android.app.Application
import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import com.example.openvpn.core.Connection
import com.example.openvpn.core.ConnectionStatus
import com.example.openvpn.core.IOpenVPNServiceInternal
import com.example.openvpn.core.ProfileManager
import com.example.openvpn.extal.bean.ConnectState

object VPN {
    var context:Application?=null
    var connectionInfo=ArrayList<ConnectState>()
    var isbegin=false
    //初始化lib
    fun init(context: Application){
        this.context=context
    }
    fun setConnectionList(list: MutableList<Connection>){
        CertUtils.profile?.mConnections=list.toTypedArray()
        ProfileManager.saveProfile(context, CertUtils.profile)
        val profileManager = ProfileManager.getInstance(context)
        profileManager.addProfile(CertUtils.profile)
        profileManager.saveProfileList(context)
    }

}