package com.testprep.fragments

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.testprep.R
import com.testprep.adapter.RecyclerviewAdapter
import com.testprep.adapter.TestPackagesAdapter
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
    private var testPackagesAdapter: TestPackagesAdapter? = null

    var dialogArray: ArrayList<String> = ArrayList()
    var tutorArr: ArrayList<FilterModel.FilterData> = ArrayList()
    var subjectArr: ArrayList<FilterModel.FilterData> = ArrayList()
    var standardArr: ArrayList<FilterModel.FilterData> = ArrayList()
    var examArr: ArrayList<FilterModel.FilterData> = ArrayList()

    var tutorids = ""
    var subids = ""
    var stdids = "0"
    var examids = "0"

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
        var fragment = MarketPlaceFragment()

//        choosemp_tvSingleTest.setOnClickListener {
//
//            val bundle = Bundle()
//            bundle.putString("tab", "Single Test")
//            fragment.arguments = bundle
//            fragmentManager!!.beginTransaction().replace(R.id.container, fragment).addToBackStack(null)
//                .commit()
//
//        }
//
//        choosemp_tvTestPackages.setOnClickListener {
//            val bundle = Bundle()
//            bundle.putString("tab", "Test Packages")
//            fragment.arguments = bundle
//            fragmentManager!!.beginTransaction().replace(R.id.container, fragment).addToBackStack(null)
//                .commit()
//
////            val intent = Intent(activity, SelectPackageActivity::class.java)
////            startActivity(intent)
//        }

        choosemp_ivFilter.setOnClickListener {
            callFilterListApi()
        }

//        choosemp_tvTutors.setOnClickListener {
//            val bundle = Bundle()
//            bundle.putString("tab", "Tutors")
//            fragment.arguments = bundle
//            fragmentManager!!.beginTransaction().replace(R.id.container, fragment).addToBackStack(null)
//                .commit()
//        }

        if (Utils.getStringValue(activity!!, "course_type_id", "") == "1") {

            choosemp_tvStandard.text = "Standard"

            rlOne.visibility = View.VISIBLE
//            rlThree.visibility = View.VISIBLE
            rlFour.visibility = View.GONE
//            llFourth.visibility = View.GONE

        } else {

            choosemp_tvStandard.text = "Exam"

            rlOne.visibility = View.GONE
//            llSecond.visibility = View.GONE
            rlFour.visibility = View.VISIBLE
//            llFourth.visibility = View.VISIBLE

        }


        rlOne.setOnClickListener {

            //            dialogArray = ArrayList()
//            dialogArray.add("9th")
//            dialogArray.add("10th")
//            dialogArray.add("11th")
//            dialogArray.add("12th")

            filterClick("single", "Select Standard", standardArr, choosemp_filterStandard, "standard")
        }

        rlTwo.setOnClickListener {

            //            dialogArray = ArrayList()
//            dialogArray.add("Social Science")
//            dialogArray.add("Science")
//            dialogArray.add("Mathematics")
//            dialogArray.add("Hindi")
//            dialogArray.add("English")

//            filterClick("multiple", "Select Subject", dialogArray, choosemp_filterSubject)
            filterClick("multiple", "Select Subjects", subjectArr, choosemp_filterSubject, "subject")
        }

        rlThree.setOnClickListener {

            //            dialogArray = ArrayList()
//            dialogArray.add("ADMS")
//            dialogArray.add("Navin Goradara")
//            dialogArray.add("Hemang Mehta")
//            dialogArray.add("Bothra Classes")

            filterClick("single", "Select Tutors", tutorArr, choosemp_filterTutors, "tutor")
        }

        rlFour.setOnClickListener {

            //            dialogArray = ArrayList()
//            dialogArray.add("JEE IIT")
//            dialogArray.add("NEET")
//            dialogArray.add("CBSE")
//            dialogArray.add("GSEB")

            filterClick("single", "Select Exam", examArr, choosemp_filterExam, "exam")
        }

