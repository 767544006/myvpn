package com.example.vpn

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.aleyn.mvvm.base.BaseActivity
import com.example.VpsFactory
import com.example.vpn.databinding.ActivityStartBinding
import kotlinx.coroutines.delay

class StartActivity:BaseActivity<ActivityStartBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        lifecycleScope.launchWhenCreated {
            delay(2000)
            VpsFactory.initVps()
            startActivity(Intent(this@StartActivity,MainActivity::class.java))
            finish()
        }
    }

    override fun initData() {
    }
}