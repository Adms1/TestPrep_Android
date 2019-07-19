package com.testprep.activity

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.Html
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.google.gson.JsonObject
import com.testprep.R
import com.testprep.adapter.TestTypeAdapter
import com.testprep.fragments.TutorProfileFragment
import com.testprep.models.PackageData
import com.testprep.retrofit.WebClient
import com.testprep.retrofit.WebInterface
import com.testprep.utils.AppConstants
import com.testprep.utils.DialogUtils
import com.testprep.utils.Utils
import kotlinx.android.synthetic.main.activity_package_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class PackageDetailActivity : AppCompatActivity() {

    var pkgid = ""
    var tutor_id = ""

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

        setContentView(R.layout.activity_package_detail)

        package_detail_tvPname.text = intent.getStringExtra("pname")
        package_detail_tvsprice.text = "Sell Price : " + intent.getStringExtra("sprice")
        package_detail_tvlprice.text = "List Price : " + intent.getStringExtra("lprice").trim()
        package_detail_tvDesc.text = intent.getStringExtra("desc")
        package_detail_name_short.text = intent.getStringExtra("pname").substring(0, 1)
        package_detail_createdby.text =
            Html.fromHtml("created by " + "<font color=\"#3ea7e0\">" + intent.getStringExtra("created_by") + "</font>")
        Log.d("pkgid", intent.getStringExtra("pkgid"))

        pkgid = intent.getStringExtra("pkgid")
        tutor_id = intent.getStringExtra("tutor_id")

        var pos = intent.getCharExtra("position", 'a')

        Log.d("colr1", " " + pos + " " + package_detail_tvPname.text.length)

//        package_detail_image1.setImageDrawable(Utils.createDrawable(pos, package_detail_tvPname.text.length))
        package_detail_tvlprice.paintFlags = package_detail_tvlprice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        package_detail_rvList.isNestedScrollingEnabled = false
        package_detail_rvList.layoutManager =
            LinearLayoutManager(this@PackageDetailActivity, LinearLayoutManager.VERTICAL, false)

        if (intent.getStringExtra("come_from") == "mypackage") {
            package_detail_btnAddTocart.visibility = View.GONE
        }

        package_detail_rvList.adapter = TestTypeAdapter(
            this@PackageDetailActivity,
            intent.getSerializableExtra("test_type_list") as ArrayList<PackageData.PackageTestType>
        )

        package_detail_createdby.setOnClickListener {

            val intent = Intent(this@PackageDetailActivity, TutorProfileFragment::class.java)
            intent.putExtra("tutor_id", tutor_id)
            startActivity(intent)

        }

        package_detail_ivBack.setOnClickListener {
            onBackPressed()
        }

        package_detail_btnAddTocart.setOnClickListener {

            DialogUtils.createConfirmDialog(
                this@PackageDetailActivity,
                "",
                "Are you sure you want to buy this package?",
                "Yes",
                "No",
                DialogInterface.OnClickListener { dialog, which ->
                    callAddTestPackageApi()

                },
                DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()


                }).show()
        }
    }

    fun callAddTestPackageApi() {

        if (!DialogUtils.isNetworkConnected(this@PackageDetailActivity)) {
            Utils.ping(this@PackageDetailActivity, "Connetion not available")
        }

        DialogUtils.showDialog(this@PackageDetailActivity)
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.addTestPackage(
            Utils.getStringValue(this@PackageDetailActivity, AppConstants.USER_ID, "0")!!,
            intent.getStringExtra("pkgid")
        )
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!["Status"].toString() == "true") {

                        Toast.makeText(
                            this@PackageDetailActivity,
                            response.body()!!["Msg"].toString().replace("\"", ""),
                            Toast.LENGTH_SHORT
                        ).show()
                        onBackPressed()
                    } else {

                        Toast.makeText(
                            this@PackageDetailActivity,
                            response.body()!!["Msg"].toString().replace("\"", ""),
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
