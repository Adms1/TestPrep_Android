package com.testcraft.testcraft.activity

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.testcraft.testcraft.R
import com.testcraft.testcraft.adapter.SWAdapter
import com.testcraft.testcraft.retrofit.WebClient
import com.testcraft.testcraft.retrofit.WebInterface
import com.testcraft.testcraft.utils.DialogUtils
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_strength_weekness.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StrengthWeeknessActivity : AppCompatActivity() {

    var studenttestid = ""

    var st = true
    var we = true

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        setContentView(R.layout.activity_strength_weekness)

        studenttestid = intent.getStringExtra("studenttestid")

        SW_rvStrength.layoutManager = LinearLayoutManager(this@StrengthWeeknessActivity, LinearLayoutManager.VERTICAL, false)
        SW_rvWeekness.layoutManager = LinearLayoutManager(this@StrengthWeeknessActivity, LinearLayoutManager.VERTICAL, false)

        SW_rvStrength.isNestedScrollingEnabled = false
        SW_rvWeekness.isNestedScrollingEnabled = false

//        SW_rl1.setOnClickListener {
//
//            st = !st
//
//            if(st) {
//                SW_rvStrength.visibility = View.VISIBLE
//                SW_step1_arrow.rotation = 90f
//            }else{
//                SW_rvStrength.visibility = View.GONE
//                SW_step1_arrow.rotation = -90f
//            }
////            SW_rvWeekness.visibility = View.GONE
//
////            SW_step2_arrow.rotation = -90f
//
//        }
//
//        SW_rl2.setOnClickListener {
//
//            we = !we
//
//            if(we) {
////            SW_rvStrength.visibility = View.GONE
//                SW_rvWeekness.visibility = View.VISIBLE
//                SW_step2_arrow.rotation = 90f
//            }else{
//                SW_rvWeekness.visibility = View.GONE
//                SW_step2_arrow.rotation = -90f
//            }
//
//
////            SW_step1_arrow.rotation = -90f
//
//        }

        SW_btnClose.setOnClickListener { onBackPressed() }

        callStudentAnalysis()
    }

    fun callStudentAnalysis() {

        DialogUtils.showDialog(this@StrengthWeeknessActivity)

        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.studentAnalysis(studenttestid)
        call.enqueue(object : Callback<JsonObject> {

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                DialogUtils.dismissDialog()
                if (response.body()!!.get("Status").asString == "true") {

                    val arr: ArrayList<String> = ArrayList()
                    val arr1: ArrayList<String> = ArrayList()

                    if(response.body()!!.get("data").asJsonObject.get("Strength").asJsonArray.size() > 0) {

                        SW_rl1.visibility = View.VISIBLE

                        for (i in 0 until response.body()!!.get("data").asJsonObject.get("Strength").asJsonArray.size()) {
                            arr.add(response.body()!!.get("data").asJsonObject.get("Strength").asJsonArray[i].asString)
                        }
                    }else{
                        SW_rl1.visibility = View.GONE
                    }

                    if(response.body()!!.get("data").asJsonObject.get("Weakness").asJsonArray.size() > 0) {

                        SW_rl2.visibility = View.VISIBLE

                        for (i in 0 until response.body()!!.get("data").asJsonObject.get("Weakness").asJsonArray.size()) {
                            arr1.add(response.body()!!.get("data").asJsonObject.get("Weakness").asJsonArray[i].asString)
                        }
                    }else{
                        SW_rl2.visibility = View.GONE
                    }

//                    if (!dialog.isShowing) {
//                        dialog.setContentView(R.layout.activity_strength_weekness)
//                        dialog.setCanceledOnTouchOutside(false)
//
//                        val rvList: ListView = dialog.findViewById(R.id.SW_rvStrength)
//                        val rvList1: ListView = dialog.findViewById(R.id.SW_rvWeekness)
//                        val btnCancel: TextView = dialog.findViewById(R.id.SW_btnClose)

                    SW_rvStrength.adapter = SWAdapter(this@StrengthWeeknessActivity, arr)
                    SW_rvWeekness.adapter = SWAdapter(this@StrengthWeeknessActivity, arr1)

//                        btnCancel.setOnClickListener {
//                            dialog.dismiss()
//                        }

//                        dialog.show()
//                    }

                } else {

                    Toast.makeText(this@StrengthWeeknessActivity, response.body()!!.get("Msg").asString, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // Log error here since request failed
                DialogUtils.dismissDialog()
                Log.e("", t.toString())
            }
        })
    }
}
