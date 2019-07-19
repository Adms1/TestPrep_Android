package com.testprep.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.testprep.R
import com.testprep.adapter.TutorsAdapter
import kotlinx.android.synthetic.main.fragment_tutors.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class TutorsFragment : Fragment() {

//    private var mDataList: ArrayList<PackageData.PackageDataList>? = null
private var tutorsAdapter: TutorsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tutors, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tutors_rvList.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

//        tutorsAdapter = TutorsAdapter(activity!!)
//        tutors_rvList.adapter = tutorsAdapter

    }

    //    fun callTutorsListApi() {
//
//        if (!DialogUtils.isNetworkConnected(activity!!)) {
//            Utils.ping(activity!!, "Connetion not available")
//        }
//
//        DialogUtils.showDialog(activity!!)
//        val apiService = WebClient.getClient().create(WebInterface::class.java)
//
//        val call = apiService.getPackage(
//            WebRequests.getPackageParams(
////                Utils.getStringValue(activity, "course_type_id", "0")!!,
////                Utils.getStringValue(activity, "course_id", "0")!!,
////                Utils.getStringValue(activity, "std_id", "0")!!,
////                intent.getStringExtra("subject_id")
//
//                Utils.getStringValue(activity!!, "course_type_id", "")!!,
//                Utils.getStringValue(activity!!, "course_id", "")!!,
//                Utils.getStringValue(activity!!, "standard_id", "")!!,
//                Utils.getStringValue(activity!!, "subject_id", "")!!
//            )
//        )
//
//        call.enqueue(object : Callback<PackageData> {
//            override fun onResponse(call: Call<PackageData>, response: Response<PackageData>) {
//
//                if (response.body() != null) {
//
//                    DialogUtils.dismissDialog()
//
//                    if (response.body()!!.Status == "true") {
//
//                        mDataList = response.body()!!.data
//
//                        tutorsAdapter = TutorsAdapter(activity!!, mDataList!!)
//                        singleTest_rvList.adapter = tutorsAdapter
//
//                    } else {
//
//                        Toast.makeText(activity!!, response.body()!!.Msg, Toast.LENGTH_SHORT).show()
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

}
