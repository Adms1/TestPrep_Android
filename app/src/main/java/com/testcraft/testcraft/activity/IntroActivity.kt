package com.testcraft.testcraft.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.facebook.*
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.gson.JsonObject
import com.testcraft.testcraft.PhoneLoginActivity
import com.testcraft.testcraft.R
import com.testcraft.testcraft.retrofit.WebClient
import com.testcraft.testcraft.retrofit.WebInterface
import com.testcraft.testcraft.utils.*
import com.testcraft.testcraft.utils.CommonWebCalls.Companion.callFCMToken
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_intro.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.system.exitProcess

class IntroActivity : AppCompatActivity() {

    private val EMAIL = "email"
    var callbackManager: CallbackManager? = null
    var mGoogleSignInClient: GoogleSignInClient? = null

    private var myViewPagerAdapter: MyViewPagerAdapter? = null
    //    private var dots: Array<TextView>? = null
    private var layouts: IntArray? = null

    /**
     * This function assumes logger is an instance of AppEventsLogger and has been
     * created using AppEventsLogger.newLogger() call.
     */
    fun logSentFriendRequestEvent() {

        AppEventsLogger.newLogger(this@IntroActivity).logEvent("sentFriendRequest")
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

//    var connectivity: Connectivity? = null
//
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

    @SuppressLint("SetTextI18n", "PackageManagerGetSignatures")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

//        connectivity = Connectivity()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        FacebookSdk.sdkInitialize(applicationContext)

        setContentView(R.layout.activity_intro)

        Log.d(
            "default acid intro",
            Utils.getTokenPref(this@IntroActivity, AppConstants.DEFAULT_ACTION_ID, "")!!
        )

        CommonWebCalls.callToken(this@IntroActivity, "1", "", ActionIdData.C200, ActionIdData.T200)

        callbackManager = CallbackManager.Factory.create()
        fb.setReadPermissions(listOf(EMAIL))
//        loginManager.logInWithReadPermissions(this@IntroActivity, listOf(EMAIL));

        layouts = intArrayOf(R.drawable.slider_logo, R.drawable.slider_logo, R.drawable.slider_logo)

//        addBottomDots(0)

        myViewPagerAdapter = MyViewPagerAdapter()
//        intro_view_pager.adapter = myViewPagerAdapter
//        intro_view_pager.addOnPageChangeListener(introViewPagerListener)

        // Add code to print out the key hash
        try {

            val info = packageManager.getPackageInfo(
                "com.testcraft.testcraft", PackageManager.GET_SIGNATURES)

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

//        1019001765891-m5260dl112gco2g1f6j0bsrajmvdq8uo.apps.googleusercontent.com
//        "428229802198-ctp9vqa11slhi8domor8f9u1lsvvabbk.apps.googleusercontent.com"
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("1019001765891-v83oi9qhvf3n623mdv4sri6s4bm3id21.apps.googleusercontent.com")
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

//        setGooglePlusButtonText(intro_btnGoogle, "Continue with Google")

        intro_btnGoogle.setOnClickListener {

            CommonWebCalls.callToken(
                this@IntroActivity,
                "1",
                "",
                ActionIdData.C202,
                ActionIdData.T202
            )
            signIn()
        }

        intro_tvSignin.setOnClickListener {

            CommonWebCalls.callToken(
                this@IntroActivity,
                "1",
                "",
                ActionIdData.C204,
                ActionIdData.T204
            )

            val i = Intent(this@IntroActivity, LoginActivity::class.java)
            startActivity(i)
            finish()
        }

        intro_btnEmail.setOnClickListener {

            CommonWebCalls.callToken(
                this@IntroActivity,
                "1",
                "",
                ActionIdData.C203,
                ActionIdData.T203
            )

            val intent = Intent(this@IntroActivity, PhoneLoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        fb.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                setResult(RESULT_OK)

                val request: GraphRequest = GraphRequest.newMeRequest(
                    loginResult.accessToken
                ) { jsonObject: JSONObject, graphResponse: GraphResponse ->
                    Log.d("IntroActivity", graphResponse.toString())

                    // Application code
                    val userID = jsonObject.get("id")
                    val email = jsonObject.getString("email")
                    val fname = jsonObject.getString("first_name")
                    val lname = jsonObject.getString("last_name")

                    Utils.setStringValue(
                        this@IntroActivity,
                        AppConstants.user_profile,
                        "https://graph.facebook.com/$userID/picture?type=large"
                    )

//                    var last_name = jsonObject.getString("last_name");
//                    var birthday = jsonObject.getString("birthday") // 01/31/1980 format

                    if(email != "") {
                        callCheckEmailApi("3", fname, lname, email, "", "")
                    }else{
                        Utils.ping(this@IntroActivity, "Email Not Found.!! Please add email in your FB Account")
                    }
//                    callSignupApi("3", name, "", email, "", "")

//                    if (AccessToken.getCurrentAccessToken() != null) {
//                        LoginManager.getInstance().logOut()
//                    }
                }

                val parameters = Bundle()
                parameters.putString("fields", "id,name,first_name,last_name,email,gender,birthday,picture")
                request.parameters = parameters
                request.executeAsync()

//                Log.d("fbsignin", "signInResult:failed code=" + e.statusCode)
//                finish()
            }

            override fun onCancel() {
                setResult(RESULT_CANCELED)

                Log.d("fbsignin", "signInResult:failed code=cancel")

                finish()
            }

            override fun onError(e: FacebookException) {

                Log.d("fbsignin", "signInResult:failed code=" + e.printStackTrace())
                // Handle exception
            }
        })
    }

//    private fun addBottomDots(currentPage: Int) {
//        var arraySize = layouts!!.size
//        dots = Array(arraySize) { textboxInit() }
//        val colorsActive = resources.getIntArray(R.array.array_dot_active)
//        val colorsInactive = resources.getIntArray(R.array.array_dot_inactive)
//        layoutDots.removeAllViews()
//        for (i in 0 until dots!!.size) {
//            dots!![i] = TextView(this)
//            dots!![i].text = Html.fromHtml("&#9679;")
//            dots!![i].textSize = 10F
//            dots!![i].setPadding(10, 0, 10, 0)
//            dots!![i].setTextColor(colorsInactive[currentPage])
//            layoutDots.addView(dots!![i])
//        }
//        if (dots!!.isNotEmpty())
//            dots!![currentPage].setTextColor(colorsActive[currentPage])
//    }

//    private fun getItem(i: Int): Int {
//        return intro_view_pager.currentItem + i
//    }

