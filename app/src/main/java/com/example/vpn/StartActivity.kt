package com.example.vpn

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.aleyn.mvvm.base.BaseActivity
import com.blankj.utilcode.util.SPStaticUtils
import com.blankj.utilcode.util.SpanUtils
import com.blankj.utilcode.util.ToastUtils
import com.example.VpsFactory
import com.example.ad.AdFactory
import com.example.vpn.databinding.ActivityStartBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StartActivity : BaseActivity<ActivityStartBinding>() {
    var isCheck = true
    var isJoin = false
    override fun initView(savedInstanceState: Bundle?) {
        upState()
        mBinding.checkbox.setOnClickListener {
            isCheck = !isCheck
            upState()
        }
        val isFirst = SPStaticUtils.getBoolean("isFirst", true)
        if (isFirst) {
            makeSpan()
            mBinding.group.visibility = View.VISIBLE
            mBinding.agree.setOnClickListener {
                if (isCheck) {
                    start()
                } else {
                    ToastUtils.showLong("please Agree Privacy")
                }
            }
        } else {
            start()
        }

    }

    private fun upState() {
        if (isCheck) {
            mBinding.checkbox.setImageResource(R.drawable.ic_selected_round)
        } else {
            mBinding.checkbox.setImageResource(R.drawable.ic_unselected_round)
        }
    }

    private fun makeSpan() {
        SpanUtils.with(mBinding.textView21).append("I am aware of the ").append(" Privacy Policy")
            .setClickSpan(
                Color.parseColor("#16FCFF"), true, object : View.OnClickListener {
                    override fun onClick(p0: View?) {
                        startActivity(Intent(this@StartActivity, PrivacyActivity::class.java))

                    }
                }).create()
    }

    private fun start() {
        lifecycleScope.launch {
            AdFactory.loadAll(this@StartActivity)
        }
        SPStaticUtils.put("isFirst", false)
        mBinding.group.visibility = View.GONE
        lifecycleScope.launch(Dispatchers.IO) {
            while (!isJoin) {
                delay(1000)
            }
            VpsFactory.initVps()
            withContext(Dispatchers.Main){
                AdFactory.show(this@StartActivity, "start", "start", null) {
                    startActivity(Intent(this@StartActivity, MainActivity::class.java))
                    finish()
                }
            }

        }
        lifecycleScope.launch(Dispatchers.IO) {
            delay(8000)
            isJoin=true
        }
        lifecycleScope.launch(Dispatchers.IO) {
            while (true) {
                delay(1000)
                if (AdFactory.result.find { it.type == "start" } != null){
                    isJoin=true
                    break
                }
            }
        }
    }

    override fun initData() {
    }
}