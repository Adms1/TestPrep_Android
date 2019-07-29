package com.testprep.activity

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.testprep.R
import com.testprep.adapter.NewChooseCoarseAdapter
import com.testprep.models.FilterModel
import com.testprep.retrofit.WebClient
import com.testprep.retrofit.WebInterface
import com.testprep.utils.DialogUtils
import com.testprep.utils.Utils
import kotlinx.android.synthetic.main.activity_new.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class NewActivity : AppCompatActivity() {

    private var chooseCoarseAdapter: NewChooseCoarseAdapter? = null

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

        new_coarse_rvCoarseList.layoutManager = GridLayoutManager(this@NewActivity, 2)

        new_ivBack.setOnClickListener { onBackPressed() }

        callCourseListApi()
    }

    fun callCourseListApi() {

        if (!DialogUtils.isNetworkConnected(this@NewActivity)) {
            Utils.ping(this@NewActivity, "Connetion not available")
        }

        DialogUtils.showDialog(this@NewActivity)
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getCourseList()
        call.enqueue(object : Callback<FilterModel> {
            override fun onResponse(call: Call<FilterModel>, response: Response<FilterModel>) {

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

            override fun onFailure(call: Call<FilterModel>, t: Throwable) {
                // Log error here since request failed
                Log.e("", t.toString())
                DialogUtils.dismissDialog()
            }
        })
    }

    override fun onBackPressed() {

    }
}
