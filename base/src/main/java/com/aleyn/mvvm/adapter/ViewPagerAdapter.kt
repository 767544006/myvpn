package com.aleyn.mvvm.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


class ViewPagerAdapter(private val list: MutableList<Fragment>, fragmentManager: FragmentManager) :
    FragmentPagerAdapter(fragmentManager) {
    override fun getCount(): Int {
        return list.size
    }



    override fun getItem(position: Int): Fragment {
        return list[position]
    }
}