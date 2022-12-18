package com.example.vpn

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.lifecycleScope
import com.aleyn.mvvm.base.BaseActivity
import com.example.App
import com.example.ad.AdFactory
import com.example.okHttpGet
import com.example.vpn.databinding.ActivityLocationBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class LocationActivity : BaseActivity<ActivityLocationBinding>() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun initView(savedInstanceState: Bundle?) {
        mBinding.map.settings.apply {
            javaScriptEnabled = true
        }
        mBinding.imageView.setOnClickListener {
            onBackPressed()
        }
        lifecycleScope.launch(Dispatchers.IO) {
            okHttpGet("https://ipapi.co/json")
        }
        mBinding.map.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView,
                url: String?
            ): Boolean { // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                view.loadUrl(url!!)
                return true
            }
        })
        mBinding.map.loadUrl("https://www.google.com/maps")
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch {
            AdFactory.show(this@LocationActivity,"nav","report",mBinding.adContainer)
        }
    }
    override fun initData() {
        App.localBean.observe(this) {
            mBinding.textView12.text = it?.ip ?: "unKnown"
            mBinding.textView13.text = it?.city ?: "unKnown"
            mBinding.textView15.text = it?.country ?: "unKnown"
            mBinding.time.text = it?.timezone ?: "unKnown"
        }
    }
}

