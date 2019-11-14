package com.testprep.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import com.google.gson.JsonObject
import com.testprep.R
import com.testprep.activity.DashboardActivity.Companion.setFragments
import com.testprep.adapter.RecentSearchAdapter
import com.testprep.models.PackageData
import com.testprep.retrofit.WebClient
import com.testprep.retrofit.WebInterface
import com.testprep.utils.AppConstants
import com.testprep.utils.DialogUtils
import com.testprep.utils.Utils
import kotlinx.android.synthetic.main.fragment_explore.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 *
 */

class ExploreFragment : Fragment() {

    private var mDataList: ArrayList<PackageData.PackageDataList>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explore, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        explore_etSearch.setOnEditorActionListener { v, actionId, event ->

            if (event != null && event.keyCode == KeyEvent.KEYCODE_SEARCH || actionId == EditorInfo.IME_ACTION_SEARCH) {

                callAddHitoryApi()

//                for (i in 0..AppConstants.recentSearchList.size) {
//                    if (!AppConstants.recentSearchList.contains(explore_etSearch.text.toString())) {
//                        if (explore_etSearch.text.toString() != "") {
//                            AppConstants.recentSearchList.add(explore_etSearch.text.toString())
//                        }
//                    }
//                }
            }

            false
        }

        explore_ivSearch.setOnClickListener {

            callAddHitoryApi()

//            for (i in 0..AppConstants.recentSearchList.size) {
//                if (!AppConstants.recentSearchList.contains(explore_etSearch.text.toString())) {
//                    if (explore_etSearch.text.toString() != "") {
//                        AppConstants.recentSearchList.add(explore_etSearch.text.toString())
//                    }
//                }
//            }

//            explore_etSearch.setText("")

//            if (explore_etSearch.text.toString() != "") {
//
//                AppConstants.isFirst = 13
//
//                val bundle = Bundle()
//                bundle.putString("type", "explore")
//                bundle.putString("pname1", "Packages")
//                bundle.putString("course_type", "")
//                bundle.putString("boardid", "")
//                bundle.putString("stdid", "")
//                bundle.putString("subid", "")
//                bundle.putString("tutorid", "")
//                bundle.putString("search_name", explore_etSearch.text.toString())
//                bundle.putString("maxprice", "")
//                bundle.putString("minprice", "")
//                setFragments(bundle)
//
//            } else {
//
//            }

//            callFilterListApi()
        }

        callSubjectListApi()

        callGetHistoryApi()

//        explore_etSearch.addTextChangedListener(object : TextWatcher {
//
//            override fun afterTextChanged(s: Editable) {
//                explore_rvList.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
//
//                explore_tvRecentsearch.visibility = View.VISIBLE
//                explore_ivSeeall.visibility = View.VISIBLE
//
//                if (AppConstants.recentSearchList.size > 0) {
//                    explore_rvList.adapter = RecentSearchAdapter(activity!!, AppConstants.recentSearchList)
//                }
//            }
//
//            override fun beforeTextChanged(
//                s: CharSequence, start: Int,
//                count: Int, after: Int
//            ) {
//            }
//
//            override fun onTextChanged(
//                s: CharSequence, start: Int,
//                before: Int, count: Int
//            ) {
//
//            }
//        })

    }

    fun callSubjectListApi(): ArrayList<PackageData.PackageDataList> {

        var filterArray: ArrayList<PackageData.PackageDataList> = ArrayList()

        if (!DialogUtils.isNetworkConnected(activity!!)) {
            Utils.ping(activity!!, "Connetion not available")
        }

        DialogUtils.showDialog(activity!!)
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getExplore()
        call.enqueue(object : Callback<PackageData> {
            override fun onResponse(call: Call<PackageData>, response: Response<PackageData>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.Status == "true") {

                        filterArray = response.body()!!.data

                        var subArr: ArrayList<String> = ArrayList()
                        for (i in 0 until filterArray.size) {
                            subArr.add(filterArray[i].Name)
                        }

                        val adapter = ArrayAdapter(
                            activity!!,
                            android.R.layout.simple_list_item_1,
                            subArr
                        )
                        explore_etSearch.setAdapter(adapter)

//                        recyclerviewAdapter =
//                            FilterAdapter(activity!!, filterArray, "multiple", "subject", filterInterface!!)
//                        filterData_rvList.adapter = recyclerviewAdapter
//                        choosemp_filterSubject.text = subjectArr[0].SubjectName
//                        subids = subjectArr[0].SubjectID

                    } else {

//                        Toast.makeText(activity, response.body()!!.Msg, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<PackageData>, t: Throwable) {
                // Log error here since request failed
                Log.e("", t.toString())
                DialogUtils.dismissDialog()
            }
        })

        return filterArray
    }

    fun callAddHitoryApi() {

        if (!DialogUtils.isNetworkConnected(activity!!)) {
            Utils.ping(activity!!, "Connetion not available")
        }

        DialogUtils.showDialog(activity!!)
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.addSearchHistory(
            Utils.getStringValue(activity!!, AppConstants.USER_ID, "0")!!,
            explore_etSearch.text.toString()
        )

        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(
                call: Call<JsonObject>,
                response: Response<JsonObject>
            ) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!["Status"].asString == "true") {

                        if (explore_etSearch.text.toString() != "") {

                            AppConstants.isFirst = 13

                            val bundle = Bundle()
                            bundle.putString("type", "explore")
                            bundle.putString("pname1", "Packages")
                            bundle.putString("course_type", "")
                            bundle.putString("boardid", "")
                            bundle.putString("stdid", "")
                            bundle.putString("subid", "")
                            bundle.putString("tutorid", "")
                            bundle.putString("search_name", explore_etSearch.text.toString())
                            bundle.putString("maxprice", "")
                            bundle.putString("minprice", "")
                            setFragments(bundle)

                        }
                    }

                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // Log error here since request failed
                Log.e("", t.toString())
                DialogUtils.dismissDialog()
            }
        })
    }

    fun callGetHistoryApi() {

        if (!DialogUtils.isNetworkConnected(activity!!)) {
            Utils.ping(activity!!, "Connetion not available")
        }

        DialogUtils.showDialog(activity!!)
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getSearchHistory(
            Utils.getStringValue(
                activity!!,
                AppConstants.USER_ID,
                "0"
            )!!
        )

        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(
                call: Call<JsonObject>,
                response: Response<JsonObject>
            ) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!["Status"].asString == "true") {

                        var recentSearchList: ArrayList<String> = ArrayList()

                        if (response.body()!!["data"].asJsonArray.size() > 0) {

                            for (i in 0 until response.body()!!["data"].asJsonArray.size()) {
                                recentSearchList.add(response.body()!!["data"].asJsonArray[i].asJsonObject["SearchText"].asString)
                            }

                            explore_rvList.layoutManager =
                                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

//                            var arr: List<String> = recentSearchList
                            explore_rvList.adapter =
                                RecentSearchAdapter(activity!!, recentSearchList)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // Log error here since request failed
                Log.e("", t.toString())
                DialogUtils.dismissDialog()
            }
        })
    }

}
