package com.testprep.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.view.WindowManager
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

class TestListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setContentView(R.layout.activity_test_list)

        test_rvPkgList.layoutManager = LinearLayoutManager(this@TestListActivity, LinearLayoutManager.VERTICAL, false)

        test_ivBack.setOnClickListener { onBackPressed() }

        callTestListApi()
    }

    fun callTestListApi() {

        if (!DialogUtils.isNetworkConnected(this@TestListActivity)) {
            Utils.ping(this@TestListActivity, "Connetion not available")
        }

        DialogUtils.showDialog(this@TestListActivity)

        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getTestList(Utils.getStringValue(this@TestListActivity, AppConstants.USER_ID, "0")!!, "1")
        call.enqueue(object : Callback<TestListModel> {
            override fun onResponse(call: Call<TestListModel>, response: Response<TestListModel>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.Status == "true") {

                        val testArr = response.body()!!.data
                        test_rvPkgList.adapter = TestListAdapter(this@TestListActivity, testArr)

                    } else {
                        Toast.makeText(this@TestListActivity, response.body()!!.Msg, Toast.LENGTH_SHORT).show()
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
