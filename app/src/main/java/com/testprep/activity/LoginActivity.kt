package com.testprep.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.util.Patterns
import android.widget.TextView
import android.widget.Toast
import com.facebook.*
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.gson.JsonObject
import com.testprep.R
import com.testprep.retrofit.WebClient
import com.testprep.retrofit.WebInterface
import com.testprep.utils.AppConstants
import com.testprep.utils.DialogUtils
import com.testprep.utils.Utils
import com.testprep.utils.WebRequests
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*


class LoginActivity : AppCompatActivity() {

    var mGoogleSignInClient: GoogleSignInClient? = null
    var callbackManager: CallbackManager? = null

    private val EMAIL = "email"

    @SuppressLint("PackageManagerGetSignatures")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FacebookSdk.sdkInitialize(applicationContext)

        setContentView(R.layout.activity_login)

        callbackManager = CallbackManager.Factory.create()
        login_btnFb.setReadPermissions(Arrays.asList(EMAIL))

        // Add code to print out the key hash
        try {
            val info = packageManager.getPackageInfo(
                "com.testprep",
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {

            Log.d("KeyHash:", "" + e.printStackTrace())
        } catch (e: NoSuchAlgorithmException) {

            Log.d("KeyHash:", "" + e.printStackTrace())
        }

        login_btnFb.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                setResult(RESULT_OK)
//                Toast.makeText(this@LoginActivity, "Welcome" + loginResult.accessToken.userId, Toast.LENGTH_LONG).show()

                val request: GraphRequest = GraphRequest.newMeRequest(
                    loginResult.accessToken
                ) { jsonObject: JSONObject, graphResponse: GraphResponse ->
                    Log.v("LoginActivity", graphResponse.toString())

                    // Application code
                    val email = jsonObject.getString("email")
                    val name = jsonObject.getString("name")
//                    var last_name = jsonObject.getString("last_name");
//                    var birthday = jsonObject.getString("birthday") // 01/31/1980 format

                    callCheckEmailApi("3", name, "", email, "", "")
//                    callSignupApi("3", name, "", email, "", "")

                }
                val parameters = Bundle()
                parameters.putString("fields", "id,name,email,gender,birthday")
                request.parameters = parameters
                request.executeAsync()

//                Log.d("fbsignin", "signInResult:failed code=" + e.statusCode)
//                finish()
            }

            override fun onCancel() {
                setResult(RESULT_CANCELED)
//                Toast.makeText(this@LoginActivity, "issue", Toast.LENGTH_LONG).show()

                Log.d("fbsignin", "signInResult:failed code=cancel")

                finish()
            }

            override fun onError(e: FacebookException) {
//                Toast.makeText(this@LoginActivity, "error", Toast.LENGTH_LONG).show()

                Log.d("fbsignin", "signInResult:failed code=" + e.printStackTrace())
                // Handle exception
            }
        })

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        login_btnSignup.setOnClickListener {

            val intent = Intent(this@LoginActivity, SignupActivity::class.java)
            intent.putExtra("comefrom", "login")
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)

        }

        setGooglePlusButtonText(login_btnGoogle, "Continue with Google")

        login_btnLogin.setOnClickListener {

            if (isValid()) {
                callLoginApi()
            }
        }

        login_btnGoogle.setOnClickListener { signIn(); }

        login_tvForgotPass.setOnClickListener {

            val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)

        }

    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, 100)
    }

    private fun setGooglePlusButtonText(signInButton: SignInButton, buttonText: String) {
        // Find the TextView that is inside of the SignInButton and set its text
        for (i in 0 until signInButton.childCount) {
            val v = signInButton.getChildAt(i)

            if (v is TextView) {
                v.text = buttonText
                return
            }
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == 100) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        } else {

            callbackManager!!.onActivityResult(requestCode, resultCode, data)
        }
    }


    var account: GoogleSignInAccount? = null
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            account = completedTask.getResult(ApiException::class.java)

            Log.d(
                "googlesigninresponse",
                account!!.givenName.toString() + " " + account!!.familyName.toString() + " " + account!!.email.toString()
            )

            callCheckEmailApi(
                "2",
                account!!.givenName.toString(),
                account!!.familyName.toString(),
                account!!.email.toString(),
                "",
                ""
            )

//            Toast.makeText(this@LoginActivity, "Welcome " + account!!.displayName, Toast.LENGTH_LONG).show()
//            updateUI(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
//            Toast.makeText(this@LoginActivity, "Login fail", Toast.LENGTH_LONG).show()
            Log.w("googlesignin", "signInResult:failed code=" + e.statusCode)
//            updateUI(null)
        }

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

                        Toast.makeText(this@LoginActivity, response.body()!!["Msg"].asString, Toast.LENGTH_LONG).show()

                        val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)

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

    fun callCheckEmailApi(logintype: String, fname: String, lname: String, email: String, pass: String, cpass: String) {

        if (!DialogUtils.isNetworkConnected(this@LoginActivity)) {
            Utils.ping(this@LoginActivity, "Connetion not available")
        }

        DialogUtils.showDialog(this@LoginActivity)

        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.checkEmail(WebRequests.checkEmailParams(email))
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!["Status"].asString == "true") {

                        Toast.makeText(this@LoginActivity, response.body()!!["Msg"].asString, Toast.LENGTH_LONG).show()

                        val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)

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


//                    Log.w("check email response", GsonBuilder().setPrettyPrinting().create().toJson(response))

//                    Log.d("loginresponse", response.body()!!.asString)
                    } else {
                        Toast.makeText(this@LoginActivity, response.body()!!["Msg"].asString, Toast.LENGTH_LONG).show()

                        callSignupApi(
                            logintype,
                            fname,
                            lname,
                            email,
                            pass,
                            cpass
                        )

//                    setPasswordDialog()

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

    fun callSignupApi(accounttype: String, fname: String, lname: String, email: String, pass: String, cpass: String) {

        if (!DialogUtils.isNetworkConnected(this@LoginActivity)) {
            Utils.ping(this@LoginActivity, "Connetion not available")
        }

        DialogUtils.showDialog(this@LoginActivity)

        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call =
            apiService.getSignup(WebRequests.addSignupParams(accounttype, "0", fname, lname, email, pass, cpass, "1"))

        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.get("Status").asString == "true") {

                        Toast.makeText(this@LoginActivity, response.body()!!.get("Msg").asString, Toast.LENGTH_LONG)
                            .show()

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

                        val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                        finish()

                        Log.d("websize", response.body()!!.get("Msg").asString)

                    } else {

                        Toast.makeText(this@LoginActivity, response.body()!!.get("Msg").asString, Toast.LENGTH_LONG)
                            .show()

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

}
