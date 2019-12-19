package com.testcraft.testcraft.activity

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import com.testcraft.testcraft.R
import com.testcraft.testcraft.adapter.NewSelectBoardAdapter
import com.testcraft.testcraft.adapter.NewSelectStandardAdapter
import com.testcraft.testcraft.adapter.NewSelectSubjectAdapter
import com.testcraft.testcraft.models.PackageData
import com.testcraft.testcraft.retrofit.WebClient
import com.testcraft.testcraft.retrofit.WebInterface
import com.testcraft.testcraft.utils.*
import kotlinx.android.synthetic.main.activity_prefrence.*
import kotlinx.android.synthetic.main.fragment_choose_coarse.coarse_rvCoarseList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class PrefrenceActivity : AppCompatActivity() {

    var chooseCoarseAdapter: NewSelectSubjectAdapter? = null
    var isOpen1: Boolean = false
    var isOPen2: Boolean = false
    var isOpen3: Boolean = false

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//        )
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setContentView(R.layout.activity_prefrence)

        CommonWebCalls.callToken(
            this@PrefrenceActivity,
            "1",
            "",
            ActionIdData.C700,
            ActionIdData.T700
        )

        AppConstants.FILTER_STANDARD_ID = "0"
        AppConstants.FILTER_SUBJECT_ID = "0"
        AppConstants.FILTER_TUTOR_ID = "0"
        AppConstants.FILTER_BOARD_ID = "0"
        AppConstants.FILTER_FROM_PRICE = "0"
        AppConstants.FILTER_TO_PRICE = "5000"

        package_ivBack.setOnClickListener {
            val intent = Intent(this@PrefrenceActivity, NewActivity::class.java)
            startActivity(intent)
        }

//        coarse_rvCoarseList.layoutManager = LinearLayoutManager(this@PrefrenceActivity, LinearLayoutManager.HORIZONTAL, false)

        coarse_rvCoarseList.layoutManager = GridLayoutManager(this@PrefrenceActivity, 3)
        coarse_rvStandardList.layoutManager = GridLayoutManager(this@PrefrenceActivity, 3)
        coarse_rvSubjectList.layoutManager = GridLayoutManager(this@PrefrenceActivity, 3)

        if (Utils.getStringValue(this@PrefrenceActivity, AppConstants.COURSE_TYPE_ID, "") == "1") {

            pref_tvStep1.text = "Step - 1 Select Board"
            pref_tvStep2.text = "Step - 2 Select Standard"
            pref_tvStep3.text = "Step - 3 Select Subject"

            pref_tvStep2.visibility = VISIBLE
            step2_arrow.visibility = VISIBLE
            coarse_rvStandardList.visibility = VISIBLE

        } else {

            pref_tvStep1.text = "Step - 1 Select Course"
            pref_tvStep3.text = "Step - 2 Select Subject"

            pref_tvStep2.visibility = GONE
            step2_arrow.visibility = GONE
            coarse_rvStandardList.visibility = GONE

        }

        step1_arrow.setOnClickListener { }

        step2_arrow.setOnClickListener { }

        step3_arrow.setOnClickListener { }

        package_btnNext.setOnClickListener {

            CommonWebCalls.callToken(
                this@PrefrenceActivity,
                "1",
                "",
                ActionIdData.C704,
                ActionIdData.T704
            )

            AppConstants.isFirst = 0
            Utils.setStringValue(this@PrefrenceActivity, AppConstants.isPrefrence, "1")

            if (Utils.getStringValue(this@PrefrenceActivity, AppConstants.COURSE_ID, "") == "") {

                if (Utils.getStringValue(
                        this@PrefrenceActivity,
                        AppConstants.COURSE_TYPE_ID,
                        ""
                    ) == "1"
                ) {
//                    Utils.ping(this@PrefrenceActivity, "Please Select Board")

                    DialogUtils.createConfirmDialog1(this@PrefrenceActivity,
                        "OK",
                        "Please Select Board",
                        DialogInterface.OnClickListener { dialog, which ->

                            dialog.dismiss()

                        }).show()


                } else {
//                    Utils.ping(this@PrefrenceActivity, "Please Select Course")

                    DialogUtils.createConfirmDialog1(this@PrefrenceActivity,
                        "OK",
                        "Please Select Course",
                        DialogInterface.OnClickListener { dialog, which ->

                            dialog.dismiss()

                        }).show()

                }

            } else {

                if (chooseCoarseAdapter != null) {

                    val subIds = chooseCoarseAdapter!!.getIds()

                    Utils.setStringValue(this@PrefrenceActivity, AppConstants.SUBJECT_ID, subIds)

                    if (subIds != "" && subIds != null) {
                        val mIntent = Intent(this@PrefrenceActivity, DashboardActivity::class.java)
                        mIntent.putExtra("subject_id", subIds)
                        startActivity(mIntent)
                        finish()

                    } else {
//                        Utils.ping(this@PrefrenceActivity, "Please Select Subject")

                        DialogUtils.createConfirmDialog1(this@PrefrenceActivity,
                            "OK",
                            "Please Select Subject",
                            DialogInterface.OnClickListener { dialog, which ->

                                dialog.dismiss()

                            }).show()

                    }

                } else {
                    if (Utils.getStringValue(
                            this@PrefrenceActivity,
                            AppConstants.COURSE_TYPE_ID,
                            ""
                        ) == "1"
                    ) {
//                        Utils.ping(this@PrefrenceActivity, "Please Select Standard")

                        DialogUtils.createConfirmDialog1(this@PrefrenceActivity,
                            "OK",
                            "Please Select Stadard",
                            DialogInterface.OnClickListener { dialog, which ->

                                dialog.dismiss()

                            }).show()

                    }
//                    else {
//                        Utils.ping(this@PrefrenceActivity, "Please Select Subject")
//                    }
                }
            }
        }

        package_btnNextt.setOnClickListener {

            CommonWebCalls.callToken(
                this@PrefrenceActivity,
                "1",
                "",
                ActionIdData.C704,
                ActionIdData.T704
            )

            AppConstants.isFirst = 0
            Utils.setStringValue(this@PrefrenceActivity, AppConstants.isPrefrence, "1")

            if (Utils.getStringValue(this@PrefrenceActivity, AppConstants.COURSE_ID, "") == "") {
                if (Utils.getStringValue(
                        this@PrefrenceActivity,
                        AppConstants.COURSE_TYPE_ID,
                        ""
                    ) == "1"
                ) {
//                    Utils.ping(this@PrefrenceActivity, "Please Select Board")

                    DialogUtils.createConfirmDialog1(this@PrefrenceActivity,
                        "OK",
                        "Please Select Board",
                        DialogInterface.OnClickListener { dialog, which ->

                            dialog.dismiss()

                        }).show()

                } else {
//                    Utils.ping(this@PrefrenceActivity, "Please Select Course")

                    DialogUtils.createConfirmDialog1(this@PrefrenceActivity,
                        "OK",
                        "Please Select Course",
                        DialogInterface.OnClickListener { dialog, which ->

                            dialog.dismiss()

                        }).show()

                }

            } else {
                if (chooseCoarseAdapter != null) {
                    val subIds = chooseCoarseAdapter!!.getIds()

                    Utils.setStringValue(this@PrefrenceActivity, AppConstants.SUBJECT_ID, subIds)

                    if (subIds != "" && subIds != null) {
                        val mIntent = Intent(this@PrefrenceActivity, DashboardActivity::class.java)
                        mIntent.putExtra("subject_id", subIds)
                        startActivity(mIntent)
                        finish()
                    } else {
//                        Utils.ping(this@PrefrenceActivity, "Please Select Subject")

                        DialogUtils.createConfirmDialog1(this@PrefrenceActivity,
                            "OK",
                            "Please Select Subject",
                            DialogInterface.OnClickListener { dialog, which ->

                                dialog.dismiss()

                            }).show()
                    }

                } else {

                    if (Utils.getStringValue(
                            this@PrefrenceActivity,
                            AppConstants.COURSE_TYPE_ID,
                            ""
                        ) == "1"
                    ) {
//                        Utils.ping(this@PrefrenceActivity, "Please Select Standard")

                        DialogUtils.createConfirmDialog1(this@PrefrenceActivity,
                            "OK",
                            "Please Select Standard",
                            DialogInterface.OnClickListener { dialog, which ->

                                dialog.dismiss()

                            }).show()

                    } else {
                        val mIntent = Intent(this@PrefrenceActivity, DashboardActivity::class.java)
                        mIntent.putExtra("subject_id", "")
                        startActivity(mIntent)
                        finish()
                    }
                }
            }
        }

        expand()

        callCourseTypeList()
    }

    fun expand() {

        rlOp1.setOnClickListener {

            step2_arrow.rotation = 0f
            step3_arrow.rotation = 0f

            coarse_rvStandardList.visibility = GONE
            coarse_rvSubjectList.visibility = GONE

//            if (isOpen1) {
            coarse_rvCoarseList.visibility = VISIBLE
            step1_arrow.rotation = 180f

//                isOpen1 = false
//
//            } else {
//                coarse_rvCoarseList.visibility = GONE
//                step1_arrow.rotation = 0f
//
//                isOpen1 = true
//
//            }

        }

        rlOp2.setOnClickListener {

            step1_arrow.rotation = 0f
            step3_arrow.rotation = 0f

            coarse_rvCoarseList.visibility = GONE
            coarse_rvSubjectList.visibility = GONE

//            if (isOPen2) {
            coarse_rvStandardList.visibility = VISIBLE
            step2_arrow.rotation = 180f

//                isOPen2 = false
//
//            } else {
//                coarse_rvStandardList.visibility = GONE
//                step2_arrow.rotation = 0f
//
//                isOPen2 = true
//
//            }

        }

        rlOp3.setOnClickListener {

            step2_arrow.rotation = 0f
            step1_arrow.rotation = 0f

            coarse_rvStandardList.visibility = GONE
            coarse_rvCoarseList.visibility = GONE

//            if (isOpen3) {
            coarse_rvSubjectList.visibility = VISIBLE
            step3_arrow.rotation = 180f

//                isOpen3 = false

//            } else {
//                coarse_rvSubjectList.visibility = GONE
//                step3_arrow.rotation = 0f
//
//                isOpen3 = true
//
//            }

        }

    }

    fun callCourseTypeList() {

        if (!DialogUtils.isNetworkConnected(this@PrefrenceActivity)) {
            Utils.ping(this@PrefrenceActivity, "Connetion not available")
        }

        DialogUtils.showDialog(this@PrefrenceActivity)
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getCourseTypeList(intent.getStringExtra("examtype"))
        call.enqueue(object : Callback<PackageData> {
            override fun onResponse(call: Call<PackageData>, response: Response<PackageData>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.Status == "true") {

                        pref_tvStep2.visibility = GONE
                        coarse_rvStandardList.visibility = GONE
                        step2_arrow.visibility = GONE

                        pref_tvStep3.visibility = GONE
                        coarse_rvSubjectList.visibility = GONE
                        step3_arrow.visibility = GONE

                        Utils.setStringValue(this@PrefrenceActivity, AppConstants.COURSE_ID, "")

                        val chooseCoarseAdapter =
                            NewSelectBoardAdapter(this@PrefrenceActivity, response.body()!!.data)
                        coarse_rvCoarseList.adapter = chooseCoarseAdapter

                    } else {

                        pref_tvStep2.visibility = GONE
                        coarse_rvStandardList.visibility = GONE
                        step2_arrow.visibility = GONE
                        pref_tvStep3.visibility = GONE
                        coarse_rvSubjectList.visibility = GONE
                        step3_arrow.visibility = GONE

                        Toast.makeText(
                            this@PrefrenceActivity,
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

    fun callSubjectList(stdId: String) {

        if (!DialogUtils.isNetworkConnected(this@PrefrenceActivity)) {
            Utils.ping(this@PrefrenceActivity, "Connetion not available")
        }

        DialogUtils.showDialog(this@PrefrenceActivity)
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        var call: Call<PackageData>? = null

        call = if (Utils.getStringValue(
                this@PrefrenceActivity,
                AppConstants.COURSE_TYPE_ID,
                ""
            ) != "1"
        ) {
            apiService.getCourseSubjectList(stdId)

        } else {
            apiService.getBoardStandardSubjectList(
                Utils.getStringValue(this@PrefrenceActivity, AppConstants.COURSE_ID, "")!!,
                stdId
            )
        }

        call.enqueue(object : Callback<PackageData> {
            override fun onResponse(call: Call<PackageData>, response: Response<PackageData>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.Status == "true") {

                        Utils.setStringValue(
                            this@PrefrenceActivity,
                            AppConstants.STANDARD_ID,
                            stdId
                        )

                        pref_tvStep3.visibility = VISIBLE
                        coarse_rvSubjectList.visibility = VISIBLE
                        step3_arrow.visibility = VISIBLE

//                        pref_tvStep1.visibility = GONE
                        coarse_rvCoarseList.visibility = GONE
                        step1_arrow.rotation = 0f

                        coarse_rvStandardList.visibility = GONE
                        step2_arrow.rotation = 0f

                        val subjectList = response.body()!!.data

                        chooseCoarseAdapter =
                            NewSelectSubjectAdapter(this@PrefrenceActivity, subjectList)
                        coarse_rvSubjectList.adapter = chooseCoarseAdapter

                    } else {

                        pref_tvStep3.visibility = GONE
                        coarse_rvSubjectList.visibility = GONE
                        step3_arrow.visibility = GONE

                        Toast.makeText(
                            this@PrefrenceActivity,
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

    fun callStandardList(courseId: Int) {

        if (!DialogUtils.isNetworkConnected(this@PrefrenceActivity)) {
            Utils.ping(this@PrefrenceActivity, "Connetion not available")
        }

        DialogUtils.showDialog(this@PrefrenceActivity)
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        var call = apiService.getBoardStandardList(courseId.toString())

        call.enqueue(object : Callback<PackageData> {
            override fun onResponse(call: Call<PackageData>, response: Response<PackageData>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.Status == "true") {

                        pref_tvStep2.visibility = VISIBLE
                        coarse_rvStandardList.visibility = VISIBLE
                        step2_arrow.visibility = VISIBLE

                        coarse_rvCoarseList.visibility = GONE
                        step1_arrow.rotation = 0f

                        coarse_rvSubjectList.visibility = GONE
                        step3_arrow.rotation = 0f

                        val standardList = response.body()!!.data

                        val chooseCoarseAdapter =
                            NewSelectStandardAdapter(this@PrefrenceActivity, standardList)
                        coarse_rvStandardList.adapter = chooseCoarseAdapter

                    } else {

                        pref_tvStep3.visibility = GONE
                        coarse_rvSubjectList.visibility = GONE
                        step3_arrow.visibility = GONE

                        pref_tvStep2.visibility = GONE
                        coarse_rvStandardList.visibility = GONE
                        step2_arrow.visibility = GONE

                        Toast.makeText(
                            this@PrefrenceActivity,
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
}
