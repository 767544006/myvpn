package com.example.vpn

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.aleyn.mvvm.base.BaseActivity
import com.blankj.utilcode.util.IntentUtils
import com.blankj.utilcode.util.TimeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.App
import com.example.AppBaseDatabase
import com.example.VpsFactory
import com.example.bean.DrawBean
import com.example.openvpn.core.ConnectionStatus
import com.example.openvpn.core.VpnStatus
import com.example.openvpn.extal.OpenConnector
import com.example.openvpn.extal.OpenConnector.isJump
import com.example.toEmail
import com.example.vpn.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class MainActivity : BaseActivity<ActivityMainBinding>(), VpnStatus.StateListener {
    private val adapter by lazy {
        DrawAdapter()
    }
    var isConnecting=false

    private val requestDataLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data?.getBooleanExtra("data",false)
                if (data == true){
                    if (OpenConnector.isActive()){
                        lifecycleScope.launch {
                            isJump=false
                            OpenConnector.disConnect()
                            delay(1500)
                            VpsFactory.connect()
                        }
                    }else{
                        lifecycleScope.launch {
                            VpsFactory.connect()
                        }
                    }
                }
            }
        }
    private val data = mutableListOf<DrawBean>()
    @SuppressLint("SetTextI18n")
    override fun initView(savedInstanceState: Bundle?) {

        mBinding.imageView.setOnClickListener {
            mBinding.draw.openDrawer(Gravity.LEFT)
        }
        mBinding.leftDrawer.recycleview.adapter = adapter
        adapter.data = data
        adapter.setOnItemClickListener{
            adapter,_,p->
            when(p){
                0->{
                    toEmail(this,"zengzhigang2022@gmail.com")
                }
                1->{
                    try {
                        val intent= IntentUtils.getShareTextIntent("https://play.google.com/store/apps/details?id=${packageName}")
                        startActivity(intent)
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                }
                2->{
                    startActivity(Intent(this,PrivacyActivity::class.java))

                }
                3->{
                    startActivity(Intent(this,AboutActivity::class.java))
                }
            }
        }
        mBinding.constraintLayout4.setOnClickListener {
            startActivity(Intent(this,HistoryActivity::class.java))
        }
        mBinding.constraintLayout2.setOnClickListener {
            val intent = Intent(this, ServerActivity::class.java)
            requestDataLauncher.launch(intent) //调用 launch 方法，该方法接收 输入类型 I
        }

        mBinding.location.setOnClickListener {
            startActivity(Intent(this, LocationActivity::class.java))
        }
        mBinding.imageView7.setOnClickListener {
            toggle()
        }
        VpsFactory.currentVps.observe(this){
            if(it!=null) {
                mBinding.textView2.text = it.vpsname
                mBinding.textView3.text = "IP ${it.ip}"
                assets.open("${it.local}.webp").use {
                    mBinding.imageView3.setImageBitmap(BitmapFactory.decodeStream(it))
                }
            }
        }

        App.history.observe(this){
            lifecycleScope.launch(Dispatchers.IO) {
                val list=AppBaseDatabase.instance.historyDao?.all?.sortedByDescending { it.time }
                withContext(Dispatchers.Main){
                    if (list.isNullOrEmpty()){
                        mBinding.textView4.text= "NULL"
                        mBinding.longa.text= "${0} minutes"
                        mBinding.country.text= "NULL"
                    }else {
                        val bean = list?.first()
                        mBinding.textView4.text =
                            TimeUtils.date2String(bean?.time?.let { Date(it) }, "HH:mm")
                        mBinding.longa.text = "${bean?.duration ?: 0} minutes"
                        mBinding.country.text = "${bean?.local ?: "null"}"
                    }
                }

            }
        }
        App.history.postValue(0)
    }

    override fun initData() {
        data.clear()
        data.addAll(
            mutableListOf(
                DrawBean(R.drawable.slice_edit, "Feedback&Support"),
                DrawBean(R.drawable.slice_share, "Share App"),
                DrawBean(R.drawable.slice_privacy, "Privacy Policy"),
//                DrawBean(R.drawable.slice_rating, "Rate US"),
                DrawBean(R.drawable.slice_about, "About")
            )
        )
        OpenConnector.registerStateListener(this)
    }

    private fun toggle() {
        if (OpenConnector.isActive()) {
            lifecycleScope.launch {
                if (isConnecting)
                {
                    isJump=false
                }
                OpenConnector.disConnect()
            }
        } else {
            lifecycleScope.launch {
                VpsFactory.connect()
            }
        }
    }

    override fun updateState(
        state: String?,
        logmessage: String?,
        localizedResId: Int,
        level: ConnectionStatus?,
        Intent: Intent?
    ) {
        lifecycleScope.launch(Dispatchers.Main) {
            when (state) {
                "CONNECTED" -> connected()
                "DISCONNECTED" -> disconnect()

            }
            when (level) {
                ConnectionStatus.LEVEL_START -> connecting()
                ConnectionStatus.LEVEL_NOTCONNECTED -> notConnected()
                ConnectionStatus.LEVEL_AUTH_FAILED -> disconnect()
                else -> {}
            }
        }
        if (state == "RECONNECTING") {

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        OpenConnector.unregisterStateListener(this)

    }
    override fun setConnectedVPN(uuid: String?) {

    }

    fun connected() {
        isConnecting=false
        mBinding.imageView7.setImageResource(R.drawable.ic_connected)
        mBinding.wave.stop()
        mBinding.wave.visibility=View.GONE
        startActivity(Intent(this, ReportActivity::class.java).apply {
            putExtra("isSuccess",true)
            putExtra("country","US")

        })
    }

    fun disconnect() {
        if (isJump) {
            isConnecting=false
            mBinding.wave.stop()
            mBinding.wave.visibility = View.GONE
            mBinding.imageView7.setImageResource(R.drawable.ic_no_connect)
            startActivity(Intent(this, ReportActivity::class.java).apply {
                putExtra("isSuccess", false)
                putExtra("country", "US")
            })
        }
    }

    fun connecting() {
        isJump=true
        isConnecting=true
        mBinding.wave.start()
        mBinding.wave.visibility=View.VISIBLE
    }

    fun notConnected() {
        isConnecting=false
        mBinding.imageView7.setImageResource(R.drawable.ic_no_connect)
        mBinding.wave.stop()
        mBinding.wave.visibility=View.GONE
    }
}

class DrawAdapter : BaseQuickAdapter<DrawBean, BaseViewHolder>(R.layout.item_draw) {
    override fun convert(holder: BaseViewHolder, item: DrawBean) {
        holder.setText(R.id.title, item.title)
        holder.setImageResource(R.id.imageView6, item.icon)
    }
}