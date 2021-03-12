package com.testcraft.testcraft.activity

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import com.testcraft.testcraft.R
import com.testcraft.testcraft.retrofit.WebClient
import com.testcraft.testcraft.utils.*
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_force_update_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForceUpdateProfileActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        setContentView(R.layout.activity_force_update_profile)

        forceprofile_etFname.setText(Utils.getStringValue(this@ForceUpdateProfileActivity, AppConstants.FIRST_NAME, ""))
        forceprofile_etLname.setText(Utils.getStringValue(this@ForceUpdateProfileActivity, AppConstants.LAST_NAME, ""))
        forceprofile_etEmail.setText(Utils.getStringValue(this@ForceUpdateProfileActivity, AppConstants.USER_EMAIL, ""))

        if (Utils.getStringValue(
                this@ForceUpdateProfileActivity,
                AppConstants.USER_ACCOUNT_TYPE, "") == "5") {
//            forceprofile_etEmail.isFocusable = true
            forceprofile_etMobile.isFocusable = false
            forceprofile_etMobile.isEnabled = false
            forceprofile_etMobile.isCursorVisible = false
            forceprofile_etMobile.keyListener = null
        } else {
            forceprofile_etEmail.isFocusable = false
            forceprofile_etEmail.isEnabled = false
            forceprofile_etEmail.isCursorVisible = false
            forceprofile_etEmail.keyListener = null
//            forceprofile_etMobile.isFocusable = true
        }

//        forceprofile_etPassword.setText(Utils.getStringValue(this@ForceUpdateProfileActivity, AppConstants.USER_PASSWORD, ""))

        forceprofile_etMobile.setText(Utils.getStringValue(this@ForceUpdateProfileActivity, AppConstants.USER_MOBILE, ""))

//        forceprofile_btnSignup.text = getString(R.string.update)

        forceprofile_btnUpdate.setOnClickListener {

            CommonWebCalls.callToken(this@ForceUpdateProfileActivity, "1", "", ActionIdData.C3401, ActionIdData.T3401)

            if (isValid()) {
                callCheckDuplicate()
            }
        }

    }

    fun callSignupApi() {

        DialogUtils.showDialog(this@ForceUpdateProfileActivity)
        val call = WebClient.buildService().updateProfile(
            WebRequests.addSignupParams(
                Utils.getStringValue(this@ForceUpdateProfileActivity, AppConstants.USER_ACCOUNT_TYPE, "")!!,
                Utils.getStringValue(this@ForceUpdateProfileActivity, AppConstants.USER_ID, "")!!,
                forceprofile_etFname.text.toString(),
                forceprofile_etLname.text.toString(),
                forceprofile_etEmail.text.toString(),
                Utils.getStringValue(this@ForceUpdateProfileActivity, AppConstants.USER_PASSWORD, "")!!,
                forceprofile_etMobile.text.toString(),
                Utils.getStringValue(this@ForceUpdateProfileActivity, AppConstants.USER_STATUSID, "")!!)
        )

        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.get("Status").asString == "true") {

                        Toast.makeText(
                            this@ForceUpdateProfileActivity,
                            response.body()!!.get("Msg").asString,
                            Toast.LENGTH_LONG
                        ).show()

                        Utils.setStringValue(
                            this@ForceUpdateProfileActivity,
                            AppConstants.FIRST_NAME,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StudentFirstName"].asString
                        )
                        Utils.setStringValue(
                            this@ForceUpdateProfileActivity,
                            AppConstants.LAST_NAME,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StudentLastName"].asString
                        )
                        Utils.setStringValue(
                            this@ForceUpdateProfileActivity,
                            AppConstants.USER_ID,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StudentID"].asString
                        )
                        Utils.setStringValue(
                            this@ForceUpdateProfileActivity,
                            AppConstants.USER_EMAIL,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StudentEmailAddress"].asString
                        )
                        Utils.setStringValue(
                            this@ForceUpdateProfileActivity,
                            AppConstants.USER_PASSWORD,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StudentPassword"].asString
                        )
                        Utils.setStringValue(
                            this@ForceUpdateProfileActivity,
                            AppConstants.USER_MOBILE,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StudentMobile"].asString
                        )
                        Utils.setStringValue(
                            this@ForceUpdateProfileActivity,
                            AppConstants.USER_ACCOUNT_TYPE,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["AccountTypeID"].asString
                        )
                        Utils.setStringValue(
                            this@ForceUpdateProfileActivity,
                            AppConstants.USER_STATUSID,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StatusID"].asString
                        )

                        finish()

                    } else {

                        Toast.makeText(
                            this@ForceUpdateProfileActivity,
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

        if (TextUtils.isEmpty(forceprofile_etFname.text.toString()) || forceprofile_etFname.text.toString().trim().isEmpty()) {
            forceprofile_etFname.error = "first name must not be null"
            isvalid = false
        }

        if (TextUtils.isEmpty(forceprofile_etLname.text.toString()) || forceprofile_etLname.text.toString().trim().isEmpty()) {
            forceprofile_etLname.error = "last name must not be null"
            isvalid = false
        }

        if (Utils.getStringValue(
                this@ForceUpdateProfileActivity,
                AppConstants.USER_ACCOUNT_TYPE, "") == "5") {
            if (TextUtils.isEmpty(forceprofile_etEmail.text.toString()) || !Patterns.EMAIL_ADDRESS.matcher(
                    forceprofile_etEmail.text.toString()
                ).matches()) {
                forceprofile_etEmail.error = "Please enter valid email"
                isvalid = false
            }
        }

        if (Utils.getStringValue(
                this@ForceUpdateProfileActivity,
                AppConstants.USER_ACCOUNT_TYPE, "") != "5") {

            if (!forceprofile_etMobile.text!!.isEmpty()) {
                if (forceprofile_etMobile.length() < 10) {
                    forceprofile_etMobile.error = "Please enter valid mobile number"
                    isvalid = false
                }
            }
        }

        return isvalid

    }

    fun callCheckDuplicate() {

        var isvalid = true

        if (!DialogUtils.isNetworkConnected(this@ForceUpdateProfileActivity)) {
//            Utils.ping(this@ForceUpdateProfileActivity, AppConstants.NETWORK_MSG)
            DialogUtils.NetworkDialog(this@ForceUpdateProfileActivity)
            DialogUtils.dismissDialog()
        }

        val call = WebClient.buildService().callCheckDuplicate(Utils.getStringValue(
            this@ForceUpdateProfileActivity,
            AppConstants.USER_ID,
            "0"
        )!!, Utils.getStringValue(this@ForceUpdateProfileActivity, AppConstants.USER_ACCOUNT_TYPE, "")!!, forceprofile_etEmail.text.toString(),
            forceprofile_etMobile.text.toString())

        call.enqueue(object : Callback<JsonObject> {

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.body()!!.get("Status").asString == "true") {

                    callSignupApi()
                } else {

                    Utils.ping(this@ForceUpdateProfileActivity, response.body()!!.get("Msg").asString)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // Log error here since request failed
                Log.e("", t.toString())
            }
        })

    }

    override fun onBackPressed() {

    }

    override fun onStop() {
        super.onStop()

        DialogUtils.dismissDialog()
    }

}
