package com.testcraft.testcraft.activity

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.View
import android.view.WindowManager
import com.testcraft.testcraft.R
import com.testcraft.testcraft.adapter.SelectPackageAdapter
import com.testcraft.testcraft.models.PackageData
import com.testcraft.testcraft.utils.AppConstants
import com.testcraft.testcraft.utils.TextDrawablee
import kotlinx.android.synthetic.main.activity_select_package.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class SelectPackageActivity : AppCompatActivity() {

    var selectPackageAdapter: SelectPackageAdapter? = null

    private var mDrawableBuilder: TextDrawablee.IBuilder? = null

    // list of data items
    private var mDataList: ArrayList<PackageData.PackageDataList>? = null

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

        setContentView(R.layout.activity_select_package)

//        mDataList.add(PackageData("Iron Man", R.drawable.ironman, "$ 100"))
//        mDataList.add(PackageData("Captain America", 0, "$ 200"))
//        mDataList.add(PackageData("James Bond", 0, "$ 500"))
//        mDataList.add(PackageData("Harry Potter", R.drawable.harrypotter, "$ 300"))
//        mDataList.add(PackageData("Sherlock Holmes", 0, "$ 700"))
//        mDataList.add(PackageData("Black Widow", R.drawable.blackwidow, "$ 400"))
//        mDataList.add(PackageData("Hawk Eye", 0, "$ 5100"))
//        mDataList.add(PackageData("Iron Man", 0, "$ 200"))
//        mDataList.add(PackageData("Guava", 0, "$ 600"))
//        mDataList.add(PackageData("Tomato", 0, "$ 400"))
//        mDataList.add(PackageData("Sherlock Holmes", 0, "$ 100"))
//        mDataList.add(PackageData("Strawberry", 0, "$ 6700"))
//        mDataList.add(PackageData("Watermelon", 0, "$ 200"))
//        mDataList.add(PackageData("Pears", 0, "$ 500"))
//        mDataList.add(PackageData("Kiwi", R.drawable.kiwi, "$ 800"))

        package_rvList.layoutManager = GridLayoutManager(this@SelectPackageActivity, 2)

        mDrawableBuilder = TextDrawablee.builder().round()

        package_ivBack.setOnClickListener {
            onBackPressed()
        }

//        callPackageListApi()

    }


//    fun callPackageListApi() {
//
//        if (!DialogUtils.isNetworkConnected(this@SelectPackageActivity)) {
//            Utils.ping(this@SelectPackageActivity, "Connetion not available")
//        }
//
//        DialogUtils.showDialog(this@SelectPackageActivity)
//        val apiService = WebClient.getClient().create(WebInterface::class.java)
//
//        val call = apiService.getPackage(
//            WebRequests.getPackageParams(
////                Utils.getStringValue(this@SelectPackageActivity, "course_type_id", "0")!!,
////                Utils.getStringValue(this@SelectPackageActivity, "course_id", "0")!!,
////                Utils.getStringValue(this@SelectPackageActivity, "std_id", "0")!!,
////                intent.getStringExtra("subject_id")
//
//                Utils.getStringValue(this@SelectPackageActivity, AppConstants.COURSE_TYPE_ID, "")!!,
//                Utils.getStringValue(this@SelectPackageActivity, AppConstants.COURSE_ID, "")!!,
//                Utils.getStringValue(this@SelectPackageActivity, AppConstants.STANDARD_ID, "")!!,
//                Utils.getStringValue(this@SelectPackageActivity, AppConstants.SUBJECT_ID, "")!!
//            )
//        )
//        call.enqueue(object : Callback<PackageData> {
//            override fun onResponse(call: Call<PackageData>, response: Response<PackageData>) {
//
//                if (response.body() != null) {
//
//                    DialogUtils.dismissDialog()
//
//                    if (response.body()!!.Status == "true") {
//
//                        mDataList = response.body()!!.data
//
//                        selectPackageAdapter = SelectPackageAdapter(this@SelectPackageActivity, mDataList!!)
//                        package_rvList.adapter = selectPackageAdapter
//
//                    } else {
//
//                        Toast.makeText(this@SelectPackageActivity, response.body()!!.Msg, Toast.LENGTH_SHORT).show()
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

    override fun onBackPressed() {
        super.onBackPressed()

        if (AppConstants.COURSE_FLOW_ARRAY.size > 0) {
            AppConstants.COURSE_FLOW_ARRAY.removeAt(AppConstants.COURSE_FLOW_ARRAY.size - 1)
        }

    }

}
