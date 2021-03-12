package com.testcraft.testcraft.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.testcraft.testcraft.R
import com.testcraft.testcraft.models.VerifyMobileData
import com.testcraft.testcraft.retrofit.WebClient.buildService
import com.testcraft.testcraft.utils.*
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_otp.*
import kotlinx.android.synthetic.main.activity_phone_login.*
import java.util.*

class PhoneLoginActivity : AppCompatActivity() {

    private var myCompositeDisposable: CompositeDisposable? = null
    private var TAG = "PhoneLoginActivity"

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        setContentView(R.layout.activity_phone_login)

        myCompositeDisposable = CompositeDisposable()

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

                callVerifyAccountApi2(phonelogin_etPhone.text.toString())

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

    @SuppressLint("CheckResult")
    private fun callVerifyAccountApi2(phone: String) {

        if (!DialogUtils.isNetworkConnected(this@PhoneLoginActivity)) {
//            Utils.ping(activity!!, AppConstants.NETWORK_MSG)
            val netdialog = Dialog(this@PhoneLoginActivity)
            netdialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            netdialog.setContentView(R.layout.dialog_network)
            netdialog.setCanceledOnTouchOutside(false)

            val btnRetry: TextView = netdialog.findViewById(R.id.network_btnRetry)

            btnRetry.setOnClickListener {
                if (DialogUtils.isNetworkConnected(this@PhoneLoginActivity)) {
                    netdialog.dismiss()
                    callVerifyAccountApi2(phonelogin_etPhone.text.toString())
                }
            }

            netdialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            netdialog.setCanceledOnTouchOutside(false)
            netdialog.setCancelable(false)
            netdialog.show()
            DialogUtils.dismissDialog()

        } else {

            DialogUtils.showDialog(this@PhoneLoginActivity)

            myCompositeDisposable?.add(buildService().checkMobile2(phone)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ response -> setResponse(response) }, { t -> onFailure(t) }))
        }
    }

    private fun setResponse(myData: VerifyMobileData) {

        DialogUtils.dismissDialog()

        if (myData.Status == "true") {

            Utils.setStringValue(
                this@PhoneLoginActivity,
                AppConstants.OTP,
                myData.data[0].OTP
            )

            Utils.setStringValue(this@PhoneLoginActivity, AppConstants.APP_MODE, AppConstants.NORMAL_MODE)

            if (myData.data[0].StudentID.toString() != "0") {
                if (myData.data[0].Preference.size > 0) {

                    Utils.setStringValue(this@PhoneLoginActivity, AppConstants.isPrefrence, "1")

                    Utils.setStringValue(
                        this@PhoneLoginActivity,
                        AppConstants.COURSE_TYPE_ID,
                        myData.data[0].Preference[0].CourseTypeID
                    )
                    Utils.setStringValue(
                        this@PhoneLoginActivity,
                        AppConstants.COURSE_ID,
                        myData.data[0].Preference[0].BoardID
                    )
                    Utils.setStringValue(
                        this@PhoneLoginActivity,
                        AppConstants.STANDARD_ID,
                        myData.data[0].Preference[0].StandardID
                    )
                    Utils.setStringValue(
                        this@PhoneLoginActivity,
                        AppConstants.SUBJECT_ID,
                        myData.data[0].Preference[0].SubjectID
                    )
                }
//                        overridePendingTransition(R.anim.slide_in_leftt, R.anim.slide_out_right)

                Utils.setStringValue(
                    this@PhoneLoginActivity,
                    AppConstants.FIRST_NAME,
                    myData.data[0].StudentFirstName
                )
                Utils.setStringValue(
                    this@PhoneLoginActivity,
                    AppConstants.LAST_NAME,
                    myData.data[0].StudentLastName
                )
                Utils.setStringValue(
                    this@PhoneLoginActivity,
                    AppConstants.USER_ID,
                    myData.data[0].StudentID.toString()
                )
                Utils.setStringValue(
                    this@PhoneLoginActivity,
                    AppConstants.USER_EMAIL,
                    myData.data[0].StudentEmailAddress
                )
                Utils.setStringValue(
                    this@PhoneLoginActivity,
                    AppConstants.USER_PASSWORD,
                    myData.data[0].StudentPassword
                )
                Utils.setStringValue(
                    this@PhoneLoginActivity,
                    AppConstants.USER_MOBILE,
                    myData.data[0].StudentMobile
                )
                Utils.setStringValue(
                    this@PhoneLoginActivity,
                    AppConstants.USER_ACCOUNT_TYPE,
                    myData.data[0].AccountTypeID.toString()
                )
                Utils.setStringValue(
                    this@PhoneLoginActivity,
                    AppConstants.USER_STATUSID,
                    myData.data[0].StatusID.toString()
                )
            }

            val intent = Intent(this@PhoneLoginActivity, OtpActivity::class.java)
            intent.putExtra("mobile_number", myData.data[0].StudentMobile)
            intent.putExtra("otp", myData.data[0].OTP)
            intent.putExtra("come_from", "signup")
            intent.putExtra("student_id", myData.data[0].StudentID.toString())
            intent.putExtra("first_name", myData.data[0].StudentFirstName)
            intent.putExtra("last_name", myData.data[0].StudentLastName)
            intent.putExtra("email", myData.data[0].StudentEmailAddress)
            intent.putExtra("password", myData.data[0].StudentPassword)
            intent.putExtra("account_type", myData.data[0].AccountTypeID.toString())
            startActivity(intent)
            finish()

        } else {

            Toast.makeText(
                this@PhoneLoginActivity,
                myData.Msg,
                Toast.LENGTH_LONG
            ).show()

        }

    }

    private fun onFailure(t: Throwable) {
//                // Log error here since request failed
        Log.e("", t.toString())
        DialogUtils.dismissDialog()
    }

