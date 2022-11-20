package com.example.adapter

import com.blankj.utilcode.util.TimeUtils
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.bean.DayBean
import com.example.bean.HistoryBean
import com.example.vpn.R
import java.util.*

class MultipleItemQuickAdapter(data: MutableList<MultiItemEntity>) :
    BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder>(data) {
    protected override fun convert(helper: BaseViewHolder, item: MultiItemEntity) {
        when (helper.itemViewType) {
            0 -> helper.setText(R.id.title, (item as DayBean).title)
            1 -> {
                val data = (item as HistoryBean)
                helper.setText(R.id.textView17, TimeUtils.date2String(Date(data.time), "HH:mm"))
                helper.setText(R.id.dur,"${data.duration} minutes")
                helper.setText(R.id.country," ${data.local}")
            }
        }
    }

    init {
        // 绑定 layout 对应的 type
        addItemType(0, R.layout.item_title)
        addItemType(1, R.layout.item_history)
    }
}