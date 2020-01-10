package com.testcraft.testcraft.fragments

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
import com.testcraft.testcraft.R
import com.testcraft.testcraft.activity.DashboardActivity.Companion.setFragments
import com.testcraft.testcraft.adapter.RecentSearchAdapter
import com.testcraft.testcraft.models.PackageData
import com.testcraft.testcraft.retrofit.WebClient
import com.testcraft.testcraft.retrofit.WebInterface
import com.testcraft.testcraft.utils.*
import kotlinx.android.synthetic.main.fragment_explore.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 *
 */

class ExploreFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explore, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CommonWebCalls.callToken(activity!!, "1", "", ActionIdData.C3100, ActionIdData.T3100)

        explore_etSearch.setOnEditorActionListener { v, actionId, event ->

            if (event != null && event.keyCode == KeyEvent.KEYCODE_SEARCH || actionId == EditorInfo.IME_ACTION_SEARCH) {

                CommonWebCalls.callToken(
                    activity!!,
                    "1",
                    "",
                    ActionIdData.C3101,
                    ActionIdData.T3101
                )

                if (explore_etSearch.text.toString().trim().isNotEmpty()) {
                    callAddHitoryApi()
                }
            }

            false
        }


        explore_ivSearch.setOnClickListener {

            CommonWebCalls.callToken(activity!!, "1", "", ActionIdData.C3101, ActionIdData.T3101)

            if (explore_etSearch.text.toString().trim().isNotEmpty()) {
                callAddHitoryApi()
            }
        }

        callSubjectListApi()

        callGetHistoryApi()

    }

    fun callSubjectListApi(): ArrayList<PackageData.PackageDataList> {

        var filterArray: ArrayList<PackageData.PackageDataList> = ArrayList()

        if (!DialogUtils.isNetworkConnected(activity!!)) {
            Utils.ping(activity!!, AppConstants.NETWORK_MSG)
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

                        val subArr: ArrayList<String> = ArrayList()
                        for (i in 0 until filterArray.size) {
                            subArr.add(filterArray[i].Name)
                        }

                        val adapter = ArrayAdapter(
                            activity!!,
                            android.R.layout.simple_list_item_1,
                            subArr
                        )
                        explore_etSearch.setAdapter(adapter)

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
            Utils.ping(activity!!, AppConstants.NETWORK_MSG)
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
                            bundle.putString("pname1", explore_etSearch.text.toString())
                            bundle.putString("filtertypeid", "-1")
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
            Utils.ping(activity!!, AppConstants.NETWORK_MSG)
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

                        val recentSearchList: ArrayList<String> = ArrayList()

                        if (response.body()!!["data"].asJsonArray.size() > 0) {

                            for (i in 0 until response.body()!!["data"].asJsonArray.size()) {
                                recentSearchList.add(response.body()!!["data"].asJsonArray[i].asJsonObject["SearchText"].asString)
                            }

                            explore_rvList.layoutManager =
                                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

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
