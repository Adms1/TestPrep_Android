package com.testprep.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.testprep.R
import com.testprep.adapter.TestPackagesAdapter
import com.testprep.models.PackageData
import com.testprep.retrofit.WebClient
import com.testprep.retrofit.WebInterface
import com.testprep.utils.AppConstants
import com.testprep.utils.DialogUtils
import com.testprep.utils.Utils
import com.testprep.utils.WebRequests
import kotlinx.android.synthetic.main.fragment_choose_market_place.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChooseMarketPlaceFragment : Fragment() {

    private var mDataList: ArrayList<PackageData.PackageDataList>? = null
    private var testPackagesAdapter: TestPackagesAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choose_market_place, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        AppConstants.ON_BACK = 1
        var fragment = MarketPlaceFragment()

//        choosemp_tvSingleTest.setOnClickListener {
//
//            val bundle = Bundle()
//            bundle.putString("tab", "Single Test")
//            fragment.arguments = bundle
//            fragmentManager!!.beginTransaction().replace(R.id.container, fragment).addToBackStack(null)
//                .commit()
//
//        }
//
//        choosemp_tvTestPackages.setOnClickListener {
//            val bundle = Bundle()
//            bundle.putString("tab", "Test Packages")
//            fragment.arguments = bundle
//            fragmentManager!!.beginTransaction().replace(R.id.container, fragment).addToBackStack(null)
//                .commit()
//
////            val intent = Intent(activity, SelectPackageActivity::class.java)
////            startActivity(intent)
//        }
//
////        choosemp_ivFilter.setOnClickListener {
////            val intent = Intent(activity, FilterActivity::class.java)
////            startActivity(intent)
////        }
//
//        choosemp_tvTutors.setOnClickListener {
//            val bundle = Bundle()
//            bundle.putString("tab", "Tutors")
//            fragment.arguments = bundle
//            fragmentManager!!.beginTransaction().replace(R.id.container, fragment).addToBackStack(null)
//                .commit()
//
//        }

        choosemp_rvList.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        callPackageListApi()

    }

    fun callPackageListApi() {

        if (!DialogUtils.isNetworkConnected(activity!!)) {
            Utils.ping(activity!!, "Connetion not available")
        }

        DialogUtils.showDialog(activity!!)
        val apiService = WebClient.getClient().create(WebInterface::class.java)

//        val call = apiService.getPackage(
//            WebRequests.getPackageParams(
//                Utils.getStringValue(activity!!, "course_type_id", "0")!!,
//                Utils.getStringValue(activity!!, "course_id", "0")!!,
//                Utils.getStringValue(activity!!, "std_id", "0")!!,
//                "9"
//            )
//        )

        val call = apiService.getPackage(
            WebRequests.getPackageParams(
//                Utils.getStringValue(activity, "course_type_id", "0")!!,
//                Utils.getStringValue(activity, "course_id", "0")!!,
//                Utils.getStringValue(activity, "std_id", "0")!!,
//                intent.getStringExtra("subject_id")

                Utils.getStringValue(activity!!, "course_type_id", "")!!,
                Utils.getStringValue(activity!!, "course_id", "")!!,
                Utils.getStringValue(activity!!, "standard_id", "")!!,
                Utils.getStringValue(activity!!, "subject_id", "")!!
            )
        )

        call.enqueue(object : Callback<PackageData> {
            override fun onResponse(call: Call<PackageData>, response: Response<PackageData>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.Status == "true") {

                        mDataList = response.body()!!.data

                        testPackagesAdapter = TestPackagesAdapter(activity!!, mDataList!!)
                        choosemp_rvList.adapter = testPackagesAdapter

                    } else {

                        Toast.makeText(activity!!, response.body()!!.Msg, Toast.LENGTH_SHORT).show()
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
