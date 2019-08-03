package com.testprep.activity

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.testprep.R
import com.testprep.adapter.FilterListAdapter
import com.testprep.adapter.TestPackagesAdapter
import com.testprep.fragments.OtherFilterFragment
import com.testprep.fragments.PriceFilterFragment
import com.testprep.interfaces.FilterTypeSelectionInteface
import com.testprep.interfaces.filterInterface
import com.testprep.models.PackageData
import com.testprep.utils.AppConstants
import com.testprep.utils.Utils
import kotlinx.android.synthetic.main.activity_filter.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class FilterActivity : AppCompatActivity(), FilterTypeSelectionInteface {

    var filterTypeArr: ArrayList<String> = ArrayList()
    var filterTypeSelectionInteface: FilterTypeSelectionInteface? = null
    var filterInterface: filterInterface? = null
    var filterAdapter: FilterListAdapter? = null

    private var testPackagesAdapter: TestPackagesAdapter? = null
    private var mDataList: ArrayList<PackageData.PackageDataList>? = null

    var coursetypeid = "1"
    var boardid = ""
    var examids = ""
    var stdids = ""
    var subids = ""
    var tutorids = ""

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

        filterTypeArr.add("Boards")
//            filterTypeArr.add("Competitive Exams")
        filterTypeArr.add("Standard")
        filterTypeArr.add("Subjects")
        filterTypeArr.add("Tutor")
        filterTypeArr.add("Price")

        filterAdapter = FilterListAdapter(this@FilterActivity, filterTypeArr, filterTypeSelectionInteface!!)
        filter_rvList.adapter = filterAdapter
        filterAdapter!!.notifyDataSetChanged()

        var fragment = OtherFilterFragment()
        val bundle = Bundle()
        bundle.putString("type", "boards")
        bundle.putString("coursetype", coursetypeid)
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction().add(R.id.filter_container, fragment).commit()

        if (Utils.getStringValue(this@FilterActivity, AppConstants.COURSE_TYPE_ID, "") == "1") {
            filter_rbBoards.isChecked = true
        } else {
            filter_rbCompetitive.isChecked = true
        }

        filter_rgCourseType.setOnCheckedChangeListener { group, checkedId ->

            if (checkedId == R.id.filter_rbBoards) {

                filterTypeArr = ArrayList()

                coursetypeid = "1"

                Utils.setStringValue(this@FilterActivity, AppConstants.COURSE_TYPE_ID, coursetypeid)

                filterTypeArr.add("Boards")
//            filterTypeArr.add("Competitive Exams")
                filterTypeArr.add("Standard")
                filterTypeArr.add("Subjects")
                filterTypeArr.add("Tutor")
                filterTypeArr.add("Price")

                filterAdapter = FilterListAdapter(this@FilterActivity, filterTypeArr, filterTypeSelectionInteface!!)
                filter_rvList.adapter = filterAdapter
                filterAdapter!!.notifyDataSetChanged()

                var fragment = OtherFilterFragment()
                val bundle = Bundle()
                bundle.putString("type", "boards")
                bundle.putString("coursetype", coursetypeid)
                fragment.arguments = bundle
                supportFragmentManager.beginTransaction().replace(R.id.filter_container, fragment).commit()

            } else if (checkedId == R.id.filter_rbCompetitive) {

                filterTypeArr = ArrayList()

                coursetypeid = "2"

                Utils.setStringValue(this@FilterActivity, AppConstants.COURSE_TYPE_ID, coursetypeid)

                filterTypeArr.add("Competitive Exams")
//            filterTypeArr.add("Standard")
                filterTypeArr.add("Subjects")
                filterTypeArr.add("Tutor")
                filterTypeArr.add("Price")

                filterAdapter = FilterListAdapter(this@FilterActivity, filterTypeArr, filterTypeSelectionInteface!!)
                filter_rvList.adapter = filterAdapter
                filterAdapter!!.notifyDataSetChanged()

                var fragment = OtherFilterFragment()
                val bundle = Bundle()
                bundle.putString("type", "competitive_exams")
                bundle.putString("coursetype", coursetypeid)
                fragment.arguments = bundle
                supportFragmentManager.beginTransaction().replace(R.id.filter_container, fragment).commit()

            }

        }

        filter_ivBack.setOnClickListener {

            onBackPressed()

        }
    }

    override fun getType(p0: Int) {

        var fragment = OtherFilterFragment()
        val bundle = Bundle()
        bundle.putString("coursetype", coursetypeid)

        when {
//            filterTypeArr[p0] == "Course Type" -> {
//
//                bundle.putString("type", "course_type")
//                fragment.arguments = bundle
//                supportFragmentManager.beginTransaction().replace(R.id.filter_container, fragment).commit()
//            }

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

//    fun callCourseListApi(): ArrayList<FilterModel.FilterData> {
//
//        var filterArray: ArrayList<FilterModel.FilterData> = ArrayList()
//
//        if (!DialogUtils.isNetworkConnected(activity!!)) {
//            Utils.ping(activity!!, "Connetion not available")
//        }
//
//        DialogUtils.showDialog(activity!!)
//        val apiService = WebClient.getClient().create(WebInterface::class.java)
//
//        val call = apiService.getCourseList()
//        call.enqueue(object : Callback<FilterModel> {
//            override fun onResponse(call: Call<FilterModel>, response: Response<FilterModel>) {
//
//                if (response.body() != null) {
//
//                    DialogUtils.dismissDialog()
//
//                    if (response.body()!!.Status == "true") {
//
//                        filterArray = response.body()!!.data
//
//                        recyclerviewAdapter = FilterAdapter(activity!!, filterArray, "multiple", "course_type", filterInterface!!)
//                        filterData_rvList.adapter = recyclerviewAdapter
////                        filterArray!!.add("Boards")
////                        filterArray!!.add("Competitve Exams")
//
//                    } else {
//
//                        Toast.makeText(activity, response.body()!!.Msg, Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<FilterModel>, t: Throwable) {
//                // Log error here since request failed
//                Log.e("", t.toString())
//                DialogUtils.dismissDialog()
//            }
//        })
//
//        return filterArray
//    }

    override fun onBackPressed() {
        finish()
    }

}
