package com.testcraft.testcraft

import android.app.Application
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.util.Log
import com.splunk.mint.Mint
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
