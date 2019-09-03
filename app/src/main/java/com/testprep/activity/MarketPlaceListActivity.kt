package com.testprep.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.testprep.R
import com.testprep.models.PackageData
import kotlinx.android.synthetic.main.activity_market_place_list.*
import kotlinx.android.synthetic.main.activity_prefrence.package_btnNext
import kotlinx.android.synthetic.main.activity_prefrence.package_btnNextt
import kotlinx.android.synthetic.main.activity_prefrence.package_ivBack
import kotlinx.android.synthetic.main.activity_prefrence.step1_arrow
import kotlinx.android.synthetic.main.activity_prefrence.step2_arrow

class MarketPlaceListActivity : AppCompatActivity() {

    var isOpen1: Boolean = false
    var isOPen2: Boolean = false
    var isOpen3: Boolean = false

    private var mDataList: ArrayList<PackageData.PackageDataList>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setContentView(R.layout.activity_market_place_list)

        package_ivBack.setOnClickListener {
            onBackPressed()
        }

//        mpList_rvPkgList.layoutManager = GridLayoutManager(this@MarketPlaceListActivity, 3)
        mpList_rvStandardList.layoutManager = GridLayoutManager(this@MarketPlaceListActivity, 3)

        package_btnNext.setOnClickListener {

            //            if (chooseCoarseAdapter != null) {
//                var subIds = chooseCoarseAdapter!!.getIds()
//
//                if (subIds != "" && subIds != null) {
//            val mIntent = Intent(this@MarketPlaceListActivity, DashboardActivity::class.java)
//            startActivity(mIntent)
//                } else {
//                    Utils.ping(this@MarketPlaceListActivity, "Please Select Subject")
//                }
//
//            } else {
//                Utils.ping(this@MarketPlaceListActivity, "Please Select Standard")
//            }
        }

        package_btnNextt.setOnClickListener {

            //            if (chooseCoarseAdapter != null) {
//                var subIds = chooseCoarseAdapter!!.getIds()
//
//                if (subIds != "" && subIds != null) {
//            val mIntent = Intent(this@MarketPlaceListActivity, DashboardActivity::class.java)
//            startActivity(mIntent)
//                } else {
//                    Utils.ping(this@MarketPlaceListActivity, "Please Select Subject")
//                }
//
//            } else {
//                Utils.ping(this@MarketPlaceListActivity, "Please Select Standard")
//            }
        }

        expand()
//        callPackageListApi()

    }

    fun expand() {

        step1_arrow.setOnClickListener {

            if (isOpen1) {
                mpList_rvPkgList.visibility = View.VISIBLE
                step1_arrow.rotation = 180f

                isOpen1 = false

            } else {
                mpList_rvPkgList.visibility = View.GONE
                step1_arrow.rotation = 0f

                isOpen1 = true

            }

        }

        step2_arrow.setOnClickListener {

            if (isOPen2) {
                mpList_rvStandardList.visibility = View.VISIBLE
                step2_arrow.rotation = 180f

                isOPen2 = false

            } else {
                mpList_rvStandardList.visibility = View.GONE
                step2_arrow.rotation = 0f

                isOPen2 = true

            }

        }

    }

//    fun callPackageListApi() {
//
//        if (!DialogUtils.isNetworkConnected(this@MarketPlaceListActivity)) {
//            Utils.ping(this@MarketPlaceListActivity, "Connetion not available")
//        }
//
//        DialogUtils.showDialog(this@MarketPlaceListActivity)
//        val apiService = WebClient.getClient().create(WebInterface::class.java)
//
//        val call = apiService.getPackage(
//            WebRequests.getPackageParams(
//                Utils.getStringValue(this@MarketPlaceListActivity, AppConstants.COURSE_TYPE_ID, "")!!,
//                Utils.getStringValue(this@MarketPlaceListActivity, AppConstants.COURSE_ID, "")!!,
//                Utils.getStringValue(this@MarketPlaceListActivity, AppConstants.STANDARD_ID, "")!!,
//                Utils.getStringValue(this@MarketPlaceListActivity, AppConstants.SUBJECT_ID, "")!!
//            )
//        )
//
////        val call = apiService.getPackage(
////            WebRequests.getPackageParams(
////                "1",
////                "1",
////                "9",
////                "1"
////            )
////        )
//
//        call.enqueue(object : Callback<PackageData> {
//            override fun onResponse(call: Call<PackageData>, response: Response<PackageData>) {
//
//                if (response.body() != null) {
//
//                    DialogUtils.dismissDialog()
//
//                    if (response.body()!!.Status == "true") {
//
//                        mDataList = response.body()!!.data
//
//                        mpList_rvPkgList.layoutManager = GridLayoutManager(this@MarketPlaceListActivity, 3)
//                        testPackagesAdapter = TestPackagesAdapter(this@MarketPlaceListActivity, mDataList!!)
//                        mpList_rvPkgList.adapter = testPackagesAdapter
//
//                    } else {
//
//                        Toast.makeText(this@MarketPlaceListActivity, response.body()!!.Msg, Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<PackageData>, t: Throwable) {
//                // Log error here since request failed
//                Log.e("", t.toString())
//                DialogUtils.dismissDialog()
//            }
//        })
//    }

}
