package com.donate.us.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.donate.us.viewpagers.DonateHistory
import com.donate.us.viewpagers.DonateNow
import com.donate.us.viewpagers.HomePage

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> {
                HomePage()
            }

            1 -> {
                DonateNow()
            }

            2 -> {
                DonateHistory()
            }

            else -> {
                Fragment()
            }
        }
    }
}
