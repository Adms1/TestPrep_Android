package com.testcraft.testcraft.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import com.testcraft.testcraft.Connectivity
import com.testcraft.testcraft.R
import com.testcraft.testcraft.retrofit.WebClient
import com.testcraft.testcraft.utils.*
import com.testcraft.testcraft.utils.CommonWebCalls.Companion.callFCMToken
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    var connectivity: Connectivity? = null

//    override fun onResume() {
//        super.onResume()
//        val filter = IntentFilter()
//        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
//        registerReceiver(connectivity, filter)
//    }
//
//    override fun onStop() {
//        super.onStop()
//        unregisterReceiver(connectivity)
//    }

    @SuppressLint("PackageManagerGetSignatures", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        setContentView(R.layout.activity_login)

//        connectivity = Connectivity()

        CommonWebCalls.callToken(
            this@LoginActivity,
            "1",
            "",
            ActionIdData.C3800,
            ActionIdData.T3800
        )

        login_btnSignup.setOnClickListener {

            CommonWebCalls.callToken(
                this@LoginActivity,
                "1",
                "",
                ActionIdData.C3802,
                ActionIdData.T3802
            )

            val intent = Intent(this@LoginActivity, PhoneLoginActivity::class.java)
            intent.putExtra("comefrom", "login")
            startActivity(intent)
            finish()
//            overridePendingTransition(R.anim.slide_in_leftt, R.anim.slide_out_right)

        }

        login_btnLogin.setOnClickListener {

            CommonWebCalls.callToken(
                this@LoginActivity,
                "1",
                "",
                ActionIdData.C3803,
                ActionIdData.T3803
            )

            if (isValid()) {
                callLoginApi()
            }
        }

        login_tvForgotPass.setOnClickListener {

            CommonWebCalls.callToken(
                this@LoginActivity,
                "1",
                "",
                ActionIdData.C3801,
                ActionIdData.T3801
            )

            val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
            startActivity(intent)
//            overridePendingTransition(R.anim.slide_in_leftt, R.anim.slide_out_right)

        }

        login_etPassword.setOnEditorActionListener { v, actionId, event ->
            if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {

                CommonWebCalls.callToken(
                    this@LoginActivity,
                    "1",
                    "",
                    ActionIdData.C3803,
                    ActionIdData.T3803
                )

                if (isValid()) {
                    callLoginApi()
                }
            }
            false
        }

    }

    fun callLoginApi() {

        if (!DialogUtils.isNetworkConnected(this@LoginActivity)) {
//            Utils.ping(activity!!, AppConstants.NETWORK_MSG)
            DialogUtils.NetworkDialog(this@LoginActivity)
            DialogUtils.dismissDialog()
        }

        DialogUtils.showDialog(this@LoginActivity)

        val call = WebClient.buildService().getLogin(
            WebRequests.addLoginParams(
                login_etEmail.text.toString(),
                login_etPassword.text.toString()
            )
        )

        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!["Status"].asString == "true") {

                        Utils.setStringValue(this@LoginActivity, AppConstants.IS_LOGIN, "true")

                        if (response.body()!!["data"].asJsonArray[0].asJsonObject["Preference"].asJsonArray.size() > 0) {

                            Utils.setStringValue(this@LoginActivity, AppConstants.isPrefrence, "1")

                            Utils.setStringValue(
                                this@LoginActivity,
                                AppConstants.COURSE_TYPE_ID,
                                response.body()!!["data"].asJsonArray[0].asJsonObject["Preference"].asJsonArray[0].asJsonObject["CourseTypeID"].asString
                            )
                            Utils.setStringValue(
                                this@LoginActivity,
                                AppConstants.COURSE_ID,
                                response.body()!!["data"].asJsonArray[0].asJsonObject["Preference"].asJsonArray[0].asJsonObject["BoardID"].asString
                            )
                            Utils.setStringValue(
                                this@LoginActivity,
                                AppConstants.STANDARD_ID,
                                response.body()!!["data"].asJsonArray[0].asJsonObject["Preference"].asJsonArray[0].asJsonObject["StandardID"].asString
                            )
                            Utils.setStringValue(
                                this@LoginActivity,
                                AppConstants.SUBJECT_ID,
                                response.body()!!["data"].asJsonArray[0].asJsonObject["Preference"].asJsonArray[0].asJsonObject["SubjectID"].asString
                            )

                            AppConstants.isFirst = 0

                            val mIntent = Intent(this@LoginActivity, DashboardActivity::class.java)
                            mIntent.putExtra("subject_id", "")
                            startActivity(mIntent)
                            finish()

                        } else {
                            val intent = Intent(this@LoginActivity, NewActivity::class.java)
                            startActivity(intent)
                            finish()
                        }

//                        overridePendingTransition(R.anim.slide_in_leftt, R.anim.slide_out_right)

                        Utils.setStringValue(
                            this@LoginActivity,
                            AppConstants.FIRST_NAME,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StudentFirstName"].asString
                        )
                        Utils.setStringValue(
                            this@LoginActivity,
                            AppConstants.LAST_NAME,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StudentLastName"].asString
                        )
                        Utils.setStringValue(
                            this@LoginActivity,
                            AppConstants.USER_ID,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StudentID"].asString
                        )
                        Utils.setStringValue(
                            this@LoginActivity,
                            AppConstants.USER_EMAIL,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StudentEmailAddress"].asString
                        )
                        Utils.setStringValue(
                            this@LoginActivity,
                            AppConstants.USER_PASSWORD,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StudentPassword"].asString
                        )
                        Utils.setStringValue(
                            this@LoginActivity,
                            AppConstants.USER_MOBILE,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StudentMobile"].asString
                        )
                        Utils.setStringValue(
                            this@LoginActivity,
                            AppConstants.USER_ACCOUNT_TYPE,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["AccountTypeID"].asString
                        )
                        Utils.setStringValue(
                            this@LoginActivity,
                            AppConstants.USER_STATUSID,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StatusID"].asString
                        )

                        callFCMToken(this@LoginActivity)

                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            response.body()!!["Msg"].asString,
                            Toast.LENGTH_LONG
                        ).show()

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

        if (TextUtils.isEmpty(login_etEmail.text.toString()) || !Patterns.EMAIL_ADDRESS.matcher(
                login_etEmail.text.toString()
            ).matches()
        ) {
            login_etEmail.error = "Please enter valid Email Address"
            isvalid = false
        }

        if (TextUtils.isEmpty(login_etPassword.text.toString())) {
            login_etPassword.error = "Please enter Password"
            isvalid = false
        }

        return isvalid

    }

