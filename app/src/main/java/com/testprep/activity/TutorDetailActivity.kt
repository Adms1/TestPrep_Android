package com.testprep.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.testprep.R
import kotlinx.android.synthetic.main.activity_tutor_detail.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class TutorDetailActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setContentView(R.layout.activity_tutor_detail)

        tutor_detail_ivBack.setOnClickListener { onBackPressed() }

        tutor_detail_ivCart.setOnClickListener {
            val intent = Intent(this@TutorDetailActivity, CartActivity::class.java)
            startActivity(intent)
        }

//        configureTabLayout()

    }

//    private fun configureTabLayout() {
//
//        tutor_detail_tabs.addTab(tutor_detail_tabs.newTab().setText("Profile"))
//        tutor_detail_tabs.addTab(tutor_detail_tabs.newTab().setText("Packages & Test"))
//        tutor_detail_tabs.addTab(tutor_detail_tabs.newTab().setText("Review"))
//
//        val adapter = MarketPlaceViewpagerAdapter(supportFragmentManager, tutor_detail_tabs.tabCount, "tutor")
//        tutor_detail_viewpager.adapter = adapter
//
//        tutor_detail_viewpager.addOnPageChangeListener(
//            TabLayout.TabLayoutOnPageChangeListener(tutor_detail_tabs)
//        )
//        tutor_detail_tabs.addOnTabSelectedListener(object :
//            TabLayout.OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab) {
//                tutor_detail_viewpager.currentItem = tab.position
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab) {
//
//            }
//
//            override fun onTabReselected(tab: TabLayout.Tab) {
//
//            }
//
//        })
//    }


}
