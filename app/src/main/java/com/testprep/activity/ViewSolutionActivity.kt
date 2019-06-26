package com.testprep.activity

import android.content.Context
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import com.testprep.R
import com.testprep.adapter.QuestionsPagerAdapter
import com.testprep.old.PageActivity
import com.testprep.old.models.QuestionResponse
import com.testprep.retrofit.WebClient
import com.testprep.retrofit.WebInterface
import com.testprep.utils.DialogUtils
import com.testprep.utils.Utils
import kotlinx.android.synthetic.main.activity_view_solution.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class ViewSolutionActivity : AppCompatActivity() {

    var questionpagerAdapret: QuestionsPagerAdapter? = null
    var mToolbar: Toolbar? = null
    var movies: ArrayList<QuestionResponse.QuestionList> = ArrayList()

    var where = ""
    var reviewQue: ArrayList<Int> = ArrayList()

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setContentView(R.layout.activity_view_solution)

        setSupportActionBar(mToolbar)

        solution_sliding_tabs.setupWithViewPager(solution_viewpager)

        solution_btnNext.setOnClickListener {
            getLastPage()
            solution_viewpager.currentItem = solution_viewpager.currentItem + 1
            solution_tvTotal.text = """${solution_viewpager.currentItem + 1}/${movies.size}"""
        }

        solution_ivBack.setOnClickListener { onBackPressed() }

        solution_btnPrevious.setOnClickListener { v: View? ->

            solution_viewpager.currentItem = solution_viewpager.currentItem - 1
            solution_tvTotal.text = """${solution_viewpager.currentItem + 1}/${movies.size}"""
        }

        solution_viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            // This method will be invoked when a new page becomes selected.
            override fun onPageSelected(position: Int) {
                solution_tvTotal.text = """${position + 1}/${movies.size}"""

//                for (i in 0 until reviewQue.size){
//                    if(reviewQue.contains(position + 1)){
//                        solution_ivReview.background = resources.getDrawable(R.drawable.rotate_eye_blue)
//                        solution_ivReview.isSelected = true

//                }else{
//                    solution_ivReview.background = resources.getDrawable(R.drawable.rotate_eye_gray)
//
//                }
//                }

                getLastPage()
            }

            // This method will be invoked when the current page is scrolled
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                // Code goes here
//                getLastPage()
            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            override fun onPageScrollStateChanged(state: Int) {
                // Code goes here
//                getLastPage()
            }
        })

        callQuestionApi()
    }

    fun callQuestionApi() {

        if (!DialogUtils.isNetworkConnected(this@ViewSolutionActivity)) {
            Utils.ping(this@ViewSolutionActivity, "Connetion not available")
        }

        DialogUtils.showDialog(this@ViewSolutionActivity)

        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getQuestions("2")
        call.enqueue(object : Callback<QuestionResponse> {
            override fun onResponse(call: Call<QuestionResponse>, response: Response<QuestionResponse>) {

                DialogUtils.dismissDialog()

//                if (response.body()!!.Msg == "Question List ") {
                movies = response.body()!!.data

                solution_tvTotal.text = """1/${movies.size}"""
//                    totall.text = "Total" + movies.size

//                    qno.text = "Q." + (PageActivity.countt +1)

                Log.d("qid", "" + movies[0].QuestionID)

                if (PageActivity.countt >= 0) {

                    Utils.saveArrayList(this@ViewSolutionActivity, movies, "que_list")

                    if ("http://content.testcraft.co.in/question/" + movies[0].QuestionImage != "") {

                        for (i in 0 until movies.size) {
                            solution_sliding_tabs.addTab(solution_sliding_tabs.newTab().setText((i + 1).toString()))

                            questionpagerAdapret =
                                QuestionsPagerAdapter(supportFragmentManager, movies.size, "solution")

//                                questionpagerAdapret!!.addFragment(PageViewFragment())
                        }
                        solution_viewpager.adapter = questionpagerAdapret

                    }
                }
                Log.d("imgcall", "Number of movies received: " + movies.size)
//                } else {
//
//                    Toast.makeText(this@ViewSolutionActivity, "No Question at that time", Toast.LENGTH_LONG).show()
//                }
            }

            override fun onFailure(call: Call<QuestionResponse>, t: Throwable) {
                // Log error here since request failed
                Log.e("question res", t.toString())
                DialogUtils.dismissDialog()
            }
        })
    }

    private fun getLastPage() {

        if (solution_viewpager.currentItem == movies.size - 1) {

            solution_btnNext.text = getString(R.string.finish)

        } else {
            solution_btnNext.text = getString(R.string.next)
        }
    }

}
