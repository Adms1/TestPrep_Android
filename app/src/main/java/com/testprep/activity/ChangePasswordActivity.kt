package com.testprep.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import com.google.gson.JsonObject
import com.testprep.R
import com.testprep.retrofit.WebClient
import com.testprep.retrofit.WebInterface
import com.testprep.utils.AppConstants
import com.testprep.utils.DialogUtils
import com.testprep.utils.Utils
import com.testprep.utils.WebRequests
import kotlinx.android.synthetic.main.activity_change_password.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.view.inputmethod.InputMethodManager


class ChangePasswordActivity : Fragment() {

//    override fun attachBaseContext(newBase: Context?) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
//    }

    var bundle: Bundle? = null
    var come: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(com.testprep.R.layout.activity_change_password, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bundle = this.arguments
        come = bundle!!.getString("come_from")

        if (come == "other") {

            chngepass_ivoldpass.visibility = View.VISIBLE
            chngepass_llOldpass.visibility = View.VISIBLE

        } else {

            chngepass_ivoldpass.visibility = View.GONE
            chngepass_llOldpass.visibility = View.GONE

        }

        chngepass_btnChange.setOnClickListener {

            if (come == "other") {

                if (isValid1()) {
                    if (isValid()) {
                        callChangePasswordlApi()
                    }
                }

            } else {

                if (isValid()) {
                    callChangePasswordlApi()
                }
            }
        }

        chngepass_etconfirmpass.setOnEditorActionListener { v, actionId, event ->
            if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                if (come == "other") {

                    if (isValid1()) {
                        if (isValid()) {
                            callChangePasswordlApi()
                        }
                    }

                } else {

                    if (isValid()) {
                        callChangePasswordlApi()
                    }
                }

            }
            false
        }

        chngepass_ivBack.setOnClickListener {

//            onBackPressed()
        }

    }

    fun callChangePasswordlApi() {

        if (!DialogUtils.isNetworkConnected(activity!!)) {
            Utils.ping(activity!!, "Connetion not available")
        }

        DialogUtils.showDialog(activity!!)

        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.changePassword(
            WebRequests.changePasswordParams(
                Utils.getStringValue(activity!!, AppConstants.USER_ID, "")!!,
                chngepass_etNewpass.text.toString()
            )
        )

        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!["Status"].asString == "true") {

                        Toast.makeText(
                            activity!!,
                            response.body()!!["Msg"].asString,
                            Toast.LENGTH_SHORT
                        )
                            .show()

                        Utils.setStringValue(activity!!, "is_login", "true")

//                        overridePendingTransition(R.anim.slide_in_leftt, R.anim.slide_out_right)

                        Utils.setStringValue(
                            activity!!,
                            AppConstants.FIRST_NAME,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StudentFirstName"].asString
                        )
                        Utils.setStringValue(
                            activity!!,
                            AppConstants.LAST_NAME,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StudentLastName"].asString
                        )
                        Utils.setStringValue(
                            activity!!,
                            AppConstants.USER_ID,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StudentID"].asString
                        )
                        Utils.setStringValue(
                            activity!!,
                            AppConstants.USER_EMAIL,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StudentEmailAddress"].asString
                        )
                        Utils.setStringValue(
                            activity!!,
                            AppConstants.USER_PASSWORD,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StudentPassword"].asString
                        )
                        Utils.setStringValue(
                            activity!!,
                            AppConstants.USER_MOBILE,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StudentMobile"].asString
                        )
                        Utils.setStringValue(
                            activity!!,
                            AppConstants.USER_ACCOUNT_TYPE,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["AccountTypeID"].asString
                        )
                        Utils.setStringValue(
                            activity!!,
                            AppConstants.USER_STATUSID,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StatusID"].asString
                        )

//                        Handler().postDelayed(
//                            /* Runnable
//                                 * Showing splash screen with a timer. This will be useful when you
//                                 * want to show case your app logo / company
//                                 */
//
//                            {

                                if (come == "other") {

                                    AppConstants.isFirst = 4
                                    val intent1 = Intent(activity, DashboardActivity::class.java)
                                    intent1.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(intent1)
                                    activity!!.finish()

                                } else {
                                    AppConstants.isFirst = 0
                                    val intent = Intent(activity!!, LoginActivity::class.java)
                                    startActivity(intent)
                                    activity!!.finish()
                                }
//                            }, 2500
//                        )

                    } else {
                        Toast.makeText(
                            activity!!,
                            response.body()!!["Msg"].asString,
                            Toast.LENGTH_SHORT
                        )
                            .show()
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

    fun isValid(): Boolean {

        var isvalid = true

//        if(TextUtils.isEmpty(login_etEmail.text.toString())){
//            login_etEmail.error = "Email Address must not be null"
//            isvalid = false
//        }

        if (TextUtils.isEmpty(chngepass_etNewpass.text.toString()) || chngepass_etNewpass.text!!.length < 6) {
            chngepass_etNewpass.error = "you have to enter at least 6 digit!"
            isvalid = false
        }

        if (TextUtils.isEmpty(chngepass_etconfirmpass.text.toString()) || chngepass_etconfirmpass.text!!.length < 6) {
            chngepass_etconfirmpass.error = "you have to enter at least 6 digit!"
            isvalid = false
        }

        if (chngepass_etNewpass.text.toString() != chngepass_etconfirmpass.text.toString()) {
            chngepass_etconfirmpass.error = "Password does not match"
            isvalid = false
        }

        return isvalid

    }


    fun isValid1(): Boolean {

        var isvalid1 = true

//        if(TextUtils.isEmpty(login_etEmail.text.toString())){
//            login_etEmail.error = "Email Address must not be null"
//            isvalid = false
//        }

        if (TextUtils.isEmpty(chngepass_etOldpass.text.toString()) || chngepass_etOldpass.text!!.length < 6) {
            chngepass_etOldpass.error = "you have to enter at least 6 digit!"
            isvalid1 = false
        }

        if (chngepass_etOldpass.text.toString() != Utils.getStringValue(
                activity!!,
                AppConstants.USER_PASSWORD,
                ""
            )!!
        ) {

            chngepass_etOldpass.error = "Old password does not matched."
            isvalid1 = false
        }

        return isvalid1

    }


}
