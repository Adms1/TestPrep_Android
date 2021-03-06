package com.testcraft.testcraft.activity

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.gson.JsonObject
import com.testcraft.testcraft.R
import com.testcraft.testcraft.activity.DashboardActivity.Companion.setFragments
import com.testcraft.testcraft.retrofit.WebClient
import com.testcraft.testcraft.utils.*
import kotlinx.android.synthetic.main.activity_update_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateProfileActivity : Fragment() {

//    override fun attachBaseContext(newBase: Context?) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
//    }

    var userid = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.activity_update_profile, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//    }
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        // Inflate the layout for this fragment
//        val view = inflater.inflate(R.layout.activity_signup, container, false)
//
//        return view
//    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        val heading = activity!!.findViewById(R.id.dashboard_tvTitle) as TextView
//        heading.text = "Profile"

        CommonWebCalls.callToken(activity!!, "1", "", ActionIdData.C3400, ActionIdData.T3400)

        userid = Utils.getStringValue(activity!!, AppConstants.USER_ID, "").toString()
        update_etFname.setText(Utils.getStringValue(activity!!, AppConstants.FIRST_NAME, ""))
        update_etLname.setText(Utils.getStringValue(activity!!, AppConstants.LAST_NAME, ""))
        update_etEmail.setText(Utils.getStringValue(activity!!, AppConstants.USER_EMAIL, ""))

        if (Utils.getStringValue(
                activity!!,
                AppConstants.USER_ACCOUNT_TYPE, "") == "5") {
//            update_etEmail.isFocusable = true
            update_etMobile.isFocusable = false
            update_etMobile.isEnabled = false
            update_etMobile.isCursorVisible = false
            update_etMobile.keyListener = null
        } else {
            update_etEmail.isFocusable = false
            update_etEmail.isEnabled = false
            update_etEmail.isCursorVisible = false
            update_etEmail.keyListener = null
//            update_etMobile.isFocusable = true
        }

//        update_etPassword.setText(Utils.getStringValue(activity!!, AppConstants.USER_PASSWORD, ""))

        update_etMobile.setText(Utils.getStringValue(activity!!, AppConstants.USER_MOBILE, ""))

//        update_btnSignup.text = getString(R.string.update)

        update_btnUpdate.setOnClickListener {

            CommonWebCalls.callToken(activity!!, "1", "", ActionIdData.C3401, ActionIdData.T3401)

            if (isValid()) {
                callCheckDuplicate()
            }
        }

//        update_ivBack.setOnClickListener { onBackPressed() }
    }

    fun callSignupApi() {

        DialogUtils.showDialog(activity!!)

        val call = WebClient.buildService().updateProfile(
            WebRequests.addSignupParams(
                Utils.getStringValue(activity!!, AppConstants.USER_ACCOUNT_TYPE, "")!!,
                Utils.getStringValue(activity!!, AppConstants.USER_ID, "")!!,
                update_etFname.text.toString(),
                update_etLname.text.toString(),
                update_etEmail.text.toString(),
                Utils.getStringValue(activity!!, AppConstants.USER_PASSWORD, "")!!,
                update_etMobile.text.toString(),
                Utils.getStringValue(activity!!, AppConstants.USER_STATUSID, "")!!)
        )

        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.get("Status").asString == "true") {

                        Toast.makeText(
                            activity!!,
                            response.body()!!.get("Msg").asString,
                            Toast.LENGTH_LONG
                        ).show()

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

                        AppConstants.isFirst = 4
                        setFragments(null)

                    } else {

                        Toast.makeText(
                            activity!!,
                            response.body()!!.get("Msg").asString,
                            Toast.LENGTH_LONG
                        ).show()

                        Log.d("websize", response.body()!!.get("Msg").asString)
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

        if (TextUtils.isEmpty(update_etFname.text.toString()) || update_etFname.text.toString().trim().isEmpty()) {
            update_etFname.error = "first name must not be null"
            isvalid = false
        }

        if (TextUtils.isEmpty(update_etLname.text.toString()) || update_etLname.text.toString().trim().isEmpty()) {
            update_etLname.error = "last name must not be null"
            isvalid = false
        }

        if (Utils.getStringValue(
                activity!!,
                AppConstants.USER_ACCOUNT_TYPE, "") == "5") {
            if (TextUtils.isEmpty(update_etEmail.text.toString()) || !Patterns.EMAIL_ADDRESS.matcher(
                    update_etEmail.text.toString()
                ).matches()) {
                update_etEmail.error = "Please enter valid email"
                isvalid = false
            }
        }

        if (Utils.getStringValue(
                activity!!,
                AppConstants.USER_ACCOUNT_TYPE, "") != "5") {

            if (!update_etMobile.text!!.isEmpty()) {
                if (update_etMobile.length() < 10) {
                    update_etMobile.error = "Please enter valid mobile number"
                    isvalid = false
                }
            }
        }

        return isvalid

    }

    fun callCheckDuplicate() {

        var isvalid = true

        if (!DialogUtils.isNetworkConnected(activity!!)) {
//            Utils.ping(activity!!, AppConstants.NETWORK_MSG)
            DialogUtils.NetworkDialog(activity!!)
            DialogUtils.dismissDialog()
        }

        val call = WebClient.buildService().callCheckDuplicate(Utils.getStringValue(
            activity!!,
            AppConstants.USER_ID,
            "0"
        )!!, Utils.getStringValue(activity!!, AppConstants.USER_ACCOUNT_TYPE, "")!!, update_etEmail.text.toString(),
            update_etMobile.text.toString())

        call.enqueue(object : Callback<JsonObject> {

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.body()!!.get("Status").asString == "true") {

                    callSignupApi()
                } else {

                    Utils.ping(activity!!, response.body()!!.get("Msg").asString)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // Log error here since request failed
                Log.e("", t.toString())
            }
        })

    }

}
