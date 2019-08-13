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
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.testprep.R
import com.testprep.adapter.TestTypeAdapter
import com.testprep.fragments.TutorProfileFragment
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

//        Log.d("colr1", " " + pos + " " + package_detail_tvPname.text.length)

//        package_detail_image1.setImageDrawable(Utils.createDrawable(pos, package_detail_tvPname.text.length))
        package_detail_tvlprice.paintFlags = package_detail_tvlprice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        package_detail_rvList.isNestedScrollingEnabled = false
        package_detail_rvList.layoutManager =
            LinearLayoutManager(this@PackageDetailActivity, LinearLayoutManager.VERTICAL, false)

        if (intent.getStringExtra("come_from") == "mypackage") {
            package_detail_btnAddTocart.visibility = View.GONE
        }

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

        callTestPackageDetailApi()
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


    fun callTestPackageDetailApi() {

        if (!DialogUtils.isNetworkConnected(this@PackageDetailActivity)) {
            Utils.ping(this@PackageDetailActivity, "Connetion not available")
        }

        DialogUtils.showDialog(this@PackageDetailActivity)
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getPackageDetail(intent.getStringExtra("pkgid"))

        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!["Status"].asString == "true") {

                        package_detail_tvPname.text =
                            response.body()!!.get("data").asJsonObject.get("TestPackageName").asString
                        package_detail_tvsprice.text =
                            "Sell Price : " + response.body()!!.get("data").asJsonObject.get("TestPackageSalePrice").asString
                        package_detail_tvlprice.text =
                            "List Price : " + response.body()!!.get("data").asJsonObject.get("TestPackageListPrice").asString.trim()
                        package_detail_tvDesc.text =
                            response.body()!!.get("data").asJsonObject.get("TestPackageDescription").asString
                        package_detail_name_short.text =
                            response.body()!!.get("data").asJsonObject.get("TestPackageName").asString.substring(0, 1)

                        if (response.body()!!.get("data").asJsonObject.get("InstituteName").asString != "" && response.body()!!.get(
                                "data"
                            ).asJsonObject.get("InstituteName").asString != null
                        ) {
                            package_detail_createdby.text =
                                Html.fromHtml(
                                    "created by " + "<font color=\"#3ea7e0\">" + response.body()!!.get("data").asJsonObject.get(
                                        "InstituteName"
                                    ).asString + "</font>"
                                )
                        } else {
                            package_detail_createdby.text =
                                Html.fromHtml(
                                    "created by " + "<font color=\"#3ea7e0\">" + response.body()!!.get("data").asJsonObject.get(
                                        "TutorName"
                                    ).asString + "</font>"
                                )
                        }


                        Log.d("pkgid", intent.getStringExtra("pkgid"))

                        pkgid = response.body()!!.get("data").asJsonObject.get("TestPackageID").asString
                        tutor_id = response.body()!!.get("data").asJsonObject.get("TutorID").asString

//                        var pos = intent.getCharExtra("position", 'a')


                        var testList: JsonArray? =
                            response.body()!!.get("data").asJsonObject.get("TestList").asJsonArray

                        package_detail_rvList.adapter = TestTypeAdapter(
                            this@PackageDetailActivity, testList!!
                        )
                    } else {

                        Toast.makeText(
                            this@PackageDetailActivity,
                            response.body()!!["Msg"].toString().replace("\"", ""), Toast.LENGTH_SHORT
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
