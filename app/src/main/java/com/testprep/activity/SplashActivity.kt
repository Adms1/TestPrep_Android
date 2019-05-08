package com.testprep.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.testprep.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed(
            /* Runnable
                 * Showing splash screen with a timer. This will be useful when you
                 * want to show case your app logo / company
                 */

            {
                // This method will be executed once the timer is over
                // Start your app main activity
                val i = Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(i)

                // close this activity
                finish()
            }, 3000
        )

    }
}
