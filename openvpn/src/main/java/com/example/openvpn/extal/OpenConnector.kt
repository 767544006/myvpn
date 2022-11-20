package com.example.openvpn.extal

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.VpnService
import android.os.IBinder
import com.example.openvpn.VpnProfile
import com.example.openvpn.core.*
import com.example.openvpn.extal.impl.IVpn
import kotlinx.coroutines.delay

object OpenConnector:IVpn {
    var mService: IOpenVPNServiceInternal?=null
    var isJump=true
    val disAllow= hashSetOf<String>()
    private val mConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(
            className: ComponentName,
            service: IBinder
        ) {
            mService = IOpenVPNServiceInternal.Stub.asInterface(service)
        }
        override fun onServiceDisconnected(arg0: ComponentName) {
            mService = null
        }
    }

    override suspend fun connect(user:String,pass:String) {
//        CertUtils.profile?.mUsername = user;//对应线路的用户名
//        CertUtils.profile?.mPassword = pass;//对应线路的密码
//        //关键代码 如果不设置成TYPE_USERPASS_CERTIFICATES 在C/C++层是不会调用用户密码的
//        CertUtils.profile?.mAuthenticationType = VpnProfile.TYPE_USERPASS_CERTIFICATES;
//        CertUtils.profile?.mCheckRemoteCN = false
        if (!isActive()) {
            if (CertUtils.profile!=null) {
                if (VpnService.prepare(VPN.context) != null) {
                    VPN.context?.startActivity(
                        Intent(
                            VPN.context,
                            VpnManageActivity::class.java
                        ).apply {
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        })
                } else {
                    ProfileManager.updateLRU(VPN.context, CertUtils.profile)
                    VPNLaunchHelper.startOpenVpn(CertUtils.profile, VPN.context)
                }
            }
        }
    }

    fun setDisAllow(set:Set<String>){
        disAllow.clear()
        disAllow.addAll(set)
    }
    override suspend fun disConnect() {
        if (mService==null) {
            bindService()
        }
        delay(1000)
        mService?.stopVPN(true)
    }
    override fun isActive(): Boolean {
        return VpnStatus.isVPNActive()
    }
    private fun bindService() {
        val intent = Intent(VPN.context, OpenVPNService::class.java)
        intent.action = OpenVPNService.START_SERVICE
        VPN.context?.bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
    }
    fun registerStateListener(listener: VpnStatus.StateListener){
        VpnStatus.addStateListener(listener)
    }
    fun unregisterStateListener(listener: VpnStatus.StateListener){
        VpnStatus.removeStateListener(listener)
    }

}