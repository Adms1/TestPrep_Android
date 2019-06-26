package com.testprep.adapter

import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.testprep.old.PageViewFragment

class QuestionsPagerAdapter(fm: FragmentManager, val noOfTab: Int, val come: String) : FragmentStatePagerAdapter(fm) {

    private val mFragmentList: ArrayList<Fragment> = ArrayList()
    private val mFragmentTitleList: ArrayList<String> = ArrayList()

    override fun getCount(): Int {
        return noOfTab
    }

    override fun getItem(position: Int): Fragment {
        return PageViewFragment.newInstance(position, come)
    }

//    fun addFragment(fragment: Fragment) {
//        mFragmentList.add(fragment)
//    }

    @Nullable
    override fun getPageTitle(position: Int): CharSequence {
        return (position + 1).toString()
    }
}
