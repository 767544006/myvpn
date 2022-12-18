package com.example

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.Utils
import com.example.bean.LocalBean
import com.example.bean.VpsBeanItem
import com.example.openvpn.extal.CertUtils
import com.example.openvpn.extal.VPN
import com.example.vpn.StartActivity
import com.google.android.gms.ads.MobileAds
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class App : Application() {
    val str = "client\n" +
            "proto udp\n" +
            "explicit-exit-notify\n" +
            "dev tun\n" +
            "resolv-retry infinite\n" +
            "nobind\n" +
            "persist-key\n" +
            "persist-tun\n" +
            "remote-cert-tls server\n" +
            "verify-x509-name server_TVIeM8NimcvxWNQz name\n" +
            "auth SHA256\n" +
            "auth-nocache\n" +
            "cipher AES-128-GCM\n" +
            "tls-client\n" +
            "tls-version-min 1.2\n" +
            "tls-cipher TLS-ECDHE-ECDSA-WITH-AES-128-GCM-SHA256\n" +
            "ignore-unknown-option block-outside-dns\n" +
            "setenv opt block-outside-dns # Prevent Windows 10 DNS leak\n" +
            "verb 3\n" +
            "<ca>\n" +
            "-----BEGIN CERTIFICATE-----\n" +
            "MIIB1jCCAX2gAwIBAgIUIivs/rymmcYQWKPKfT/HPGo3M1IwCgYIKoZIzj0EAwIw\n" +
            "HjEcMBoGA1UEAwwTY25fYWJSb1F1ZUFaMUQwVEZ5QjAeFw0yMjExMTIxMjUxMTNa\n" +
            "Fw0zMjExMDkxMjUxMTNaMB4xHDAaBgNVBAMME2NuX2FiUm9RdWVBWjFEMFRGeUIw\n" +
            "WTATBgcqhkjOPQIBBggqhkjOPQMBBwNCAAQ6Tzeg/o8yXFOd2YwqynU7M4UX3AMK\n" +
            "su+MuNvbeadB76Ey7Wgkbg3bYSP5H2K+k0AeTf+1zady06PiBoP9qImWo4GYMIGV\n" +
            "MB0GA1UdDgQWBBRTzMKbxl/1gNa3Fu0Ha0aXguc96zBZBgNVHSMEUjBQgBRTzMKb\n" +
            "xl/1gNa3Fu0Ha0aXguc966EipCAwHjEcMBoGA1UEAwwTY25fYWJSb1F1ZUFaMUQw\n" +
            "VEZ5QoIUIivs/rymmcYQWKPKfT/HPGo3M1IwDAYDVR0TBAUwAwEB/zALBgNVHQ8E\n" +
            "BAMCAQYwCgYIKoZIzj0EAwIDRwAwRAIgW+eg+TGhsAEUQJ3XItVKViIQBzCPC2iw\n" +
            "TLhH/Wm2gMwCIGGyvUz6sEiUwSDL30fLtnd8MFIMJHjizgyvKdY444/7\n" +
            "-----END CERTIFICATE-----\n" +
            "</ca>\n" +
            "<cert>\n" +
            "-----BEGIN CERTIFICATE-----\n" +
            "MIIB2TCCAX6gAwIBAgIQMnHVs23mIFk+CurAD6dzSDAKBggqhkjOPQQDAjAeMRww\n" +
            "GgYDVQQDDBNjbl9hYlJvUXVlQVoxRDBURnlCMB4XDTIyMTExMjEyNTEzMloXDTI1\n" +
            "MDIxNDEyNTEzMlowETEPMA0GA1UEAwwGY2xpZW50MFkwEwYHKoZIzj0CAQYIKoZI\n" +
            "zj0DAQcDQgAE6BhDxZ/WvOBd0RFqpX00WOxfLQGvBcx8C/3MRxuplRipmAt6D8F6\n" +
            "+KZvi8rGef30NYqmg9hJU7whv8g7fasKQKOBqjCBpzAJBgNVHRMEAjAAMB0GA1Ud\n" +
            "DgQWBBQ+qjDz6OoYVRotShjwuAYgTdNcvjBZBgNVHSMEUjBQgBRTzMKbxl/1gNa3\n" +
            "Fu0Ha0aXguc966EipCAwHjEcMBoGA1UEAwwTY25fYWJSb1F1ZUFaMUQwVEZ5QoIU\n" +
            "Iivs/rymmcYQWKPKfT/HPGo3M1IwEwYDVR0lBAwwCgYIKwYBBQUHAwIwCwYDVR0P\n" +
            "BAQDAgeAMAoGCCqGSM49BAMCA0kAMEYCIQDKlTjDK3bGFOOGitGR7aJVbnreezXg\n" +
            "1XxRHGCTyosSfwIhAN8YDQHWWT/pkwGcScfwMT/78YHpfpzgmMLq6sQ5vFNx\n" +
            "-----END CERTIFICATE-----\n" +
            "</cert>\n" +
            "<key>\n" +
            "-----BEGIN PRIVATE KEY-----\n" +
            "MIGHAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBG0wawIBAQQgfjs+wNGW8j3SsLZT\n" +
            "mLbLXf5C66ahbh81bTGUNgJxPOehRANCAAToGEPFn9a84F3REWqlfTRY7F8tAa8F\n" +
            "zHwL/cxHG6mVGKmYC3oPwXr4pm+LysZ5/fQ1iqaD2ElTvCG/yDt9qwpA\n" +
            "-----END PRIVATE KEY-----\n" +
            "</key>\n" +
            "<tls-crypt>\n" +
            "#\n" +
            "# 2048 bit OpenVPN static key\n" +
            "#\n" +
            "-----BEGIN OpenVPN Static key V1-----\n" +
            "19552dd934bf0c349ded130bbd562561\n" +
            "29911b69256c01e18733521a1f4fc1be\n" +
            "69d69a1171f3bd094842d257f7d70dd1\n" +
            "6bec86bd6a337f47d363ccc7b2a784a5\n" +
            "88d6a13739a54994ec707330f0dc1c0e\n" +
            "f9b75054ce353deb51d4b706dfc42535\n" +
            "09f9f04476c1620418e27b76fc82fc88\n" +
            "3d949401ed67cf01345203c8bcdf5cd9\n" +
            "fc6f83c1a937c8cf0eac73af3397ac86\n" +
            "76e7b4040b1668c7f60a564724b7c276\n" +
            "acf906e5f12ad37d50de1b8f32b11b0b\n" +
            "4c2ef958ed0df486b7104af260088480\n" +
            "7e7227fc44a4505a438d9d4847b71686\n" +
            "7489e0624391193ef48d26db314cdcf0\n" +
            "325375c760487829a4551b0c45e1ffa8\n" +
            "bbc6bd3c86483d5da9e18a2ea089a838\n" +
            "-----END OpenVPN Static key V1-----\n" +
            "</tls-crypt>\n"
    companion object {
        var context: Context? = null
        var localBean = MutableLiveData<LocalBean?>()
        var data = mutableListOf<VpsBeanItem>()
        var history = MutableLiveData<Int>()
        var isBack=false
    }

    override fun onCreate() {
        super.onCreate()
        VPN.init(this)
        context = this
        GlobalScope.launch(Dispatchers.IO) {
            okHttpGet("https://ipapi.co/json")
        }
        CertUtils.setCert(str)
        MobileAds.initialize(
            this
        ) { }
        data = GsonUtils.fromJson(Data.vps, object : TypeToken<List<VpsBeanItem?>?>() {}.type)
        AppUtils.registerAppStatusChangedListener(object :Utils.OnAppStatusChangedListener{
            override fun onForeground(activity: Activity?) {
                if (isBack){
                    startActivity(Intent(this@App,StartActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    })
                    isBack=false
                }
            }

            override fun onBackground(activity: Activity?) {
                isBack=true
            }

        })
    }


}