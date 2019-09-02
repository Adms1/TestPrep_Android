package com.testprep.fragments

import android.content.Intent
import android.opengl.ETC1.isValid
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import com.testprep.R
import com.testprep.activity.TutorDetailActivity
import com.testprep.adapter.RecentSearchAdapter
import com.testprep.models.PackageData
import com.testprep.retrofit.WebClient
import com.testprep.retrofit.WebInterface
import com.testprep.utils.AppConstants
import com.testprep.utils.DialogUtils
import com.testprep.utils.Utils
import com.testprep.utils.WebRequests
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.fragment_explore.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 *
 */

class ExploreFragment : Fragment() {

    private var mDataList: ArrayList<PackageData.PackageDataList>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explore, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (AppConstants.recentSearchList.size > 0) {
            explore_rvList.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

            explore_rvList.adapter = RecentSearchAdapter(activity!!, AppConstants.recentSearchList)
        }

        explore_etSearch.setOnEditorActionListener(object : TextView.OnEditorActionListener {

            override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
                if (event != null && event.keyCode == KeyEvent.KEYCODE_SEARCH || actionId == EditorInfo.IME_ACTION_SEARCH) {
                    for (i in 0..AppConstants.recentSearchList.size) {
                        if (!AppConstants.recentSearchList.contains(explore_etSearch.text.toString())) {
                            AppConstants.recentSearchList.add(explore_etSearch.text.toString())
                        }
                    }

                    val intent = Intent(context, TutorDetailActivity::class.java)
                    intent.putExtra("type", "explore")
                    intent.putExtra("pname", "Packages")
                    intent.putExtra("boardid", "")
                    intent.putExtra("stdid", "")
                    intent.putExtra("subid", "")
                    intent.putExtra("tutorid", "")
                    intent.putExtra("search_name", explore_etSearch.text.toString())
                    startActivity(intent)
                }
                return false
            }
        })

        explore_ivSearch.setOnClickListener {

            for (i in 0..AppConstants.recentSearchList.size) {
                if (!AppConstants.recentSearchList.contains(explore_etSearch.text.toString())) {
                    AppConstants.recentSearchList.add(explore_etSearch.text.toString())
                }
            }

//            explore_etSearch.setText("")

            val intent = Intent(context, TutorDetailActivity::class.java)
            intent.putExtra("type", "explore")
            intent.putExtra("pname", "Packages")
            intent.putExtra("boardid", "")
            intent.putExtra("stdid", "")
            intent.putExtra("subid", "")
            intent.putExtra("tutorid", "")
            intent.putExtra("search_name", explore_etSearch.text.toString())
            startActivity(intent)

//            callFilterListApi()
        }

        callSubjectListApi()

//        explore_etSearch.addTextChangedListener(object : TextWatcher {
//
//            override fun afterTextChanged(s: Editable) {
//                explore_rvList.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
//
//                explore_tvRecentsearch.visibility = View.VISIBLE
//                explore_ivSeeall.visibility = View.VISIBLE
//
//                if (AppConstants.recentSearchList.size > 0) {
//                    explore_rvList.adapter = RecentSearchAdapter(activity!!, AppConstants.recentSearchList)
//                }
//            }
//
//            override fun beforeTextChanged(
//                s: CharSequence, start: Int,
//                count: Int, after: Int
//            ) {
//            }
//
//            override fun onTextChanged(
//                s: CharSequence, start: Int,
//                before: Int, count: Int
//            ) {
//
//            }
//        })

    }

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
                "",
                "",
                "",
                "",
                "",
                "",
                explore_etSearch.text.toString(), "-1"
            )
        )

        call.enqueue(object : Callback<PackageData> {
            override fun onResponse(call: Call<PackageData>, response: Response<PackageData>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.Status == "true") {

                        mDataList = response.body()!!.data

//                        val intent = Intent(context, TutorDetailActivity::class.java)
//                        intent.putExtra("type", "explore")
//                        intent.putExtra("pname", "Packages")
//                        intent.putExtra("parr", mDataList)
//                        startActivity(intent)

//                        explore_rvList.layoutManager = GridLayoutManager(activity, 2)

//                        if (mDataList!!.size > 0) {
//                            explore_rvList.adapter = TestPackagesAdapter(activity!!, mDataList!!)
//                        }

                    } else {

                        Toast.makeText(activity!!, response.body()!!.Msg, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<PackageData>, t: Throwable) {
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

                        var subArr: ArrayList<String> = ArrayList()
                        for (i in 0 until filterArray.size) {
                            subArr.add(filterArray[i].SubjectName)
                        }

                        val adapter = ArrayAdapter(
                            activity!!,
                            android.R.layout.simple_list_item_1,
                            subArr
                        )
                        explore_etSearch.setAdapter(adapter)

//                        recyclerviewAdapter =
//                            FilterAdapter(activity!!, filterArray, "multiple", "subject", filterInterface!!)
//                        filterData_rvList.adapter = recyclerviewAdapter
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


}
