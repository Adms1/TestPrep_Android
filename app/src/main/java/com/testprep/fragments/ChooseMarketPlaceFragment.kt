package com.testprep.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.testprep.R
import com.testprep.adapter.MainPackageAdapter
import com.testprep.models.FilterModel
import com.testprep.models.PackageData
import com.testprep.retrofit.WebClient
import com.testprep.retrofit.WebInterface
import com.testprep.utils.AppConstants
import com.testprep.utils.DialogUtils
import com.testprep.utils.Utils
import com.testprep.utils.WebRequests
import kotlinx.android.synthetic.main.fragment_choose_market_place.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChooseMarketPlaceFragment : Fragment() {

    private var mDataList: ArrayList<PackageData.PackageDataList>? = null
    private var testPackagesAdapter: MainPackageAdapter? = null

    var dialogArray: ArrayList<String> = ArrayList()
    var tutorArr: ArrayList<FilterModel.FilterData> = ArrayList()
    var subjectArr: ArrayList<FilterModel.FilterData> = ArrayList()
    var standardArr: ArrayList<FilterModel.FilterData> = ArrayList()
    var examArr: ArrayList<FilterModel.FilterData> = ArrayList()

    var tutorids = ""
    var subids = ""
    var stdids = ""
    var examids = ""

    private var myViewPagerAdapter: MyViewPagerAdapter? = null
    private var layouts: IntArray? = null

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {

        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {

//            callFilterListApi()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choose_market_place, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        AppConstants.ON_BACK = 1

        layouts = intArrayOf(R.drawable.result_bg, R.drawable.result_bg, R.drawable.result_bg)

        myViewPagerAdapter = MyViewPagerAdapter()
        choosemp_view_pager.adapter = myViewPagerAdapter
        choosemp_view_pager.addOnPageChangeListener(introViewPagerListener)

//        choosemp_ivFilter.setOnClickListener {
//            callFilterListApi()
//        }
//
//        if (Utils.getStringValue(context as DashboardActivity, AppConstants.COURSE_TYPE_ID, "") == "1") {
//
//            choosemp_tvStandard.text = "Standard"
//
//            rlOne.visibility = View.VISIBLE
////            rlThree.visibility = View.VISIBLE
//            rlFour.visibility = View.GONE
////            llFourth.visibility = View.GONE
//
//        } else {
//
//            choosemp_tvStandard.text = "Exam"
//
//            rlOne.visibility = View.GONE
////            llSecond.visibility = View.GONE
//            rlFour.visibility = View.VISIBLE
////            llFourth.visibility = View.VISIBLE
//
//        }

        choosemp_rvList.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        choosemp_rvList.isNestedScrollingEnabled = false

//        callPackageListApi()
        callFilterListApi()
    }

    private fun textboxInit(): TextView {
        return TextView(activity)
    }

    private var introViewPagerListener: ViewPager.OnPageChangeListener = object : ViewPager.OnPageChangeListener {

        override fun onPageSelected(position: Int) {
//            addBottomDots(position)
            /*Based on the page position change the button text*/

        }

        override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {
            //Do nothing for now
        }

        override fun onPageScrollStateChanged(arg0: Int) {
            //Do nothing for now
        }
    }

    inner class MyViewPagerAdapter : PagerAdapter() {
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val layoutInflater: LayoutInflater = LayoutInflater.from(activity)
            val view = layoutInflater.inflate(R.layout.slider_item_layout, container, false)

            var iv: ImageView = view.findViewById(R.id.imageView)

            iv.setImageResource(layouts!![position])

            container.addView(view)
            return view
        }

        override fun getCount(): Int {
            return 3
        }

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view === obj
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val view = `object` as View
            container.removeView(view)
        }

//        override fun getPageWidth(position: Int): Float {
//            return 0.6f
//        }

    }

//    private fun getItem(i: Int): Int {
//        return intro_view_pager.currentItem + i
//    }


