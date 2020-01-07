package com.testcraft.testcraft.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.testcraft.testcraft.R
import com.testcraft.testcraft.activity.DashboardActivity.Companion.setFragments
import com.testcraft.testcraft.adapter.FilterAdapter
import com.testcraft.testcraft.interfaces.filterInterface
import com.testcraft.testcraft.models.PackageData
import com.testcraft.testcraft.retrofit.WebClient
import com.testcraft.testcraft.retrofit.WebInterface
import com.testcraft.testcraft.utils.AppConstants
import com.testcraft.testcraft.utils.DialogUtils
import com.testcraft.testcraft.utils.Utils
import com.testcraft.testcraft.utils.WebRequests
import kotlinx.android.synthetic.main.fragment_other_filter.*
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
class OtherFilterFragment : Fragment(), filterInterface {

    var filter_type = ""
    var finalfilterArray: ArrayList<PackageData.PackageDataList>? = null
    var recyclerviewAdapter: FilterAdapter? = null

    private var mDataList: ArrayList<PackageData.PackageDataList>? = null

    var filterInterface: filterInterface? = null

    var filtertypeid = ""
    var coursetypeid = ""
    var tutorids = ""
    var subids = ""
    var stdids = ""
    var max = ""
    var min = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        filter_type = arguments!!.getString("type")
        coursetypeid = arguments!!.getString("coursetype")
        filtertypeid = arguments!!.getString("filtertypeid")
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_other_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        filterInterface = this

        price_filter_etMin.text = "₹ " + AppConstants.FILTER_FROM_PRICE
        price_filter_etMax.text = "₹ " + AppConstants.FILTER_TO_PRICE

//        if (min == "0" && max == "5000") {
////        price_filter_tvMin.text = AppConstants.FILTER_FROM_PRICE
//            price_filter_tvMin.text = "0"
////        price_filter_tvMax.text = AppConstants.FILTER_TO_PRICE
//            price_filter_tvMax.text = "144"
//        }

        filterData_rvList.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        if (AppConstants.FILTER_BOARD_ID == "0") {
//           examids = Utils.getStringValue(activity!!, AppConstants.COURSE_ID, "")!!
//            AppConstants.FILTER_BOARD_ID = Utils.getStringValue(activity!!, AppConstants.COURSE_ID, "")!!
        } else {
//           examids = AppConstants.FILTER_BOARD_ID
        }

        if (AppConstants.FILTER_STANDARD_ID == "0") {
//            stdids = Utils.getStringValue(activity!!, AppConstants.STANDARD_ID, "")!!
//            AppConstants.FILTER_STANDARD_ID = Utils.getStringValue(activity!!, AppConstants.STANDARD_ID, "")!!
        }
//        else if (AppConstants.FILTER_STANDARD_ID == ""){
//            stdids = ""
//        }else{
//            stdids = AppConstants.FILTER_STANDARD_ID
//        }

        if (AppConstants.FILTER_SUBJECT_ID == "0") {
            subids = Utils.getStringValue(activity!!, AppConstants.SUBJECT_ID, "")!!
//            AppConstants.FILTER_SUBJECT_ID = Utils.getStringValue(activity!!, AppConstants.SUBJECT_ID, "")!!
        } else {
            subids = AppConstants.FILTER_SUBJECT_ID
        }

        if (AppConstants.FILTER_TUTOR_ID == "0") {
//            tutorids = Utils.getStringValue(activity!!, AppConstants.TUTOR_ID, "")!!
            AppConstants.FILTER_TUTOR_ID =
                Utils.getStringValue(activity!!, AppConstants.TUTOR_ID, "")!!
        } else {
            tutorids = AppConstants.FILTER_TUTOR_ID
        }

        // set listener

        var isFirst = true

