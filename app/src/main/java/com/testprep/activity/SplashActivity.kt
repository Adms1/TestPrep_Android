package com.testprep.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import com.testprep.R
import com.testprep.utils.Utils

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        changeStatusbarColor(this@SplashActivity)

        // In Activity's onCreate() for instance
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setContentView(R.layout.activity_splash)

        Handler().postDelayed(
            /* Runnable
                 * Showing splash screen with a timer. This will be useful when you
                 * want to show case your app logo / company
                 */

            {
                if (Utils.getStringValue(this@SplashActivity, "is_login", "") == "true") {
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    val i = Intent(this@SplashActivity, NewActivity::class.java)
                    startActivity(i)

                } else {
                    val i = Intent(this@SplashActivity, IntroActivity::class.java)
                    startActivity(i)

                }

                finish()

            }, 3000
        )

    }

}

