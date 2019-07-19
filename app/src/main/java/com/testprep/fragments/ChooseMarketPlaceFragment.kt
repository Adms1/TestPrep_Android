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
import com.testprep.models.Model
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(com.testprep.R.layout.fragment_choose_market_place, container, false)
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
//
////        choosemp_ivFilter.setOnClickListener {
////            val intent = Intent(activity, FilterActivity::class.java)
////            startActivity(intent)
////        }
//
//        choosemp_tvTutors.setOnClickListener {
//            val bundle = Bundle()
//            bundle.putString("tab", "Tutors")
//            fragment.arguments = bundle
//            fragmentManager!!.beginTransaction().replace(R.id.container, fragment).addToBackStack(null)
//                .commit()
//
//        }

        if (Utils.getStringValue(activity!!, "course_type_id", "") == "1") {

            llFirst.visibility = View.VISIBLE
            llSecond.visibility = View.VISIBLE
            llThird.visibility = View.GONE
            llFourth.visibility = View.GONE

        } else {

            llFirst.visibility = View.GONE
            llSecond.visibility = View.GONE
            llThird.visibility = View.VISIBLE
            llFourth.visibility = View.VISIBLE

        }


        rlOne.setOnClickListener {

            dialogArray = ArrayList()
            dialogArray.add("9th")
            dialogArray.add("10th")
            dialogArray.add("11th")
            dialogArray.add("12th")

            filterClick("single", "Select Standard", dialogArray, choosemp_filterStandard)
        }

        rlTwo.setOnClickListener {

            dialogArray = ArrayList()
            dialogArray.add("Social Science")
            dialogArray.add("Science")
            dialogArray.add("Mathematics")
            dialogArray.add("Hindi")
            dialogArray.add("English")

            filterClick("multiple", "Select Subject", dialogArray, choosemp_filterSubject)
        }

        rlThree.setOnClickListener {

            dialogArray = ArrayList()
            dialogArray.add("ADMS")
            dialogArray.add("Navin Goradara")
            dialogArray.add("Hemang Mehta")
            dialogArray.add("Bothra Classes")

            filterClick("multiple", "Select Tutors", dialogArray, choosemp_filterTutors)
        }

        rlFour.setOnClickListener {

            dialogArray = ArrayList()
            dialogArray.add("JEE IIT")
            dialogArray.add("NEET")
            dialogArray.add("CBSE")
            dialogArray.add("GSEB")

            filterClick("single", "Select Exam", dialogArray, choosemp_filterExam)
        }

        rlFive.setOnClickListener {

            dialogArray = ArrayList()
            dialogArray.add("Social Science")
            dialogArray.add("Science")
            dialogArray.add("Mathematics")
            dialogArray.add("Hindi")
            dialogArray.add("English")

            filterClick("multiple", "Select Subject", dialogArray, choosemp_filtereSubject)
        }

        rlSix.setOnClickListener {

            dialogArray = ArrayList()
            dialogArray.add("ADMS")
            dialogArray.add("Navin Goradara")
            dialogArray.add("Hemang Mehta")
            dialogArray.add("Bothra Classes")

            filterClick("multiple", "Select Tutors", dialogArray, choosemp_filtereTutors)
        }

        choosemp_rvList.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        callPackageListApi()

    }

    fun filterClick(selectionType: String, selection: String, arr: ArrayList<String>, view: TextView) {

        val dialog = Dialog(context!!)
        dialog.setContentView(R.layout.dialog_filter_selection)

        val mModelList: ArrayList<Model> = ArrayList()
        for (i in 0 until arr.size) {
            val model = Model()
            model.textt = arr[i]
            model.isSelected = false
            mModelList.add(model)
        }

        val list: RecyclerView = dialog.findViewById(R.id.recycler_view)
        val btnDone: Button = dialog.findViewById(R.id.btnDone)
        val btnCancel: Button = dialog.findViewById(R.id.btnCancel)
        val header: TextView = dialog.findViewById(R.id.filterSelection)

        header.text = selection

        list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        var recyclerviewAdapter = RecyclerviewAdapter(activity!!, mModelList, selectionType)
        list.adapter = recyclerviewAdapter

        var str = ""

        btnDone.setOnClickListener {

            if (selectionType == "multiple") {
                var finalFilerArray = recyclerviewAdapter.sendArray()

                Log.d("filterarraysize", "" + finalFilerArray.size)

                for (i in 0 until finalFilerArray.size) {
                    if (finalFilerArray[i].isSelected) {
                        str += finalFilerArray[i].textt + ","
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

            view.text = str
        }

        btnCancel.setOnClickListener { dialog.dismiss() }

        dialog.show()

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


}
