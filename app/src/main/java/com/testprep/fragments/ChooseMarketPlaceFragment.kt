package com.testprep.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.testprep.R
import com.testprep.adapter.MainPackageAdapter
import com.testprep.models.MyPackageModel
import com.testprep.retrofit.WebClient
import com.testprep.retrofit.WebInterface
import com.testprep.utils.AppConstants
import com.testprep.utils.DialogUtils
import com.testprep.utils.Utils
import kotlinx.android.synthetic.main.fragment_choose_market_place.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChooseMarketPlaceFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choose_market_place, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        choosemp_rvList.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
//        choosemp_rvList.adapter = MainPackageAdapter(activity!!)

        callMyPackagesApi()
    }

    fun callMyPackagesApi() {

        if (!DialogUtils.isNetworkConnected(activity!!)) {
            Utils.ping(activity!!, "Connetion not available")
        }

        DialogUtils.showDialog(activity!!)
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getMyPackages(
            Utils.getStringValue(activity!!, AppConstants.USER_ID, "0")!!,
            "",
            "",
            ""
        )

        call.enqueue(object : Callback<MyPackageModel> {
            override fun onResponse(call: Call<MyPackageModel>, response: Response<MyPackageModel>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.Status == "true") {

                        val pkgArr = response.body()!!.data
                        choosemp_rvList.adapter = MainPackageAdapter(activity!!, pkgArr)
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
