package com.testprep.activity

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.testprep.R
import com.testprep.adapter.FilterListAdapter
import com.testprep.fragments.OtherFilterFragment
import com.testprep.fragments.PriceFilterFragment
import com.testprep.interfaces.FilterTypeSelectionInteface
import com.testprep.models.PackageData
import com.testprep.utils.AppConstants
import com.testprep.utils.Utils
import kotlinx.android.synthetic.main.activity_filter.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class FilterActivity : AppCompatActivity(), FilterTypeSelectionInteface {

    var filterTypeArr: ArrayList<PackageData.PackageDataList> = ArrayList()
    var filterTypeSelectionInteface: FilterTypeSelectionInteface? = null
    var filterAdapter: FilterListAdapter? = null

    var coursetypeid = "1"

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setContentView(R.layout.activity_filter)

        filterTypeSelectionInteface = this

        filter_rvList.layoutManager = LinearLayoutManager(this@FilterActivity, LinearLayoutManager.VERTICAL, false)

        if (Utils.getStringValue(this@FilterActivity, AppConstants.COURSE_TYPE_ID, "") == "1") {
            filter_rbBoards.isChecked = true

            val pp1: PackageData.PackageDataList = PackageData.PackageDataList(0, "Boards")
            pp1.isSelected = false
            filterTypeArr.add(pp1)

        } else {
            filter_rbCompetitive.isChecked = true

            val pp1: PackageData.PackageDataList = PackageData.PackageDataList(0, "Competitive Exams")
            pp1.isSelected = false
            filterTypeArr.add(pp1)
        }

        val pp2: PackageData.PackageDataList = PackageData.PackageDataList(0, "Standard")
        pp2.isSelected = false
        filterTypeArr.add(pp2)

        val pp3: PackageData.PackageDataList = PackageData.PackageDataList(0, "Subjects")
        pp3.isSelected = false
        filterTypeArr.add(pp3)

        val pp4: PackageData.PackageDataList = PackageData.PackageDataList(0, "Tutor")
        pp4.isSelected = false
        filterTypeArr.add(pp4)

        val pp5: PackageData.PackageDataList = PackageData.PackageDataList(0, "Price")
        pp5.isSelected = false
        filterTypeArr.add(pp5)

        filterAdapter = FilterListAdapter(this@FilterActivity, filterTypeArr, filterTypeSelectionInteface!!)
        filter_rvList.adapter = filterAdapter
        filterAdapter!!.notifyDataSetChanged()

        getType("adapter", 0, 0)

        filter_rgCourseType.setOnCheckedChangeListener { group, checkedId ->

            if (checkedId == R.id.filter_rbBoards) {

                filterTypeArr = ArrayList()

                coursetypeid = "1"

                Utils.setStringValue(this@FilterActivity, AppConstants.COURSE_TYPE_ID, coursetypeid)

                val pp1: PackageData.PackageDataList = PackageData.PackageDataList(0, "Boards")
                pp1.isSelected = false
                filterTypeArr.add(pp1)

                val pp2: PackageData.PackageDataList = PackageData.PackageDataList(0, "Standard")
                pp2.isSelected = false
                filterTypeArr.add(pp2)

                val pp3: PackageData.PackageDataList = PackageData.PackageDataList(0, "Subjects")
                pp3.isSelected = false
                filterTypeArr.add(pp3)

                val pp4: PackageData.PackageDataList = PackageData.PackageDataList(0, "Tutor")
                pp4.isSelected = false
                filterTypeArr.add(pp4)

                val pp5: PackageData.PackageDataList = PackageData.PackageDataList(0, "Price")
                pp5.isSelected = false
                filterTypeArr.add(pp5)


                filterAdapter = FilterListAdapter(this@FilterActivity, filterTypeArr, filterTypeSelectionInteface!!)
                filter_rvList.adapter = filterAdapter
                filterAdapter!!.notifyDataSetChanged()

                var fragment = OtherFilterFragment()
                val bundle = Bundle()
                bundle.putString("type", "boards")
                bundle.putString("coursetype", coursetypeid)
                fragment.arguments = bundle
                supportFragmentManager.beginTransaction().replace(R.id.filter_container, fragment).commit()

            } else if (checkedId == R.id.filter_rbCompetitive) {

                filterTypeArr = ArrayList()

                coursetypeid = "2"

                Utils.setStringValue(this@FilterActivity, AppConstants.COURSE_TYPE_ID, coursetypeid)

                val pp1: PackageData.PackageDataList = PackageData.PackageDataList(0, "Competitive Exams")
                pp1.isSelected = false
                filterTypeArr.add(pp1)

                val pp3: PackageData.PackageDataList = PackageData.PackageDataList(0, "Subjects")
                pp3.isSelected = false
                filterTypeArr.add(pp3)

                val pp4: PackageData.PackageDataList = PackageData.PackageDataList(0, "Tutor")
                pp4.isSelected = false
                filterTypeArr.add(pp4)

                val pp5: PackageData.PackageDataList = PackageData.PackageDataList(0, "Price")
                pp5.isSelected = false
                filterTypeArr.add(pp5)

                filterAdapter = FilterListAdapter(this@FilterActivity, filterTypeArr, filterTypeSelectionInteface!!)
                filter_rvList.adapter = filterAdapter
                filterAdapter!!.notifyDataSetChanged()

                var fragment = OtherFilterFragment()
                val bundle = Bundle()
                bundle.putString("type", "competitive_exams")
                bundle.putString("coursetype", coursetypeid)
                fragment.arguments = bundle
                supportFragmentManager.beginTransaction().replace(R.id.filter_container, fragment).commit()

            }

        }

        filter_ivBack.setOnClickListener {

            finish()

        }
    }

    override fun getType(itype: String, p0: Int, p1: Int) {

        var fragment = OtherFilterFragment()
        val bundle = Bundle()
        bundle.putString("coursetype", coursetypeid)

        when {
//            filterTypeArr[p0] == "Course Type" -> {
//
//                bundle.putString("type", "course_type")
//                fragment.arguments = bundle
//                supportFragmentManager.beginTransaction().replace(R.id.filter_container, fragment).commit()
//            }

            filterTypeArr[p1].TestPackageName == "Boards" -> {

                bundle.putString("type", "boards")
                fragment.arguments = bundle
                supportFragmentManager.beginTransaction().replace(R.id.filter_container, fragment).commit()
            }

            filterTypeArr[p1].TestPackageName == "Competitive Exams" -> {

                bundle.putString("type", "competitive_exams")
                fragment.arguments = bundle

                supportFragmentManager.beginTransaction().replace(R.id.filter_container, fragment).commit()
            }

            filterTypeArr[p1].TestPackageName == "Standard" -> {

                bundle.putString("type", "standards")
                fragment.arguments = bundle

                supportFragmentManager.beginTransaction().replace(R.id.filter_container, fragment).commit()
            }

            filterTypeArr[p1].TestPackageName == "Subjects" -> {

                bundle.putString("type", "subjects")
                fragment.arguments = bundle

                supportFragmentManager.beginTransaction().replace(R.id.filter_container, fragment).commit()
            }

            filterTypeArr[p1].TestPackageName == "Tutor" -> {

                bundle.putString("type", "tutor")
                fragment.arguments = bundle

                supportFragmentManager.beginTransaction().replace(R.id.filter_container, fragment).commit()
            }

            filterTypeArr[p1].TestPackageName == "Price" -> {

                bundle.putString("type", "price")
                fragment.arguments = bundle

                supportFragmentManager.beginTransaction().replace(R.id.filter_container, fragment).commit()
            }


            filterTypeArr[p1].TestPackageName == "Price1" -> supportFragmentManager.beginTransaction().add(
                R.id.filter_container,
                PriceFilterFragment()
            ).commit()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}
