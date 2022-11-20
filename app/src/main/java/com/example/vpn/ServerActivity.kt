package com.example.vpn

import android.graphics.BitmapFactory
import android.os.Bundle
import com.aleyn.mvvm.base.BaseActivity
import com.example.App
import com.example.VpsFactory
import com.example.adapter.VpsAdapter
import com.example.bean.VpsBeanItem
import com.example.vpn.databinding.ActivityServerBinding

class ServerActivity:BaseActivity<ActivityServerBinding>() {
    private val vpsAdapter by lazy {
        VpsAdapter()
    }
    override fun initView(savedInstanceState: Bundle?) {
        vpsAdapter.data= App.data
        mBinding.recycleview.adapter=vpsAdapter
        mBinding.textView2.text=VpsFactory.fastVps.value?.vpsname
        assets.open("${VpsFactory.fastVps.value?.local}.webp").use {
            mBinding.imageView3.setImageBitmap(BitmapFactory.decodeStream(it))
        }
        mBinding.constraintLayout2.setOnClickListener {
            vpsAdapter.data.find { it.isSelect }?.isSelect=false
            vpsAdapter.data.find { it.ip==VpsFactory.fastVps.value?.ip}?.isSelect=true
            VpsFactory.currentVps.postValue(VpsFactory.fastVps.value)
            vpsAdapter.notifyDataSetChanged()
            intent.putExtra("data",true)
            setResult(RESULT_OK,intent)
            finish()
        }
        VpsFactory.currentVps.observe(this){
            if (VpsFactory.fastVps.value==VpsFactory.currentVps.value){
                mBinding.right.setImageResource(R.drawable.ic_selected_round)

            }else{
                mBinding.right.setImageResource(R.drawable.ic_unselected_round)
            }

        }

    }

    override fun initData() {

        vpsAdapter.setOnItemClickListener{ adapter, _, p->
            val item=adapter.data[p] as VpsBeanItem
            vpsAdapter.data.forEach {
                it.isSelect=false
            }
            item.isSelect=true
            VpsFactory.currentVps.postValue(item)
            adapter.notifyDataSetChanged()
            intent.putExtra("data",true)
            setResult(RESULT_OK,intent)
            finish()
        }
    }
}