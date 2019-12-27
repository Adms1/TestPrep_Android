package com.testcraft.testcraft.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.google.gson.JsonObject
import com.testcraft.testcraft.R
import com.testcraft.testcraft.activity.DashboardActivity.Companion.setFragments
import com.testcraft.testcraft.retrofit.WebClient
import com.testcraft.testcraft.retrofit.WebInterface
import com.testcraft.testcraft.utils.*
import kotlinx.android.synthetic.main.activity_change_password.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChangePasswordActivity : Fragment() {

//    override fun attachBaseContext(newBase: Context?) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
//    }

    var bundle: Bundle? = null
    var come: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.activity_change_password, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CommonWebCalls.callToken(activity!!, "1", "", ActionIdData.C3500, ActionIdData.T3500)

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

                CommonWebCalls.callToken(
                    activity!!,
                    "1",
                    "",
                    ActionIdData.C3501,
                    ActionIdData.T3501
                )

                if (isValid1()) {
                    if (isValid()) {
                        callChangePasswordlApi()
                    }
                }

            } else {

                CommonWebCalls.callToken(
                    activity!!,
                    "1",
                    "",
                    ActionIdData.C4101,
                    ActionIdData.T4101
                )


                if (isValid()) {
                    callChangePasswordlApi()
                }
            }
        }

        chngepass_etconfirmpass.setOnEditorActionListener { v, actionId, event ->
            if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                if (come == "other") {

                    CommonWebCalls.callToken(
                        activity!!,
                        "1",
                        "",
                        ActionIdData.C3501,
                        ActionIdData.T3501
                    )

                    if (isValid1()) {
                        if (isValid()) {
                            callChangePasswordlApi()
                        }
                    }

                } else {

                    CommonWebCalls.callToken(
                        activity!!,
                        "1",
                        "",
                        ActionIdData.C4101,
                        ActionIdData.T4101
                    )

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
            Utils.ping(activity!!, AppConstants.NETWORK_MSG)
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
                            setFragments(null)

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
