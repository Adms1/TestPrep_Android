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
import com.testcraft.testcraft.activity.ViewInvoiceActivity
import com.testcraft.testcraft.adapter.MyPackageAdapter
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

//        my_packages_ivBack.setOnClickListener { onBackPressed() }

//        my_packages_header.text = bundle!!.getString("sub_name")

        my_packages_rvList.layoutManager =
            LinearLayoutManager(activity!!, LinearLayoutManager.VERTICAL, false)

        my_packages_rvList.isNestedScrollingEnabled = false

        DialogUtils.showDialog(activity!!)

        my_packages_tvTotalCount.setOnClickListener { onKnowledgegapClick() }
        my_packages_ivTotalTest.setOnClickListener { onKnowledgegapClick() }

        my_packages_ivPendingTest.setOnClickListener { onsummaryreportClick() }
        my_packages_tvPendingCount.setOnClickListener { onsummaryreportClick() }

        callMyPackagesApi()
    }

    fun onKnowledgegapClick() {

        CommonWebCalls.callToken(activity!!, "1", "", ActionIdData.C1701, ActionIdData.T1701)

        if (iscompetitive == "1") {
            val intent = Intent(context, ViewInvoiceActivity::class.java)
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
            val intent = Intent(context, ViewInvoiceActivity::class.java)
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
            val intent = Intent(context, ViewInvoiceActivity::class.java)
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
            val intent = Intent(context, ViewInvoiceActivity::class.java)
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

        val call = apiService.getMyPackages(
            Utils.getStringValue(activity!!, AppConstants.USER_ID, "0")!!,
            subid.toString(), stdid, iscompetitive
        )

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

                        my_packages_ivProgress.setProgress(final, true)
                        my_packages_tvProgress!!.text = "$completecount/$totalcount"

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
}
