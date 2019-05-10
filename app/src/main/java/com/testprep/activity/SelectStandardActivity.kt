package com.testprep.activity

import adapter.ChooseCoarseAdapter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import com.testprep.R
import com.testprep.utils.AppConstants
import kotlinx.android.synthetic.main.activity_course_type.*

class SelectStandardActivity : AppCompatActivity() {

    private var chooseCoarseAdapter: ChooseCoarseAdapter? = null
    private var courseType = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_standard_fragment)

        val sb = StringBuilder()
        for (s in AppConstants.COURSE_FLOW_ARRAY) {
            sb.append(s)
            sb.append(" > ")
            AppConstants.COURSE_FLOW = sb.toString()
        }
        course_type_tvFlow.text = AppConstants.COURSE_FLOW

        if (intent != null) {
            courseType = intent.extras.getString("course_type", "")
        }

        coarse_rvCoarseList.layoutManager = GridLayoutManager(this@SelectStandardActivity, 2)

        course_type_ivBack.setOnClickListener {

            onBackPressed()

        }

    }

    override fun onBackPressed() {
        super.onBackPressed()

        if (AppConstants.COURSE_FLOW_ARRAY.size > 0) {
            AppConstants.COURSE_FLOW_ARRAY.removeAt(AppConstants.COURSE_FLOW_ARRAY.size - 1)
        }

    }

}
