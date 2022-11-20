package com.example.openvpn.extal

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.VpnService
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.openvpn.LaunchVPN
import com.example.openvpn.R
import com.example.openvpn.core.*

class VpnManageActivity:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        startActivityForResult(VpnService.prepare(this),7676)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==7676&&resultCode==Activity.RESULT_OK){
            ProfileManager.updateLRU(VPN.context, CertUtils.profile)
            VPNLaunchHelper.startOpenVpn(CertUtils.profile, VPN.context)
            finish()
            Toast.makeText(this, "Permission", Toast.LENGTH_SHORT).show()
        }else{
            finish()
        }
    }
}