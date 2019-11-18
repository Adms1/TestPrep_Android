package com.testprep.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.testprep.R
import com.testprep.activity.DashboardActivity.Companion.ivSort
import com.testprep.activity.DashboardActivity.Companion.rlFilter
import com.testprep.activity.DashboardActivity.Companion.tvFilter
import com.testprep.adapter.MyPackageAdapter
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

class TutorDetailActivity : Fragment() {

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

        DashboardActivity.main_header!!.text = DashboardActivity.pname
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

//        tutor_detail_ivBack.setOnClickListener {
//            AppConstants.isFirst = 0
//            onBackPressed()
//        }

//        tutor_detail_ivCart.setOnClickListener {
//            val intent = Intent(activity!!, CartActivity::class.java)
//            startActivity(intent)
//        }

        when (filtypeid) {

            "0" -> {

                rlFilter!!.visibility = View.VISIBLE

                tutor_packages_rvPopularPkg.layoutManager = GridLayoutManager(activity!!, 2)

                callFilterListApi("", filtypeid)

            }

            "1" -> {

                rlFilter!!.visibility = View.VISIBLE

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
            "-1" -> {

                rlFilter!!.visibility = View.VISIBLE

                tutor_packages_rvPopularPkg.layoutManager = GridLayoutManager(activity!!, 2)

                callFilterListApi(bundle!!.getString("search_name")!!, filtypeid)

            }
            "2" -> {

                rlFilter!!.visibility = View.GONE
                tutor_detail_ivNoPkg.visibility = View.GONE

                tutor_packages_rvPopularPkg.layoutManager =
                    LinearLayoutManager(activity!!, LinearLayoutManager.VERTICAL, false)

                tutorAdapter = TutorsAdapter(activity!!, data)

                tutor_packages_rvPopularPkg.adapter = tutorAdapter

            }
            "3" -> {
                rlFilter!!.visibility = View.VISIBLE

                tutor_packages_rvPopularPkg.layoutManager =
                    LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

                callFilterListApi("", filtypeid)
            }
        }

        ivSort!!.setOnClickListener {

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

    fun callFilterListApi(name: String, type: String) {

        if (!DialogUtils.isNetworkConnected(activity!!)) {
            Utils.ping(activity!!, "Connetion not available")
        }

        DialogUtils.showDialog(activity!!)
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        var call: Call<PackageData>? = null

        if (course_type == "1") {
            call = apiService.getFilterData(
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
            call = apiService.getFilterData(
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

                            if (type != "3") {
                                pkgAdapter = TestPackagesAdapter(activity!!, data)
                                tutor_packages_rvPopularPkg.adapter = pkgAdapter
                            } else {

                                singleAdapter = MyPackageAdapter(activity!!, data, "market_place")
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

                singleAdapter = MyPackageAdapter(activity!!, modelList, "market_place")
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

                singleAdapter = MyPackageAdapter(activity!!, modelList, "market_place")
                tutor_packages_rvPopularPkg.adapter = singleAdapter
                singleAdapter!!.notifyDataSetChanged()

            }
        }
    }
}
