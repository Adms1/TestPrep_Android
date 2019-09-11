package com.testprep.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.testprep.R
import com.testprep.adapter.TestListAdapter
import com.testprep.models.TestListModel
import com.testprep.retrofit.WebClient
import com.testprep.retrofit.WebInterface
import com.testprep.utils.AppConstants
import com.testprep.utils.DialogUtils
import com.testprep.utils.Utils
import kotlinx.android.synthetic.main.activity_test_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TestListActivity : Fragment() {

    var bundle: Bundle? = null
    private var pname = ""
    private var pkgid = ""

//    override fun attachBaseContext(newBase: Context?) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_test_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        bundle = this.arguments
        pname = bundle!!.getString("pname")
        pkgid = bundle!!.getString("pkgid")

//        test_header.text = intent.getStringExtra("pname")

        test_rvPkgList.layoutManager = LinearLayoutManager(activity!!, LinearLayoutManager.VERTICAL, false)

//        test_ivBack.setOnClickListener { onBackPressed() }

        callTestListApi()
    }

//    override fun onResume() {
//        super.onResume()
//
//        callTestListApi()
//    }

    fun callTestListApi() {

        if (!DialogUtils.isNetworkConnected(activity!!)) {
            Utils.ping(activity!!, "Connetion not available")
        }

        DialogUtils.showDialog(activity!!)

        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getTestList(
            Utils.getStringValue(activity!!, AppConstants.USER_ID, "0")!!,
            pkgid
        )
        call.enqueue(object : Callback<TestListModel> {
            override fun onResponse(call: Call<TestListModel>, response: Response<TestListModel>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.Status == "true") {

                        val testArr = response.body()!!.data
                        test_rvPkgList.adapter = TestListAdapter(activity!!, testArr, pkgid, pname)

                    } else {
                        Toast.makeText(activity!!, response.body()!!.Msg, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<TestListModel>, t: Throwable) {
                // Log error here since request failed
                Log.e("", t.toString())
                DialogUtils.dismissDialog()
            }
        })
    }
}
