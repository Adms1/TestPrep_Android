package com.testprep.fragments

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.*
import android.widget.Toast
import com.google.gson.JsonObject
import com.testprep.R
import com.testprep.retrofit.WebClient
import com.testprep.retrofit.WebInterface
import com.testprep.utils.AppConstants
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
class UpdateProfileFragment : Fragment() {

    var userid = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_signup, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userid = Utils.getStringValue(activity!!, AppConstants.USER_ID, "").toString()
        signup_etFname.setText(Utils.getStringValue(activity!!, AppConstants.FIRST_NAME, ""))
        signup_etLname.setText(Utils.getStringValue(activity!!, AppConstants.LAST_NAME, ""))
        signup_etEmail.setText(Utils.getStringValue(activity!!, AppConstants.USER_EMAIL, ""))
        signup_etPassword.setText(Utils.getStringValue(activity!!, AppConstants.USER_PASSWORD, ""))
        signup_etMobile.setText(Utils.getStringValue(activity!!, AppConstants.USER_MOBILE, ""))

        signup_btnSignup.text = getString(R.string.update)
        signup_tvHeading.visibility = View.GONE

        signup_btnSignup.setOnClickListener {

            if (isValid()) {
                callSignupApi()
            }
        }
    }

    fun callSignupApi() {

        val sortDialog = Dialog(activity!!)//,R.style.PauseDialog);//, R.style.PauseDialog);
        val window = sortDialog.window
        val wlp = window!!.attributes
        sortDialog.window!!.attributes.verticalMargin = 0.10f
        wlp.gravity = Gravity.BOTTOM
        window.attributes = wlp

//        sortDialog.window!!.setBackgroundDrawableResource(R.drawable.filter1_1)

        sortDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        sortDialog.setCancelable(true)
//        sortDialog.setContentView(getRoot())
        sortDialog.show()

        val apiService = WebClient.getClient().create(WebInterface::class.java)
        val call = apiService.updateProfile(
            WebRequests.addSignupParams(
                Utils.getStringValue(activity!!, AppConstants.USER_LOGIN_TYPE, "")!!,
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

                if (response.body()!!.get("Status").asString == "true") {

                    sortDialog.dismiss()
                    Toast.makeText(activity!!, response.body()!!.get("Msg").asString, Toast.LENGTH_LONG).show()

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
                        AppConstants.USER_LOGIN_TYPE,
                        response.body()!!["data"].asJsonArray[0].asJsonObject["LoginTypeID"].asString
                    )
                    Utils.setStringValue(
                        activity!!,
                        AppConstants.USER_STATUSID,
                        response.body()!!["data"].asJsonArray[0].asJsonObject["StatusID"].asString
                    )

                    Log.d("websize", response.body()!!.get("Msg").asString)

                } else {

                    sortDialog.dismiss()
                    Toast.makeText(activity!!, response.body()!!.get("Msg").asString, Toast.LENGTH_LONG).show()

                    Log.d("websize", response.body()!!.get("Msg").asString)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // Log error here since request failed
                Log.e("", t.toString())
                sortDialog.dismiss()
            }
        })

    }

    fun isValid(): Boolean {

        var isvalid = true

        if (TextUtils.isEmpty(signup_etFname.text.toString())) {
            signup_etFname.error = "first name must not be null"
            isvalid = false
        }

        if (TextUtils.isEmpty(signup_etLname.text.toString())) {
            signup_etLname.error = "last name must not be null"
            isvalid = false
        }

        if (TextUtils.isEmpty(signup_etEmail.text.toString()) || !Patterns.EMAIL_ADDRESS.matcher(signup_etEmail.text.toString()).matches()) {
            signup_etEmail.error = "Please enter valid Email Address"
            isvalid = false
        }

        if (TextUtils.isEmpty(signup_etPassword.text.toString())) {
            signup_etPassword.error = "password must not be null"
            isvalid = false
        }

        if (TextUtils.isEmpty(signup_etCPassword.text.toString())) {
            signup_etCPassword.error = "confirm password must not be null"
            isvalid = false
        }

        if (signup_etPassword.text.toString() != signup_etCPassword.text.toString()) {
            signup_etCPassword.error = "password and confirm password must be same"
            isvalid = false
        }

        if (TextUtils.isEmpty(signup_etMobile.text.toString()) || !android.util.Patterns.PHONE.matcher(signup_etMobile.text.toString()).matches()) {
            signup_etMobile.error = "Please enter valid mobile number"
            isvalid = false
        }

        return isvalid

    }

}
