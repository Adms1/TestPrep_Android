package com.testcraft.testcraft.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.testcraft.testcraft.R
import com.testcraft.testcraft.activity.DashboardActivity
import com.testcraft.testcraft.activity.DashboardActivity.Companion.ivSort
import com.testcraft.testcraft.activity.DashboardActivity.Companion.rlFilter
import com.testcraft.testcraft.activity.DashboardActivity.Companion.tvFilter
import com.testcraft.testcraft.activity.FilterActivity
import com.testcraft.testcraft.adapter.MyPackageAdapter
import com.testcraft.testcraft.adapter.TestPackagesAdapter
import com.testcraft.testcraft.adapter.TutorsAdapter
import com.testcraft.testcraft.models.PackageData
import com.testcraft.testcraft.retrofit.WebClient
import com.testcraft.testcraft.utils.*
import kotlinx.android.synthetic.main.activity_tutor_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TutorDetailFragment : Fragment() {

    var data: ArrayList<PackageData.PackageDataList> = ArrayList()

    var tutorAdapter: TutorsAdapter? = null
    var pkgAdapter: TestPackagesAdapter? = null
    var singleAdapter: MyPackageAdapter? = null

    var minprice = ""
    var maxprice = ""

    var boardid = ""
    var stdid = ""
    var subid = ""
    var tutorid = ""
    var ptype = ""
    //    var pname = ""
    var course_type = ""
    var filtypeid = ""

//    var typeid = "4"

    var isSort = false

    var bundle: Bundle? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.activity_tutor_detail, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CommonWebCalls.callToken(activity!!, "1", "", ActionIdData.C900, ActionIdData.T900)

        DashboardActivity.main_header!!.text =
            DashboardActivity.pname
        DashboardActivity.btnBack!!.visibility = View.VISIBLE

        rlFilter!!.visibility = View.VISIBLE
        ivSort!!.visibility = View.VISIBLE

        bundle = this.arguments

        course_type = bundle!!.getString("course_type")!!
        boardid = bundle!!.getString("boardid")!!
        stdid = bundle!!.getString("stdid")!!
        subid = bundle!!.getString("subid")!!
        tutorid = bundle!!.getString("tutorid")!!
//        pname = bundle!!.getString("pname")!!
        ptype = bundle!!.getString("type")!!
        filtypeid = bundle!!.getString("filtertypeid")!!

        if (bundle!!.containsKey("parr")) {
            data = bundle!!.getSerializable("parr") as ArrayList<PackageData.PackageDataList>
        }

        when {
            !isSort -> {

                ivSort!!.rotation = 0f

            }
            else -> {

                ivSort!!.rotation = 180f

            }
        }

//        tutor_detail_ivBack.setOnClickListener {
//            AppConstants.isFirst = 0
//            onBackPressed()
//        }

//        tutor_detail_ivCart.setOnClickListener {
//            val intent = Intent(activity!!, CartActivity::class.java)
//            startActivity(intent)
//        }

        getFilterType()

        ivSort!!.setOnClickListener {

            CommonWebCalls.callToken(activity!!, "1", "", ActionIdData.C902, ActionIdData.T902)

            isSort = !isSort

            if (ptype == "free" || ptype == "pkg" || ptype == "explore" || ptype == "filter") {

                sorting("pkg", data)

            } else if (ptype == "tutor") {

                sorting("tutor", data)

            } else if (ptype == "single") {

                sorting("single", data)

            }

        }

        rlFilter!!.setOnClickListener {

            CommonWebCalls.callToken(activity!!, "1", "", ActionIdData.C901, ActionIdData.T901)

            val intent = Intent(activity!!, FilterActivity::class.java)
            intent.putExtra("filtertype", ptype)
            intent.putExtra("filtertypeid", filtypeid)
            startActivityForResult(intent, 101)
        }

        if (ptype == "free" || ptype == "pkg" || ptype == "single" || ptype == "filter") {
            setFilterCount()
        }

    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == 101) {
//
//            var filtypeid = ""
//
//            if (data != null) {
//                ptype = data.getStringExtra("type")
//                pname = data.getStringExtra("pname")
//                filtypeid = data.getStringExtra("filtertypeid")
//                boardid = data.getStringExtra("boardid")
//                stdid = data.getStringExtra("stdid")
//                subid = data.getStringExtra("subid")
//                tutorid = data.getStringExtra("tutorid")
//                minprice = data.getStringExtra("minprice")
//                maxprice = data.getStringExtra("maxprice")
//
//                callFilterListApi("", filtypeid)
//
//            }
//        }
//
//    }

    fun getFilterType() {
        when (filtypeid) {

            "0" -> {

                rlFilter!!.visibility = View.VISIBLE

                minprice = bundle!!.getString("minprice")!!
                maxprice = bundle!!.getString("maxprice")!!

                Utils.setStringValue(activity!!, AppConstants.MIN_PRICE, minprice)
                Utils.setStringValue(activity!!, AppConstants.MAX_PRICE, maxprice)

//                tutor_packages_rvPopularPkg.layoutManager = GridLayoutManager(activity!!, 2)

                tutor_packages_rvPopularPkg.layoutManager =
                    LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)


                callFilterListApi("", filtypeid)

            }

            "1"      -> {

                rlFilter!!.visibility = View.VISIBLE

                minprice = bundle!!.getString("minprice")!!
                maxprice = bundle!!.getString("maxprice")!!

                Utils.setStringValue(activity!!, AppConstants.MIN_PRICE, minprice)
                Utils.setStringValue(activity!!, AppConstants.MAX_PRICE, maxprice)

                tutor_packages_rvPopularPkg.layoutManager = GridLayoutManager(activity!!, 2)

                callFilterListApi("", filtypeid)

            }
            "filter" -> {

                rlFilter!!.visibility = View.VISIBLE

                minprice = bundle!!.getString("minprice")!!
                maxprice = bundle!!.getString("maxprice")!!

                Utils.setStringValue(activity!!, AppConstants.MIN_PRICE, minprice)
                Utils.setStringValue(activity!!, AppConstants.MAX_PRICE, maxprice)

                if (ptype == "single") {

                    tutor_packages_rvPopularPkg.layoutManager =
                        LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

                } else {
                    tutor_packages_rvPopularPkg.layoutManager = GridLayoutManager(activity!!, 2)
                }

                callFilterListApi("", filtypeid)

            }
            "-1"     -> {

                rlFilter!!.visibility = View.GONE

                minprice = bundle!!.getString("minprice")!!
                maxprice = bundle!!.getString("maxprice")!!

                Utils.setStringValue(activity!!, AppConstants.MIN_PRICE, minprice)
                Utils.setStringValue(activity!!, AppConstants.MAX_PRICE, maxprice)

                tutor_packages_rvPopularPkg.layoutManager = GridLayoutManager(activity!!, 2)

                callFilterListApi(bundle!!.getString("search_name")!!, filtypeid)

            }
            "2"      -> {

                CommonWebCalls.callToken(
                    activity!!,
                    "1",
                    "",
                    ActionIdData.C2700,
                    ActionIdData.T2700
                )

                rlFilter!!.visibility = View.GONE
                tutor_detail_ivNoPkg.visibility = View.GONE

                minprice = bundle!!.getString("minprice")!!
                maxprice = bundle!!.getString("maxprice")!!

                Utils.setStringValue(activity!!, AppConstants.MIN_PRICE, minprice)
                Utils.setStringValue(activity!!, AppConstants.MAX_PRICE, maxprice)

                tutor_packages_rvPopularPkg.layoutManager =
                    LinearLayoutManager(activity!!, LinearLayoutManager.VERTICAL, false)

                tutorAdapter = TutorsAdapter(activity!!, data)

                tutor_packages_rvPopularPkg.adapter = tutorAdapter

            }
            "3"      -> {
                rlFilter!!.visibility = View.VISIBLE

                minprice = bundle!!.getString("minprice")!!
                maxprice = bundle!!.getString("maxprice")!!

                Utils.setStringValue(activity!!, AppConstants.MIN_PRICE, minprice)
                Utils.setStringValue(activity!!, AppConstants.MAX_PRICE, maxprice)

                tutor_packages_rvPopularPkg.layoutManager =
                    LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

                callFilterListApi("", filtypeid)
            }
        }
    }

    fun callFilterListApi(name: String, type: String) {

        if (!DialogUtils.isNetworkConnected(activity!!)) {
//            Utils.ping(activity!!, AppConstants.NETWORK_MSG)
            val netdialog = Dialog(activity!!)
            netdialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            netdialog.setContentView(R.layout.dialog_network)
            netdialog.setCanceledOnTouchOutside(false)

            val btnRetry: TextView = netdialog.findViewById(R.id.network_btnRetry)

            btnRetry.setOnClickListener {
                if (DialogUtils.isNetworkConnected(activity!!)) {
                    netdialog.dismiss()
                    getFilterType()
                }
            }

            netdialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            netdialog.setCanceledOnTouchOutside(false)
            netdialog.setCancelable(false)
            netdialog.show()
            DialogUtils.dismissDialog()
        }

        DialogUtils.showDialog(activity!!)

        lateinit var call: Call<PackageData>

        if (course_type == "1") {
            call = WebClient.buildService().getFilterData(
                WebRequests.getFilterParams(
                    course_type,
                    "",
                    boardid,
                    stdid,
                    subid,
                    tutorid,
                    minprice,
                    maxprice,
                    name,
                    type
                )
            )
        } else {
            call = WebClient.buildService().getFilterData(
                WebRequests.getFilterParams(
                    course_type,
                    boardid,
                    "",
                    "",
                    "",
                    tutorid,
                    minprice,
                    maxprice,
                    name,
                    type
                )
            )
        }

        call.enqueue(object : Callback<PackageData> {
            override fun onResponse(call: Call<PackageData>, response: Response<PackageData>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.Status == "true") {

//                        if (type == "pkg") {
//                            data = response.body()!!.data[0].TestPackage

//                        tutor_detail_header.text = response.body()!!.data[0].Name

//                        } else {

                        data = response.body()!!.data[0].TestPackage

//                        tutor_detail_header.text = response.body()!!.data[0].Name
//                        }

                        if (data.size > 0) {

                            tutor_packages_rvPopularPkg.visibility = View.VISIBLE
                            tutor_detail_ivNoPkg.visibility = View.GONE

                            if (type == "1" || type == "-1") {
                                pkgAdapter = TestPackagesAdapter(activity!!, data)
                                tutor_packages_rvPopularPkg.adapter = pkgAdapter

                            } else {
                                singleAdapter =
                                    MyPackageAdapter(activity!!, data, "market_place", "1")
                                tutor_packages_rvPopularPkg.adapter = singleAdapter

                            }
                        } else {

                            tutor_packages_rvPopularPkg.visibility = View.GONE
                            tutor_detail_ivNoPkg.visibility = View.VISIBLE
                        }

                    } else {

                        tutor_packages_rvPopularPkg.visibility = View.GONE
                        tutor_detail_ivNoPkg.visibility = View.VISIBLE
//                        Toast.makeText(activity!!, response.body()!!.Msg, Toast.LENGTH_SHORT).show()
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

    companion object {

        fun setFilterCount() {

            var filterCount = 0

            if (AppConstants.FILTER_STANDARD_ID != "0" && AppConstants.FILTER_STANDARD_ID != "") {
                filterCount += 1
            }

            if (AppConstants.FILTER_BOARD_ID != "0" && AppConstants.FILTER_BOARD_ID != "") {
                filterCount += 1
            }

            if (AppConstants.FILTER_SUBJECT_ID != "0" && AppConstants.FILTER_SUBJECT_ID != "") {
                filterCount += 1
            }

            if (AppConstants.FILTER_TUTOR_ID != "0" && AppConstants.FILTER_TUTOR_ID != "") {
                filterCount += 1
            }

            if (AppConstants.FILTER_FROM_PRICE != "0" || AppConstants.FILTER_TO_PRICE != "5000") {
                filterCount += 1
            }

            if (filterCount > 0) {
                tvFilter!!.visibility = View.VISIBLE
                tvFilter!!.text = filterCount.toString()
            } else {
                tvFilter!!.visibility = View.GONE
            }
        }
    }

    @SuppressLint("DefaultLocale")
    fun sorting(type: String, modelList: ArrayList<PackageData.PackageDataList>) {

        if (isSort) {

            ivSort!!.rotation = 180F

            modelList.sortWith(Comparator { lhs, rhs ->

                if (type == "tutor") {
                    lhs.TutorName.toLowerCase().compareTo(rhs.TutorName.toLowerCase())
                } else {
                    lhs.TestPackageName.toLowerCase().compareTo(rhs.TestPackageName.toLowerCase())
                }
            })

            if (type == "tutor") {

                tutorAdapter = TutorsAdapter(activity!!, modelList)
                tutor_packages_rvPopularPkg.adapter = tutorAdapter
                tutorAdapter!!.notifyDataSetChanged()

            } else if (type == "free" || type == "pkg" || type == "explore" || type == "filter") {

                pkgAdapter = TestPackagesAdapter(activity!!, modelList)
                tutor_packages_rvPopularPkg.adapter = pkgAdapter
                pkgAdapter!!.notifyDataSetChanged()

            } else if (type == "single") {

                singleAdapter = MyPackageAdapter(activity!!, modelList, "market_place", "1")
                tutor_packages_rvPopularPkg.adapter = singleAdapter
                singleAdapter!!.notifyDataSetChanged()

            }
        } else {

            ivSort!!.rotation = 0F

            modelList.sortWith(Comparator { lhs, rhs ->

                if (type == "tutor") {
                    rhs.TutorName.toLowerCase().compareTo(lhs.TutorName.toLowerCase())
                } else {
                    rhs.TestPackageName.toLowerCase().compareTo(lhs.TestPackageName.toLowerCase())
                }

            })

            if (type == "tutor") {

                tutorAdapter = TutorsAdapter(activity!!, modelList)
                tutor_packages_rvPopularPkg.adapter = tutorAdapter
                tutorAdapter!!.notifyDataSetChanged()

            } else if (type == "free" || type == "pkg" || type == "explore" || type == "filter") {
                pkgAdapter = TestPackagesAdapter(activity!!, modelList)
                tutor_packages_rvPopularPkg.adapter = pkgAdapter
                pkgAdapter!!.notifyDataSetChanged()


            } else if (type == "single") {

                singleAdapter = MyPackageAdapter(activity!!, modelList, "market_place", "1")
                tutor_packages_rvPopularPkg.adapter = singleAdapter
                singleAdapter!!.notifyDataSetChanged()

            }
        }
    }
}