//    fun filterClick(
//        selectionType: String,
//        selection: String,
//        arr: ArrayList<FilterModel.FilterData>,
//        view: TextView,
//        filterType: String
//    ) {
//
//        val dialog = Dialog(context!!)
//        dialog.setContentView(R.layout.dialog_filter_selection)
//
////        val mModelList: ArrayList<FilterModel.FilterData> = ArrayList()
////        for (i in 0 until arr.size) {
////            val model = TutorModel.TutorData()
////            model.TutorName = arr[i].TutorName
////            model.isSelected = false
////            mModelList.add(model)
////        }
//
//        val list: RecyclerView = dialog.findViewById(R.id.recycler_view)
//        val btnDone: Button = dialog.findViewById(R.id.btnDone)
//        val btnCancel: Button = dialog.findViewById(R.id.btnCancel)
//        val header: TextView = dialog.findViewById(R.id.filterSelection)
//
//        header.text = selection
//
//        list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//        val recyclerviewAdapter = RecyclerviewAdapter(activity!!, arr, selectionType, filterType)
//        list.adapter = recyclerviewAdapter
//
//        btnDone.setOnClickListener {
//
//            var str = ""
//
//            if (selectionType == "multiple") {
//                val finalFilerArray = recyclerviewAdapter.sendArray()
//
//                Log.d("filterarraysize", "" + finalFilerArray.size)
//
//                when (filterType) {
//                    "standard" -> {
//                        for (i in 0 until finalFilerArray.size) {
//                            if (finalFilerArray[i].isSelected) {
//
//                                str += finalFilerArray[i].StandardName + ","
//                                stdids += finalFilerArray[i].StandardID + ","
//                            }
//                        }
//
//                        stdids = stdids.substring(0, stdids.length - 1)
//                    }
//                    "subject" -> {
//
//                        subids = ""
//
//                        for (i in 0 until finalFilerArray.size) {
//                            if (finalFilerArray[i].isSelected) {
//
//                                str += finalFilerArray[i].SubjectName + ","
//                                subids += finalFilerArray[i].SubjectID + ","
//                            }
//                        }
//
//                        if (subids != "") {
//                            subids = subids.substring(0, subids.length - 1)
//                        }
//                        Log.d("subids", "" + subids)
//                    }
//                    "tutor" -> {
//                        for (i in 0 until finalFilerArray.size) {
//                            if (finalFilerArray[i].isSelected) {
//
//                                str += finalFilerArray[i].TutorName + ","
//                                tutorids += finalFilerArray[i].TutorID + ","
//                            }
//                        }
//
//                        tutorids = tutorids.substring(0, tutorids.length - 1)
//                    }
//                    "exam" -> {
//                        for (i in 0 until finalFilerArray.size) {
//                            if (finalFilerArray[i].isSelected) {
//
//                                str += finalFilerArray[i].CourseName + ","
//                                examids += finalFilerArray[i].CourseID + ","
//                            }
//                        }
//                        examids = examids.substring(0, examids.length - 1)
//                    }
//                }
//
//                if (str != null && str != "") {
//                    str = str.substring(0, str.length - 1)
//
//                    Log.d("filterarraytxt", "" + str)
//                    dialog.dismiss()
//                } else {
//                    Utils.ping(activity!!, "You have to select atleast one")
//                }
//
//            } else {
//
//                str = recyclerviewAdapter.sendStandard()
//
//                if (str != null && str != "") {
//
//                    dialog.dismiss()
//                } else {
//                    Utils.ping(activity!!, "Please select any one")
//                }
//            }
//
//            view.text = str.substringAfter("-")
//
//            when (view) {
//                choosemp_filterTutors -> tutorids = str.substringBefore("-")
//                choosemp_filterStandard -> stdids = str.substringBefore("-")
//                choosemp_filterExam -> examids = str.substringBefore("-")
//                choosemp_filterSubject -> {
//                }
//            }
//
//        }
//
//        btnCancel.setOnClickListener { dialog.dismiss() }
//
//        dialog.show()
//
//    }


