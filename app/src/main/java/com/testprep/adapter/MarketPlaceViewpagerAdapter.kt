package com.testprep.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.testprep.fragments.SingleTestFragment
import com.testprep.fragments.TestPackageFragment
import com.testprep.fragments.TutorsFragment

class MarketPlaceViewpagerAdapter(fm: FragmentManager, private var tabCount: Int) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {

        return when (position) {
            1 -> SingleTestFragment()
            0 -> TestPackageFragment()
            2 -> TutorsFragment()
            else -> null
        }
    }

    override fun getCount(): Int {
        return tabCount
    }
}
