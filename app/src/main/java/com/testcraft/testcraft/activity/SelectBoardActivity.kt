package com.testcraft.testcraft.activity

import adapter.ChooseCoarseAdapter
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.testcraft.testcraft.R
import com.testcraft.testcraft.models.PackageData
import com.testcraft.testcraft.retrofit.WebClient
import com.testcraft.testcraft.retrofit.WebInterface
import com.testcraft.testcraft.utils.AppConstants
import com.testcraft.testcraft.utils.DialogUtils
import com.testcraft.testcraft.utils.Utils
import kotlinx.android.synthetic.main.activity_course_type.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper


class SelectBoardActivity : AppCompatActivity() {

    private var chooseCoarseAdapter: ChooseCoarseAdapter? = null
    private var courseType = ""
    private var courseName = ""

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setContentView(R.layout.activity_course_type)

        val sb = StringBuilder()
        for (s in AppConstants.COURSE_FLOW_ARRAY) {
            sb.append(s)
            sb.append(" > ")
            AppConstants.COURSE_FLOW = sb.toString()
        }

        course_type_tvFlow.text = AppConstants.COURSE_FLOW

        if (intent != null) {
            courseType = intent.extras!!.getString("course_type", "")
            courseName = intent.extras!!.getString("course_name", "")
        }

        coarse_rvCoarseList.layoutManager = GridLayoutManager(this@SelectBoardActivity, 2)

        course_type_ivBack.setOnClickListener {

            onBackPressed()
        }

        callCourseTypeList()

    }

    fun callCourseTypeList() {

        if (!DialogUtils.isNetworkConnected(this@SelectBoardActivity)) {
            Utils.ping(this@SelectBoardActivity, "Connection not available")
        }

        DialogUtils.showDialog(this@SelectBoardActivity)
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getCourseTypeList(courseType)
        call.enqueue(object : Callback<PackageData> {
            override fun onResponse(call: Call<PackageData>, response: Response<PackageData>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.Status == "true") {

                        chooseCoarseAdapter =
                            ChooseCoarseAdapter(
                                this@SelectBoardActivity,
                                "course_type",
                                response.body()!!.data,
                                "no"
                            )
                        coarse_rvCoarseList.adapter = chooseCoarseAdapter

                        Log.d(
                            "flow",
                            Utils.getStringValue(
                                this@SelectBoardActivity,
                                AppConstants.COURSE_FLOW,
                                ""
                            )
                        )

                        val sb = StringBuilder()
                        for (s in AppConstants.COURSE_FLOW_ARRAY) {
                            sb.append(s)
                            sb.append(" > ")
                            AppConstants.COURSE_FLOW = sb.toString()
                        }
                        course_type_tvFlow.text = AppConstants.COURSE_FLOW

                    } else {

                        Toast.makeText(
                            this@SelectBoardActivity,
                            response.body()!!.Msg,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<PackageData>, t: Throwable) {
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
