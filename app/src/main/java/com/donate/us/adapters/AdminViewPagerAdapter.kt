package com.donate.us.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.donate.us.viewpagers.DonateHistory
import com.donate.us.viewpagers.HomePage

class AdminViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> {
                HomePage()
            }

            1 -> {
                DonateHistory()
            }

            else -> {
                Fragment()
            }
        }
    }
}
