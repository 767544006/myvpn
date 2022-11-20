package com.example.vpn

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.aleyn.mvvm.base.BaseActivity
import com.blankj.utilcode.util.LogUtils
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.example.AppBaseDatabase
import com.example.DataUtil
import com.example.adapter.MultipleItemQuickAdapter
import com.example.bean.DayBean
import com.example.vpn.databinding.ActivityHistoryBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class HistoryActivity:BaseActivity<ActivityHistoryBinding>() {
    private val date= mutableListOf<MultiItemEntity>()
    val adapter by lazy {
        MultipleItemQuickAdapter(date)
    }
    override fun initView(savedInstanceState: Bundle?) {
        mBinding.recycleview.adapter=adapter
    }

    override fun initData() {
        val calendar = GregorianCalendar.getInstance()
        lifecycleScope.launch (Dispatchers.IO){
            AppBaseDatabase.instance.historyDao?.all?.sortedByDescending { it.time }?.groupBy {
                calendar.time = Date(it.time)
                calendar.get(Calendar.DAY_OF_MONTH)
            }?.forEach {
                adapter.addData(DayBean("${it.key} ${DataUtil.name[calendar.get(Calendar.MONTH)+1]}"))
                it.value.forEach {
                    adapter.addData(it)
                }
            }
        }
    }

}