package com.testcraft.testcraft.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.testcraft.testcraft.R
import com.testcraft.testcraft.activity.DashboardActivity
import com.testcraft.testcraft.adapter.MainPackageAdapter
import com.testcraft.testcraft.models.MyPackageModel
import com.testcraft.testcraft.retrofit.WebClient
import com.testcraft.testcraft.retrofit.WebInterface
import com.testcraft.testcraft.utils.AppConstants
import com.testcraft.testcraft.utils.DialogUtils
import com.testcraft.testcraft.utils.Utils
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

        choosemp_rvList.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
//        choosemp_rvList.adapter = MainPackageAdapter(activity!!)

        choosemp_tvNoPkg.setOnClickListener {

            AppConstants.isFirst = 0
            DashboardActivity.setFragments(null)

        }

        callMyPackagesApi()
    }

    fun callMyPackagesApi() {

        if (!DialogUtils.isNetworkConnected(activity!!)) {
            Utils.ping(activity!!, AppConstants.NETWORK_MSG)
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
            override fun onResponse(
                call: Call<MyPackageModel>,
                response: Response<MyPackageModel>
            ) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.Status == "true") {

                        val pkgArr = response.body()!!.data

                        if (pkgArr.size > 0 && pkgArr != null) {

                            choosemp_tvNoPkg.visibility = View.GONE
                            choosemp_rvList.visibility = View.VISIBLE
                            choosemp_rvList.adapter = MainPackageAdapter(activity!!, pkgArr)
                        } else {

                            choosemp_tvNoPkg.visibility = View.VISIBLE
                            choosemp_rvList.visibility = View.GONE

                        }
                    } else {
                        choosemp_tvNoPkg.visibility = View.VISIBLE
                        choosemp_rvList.visibility = View.GONE
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
