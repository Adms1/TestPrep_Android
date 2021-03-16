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
import com.testcraft.testcraft.retrofit.WebClient
import com.testcraft.testcraft.utils.AppConstants
import com.testcraft.testcraft.utils.DialogUtils
import com.testcraft.testcraft.utils.Utils
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_deeplink_test.*
import kotlinx.android.synthetic.main.activity_phone_login.*

class DeeplinkTestActivity : AppCompatActivity() {

    var compositeDisposable: CompositeDisposable? = null

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        setContentView(R.layout.activity_deeplink_test)

        compositeDisposable = CompositeDisposable()

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

        compositeDisposable?.add(WebClient.buildService().getDplinkTest(Utils.getStringValue(this@DeeplinkTestActivity, AppConstants.USER_ID, "0")!!)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({ response ->
                if (response != null) {

                    DialogUtils.dismissDialog()

                    if (response.Status == "true") {

                        val testArr = response.data
                        dplinktest_rvPkgList.adapter =
                            DeeplinkTestListAdapter(this@DeeplinkTestActivity, testArr)

                    } else {
                        Toast.makeText(this@DeeplinkTestActivity, response.Msg, Toast.LENGTH_SHORT).show()
                    }
                }
            }, { error ->
                Log.e("", error.toString())
                DialogUtils.dismissDialog()
            }))

    }

    override fun onDestroy() {
        super.onDestroy()

        compositeDisposable!!.clear()
    }

}
