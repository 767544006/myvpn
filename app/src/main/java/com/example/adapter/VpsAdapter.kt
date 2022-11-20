package com.example.adapter

import android.graphics.BitmapFactory
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.bean.VpsBeanItem
import com.example.vpn.R

class VpsAdapter:BaseQuickAdapter<VpsBeanItem,BaseViewHolder>(R.layout.item_server) {
    override fun convert(holder: BaseViewHolder, item: VpsBeanItem) {
        holder.setText(R.id.title,item.vpsname)
        if (item.isSelect){
            holder.setImageResource(R.id.checkbox,R.drawable.ic_selected_round)
        }else{
            holder.setImageResource(R.id.checkbox,R.drawable.ic_unselected_round)
        }
        context.assets.open("${item.local}.webp").use {
            holder.getView<ImageView>(R.id.profile_image).setImageBitmap(BitmapFactory.decodeStream(it))
        }
    }
}