package com.example.bean

import com.chad.library.adapter.base.entity.MultiItemEntity

data class DayBean(val title: String):MultiItemEntity {
    override val itemType: Int
        get() = 0
}