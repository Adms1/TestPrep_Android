package com.testcraft.testcraft.fragments

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
import com.testcraft.testcraft.activity.DashboardActivity
import com.testcraft.testcraft.retrofit.WebClient
import com.testcraft.testcraft.utils.AppConstants
import com.testcraft.testcraft.utils.DialogUtils
import com.testcraft.testcraft.utils.Utils
import kotlinx.android.synthetic.main.fragment_contact_us.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContactUsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact_us, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        contactus_etFname.setText(Utils.getStringValue(activity!!, AppConstants.FIRST_NAME, ""))
        contactus_etLname.setText(Utils.getStringValue(activity!!, AppConstants.LAST_NAME, ""))
        contactus_etEmail.setText(Utils.getStringValue(activity!!, AppConstants.USER_EMAIL, ""))
        contactus_etMobile.setText(Utils.getStringValue(activity!!, AppConstants.USER_MOBILE, ""))

        contactus_btnSignup.setOnClickListener {

            if (isValid()) {
                callSendEnquiry()
            }
        }
    }

    fun callSendEnquiry() {

        if (!DialogUtils.isNetworkConnected(activity!!)) {
//            Utils.ping(activity!!, AppConstants.NETWORK_MSG)
            DialogUtils.NetworkDialog(activity!!)
            DialogUtils.dismissDialog()
        }

        DialogUtils.showDialog(activity!!)

        val call = WebClient.buildService().sendEnquiry(
            contactus_etFname.text.toString(),
            contactus_etLname.text.toString(),
            contactus_etEmail.text.toString(),
            contactus_etMobile.text.toString(),
            contactus_etComment.text.toString()
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

                        AppConstants.isFirst = 4
                        DashboardActivity.setFragments(null)

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

        if (TextUtils.isEmpty(contactus_etFname.text.toString()) || contactus_etFname.text.toString().trim().isEmpty()) {
            contactus_etFname.error = "first name must not be null"
            isvalid = false
        }

        if (TextUtils.isEmpty(contactus_etLname.text.toString()) || contactus_etLname.text.toString().trim().isEmpty()) {
            contactus_etLname.error = "last name must not be null"
            isvalid = false
        }

        if (TextUtils.isEmpty(contactus_etComment.text.toString())) {
            contactus_etComment.error = "Please enter your comment"
            isvalid = false
        }

        if (TextUtils.isEmpty(contactus_etEmail.text.toString()) || !Patterns.EMAIL_ADDRESS.matcher(
                contactus_etEmail.text.toString()
            ).matches()
        ) {
            contactus_etEmail.error = "Please enter valid Email Address"
            isvalid = false
        }

        if (TextUtils.isEmpty(contactus_etMobile.text.toString()) || !Patterns.PHONE.matcher(
                contactus_etMobile.text.toString()
            ).matches()
        ) {
            contactus_etMobile.error = "Please enter valid mobile number"
            isvalid = false
        }

        if (contactus_etMobile.text.toString().length != 10) {
            contactus_etMobile.error = "Please enter valid mobile number"
            isvalid = false
        }

        return isvalid

    }


}
