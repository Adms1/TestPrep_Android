package com.testprep.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.testprep.R
import com.testprep.adapter.TestPackagesAdapter
import com.testprep.adapter.TutorsAdapter
import com.testprep.models.PackageData
import com.testprep.retrofit.WebClient
import com.testprep.retrofit.WebInterface
import com.testprep.utils.AppConstants
import com.testprep.utils.DialogUtils
import com.testprep.utils.Utils
import com.testprep.utils.WebRequests
import kotlinx.android.synthetic.main.activity_tutor_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class TutorDetailActivity : AppCompatActivity() {

    var data: ArrayList<PackageData.PackageDataList> = ArrayList()

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setContentView(R.layout.activity_tutor_detail)

        if (intent.hasExtra("parr")) {
            data = intent.getSerializableExtra("parr") as ArrayList<PackageData.PackageDataList>
        }

        tutor_detail_ivBack.setOnClickListener {
            AppConstants.isFirst = 0
            onBackPressed()
        }

//        tutor_detail_ivCart.setOnClickListener {
//            val intent = Intent(this@TutorDetailActivity, CartActivity::class.java)
//            startActivity(intent)
//        }

        tutor_detail_header.text = intent.getStringExtra("pname")

        if (intent.getStringExtra("type") == "pkg" || intent.getStringExtra("type") == "explore") {

            tutor_detail_ivFilter.visibility = View.VISIBLE

            tutor_detail_tvFilter.visibility = View.VISIBLE
            tutor_packages_rvPopularPkg.layoutManager = GridLayoutManager(this@TutorDetailActivity, 2)
            callFilterListApi()

        } else if (intent.getStringExtra("type") == "tutor") {

            tutor_detail_ivFilter.visibility = View.GONE
            tutor_detail_tvFilter.visibility = View.GONE

            tutor_packages_rvPopularPkg.layoutManager =
                LinearLayoutManager(this@TutorDetailActivity, LinearLayoutManager.VERTICAL, false)
            tutor_packages_rvPopularPkg.adapter = TutorsAdapter(this@TutorDetailActivity, data)

        }

        setFilterCount()

    }

    fun onClick(v: View) {
        when (v) {

            tutor_detail_rlFilter -> {

                val intent = Intent(this@TutorDetailActivity, FilterActivity::class.java)
                startActivity(intent)
            }
        }
    }

    fun callFilterListApi() {

        if (!DialogUtils.isNetworkConnected(this@TutorDetailActivity)) {
            Utils.ping(this@TutorDetailActivity, "Connetion not available")
        }

        DialogUtils.showDialog(this@TutorDetailActivity)
        val apiService = WebClient.getClient().create(WebInterface::class.java)

//        val call = apiService.getFilterData(
//            WebRequests.getFilterParams(
//                Utils.getStringValue(activity!!, AppConstants.COURSE_TYPE_ID, "")!!,
//                "",
//                Utils.getStringValue(activity!!, AppConstants.COURSE_ID, "")!!,
//                Utils.getStringValue(activity!!, AppConstants.STANDARD_ID, "")!!,
//                Utils.getStringValue(activity!!, AppConstants.SUBJECT_ID, "")!!,
//                Utils.getStringValue(activity!!, AppConstants.TUTOR_ID, "")!!,
//                "",
//                ""
//            )
//        )

        val call = apiService.getFilterData(
            WebRequests.getFilterParams(
                Utils.getStringValue(this@TutorDetailActivity, AppConstants.COURSE_TYPE_ID, "")!!,
                "",
                intent.getStringExtra("boardid"),
                intent.getStringExtra("stdid"),
                intent.getStringExtra("subid"),
                intent.getStringExtra("tutorid"),
                "",
                "",
                ""
            )
//            WebRequests.getFilterParams(
//                "",
//                "",
//                "",
//                "",
//                "",
//                "",
//                "",
//                "",
//                ""
//            )
        )

        call.enqueue(object : Callback<PackageData> {
            override fun onResponse(call: Call<PackageData>, response: Response<PackageData>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.Status == "true") {

                        data = response.body()!!.data[0].TestPackage

//                        tutor_detail_header.text = response.body()!!.data[0].Name
                        tutor_packages_rvPopularPkg.adapter = TestPackagesAdapter(this@TutorDetailActivity, data)

                    } else {

                        Toast.makeText(this@TutorDetailActivity, response.body()!!.Msg, Toast.LENGTH_SHORT).show()
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

        if (intent.getStringExtra("type") == "explore") {
            finish()
        } else {
            val intent = Intent(this@TutorDetailActivity, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun setFilterCount() {

        var filterCount = 0

        if (AppConstants.FILTER_BOARD_ID != "") {
            filterCount += 1
        }

        if (AppConstants.FILTER_STANDARD_ID != "") {
            filterCount += 1
        }

        if (AppConstants.FILTER_BOARD_ID != "" && AppConstants.FILTER_BOARD_ID != "111") {
            filterCount += 1
        }

        if (AppConstants.FILTER_SUBJECT_ID != "") {
            filterCount += 1
        }

        if (AppConstants.FILTER_TUTOR_ID != "") {
            filterCount += 1
        }

        if (filterCount > 0) {
            tutor_detail_tvFilter.visibility = View.VISIBLE
            tutor_detail_tvFilter.text = filterCount.toString()
        } else {
            tutor_detail_tvFilter.visibility = View.GONE
        }

    }
}
