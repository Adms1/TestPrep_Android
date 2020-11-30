package com.testcraft.testcraft.fragments

import android.content.DialogInterface
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.JsonObject
import com.testcraft.testcraft.R
import com.testcraft.testcraft.adapter.CourseSpinnerAdapter
import com.testcraft.testcraft.adapter.CoursetypeSpinnerAdapter
import com.testcraft.testcraft.adapter.StdSpinnerAdapter
import com.testcraft.testcraft.models.PackageData
import com.testcraft.testcraft.retrofit.WebClient
import com.testcraft.testcraft.retrofit.WebInterface
import com.testcraft.testcraft.utils.AppConstants
import com.testcraft.testcraft.utils.DialogUtils
import com.testcraft.testcraft.utils.Utils
import kotlinx.android.synthetic.main.fragment_market_place_bottom_sheet.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MarketPlaceBottomSheetFragment : BottomSheetDialogFragment() {

    var strCourseType = 0
    var strBoard = "0"
    var strStd = "0"
    var courseTypeList: ArrayList<PackageData.PackageDataList> = ArrayList()
    var courseList: ArrayList<PackageData.PackageDataList> = ArrayList()
    var stdList: ArrayList<PackageData.PackageDataList> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_market_place_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Utils.setFont(activity!!, "fonts/Inter-SemiBold.ttf", bottomsheet_tvHeader)
        Utils.setFont(activity!!, "fonts/Inter-SemiBold.ttf", bottomsheet_tvText)
        Utils.setFont(activity!!, "fonts/Inter-SemiBold.ttf", bottomsheet_tvListAmount)

        bottomsheet_tvPaynow.setOnClickListener {
            callGetSubscriptionConfirm()
        }

        bottomsheet_spCourseType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) { // Get the value selected by the user
                    // e.g. to store it as a field or immediately call a method
                    strCourseType = courseTypeList[position].CourseTypeID

                    if (strCourseType != 0) {

                        if (strCourseType == 1 && strBoard != "0") {
                            bottomsheet_spStd.visibility = View.VISIBLE
                            bottomsheet_line2.visibility = View.VISIBLE
                        } else {
                            bottomsheet_spStd.visibility = View.INVISIBLE
                            bottomsheet_line2.visibility = View.INVISIBLE
                        }
                        bottomsheet_spBoard.visibility = View.VISIBLE
                        bottomsheet_line1.visibility = View.VISIBLE

                        callCourseListApi(strCourseType)
                    } else {

                        bottomsheet_spStd.visibility = View.INVISIBLE
                        bottomsheet_line2.visibility = View.INVISIBLE
                        bottomsheet_spBoard.visibility = View.INVISIBLE
                        bottomsheet_line1.visibility = View.INVISIBLE
                        bottomsheet_llAmount.visibility = View.INVISIBLE
                        bottomsheet_tvPaynow.visibility = View.INVISIBLE
                    }

                    bottomsheet_llAmount.visibility = View.INVISIBLE
                    bottomsheet_tvPaynow.visibility = View.INVISIBLE

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        bottomsheet_spBoard.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) { // Get the value selected by the user
                    // e.g. to store it as a field or immediately call a method

                    bottomsheet_llAmount.visibility = View.INVISIBLE
                    bottomsheet_tvPaynow.visibility = View.INVISIBLE

                    if (courseList.size > 0) {

//                        if(strBoard != "0") {

                        strBoard = courseList[position].CourseID



                        if (strCourseType == 2) {
                            if (strBoard != "0") {
                                callInsertSubscriptionSubject()
                            }
                        } else {
                            callStandardList(strBoard)
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        bottomsheet_spStd.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) { // Get the value selected by the user
                    // e.g. to store it as a field or immediately call a method

                    bottomsheet_llAmount.visibility = View.INVISIBLE
                    bottomsheet_tvPaynow.visibility = View.INVISIBLE

                    strStd = stdList[position].StandardID

                    callInsertSubscriptionSubject()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        callCourseTypeList()
    }

    fun callCourseTypeList() {

        if (!DialogUtils.isNetworkConnected(activity!!)) {
            Utils.ping(activity!!, AppConstants.NETWORK_MSG)
        }

        DialogUtils.showDialog(activity!!)
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getCourseList()
        call.enqueue(object : Callback<PackageData> {
            override fun onResponse(call: Call<PackageData>, response: Response<PackageData>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    val packageData = PackageData.PackageDataList(0, "")
                    packageData.CourseTypeID = 0
                    packageData.CourseTypeName = "Select Exam"
                    courseTypeList.add(packageData)

                    if (response.body()!!.Status == "true") {

                        for (i in 0 until response.body()!!.data.size) {
                            courseTypeList.add(response.body()!!.data[i])
                        }
                        bottomsheet_spCourseType.adapter =
                            CoursetypeSpinnerAdapter(activity!!, courseTypeList)

                    } else {

//                        Toast.makeText(activity!!, response.body()!!.Msg, Toast.LENGTH_SHORT).show()

                    }
                }
            }

            override fun onFailure(call: Call<PackageData>, t: Throwable) {

                Log.e("", t.toString())
                DialogUtils.dismissDialog()

            }
        })
    }

    fun callCourseListApi(strcourseType: Int) {

        courseList = ArrayList()
        if (!DialogUtils.isNetworkConnected(activity!!)) {
            Utils.ping(activity!!, AppConstants.NETWORK_MSG)
        }

        DialogUtils.showDialog(activity!!)
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getCourseTypeList(strcourseType.toString())
        call.enqueue(object : Callback<PackageData> {
            override fun onResponse(call: Call<PackageData>, response: Response<PackageData>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    val packageData = PackageData.PackageDataList(0, "")

                    if (response.body()!!.Status == "true") {

                        if (strCourseType == 1) {
                            packageData.CourseID = "0"
                            packageData.CourseName = "Select Board"
                            courseList.add(packageData)
                        } else {
                            packageData.CourseID = "0"
                            packageData.CourseName = "Select Course"
                            courseList.add(packageData)
                        }

                        for (i in 0 until response.body()!!.data.size) {
                            courseList.add(response.body()!!.data[i])
                        }
                        bottomsheet_spBoard.adapter =
                            CourseSpinnerAdapter(activity!!, courseList)

//                        callSubscriptionPrice()


                    } else {

//                        Toast.makeText(activity!!, response.body()!!.Msg, Toast.LENGTH_SHORT).show()

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

    fun callStandardList(courseId: String) {

        stdList = ArrayList()

        if (strCourseType == 1) {
            if (courseId == "0") {
                bottomsheet_spStd.visibility = View.INVISIBLE
                bottomsheet_line2.visibility = View.INVISIBLE
            } else {
                bottomsheet_spStd.visibility = View.VISIBLE
                bottomsheet_line2.visibility = View.VISIBLE
            }
        }

        if (!DialogUtils.isNetworkConnected(activity!!)) {
            Utils.ping(activity!!, AppConstants.NETWORK_MSG)
        }

        DialogUtils.showDialog(activity!!)
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getBoardStandardList(courseId.toString())

        call.enqueue(object : Callback<PackageData> {
            override fun onResponse(call: Call<PackageData>, response: Response<PackageData>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.Status == "true") {

                        for (i in 0 until response.body()!!.data.size) {
                            stdList.add(response.body()!!.data[i])
                        }
                        bottomsheet_spStd.adapter = StdSpinnerAdapter(activity!!, stdList)

//                        callSubscriptionPrice()

                    } else {

//                        Toast.makeText(activity!!, response.body()!!.Msg, Toast.LENGTH_SHORT).show()

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

    fun callSubscriptionPrice() {
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call =
            apiService.getSubscriptionCount(Utils.getStringValue(activity!!, AppConstants.USER_ID, "0")!!)

        call.enqueue(object : Callback<JsonObject> {

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.body()!!.get("data").asJsonObject != null) {

                    bottomsheet_tvPaynow.visibility = View.VISIBLE
                    bottomsheet_llAmount.visibility = View.VISIBLE

                    bottomsheet_tvListAmount.paintFlags =
                        bottomsheet_tvListAmount.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    Utils.setFont(activity!!, "fonts/Inter-Bold.ttf", bottomsheet_tvAmount)

                    if (response.body()!!.get("data").asJsonObject.get("Price").asString == "0") {
                        bottomsheet_tvAmount.text = " Free"
                    } else {
                        bottomsheet_tvAmount.text =
                            "₹" + response.body()!!.get("data").asJsonObject.get("Price").asString
                    }

//                    bottomsheet_tvAmount.text =  "₹" + response.body()!!.get("data").asJsonObject.get("Price").asString
                    bottomsheet_tvListAmount.text =
                        "₹" + response.body()!!.get("data").asJsonObject.get("ListPrice").asString

                } else {
//
                    bottomsheet_tvPaynow.visibility = View.INVISIBLE
                    bottomsheet_llAmount.visibility = View.INVISIBLE
//                    bottomsheet_tvAmount.text =  "₹" + response.body()!!.get("data").asJsonObject.get("Price").asString
//                    bottomsheet_tvListAmount.text = "₹ " + response.body()!!.get("data").asJsonObject.get("ListPrice").asString
//
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // Log error here since request failed
                Log.e("", t.toString())
            }
        })
    }

    fun callGetSubscriptionConfirm() {
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        DialogUtils.showDialog(activity!!)
        val call = apiService.subscription_checkout(bottomsheet_tvAmount.text.toString(), "0",
            Utils.getStringValue(activity!!, AppConstants.USER_ID, "0")!!)

        call.enqueue(object : Callback<JsonObject> {

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                DialogUtils.dismissDialog()
                if (response.body() != null) {

                    if (response.body()!!["Status"].asString == "true") {

                        updatepayment(response.body()!!["data"].asJsonArray[0].asJsonObject["OrderID"].asString)
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // Log error here since request failed
                DialogUtils.dismissDialog()
                Log.e("", t.toString())
            }
        })
    }

    fun updatepayment(orderid: String) {
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        DialogUtils.showDialog(activity!!)

        val hashmap: HashMap<String, String> = HashMap()
        hashmap["PaymentOrderID"] = orderid
        hashmap["StudentID"] = Utils.getStringValue(activity!!, AppConstants.USER_ID, "0")!!
        hashmap["ExternalTransactionID"] = "2"
        hashmap["ExternalTransactionStatus"] = "success"

        val call = apiService.updatesubscriptionPayment(hashmap)

        call.enqueue(object : Callback<JsonObject> {

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                DialogUtils.dismissDialog()
                if (response.body() != null) {

                    if (response.body()!!["Status"].asString == "true") {

//                        AppConstants.isFirst = 1
//                        setFragments(null)
                        MarketPlaceFragment.sheetClose()
                        Utils.ping(activity!!, "Successfully subscribe")

                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // Log error here since request failed
                DialogUtils.dismissDialog()
                Log.e("", t.toString())
            }
        })
    }

    fun callInsertSubscriptionSubject() {
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        var board = ""
        var cource = ""
        var std = ""

        if (strCourseType == 1) {
            board = strBoard
            cource = "0"
            std = strStd
        } else {
            std = "0"
            board = "0"
            cource = strBoard
        }

        val call = apiService.insertSubscriptionSubject(
            Utils.getStringValue(activity!!, AppConstants.USER_ID, "0")!!,
            cource,
            board,
            std,
            strCourseType.toString()
        )

        call.enqueue(object : Callback<JsonObject> {

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.body()!!.get("Status").asString == "true") {

                    callSubscriptionPrice()
//                    callGetSubscriptionConfirm()

                } else {

                    bottomsheet_tvPaynow.visibility = View.INVISIBLE
                    bottomsheet_llAmount.visibility = View.INVISIBLE

                    DialogUtils.createConfirmDialog1(
                        activity!!,
                        "OK",
                        response.body()!!.get("Msg").asString,
                        DialogInterface.OnClickListener { dialog, which ->

                            dialog.dismiss()

                        }).show()

                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // Log error here since request failed
                Log.e("", t.toString())
            }
        })
    }

}