package com.testprep.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.testprep.activity.DashboardActivity.Companion.setFragments
import com.testprep.adapter.FilterAdapter
import com.testprep.interfaces.filterInterface
import com.testprep.models.PackageData
import com.testprep.retrofit.WebClient
import com.testprep.retrofit.WebInterface
import com.testprep.utils.AppConstants
import com.testprep.utils.DialogUtils
import com.testprep.utils.Utils
import com.testprep.utils.WebRequests
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

    var coursetypeid = ""
    var tutorids = ""
    var subids = ""
    var stdids = ""
    var examids = ""
    var max = ""
    var min = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        filter_type = arguments!!.getString("type")
        coursetypeid = arguments!!.getString("coursetype")
        // Inflate the layout for this fragment
        return inflater.inflate(com.testprep.R.layout.fragment_other_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        filterInterface = this

        filterData_rvList.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

//        price_filter_etMin.setText(Utils.getStringValue(activity!!, AppConstants.MIN_PRICE, "0"))
//        price_filter_etMax.setText(Utils.getStringValue(activity!!, AppConstants.MAX_PRICE, "0"))

        Log.d("min_price", "" + Utils.getStringValue(activity!!, AppConstants.MIN_PRICE, "0"))
        Log.d("max_price", "" + Utils.getStringValue(activity!!, AppConstants.MAX_PRICE, "0"))

        examids = if (AppConstants.FILTER_BOARD_ID == "111") {
            Utils.getStringValue(activity!!, AppConstants.COURSE_ID, "")!!
        } else {
            AppConstants.FILTER_BOARD_ID
        }

        stdids = if (AppConstants.FILTER_STANDARD_ID == "111") {
            Utils.getStringValue(activity!!, AppConstants.STANDARD_ID, "")!!
        } else {
            AppConstants.FILTER_STANDARD_ID
        }

        subids = if (AppConstants.FILTER_SUBJECT_ID == "111") {
            Utils.getStringValue(activity!!, AppConstants.SUBJECT_ID, "")!!
        } else {
            AppConstants.FILTER_SUBJECT_ID
        }

        tutorids = if (AppConstants.FILTER_TUTOR_ID == "111") {
            Utils.getStringValue(activity!!, AppConstants.TUTOR_ID, "")!!
        } else {
            AppConstants.FILTER_TUTOR_ID
        }

        // set listener
        rangeSeekbar3.setOnRangeSeekbarChangeListener { minValue, maxValue ->

            min = minValue.toString()
            max = maxValue.toString()

            price_filter_etMin.text = "₹ " + minValue.toString()
            price_filter_etMax.text = "₹ " + maxValue.toString()
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

            if (stdids != "") {
                AppConstants.FILTER_STANDARD_ID = stdids
            } else {
                AppConstants.FILTER_STANDARD_ID = ""
            }

            if (subids != "") {
                AppConstants.FILTER_SUBJECT_ID = subids
            } else {
                AppConstants.FILTER_SUBJECT_ID = ""
            }

            if (tutorids != "") {
                AppConstants.FILTER_TUTOR_ID = tutorids
            } else {
                AppConstants.FILTER_TUTOR_ID = ""
            }

            if (examids != "") {
                AppConstants.FILTER_BOARD_ID = examids
            } else {
                AppConstants.FILTER_BOARD_ID = ""
            }

            AppConstants.isFirst = 13

            val bundle = Bundle()
            bundle.putString("type", "filter")
            bundle.putString("pname1", "Packages")
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

                callStandardListApi()

            }
            "tutor" -> {

                filterData_rvList.visibility = View.VISIBLE
                price_ll.visibility = View.GONE

                callTutorListApi()

            }
            "subjects" -> {

                filterData_rvList.visibility = View.VISIBLE
                price_ll.visibility = View.GONE

                callSubjectListApi()

            }
            "price" -> {
                filterData_rvList.visibility = View.GONE
                price_ll.visibility = View.VISIBLE
            }
        }
    }

    fun callStandardListApi() {

        var filterArray: ArrayList<PackageData.PackageDataList> = ArrayList()

        if (!DialogUtils.isNetworkConnected(activity!!)) {
            Utils.ping(activity!!, "Connetion not available")
        }

        DialogUtils.showDialog(activity!!)
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getStandardList()
        call.enqueue(object : Callback<PackageData> {
            override fun onResponse(call: Call<PackageData>, response: Response<PackageData>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.Status == "true") {

                        filterArray = response.body()!!.data

                        if (AppConstants.FILTER_STANDARD_ID == "111") {
//                        }else{
                            AppConstants.FILTER_STANDARD_ID =
                                Utils.getStringValue(activity!!, AppConstants.STANDARD_ID, "")!!
//                            stdids = Utils.getStringValue(activity!!, AppConstants.STANDARD_ID, "")!!
                        }

                        val strArray = AppConstants.FILTER_STANDARD_ID

                        for (i in 0 until filterArray.size) {
                            if (filterArray[i].StandardID == strArray) {
                                filterArray[i].isSelected = true
                            }
                        }

                        recyclerviewAdapter =
                            FilterAdapter(activity!!, filterArray, "single", "standard", filterInterface!!)
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

    fun callSubjectListApi(): ArrayList<PackageData.PackageDataList> {

        var filterArray: ArrayList<PackageData.PackageDataList> = ArrayList()

        if (!DialogUtils.isNetworkConnected(activity!!)) {
            Utils.ping(activity!!, "Connetion not available")
        }

        DialogUtils.showDialog(activity!!)
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getSubjectList()
        call.enqueue(object : Callback<PackageData> {
            override fun onResponse(call: Call<PackageData>, response: Response<PackageData>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.Status == "true") {

                        filterArray = response.body()!!.data

                        if (AppConstants.FILTER_SUBJECT_ID == "111") {
//                        }else{
                            AppConstants.FILTER_SUBJECT_ID =
                                Utils.getStringValue(activity!!, AppConstants.SUBJECT_ID, "")!!
                        }

                        val strArray = AppConstants.FILTER_SUBJECT_ID.replace(" ", "").split(",")

                        for (i in 0 until filterArray.size) {
                            for (j in 0 until strArray.size) {
                                if (strArray[j] == filterArray[i].SubjectID) {
                                    filterArray[i].isSelected = true
                                }
                            }
                        }

                        recyclerviewAdapter =
                            FilterAdapter(activity!!, filterArray, "multiple", "subject", filterInterface!!)
                        filterData_rvList.adapter = recyclerviewAdapter
//                        choosemp_filterSubject.text = subjectArr[0].SubjectName
//                        subids = subjectArr[0].SubjectID

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

        return filterArray
    }

    fun callExamListApi(type: String) {

        var filterArray: ArrayList<PackageData.PackageDataList> = ArrayList()

        if (!DialogUtils.isNetworkConnected(activity!!)) {
            Utils.ping(activity!!, "Connetion not available")
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

                        if (AppConstants.FILTER_BOARD_ID == "111") {
//                        }else{
                            AppConstants.FILTER_BOARD_ID =
                                Utils.getStringValue(activity!!, AppConstants.COURSE_ID, "")!!
                        }

                        val strArray = AppConstants.FILTER_BOARD_ID.replace(" ", "").split(",")

                        for (i in 0 until filterArray.size) {
                            for (j in 0 until strArray.size) {
                                if (strArray[j] == filterArray[i].CourseID) {
                                    filterArray[i].isSelected = true
                                }
                            }
                        }

                        recyclerviewAdapter =
                            FilterAdapter(activity!!, filterArray, "single", "exam", filterInterface!!)
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
            Utils.ping(activity!!, "Connetion not available")
        }

        DialogUtils.showDialog(activity!!)
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getTutorList()
        call.enqueue(object : Callback<PackageData> {
            override fun onResponse(call: Call<PackageData>, response: Response<PackageData>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.Status == "true") {

                        filterArray = response.body()!!.data

                        if (AppConstants.FILTER_TUTOR_ID == "111") {
////                        }else{
                            AppConstants.FILTER_TUTOR_ID = Utils.getStringValue(activity!!, AppConstants.TUTOR_ID, "")!!
                        }

                        val strArray = AppConstants.FILTER_TUTOR_ID.replace(" ", "").split(",")

                        for (i in 0 until filterArray.size) {
                            for (j in 0 until strArray.size) {
                                if (strArray[j] == filterArray[i].TutorID) {
                                    filterArray[i].isSelected = true
                                }
                            }
                        }


                        recyclerviewAdapter =
                            FilterAdapter(activity!!, filterArray, "multiple", "tutor", filterInterface!!)
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

                var str = ""
                stdids = ""

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
                Log.d("stdid", "" + stdids)

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

                var str = ""
                examids = ""

                val finalFilerArray = recyclerviewAdapter!!.sendStandard()

                if (finalFilerArray != "") {
                    examids = finalFilerArray
                }

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

                AppConstants.FILTER_BOARD_ID = examids
                Log.d("examids", "" + examids)
            }
        }
    }

    fun callFilterListApi() {

        if (!DialogUtils.isNetworkConnected(activity!!)) {
            Utils.ping(activity!!, "Connetion not available")
        }

        DialogUtils.showDialog(activity!!)
        val apiService = WebClient.getClient().create(WebInterface::class.java)

//        val call = apiService.getFilterData(
//            WebRequests.getFilterParams(
//                Utils.getStringValue(activity!!, AppConstants.COURSE_TYPE_ID, "")!!,
//                "",
//                Utils.getStringValue(activity!!, AppConstants.COURSE_ID, "")!!,
//                Utils.getStringValue(activity!!, AppConstants.STANDARD_ID, "")!!,
//                Utils.getStringValue(activity!!, AppConstants.SUBJECT_ID, "")!!,
//                Utils.getStringValue(activity!!, AppConstants.TUTOR_ID, "")!!,
//                "",
//                ""
//            )
//        )

        val call = apiService.getFilterData(
            WebRequests.getFilterParams(
                Utils.getStringValue(activity!!, AppConstants.COURSE_TYPE_ID, "")!!,
                "",
                examids,
                stdids,
                subids,
                tutorids,
                "",
                "",
                "", ""
            )
//            WebRequests.getFilterParams(
//                "",
//                "",
//                "",
//                "",
//                "",
//                "",
//                "",
//                "",
//                ""
//            )
        )

        call.enqueue(object : Callback<PackageData> {
            override fun onResponse(call: Call<PackageData>, response: Response<PackageData>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.Status == "true") {

                        mDataList = response.body()!!.data

//                        val intent = Intent(context, TutorDetailActivity::class.java)
//                        intent.putExtra("type", "pkg")
//                        intent.putExtra("pname", "Packages")
//                        intent.putExtra("parr", mDataList)
//                        activity!!.setResult(101, intent)
//                        activity!!.finish()


                    } else {

                        Toast.makeText(activity!!, response.body()!!.Msg, Toast.LENGTH_SHORT).show()
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
