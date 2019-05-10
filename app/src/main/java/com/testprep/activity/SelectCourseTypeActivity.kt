package com.testprep.activity

import adapter.ChooseCoarseAdapter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.widget.Toast
import com.testprep.R
import com.testprep.models.MainModel
import com.testprep.retrofit.WebClient
import com.testprep.retrofit.WebInterface
import com.testprep.utils.AppConstants
import com.testprep.utils.DialogUtils
import com.testprep.utils.Utils
import kotlinx.android.synthetic.main.activity_course_type.*
import kotlinx.android.synthetic.main.fragment_choose_coarse.coarse_rvCoarseList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SelectCourseTypeActivity : AppCompatActivity() {

    private var chooseCoarseAdapter: ChooseCoarseAdapter? = null
    private var courseType = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_type)

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

        coarse_rvCoarseList.layoutManager = GridLayoutManager(this@SelectCourseTypeActivity, 2)

        course_type_ivBack.setOnClickListener {

            onBackPressed()

        }

        callCourseTypeList()

    }

    fun callCourseTypeList() {

        if (!DialogUtils.isNetworkConnected(this@SelectCourseTypeActivity)) {
            Utils.ping(this@SelectCourseTypeActivity, "Connetion not available")
        }

        DialogUtils.showDialog(this@SelectCourseTypeActivity)
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getCourseTypeList(courseType)
        call.enqueue(object : Callback<MainModel> {
            override fun onResponse(call: Call<MainModel>, response: Response<MainModel>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.Status == "true") {

                        chooseCoarseAdapter =
                            ChooseCoarseAdapter(this@SelectCourseTypeActivity, "course_type", response.body()!!.data)
                        coarse_rvCoarseList.adapter = chooseCoarseAdapter

                        Log.d("flow", Utils.getStringValue(this@SelectCourseTypeActivity, AppConstants.COURSE_FLOW, ""))

                        val sb = StringBuilder()
                        for (s in AppConstants.COURSE_FLOW_ARRAY) {
                            sb.append(s)
                            sb.append(" > ")
                            AppConstants.COURSE_FLOW = sb.toString()
                        }
                        course_type_tvFlow.text = AppConstants.COURSE_FLOW

                    } else {

                        Toast.makeText(this@SelectCourseTypeActivity, response.body()!!.Msg, Toast.LENGTH_SHORT)
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
