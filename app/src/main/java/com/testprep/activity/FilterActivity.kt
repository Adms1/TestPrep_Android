package com.testprep.activity

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.testprep.R
import com.testprep.adapter.FilterListAdapter
import com.testprep.adapter.TestPackagesAdapter
import com.testprep.fragments.OtherFilterFragment
import com.testprep.fragments.PriceFilterFragment
import com.testprep.interfaces.FilterTypeSelectionInteface
import com.testprep.models.PackageData
import com.testprep.retrofit.WebClient
import com.testprep.retrofit.WebInterface
import com.testprep.utils.DialogUtils
import com.testprep.utils.Utils
import com.testprep.utils.WebRequests
import kotlinx.android.synthetic.main.activity_filter.*
import kotlinx.android.synthetic.main.fragment_choose_market_place.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class FilterActivity : AppCompatActivity(), FilterTypeSelectionInteface {

    val filterTypeArr: ArrayList<String> = ArrayList()
    var filterTypeSelectionInteface: FilterTypeSelectionInteface? = null
    var filterAdapter: FilterListAdapter? = null

    private var testPackagesAdapter: TestPackagesAdapter? = null
    private var mDataList: ArrayList<PackageData.PackageDataList>? = null

    var coursetypeid = ""
    var boardid = ""
    var examids = ""
    var stdids = ""
    var subids = ""
    var tutorids = ""
    var max = ""
    var min = ""

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setContentView(R.layout.activity_filter)

        filterTypeSelectionInteface = this

        filter_rvList.layoutManager = LinearLayoutManager(this@FilterActivity, LinearLayoutManager.VERTICAL, false)

//        filterTypeArr.add("Standards")
//        filterTypeArr.add("Subjects")
//        filterTypeArr.add("Popular")
//        filterTypeArr.add("Price")
//        filterTypeArr.add("Reviews & Ratings")
//        filterTypeArr.add("discount")
//        filterTypeArr.add("Promoted")

        filterAdapter = FilterListAdapter(this@FilterActivity, filterTypeArr, filterTypeSelectionInteface!!)
        filter_rvList.adapter = filterAdapter

        filter_rgCourseType.setOnCheckedChangeListener { group, checkedId ->

            if (checkedId == R.id.filter_rbBoards) {

                coursetypeid = "1"

            } else if (checkedId == R.id.filter_rbCompetitive) {

                coursetypeid = "2"

            }

        }

        if (filter_rbBoards.isChecked) {
//            filterTypeArr.add("Course Type")
            filterTypeArr.add("Boards")
//            filterTypeArr.add("Competitive Exams")
            filterTypeArr.add("Standard")
            filterTypeArr.add("Subjects")
            filterTypeArr.add("Tutor")
            filterTypeArr.add("Price")

            filterAdapter!!.notifyDataSetChanged()

        } else {
//            filterTypeArr.add("Course Type")
//            filterTypeArr.add("Boards")
            filterTypeArr.add("Competitive Exams")
//            filterTypeArr.add("Standard")
            filterTypeArr.add("Subjects")
            filterTypeArr.add("Tutor")
            filterTypeArr.add("Price")

            filterAdapter!!.notifyDataSetChanged()

        }

        var fragment = OtherFilterFragment()
        val bundle = Bundle()
        bundle.putString("type", "course_type")
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction().add(R.id.filter_container, fragment).commit()

        filter_ivBack.setOnClickListener { onBackPressed() }

        filter_btnApply.setOnClickListener { callFilterListApi() }
    }

    override fun getType(p0: Int) {


        var fragment = OtherFilterFragment()
        val bundle = Bundle()

        when {
            filterTypeArr[p0] == "Course Type" -> {

                bundle.putString("type", "course_type")
                fragment.arguments = bundle
                supportFragmentManager.beginTransaction().replace(R.id.filter_container, fragment).commit()
            }

            filterTypeArr[p0] == "Boards" -> {

                bundle.putString("type", "boards")
                fragment.arguments = bundle
                supportFragmentManager.beginTransaction().replace(R.id.filter_container, fragment).commit()
            }

            filterTypeArr[p0] == "Competitive Exams" -> {

                bundle.putString("type", "competitive_exams")
                fragment.arguments = bundle

                supportFragmentManager.beginTransaction().replace(R.id.filter_container, fragment).commit()
            }

            filterTypeArr[p0] == "Standard" -> {

                bundle.putString("type", "standards")
                fragment.arguments = bundle

                supportFragmentManager.beginTransaction().replace(R.id.filter_container, fragment).commit()
            }

            filterTypeArr[p0] == "Subjects" -> {

                bundle.putString("type", "subjects")
                fragment.arguments = bundle

                supportFragmentManager.beginTransaction().replace(R.id.filter_container, fragment).commit()
            }

            filterTypeArr[p0] == "Tutor" -> {

                bundle.putString("type", "tutor")
                fragment.arguments = bundle

                supportFragmentManager.beginTransaction().replace(R.id.filter_container, fragment).commit()
            }

            filterTypeArr[p0] == "Price" -> supportFragmentManager.beginTransaction().add(
                R.id.filter_container,
                PriceFilterFragment()
            ).commit()
        }
    }

    fun callFilterListApi() {

        if (!DialogUtils.isNetworkConnected(this@FilterActivity)) {
            Utils.ping(this@FilterActivity, "Connetion not available")
        }

        DialogUtils.showDialog(this@FilterActivity)
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getFilterData(
            WebRequests.getFilterParams(
                coursetypeid,
                "",
                examids,
                stdids,
                subids,
                tutorids,
                min,
                max
            )
        )

        call.enqueue(object : Callback<PackageData> {
            override fun onResponse(call: Call<PackageData>, response: Response<PackageData>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.Status == "true") {

                        mDataList = response.body()!!.data

                        choosemp_rvList.visibility = View.VISIBLE
                        testPackagesAdapter = TestPackagesAdapter(this@FilterActivity, mDataList!!)
                        choosemp_rvList.adapter = testPackagesAdapter
                        finish()

                    } else {

                        Toast.makeText(this@FilterActivity, response.body()!!.Msg, Toast.LENGTH_SHORT).show()
                        choosemp_rvList.visibility = View.GONE
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
