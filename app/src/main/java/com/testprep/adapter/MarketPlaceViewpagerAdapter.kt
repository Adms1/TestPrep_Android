package com.testprep.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.testprep.fragments.*

class MarketPlaceViewpagerAdapter(fm: FragmentManager, private var tabCount: Int, var come: String) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {

        return if (come == "market place") {
            when (position) {
                1 -> SingleTestFragment()
                0 -> TestPackageFragment()
                2 -> TutorsFragment()
                else -> null
            }
        } else {
            when (position) {
                0 -> TutorProfileFragment()
                1 -> TutorPackagesFragment()
                2 -> TutorsReviewFragment()
                else -> null
            }
        }
    }

    override fun getCount(): Int {
        return tabCount
    }
}
