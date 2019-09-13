//package com.testprep.fragments
//
//
//import android.os.Bundle
//import android.support.v4.app.Fragment
//import android.support.v7.widget.GridLayoutManager
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Toast
//import com.testprep.R
//import com.testprep.adapter.NewChooseCoarseAdapter
//import com.testprep.models.PackageData
//import com.testprep.retrofit.WebClient
//import com.testprep.retrofit.WebInterface
//import com.testprep.utils.DialogUtils
//import com.testprep.utils.Utils
//import kotlinx.android.synthetic.main.activity_new.*
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//
//// TODO: Rename parameter arguments, choose names that match
//// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"
//
///**
// * A simple [Fragment] subclass.
// *
// */
//class NewActivity : Fragment() {
//
//    private var chooseCoarseAdapter: NewChooseCoarseAdapter? = null
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.activity_new, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
////        val heading = activity!!.findViewById(R.id.dashboard_tvTitle) as TextView
////        heading.text = "Home"
//
//        new_coarse_rvCoarseList.layoutManager = GridLayoutManager(activity!!, 2)
//
//        callCourseListApi()
//
//    }
//
//    fun callCourseListApi() {
//
//        if (!DialogUtils.isNetworkConnected(activity!!)) {
//            Utils.ping(activity!!, "Connetion not available")
//        }
//
//        DialogUtils.showDialog(activity!!)
//        val apiService = WebClient.getClient().create(WebInterface::class.java)
//
//        val call = apiService.getCourseList()
//        call.enqueue(object : Callback<PackageData> {
//            override fun onResponse(call: Call<PackageData>, response: Response<PackageData>) {
//
//                if (response.body() != null) {
//
//                    DialogUtils.dismissDialog()
//
//                    if (response.body()!!.Status == "true") {
//
//                        chooseCoarseAdapter = NewChooseCoarseAdapter(activity!!, response.body()!!.data)
//                        new_coarse_rvCoarseList.adapter = chooseCoarseAdapter
//
//                    } else {
//
//                        Toast.makeText(activity, response.body()!!.Msg, Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<PackageData>, t: Throwable) {
//                // Log error here since request failed
//                Log.e("", t.toString())
//                DialogUtils.dismissDialog()
//            }
//        })
//    }
//
//}