//        rlFive.setOnClickListener {
//
//            dialogArray = ArrayList()
//            dialogArray.add("Social Science")
//            dialogArray.add("Science")
//            dialogArray.add("Mathematics")
//            dialogArray.add("Hindi")
//            dialogArray.add("English")
//
////            filterClick("multiple", "Select Subject", dialogArray, choosemp_filtereSubject)
//        }
//
//        rlSix.setOnClickListener {
//
//            dialogArray = ArrayList()
//            dialogArray.add("ADMS")
//            dialogArray.add("Navin Goradara")
//            dialogArray.add("Hemang Mehta")
//            dialogArray.add("Bothra Classes")
//
////            filterClick("multiple", "Select Tutors", dialogArray, choosemp_filtereTutors)
//        }

        choosemp_rvList.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        callTutorListApi()
        callSubjectListApi()

        if (Utils.getStringValue(activity!!, "course_type_id", "") == "1") {
            callStandardListApi()
        } else {
            callExamListApi()
        }
        callPackageListApi()

    }

    fun filterClick(
        selectionType: String,
        selection: String,
        arr: ArrayList<FilterModel.FilterData>,
        view: TextView,
        filterType: String
    ) {

        val dialog = Dialog(context!!)
        dialog.setContentView(R.layout.dialog_filter_selection)

//        val mModelList: ArrayList<FilterModel.FilterData> = ArrayList()
//        for (i in 0 until arr.size) {
//            val model = TutorModel.TutorData()
//            model.TutorName = arr[i].TutorName
//            model.isSelected = false
//            mModelList.add(model)
//        }

        val list: RecyclerView = dialog.findViewById(R.id.recycler_view)
        val btnDone: Button = dialog.findViewById(R.id.btnDone)
        val btnCancel: Button = dialog.findViewById(R.id.btnCancel)
        val header: TextView = dialog.findViewById(R.id.filterSelection)

        header.text = selection

        list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val recyclerviewAdapter = RecyclerviewAdapter(activity!!, arr, selectionType, filterType)
        list.adapter = recyclerviewAdapter

        btnDone.setOnClickListener {

            var str = ""
            subids = ""

            if (selectionType == "multiple") {
                val finalFilerArray = recyclerviewAdapter.sendArray()

                Log.d("filterarraysize", "" + finalFilerArray.size)

                when (filterType) {
                    "standard" -> {
                        for (i in 0 until finalFilerArray.size) {
                            if (finalFilerArray[i].isSelected) {

                                str += finalFilerArray[i].StandardName + ","
                                stdids += finalFilerArray[i].StandardID + ","
                            }
                        }

                        stdids = stdids.substring(0, stdids.length - 1)
                    }
                    "subject" -> {
                        for (i in 0 until finalFilerArray.size) {
                            if (finalFilerArray[i].isSelected) {

                                str += finalFilerArray[i].SubjectName + ","
                                subids += finalFilerArray[i].SubjectID + ","
                            }
                        }

                        subids = subids.substring(0, subids.length - 1)
                        Log.d("subids", "" + subids)
                    }
                    "tutor" -> {
                        for (i in 0 until finalFilerArray.size) {
                            if (finalFilerArray[i].isSelected) {

                                str += finalFilerArray[i].TutorName + ","
                                tutorids += finalFilerArray[i].TutorID + ","
                            }
                        }

                        tutorids = tutorids.substring(0, tutorids.length - 1)
                    }
                    "exam" -> {
                        for (i in 0 until finalFilerArray.size) {
                            if (finalFilerArray[i].isSelected) {

                                str += finalFilerArray[i].CourseName + ","
                                examids += finalFilerArray[i].CourseID + ","
                            }
                        }
                        examids = examids.substring(0, examids.length - 1)
                    }
                }

                if (str != null && str != "") {
                    str = str.substring(0, str.length - 1)

                    Log.d("filterarraytxt", "" + str)
                    dialog.dismiss()
                } else {
                    Utils.ping(activity!!, "You have to select atleast one")
                }

            } else {

                str = recyclerviewAdapter.sendStandard()

                if (str != null && str != "") {

                    dialog.dismiss()
                } else {
                    Utils.ping(activity!!, "Please select any one")
                }
            }

            view.text = str.substringAfter("-")

            when (view) {
                choosemp_filterTutors -> tutorids = str.substringBefore("-")
                choosemp_filterStandard -> stdids = str.substringBefore("-")
                choosemp_filterExam -> examids = str.substringBefore("-")
                choosemp_filterSubject -> {
                }
            }

        }

        btnCancel.setOnClickListener { dialog.dismiss() }

        dialog.show()

    }

    fun callTutorListApi() {

        if (!DialogUtils.isNetworkConnected(activity!!)) {
            Utils.ping(activity!!, "Connetion not available")
        }

        DialogUtils.showDialog(activity!!)
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getTutorList()
        call.enqueue(object : Callback<FilterModel> {
            override fun onResponse(call: Call<FilterModel>, response: Response<FilterModel>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.Status == "true") {

                        tutorArr = response.body()!!.data

                        choosemp_filterTutors.text = tutorArr[0].TutorName
//                        tutorids = tutorArr[0].TutorID

                    } else {

                        Toast.makeText(activity, response.body()!!.Msg, Toast.LENGTH_SHORT).show()
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

    fun callSubjectListApi() {

        if (!DialogUtils.isNetworkConnected(activity!!)) {
            Utils.ping(activity!!, "Connetion not available")
        }

        DialogUtils.showDialog(activity!!)
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getSubjectList()
        call.enqueue(object : Callback<FilterModel> {
            override fun onResponse(call: Call<FilterModel>, response: Response<FilterModel>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.Status == "true") {

                        subjectArr = response.body()!!.data

                        choosemp_filterSubject.text = subjectArr[0].SubjectName
//                        subids = subjectArr[0].SubjectID

                    } else {

                        Toast.makeText(activity, response.body()!!.Msg, Toast.LENGTH_SHORT).show()
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

    fun callStandardListApi() {

        if (!DialogUtils.isNetworkConnected(activity!!)) {
            Utils.ping(activity!!, "Connetion not available")
        }

        DialogUtils.showDialog(activity!!)
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getStandardList()
        call.enqueue(object : Callback<FilterModel> {
            override fun onResponse(call: Call<FilterModel>, response: Response<FilterModel>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.Status == "true") {

                        standardArr = ArrayList()
                        standardArr = response.body()!!.data

                        choosemp_filterStandard.text = standardArr[0].StandardName
//                        stdids = standardArr[0].StandardID

                    } else {

                        Toast.makeText(activity, response.body()!!.Msg, Toast.LENGTH_SHORT).show()
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

    fun callExamListApi() {

        if (!DialogUtils.isNetworkConnected(activity!!)) {
            Utils.ping(activity!!, "Connetion not available")
        }

        DialogUtils.showDialog(activity!!)
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getExamList(Utils.getStringValue(activity!!, "course_type_id", "")!!)
        call.enqueue(object : Callback<FilterModel> {
            override fun onResponse(call: Call<FilterModel>, response: Response<FilterModel>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.Status == "true") {

                        examArr = ArrayList()
                        examArr = response.body()!!.data

                        choosemp_filterExam.text = examArr[0].CourseName
//                        examids = examArr[0].CourseID

                    } else {

                        Toast.makeText(activity, response.body()!!.Msg, Toast.LENGTH_SHORT).show()
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


    fun callPackageListApi() {

        if (!DialogUtils.isNetworkConnected(activity!!)) {
            Utils.ping(activity!!, "Connetion not available")
        }

        DialogUtils.showDialog(activity!!)
        val apiService = WebClient.getClient().create(WebInterface::class.java)

//        val call = apiService.getPackage(
//            WebRequests.getPackageParams(
//                Utils.getStringValue(activity!!, "course_type_id", "0")!!,
//                Utils.getStringValue(activity!!, "course_id", "0")!!,
//                Utils.getStringValue(activity!!, "std_id", "0")!!,
//                "9"
//            )
//        )

        val call = apiService.getPackage(
            WebRequests.getPackageParams(
//                Utils.getStringValue(activity, "course_type_id", "0")!!,
//                Utils.getStringValue(activity, "course_id", "0")!!,
//                Utils.getStringValue(activity, "std_id", "0")!!,
//                intent.getStringExtra("subject_id")

                Utils.getStringValue(activity!!, "course_type_id", "")!!,
                Utils.getStringValue(activity!!, "course_id", "")!!,
                Utils.getStringValue(activity!!, "standard_id", "")!!,
                Utils.getStringValue(activity!!, "subject_id", "")!!
            )
        )

        call.enqueue(object : Callback<PackageData> {
            override fun onResponse(call: Call<PackageData>, response: Response<PackageData>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.Status == "true") {

                        mDataList = response.body()!!.data

                        choosemp_filterExam.text = Utils.getStringValue(activity!!, "course_name", "")!!
                        choosemp_filterStandard.text = Utils.getStringValue(activity!!, "standard_name", "")!!
                        choosemp_filterSubject.text = Utils.getStringValue(activity!!, "subject_name", "")!!
                        choosemp_filterTutors.text = "All"

                        testPackagesAdapter = TestPackagesAdapter(activity!!, mDataList!!)
                        choosemp_rvList.adapter = testPackagesAdapter

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

    fun callFilterListApi() {

        if (!DialogUtils.isNetworkConnected(activity!!)) {
            Utils.ping(activity!!, "Connetion not available")
        }

        DialogUtils.showDialog(activity!!)
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getFilterData(
            WebRequests.getFilterParams(
                Utils.getStringValue(activity!!, "course_type_id", "")!!,
                examids,
                stdids,
                subids,
                tutorids
            )
        )

        call.enqueue(object : Callback<PackageData> {
            override fun onResponse(call: Call<PackageData>, response: Response<PackageData>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.Status == "true") {

                        mDataList = response.body()!!.data

                        choosemp_rvList.visibility = View.VISIBLE
                        testPackagesAdapter = TestPackagesAdapter(activity!!, mDataList!!)
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
