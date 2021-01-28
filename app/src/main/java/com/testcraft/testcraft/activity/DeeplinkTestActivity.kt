package com.testcraft.testcraft.activity

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.testcraft.testcraft.R
import com.testcraft.testcraft.adapter.DeeplinkTestListAdapter
import com.testcraft.testcraft.models.TestListModel
import com.testcraft.testcraft.retrofit.WebClient
import com.testcraft.testcraft.retrofit.WebInterface
import com.testcraft.testcraft.utils.AppConstants
import com.testcraft.testcraft.utils.DialogUtils
import com.testcraft.testcraft.utils.Utils
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_deeplink_test.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeeplinkTestActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        setContentView(R.layout.activity_deeplink_test)

        dplinktest_rvPkgList.layoutManager =
            LinearLayoutManager(this@DeeplinkTestActivity, LinearLayoutManager.VERTICAL, false)

        Utils.setStringValue(this@DeeplinkTestActivity, AppConstants.IS_DEEPLINK_STEP, "2")

        calldplinkTestListApi()
    }

    private fun calldplinkTestListApi() {

        if (!DialogUtils.isNetworkConnected(this@DeeplinkTestActivity)) {
//            Utils.ping(activity!!, AppConstants.NETWORK_MSG)
            DialogUtils.NetworkDialog(this@DeeplinkTestActivity)
            DialogUtils.dismissDialog()
        }

        DialogUtils.showDialog(this@DeeplinkTestActivity)

        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getDplinkTest(
            Utils.getStringValue(this@DeeplinkTestActivity, AppConstants.USER_ID, "0")!!)

        call.enqueue(object : Callback<TestListModel> {
            override fun onResponse(call: Call<TestListModel>, response: Response<TestListModel>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.Status == "true") {

                        val testArr = response.body()!!.data
                        dplinktest_rvPkgList.adapter =
                            DeeplinkTestListAdapter(this@DeeplinkTestActivity, testArr)

                    } else {
                        Toast.makeText(this@DeeplinkTestActivity, response.body()!!.Msg, Toast.LENGTH_SHORT).show()
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