        rangeSeekbar3.setOnRangeSeekbarChangeListener { minValue, maxValue ->

            min = minValue.toString()
            max = maxValue.toString()

            if (isFirst) {
                price_filter_etMin.text = "₹ " + AppConstants.FILTER_FROM_PRICE
                price_filter_etMax.text = "₹ " + AppConstants.FILTER_TO_PRICE

                isFirst = false

            } else {
                price_filter_etMin.text = "₹ $minValue"
                price_filter_etMax.text = "₹ $maxValue"
            }

//            if (min != "0" && max != "5000") {
//////        price_filter_tvMin.text = AppConstants.FILTER_FROM_PRICE
////
//////                rangeSeekbar3.setMinValue(AppConstants.FILTER_FROM_PRICE.toFloat())
//////                rangeSeekbar3.setMaxValue(AppConstants.FILTER_TO_PRICE.toFloat())
////
//                price_filter_etMin.text = "₹ " + AppConstants.FILTER_FROM_PRICE
//                price_filter_etMax.text = "₹ " + AppConstants.FILTER_TO_PRICE
//
//                min = "0"
//                max = "5000"
//
//            } else {
//                price_filter_etMin.text = "₹ " + minValue.toString()
//                price_filter_etMax.text = "₹ " + maxValue.toString()
//            }

        }

//        price_filter_range_slider.min = Utils.getStringValue(activity!!, AppConstants.MIN_PRICE, "0")!!.toInt()
//        price_filter_range_slider.max = Utils.getStringValue(activity!!, AppConstants.MAX_PRICE, "0")!!.toInt()

//        price_filter_range_slider.setOnThumbValueChangeListener(object : MultiSlider.SimpleChangeListener() {
//            override fun onValueChanged(
//                multiSlider: MultiSlider?,
//                thumb: MultiSlider.Thumb?,
//                thumbIndex: Int,
//                value: Int
//            ) {
//                if (thumbIndex == 0) {
//
//                    price_filter_etMin.setText("₹ " + value.toString())
//
//                } else {
//
//                    price_filter_etMax.setText("₹ " + value.toString())
//
//                }
//            }
//        })

