package com.testcraft.testcraft.activity

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.testcraft.testcraft.R
import com.testcraft.testcraft.adapter.DeepLinkSubjectAdapter
import com.testcraft.testcraft.models.DeepLinkModel
import com.testcraft.testcraft.retrofit.WebClient
import com.testcraft.testcraft.retrofit.WebInterface
import com.testcraft.testcraft.utils.AppConstants
import com.testcraft.testcraft.utils.AppConstants.Companion.GUEST_FIRSTNAME
import com.testcraft.testcraft.utils.AppConstants.Companion.GUEST_LASTNAME
import com.testcraft.testcraft.utils.CommonWebCalls
import com.testcraft.testcraft.utils.DialogUtils
import com.testcraft.testcraft.utils.Utils
import kotlinx.android.synthetic.main.activity_deeplink_entry.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.util.*

class DeeplinkEntryActivity : AppCompatActivity() {

    var list: ArrayList<String> = ArrayList()

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        setContentView(R.layout.activity_deeplink_entry)

        rvList = findViewById(R.id.deeplink_rvList)

        deeplink_rvList.layoutManager =
            LinearLayoutManager(this@DeeplinkEntryActivity, LinearLayoutManager.VERTICAL, false)

        if (Utils.getStringValue(this@DeeplinkEntryActivity, AppConstants.USER_ID, "")!! == "") {
            CommonWebCalls.callSignupApi("deeplink", this@DeeplinkEntryActivity, "5", "0", GUEST_FIRSTNAME, GUEST_LASTNAME, "", "", "")
        } else {
            getDeeplinkSubject(this@DeeplinkEntryActivity)
        }
    }

    companion object {

        lateinit var rvList: RecyclerView

        fun getDeeplinkSubject(context: Context) {
            if (!DialogUtils.isNetworkConnected(context)) {
                Utils.ping(context, AppConstants.NETWORK_MSG)
            }

            DialogUtils.showDialog(context)
            val apiService = WebClient.getClient().create(WebInterface::class.java)

            val call =
                apiService.getDplinkSubject(Utils.getStringValue(context, AppConstants.DEEPLINK_CODE, "")!!, Utils.getStringValue(context, AppConstants.USER_ID, "")!!)

            call.enqueue(object : Callback<DeepLinkModel> {
                override fun onResponse(call: Call<DeepLinkModel>, response: Response<DeepLinkModel>) {

                    DialogUtils.dismissDialog()

                    if (response.body() != null) {

                        if (response.body()!!.Status == "true") {

                            val data = response.body()!!.data

                            rvList.adapter = DeepLinkSubjectAdapter(context, data)

                        } else {

//                            val i = Intent(context, DeeplinkTestActivity::class.java)
//                            context.startActivity(i)

//                        Toast.makeText(
//                            this@DeeplinkEntryActivity,
//                            response.body()!!.Msg.replace("\"", ""),
//                            Toast.LENGTH_SHORT
//                        ).show()
                        }
                    }
                }

                override fun onFailure(call: Call<DeepLinkModel>, t: Throwable) {
                    // Log error here since request failed
                    Log.e("", t.toString())
                    DialogUtils.dismissDialog()
                }
            })
        }
    }

}