//    fun callPackageListApi() {
//
//        if (!DialogUtils.isNetworkConnected(activity!!)) {
//            Utils.ping(activity!!, "Connetion not available")
//        }
//
//        DialogUtils.showDialog(activity!!)
//        val apiService = WebClient.getClient().create(WebInterface::class.java)
//
////        val call = apiService.getPackage(
////            WebRequests.getPackageParams(
////                Utils.getStringValue(activity!!, AppConstants.COURSE_TYPE_ID, "0")!!,
////                Utils.getStringValue(activity!!, "course_id", "0")!!,
////                Utils.getStringValue(activity!!, "std_id", "0")!!,
////                "9"
////            )
////        )
//
//        val call = apiService.getPackage(
//            WebRequests.getPackageParams(
////                Utils.getStringValue(activity, AppConstants.COURSE_TYPE_ID, "0")!!,
////                Utils.getStringValue(activity, "course_id", "0")!!,
////                Utils.getStringValue(activity, "std_id", "0")!!,
////                intent.getStringExtra("subject_id")
//
//                Utils.getStringValue(activity!!, AppConstants.COURSE_TYPE_ID, "")!!,
//                Utils.getStringValue(activity!!, "course_id", "")!!,
//                Utils.getStringValue(activity!!, AppConstants.STANDARD_ID, "")!!,
//                Utils.getStringValue(activity!!, "subject_id", "")!!
//            )
//        )
//
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
//                        choosemp_filterExam.text = Utils.getStringValue(activity!!, "course_name", "")!!
//                        choosemp_filterStandard.text = Utils.getStringValue(activity!!, "standard_name", "")!!
//                        choosemp_filterSubject.text = Utils.getStringValue(activity!!, "subject_name", "")!!
//                        choosemp_filterTutors.text = "All"
//
//                        testPackagesAdapter = TestPackagesAdapter(activity!!, mDataList!!)
//                        choosemp_rvList.adapter = testPackagesAdapter
//
//                    } else {
//
//                        Toast.makeText(activity!!, response.body()!!.Msg, Toast.LENGTH_SHORT).show()
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

    fun callFilterListApi() {

        if (!DialogUtils.isNetworkConnected(activity!!)) {
            Utils.ping(activity!!, "Connetion not available")
        }

        DialogUtils.showDialog(activity!!)
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getFilterData(
            WebRequests.getFilterParams(
                Utils.getStringValue(activity!!, AppConstants.COURSE_TYPE_ID, "")!!,
                "",
                Utils.getStringValue(activity!!, AppConstants.COURSE_ID, "")!!,
                Utils.getStringValue(activity!!, AppConstants.STANDARD_ID, "")!!,
                Utils.getStringValue(activity!!, AppConstants.SUBJECT_ID, "")!!,
                Utils.getStringValue(activity!!, AppConstants.TUTOR_ID, "")!!,
                "",
                ""
            )
        )

//        val call = apiService.getFilterData(
//            WebRequests.getFilterParams(
//                Utils.getStringValue(activity!!, AppConstants.COURSE_TYPE_ID, "")!!,
//                "",
//                examids,
//                stdids,
//                subids,
//                tutorids,
//                "",
//                ""
//            )
//        )

        call.enqueue(object : Callback<PackageData> {
            override fun onResponse(call: Call<PackageData>, response: Response<PackageData>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.Status == "true") {

                        mDataList = response.body()!!.data

                        choosemp_rvList.visibility = View.VISIBLE
                        testPackagesAdapter = MainPackageAdapter(activity!!, mDataList!!)
                        choosemp_rvList.adapter = testPackagesAdapter

                    } else {

                        Toast.makeText(activity!!, response.body()!!.Msg, Toast.LENGTH_SHORT).show()
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
