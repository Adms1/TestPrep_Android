package com.testcraft.testcraft.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.phone.SmsRetriever
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
import java.util.regex.Matcher
import java.util.regex.Pattern

class PhoneLoginActivity : AppCompatActivity() {

    private var myCompositeDisposable: CompositeDisposable? = null
    private var TAG = "PhoneLoginActivity"

    private val REQ_USER_CONSENT = 200
    var smsBroadcastReceiver: SmsBroadcastReceiver? = null

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

            startSmsUserConsent()

        } else {

            Toast.makeText(
                this@PhoneLoginActivity,
                myData.Msg,
                Toast.LENGTH_LONG
            ).show()

        }

    }

    private fun startSmsUserConsent() {
        val client = SmsRetriever.getClient(this)
        //We can add sender phone number or leave it blank
        // I'm adding null here
        client.startSmsUserConsent(null)
            .addOnSuccessListener {
//                Toast.makeText(applicationContext, "On Success", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
//                Toast.makeText(applicationContext, "On OnFailure", Toast.LENGTH_LONG).show()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_USER_CONSENT) {
            if (resultCode == RESULT_OK && data != null) {
                //That gives all message to us.
                // We need to get the code from inside with regex
                val message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
//                Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
//                otp_etOtp.value = String.format("%s - %s", getString(R.string.received_message), message)
                getOtpFromMessage(message)
            }
        }
    }

    private fun getOtpFromMessage(message: String) {
        // This will match any 6 digit number in the message
        val pattern: Pattern = Pattern.compile("(|^)\\d{6}")
        val matcher: Matcher = pattern.matcher(message)
        if (matcher.find()) {
            otp_etOtp.value = matcher.group(0)
        }
    }

    private fun registerBroadcastReceiver() {
        smsBroadcastReceiver = SmsBroadcastReceiver()
        smsBroadcastReceiver!!.smsBroadcastReceiverListener =
            object : SmsBroadcastReceiver.SmsBroadcastReceiverListener {
                override fun onSuccess(intent: Intent?) {
                    startActivityForResult(intent, REQ_USER_CONSENT)
                }

                override fun onFailure() {}
            }
        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        registerReceiver(smsBroadcastReceiver, intentFilter)
    }

    override fun onStart() {
        super.onStart()
        registerBroadcastReceiver()
    }

    private fun onFailure(t: Throwable) {
//                // Log error here since request failed
        Log.e("", t.toString())
        DialogUtils.dismissDialog()
    }

    override fun onDestroy() {
        super.onDestroy()

        myCompositeDisposable?.clear()
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(smsBroadcastReceiver)
    }

}