        filter_btnApply.setOnClickListener {

            Log.d("min_price", "" + Utils.getStringValue(activity!!, AppConstants.MIN_PRICE, "0"))
            Log.d("max_price", "" + Utils.getStringValue(activity!!, AppConstants.MAX_PRICE, "0"))

//            if (AppConstants.FILTER_COURSE_TYPE_ID == "1") {
//
//                if (stdids != "") {
//                    AppConstants.FILTER_STANDARD_ID = stdids
//                } else {
//                    AppConstants.FILTER_STANDARD_ID = ""
//                }
//
//                if (subids != "") {
//                    AppConstants.FILTER_SUBJECT_ID = subids
//                } else {
//                    AppConstants.FILTER_SUBJECT_ID = ""
//                }
//            }
//
//            if (tutorids != "") {
//                AppConstants.FILTER_TUTOR_ID = tutorids
//            } else {
//                AppConstants.FILTER_TUTOR_ID = ""
//            }

//            if (examids != "") {
//                AppConstants.FILTER_BOARD_ID = examids
//            } else {
//                AppConstants.FILTER_BOARD_ID = ""
//            }

            AppConstants.isFirst = 13

            AppConstants.FILTER_FROM_PRICE = min
            AppConstants.FILTER_TO_PRICE = max

            if (AppConstants.FILTER_COURSE_TYPE_ID == "1") {

                if (AppConstants.FILTER_BOARD_ID != "" && AppConstants.FILTER_BOARD_ID != "0") {

                    if (AppConstants.FILTER_STANDARD_ID != "" && AppConstants.FILTER_STANDARD_ID != "0") {

                        val bundle = Bundle()
                        bundle.putString("type", "filter")
                        if (filtertypeid == "1") {
                            bundle.putString("pname1", "Packages")
                        } else {
                            bundle.putString("pname1", "Single Test")
                        }
                        bundle.putString("filtertypeid", filtertypeid)
                        bundle.putString("course_type", AppConstants.FILTER_COURSE_TYPE_ID)
                        bundle.putString("boardid", AppConstants.FILTER_BOARD_ID)
                        bundle.putString("stdid", AppConstants.FILTER_STANDARD_ID)
                        bundle.putString("subid", AppConstants.FILTER_SUBJECT_ID)
                        bundle.putString("tutorid", AppConstants.FILTER_TUTOR_ID)
                        bundle.putString("search_name", "")
                        bundle.putString("maxprice", max)
                        bundle.putString("minprice", min)
                        setFragments(bundle)
                        activity!!.finish()

                    } else {
                        Toast.makeText(activity, "Please Select Standard", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(activity, "Please Select Board", Toast.LENGTH_SHORT).show()
                }

            } else {
                if (AppConstants.FILTER_BOARD_ID != "" && AppConstants.FILTER_STANDARD_ID != "0") {
                    val bundle = Bundle()
                    bundle.putString("type", "filter")
                    if (filtertypeid == "1") {
                        bundle.putString("pname1", "Packages")
                    } else {
                        bundle.putString("pname1", "Single Test")
                    }
                    bundle.putString("filtertypeid", filtertypeid)
                    bundle.putString("course_type", AppConstants.FILTER_COURSE_TYPE_ID)
                    bundle.putString("boardid", AppConstants.FILTER_BOARD_ID)
                    bundle.putString("stdid", AppConstants.FILTER_STANDARD_ID)
                    bundle.putString("subid", AppConstants.FILTER_SUBJECT_ID)
                    bundle.putString("tutorid", AppConstants.FILTER_TUTOR_ID)
                    bundle.putString("search_name", "")
                    bundle.putString("maxprice", max)
                    bundle.putString("minprice", min)
                    setFragments(bundle)
                    activity!!.finish()

                } else {
                    Toast.makeText(activity, "Please Select Course", Toast.LENGTH_SHORT).show()
                }

            }
//            val intent = Intent(context, TutorDetailActivity::class.java)
//            intent.putExtra("type", "filter")
//            intent.putExtra("pname", "Packages")
//
//            intent.putExtra("minprice", price_filter_etMin.text.toString())
//            intent.putExtra("maxprice", price_filter_etMax.text.toString())
//
//            activity!!.setResult(101, intent)
//            activity!!.finish()

//            callFilterListApi()
//            callFilterListApi()
        }

//        callCourseListApi()

        when (filter_type) {
//            "course_type" -> {
//
//                callCourseListApi()
//
//            }
            "boards" -> {

                filterData_rvList.visibility = View.VISIBLE
                price_ll.visibility = View.GONE

                callExamListApi("1")

            }
            "competitive_exams" -> {

                filterData_rvList.visibility = View.VISIBLE
                price_ll.visibility = View.GONE

                callExamListApi("2")

            }
            "standards" -> {

                filterData_rvList.visibility = View.VISIBLE
                price_ll.visibility = View.GONE

                if (AppConstants.FILTER_BOARD_ID != "") {
                    callStandardListApi()
                } else {
                    Toast.makeText(activity, "Please Select Board", Toast.LENGTH_SHORT).show()
                }

            }
            "tutor" -> {

                filterData_rvList.visibility = View.VISIBLE
                price_ll.visibility = View.GONE

                callTutorListApi()

            }
            "subjects" -> {

                filterData_rvList.visibility = View.VISIBLE
                price_ll.visibility = View.GONE

                if (AppConstants.FILTER_BOARD_ID != "") {
                    if (AppConstants.FILTER_STANDARD_ID != "") {
                        callSubjectListApi()
                    } else {
                        Toast.makeText(activity, "Please Select Standard", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(activity, "Please Select Board", Toast.LENGTH_SHORT).show()
                }

            }
            "price" -> {

                rangeSeekbar3.setMinValue(0f)
                rangeSeekbar3.setMaxValue(5000f)

                filterData_rvList.visibility = View.GONE
                price_ll.visibility = View.VISIBLE

            }
        }
    }

    fun callStandardListApi() {

        var filterArray: ArrayList<PackageData.PackageDataList> = ArrayList()

        if (!DialogUtils.isNetworkConnected(activity!!)) {
            Utils.ping(activity!!, AppConstants.NETWORK_MSG)
        }

        DialogUtils.showDialog(activity!!)
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getBoardStandardList(AppConstants.FILTER_BOARD_ID)
        call.enqueue(object : Callback<PackageData> {
            override fun onResponse(call: Call<PackageData>, response: Response<PackageData>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.Status == "true") {

                        filterArray = response.body()!!.data

                        for (i in 0 until filterArray.size) {
                            if (filterArray[i].StandardID == AppConstants.FILTER_STANDARD_ID) {
                                filterArray[i].isSelected = true

                            }
                        }

//                        AppConstants.FILTER_STANDARD_ID = ""

                        recyclerviewAdapter =
                            FilterAdapter(
                                activity!!,
                                filterArray,
                                "single",
                                "standard",
                                filterInterface!!
                            )
                        filterData_rvList.adapter = recyclerviewAdapter

                    } else {

                        Toast.makeText(activity, "Please Select Board", Toast.LENGTH_SHORT).show()
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

    fun callSubjectListApi(): ArrayList<PackageData.PackageDataList> {

        var filterArray: ArrayList<PackageData.PackageDataList> = ArrayList()

        if (!DialogUtils.isNetworkConnected(activity!!)) {
            Utils.ping(activity!!, AppConstants.NETWORK_MSG)
        }

        DialogUtils.showDialog(activity!!)
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getBoardStandardSubjectList(
            AppConstants.FILTER_BOARD_ID,
            AppConstants.FILTER_STANDARD_ID
        )
        call.enqueue(object : Callback<PackageData> {
            override fun onResponse(call: Call<PackageData>, response: Response<PackageData>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.Status == "true") {

                        filterArray = response.body()!!.data

//                        if (AppConstants.FILTER_SUBJECT_ID == "") {
////                        }else{
//                            AppConstants.FILTER_SUBJECT_ID =
//                                Utils.getStringValue(activity!!, AppConstants.SUBJECT_ID, "")!!
//                        }

                        val strArray = AppConstants.FILTER_SUBJECT_ID.replace(" ", "").split(",")

                        for (i in 0 until filterArray.size) {
                            for (j in 0 until strArray.size) {
                                if (strArray[j] == filterArray[i].SubjectID) {
                                    filterArray[i].isSelected = true
                                }
                            }
                        }

//                        AppConstants.FILTER_SUBJECT_ID = "0"

                        recyclerviewAdapter =
                            FilterAdapter(
                                activity!!,
                                filterArray,
                                "multiple",
                                "subject",
                                filterInterface!!
                            )
                        filterData_rvList.adapter = recyclerviewAdapter
//                        choosemp_filterSubject.text = subjectArr[0].SubjectName
//                        subids = subjectArr[0].SubjectID

                    } else {

                        AppConstants.FILTER_SUBJECT_ID = ""

                        if (AppConstants.FILTER_BOARD_ID == "" && AppConstants.FILTER_BOARD_ID == "0") {
                            Toast.makeText(activity, "Please select Board", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            Toast.makeText(activity, "Please select standard", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<PackageData>, t: Throwable) {
                // Log error here since request failed
                Log.e("", t.toString())
                DialogUtils.dismissDialog()
            }
        })

        return filterArray
    }

    fun callExamListApi(type: String) {

        var filterArray: ArrayList<PackageData.PackageDataList> = ArrayList()

        if (!DialogUtils.isNetworkConnected(activity!!)) {
            Utils.ping(activity!!, AppConstants.NETWORK_MSG)
        }

        DialogUtils.showDialog(activity!!)
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getExamList(type)
        call.enqueue(object : Callback<PackageData> {
            override fun onResponse(call: Call<PackageData>, response: Response<PackageData>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.Status == "true") {

                        filterArray = response.body()!!.data

//                        if (AppConstants.FILTER_BOARD_ID == "") {
//                            AppConstants.FILTER_BOARD_ID =
//                                Utils.getStringValue(activity!!, AppConstants.COURSE_ID, "")!!
//                        }

                        for (i in 0 until filterArray.size) {
                            if (filterArray[i].CourseID == AppConstants.FILTER_BOARD_ID) {
                                filterArray[i].isSelected = true
                            }
                        }

                        recyclerviewAdapter =
                            FilterAdapter(
                                activity!!,
                                filterArray,
                                "single",
                                "exam",
                                filterInterface!!
                            )
                        filterData_rvList.adapter = recyclerviewAdapter

                    } else {

                        Toast.makeText(activity, response.body()!!.Msg, Toast.LENGTH_SHORT).show()
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

    fun callTutorListApi() {

        var filterArray: ArrayList<PackageData.PackageDataList> = ArrayList()

        if (!DialogUtils.isNetworkConnected(activity!!)) {
            Utils.ping(activity!!, AppConstants.NETWORK_MSG)
        }

        DialogUtils.showDialog(activity!!)
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        var fil_courseid = ""
        var fil_boardid = ""

        if (AppConstants.FILTER_COURSE_TYPE_ID == "1") {

            fil_courseid = ""
            fil_boardid = AppConstants.FILTER_BOARD_ID

        } else {

            fil_courseid = AppConstants.FILTER_BOARD_ID
            fil_boardid = ""

        }

        val call = apiService.getTutorFilterName(
            WebRequests.getTutorFiltername(
                AppConstants.FILTER_COURSE_TYPE_ID,
                fil_boardid,
                fil_courseid,
                AppConstants.FILTER_STANDARD_ID,
                AppConstants.FILTER_SUBJECT_ID

            )
        )
        call.enqueue(object : Callback<PackageData> {
            override fun onResponse(call: Call<PackageData>, response: Response<PackageData>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.Status == "true") {

                        filterArray = response.body()!!.data

//                        if (AppConstants.FILTER_TUTOR_ID == "111") {
////                        }else{
//                            AppConstants.FILTER_TUTOR_ID = Utils.getStringValue(activity!!, AppConstants.TUTOR_ID, "")!!
//                        }

                        val strArray = AppConstants.FILTER_TUTOR_ID.replace(" ", "").split(",")

                        for (i in 0 until filterArray.size) {
                            for (j in 0 until strArray.size) {
                                if (strArray[j] == filterArray[i].TutorID) {
                                    filterArray[i].isSelected = true
                                }
                            }
                        }


                        recyclerviewAdapter =
                            FilterAdapter(
                                activity!!,
                                filterArray,
                                "multiple",
                                "tutor",
                                filterInterface!!
                            )
                        filterData_rvList.adapter = recyclerviewAdapter

                    } else {

                        Toast.makeText(activity, response.body()!!.Msg, Toast.LENGTH_SHORT).show()
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

    override fun filterData(filterType: String) {

        when (filterType) {
            "standard" -> {

                val finalFilerArray = recyclerviewAdapter!!.sendStandard()

//                for (i in 0 until finalFilerArray.size) {
//                    if (finalFilerArray[i].isSelected) {
//
//                        str += finalFilerArray[i].StandardName + ","
//                        stdids += finalFilerArray[i].StandardID + ","
//                    }
//                }
//
//                if (stdids != "") {
//                    stdids = stdids.substring(0, stdids.length - 1)
//                }

                if (finalFilerArray != "") {
                    stdids = finalFilerArray
                }

                AppConstants.FILTER_STANDARD_ID = stdids

                AppConstants.FILTER_SUBJECT_ID = ""
                AppConstants.FILTER_TUTOR_ID = ""
//                Log.d("stdid", "" + stdids)

            }
            "subject" -> {

                var str = ""
                subids = ""

                val finalFilerArray = recyclerviewAdapter!!.sendArray()

                for (i in 0 until finalFilerArray.size) {
                    if (finalFilerArray[i].isSelected) {

                        str += finalFilerArray[i].SubjectName + ","
                        subids += finalFilerArray[i].SubjectID + ","
                    }
                }

                if (subids != "") {
                    subids = subids.substring(0, subids.length - 1)
                }

                AppConstants.FILTER_SUBJECT_ID = subids
                AppConstants.FILTER_TUTOR_ID = ""
                Log.d("subids", "" + subids)

            }
            "tutor" -> {

                var str = ""
                tutorids = ""

                val finalFilerArray = recyclerviewAdapter!!.sendArray()

                for (i in 0 until finalFilerArray.size) {
                    if (finalFilerArray[i].isSelected) {

                        str += finalFilerArray[i].TutorName + ","
                        tutorids += finalFilerArray[i].TutorID + ","
                    }
                }

                if (tutorids != "") {
                    tutorids = tutorids.substring(0, tutorids.length - 1)
                }

                AppConstants.FILTER_TUTOR_ID = tutorids
                Log.d("tutorids", "" + tutorids)

            }
            "exam" -> {

                val finalFilerArray = recyclerviewAdapter!!.sendStandard()

                if (finalFilerArray != "") {
                    AppConstants.FILTER_BOARD_ID = finalFilerArray
                }

                AppConstants.FILTER_STANDARD_ID = ""
                AppConstants.FILTER_SUBJECT_ID = ""

//                for (i in 0 until finalFilerArray.size) {
//                    if (finalFilerArray[i].isSelected) {
//
//                        str += finalFilerArray[i].CourseName + ","
//                        examids += finalFilerArray[i].CourseID + ","
//                    }
//                }
//
//                if (examids != "") {
//                    examids = examids.substring(0, examids.length - 1)
//                }

//                Log.d("examids", "" + examids)
            }
        }
    }

//    fun callFilterListApi() {
//
//        if (!DialogUtils.isNetworkConnected(activity!!)) {
//            Utils.ping(activity!!, AppConstants.NETWORK_MSG)
//        }
//
//        DialogUtils.showDialog(activity!!)
//        val apiService = WebClient.getClient().create(WebInterface::class.java)
//
////        val call = apiService.getFilterData(
////            WebRequests.getFilterParams(
////                Utils.getStringValue(activity!!, AppConstants.COURSE_TYPE_ID, "")!!,
////                "",
////                Utils.getStringValue(activity!!, AppConstants.COURSE_ID, "")!!,
////                Utils.getStringValue(activity!!, AppConstants.STANDARD_ID, "")!!,
////                Utils.getStringValue(activity!!, AppConstants.SUBJECT_ID, "")!!,
////                Utils.getStringValue(activity!!, AppConstants.TUTOR_ID, "")!!,
////                "",
////                ""
////            )
////        )
//
//        val call = apiService.getFilterData(
//            WebRequests.getFilterParams(
//                Utils.getStringValue(activity!!, AppConstants.COURSE_TYPE_ID, "")!!,
//                "",
//                examids,
//                stdids,
//                subids,
//                tutorids,
//                "",
//                "",
//                "", ""
//            )
////            WebRequests.getFilterParams(
////                "",
////                "",
////                "",
////                "",
////                "",
////                "",
////                "",
////                "",
////                ""
////            )
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
////                        val intent = Intent(context, TutorDetailActivity::class.java)
////                        intent.putExtra("type", "pkg")
////                        intent.putExtra("pname", "Packages")
////                        intent.putExtra("parr", mDataList)
////                        activity!!.setResult(101, intent)
////                        activity!!.finish()
//
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

}
