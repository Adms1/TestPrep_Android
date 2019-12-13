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
import kotlinx.android.synthetic.main.activity_select_standard.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class SelectStandardActivity : AppCompatActivity() {

    private var chooseCoarseAdapter: ChooseCoarseAdapter? = null
    private var standardList: ArrayList<PackageData.PackageDataList> = ArrayList()
    private var selectType = "no"
    private var stdId = ""

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

        setContentView(R.layout.activity_select_standard)

        if (intent != null) {
            stdId = intent.extras!!.getString("course_id", "")
        }

        standard_rvList.layoutManager = GridLayoutManager(this@SelectStandardActivity, 2)

        standard_ivBack.setOnClickListener {
            onBackPressed()
        }

        callStandardList()
    }

    fun callStandardList() {

        if (!DialogUtils.isNetworkConnected(this@SelectStandardActivity)) {
            Utils.ping(this@SelectStandardActivity, "Connetion not available")
        }

        DialogUtils.showDialog(this@SelectStandardActivity)
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        var call = apiService.getBoardStandardList(stdId)

        call.enqueue(object : Callback<PackageData> {
            override fun onResponse(call: Call<PackageData>, response: Response<PackageData>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.Status == "true") {

                        standardList = response.body()!!.data

                        chooseCoarseAdapter =
                            ChooseCoarseAdapter(
                                this@SelectStandardActivity,
                                "course_standard",
                                response.body()!!.data,
                                selectType
                            )
                        standard_rvList.adapter = chooseCoarseAdapter

                        Log.d(
                            "flow",
                            Utils.getStringValue(
                                this@SelectStandardActivity,
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
                        standard_tvFlow.text = AppConstants.COURSE_FLOW

                    } else {

                        Toast.makeText(
                            this@SelectStandardActivity,
                            response.body()!!.Msg,
                            Toast.LENGTH_SHORT
                        )
                            .show()
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