    private fun textboxInit(): TextView {
        return TextView(applicationContext)
    }

    private var introViewPagerListener: ViewPager.OnPageChangeListener =
        object : ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) {
//            addBottomDots(position)
                /*Based on the page position change the button text*/

            }

            override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {
                //Do nothing for now
            }

            override fun onPageScrollStateChanged(arg0: Int) {
                //Do nothing for now
            }
        }

    inner class MyViewPagerAdapter : PagerAdapter() {
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val layoutInflater: LayoutInflater = LayoutInflater.from(applicationContext)
            val view = layoutInflater.inflate(R.layout.slider_item_layout, container, false)

            val iv: ImageView = view.findViewById(R.id.imageView)

            iv.setImageResource(layouts!![position])

            container.addView(view)
            return view
        }

        override fun getCount(): Int {
            return layouts!!.size
        }

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view === obj
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val view = `object` as View
            container.removeView(view)
        }
    }

    fun onClick(v: View) {

        CommonWebCalls.callToken(this@IntroActivity, "1", "", ActionIdData.C201, ActionIdData.T201)

        if (v == intro_btnFb) {

            if (!DialogUtils.isNetworkConnected(this@IntroActivity)) {
                Utils.ping(this@IntroActivity, AppConstants.NETWORK_MSG)

            } else {

                fb.performClick()
            }

        }
    }

    private fun signIn() {

        if (!DialogUtils.isNetworkConnected(this@IntroActivity)) {
            Utils.ping(this@IntroActivity, AppConstants.NETWORK_MSG)

        } else {

            val signInIntent = mGoogleSignInClient!!.signInIntent
            startActivityForResult(signInIntent, 100)
        }
    }

//    private fun setGooglePlusButtonText(signInButton: Button, buttonText: String) {
//        // Find the TextView that is inside of the SignInButton and set its text
//        for (i in 0 until signInButton.childCount) {
//            val v = signInButton.getChildAt(i)
//
//            if (v is TextView) {
//                v.text = buttonText
//                return
//            }
//        }
//    }

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

            Utils.setStringValue(
                this@IntroActivity,
                AppConstants.user_profile,
                account!!.photoUrl.toString()
            )

            callCheckEmailApi(
                "2",
                account!!.givenName.toString(),
                account!!.familyName.toString(),
                account!!.email.toString(),
                "",
                ""
            )

//            Toast.makeText(this@IntroActivity, "Welcome " + account!!.displayName, Toast.LENGTH_LONG).show()
//            updateUI(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
//            Toast.makeText(this@IntroActivity, "Login fail", Toast.LENGTH_LONG).show()
            Log.w("googlesignin", "signInResult:failed code=" + e.statusCode)
