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

    var minprice = ""
    var maxprice = ""

    var boardid = ""
    var stdid = ""
    var subid = ""
    var tutorid = ""
    var ptype = ""
    var pname = ""
    var course_type = ""

    var isSort = false

    var bundle: Bundle? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.activity_tutor_detail, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bundle = this.arguments
        course_type = bundle!!.getString("course_type")!!
        boardid = bundle!!.getString("boardid")!!
        stdid = bundle!!.getString("stdid")!!
        subid = bundle!!.getString("subid")!!
        tutorid = bundle!!.getString("tutorid")!!
        pname = bundle!!.getString("pname")!!
        ptype = bundle!!.getString("type")!!

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

        tutor_detail_header.text = pname

        if (ptype == "pkg") {

            tutor_detail_rlFilter.visibility = View.VISIBLE

            tutor_packages_rvPopularPkg.layoutManager = GridLayoutManager(activity!!, 2)
            callFilterListApi("", "1")

        } else if (ptype == "filter") {

            tutor_detail_rlFilter.visibility = View.VISIBLE

            minprice = bundle!!.getString("minprice")!!
            maxprice = bundle!!.getString("maxprice")!!

            Utils.setStringValue(activity!!, AppConstants.MIN_PRICE, minprice)
            Utils.setStringValue(activity!!, AppConstants.MAX_PRICE, maxprice)

            tutor_packages_rvPopularPkg.layoutManager = GridLayoutManager(activity!!, 2)
            callFilterListApi("", "-1")

        } else if (ptype == "explore") {

            tutor_detail_rlFilter.visibility = View.VISIBLE

            tutor_packages_rvPopularPkg.layoutManager = GridLayoutManager(activity!!, 2)
            callFilterListApi(bundle!!.getString("search_name")!!, "-1")

        } else if (ptype == "tutor") {

            tutor_detail_rlFilter.visibility = View.GONE

            tutor_packages_rvPopularPkg.layoutManager =
                LinearLayoutManager(activity!!, LinearLayoutManager.VERTICAL, false)

            tutorAdapter = TutorsAdapter(activity!!, data)

            tutor_packages_rvPopularPkg.adapter = tutorAdapter

        } else if (ptype == "single") {
            tutor_detail_rlFilter.visibility = View.VISIBLE

            tutor_packages_rvPopularPkg.layoutManager = GridLayoutManager(activity!!, 2)
            callFilterListApi("", "3")
        }

        tutor_detail_ivSort.setOnClickListener {

            isSort = !isSort

            if (ptype == "single" || ptype == "pkg" || ptype == "explore" || ptype == "filter") {

                sorting("pkg", data)

            } else if (ptype == "tutor") {

                sorting("tutor", data)

            }


        }

        tutor_detail_rlFilter.setOnClickListener {
            val intent = Intent(activity!!, FilterActivity::class.java)
            startActivityForResult(intent, 101)
        }

        setFilterCount()

    }

    fun onClick(v: View) {
        when (v) {

            tutor_detail_rlFilter -> {

                val intent = Intent(activity!!, FilterActivity::class.java)
                startActivityForResult(intent, 101)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 101) {
            if (data != null) {
                ptype = data.getStringExtra("type")
                pname = data.getStringExtra("pname")
                boardid = data.getStringExtra("boardid")
                stdid = data.getStringExtra("stdid")
                subid = data.getStringExtra("subid")
                tutorid = data.getStringExtra("tutorid")
                minprice = data.getStringExtra("minprice")
                maxprice = data.getStringExtra("maxprice")

                callFilterListApi("", "-1")

            }
        }

    }

    override fun onResume() {
        super.onResume()

        setFilterCount()

//        if (intent.getStringExtra("type") == "pkg") {
//
//            tutor_detail_rlFilter.visibility = View.VISIBLE
//
//            tutor_packages_rvPopularPkg.layoutManager = GridLayoutManager(activity!!, 2)
//            callFilterListApi("", "1")
//
//        } else if (intent.getStringExtra("type") == "filter") {
//
//            tutor_detail_rlFilter.visibility = View.VISIBLE
//
//            minprice = intent.getStringExtra("minprice")
//            maxprice = intent.getStringExtra("maxprice")
//
////            Utils.setStringValue(activity!!, AppConstants.MIN_PRICE, minprice)
////            Utils.setStringValue(activity!!, AppConstants.MAX_PRICE, maxprice)
//
//            tutor_packages_rvPopularPkg.layoutManager = GridLayoutManager(activity!!, 2)
//            callFilterListApi("", "-1")
//
//        } else if (intent.getStringExtra("type") == "explore") {
//
//            tutor_detail_rlFilter.visibility = View.VISIBLE
//
//            tutor_packages_rvPopularPkg.layoutManager = GridLayoutManager(activity!!, 2)
//            callFilterListApi(intent.getStringExtra("search_name"), "-1")
//
//        } else if (intent.getStringExtra("type") == "tutor") {
//
//            tutor_detail_rlFilter.visibility = View.GONE
//
//            tutor_packages_rvPopularPkg.layoutManager =
//                LinearLayoutManager(activity!!, LinearLayoutManager.VERTICAL, false)
//
//            tutorAdapter = TutorsAdapter(activity!!, data)
//
//            tutor_packages_rvPopularPkg.adapter = tutorAdapter
//
//        } else if (intent.getStringExtra("type") == "single") {
//            tutor_detail_rlFilter.visibility = View.VISIBLE
//
//            tutor_packages_rvPopularPkg.layoutManager = GridLayoutManager(activity!!, 2)
//            callFilterListApi("", "3")
//        }
    }

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

                        tutor_packages_rvPopularPkg.visibility = View.VISIBLE
                        tutor_packages_tvdatanotfound.visibility = View.GONE

//                        if (type == "pkg") {
//                            data = response.body()!!.data[0].TestPackage

//                        tutor_detail_header.text = response.body()!!.data[0].Name

//                        } else {
                            data = response.body()!!.data[0].TestPackage

//                        tutor_detail_header.text = response.body()!!.data[0].Name
//                        }

                        pkgAdapter = TestPackagesAdapter(activity!!, data)
                        tutor_packages_rvPopularPkg.adapter = pkgAdapter


                    } else {

                        tutor_packages_tvdatanotfound.visibility = View.VISIBLE
                        tutor_packages_rvPopularPkg.visibility = View.GONE
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

    fun sorting(type: String, modelList: ArrayList<PackageData.PackageDataList>) {

        if (isSort) {
            modelList.sortWith(Comparator { lhs, rhs ->
                if (type != "tutor") {
                    lhs.TestPackageName.toLowerCase().compareTo(rhs.TestPackageName.toLowerCase())
                } else {
                    lhs.TutorName.toLowerCase().compareTo(rhs.TutorName.toLowerCase())
                }
            })

            if (type == "tutor") {

                tutorAdapter = TutorsAdapter(activity!!, modelList)
                tutor_packages_rvPopularPkg.adapter = tutorAdapter
                tutorAdapter!!.notifyDataSetChanged()


            } else {

                pkgAdapter = TestPackagesAdapter(activity!!, modelList)
                tutor_packages_rvPopularPkg.adapter = pkgAdapter
                pkgAdapter!!.notifyDataSetChanged()


            }
        } else {
            modelList.sortWith(Comparator { lhs, rhs ->
                if (type != "tutor") {
                    rhs.TestPackageName.toLowerCase().compareTo(lhs.TestPackageName.toLowerCase())
                } else {
                    rhs.TutorName.toLowerCase().compareTo(lhs.TutorName.toLowerCase())
                }
            })

            if (type == "tutor") {

                tutorAdapter = TutorsAdapter(activity!!, modelList)
                tutor_packages_rvPopularPkg.adapter = tutorAdapter
                tutorAdapter!!.notifyDataSetChanged()


            } else {

                pkgAdapter = TestPackagesAdapter(activity!!, modelList)
                tutor_packages_rvPopularPkg.adapter = pkgAdapter
                pkgAdapter!!.notifyDataSetChanged()


            }
        }
    }

}
