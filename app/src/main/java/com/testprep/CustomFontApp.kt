package com.testprep

import android.app.Application
import com.splunk.mint.Mint
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

class CustomFontApp : Application() {
    override fun onCreate() {
        super.onCreate()

//        TypefaceUtil.overrideFont(applicationContext, "SERIF", "fonts/inter_regular.ttf")
//        Utils.changeStatusbarColor(Activity)

        Mint.initAndStartSession(this, "e460283d")

        CalligraphyConfig.initDefault(
            CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Inter-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        )

    }
}
