package com.testcraft.testcraft.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
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

class ExploreFragment : Fragment() {

    private var call1: Call<PackageData>? = null
    private var call2: Call<JsonObject>? = null

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

        if (getView() != null) {
            callSubjectListApi()

            callGetHistoryApi()
        }

    }

    fun callSubjectListApi(): ArrayList<PackageData.PackageDataList> {

        var filterArray: ArrayList<PackageData.PackageDataList> = ArrayList()

//        if (!DialogUtils.isNetworkConnected(activity!!)) {
////            Utils.ping(activity!!, AppConstants.NETWORK_MSG)
//            DialogUtils.NetworkDialog(activity!!)
//            DialogUtils.dismissDialog()
//        }

        DialogUtils.showDialog(activity!!)
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        call1 = apiService.getExplore()
        call1!!.enqueue(object : Callback<PackageData> {
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
//            Utils.ping(activity!!, AppConstants.NETWORK_MSG)
            DialogUtils.NetworkDialog(activity!!)
            DialogUtils.dismissDialog()
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
//            Utils.ping(activity!!, AppConstants.NETWORK_MSG)
            val netdialog = Dialog(activity!!)
            netdialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            netdialog.setContentView(R.layout.dialog_network)
            netdialog.setCanceledOnTouchOutside(false)

            val btnRetry: TextView = netdialog.findViewById(R.id.network_btnRetry)

            btnRetry.setOnClickListener {
                if (DialogUtils.isNetworkConnected(activity!!)) {
                    netdialog.dismiss()
                    callGetHistoryApi()
                }
            }

            netdialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            netdialog.setCanceledOnTouchOutside(false)
            netdialog.setCancelable(false)
            netdialog.show()
            DialogUtils.dismissDialog()
        }

        DialogUtils.showDialog(activity!!)
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        call2 = apiService.getSearchHistory(
            Utils.getStringValue(
                activity!!,
                AppConstants.USER_ID,
                "0"
            )!!
        )

        call2!!.enqueue(object : Callback<JsonObject> {
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

    override fun onDestroyView() {
        super.onDestroyView()
        call1!!.cancel()
        call2!!.cancel()
    }
}
