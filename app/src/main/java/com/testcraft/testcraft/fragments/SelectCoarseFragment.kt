package com.testcraft.testcraft.fragments

import adapter.ChooseCoarseAdapter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.testcraft.testcraft.R
import com.testcraft.testcraft.models.PackageData
import com.testcraft.testcraft.retrofit.WebClient
import com.testcraft.testcraft.retrofit.WebInterface
import com.testcraft.testcraft.utils.DialogUtils
import com.testcraft.testcraft.utils.Utils
import kotlinx.android.synthetic.main.fragment_choose_coarse.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 *
 */

class SelectCoarseFragment : Fragment() {

    private var chooseCoarseAdapter: ChooseCoarseAdapter? = null
    private var coarseList = arrayOf("Competitive", "Academic")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choose_coarse, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val heading = activity!!.findViewById(R.id.dashboard_tvTitle) as TextView
//        heading.text = "Home"

        coarse_rvCoarseList.layoutManager = GridLayoutManager(activity!!, 2)

        callCourseListApi()
    }

    fun callCourseListApi() {

        if (!DialogUtils.isNetworkConnected(activity!!)) {
            Utils.ping(activity!!, "Connetion not available")
        }

        DialogUtils.showDialog(activity!!)
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getCourseList()
        call.enqueue(object : Callback<PackageData> {
            override fun onResponse(call: Call<PackageData>, response: Response<PackageData>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.Status == "true") {

                        chooseCoarseAdapter =
                            ChooseCoarseAdapter(
                                activity!!,
                                "main_course",
                                response.body()!!.data,
                                "no"
                            )
                        coarse_rvCoarseList.adapter = chooseCoarseAdapter

                    } else {

                        Toast.makeText(activity, response.body()!!.Msg, Toast.LENGTH_SHORT).show()
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
}
