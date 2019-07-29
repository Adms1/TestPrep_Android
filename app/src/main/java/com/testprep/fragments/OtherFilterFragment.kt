package com.testprep.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.testprep.R
import com.testprep.adapter.RecyclerviewAdapter
import com.testprep.models.FilterModel
import com.testprep.retrofit.WebClient
import com.testprep.retrofit.WebInterface
import com.testprep.utils.DialogUtils
import com.testprep.utils.Utils
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
class OtherFilterFragment : Fragment() {

    var filter_type = ""
    var finalfilterArray: ArrayList<FilterModel.FilterData>? = null
    var recyclerviewAdapter: RecyclerviewAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        filter_type = arguments!!.getString("type")
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_other_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        filterData_rvList.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        callCourseListApi()

        when (filter_type) {
            "course_type" -> {

                callCourseListApi()

            }
            "boards" -> {

                callExamListApi("1")

            }
            "competitive_exams" -> {

                callExamListApi("2")

            }
            "standards" -> {

                callStandardListApi()

            }
            "subjects" -> {

                callSubjectListApi()

            }

        }

    }

    fun callCourseListApi(): ArrayList<FilterModel.FilterData> {

        var filterArray: ArrayList<FilterModel.FilterData> = ArrayList()

        if (!DialogUtils.isNetworkConnected(activity!!)) {
            Utils.ping(activity!!, "Connetion not available")
        }

        DialogUtils.showDialog(activity!!)
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getCourseList()
        call.enqueue(object : Callback<FilterModel> {
            override fun onResponse(call: Call<FilterModel>, response: Response<FilterModel>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.Status == "true") {

                        filterArray = response.body()!!.data
                        recyclerviewAdapter = RecyclerviewAdapter(activity!!, filterArray, "multiple", "course_type")
                        filterData_rvList.adapter = recyclerviewAdapter
//                        filterArray!!.add("Boards")
//                        filterArray!!.add("Competitve Exams")

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

        return filterArray
    }

    fun callStandardListApi() {

        var filterArray: ArrayList<FilterModel.FilterData> = ArrayList()

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

                        filterArray = response.body()!!.data
                        recyclerviewAdapter = RecyclerviewAdapter(activity!!, filterArray, "multiple", "standard")
                        filterData_rvList.adapter = recyclerviewAdapter

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

    fun callSubjectListApi(): ArrayList<FilterModel.FilterData> {

        var filterArray: ArrayList<FilterModel.FilterData> = ArrayList()

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

                        filterArray = response.body()!!.data
                        recyclerviewAdapter = RecyclerviewAdapter(activity!!, filterArray, "multiple", "subject")
                        filterData_rvList.adapter = recyclerviewAdapter
//                        choosemp_filterSubject.text = subjectArr[0].SubjectName
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

        return filterArray
    }

    fun callExamListApi(type: String) {

        var filterArray: ArrayList<FilterModel.FilterData> = ArrayList()

        if (!DialogUtils.isNetworkConnected(activity!!)) {
            Utils.ping(activity!!, "Connetion not available")
        }

        DialogUtils.showDialog(activity!!)
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getExamList(type)
        call.enqueue(object : Callback<FilterModel> {
            override fun onResponse(call: Call<FilterModel>, response: Response<FilterModel>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.Status == "true") {

                        filterArray = response.body()!!.data
                        recyclerviewAdapter = RecyclerviewAdapter(activity!!, filterArray, "multiple", "exam")
                        filterData_rvList.adapter = recyclerviewAdapter

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

}
