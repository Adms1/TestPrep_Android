package com.testprep.activity

import adapter.ChooseCoarseAdapter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.widget.Toast
import com.testprep.models.GetCourseListData
import com.testprep.models.MainModel
import com.testprep.retrofit.WebClient
import com.testprep.retrofit.WebInterface
import com.testprep.utils.AppConstants
import com.testprep.utils.DialogUtils
import com.testprep.utils.Utils
import kotlinx.android.synthetic.main.activity_course_type.coarse_rvCoarseList
import kotlinx.android.synthetic.main.activity_course_type.course_type_ivBack
import kotlinx.android.synthetic.main.activity_course_type.course_type_tvFlow
import kotlinx.android.synthetic.main.activity_standard_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SelectStandardActivity : AppCompatActivity() {

    private var chooseCoarseAdapter: ChooseCoarseAdapter? = null
    private var courseId = ""
    private var selectType = "no"
    private var subjectList: ArrayList<GetCourseListData> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.testprep.R.layout.activity_standard_fragment)

        val sb = StringBuilder()
        for (s in AppConstants.COURSE_FLOW_ARRAY) {
            sb.append(s)
            sb.append(" > ")
            AppConstants.COURSE_FLOW = sb.toString()
        }
        course_type_tvFlow.text = AppConstants.COURSE_FLOW

        if (intent != null) {
            courseId = intent.extras.getString("course_id", "")
        }

        coarse_rvCoarseList.layoutManager = GridLayoutManager(this@SelectStandardActivity, 2)

        course_type_ivBack.setOnClickListener {

            onBackPressed()
        }

        course_type_chbAll.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                // perform logic
                selectType = "yes"

                chooseCoarseAdapter =
                    ChooseCoarseAdapter(this@SelectStandardActivity, "course_subject", subjectList, selectType)
                coarse_rvCoarseList.adapter = chooseCoarseAdapter

            } else {
                selectType = "no"

                chooseCoarseAdapter =
                    ChooseCoarseAdapter(this@SelectStandardActivity, "course_subject", subjectList, selectType)
                coarse_rvCoarseList.adapter = chooseCoarseAdapter
            }
        }

        callSubjectList()
    }

    fun callSubjectList() {

        if (!DialogUtils.isNetworkConnected(this@SelectStandardActivity)) {
            Utils.ping(this@SelectStandardActivity, "Connetion not available")
        }

        DialogUtils.showDialog(this@SelectStandardActivity)
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getCourseSubjectList(courseId)
        call.enqueue(object : Callback<MainModel> {
            override fun onResponse(call: Call<MainModel>, response: Response<MainModel>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.Status == "true") {

                        subjectList = response.body()!!.data

                        chooseCoarseAdapter =
                            ChooseCoarseAdapter(
                                this@SelectStandardActivity,
                                "course_subject",
                                response.body()!!.data,
                                selectType
                            )
                        coarse_rvCoarseList.adapter = chooseCoarseAdapter

                        Log.d("flow", Utils.getStringValue(this@SelectStandardActivity, AppConstants.COURSE_FLOW, ""))

                        val sb = StringBuilder()
                        for (s in AppConstants.COURSE_FLOW_ARRAY) {
                            sb.append(s)
                            sb.append(" > ")
                            AppConstants.COURSE_FLOW = sb.toString()
                        }
                        course_type_tvFlow.text = AppConstants.COURSE_FLOW

                    } else {

                        Toast.makeText(this@SelectStandardActivity, response.body()!!.Msg, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

            override fun onFailure(call: Call<MainModel>, t: Throwable) {
                // Log error here since request failed
                Log.e("", t.toString())
                DialogUtils.dismissDialog()
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()

        if (AppConstants.COURSE_FLOW_ARRAY.size > 0) {
            AppConstants.COURSE_FLOW_ARRAY.removeAt(AppConstants.COURSE_FLOW_ARRAY.size - 1)
        }

    }

}
