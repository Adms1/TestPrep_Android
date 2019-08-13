package com.testprep.fragments


import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.testprep.R
import kotlinx.android.synthetic.main.fragment_my_packages.*

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

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        setContentView(R.layout.fragment_my_packages)

//    }
//
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

//        val heading = this@MyPackagesFragment.findViewById(R.id.dashboard_tvTitle) as TextView
//        heading.text = "My Packages"

        my_packages_rvList.layoutManager =
            LinearLayoutManager(this@MyPackagesFragment, LinearLayoutManager.VERTICAL, false)

//        val dividerItemDecoration = DividerItemDecoration(
//            my_packages_rvList.context,
//            LinearLayoutManager.VERTICAL
//        )
//        my_packages_rvList.addItemDecoration(dividerItemDecoration)


//        callMyPackagesApi()
    }

//    fun callMyPackagesApi() {
//
//        if (!DialogUtils.isNetworkConnected(this@MyPackagesFragment)) {
//            Utils.ping(this@MyPackagesFragment, "Connetion not available")
//        }
//
//        DialogUtils.showDialog(this@MyPackagesFragment)
//        val apiService = WebClient.getClient().create(WebInterface::class.java)
//
//        val call = apiService.myTestPackage(Utils.getStringValue(this@MyPackagesFragment, AppConstants.USER_ID, "0")!!)
//        call.enqueue(object : Callback<PackageData> {
//            override fun onResponse(call: Call<PackageData>, response: Response<PackageData>) {
//
//                if (response.body() != null) {
//
//                    DialogUtils.dismissDialog()
//
//                    if (response.body()!!.Status == "true") {
//
//                        var pkgArr = response.body()!!.data
//                        my_packages_rvList.adapter = MyPackageAdapter(this@MyPackagesFragment, pkgArr)
//                    } else {
//
//                        Toast.makeText(this@MyPackagesFragment, response.body()!!.Msg.replace("\"", ""), Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<PackageData>, t: Throwable) {
//                // Log error here since request failed
//                Log.e("", t.toString())
//                DialogUtils.dismissDialog()
//            }
//        })
//    }


}
