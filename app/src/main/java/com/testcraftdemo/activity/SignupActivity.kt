package com.testcraftdemo.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.testcraftdemo.R
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

       signup_btnSignup.setOnClickListener {

            val intent = Intent(this@SignupActivity, OtpActivity::class.java)
            startActivity(intent)
           overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        }


    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


}
