package com.testcraft.testcraft.activity

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.testcraft.testcraft.R
import com.testcraft.testcraft.adapter.ApplyCouponAdapter
import com.testcraft.testcraft.models.CouponModel
import com.testcraft.testcraft.retrofit.WebClient
import com.testcraft.testcraft.retrofit.WebInterface
import com.testcraft.testcraft.utils.DialogUtils
import com.testcraft.testcraft.utils.Utils
import kotlinx.android.synthetic.main.activity_apply_coupon.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class ApplyCouponActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setContentView(R.layout.activity_apply_coupon)

        coupon_rvList.layoutManager =
            LinearLayoutManager(this@ApplyCouponActivity, LinearLayoutManager.VERTICAL, false)

        coupon_ivBack.setOnClickListener {
            onBackPressed()
        }

        getCoupons()

    }

    private fun getCoupons() {
        if (!DialogUtils.isNetworkConnected(this@ApplyCouponActivity)) {
            Utils.ping(this@ApplyCouponActivity, "Connection not available")
        }

        DialogUtils.showDialog(this@ApplyCouponActivity)
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getCoupons()

        call.enqueue(object : Callback<CouponModel> {
            override fun onResponse(call: Call<CouponModel>, response: Response<CouponModel>) {

                DialogUtils.dismissDialog()

                if (response.body() != null) {

                    if (response.body()!!.Status == "true") {

                        val data = response.body()!!.data

                        coupon_rvList.adapter = ApplyCouponAdapter(this@ApplyCouponActivity, data)


                    } else {

                        Toast.makeText(
                            this@ApplyCouponActivity,
                            response.body()!!.Msg.replace("\"", ""),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<CouponModel>, t: Throwable) {
                // Log error here since request failed
                Log.e("", t.toString())
                DialogUtils.dismissDialog()
            }
        })
    }

}
