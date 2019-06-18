package com.testprep.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.testprep.R
import com.testprep.adapter.FilterAdapter
import com.testprep.fragments.PriceFilterFragment
import com.testprep.interfaces.FilterTypeSelectionInteface
import kotlinx.android.synthetic.main.activity_filter.*

class FilterActivity : AppCompatActivity(), FilterTypeSelectionInteface {

    val filterTypeArr: ArrayList<String> = ArrayList()
    var filterTypeSelectionInteface: FilterTypeSelectionInteface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)

        filterTypeSelectionInteface = this

        filter_rvList.layoutManager = LinearLayoutManager(this@FilterActivity, LinearLayoutManager.VERTICAL, false)

        filterTypeArr.add("Standards")
        filterTypeArr.add("Subjects")
        filterTypeArr.add("Popular")
        filterTypeArr.add("Price")
        filterTypeArr.add("Reviews & Ratings")
        filterTypeArr.add("discount")
        filterTypeArr.add("Promoted")

        val filterAdapter = FilterAdapter(this@FilterActivity, filterTypeArr, filterTypeSelectionInteface!!)
        filter_rvList.adapter = filterAdapter

        filter_ivBack.setOnClickListener { onBackPressed() }
    }

    override fun getType(p0: Int) {
        if (filterTypeArr[p0] == "Price") {

            supportFragmentManager.beginTransaction().add(R.id.filter_container, PriceFilterFragment()).commit()
        }
    }

}
