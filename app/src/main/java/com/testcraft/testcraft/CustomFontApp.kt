package com.testcraft.testcraft

import android.app.Application
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.util.Log
import androidx.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import com.splunk.mint.Mint
import com.testcraft.testcraft.utils.AppConstants
import com.testcraft.testcraft.utils.Utils
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

class CustomFontApp : Application() {

    private var connectivityReceiver: Connectivity? = null

    private fun getConnectivityReceiver(): Connectivity? {
        if (connectivityReceiver == null) connectivityReceiver = Connectivity()
        return connectivityReceiver
    }

    override fun onCreate() {
        super.onCreate()

//        TypefaceUtil.overrideFont(applicationContext, "SERIF", "fonts/inter_regular.ttf")
//        Utils.changeStatusbarColor(Activity)

        Mint.initAndStartSession(this, "e460283d")

        registerConnectivityReceiver()

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(object : OnCompleteListener<InstanceIdResult?> {
                override fun onComplete(@NonNull task: Task<InstanceIdResult?>) {
                    if (!task.isSuccessful) {
                        Log.w("splash notification", "getInstanceId failed", task.exception)
                        return
                    }

                    // Get new Instance ID token
                    val token: String = task.result!!.token

                    // Log and toast
//                    val msg = "splash notification - $token"

                    Utils.setStringValue(this@CustomFontApp, AppConstants.FCM_TOKEN, token)

                    Log.d("fcm token", token)
//                    Toast.makeText(this@SplashActivity, msg, Toast.LENGTH_SHORT).show()
                }
            })

        CalligraphyConfig.initDefault(
            CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Inter-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        )

    }

    // register here your filtters
    private fun registerConnectivityReceiver() {
        try { // if (android.os.Build.VERSION.SDK_INT >= 26) {
            val filter = IntentFilter()
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
            //filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
//filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
//filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
            registerReceiver(getConnectivityReceiver(), filter)
        } catch (e: Exception) {
            Log.e("connection12233", e.message)
        }
    }

}
