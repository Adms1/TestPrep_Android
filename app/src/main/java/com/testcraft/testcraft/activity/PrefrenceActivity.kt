package com.testcraft.testcraft.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.JsonObject
import com.testcraft.testcraft.Connectivity
import com.testcraft.testcraft.R
import com.testcraft.testcraft.adapter.NewSelectBoardAdapter
import com.testcraft.testcraft.adapter.NewSelectStandardAdapter
import com.testcraft.testcraft.adapter.NewSelectSubjectAdapter
import com.testcraft.testcraft.models.PackageData
import com.testcraft.testcraft.retrofit.WebClient
import com.testcraft.testcraft.utils.*
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_prefrence.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("SetTextI18n")
class PrefrenceActivity : AppCompatActivity() {

    var chooseCoarseAdapter: NewSelectSubjectAdapter? = null
    var comefrom = ""

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    var connectivity: Connectivity? = null

    override fun onStart() {
        super.onStart()

        val filter = IntentFilter()
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        registerReceiver(connectivity, filter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(connectivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        comefrom = intent.getStringExtra("comefrom")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        setContentView(R.layout.activity_prefrence)

        CommonWebCalls.callToken(
            this@PrefrenceActivity,
            "1",
            "",
            ActionIdData.C700,
            ActionIdData.T700
        )

        connectivity = Connectivity()

        AppConstants.FILTER_STANDARD_ID = "0"
        AppConstants.FILTER_SUBJECT_ID = "0"
        AppConstants.FILTER_TUTOR_ID = "0"
        AppConstants.FILTER_BOARD_ID = "0"
        AppConstants.FILTER_FROM_PRICE = "0"
        AppConstants.FILTER_TO_PRICE = "5000"

        package_ivBack.setOnClickListener {
            onBackPressed()
        }

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

        package_btnNext.setOnClickListener {

            callNextButton()
        }

        package_btnNextt.setOnClickListener {

            callNextButton()
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

            coarse_rvCoarseList.visibility = VISIBLE
            step1_arrow.rotation = 180f

        }

        rlOp2.setOnClickListener {

            step1_arrow.rotation = 0f
            step3_arrow.rotation = 0f

            coarse_rvCoarseList.visibility = GONE
            coarse_rvSubjectList.visibility = GONE

            coarse_rvStandardList.visibility = VISIBLE
            step2_arrow.rotation = 180f

        }

        rlOp3.setOnClickListener {

            step2_arrow.rotation = 0f
            step1_arrow.rotation = 0f

            coarse_rvStandardList.visibility = GONE
            coarse_rvCoarseList.visibility = GONE

            coarse_rvSubjectList.visibility = VISIBLE
            step3_arrow.rotation = 180f

        }

    }

    fun callCourseTypeList() {

//        if (!DialogUtils.isNetworkConnected(this@PrefrenceActivity)) {
////            Utils.ping(activity!!, AppConstants.NETWORK_MSG)
//            DialogUtils.NetworkDialog(this@PrefrenceActivity)
//            DialogUtils.dismissDialog()
//        }

        DialogUtils.showDialog(this@PrefrenceActivity)

        val call = WebClient.buildService().getCourseTypeList(intent.getStringExtra("examtype"))
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

//        if (!DialogUtils.isNetworkConnected(this@PrefrenceActivity)) {
////            Utils.ping(activity!!, AppConstants.NETWORK_MSG)
//            DialogUtils.NetworkDialog(this@PrefrenceActivity)
//            DialogUtils.dismissDialog()
//        }

        DialogUtils.showDialog(this@PrefrenceActivity)

        var call: Call<PackageData>? = null

        call = if (Utils.getStringValue(
                this@PrefrenceActivity,
                AppConstants.COURSE_TYPE_ID,
                ""
            ) != "1"
        ) {
            WebClient.buildService().getCourseSubjectList(stdId)

        } else {
            WebClient.buildService().getBoardStandardSubjectList(
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

                        for(i in 0 until subjectList.size){
                            subjectList[i].isSelected = true
                        }

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

//        if (!DialogUtils.isNetworkConnected(this@PrefrenceActivity)) {
////            Utils.ping(activity!!, AppConstants.NETWORK_MSG)
//            DialogUtils.NetworkDialog(this@PrefrenceActivity)
//            DialogUtils.dismissDialog()
//        }

        DialogUtils.showDialog(this@PrefrenceActivity)

        val call = WebClient.buildService().getBoardStandardList(courseId.toString())

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

    override fun onBackPressed() {
        val intent = Intent(this@PrefrenceActivity, NewActivity::class.java)
//        intent.putExtra("comeadater", "changeprefrence")
        intent.putExtra("comeadater", comefrom)
        startActivity(intent)
    }

    fun callNextButton() {

        CommonWebCalls.callToken(
            this@PrefrenceActivity,
            "1",
            "",
            ActionIdData.C704,
            ActionIdData.T704
        )

        AppConstants.isFirst = 0
        Utils.setStringValue(this@PrefrenceActivity, AppConstants.isPrefrence, "1")

        if (Utils.getStringValue(this@PrefrenceActivity, AppConstants.APP_MODE, "") == AppConstants.NORMAL_MODE ||
            Utils.getStringValue(this@PrefrenceActivity, AppConstants.APP_MODE, "") == "") {
            Utils.setStringValue(this@PrefrenceActivity, AppConstants.IS_LOGIN, "true")
        } else {
            Utils.setStringValue(this@PrefrenceActivity, AppConstants.IS_LOGIN, "false")
        }

        if (Utils.getStringValue(this@PrefrenceActivity, AppConstants.COURSE_ID, "") == "") {
            if (Utils.getStringValue(
                    this@PrefrenceActivity,
                    AppConstants.COURSE_TYPE_ID,
                    ""
                ) == "1"
            ) {

                DialogUtils.createConfirmDialog1(this@PrefrenceActivity,
                    "OK",
                    "Please Select Board",
                    DialogInterface.OnClickListener { dialog, which ->

                        dialog.dismiss()

                    }).show()

            } else {

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

                if (subIds != "") {

                    if(comefrom == "subscription"){
                        callInsertSubscriptionSubject()
                    }else {

                        val mIntent = Intent(this@PrefrenceActivity, DashboardActivity::class.java)
                        mIntent.putExtra("subject_id", subIds)
                        startActivity(mIntent)
                        finish()
                    }
                } else {

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

                    DialogUtils.createConfirmDialog1(this@PrefrenceActivity,
                        "OK",
                        "Please Select Standard",
                        DialogInterface.OnClickListener { dialog, which ->

                            dialog.dismiss()

                        }).show()

                } else {

                    if(comefrom == "subscription"){
                        callInsertSubscriptionSubject()
                    }else {

                        val mIntent = Intent(this@PrefrenceActivity, DashboardActivity::class.java)
                        mIntent.putExtra("subject_id", "")
                        startActivity(mIntent)
                        finish()
                    }
                }
            }
        }
    }

    fun callInsertSubscriptionSubject(){

        var board = ""
        var cource = ""
        var std = ""
        if(Utils.getStringValue(this@PrefrenceActivity, AppConstants.COURSE_TYPE_ID, "0")!! == "1"){
            board =  Utils.getStringValue(this@PrefrenceActivity, AppConstants.COURSE_ID, "0")!!
            cource = "0"
            std = Utils.getStringValue(this@PrefrenceActivity, AppConstants.STANDARD_ID, "0")!!
        }else{
            std = "0"
            board = "0"
            cource =  Utils.getStringValue(this@PrefrenceActivity, AppConstants.COURSE_ID, "0")!!
        }

        val call = WebClient.buildService().insertSubscriptionSubject(
            Utils.getStringValue(this@PrefrenceActivity, AppConstants.USER_ID, "0")!!,
            cource,
            board,
            std,
            Utils.getStringValue(this@PrefrenceActivity, AppConstants.COURSE_TYPE_ID, "0")!!, "0"
        )

        call.enqueue(object : Callback<JsonObject> {

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if(response.body()!!.get("Status").asString == "true") {
                    val mIntent = Intent(this@PrefrenceActivity, SubscriptionActivity::class.java)
                    startActivity(mIntent)
                    finish()
                }else{
                    DialogUtils.createConfirmDialog1(
                        this@PrefrenceActivity,
                        "OK",
                        response.body()!!.get("Msg").asString,
                        DialogInterface.OnClickListener { dialog, which ->

                            val mIntent = Intent(this@PrefrenceActivity, SubscriptionActivity::class.java)
                            startActivity(mIntent)
                            finish()

                        }).show()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // Log error here since request failed
                Log.e("", t.toString())
            }
        })
    }

}
