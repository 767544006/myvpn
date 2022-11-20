package com.example.vpn

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.aleyn.mvvm.base.BaseActivity
import com.blankj.utilcode.util.LogUtils
import com.example.App
import com.example.AppBaseDatabase
import com.example.VpsFactory
import com.example.bean.HistoryBean
import com.example.openvpn.extal.VPN
import com.example.vpn.databinding.ActivityReportBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReportActivity:BaseActivity<ActivityReportBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        mBinding.time.text = "0 minutes"
        VpsFactory.currentVps.observe(this){
            if(it!=null) {
                mBinding.textView2.text = it.vpsname
                mBinding.textView3.text = "IP ${it.ip}"
                assets.open("${it.local}.webp").use {
                    mBinding.imageView3.setImageBitmap(BitmapFactory.decodeStream(it))
                }
            }
        }
        mBinding.imageView.setOnClickListener {
            onBackPressed()
        }
        if (!intent.getBooleanExtra("isSuccess",false)) {
            LogUtils.d(VPN.connectionInfo)
            VPN.connectionInfo.let {
                if (it.isNotEmpty()) {
                    val duration = (System.currentTimeMillis() - it.last().startTime) / 1000 / 60
                    mBinding.time.text = "${duration.toString()} minutes"
                    lifecycleScope.launch (Dispatchers.IO){
                        AppBaseDatabase.instance.historyDao?.insert(HistoryBean(it.last().startTime,duration.toInt(),intent.getStringExtra("country")?:""))
                        App.history.postValue(1)
                    }
                }
            }
        }
    }

    override fun initData() {

    }
}
