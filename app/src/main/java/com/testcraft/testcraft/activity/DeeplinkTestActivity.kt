package com.testcraft.testcraft.activity

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.testcraft.testcraft.R
import com.testcraft.testcraft.adapter.DeeplinkTestListAdapter
import com.testcraft.testcraft.models.TestListModel
import com.testcraft.testcraft.retrofit.WebClient
import com.testcraft.testcraft.utils.AppConstants
import com.testcraft.testcraft.utils.DialogUtils
import com.testcraft.testcraft.utils.Utils
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_deeplink_test.*
import kotlinx.android.synthetic.main.activity_phone_login.*
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
            val netdialog = Dialog(this@DeeplinkTestActivity)
            netdialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            netdialog.setContentView(R.layout.dialog_network)
            netdialog.setCanceledOnTouchOutside(false)

            val btnRetry: TextView = netdialog.findViewById(R.id.network_btnRetry)

            btnRetry.setOnClickListener {
                if (DialogUtils.isNetworkConnected(this@DeeplinkTestActivity)) {
                    netdialog.dismiss()
                    calldplinkTestListApi()
                }
            }

            netdialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            netdialog.setCanceledOnTouchOutside(false)
            netdialog.setCancelable(false)
            netdialog.show()
            DialogUtils.dismissDialog()
        }

        DialogUtils.showDialog(this@DeeplinkTestActivity)

        val call = WebClient.buildService().getDplinkTest(
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
