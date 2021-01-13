package com.testcraft.testcraft.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.testcraft.testcraft.R
import com.testcraft.testcraft.activity.CreateTestActivity
import com.testcraft.testcraft.activity.DashboardActivity
import com.testcraft.testcraft.activity.ViewInvoiceSecondActivity
import com.testcraft.testcraft.adapter.MyPackageAdapter
import com.testcraft.testcraft.adapter.SelfTestAdapter
import com.testcraft.testcraft.models.GetSelfTest
import com.testcraft.testcraft.models.MyPackageModel
import com.testcraft.testcraft.retrofit.WebClient
import com.testcraft.testcraft.retrofit.WebInterface
import com.testcraft.testcraft.utils.*
import kotlinx.android.synthetic.main.fragment_my_packages.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat

class MyPackagesFragment : Fragment() {

    private var subid = 0
    private var stdid = ""
    private var iscompetitive = ""
    private var boardid = ""
    private var subname = ""

    var bundle: Bundle? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_packages, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        CommonWebCalls.callToken(activity!!, "1", "", ActionIdData.C1700, ActionIdData.T1700)

        bundle = this.arguments
        subid = bundle!!.getInt("sub_id", 0)
        stdid = bundle!!.getString("std_id", "")
        subname = bundle!!.getString("sub_name", "")
        boardid = bundle!!.getString("board_id", "")

        if (bundle!!.containsKey("isCompetitive")) {
            iscompetitive = if (bundle!!.getBoolean("isCompetitive", false)) {
                "1"
            } else {
                "0"
            }
        }

//        my_packages_ivReport.setOnClickListener {

        //            val intent = Intent(context, ViewInvoiceActivity::class.java)
//            intent.putExtra("header", "Knowledge Gap")
//            intent.putExtra(
//                "url",
//                "http://webservice.testcraft.in/TestPackageSummaryReport.aspx?STPID=" + AppConstants.PKG_ID
//            )
//            startActivity(intent)
//        }

        my_packages_ivCreateTest.setOnClickListener {

            val intent = Intent(context, CreateTestActivity::class.java)

            if(iscompetitive == "1"){
                intent.putExtra("coursetypeid", "2")

                intent.putExtra("board_id", "")
                intent.putExtra("sub_id", subid.toString())
                intent.putExtra("std_id", "")
                intent.putExtra("sub_name", subname)

            } else {
                intent.putExtra("coursetypeid", "1")

                intent.putExtra("board_id", boardid)
                intent.putExtra("sub_id", subid.toString())
                intent.putExtra("std_id", stdid)
                intent.putExtra("sub_name", subname)
            }

            startActivity(intent)
            (context as DashboardActivity).finish()
        }

        my_create_pkgs.setOnClickListener {

            val intent = Intent(context, CreateTestActivity::class.java)

            if (iscompetitive == "1") {
                intent.putExtra("coursetypeid", "2")

                intent.putExtra("board_id", "")
                intent.putExtra("sub_id", subid.toString())
                intent.putExtra("std_id", "")
                intent.putExtra("sub_name", subname)

            } else {
                intent.putExtra("coursetypeid", "1")

                intent.putExtra("board_id", boardid)
                intent.putExtra("sub_id", subid.toString())
                intent.putExtra("std_id", stdid)
                intent.putExtra("sub_name", subname)
            }

            startActivity(intent)
            (context as DashboardActivity).finish()
        }

//        my_packages_header.text = bundle!!.getString("sub_name")

        my_packages_rvList.layoutManager =
            LinearLayoutManager(activity!!, LinearLayoutManager.VERTICAL, false)
        my_create_rvList.layoutManager =
            LinearLayoutManager(activity!!, LinearLayoutManager.VERTICAL, false)

        my_packages_rvList.isNestedScrollingEnabled = false

        DialogUtils.showDialog(activity!!)

//        my_packages_ivReport.setOnClickListener { onKnowledgegapClick() }
        my_packages_tvReport.setOnClickListener { onKnowledgegapClick() }
//        my_packages_ivPendingTest.setOnClickListener { onsummaryreportClick() }
        my_packages_tvSummartreport.setOnClickListener { onsummaryreportClick() }

//        my_packages_ivTotalTest.setOnClickListener { onKnowledgegapClick() }
//        my_packages_ivTotalTest.setOnClickListener { onKnowledgegapClick() }

//        my_packages_ivPendingTest.setOnClickListener { onsummaryreportClick() }
//        my_packages_tvPendingCount.setOnClickListener { onsummaryreportClick() }

