package com.testprep.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.gson.JsonObject
import com.testprep.R
import com.testprep.activity.DashboardActivity.Companion.setFragments
import com.testprep.retrofit.WebClient
import com.testprep.retrofit.WebInterface
import com.testprep.utils.AppConstants
import com.testprep.utils.DialogUtils
import com.testprep.utils.Utils
import com.testprep.utils.WebRequests
import kotlinx.android.synthetic.main.activity_signup.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * A simple [Fragment] subclass.
 *
 */
class UpdateProfileActivity : Fragment() {

//    override fun attachBaseContext(newBase: Context?) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
//    }

    var userid = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

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

        userid = Utils.getStringValue(activity!!, AppConstants.USER_ID, "").toString()
        signup_etFname.setText(Utils.getStringValue(activity!!, AppConstants.FIRST_NAME, ""))
        signup_etLname.setText(Utils.getStringValue(activity!!, AppConstants.LAST_NAME, ""))
        signup_etEmail.setText(Utils.getStringValue(activity!!, AppConstants.USER_EMAIL, ""))

        signup_etEmail.isFocusable = false
        signup_etMobile.isFocusable = false

        signup_etPassword.setText(Utils.getStringValue(activity!!, AppConstants.USER_PASSWORD, ""))

        signup_etMobile.setText(Utils.getStringValue(activity!!, AppConstants.USER_MOBILE, ""))

        signup_btnSignup.text = getString(com.testprep.R.string.update)

        signup_btnSignup.setOnClickListener {

            if (isValid()) {
                callSignupApi()
            }
        }

//        signup_ivBack.setOnClickListener { onBackPressed() }
    }

    fun callSignupApi() {

        if (!DialogUtils.isNetworkConnected(activity!!)) {
            Utils.ping(activity!!, "Connetion not available")
        }

        DialogUtils.showDialog(activity!!)
        val apiService = WebClient.getClient().create(WebInterface::class.java)
        val call = apiService.updateProfile(
            WebRequests.addSignupParams(
                Utils.getStringValue(activity!!, AppConstants.USER_ACCOUNT_TYPE, "")!!,
                Utils.getStringValue(activity!!, AppConstants.USER_ID, "")!!,
                signup_etFname.text.toString(),
                signup_etLname.text.toString(),
                signup_etEmail.text.toString(),
                signup_etPassword.text.toString(),
                signup_etMobile.text.toString(),
                Utils.getStringValue(activity!!, AppConstants.USER_STATUSID, "")!!
            )
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

        if (TextUtils.isEmpty(signup_etFname.text.toString()) || signup_etFname.text.toString().contains(
                " "
            )
        ) {
            signup_etFname.error = "first name must not be null"
            isvalid = false
        }

        if (TextUtils.isEmpty(signup_etLname.text.toString()) || signup_etLname.text.toString().contains(
                " "
            )
        ) {
            signup_etLname.error = "last name must not be null"
            isvalid = false
        }

        if (Utils.getStringValue(activity!!, AppConstants.USER_ACCOUNT_TYPE, "") == "1") {

            if (TextUtils.isEmpty(signup_etPassword.text.toString())) {
                signup_etPassword.error = "password must not be null"
                isvalid = false
            }
        }

//        if (TextUtils.isEmpty(signup_etMobile.text.toString()) || !android.util.Patterns.PHONE.matcher(signup_etMobile.text.toString()).matches()) {
//            signup_etMobile.error = "Please enter valid mobile number"
//            isvalid = false
//        }

        return isvalid

    }

}
