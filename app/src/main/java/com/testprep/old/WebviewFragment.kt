package com.testprep.old


import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.*
import android.widget.Toast
import com.testprep.R
import com.testprep.old.WebviewPageActivity.Companion.count
import com.testprep.old.adapter.SelectOptionAdapter
import com.testprep.old.models.QuestionResponse
import com.testprep.old.retrofit.ApiClient
import com.testprep.old.retrofit.ApiInterface
import com.testprep.old.retrofit.WebInterface
import kotlinx.android.synthetic.main.fragment_webview.*
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
class WebviewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_webview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        var bundle = arguments
//        var posi = bundle!!.getInt("posi", 0)

        wv_question_list.isNestedScrollingEnabled = false

        callQuestionApi()
    }

    fun callQuestionApi() {

        val sortDialog = Dialog(activity!!)//,R.style.PauseDialog);//, R.style.PauseDialog);
        val window = sortDialog.window
        val wlp = window!!.attributes
        sortDialog.window!!.attributes.verticalMargin = 0.10f
        wlp.gravity = Gravity.BOTTOM
        window.attributes = wlp

//        sortDialog.window!!.setBackgroundDrawableResource(R.drawable.filter1_1)

        sortDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        sortDialog.setCancelable(true)
//        sortDialog.setContentView(getRoot())
        sortDialog.show()

        val apiService = ApiClient.getClient().create(ApiInterface::class.java)

        val call = apiService.getTopRatedMovies("t1506-o2506-u3506-r4506")
        call.enqueue(object : Callback<QuestionResponse> {
            override fun onResponse(call: Call<QuestionResponse>, response: Response<QuestionResponse>) {

                if (response.body()!!.message == "Success") {
                    val movies = response.body()!!.data

                    wv_que_img.loadData(
                        "<b>Q." + (count+1) + "</b> " + movies[0].title + movies[0].titlehtml,
                        "text/html",
                        "UTF-8"
                    )

                    wv_question_list.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                    wv_question_list.adapter =
                        SelectOptionAdapter(activity!!, movies[0].mcq)

//                    optwo1.loadData(
//                        movies[0].mcq[0].title + "<div></div>" + movies[0].mcq[0].titlehtml,
//                        "text/html",
//                        "UTF-8"
//                    )
//                    opone1.loadData(
//                        movies[0].mcq[1].title + "<div></div>" + movies[0].mcq[1].titlehtml,
//                        "text/html",
//                        "UTF-8"
//                    )
//                    opthree1.loadData(
//                        movies[0].mcq[2].title + "<div></div>" + movies[0].mcq[2].titlehtml,
//                        "text/html",
//                        "UTF-8"
//                    )
//                    opfour1.loadData(
//                        movies[0].mcq[3].title + "<div></div>" + movies[0].mcq[3].titlehtml,
//                        "text/html",
//                        "UTF-8"
//                    )
//
//                    opone.setOnCheckedChangeListener { group, checkedId ->
//
//                        if (checkedId) {
//                            opone.isChecked = true
//                            optwo.isChecked = false
//                            opthree.isChecked = false
//                            opfour.isChecked = false
//                        }
//                    }
//
//                    optwo.setOnCheckedChangeListener { group, checkedId ->
//                        if (checkedId) {
//                            opone.isChecked = false
//                            optwo.isChecked = true
//                            opthree.isChecked = false
//                            opfour.isChecked = false
//                        }
//                    }
//
//                    opthree.setOnCheckedChangeListener { group, checkedId ->
//                        if (checkedId) {
//                            opone.isChecked = false
//                            optwo.isChecked = false
//                            opthree.isChecked = true
//                            opfour.isChecked = false
//                        }
//                    }
//
//                    opfour.setOnCheckedChangeListener { group, checkedId ->
//                        if (checkedId) {
//                            opone.isChecked = false
//                            optwo.isChecked = false
//                            opthree.isChecked = false
//                            opfour.isChecked = true
//                        }
//                    }

                    sortDialog.dismiss()

                    Log.d("websize", "Number of movies received: " + movies.size)
                } else {
                    sortDialog.dismiss()
                    Toast.makeText(activity!!, "No Question at that time", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<QuestionResponse>, t: Throwable) {
                // Log error here since request failed
                Log.e("", t.toString())
                sortDialog.dismiss()
            }
        })
    }

}