        callMyPackagesApi()
        callgetMyTest()
    }

    fun onKnowledgegapClick() {

        CommonWebCalls.callToken(activity!!, "1", "", ActionIdData.C1701, ActionIdData.T1701)

        if (iscompetitive == "1") {
            val intent = Intent(context, ViewInvoiceSecondActivity::class.java)
            intent.putExtra("header", "Knowledge Gap")
            intent.putExtra(
                "url",
                AppConstants.SUBJECT_SUMMARY_REPORT_URL + "IsCompetitive=1&CourseID=" + subid.toString() + "&StudentID=" + Utils.getStringValue(
                    activity!!,
                    AppConstants.USER_ID,
                    "0"
                )!!
            )

            startActivity(intent)

        } else {
            val intent = Intent(context, ViewInvoiceSecondActivity::class.java)
            intent.putExtra("header", "Knowledge Gap")
            intent.putExtra(
                "url",
                AppConstants.SUBJECT_SUMMARY_REPORT_URL + "IsCompetitive=0&StandardID=" + stdid + "&SubjectID=" + subid.toString() + "&StudentID=" + Utils.getStringValue(
                    activity!!,
                    AppConstants.USER_ID,
                    "0"
                )!!
            )
            startActivity(intent)
        }
    }

    fun onsummaryreportClick() {

        CommonWebCalls.callToken(activity!!, "1", "", ActionIdData.C1701, ActionIdData.T1701)

        if (iscompetitive == "1") {
            val intent = Intent(context, ViewInvoiceSecondActivity::class.java)
            intent.putExtra("header", "Summary Report")
            intent.putExtra(
                "url",
                AppConstants.ALL_SUMMARY_REPORT_URL + "IsCompetitive=1&CourseID=" + subid.toString() + "&StudentID=" + Utils.getStringValue(
                    activity!!,
                    AppConstants.USER_ID,
                    "0"
                )!! + "&StandardID=0&SubjectID=0"
            )

            startActivity(intent)

        } else {
            val intent = Intent(context, ViewInvoiceSecondActivity::class.java)
            intent.putExtra("header", "Summary Report")
            intent.putExtra(
                "url",
                AppConstants.ALL_SUMMARY_REPORT_URL + "IsCompetitive=0&StandardID=" + stdid + "&SubjectID=" + subid.toString() + "&StudentID=" + Utils.getStringValue(
                    activity!!,
                    AppConstants.USER_ID,
                    "0"
                )!! + "&CourseID=0"
            )
            startActivity(intent)
        }

    }

    fun callMyPackagesApi() {

        if (!DialogUtils.isNetworkConnected(activity!!)) {
            Utils.ping(activity!!, AppConstants.NETWORK_MSG)
        }

        DialogUtils.showDialog(activity!!)

        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call: Call<MyPackageModel>?
        if (bundle!!.getBoolean("isCompetitive", false)) {
            call = apiService.getMyPackages2(
                Utils.getStringValue(activity!!, AppConstants.USER_ID, "0")!!,
                subid.toString(), stdid, iscompetitive, "0", subid.toString()
            )
        } else {
            call = apiService.getMyPackages2(
                Utils.getStringValue(activity!!, AppConstants.USER_ID, "0")!!,
                subid.toString(), stdid, iscompetitive, boardid, "0"
            )
        }

        call.enqueue(object : Callback<MyPackageModel> {

            override fun onResponse(
                call: Call<MyPackageModel>,
                response: Response<MyPackageModel>
            ) {

                if (response.body() != null) {

                    if (response.body()!!.Status == "true") {

                        val summaryArr = response.body()!!.data[0].TestSummary

                        var totalcount = 0
                        var pendingcount = 0
                        var completecount = 0
                        var startCount = 0

//                        boardid = response.body()!!.data[0].BoardID

                        if (response.body()!!.data[0].isSubscription == "0") {
                            card_view2.visibility = View.GONE
                        } else {
                            card_view2.visibility = View.VISIBLE
                        }

                        for (i in 0 until summaryArr.size) {
                            totalcount += summaryArr[i].count

                            if (summaryArr[i].status == "Analyse") {
                                completecount = summaryArr[i].count
                            } else if (summaryArr[i].status == "Start") {
                                startCount = summaryArr[i].count
                            }
                        }

                        pendingcount = totalcount - completecount

                        var final = 0F

                        val per: Float =
                            DecimalFormat("##.##").format((((pendingcount.toFloat()) / totalcount.toFloat()) * 100))
                                .toFloat()

//                        per = DecimalFormat("##.##").format(per).toFloat()

                        final = when {
                            per > 0   -> {
                                100 - per
                            }
                            per == 0F -> {
                                100.0F
                            }
                            else      -> {
                                0F
                            }
                        }

//                        var per = (pendingcount.toFloat()/totalcount.toFloat())*100
                        Log.d("percentage", "" + final)

                        when {
                            final <= 40         -> {
                                my_packages_ivProgress.progressColor =
                                    resources.getColor(R.color.red)
                            }
                            final in 41.0..60.0 -> {
                                my_packages_ivProgress.progressColor =
                                    resources.getColor(R.color.yellow)
                            }
                            else                -> {
                                my_packages_ivProgress.progressColor =
                                    resources.getColor(R.color.green)
                            }
                        }

//                        my_packages_tvProgress!!.text = "$completecount/$totalcount"

                        if (response.body()!!.data[0].Performance != "") {
                            my_packages_ivProgress.setProgress(response.body()!!.data[0].Performance.toFloat(), true)


                            my_packages_tvProgress!!.text =
                                response.body()!!.data[0].Performance + "%"
                        } else {
                            my_packages_ivProgress.setProgress(0f, true)

                            my_packages_tvProgress!!.text = "0" + "%"
                        }

//                        my_packages_tvPendingCount.text = pendingcount.toString()
//                        my_packages_tvTotalCount.text = totalcount.toString()

                        val pkgArr = response.body()!!.data[0].PackageList
                        my_packages_rvList.adapter = MyPackageAdapter(activity!!, pkgArr, "my_pkgs")
//                        my_packages_rvList.adapter = TestPackagesAdapter(activity!!, pkgArr)

                        DialogUtils.dismissDialog()
                    }
                }
            }

            override fun onFailure(call: Call<MyPackageModel>, t: Throwable) {
                // Log error here since request failed
                Log.e("", t.toString())
                DialogUtils.dismissDialog()
            }
        })
    }

    fun callgetMyTest() {

        if (!DialogUtils.isNetworkConnected(activity!!)) {
            Utils.ping(activity!!, AppConstants.NETWORK_MSG)
        }

        DialogUtils.showDialog(activity!!)

        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val hashmap = HashMap<String, String>()
        hashmap["StudentID"] = Utils.getStringValue(activity!!, AppConstants.USER_ID, "0")!!

        if(iscompetitive == "1"){
            hashmap["TypeID"] = "2"
            hashmap["CourseID"] = subid.toString()
            hashmap["BoardID"] = "0"
            hashmap["StandardID"] = "0"
            hashmap["SubjectID"] = "0"
        }else{
            hashmap["TypeID"] = "1"
            hashmap["CourseID"] = "0"
            hashmap["BoardID"] = boardid
            hashmap["StandardID"] = stdid
            hashmap["SubjectID"] = subid.toString()
        }

        val call = apiService.callGetSelfTest(hashmap)

        call.enqueue(object : Callback<GetSelfTest> {

            override fun onResponse(
                call: Call<GetSelfTest>,
                response: Response<GetSelfTest>
            ) {

                if (response.body() != null) {

                    if (response.body()!!.Status == "true") {

                        DialogUtils.dismissDialog()

                        my_create_rvList.adapter = SelfTestAdapter(activity!!, response.body()!!.data)

                    }
                }
            }

            override fun onFailure(call: Call<GetSelfTest>, t: Throwable) {
                // Log error here since request failed
                Log.e("", t.toString())
                DialogUtils.dismissDialog()
            }
        })
    }

}
