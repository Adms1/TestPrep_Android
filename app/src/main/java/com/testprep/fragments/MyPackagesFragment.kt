package com.testprep.fragments


import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.WindowManager
import com.testprep.adapter.MyPackageAdapter
import com.testprep.models.MyPackageModel
import com.testprep.retrofit.WebClient
import com.testprep.retrofit.WebInterface
import com.testprep.utils.AppConstants
import com.testprep.utils.DialogUtils
import com.testprep.utils.Utils
import kotlinx.android.synthetic.main.fragment_my_packages.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class MyPackagesFragment : AppCompatActivity() {

    private var packageSize: ArrayList<Int> = ArrayList()
    private var subid = 0

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setContentView(com.testprep.R.layout.fragment_my_packages)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w = window
            w.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }

        subid = intent.getIntExtra("sub_id", 0)

        my_packages_ivBack.setOnClickListener { onBackPressed() }

        my_packages_header.text = intent.getStringExtra("sub_name")

        my_packages_rvList.layoutManager =
            LinearLayoutManager(this@MyPackagesFragment, LinearLayoutManager.VERTICAL, false)

        my_packages_rvList.isNestedScrollingEnabled = false

        DialogUtils.showDialog(this@MyPackagesFragment)

        callMyPackagesApi()
    }

    fun callMyPackagesApi() {

        if (!DialogUtils.isNetworkConnected(this@MyPackagesFragment)) {
            Utils.ping(this@MyPackagesFragment, "Connetion not available")
        }

        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getMyPackages(
            Utils.getStringValue(this@MyPackagesFragment, AppConstants.USER_ID, "0")!!,
            subid.toString()
        )

        call.enqueue(object : Callback<MyPackageModel> {

            override fun onResponse(call: Call<MyPackageModel>, response: Response<MyPackageModel>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.Status == "true") {

                        var summaryArr = response.body()!!.data[0].TestSummary

                        var totalcount = 0
                        var pendingcount = 0
                        var completecount = 0
                        var startCount = 0

                        for (i in 0 until summaryArr.size) {
                            totalcount += summaryArr[i].count

                            if (summaryArr[i].status == "Analyse") {
                                completecount = summaryArr[i].count
                            } else if (summaryArr[i].status == "Start") {
                                startCount = summaryArr[i].count
                            }
                        }

                        pendingcount = totalcount - completecount

                        val amount = java.lang.Double.parseDouble(totalcount.toString())
                        val res = amount / 100.0f * 10

                        var final = 0F

                        var per = (((pendingcount.toFloat()) / totalcount.toFloat()) * 100)

                        if (per > 0) {
                            final = 100 - per
                        } else if (per == 0F) {
                            final = 100.0F
                        } else {
                            final = 0F
                        }

//                        var per = (pendingcount.toFloat()/totalcount.toFloat())*100
                        Log.d("percentage", "" + final.toFloat())

                        my_packages_ivProgress.setProgress(final.toFloat(), true)
//                        my_packages_ivProgress.maxValue = totalcount.toFloat()

                        my_packages_tvPendingCount.text = pendingcount.toString()
                        my_packages_tvTotalCount.text = totalcount.toString()

                        val pkgArr = response.body()!!.data[0].PackageList
                        my_packages_rvList.adapter = MyPackageAdapter(this@MyPackagesFragment, pkgArr)
//                        my_packages_rvList.adapter = TestPackagesAdapter(this@MyPackagesFragment, pkgArr)
                    }
                }
            }

            override fun onFailure(call: Call<MyPackageModel>, t: Throwable) {
                // Log error here since request failed
                Log.e("", t.toString())
                DialogUtils.dismissDialog()
            }
        })
    }
}