//    private fun setPasswordDialog() {
//
//
//
//        val dialog = Dialog(this@LoginActivity)
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog.setContentView(R.layout.dialog_setpassword)
//        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//
//        val window = dialog.window
//        val wlp: WindowManager.LayoutParams?
//        if (window != null) {
//            wlp = window.attributes
//
//            wlp!!.gravity = Gravity.CENTER
//            window.attributes = wlp
//
//        }
//
//        val btnClose: Button = dialog.findViewById(R.id.dialog_setpass_close_btn)
//        val etPassword: EditText = dialog.findViewById(R.id.dialog_setpass_etPassword)
//        val etCpassword: EditText = dialog.findViewById(R.id.dialog_setpass_etCPassword)
//        val btnSubmit: Button = dialog.findViewById(R.id.dialog_setpass_btnSignup)
//
//        btnSubmit.setOnClickListener {
//
//            callSignupApi(account!!.givenName.toString(), account!!.familyName.toString(), account!!.email.toString(), etPassword.text.toString(), etCpassword.text.toString())
//        }
//
//        btnClose.setOnClickListener { dialog.dismiss() }
//
//        dialog.show()
//    }

    override fun onBackPressed() {

        val intent = Intent(this@LoginActivity, IntroActivity::class.java)
        startActivity(intent)
        finish()

    }

}
