package com.testcraft.testcraft.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import com.testcraft.testcraft.R
import com.testcraft.testcraft.retrofit.WebClient
import com.testcraft.testcraft.utils.*
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : AppCompatActivity() {

    private var myCompositeDisposable: CompositeDisposable? = null

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

//    var connectivity: Connectivity? = null
//
//    override fun onResume() {
//        super.onResume()
//        val filter = IntentFilter()
//        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
//        registerReceiver(connectivity, filter)
//    }
//
//    override fun onStop() {
//        super.onStop()
//        unregisterReceiver(connectivity)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        setContentView(R.layout.activity_forgot_password)

        myCompositeDisposable = CompositeDisposable()
//        connectivity = Connectivity()

        CommonWebCalls.callToken(
            this@ForgotPasswordActivity,
            "1",
            "",
            ActionIdData.C3900,
            ActionIdData.T3900
        )

        forgot_pass_btnReset.setOnClickListener {

            resetPassword()
        }

        forgot_pass_etEmail.setOnEditorActionListener { v, actionId, event ->
            if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {

                resetPassword()

            }
            false
        }


        forgot_pass_ivBack.setOnClickListener { onBackPressed() }

    }

    fun resetPassword() {

        CommonWebCalls.callToken(
            this@ForgotPasswordActivity,
            "1",
            "",
            ActionIdData.C3901,
            ActionIdData.T3901
        )

        when {
            TextUtils.isEmpty(forgot_pass_etEmail.text.toString()) -> forgot_pass_etEmail.error =
                "Please Enter Mobile Number"
            forgot_pass_etEmail.text!!.length != 10                -> forgot_pass_etEmail.error =
                "Please enter valid Mobile Number"
            else                                                   -> {
                callForgotPasswordlApi()
            }

        }
    }

    fun callForgotPasswordlApi() {

        if (!DialogUtils.isNetworkConnected(this@ForgotPasswordActivity)) {
//            Utils.ping(activity!!, AppConstants.NETWORK_MSG)
            DialogUtils.NetworkDialog(this@ForgotPasswordActivity)
            DialogUtils.dismissDialog()
        }

        DialogUtils.showDialog(this@ForgotPasswordActivity)

        myCompositeDisposable?.add(WebClient.buildService().forgotPassword(WebRequests.checkForgotpassParams(forgot_pass_etEmail.text.toString()))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({ response -> setResponse(response) }, { t -> onFailure(t) }))

//        val call =
//            WebClient.buildService().forgotPassword(WebRequests.checkForgotpassParams(forgot_pass_etEmail.text.toString()))
//        call.enqueue(object : Callback<JsonObject> {
//            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
//
//                if (response.body() != null) {
//
//                    DialogUtils.dismissDialog()
//
//                    if (response.body()!!["Status"].asString == "true") {
//
//                        Utils.setStringValue(
//                            this@ForgotPasswordActivity,
//                            AppConstants.USER_ID,
//                            response.body()!!["data"].asJsonArray[0].asJsonObject["StudentID"].asString
//                        )
//
//                        val intent = Intent(this@ForgotPasswordActivity, OtpActivity::class.java)
//
//                        intent.putExtra("mobile_number", forgot_pass_etEmail.text.toString())
//                        intent.putExtra(
//                            "otp",
//                            response.body()!!["data"].asJsonArray[0].asJsonObject["OTP"].asString
//                        )
//                        intent.putExtra("come_from", "forgot password")
//                        intent.putExtra("first_name", "")
//                        intent.putExtra("last_name", "")
//                        intent.putExtra("email", "")
//                        intent.putExtra("password", "")
//                        intent.putExtra("account_type", "1")
//                        startActivity(intent)
//
//                    } else {
//                        Toast.makeText(
//                            this@ForgotPasswordActivity,
//                            response.body()!!["Msg"].asString,
//                            Toast.LENGTH_SHORT
//                        )
//                            .show()
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
//                // Log error here since request failed
//                Log.e("", t.toString())
//                DialogUtils.dismissDialog()
//            }
//        })
    }

    private fun setResponse(response: JsonObject) {
        DialogUtils.dismissDialog()

        if (response.get("Status").asString == "true") {

            Utils.setStringValue(
                this@ForgotPasswordActivity, AppConstants.USER_ID, response.get("data").asJsonArray[0].asJsonObject["StudentID"].asString)

            val intent = Intent(this@ForgotPasswordActivity, OtpActivity::class.java)

            intent.putExtra("mobile_number", forgot_pass_etEmail.text.toString())
            intent.putExtra("otp", response.get("data").asJsonArray[0].asJsonObject["OTP"].asString)
            intent.putExtra("come_from", "forgot password")
            intent.putExtra("first_name", "")
            intent.putExtra("last_name", "")
            intent.putExtra("email", "")
            intent.putExtra("password", "")
            intent.putExtra("account_type", "1")
            startActivity(intent)

        } else {
            Toast.makeText(
                this@ForgotPasswordActivity,
                response.get("Msg").asString,
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    private fun onFailure(t: Throwable) {
        Log.e("", t.toString())
        DialogUtils.dismissDialog()
    }

    override fun onDestroy() {
        super.onDestroy()

        myCompositeDisposable!!.clear()
    }

}
