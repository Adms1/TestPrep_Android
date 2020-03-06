package com.testcraft.testcraft.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.testcraft.testcraft.R
import com.testcraft.testcraft.adapter.TestListAdapter
import com.testcraft.testcraft.models.TestListModel
import com.testcraft.testcraft.retrofit.WebClient
import com.testcraft.testcraft.retrofit.WebInterface
import com.testcraft.testcraft.utils.*
import kotlinx.android.synthetic.main.activity_test_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TestListFragment : Fragment() {

    var bundle: Bundle? = null
//    private var pname = ""
//    private var pkgid = ""

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

//        bundle = this.arguments
//        pname = bundle!!.getString("pname")
//        pkgid = bundle!!.getString("pkgid")

//        AppConstants.PKG_ID = pkgid
//        AppConstants.PKG_NAME = pname

//        test_header.text = intent.getStringExtra("pname")

        CommonWebCalls.callToken(activity!!, "1", "", ActionIdData.C1900, ActionIdData.T1900)

        test_rvPkgList.layoutManager =
            LinearLayoutManager(activity!!, LinearLayoutManager.VERTICAL, false)

        test_ivInstruction.setOnClickListener {

            CommonWebCalls.callToken(activity!!, "1", "", ActionIdData.C1902, ActionIdData.T1902)

            callTestPkgInstructionApi()
        }

        callTestListApi()
    }

//    override fun onResume() {
//        super.onResume()
//
//        callTestListApi()
//    }

    private fun callTestListApi() {

        if (!DialogUtils.isNetworkConnected(activity!!)) {
            Utils.ping(activity!!, AppConstants.NETWORK_MSG)
        }

        DialogUtils.showDialog(activity!!)

        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getTestList(
            Utils.getStringValue(activity!!, AppConstants.USER_ID, "0")!!,
            AppConstants.PKG_ID
        )
        call.enqueue(object : Callback<TestListModel> {
            override fun onResponse(call: Call<TestListModel>, response: Response<TestListModel>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.Status == "true") {

                        val testArr = response.body()!!.data
                        test_rvPkgList.adapter = TestListAdapter(activity!!, testArr)

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

    fun callTestPkgInstructionApi() {

        if (!DialogUtils.isNetworkConnected(activity!!)) {
            Utils.ping(activity!!, AppConstants.NETWORK_MSG)
        }

        DialogUtils.showDialog(activity!!)

        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getPackageInstruction(AppConstants.PKG_ID)

        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.get("Status").asString == "true") {

                        val msg = response.body()!!.get("data").asString

                        DialogUtils.createConfirmDialog(
                            activity!!,
                            "Package Description",
                            msg,
                            "OK",
                            "",
                            DialogInterface.OnClickListener { dialog, which ->

                                dialog.dismiss()

                            },

                            DialogInterface.OnClickListener { dialog, which ->

                                dialog.dismiss()

                            }).show()

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
