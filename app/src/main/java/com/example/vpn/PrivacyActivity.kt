package com.example.vpn

import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import com.aleyn.mvvm.base.BaseActivity
import com.example.vpn.databinding.ActivityWebBinding

class PrivacyActivity : BaseActivity<ActivityWebBinding>() {
    val webView by lazy {
        WebView(this);
    }

    override fun initView(savedInstanceState: Bundle?) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://sites.google.com/view/yourprivacyproxy/%E9%A6%96%E9%A1%B5");
        mBinding.container.addView(webView)
        webView.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView,
                url: String?
            ): Boolean { // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                view.loadUrl(url!!)
                return true
            }
        })

    }

    override fun initData() {

    }

    override fun onDestroy() {
        if (webView != null) {
            val parent = webView .parent;
            if (parent != null) {
                val a=parent as ViewGroup
                a.removeAllViews()
            }
            webView.stopLoading();
            webView.getSettings().setJavaScriptEnabled(false);
            webView.clearHistory();
            webView.removeAllViews();
            webView.destroy();
        }
        super.onDestroy()

    }
}