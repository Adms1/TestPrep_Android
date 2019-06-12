package com.testprep

import android.app.Application
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

class CustomFontApp : Application() {
    override fun onCreate() {
        super.onCreate()

//        TypefaceUtil.overrideFont(applicationContext, "SERIF", "fonts/Inter-Regular.ttf")
//        Utils.changeStatusbarColor(Activity)

        CalligraphyConfig.initDefault(
            CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Inter-Regular.ttf")
//                .setFontAttrId(R.attr.fontPath)
                .build()
        )

    }
}
