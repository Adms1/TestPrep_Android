package com.testcraft.testcraft.activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.testcraft.testcraft.Connectivity
import com.testcraft.testcraft.R
import com.testcraft.testcraft.adapter.NewChooseCoarseAdapter
import com.testcraft.testcraft.models.PackageData
import com.testcraft.testcraft.retrofit.WebClient
import com.testcraft.testcraft.retrofit.WebInterface
import com.testcraft.testcraft.utils.*
import kotlinx.android.synthetic.main.activity_new.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class NewActivity : AppCompatActivity() {

    private var chooseCoarseAdapter: NewChooseCoarseAdapter? = null

    var dialog: Dialog? = null

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    var connectivity: Connectivity? = null

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter()
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        registerReceiver(connectivity, filter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(connectivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        setContentView(R.layout.activity_new)

        connectivity = Connectivity()

        CommonWebCalls.callToken(this@NewActivity, "1", "", ActionIdData.C600, ActionIdData.T600)

//        if (Utils.getStringValue(this@NewActivity, AppConstants.USER_MOBILE, "")!! == "") {
//
//            dialog = Dialog(this@NewActivity)
//            dialog!!.setContentView(R.layout.dialog_verify_number)
//            dialog!!.setCanceledOnTouchOutside(false)
//            dialog!!.setCancelable(false)
//
//            val etMobile: EditText = dialog!!.findViewById(R.id.dialog_verify_etMobile)
//            val submit: TextView = dialog!!.findViewById(R.id.dialog_verify_btnSubmit)
//
////            val skip: TextView =
////                dialog!!.findViewById(R.id.dialog_verify_btnSkip)
//
////            skip.setOnClickListener {
////                dialog!!.dismiss()
////            }
//
//            submit.setOnClickListener {
//
//                if (etMobile.text.toString() != "") {
//
//                    callCheckPhoneApi(etMobile.text.toString())
//
//                }
//
//            }
//
//            dialog!!.show()
//
//        }

        if (!intent.hasExtra("comeadater")) {

            if (Utils.getStringValue(this@NewActivity, AppConstants.USER_ID, "")!! == "") {
                CommonWebCalls.callSignupApi("guest", this@NewActivity, "5", "0",
                    AppConstants.GUEST_FIRSTNAME,
                    AppConstants.GUEST_LASTNAME, "", "", "")
            } else {

                if (Utils.getStringValue(this@NewActivity, AppConstants.isPrefrence, "")!! != "") {
                    val i = Intent(this@NewActivity, DashboardActivity::class.java)
                    startActivity(i)
                }

            }
        }

        new_coarse_rvCoarseList.layoutManager = GridLayoutManager(this@NewActivity, 2)

        callCourseListApi()
    }

    override fun onBackPressed() {

    }

    fun callCourseListApi() {

        if (!DialogUtils.isNetworkConnected(this@NewActivity)) {
            Utils.ping(this@NewActivity, AppConstants.NETWORK_MSG)
        }

        DialogUtils.showDialog(this@NewActivity)
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getCourseList()
        call.enqueue(object : Callback<PackageData> {
            override fun onResponse(call: Call<PackageData>, response: Response<PackageData>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.Status == "true") {

                        chooseCoarseAdapter =
                            NewChooseCoarseAdapter(this@NewActivity, response.body()!!.data)
                        new_coarse_rvCoarseList.adapter = chooseCoarseAdapter

                    } else {

                        Toast.makeText(this@NewActivity, response.body()!!.Msg, Toast.LENGTH_SHORT).show()

                    }
                }
            }

            override fun onFailure(call: Call<PackageData>, t: Throwable) {

                Log.e("", t.toString())
                DialogUtils.dismissDialog()

                call.clone().enqueue(this)
            }
        })
    }

}
