package com.example.vpn

import android.os.Bundle
import com.aleyn.mvvm.base.BaseActivity
import com.example.vpn.databinding.ActivityAboutBinding
import com.example.vpn.databinding.ActivityMainBinding

class AboutActivity:BaseActivity<ActivityAboutBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        mBinding.imageView.setOnClickListener {
            onBackPressed()
        }
    }

    override fun initData() {

    }
}