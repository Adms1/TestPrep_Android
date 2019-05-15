package com.testprep.activity

import adapter.ChooseCoarseAdapter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.testprep.models.GetCourseListData
import com.testprep.models.MainModel
import com.testprep.retrofit.WebClient
import com.testprep.retrofit.WebInterface
import com.testprep.utils.AppConstants
import com.testprep.utils.DialogUtils
import com.testprep.utils.Utils
import kotlinx.android.synthetic.main.activity_standard_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SelectSubjectActivity : AppCompatActivity() {

    private var chooseCoarseAdapter: ChooseCoarseAdapter? = null
    private var courseId = ""
    private var selectType = "no"
    private var standardList: ArrayList<GetCourseListData> = ArrayList()
    private var selectType1 = "no"
    private var stdId = ""
    var apitype = ""
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
        subject_tvFlow.text = AppConstants.COURSE_FLOW

        if (intent != null) {
            courseId = intent.extras!!.getString("course_id", "")
        }

        subject_rvList.layoutManager = GridLayoutManager(this@SelectSubjectActivity, 2)

        subject_ivBack.setOnClickListener {

            onBackPressed()
        }

        subject_chbAll.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked) {
                /* perform logic */
                selectType = "yes"

                chooseCoarseAdapter =
                    ChooseCoarseAdapter(this@SelectSubjectActivity, apitype, subjectList, selectType)
                subject_rvList.adapter = chooseCoarseAdapter

            } else {
                selectType = "no"

                chooseCoarseAdapter =
                    ChooseCoarseAdapter(this@SelectSubjectActivity, apitype, subjectList, selectType)
                subject_rvList.adapter = chooseCoarseAdapter
            }
        }

//        if(!AppConstants.COURSE_FLOW_ARRAY[0].equals("board", true)) {
        callSubjectList()
//        }else{
//            callStandardList()
//        }
    }

    fun callSubjectList() {

        if (!DialogUtils.isNetworkConnected(this@SelectSubjectActivity)) {
            Utils.ping(this@SelectSubjectActivity, "Connetion not available")
        }

        DialogUtils.showDialog(this@SelectSubjectActivity)
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        var call: Call<MainModel>? = null

        if (Utils.getStringValue(this@SelectSubjectActivity, "course_type_id", "") != "1") {
            call = apiService.getCourseSubjectList(courseId)
            apitype = "course_subject"

        } else {
            call = apiService.getBoardStandardSubjectList("1", courseId)
            apitype = "course_stdSubject"
            subject_chbAll.visibility = View.GONE

        }

        call.enqueue(object : Callback<MainModel> {
            override fun onResponse(call: Call<MainModel>, response: Response<MainModel>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.Status == "true") {

                        subjectList = response.body()!!.data

                        chooseCoarseAdapter =
                            ChooseCoarseAdapter(
                                this@SelectSubjectActivity,
                                apitype,
                                response.body()!!.data,
                                selectType
                            )
                        subject_rvList.adapter = chooseCoarseAdapter

                        Log.d("flow", Utils.getStringValue(this@SelectSubjectActivity, AppConstants.COURSE_FLOW, ""))

                        val sb = StringBuilder()
                        for (s in AppConstants.COURSE_FLOW_ARRAY) {
                            sb.append(s)
                            sb.append(" > ")
                            AppConstants.COURSE_FLOW = sb.toString()
                        }
                        subject_tvFlow.text = AppConstants.COURSE_FLOW

                    } else {

                        Toast.makeText(this@SelectSubjectActivity, response.body()!!.Msg, Toast.LENGTH_SHORT)
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
