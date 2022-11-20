package com.example

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.example.bean.LocalBean
import okhttp3.OkHttpClient
import okhttp3.Request

object DataUtil {
    val name= mutableListOf("illegal month","January","February",
        "March","April","May","June","July","August",
        "September","October","November","December")
}
fun okHttpGet(url: String) {
    try {
        // 首先需要创建一个OkHttpClient对象用于Ht
        // tp请求, 可以改成全局型
        val client = OkHttpClient().newBuilder().build()
        // 创建一个request对象
        val request: Request = Request.Builder().url(url).build()
        // 执行和回调
        val string=client.newCall(request).execute().body?.string()
        LogUtils.d(string)
        val bean=GsonUtils.fromJson<LocalBean>(string
            ,
            LocalBean::class.java)
        if (bean!=null) {
            App.localBean.postValue(
                bean
            )
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
fun toEmail(context: Context, address: String) {
    try {
        val uri = Uri.parse("mailto:$address")
        Intent(Intent.ACTION_SENDTO, uri).apply {
            putExtra(Intent.EXTRA_EMAIL, uri)
            context.startActivity(Intent.createChooser(this, "Select Email App"))
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}