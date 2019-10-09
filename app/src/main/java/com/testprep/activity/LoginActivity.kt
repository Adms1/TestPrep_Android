package com.testprep.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import com.google.gson.JsonObject
import com.testprep.R
import com.testprep.retrofit.WebClient
import com.testprep.retrofit.WebInterface
import com.testprep.utils.AppConstants
import com.testprep.utils.DialogUtils
import com.testprep.utils.Utils
import com.testprep.utils.WebRequests
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper


class LoginActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    @SuppressLint("PackageManagerGetSignatures")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setContentView(R.layout.activity_login)

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            login_btnSignup.text = "DON\'T HAVE AN ACCOUNT? SIGN UP"
        }


        login_btnSignup.setOnClickListener {

            val intent = Intent(this@LoginActivity, SignupActivity::class.java)
            intent.putExtra("comefrom", "login")
            startActivity(intent)
//            overridePendingTransition(R.anim.slide_in_leftt, R.anim.slide_out_right)

        }

        login_btnLogin.setOnClickListener {

            if (isValid()) {
                callLoginApi()
            }
        }

        login_tvForgotPass.setOnClickListener {

            val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
            startActivity(intent)
//            overridePendingTransition(R.anim.slide_in_leftt, R.anim.slide_out_right)

        }

        login_etPassword.setOnEditorActionListener(object : TextView.OnEditorActionListener {

            override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
                if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                    if (isValid()) {
                        callLoginApi()
                    }
                }
                return false
            }
        })

    }

    fun callLoginApi() {

        if (!DialogUtils.isNetworkConnected(this@LoginActivity)) {
            Utils.ping(this@LoginActivity, "Connetion not available")
        }

        DialogUtils.showDialog(this@LoginActivity)

        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getLogin(
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

//                        Toast.makeText(this@LoginActivity, response.body()!!["Msg"].asString, Toast.LENGTH_LONG).show()

                        Utils.setStringValue(this@LoginActivity, "is_login", "true")

                        val intent = Intent(this@LoginActivity, NewActivity::class.java)
                        startActivity(intent)
                        finish()
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

//                    Log.d("loginresponse", response.body()!!.asString)
                    } else {
                        Toast.makeText(this@LoginActivity, response.body()!!["Msg"].asString, Toast.LENGTH_LONG).show()

//                    Log.d("loginresponse", response.body()!!.asString)
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

        if (TextUtils.isEmpty(login_etEmail.text.toString()) || !Patterns.EMAIL_ADDRESS.matcher(login_etEmail.text.toString()).matches()) {
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

    }

}