//    fun callVerifyAccountApi(phone: String) {
//
//        if (!DialogUtils.isNetworkConnected(this@PhoneLoginActivity)) {
////            Utils.ping(activity!!, AppConstants.NETWORK_MSG)
//            val netdialog = Dialog(this@PhoneLoginActivity)
//            netdialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//            netdialog.setContentView(R.layout.dialog_network)
//            netdialog.setCanceledOnTouchOutside(false)
//
//            val btnRetry: TextView = netdialog.findViewById(R.id.network_btnRetry)
//
//            btnRetry.setOnClickListener {
//                if (DialogUtils.isNetworkConnected(this@PhoneLoginActivity)) {
//                    netdialog.dismiss()
//                    callVerifyAccountApi(phonelogin_etPhone.text.toString())
//                }
//            }
//
//            netdialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            netdialog.setCanceledOnTouchOutside(false)
//            netdialog.setCancelable(false)
//            netdialog.show()
//            DialogUtils.dismissDialog()
//        }
//
//        DialogUtils.showDialog(this@PhoneLoginActivity)
//
//        val apiService = WebClient.getClient().create(WebInterface::class.java)
//
//        val call = apiService.checkMobile(phone)
//
//        call.enqueue(object : Callback<VerifyMobileData> {
//            override fun onResponse(call: Call<VerifyMobileData>, response: Response<VerifyMobileData>) {
//
//                if (response.body() != null) {
//
//                    DialogUtils.dismissDialog()
//
//                    if (response.body()!!.Status == "true") {
//
//                        Utils.setStringValue(
//                            this@PhoneLoginActivity,
//                            AppConstants.OTP,
//                            response.body()!!.data[0].OTP
//                        )
//
//                        Utils.setStringValue(this@PhoneLoginActivity, AppConstants.APP_MODE, AppConstants.NORMAL_MODE)
//
//                        if (response.body()!!.data[0].StudentID.toString() != "0") {
//                            if (response.body()!!.data[0].Preference.size > 0) {
//
//                                Utils.setStringValue(this@PhoneLoginActivity, AppConstants.isPrefrence, "1")
//
//                                Utils.setStringValue(
//                                    this@PhoneLoginActivity,
//                                    AppConstants.COURSE_TYPE_ID,
//                                    response.body()!!.data[0].Preference[0].CourseTypeID
//                                )
//                                Utils.setStringValue(
//                                    this@PhoneLoginActivity,
//                                    AppConstants.COURSE_ID,
//                                    response.body()!!.data[0].Preference[0].BoardID
//                                )
//                                Utils.setStringValue(
//                                    this@PhoneLoginActivity,
//                                    AppConstants.STANDARD_ID,
//                                    response.body()!!.data[0].Preference[0].StandardID
//                                )
//                                Utils.setStringValue(
//                                    this@PhoneLoginActivity,
//                                    AppConstants.SUBJECT_ID,
//                                    response.body()!!.data[0].Preference[0].SubjectID
//                                )
//                            }
////                        overridePendingTransition(R.anim.slide_in_leftt, R.anim.slide_out_right)
//
//                            Utils.setStringValue(
//                                this@PhoneLoginActivity,
//                                AppConstants.FIRST_NAME,
//                                response.body()!!.data[0].StudentFirstName
//                            )
//                            Utils.setStringValue(
//                                this@PhoneLoginActivity,
//                                AppConstants.LAST_NAME,
//                                response.body()!!.data[0].StudentLastName
//                            )
//                            Utils.setStringValue(
//                                this@PhoneLoginActivity,
//                                AppConstants.USER_ID,
//                                response.body()!!.data[0].StudentID.toString()
//                            )
//                            Utils.setStringValue(
//                                this@PhoneLoginActivity,
//                                AppConstants.USER_EMAIL,
//                                response.body()!!.data[0].StudentEmailAddress
//                            )
//                            Utils.setStringValue(
//                                this@PhoneLoginActivity,
//                                AppConstants.USER_PASSWORD,
//                                response.body()!!.data[0].StudentPassword
//                            )
//                            Utils.setStringValue(
//                                this@PhoneLoginActivity,
//                                AppConstants.USER_MOBILE,
//                                response.body()!!.data[0].StudentMobile
//                            )
//                            Utils.setStringValue(
//                                this@PhoneLoginActivity,
//                                AppConstants.USER_ACCOUNT_TYPE,
//                                response.body()!!.data[0].AccountTypeID.toString()
//                            )
//                            Utils.setStringValue(
//                                this@PhoneLoginActivity,
//                                AppConstants.USER_STATUSID,
//                                response.body()!!.data[0].StatusID.toString()
//                            )
//                        }
//
//                        val intent = Intent(this@PhoneLoginActivity, OtpActivity::class.java)
//                        intent.putExtra("mobile_number", response.body()!!.data[0].StudentMobile)
//                        intent.putExtra("otp", response.body()!!.data[0].OTP)
//                        intent.putExtra("come_from", "signup")
//                        intent.putExtra("student_id", response.body()!!.data[0].StudentID.toString())
//                        intent.putExtra("first_name", response.body()!!.data[0].StudentFirstName)
//                        intent.putExtra("last_name", response.body()!!.data[0].StudentLastName)
//                        intent.putExtra("email", response.body()!!.data[0].StudentEmailAddress)
//                        intent.putExtra("password", response.body()!!.data[0].StudentPassword)
//                        intent.putExtra("account_type", response.body()!!.data[0].AccountTypeID.toString())
//                        startActivity(intent)
//                        finish()
//
//                    } else {
//                        Toast.makeText(
//                            this@PhoneLoginActivity,
//                            response.body()!!.Msg,
//                            Toast.LENGTH_LONG
//                        ).show()
//
////                    Log.d("loginresponse", response.body()!!.asString)
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<VerifyMobileData>, t: Throwable) {
//                // Log error here since request failed
//                Log.e("", t.toString())
//                DialogUtils.dismissDialog()
//            }
//        })
//    }

    override fun onDestroy() {
        super.onDestroy()

        myCompositeDisposable?.clear()
    }
}
