package com.testprep.activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.gson.JsonObject
import com.testprep.R
import com.testprep.adapter.NewChooseCoarseAdapter
import com.testprep.models.PackageData
import com.testprep.retrofit.WebClient
import com.testprep.retrofit.WebInterface
import com.testprep.utils.AppConstants
import com.testprep.utils.DialogUtils
import com.testprep.utils.Utils
import kotlinx.android.synthetic.main.activity_new.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper


class NewActivity : AppCompatActivity() {

    private var chooseCoarseAdapter: NewChooseCoarseAdapter? = null

    var resumeblock = false
    var dialog: Dialog? = null

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setContentView(R.layout.activity_new)

        if (Utils.getStringValue(this@NewActivity, AppConstants.USER_MOBILE, "")!! == "") {

            dialog = Dialog(this@NewActivity)
            dialog!!.setContentView(R.layout.dialog_verify_number)
            dialog!!.setCanceledOnTouchOutside(false)
            dialog!!.setCancelable(false)

            val etMobile: EditText = dialog!!.findViewById(R.id.dialog_verify_etMobile)
            val submit: TextView = dialog!!.findViewById(R.id.dialog_verify_btnSubmit)

            submit.setOnClickListener {

                if (etMobile.text.toString() != "") {

                    callCheckPhoneApi(etMobile.text.toString())

                }

            }

            dialog!!.show()

        }

        new_coarse_rvCoarseList.layoutManager = GridLayoutManager(this@NewActivity, 2)

        new_ivBack.setOnClickListener {
            dialog!!.dismiss()
            onBackPressed()
        }

        callCourseListApi()
    }

    override fun onBackPressed() {

    }

    override fun onResume() {

        super.onResume()

        if (resumeblock) {

            if (Utils.getStringValue(this@NewActivity, AppConstants.USER_MOBILE, "")!! == "") {

                dialog = Dialog(this@NewActivity)
                dialog!!.setContentView(R.layout.dialog_verify_number)
                dialog!!.setCanceledOnTouchOutside(false)
                dialog!!.setCancelable(false)

                val etMobile: EditText =
                    dialog!!.findViewById(R.id.dialog_verify_etMobile)
                val submit: TextView =
                    dialog!!.findViewById(R.id.dialog_verify_btnSubmit)

                submit.setOnClickListener {

                    if (etMobile.text.toString() != "") {
                        callCheckPhoneApi(etMobile.text.toString())
                    }

                }

                dialog!!.show()

            }
        }

        resumeblock = true

    }

    fun callCourseListApi() {

        if (!DialogUtils.isNetworkConnected(this@NewActivity)) {
            Utils.ping(this@NewActivity, "Connetion not available")
        }

        DialogUtils.showDialog(this@NewActivity)
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getCourseList()
        call.enqueue(object : Callback<PackageData> {
            override fun onResponse(call: Call<PackageData>, response: Response<PackageData>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.Status == "true") {

                        chooseCoarseAdapter = NewChooseCoarseAdapter(this@NewActivity, response.body()!!.data)
                        new_coarse_rvCoarseList.adapter = chooseCoarseAdapter

                    } else {

                        Toast.makeText(this@NewActivity, response.body()!!.Msg, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<PackageData>, t: Throwable) {
                // Log error here since request failed
                Log.e("", t.toString())
                DialogUtils.dismissDialog()
            }
        })
    }

    fun callCheckPhoneApi(phone: String) {

        if (!DialogUtils.isNetworkConnected(this@NewActivity)) {
            Utils.ping(this@NewActivity, "Connetion not available")
        }

        DialogUtils.showDialog(this@NewActivity)

        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.checkMobile(phone)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!["Status"].asString == "true") {
                        val intent = Intent(this@NewActivity, OtpActivity::class.java)
                        intent.putExtra("mobile_number", phone)
                        intent.putExtra(
                            "otp",
                            response.body()!!.get("data").asString
                        )
                        intent.putExtra("come_from", "phnverify")
                        intent.putExtra(
                            "first_name",
                            Utils.getStringValue(this@NewActivity, AppConstants.FIRST_NAME, "")
                        )
                        intent.putExtra(
                            "last_name",
                            Utils.getStringValue(this@NewActivity, AppConstants.LAST_NAME, "")
                        )
                        intent.putExtra(
                            "email",
                            Utils.getStringValue(this@NewActivity, AppConstants.USER_EMAIL, "")
                        )
                        intent.putExtra("password", "")
                        intent.putExtra(
                            "account_type",
                            Utils.getStringValue(
                                this@NewActivity,
                                AppConstants.USER_ACCOUNT_TYPE,
                                ""
                            )
                        )
                        startActivity(intent)

                        dialog!!.dismiss()

                    } else {
                        Toast.makeText(
                            this@NewActivity,
                            response.body()!!.get("Msg").asString,
                            Toast.LENGTH_SHORT
                        ).show()
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

}
