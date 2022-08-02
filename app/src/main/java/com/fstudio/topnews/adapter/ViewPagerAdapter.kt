package com.fstudio.topnews.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fstudio.topnews.fragments.*

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    var title =
        arrayOf("All", "Business", "Science", "Technology", "Entertainment", "Health", "Sports")

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return All()
            1 -> return Business()
            2 -> return Science()
            3 -> return Technology()
            4 -> return Entertainment()
            5 -> return Health()
            6 -> return Sports()
        }
        return All()
    }

    override fun getItemCount(): Int {
        return title.size
    }
}