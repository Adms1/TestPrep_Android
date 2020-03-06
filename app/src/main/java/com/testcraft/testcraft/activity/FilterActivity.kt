package com.testcraft.testcraft.activity

import android.content.Context
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.testcraft.testcraft.Connectivity
import com.testcraft.testcraft.R
import com.testcraft.testcraft.adapter.FilterListAdapter
import com.testcraft.testcraft.fragments.OtherFilterFragment
import com.testcraft.testcraft.interfaces.FilterTypeSelectionInteface
import com.testcraft.testcraft.models.PackageData
import com.testcraft.testcraft.utils.ActionIdData
import com.testcraft.testcraft.utils.AppConstants
import com.testcraft.testcraft.utils.CommonWebCalls
import com.testcraft.testcraft.utils.Utils
import kotlinx.android.synthetic.main.activity_filter.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class FilterActivity : AppCompatActivity(), FilterTypeSelectionInteface {

    var filterTypeArr: ArrayList<PackageData.PackageDataList> = ArrayList()
    var filterTypeSelectionInteface: FilterTypeSelectionInteface? = null
    var filterAdapter: FilterListAdapter? = null

    var coursetypeid = "1"
    var filtertypeid = ""

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    var connectivity: Connectivity? = null

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter()
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        registerReceiver(connectivity, filter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(connectivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        setContentView(R.layout.activity_filter)

        connectivity = Connectivity()

        CommonWebCalls.callToken(
            this@FilterActivity,
            "1",
            "",
            ActionIdData.C1000,
            ActionIdData.T1000
        )

        filterTypeSelectionInteface = this

        filtertypeid = intent.getStringExtra("filtertypeid")

        filter_rvList.layoutManager =
            LinearLayoutManager(this@FilterActivity, LinearLayoutManager.VERTICAL, false)

        if (AppConstants.FILTER_COURSE_TYPE_ID == "0" || AppConstants.FILTER_COURSE_TYPE_ID == "") {
            AppConstants.FILTER_COURSE_TYPE_ID =
                Utils.getStringValue(this@FilterActivity, AppConstants.COURSE_TYPE_ID, "")!!
        }

        if (AppConstants.FILTER_COURSE_TYPE_ID == "1") {

            AppConstants.FILTER_COURSE_TYPE_ID = "1"
            filter_rbBoards.isChecked = true

            val pp1: PackageData.PackageDataList = PackageData.PackageDataList(0, "Boards")
            pp1.isSelected = false
            filterTypeArr.add(pp1)

            val pp2: PackageData.PackageDataList = PackageData.PackageDataList(0, "Standard")
            pp2.isSelected = false
            filterTypeArr.add(pp2)

            val pp3: PackageData.PackageDataList = PackageData.PackageDataList(0, "Subjects")
            pp3.isSelected = false
            filterTypeArr.add(pp3)

        } else {
            filter_rbCompetitive.isChecked = true
            AppConstants.FILTER_COURSE_TYPE_ID = "2"

            val pp1: PackageData.PackageDataList = PackageData.PackageDataList(0, "Course")
            pp1.isSelected = false
            filterTypeArr.add(pp1)
        }

        val pp4: PackageData.PackageDataList = PackageData.PackageDataList(0, "Tutor")
        pp4.isSelected = false
        filterTypeArr.add(pp4)

        val pp5: PackageData.PackageDataList = PackageData.PackageDataList(0, "Price")
        pp5.isSelected = false
        filterTypeArr.add(pp5)

        filterAdapter =
            FilterListAdapter(this@FilterActivity, filterTypeArr, filterTypeSelectionInteface!!)
        filter_rvList.adapter = filterAdapter
        filterAdapter!!.notifyDataSetChanged()

        getType("adapter", 0, 0)

        filter_rgCourseType.setOnCheckedChangeListener { group, checkedId ->

            if (checkedId == R.id.filter_rbBoards) {

                filterTypeArr = ArrayList()

//                AppConstants.FILTER_STANDARD_ID = Utils.getStringValue(this@FilterActivity, AppConstants.STANDARD_ID, "")!!
//                AppConstants.FILTER_SUBJECT_ID = Utils.getStringValue(this@FilterActivity, AppConstants.SUBJECT_ID, "")!!
//                AppConstants.FILTER_TUTOR_ID = "111"
//                AppConstants.FILTER_BOARD_ID = Utils.getStringValue(this@FilterActivity, AppConstants.COURSE_ID, "")!!

                AppConstants.FILTER_COURSE_TYPE_ID = "1"
                AppConstants.FILTER_BOARD_ID = ""
                AppConstants.FILTER_STANDARD_ID = ""
                AppConstants.FILTER_SUBJECT_ID = ""
                coursetypeid = "1"

//                Utils.setStringValue(this@FilterActivity, AppConstants.COURSE_TYPE_ID, coursetypeid)

                val pp11: PackageData.PackageDataList = PackageData.PackageDataList(0, "Boards")
                pp11.isSelected = false
                filterTypeArr.add(pp11)

                val pp22: PackageData.PackageDataList = PackageData.PackageDataList(0, "Standard")
                pp22.isSelected = false
                filterTypeArr.add(pp22)

                val pp33: PackageData.PackageDataList = PackageData.PackageDataList(0, "Subjects")
                pp33.isSelected = false
                filterTypeArr.add(pp33)

                val pp44: PackageData.PackageDataList = PackageData.PackageDataList(0, "Tutor")
                pp44.isSelected = false
                filterTypeArr.add(pp44)

                val pp55: PackageData.PackageDataList = PackageData.PackageDataList(0, "Price")
                pp55.isSelected = false
                filterTypeArr.add(pp55)

                filterAdapter = FilterListAdapter(
                    this@FilterActivity,
                    filterTypeArr,
                    filterTypeSelectionInteface!!)

                filter_rvList.adapter = filterAdapter
                filterAdapter!!.notifyDataSetChanged()

                val fragment = OtherFilterFragment()
                val bundle = Bundle()
                bundle.putString("type", "boards")
                bundle.putString("coursetype", coursetypeid)
                bundle.putString("filtertypeid", filtertypeid)
                fragment.arguments = bundle
                supportFragmentManager.beginTransaction().replace(R.id.filter_container, fragment).commit()

            } else if (checkedId == R.id.filter_rbCompetitive) {

                filterTypeArr = ArrayList()

//                AppConstants.FILTER_STANDARD_ID = "111"
//                AppConstants.FILTER_SUBJECT_ID = "111"
//                AppConstants.FILTER_TUTOR_ID = "111"
//                AppConstants.FILTER_BOARD_ID = Utils.getStringValue(this@FilterActivity, AppConstants.COURSE_ID, "")!!

                AppConstants.FILTER_COURSE_TYPE_ID = "2"
                AppConstants.FILTER_BOARD_ID = ""
                coursetypeid = "2"

//                Utils.setStringValue(this@FilterActivity, AppConstants.COURSE_TYPE_ID, coursetypeid)

                val pp11: PackageData.PackageDataList = PackageData.PackageDataList(0, "Course")
                pp11.isSelected = true
                filterTypeArr.add(pp11)

//                val pp3: PackageData.PackageDataList = PackageData.PackageDataList(0, "Subjects")
//                pp3.isSelected = false
//                filterTypeArr.add(pp3)

                val pp44: PackageData.PackageDataList = PackageData.PackageDataList(0, "Tutor")
                pp44.isSelected = false
                filterTypeArr.add(pp44)

                val pp55: PackageData.PackageDataList = PackageData.PackageDataList(0, "Price")
                pp55.isSelected = false
                filterTypeArr.add(pp55)

                filterAdapter = FilterListAdapter(
                    this@FilterActivity,
                    filterTypeArr,
                    filterTypeSelectionInteface!!
                )
                filter_rvList.adapter = filterAdapter
                filterAdapter!!.notifyDataSetChanged()

                val fragment = OtherFilterFragment()
                val bundle = Bundle()
                bundle.putString("type", "competitive_exams")
                bundle.putString("coursetype", coursetypeid)
                bundle.putString("filtertypeid", filtertypeid)
                fragment.arguments = bundle
                supportFragmentManager.beginTransaction().replace(R.id.filter_container, fragment)
                    .commit()

            }

        }

        filter_ivBack.setOnClickListener {

            finish()

        }
    }

    override fun getType(itype: String, p0: Int, p1: Int) {

//        AppConstants.FILTER_STANDARD_ID = Utils.getStringValue(this@FilterActivity, AppConstants.STANDARD_ID, "")!!
//        AppConstants.FILTER_SUBJECT_ID = Utils.getStringValue(this@FilterActivity, AppConstants.SUBJECT_ID, "")!!
//        AppConstants.FILTER_TUTOR_ID = "111"
//        AppConstants.FILTER_BOARD_ID = Utils.getStringValue(this@FilterActivity, AppConstants.COURSE_ID, "")!!

        val fragment = OtherFilterFragment()
        val bundle = Bundle()
        bundle.putString("coursetype", coursetypeid)
        bundle.putString("filtertypeid", filtertypeid)

        when (filterTypeArr[p1].TestPackageName) {
            "Boards" -> {

                bundle.putString("type", "boards")
                fragment.arguments = bundle
                supportFragmentManager.beginTransaction().replace(R.id.filter_container, fragment)
                    .commit()
            }
            "Course" -> {

                bundle.putString("type", "competitive_exams")
                fragment.arguments = bundle

                supportFragmentManager.beginTransaction().replace(R.id.filter_container, fragment)
                    .commit()
            }
            "Standard" -> {

                bundle.putString("type", "standards")
                fragment.arguments = bundle

                supportFragmentManager.beginTransaction().replace(R.id.filter_container, fragment)
                    .commit()
            }
            "Subjects" -> {

                bundle.putString("type", "subjects")
                fragment.arguments = bundle

                supportFragmentManager.beginTransaction().replace(R.id.filter_container, fragment)
                    .commit()
            }
            "Tutor" -> {

                bundle.putString("type", "tutor")
                fragment.arguments = bundle

                supportFragmentManager.beginTransaction().replace(R.id.filter_container, fragment)
                    .commit()
            }
            "Price" -> {

                bundle.putString("type", "price")
                fragment.arguments = bundle

                supportFragmentManager.beginTransaction().replace(R.id.filter_container, fragment)
                    .commit()
            }


            //            filterTypeArr[p1].TestPackageName == "Price1" -> supportFragmentManager.beginTransaction().add(
            //                R.id.filter_container,
            ////                PriceFilterFragment()
            //            ).commit()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}