//            updateUI(null)
        }
    }

    fun callCheckEmailApi(
        logintype: String,
        fname: String,
        lname: String,
        email: String,
        pass: String,
        phone: String
    ) {

        if (!DialogUtils.isNetworkConnected(this@IntroActivity)) {
            Utils.ping(this@IntroActivity, AppConstants.NETWORK_MSG)
        }

        DialogUtils.showDialog(this@IntroActivity)

        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.checkEmail(WebRequests.checkEmailParams(email))

        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!["Status"].asString == "true") {

                        Utils.setStringValue(this@IntroActivity, AppConstants.IS_LOGIN, "true")

                        if(email != response.body()!!["data"].asJsonArray[0].asJsonObject["StudentEmailAddress"].asString){

                            AppConstants.isFirst = 1
                            val intent = Intent(this@IntroActivity, DashboardActivity::class.java)
                            startActivity(intent)

                        }else {

                            if (Utils.getStringValue(this@IntroActivity, AppConstants.APP_MODE, "") == AppConstants.DEEPLINK_MODE || Utils.getStringValue(this@IntroActivity, AppConstants.APP_MODE, "") == AppConstants.GUEST_MODE) {

                                Utils.setStringValue(this@IntroActivity, AppConstants.APP_MODE, AppConstants.NORMAL_MODE)

                                if (Utils.getStringValue(this@IntroActivity, AppConstants.IS_DEEPLINK_STEP, "") == "2" || Utils.getStringValue(this@IntroActivity, AppConstants.IS_DEEPLINK_STEP, "") == "3") {

                                    finish()

                                } else {
                                    if (response.body()!!["data"].asJsonArray[0].asJsonObject["Preference"].asJsonArray.size() > 0) {

                                        AppConstants.isFirst = 0

                                        val mIntent = Intent(this@IntroActivity, DashboardActivity::class.java)
                                        mIntent.putExtra("subject_id", "")
                                        startActivity(mIntent)
                                        finish()

                                    } else {
                                        val intent = Intent(this@IntroActivity, NewActivity::class.java)
                                        startActivity(intent)
                                    }
                                }
                            } else {

                                if (response.body()!!["data"].asJsonArray[0].asJsonObject["Preference"].asJsonArray.size() > 0) {

                                    val mIntent = Intent(this@IntroActivity, DashboardActivity::class.java)
                                    mIntent.putExtra("subject_id", "")
                                    startActivity(mIntent)
                                    finish()

                                } else {

                                    val intent = Intent(this@IntroActivity, NewActivity::class.java)
                                    startActivity(intent)
                                }
                            }
                        }

                        if (response.body()!!["data"].asJsonArray[0].asJsonObject["Preference"].asJsonArray.size() > 0) {

                            Utils.setStringValue(this@IntroActivity, AppConstants.isPrefrence, "1")

                            Utils.setStringValue(
                                this@IntroActivity,
                                AppConstants.COURSE_TYPE_ID,
                                response.body()!!["data"].asJsonArray[0].asJsonObject["Preference"].asJsonArray[0].asJsonObject["CourseTypeID"].asString
                            )
                            Utils.setStringValue(
                                this@IntroActivity,
                                AppConstants.COURSE_ID,
                                response.body()!!["data"].asJsonArray[0].asJsonObject["Preference"].asJsonArray[0].asJsonObject["BoardID"].asString
                            )
                            Utils.setStringValue(
                                this@IntroActivity,
                                AppConstants.STANDARD_ID,
                                response.body()!!["data"].asJsonArray[0].asJsonObject["Preference"].asJsonArray[0].asJsonObject["StandardID"].asString
                            )
                            Utils.setStringValue(
                                this@IntroActivity,
                                AppConstants.SUBJECT_ID,
                                response.body()!!["data"].asJsonArray[0].asJsonObject["Preference"].asJsonArray[0].asJsonObject["SubjectID"].asString
                            )
                        }
//                        overridePendingTransition(R.anim.slide_in_leftt, R.anim.slide_out_right)

                        Utils.setStringValue(
                            this@IntroActivity,
                            AppConstants.FIRST_NAME,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StudentFirstName"].asString
                        )
                        Utils.setStringValue(
                            this@IntroActivity,
                            AppConstants.LAST_NAME,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StudentLastName"].asString
                        )
                        Utils.setStringValue(
                            this@IntroActivity,
                            AppConstants.USER_ID,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StudentID"].asString
                        )
                        Utils.setStringValue(
                            this@IntroActivity,
                            AppConstants.USER_EMAIL,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StudentEmailAddress"].asString
                        )
                        Utils.setStringValue(
                            this@IntroActivity,
                            AppConstants.USER_PASSWORD,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StudentPassword"].asString
                        )
                        Utils.setStringValue(
                            this@IntroActivity,
                            AppConstants.USER_MOBILE,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StudentMobile"].asString
                        )
                        Utils.setStringValue(
                            this@IntroActivity,
                            AppConstants.USER_ACCOUNT_TYPE,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["AccountTypeID"].asString
                        )
                        Utils.setStringValue(
                            this@IntroActivity,
                            AppConstants.USER_STATUSID,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StatusID"].asString
                        )

//                        LoginManager.getInstance().logOut()
                        callFCMToken(this@IntroActivity)


                    } else {

                        if (Utils.getStringValue(this@IntroActivity, AppConstants.APP_MODE, "") == AppConstants.DEEPLINK_MODE) {

                            CommonWebCalls.callSignupApi("intro", this@IntroActivity, logintype, Utils.getStringValue(this@IntroActivity, AppConstants.USER_ID, "")!!, fname, lname, email, pass, phone)

                        } else {

                            Utils.setStringValue(this@IntroActivity, AppConstants.APP_MODE, AppConstants.NORMAL_MODE)
                            CommonWebCalls.callSignupApi("intro", this@IntroActivity, logintype, "0", fname, lname, email, pass, phone)

                        }

//                        callSignupApi(
//                            logintype,
//                            fname,
//                            lname,
//                            email,
//                            pass,
//                            phone
//                        )

//                    setPasswordDialog()

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

    fun callSignupApi(
        accounttype: String,
        fname: String,
        lname: String,
        email: String,
        pass: String,
        phone: String
    ) {

        if (!DialogUtils.isNetworkConnected(this@IntroActivity)) {
            Utils.ping(this@IntroActivity, AppConstants.NETWORK_MSG)
        }

        DialogUtils.showDialog(this@IntroActivity)

        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call =
            apiService.getSignup(
                WebRequests.addSignupParams(
                    accounttype,
                    "0",
                    fname,
                    lname,
                    email,
                    pass,
                    phone, Utils.getDeviceId(this@IntroActivity))
            )

        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.get("Status").asString == "true") {

                        Utils.setStringValue(
                            this@IntroActivity,
                            AppConstants.FIRST_NAME,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StudentFirstName"].asString
                        )
                        Utils.setStringValue(
                            this@IntroActivity,
                            AppConstants.LAST_NAME,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StudentLastName"].asString
                        )
                        Utils.setStringValue(
                            this@IntroActivity,
                            AppConstants.USER_ID,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StudentID"].asString
                        )
                        Utils.setStringValue(
                            this@IntroActivity,
                            AppConstants.USER_EMAIL,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StudentEmailAddress"].asString
                        )
                        Utils.setStringValue(
                            this@IntroActivity,
                            AppConstants.USER_PASSWORD,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StudentPassword"].asString
                        )
                        Utils.setStringValue(
                            this@IntroActivity,
                            AppConstants.USER_MOBILE,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StudentMobile"].asString
                        )
                        Utils.setStringValue(
                            this@IntroActivity,
                            AppConstants.USER_ACCOUNT_TYPE,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["AccountTypeID"].asString
                        )
                        Utils.setStringValue(
                            this@IntroActivity,
                            AppConstants.USER_STATUSID,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StatusID"].asString
                        )

                        val intent = Intent(this@IntroActivity, NewActivity::class.java)
                        startActivity(intent)
//                        overridePendingTransition(R.anim.slide_in_leftt, R.anim.slide_out_right)
                        finish()

                        Log.d("websize", response.body()!!.get("Msg").asString)
//                        LoginManager.getInstance().logOut()
                    } else {

                        Toast.makeText(
                            this@IntroActivity,
                            response.body()!!.get("Msg").asString,
                            Toast.LENGTH_LONG
                        )
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

    override fun onBackPressed() {

        if (Utils.getStringValue(this@IntroActivity, AppConstants.APP_MODE, "") == AppConstants.DEEPLINK_MODE) {

            if (Utils.getStringValue(this@IntroActivity, AppConstants.IS_DEEPLINK_STEP, "") == "3") {
                super.onBackPressed()
            } else {
                exitProcess(0)
            }
        }

    }

    companion object {

        private var loginManager: LoginManager = LoginManager.getInstance()
        var context: Context? = null

        fun disconnectFromFacebook() {

//            if (Utils.getStringValue(context!!, AppConstants.FB_ACCESS_TOKEN, "") == "") {
//                return // already logged out
//                }

//            LoginManager.getInstance().setLoginBehavior(LoginBehavior.WEB_ONLY)

            loginManager.logOut()

            GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/permissions/",
                null,
                HttpMethod.DELETE,
                GraphRequest
                    .Callback {

                        loginManager.logOut()

                    })
        }
    }

}
