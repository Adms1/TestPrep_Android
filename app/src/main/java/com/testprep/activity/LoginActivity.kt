package com.testprep.activity

import android.app.Dialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.facebook.FacebookSdk
import com.testprep.R
import kotlinx.android.synthetic.main.activity_login.*
import android.widget.TextView
import com.google.android.gms.common.SignInButton
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.android.gms.common.util.IOUtils.toByteArray
import android.provider.SyncStateContract.Helpers.update
import android.content.pm.PackageManager
import android.content.pm.PackageInfo
import android.opengl.ETC1.isValid
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.support.v4.content.ContextCompat.startActivity
import android.text.TextUtils
import android.util.Base64
import android.util.Patterns
import android.view.Gravity
import android.view.Window
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.gson.JsonObject
import com.testprep.old.retrofit.WebClient
import com.testprep.old.retrofit.WebInterface
import com.testprep.utils.AppConstants
import com.testprep.utils.Utils
import com.testprep.utils.WebRequests
import kotlinx.android.synthetic.main.activity_signup.*
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FacebookSdk.sdkInitialize(applicationContext)

        setContentView(R.layout.activity_login)

        callbackManager = CallbackManager.Factory.create()
        login_btnFb.setReadPermissions(Arrays.asList(EMAIL));

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
                    setResult(RESULT_OK);
                    Toast.makeText(this@LoginActivity, "Welcome", Toast.LENGTH_LONG).show()

//                Log.d("fbsignin", "signInResult:failed code=" + e.statusCode)
                    finish();
                }

                override fun onCancel() {
                    setResult(RESULT_CANCELED);
                    Toast.makeText(this@LoginActivity, "issue", Toast.LENGTH_LONG).show()

                    Log.d("fbsignin", "signInResult:failed code=cancel")

                    finish()
                }

                override fun onError(e: FacebookException) {
                    Toast.makeText(this@LoginActivity, "error", Toast.LENGTH_LONG).show()

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
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)

        }

        setGooglePlusButtonText(login_btnGoogle, "Continue with Google")

        login_btnLogin.setOnClickListener {

            if(isValid()) {
                callLoginApi()
            }
        }

        login_btnGoogle.setOnClickListener {   signIn(); }

//        login_btnFb.setOnClickListener {    }

    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, 100)
    }

    protected fun setGooglePlusButtonText(signInButton: SignInButton, buttonText: String) {
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
//        if (requestCode == 100) {
//            // The Task returned from this call is always completed, no need to attach
//            // a listener.
//            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//            handleSignInResult(task)
//        }
//        else{

            callbackManager!!.onActivityResult(requestCode, resultCode, data)
//        }
    }

    var account: GoogleSignInAccount? = null
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            account = completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.

//            if(Utils.callSignupApi(
//                this@LoginActivity,
//                "0",
//                account!!.givenName.toString(),
//                account.familyName.toString(),
//                account.email.toString(),
//                "",
//                "",
//                "1"
//            )){
                val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                finish()
//            }

            Toast.makeText(this@LoginActivity, "Welcome " + account!!.displayName, Toast.LENGTH_LONG).show()
//            updateUI(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Toast.makeText(this@LoginActivity, "Login fail", Toast.LENGTH_LONG).show()
            Log.w("googlesignin", "signInResult:failed code=" + e.statusCode)
//            updateUI(null)
        }

    }

    fun callLoginApi() {

        val sortDialog = Dialog(this@LoginActivity)//,R.style.PauseDialog);//, R.style.PauseDialog);
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

        val call = apiService.getLogin(WebRequests.addLoginParams(login_etEmail.text.toString(), login_etPassword.text.toString()))
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.body()!!["Status"].asString == "true") {

                    sortDialog.dismiss()
                    Toast.makeText(this@LoginActivity, response.body()!!["Msg"].asString, Toast.LENGTH_LONG).show()

                    val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)

                    Utils.setStringValue(this@LoginActivity, AppConstants.USER_NAME, response.body()!!["data"].asJsonArray[0].asJsonObject["StudentFirstName"].asString + " " + response.body()!!["data"].asJsonArray[0].asJsonObject["StudentLastName"].asString)

//                    Log.d("loginresponse", response.body()!!.asString)
                } else {
                    sortDialog.dismiss()
                    Toast.makeText(this@LoginActivity, response.body()!!["Msg"].asString, Toast.LENGTH_LONG).show()

//                    Log.d("loginresponse", response.body()!!.asString)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // Log error here since request failed
                Log.e("", t.toString())
                sortDialog.dismiss()
            }
        })
    }

    fun isValid(): Boolean{

        var isvalid = true

//        if(TextUtils.isEmpty(login_etEmail.text.toString())){
//            login_etEmail.error = "Email Address must not be null"
//            isvalid = false
//        }

        if(TextUtils.isEmpty(login_etEmail.text.toString()) || !Patterns.EMAIL_ADDRESS.matcher(login_etEmail.text.toString()).matches()){
            login_etEmail.error = "Please enter valid Email Address"
            isvalid = false
        }

        if(TextUtils.isEmpty(login_etPassword.text.toString())){
            login_etPassword.error = "Password must not be null"
            isvalid = false
        }

        return isvalid

    }

}
