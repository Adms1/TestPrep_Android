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
import kotlinx.android.synthetic.main.activity_filter.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class FilterActivity : AppCompatActivity(), FilterTypeSelectionInteface {

    val filterTypeArr: ArrayList<String> = ArrayList()
    var filterTypeSelectionInteface: FilterTypeSelectionInteface? = null

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setContentView(R.layout.activity_filter)

        filterTypeSelectionInteface = this

        filter_rvList.layoutManager = LinearLayoutManager(this@FilterActivity, LinearLayoutManager.VERTICAL, false)

//        filterTypeArr.add("Standards")
//        filterTypeArr.add("Subjects")
//        filterTypeArr.add("Popular")
//        filterTypeArr.add("Price")
//        filterTypeArr.add("Reviews & Ratings")
//        filterTypeArr.add("discount")
//        filterTypeArr.add("Promoted")

        filterTypeArr.add("Course Type")
        filterTypeArr.add("Boards")
        filterTypeArr.add("Competitive Exams")
        filterTypeArr.add("Standard")
        filterTypeArr.add("Subjects")
        filterTypeArr.add("Price")

        val filterAdapter = FilterListAdapter(this@FilterActivity, filterTypeArr, filterTypeSelectionInteface!!)
        filter_rvList.adapter = filterAdapter

        var fragment = OtherFilterFragment()
        val bundle = Bundle()
        bundle.putString("type", "course_type")
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction().add(R.id.filter_container, fragment).commit()

        filter_ivBack.setOnClickListener { onBackPressed() }
    }

    override fun getType(p0: Int) {


        var fragment = OtherFilterFragment()
        val bundle = Bundle()

        when {
            filterTypeArr[p0] == "Course Type" -> {

                bundle.putString("type", "course_type")
                fragment.arguments = bundle
                supportFragmentManager.beginTransaction().replace(R.id.filter_container, fragment).commit()
            }

            filterTypeArr[p0] == "Boards" -> {

                bundle.putString("type", "boards")
                fragment.arguments = bundle
                supportFragmentManager.beginTransaction().replace(R.id.filter_container, fragment).commit()
            }

            filterTypeArr[p0] == "Competitive Exams" -> {

                bundle.putString("type", "competitive_exams")
                fragment.arguments = bundle

                supportFragmentManager.beginTransaction().replace(R.id.filter_container, fragment).commit()
            }

            filterTypeArr[p0] == "Standard" -> {

                bundle.putString("type", "standards")
                fragment.arguments = bundle

                supportFragmentManager.beginTransaction().replace(R.id.filter_container, fragment).commit()
            }

            filterTypeArr[p0] == "Subjects" -> {

                bundle.putString("type", "subjects")
                fragment.arguments = bundle

                supportFragmentManager.beginTransaction().replace(R.id.filter_container, fragment).commit()
            }

            filterTypeArr[p0] == "Price" -> supportFragmentManager.beginTransaction().add(
                R.id.filter_container,
                PriceFilterFragment()
            ).commit()
        }
    }

}
