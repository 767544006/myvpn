package com.example.bean

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.chad.library.adapter.base.entity.MultiItemEntity
import kotlin.time.Duration
@Entity

data class HistoryBean(val time:Long, val duration: Int, val local:String,@PrimaryKey(autoGenerate = true)
val uid:Int=0):MultiItemEntity{
    override val itemType: Int
        get() = 1

}