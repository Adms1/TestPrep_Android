package com.testcraft.testcraft

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.testcraft.testcraft.activity.IntroActivity
import com.testcraft.testcraft.activity.LoginActivity
import com.testcraft.testcraft.activity.OtpActivity
import com.testcraft.testcraft.activity.ViewInvoiceActivity
import com.testcraft.testcraft.models.VerifyMobileData
import com.testcraft.testcraft.retrofit.WebClient
import com.testcraft.testcraft.retrofit.WebInterface
import com.testcraft.testcraft.utils.*
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_otp.*
import kotlinx.android.synthetic.main.activity_phone_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PhoneLoginActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        setContentView(R.layout.activity_phone_login)

        phonelogin_btnSignup.setOnClickListener {

            phonelogin_btnSignup.isClickable = false
            phonelogin_btnSignup.postDelayed(Runnable {
                phonelogin_btnSignup.isClickable = true
            }, 2000)

            CommonWebCalls.callToken(
                this@PhoneLoginActivity,
                "1",
                "",
                ActionIdData.C301,
                ActionIdData.T301
            )

            if (!TextUtils.isEmpty(phonelogin_etPhone.text.toString()) && Patterns.PHONE.matcher(
                    phonelogin_etPhone.text.toString()).matches() && phonelogin_etPhone.length() >= 10) {

//                if (phonelogin_cbTerms.isChecked) {

                callVerifyAccountApi(phonelogin_etPhone.text.toString())

//                } else {
//                    Utils.ping(this@PhoneLoginActivity, "Select Terms & Conditions")
//                }
            } else {
                phonelogin_etPhone.error = "Please enter valid mobile number"
            }
        }

        phonelogin_btnLogin.setOnClickListener {

            CommonWebCalls.callToken(
                this@PhoneLoginActivity,
                "1",
                "",
                ActionIdData.C302,
                ActionIdData.T302
            )

            val intent = Intent(this@PhoneLoginActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        phonelogin_tvTerms.setOnClickListener {

            if (!DialogUtils.isNetworkConnected(this@PhoneLoginActivity)) {
//            Utils.ping(activity!!, AppConstants.NETWORK_MSG)
                DialogUtils.NetworkDialog(this@PhoneLoginActivity)
                DialogUtils.dismissDialog()
            } else {

                val intent = Intent(this@PhoneLoginActivity, ViewInvoiceActivity::class.java)
                intent.putExtra("header", "Terms & Conditions")
                intent.putExtra(
                    "url",
                    "http://testcraft.in/TCTerms.aspx"
                )
                startActivity(intent)
            }
        }

        phonelogin_ivBack.setOnClickListener {
            onBackPressed()
        }

    }

    override fun onBackPressed() {
        val intent = Intent(this@PhoneLoginActivity, IntroActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun callVerifyAccountApi(phone: String) {

        if (!DialogUtils.isNetworkConnected(this@PhoneLoginActivity)) {
//            Utils.ping(activity!!, AppConstants.NETWORK_MSG)
            DialogUtils.NetworkDialog(this@PhoneLoginActivity)
            DialogUtils.dismissDialog()
        }

        DialogUtils.showDialog(this@PhoneLoginActivity)

        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.checkMobile(phone)

        call.enqueue(object : Callback<VerifyMobileData> {
            override fun onResponse(call: Call<VerifyMobileData>, response: Response<VerifyMobileData>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.Status == "true") {

                        Utils.setStringValue(
                            this@PhoneLoginActivity,
                            AppConstants.OTP,
                            response.body()!!.data[0].OTP
                        )

                        Utils.setStringValue(this@PhoneLoginActivity, AppConstants.APP_MODE, AppConstants.NORMAL_MODE)

                        if(response.body()!!.data[0].StudentID.toString() != "0") {
                            if (response.body()!!.data[0].Preference.size > 0) {

                                Utils.setStringValue(this@PhoneLoginActivity, AppConstants.isPrefrence, "1")

                                Utils.setStringValue(
                                    this@PhoneLoginActivity,
                                    AppConstants.COURSE_TYPE_ID,
                                    response.body()!!.data[0].Preference[0].CourseTypeID
                                )
                                Utils.setStringValue(
                                    this@PhoneLoginActivity,
                                    AppConstants.COURSE_ID,
                                    response.body()!!.data[0].Preference[0].BoardID
                                )
                                Utils.setStringValue(
                                    this@PhoneLoginActivity,
                                    AppConstants.STANDARD_ID,
                                    response.body()!!.data[0].Preference[0].StandardID
                                )
                                Utils.setStringValue(
                                    this@PhoneLoginActivity,
                                    AppConstants.SUBJECT_ID,
                                    response.body()!!.data[0].Preference[0].SubjectID
                                )
                            }
//                        overridePendingTransition(R.anim.slide_in_leftt, R.anim.slide_out_right)

                            Utils.setStringValue(
                                this@PhoneLoginActivity,
                                AppConstants.FIRST_NAME,
                                response.body()!!.data[0].StudentFirstName
                            )
                            Utils.setStringValue(
                                this@PhoneLoginActivity,
                                AppConstants.LAST_NAME,
                                response.body()!!.data[0].StudentLastName
                            )
                            Utils.setStringValue(
                                this@PhoneLoginActivity,
                                AppConstants.USER_ID,
                                response.body()!!.data[0].StudentID.toString()
                            )
                            Utils.setStringValue(
                                this@PhoneLoginActivity,
                                AppConstants.USER_EMAIL,
                                response.body()!!.data[0].StudentEmailAddress
                            )
                            Utils.setStringValue(
                                this@PhoneLoginActivity,
                                AppConstants.USER_PASSWORD,
                                response.body()!!.data[0].StudentPassword
                            )
                            Utils.setStringValue(
                                this@PhoneLoginActivity,
                                AppConstants.USER_MOBILE,
                                response.body()!!.data[0].StudentMobile
                            )
                            Utils.setStringValue(
                                this@PhoneLoginActivity,
                                AppConstants.USER_ACCOUNT_TYPE,
                                response.body()!!.data[0].AccountTypeID.toString()
                            )
                            Utils.setStringValue(
                                this@PhoneLoginActivity,
                                AppConstants.USER_STATUSID,
                                response.body()!!.data[0].StatusID.toString()
                            )
                        }

                        val intent = Intent(this@PhoneLoginActivity, OtpActivity::class.java)
                        intent.putExtra("mobile_number", response.body()!!.data[0].StudentMobile)
                        intent.putExtra("otp", response.body()!!.data[0].OTP)
                        intent.putExtra("come_from", "signup")
                        intent.putExtra("student_id", response.body()!!.data[0].StudentID.toString())
                        intent.putExtra("first_name", response.body()!!.data[0].StudentFirstName)
                        intent.putExtra("last_name", response.body()!!.data[0].StudentLastName)
                        intent.putExtra("email", response.body()!!.data[0].StudentEmailAddress)
                        intent.putExtra("password", response.body()!!.data[0].StudentPassword)
                        intent.putExtra("account_type", response.body()!!.data[0].AccountTypeID.toString())
                        startActivity(intent)

                    } else {
                        Toast.makeText(
                            this@PhoneLoginActivity,
                            response.body()!!.Msg,
                            Toast.LENGTH_LONG
                        ).show()

//                    Log.d("loginresponse", response.body()!!.asString)
                    }
                }
            }

            override fun onFailure(call: Call<VerifyMobileData>, t: Throwable) {
                // Log error here since request failed
                Log.e("", t.toString())
                DialogUtils.dismissDialog()
            }
        })
    }
}
