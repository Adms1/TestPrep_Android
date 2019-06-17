package com.testprep.fragments

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.testprep.R
import com.testprep.adapter.MarketPlaceViewpagerAdapter
import com.testprep.adapter.QuestionsPagerAdapter
import com.testprep.utils.Utils
import kotlinx.android.synthetic.main.fragment_market_place.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class MarketPlaceFragment : Fragment() {

    var questionpagerAdapret: QuestionsPagerAdapter? = null
    var mToolbar: Toolbar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val vieww = inflater.inflate(R.layout.fragment_market_place, container, false)

        activity!!.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        return vieww
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        market_header.text = Utils.getStringValue(activity!!, "course_type_name", "") +
                "," + Utils.getStringValue(activity!!, "course_name", "") +
                "," + Utils.getStringValue(activity!!, "standard_name", "") +
                "," + Utils.getStringValue(activity!!, "subject_name", "")

        configureTabLayout()

    }

    private fun configureTabLayout() {

        market_tabs.addTab(market_tabs.newTab().setText("Single Test"))
        market_tabs.addTab(market_tabs.newTab().setText("Test Packages"))
        market_tabs.addTab(market_tabs.newTab().setText("Tutors"))

        val adapter = MarketPlaceViewpagerAdapter(childFragmentManager, market_tabs.tabCount)
        market_viewpager.adapter = adapter

        market_viewpager.addOnPageChangeListener(
            TabLayout.TabLayoutOnPageChangeListener(market_tabs)
        )
        market_tabs.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                market_viewpager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }

        })
    }

}
