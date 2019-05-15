package com.testprep.activity

import android.os.Bundle
import android.os.CountDownTimer
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import com.testprep.adapter.QuestionsPagerAdapter
import com.testprep.old.PageActivity
import com.testprep.old.models.QuestionResponse
import com.testprep.old.retrofit.ApiClient
import com.testprep.old.retrofit.ApiInterface
import com.testprep.utils.DialogUtils
import com.testprep.utils.Utils
import kotlinx.android.synthetic.main.activity_tabwise_question.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit


class TabwiseQuestionActivity : AppCompatActivity() {

    var questionpagerAdapret: QuestionsPagerAdapter? = null
    var mToolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.testprep.R.layout.activity_tabwise_question)

//        mToolbar = (Toolbar) findViewById(com.testprep.R.id.sliding_tabs)
//        sliding_tabs.setOnTabSelectedListener(this)
        setSupportActionBar(mToolbar)

        queTab_sliding_tabs.setupWithViewPager(queTab_viewpager)

        callQuestionApi()
    }

    var mTabLayout_config: Runnable = Runnable {
        if (queTab_sliding_tabs.width < this@TabwiseQuestionActivity.resources.displayMetrics.widthPixels) {
            queTab_sliding_tabs.tabMode = TabLayout.MODE_FIXED
            val mParams = queTab_sliding_tabs.layoutParams
            mParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            queTab_sliding_tabs.layoutParams = mParams

        } else {
            queTab_sliding_tabs.tabMode = TabLayout.MODE_SCROLLABLE
        }
    }

    fun callQuestionApi() {

        if (!DialogUtils.isNetworkConnected(this@TabwiseQuestionActivity)) {
            Utils.ping(this@TabwiseQuestionActivity, "Connetion not available")
        }

        DialogUtils.showDialog(this@TabwiseQuestionActivity)

        val apiService = ApiClient.getClient().create(ApiInterface::class.java)

        val call = apiService.getTopRatedMovies("t1506-o2506-u3506-r4506")
        call.enqueue(object : Callback<QuestionResponse> {
            override fun onResponse(call: Call<QuestionResponse>, response: Response<QuestionResponse>) {

                DialogUtils.dismissDialog()

                setCountdown("10")

                if (response.body()!!.message == "Success") {
                    val movies = response.body()!!.data

//                    totall.text = "Total" + movies.size

//                    qno.text = "Q." + (PageActivity.countt +1)

                    Log.d("qid", "" + movies[0].id)

                    if (PageActivity.countt >= 0) {

                        Utils.saveArrayList(this@TabwiseQuestionActivity, movies, "que_list")

                        if ("http://content.testcraft.co.in/question/" + movies[0].titleimg != "") {

                            for (i in 0 until movies.size) {
                                queTab_sliding_tabs.addTab(queTab_sliding_tabs.newTab().setText((i + 1).toString()))

                                questionpagerAdapret = QuestionsPagerAdapter(supportFragmentManager, movies.size)

//                                questionpagerAdapret!!.addFragment(PageViewFragment())
                            }
                            queTab_viewpager.adapter = questionpagerAdapret

                            queTab_sliding_tabs.post(mTabLayout_config)
                        }
                    }
                    Log.d("imgcall", "Number of movies received: " + movies.size)
                } else {

                    Toast.makeText(this@TabwiseQuestionActivity, "No Question at that time", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<QuestionResponse>, t: Throwable) {
                // Log error here since request failed
                Log.e("", t.toString())
                DialogUtils.dismissDialog()
            }
        })
    }

    private fun setCountdown(minute: String) {

        val mili = TimeUnit.MINUTES.toMillis(minute.toLong())

        object : CountDownTimer(mili, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                queTab_tvTimer.text = "" + String.format(
                    "%02d : %02d",
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(
                            millisUntilFinished
                        )
                    )
                )
            }

            override fun onFinish() {
                queTab_tvTimer.text = "done!"
            }
        }.start()
    }

}
