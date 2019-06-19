package com.testprep.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.testprep.R
import com.testprep.adapter.MyPaymentAdapter
import com.testprep.models.PackageData
import com.testprep.retrofit.WebClient
import com.testprep.retrofit.WebInterface
import com.testprep.utils.AppConstants
import com.testprep.utils.DialogUtils
import com.testprep.utils.Utils
import kotlinx.android.synthetic.main.fragment_my_payments.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class MyPaymentFragment : AppCompatActivity() {

//    private var packageSize: ArrayList<Int> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setContentView(R.layout.fragment_my_payments)

//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_my_packages, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)

//        val heading = this@MyPaymentFragment.findViewById(R.id.dashboard_tvTitle) as TextView
//        heading.text = "My Payments"

//        packageSize.add(23)
//        packageSize.add(25)
//        packageSize.add(27)
//        packageSize.add(29)
//        packageSize.add(22)

        my_payments_rvList.layoutManager =
            LinearLayoutManager(this@MyPaymentFragment, LinearLayoutManager.VERTICAL, false)

        val dividerItemDecoration = DividerItemDecoration(my_payments_rvList.context, LinearLayoutManager.VERTICAL)

        my_payments_rvList.addItemDecoration(dividerItemDecoration)

        my_payments_ivBack.setOnClickListener { onBackPressed() }

        callPaymentListApi()
    }

    fun callPaymentListApi() {

        if (!DialogUtils.isNetworkConnected(this@MyPaymentFragment)) {
            Utils.ping(this@MyPaymentFragment, "Connetion not available")
        }

        DialogUtils.showDialog(this@MyPaymentFragment)
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getMyPayment(Utils.getStringValue(this@MyPaymentFragment, AppConstants.USER_ID, "0")!!)
        call.enqueue(object : Callback<PackageData> {
            override fun onResponse(call: Call<PackageData>, response: Response<PackageData>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.Status == "true") {

                        val mDataList = response.body()!!.data

                        my_payments_rvList.adapter = MyPaymentAdapter(this@MyPaymentFragment, mDataList)

                    } else {

                        Toast.makeText(this@MyPaymentFragment, response.body()!!.Msg, Toast.LENGTH_SHORT).show()
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
}
