package com.testprep.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.testprep.fragments.SingleTestFragment
import com.testprep.fragments.TestPackageFragment
import com.testprep.fragments.TutorsFragment

class MarketPlaceViewpagerAdapter(fm: FragmentManager, private var tabCount: Int, var come: String) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {

        var fragment: Fragment = Fragment()

//        return if (come == "market place") {
            when (position) {
                1 -> fragment = SingleTestFragment()
                0 -> fragment = TestPackageFragment()
                2 -> fragment = TutorsFragment()

            }

        return fragment

//        } else {
//            when (position) {
//                0 -> TutorProfileFragment()
//                1 -> TutorPackagesFragment()
//                2 -> TutorsReviewFragment()
//                else -> null
//            }
//        }
//        return null
    }

    override fun getCount(): Int {
        return tabCount
    }
}
