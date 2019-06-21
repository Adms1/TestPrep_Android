package com.testprep.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.os.CountDownTimer
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager.OnPageChangeListener
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import com.testprep.R
import com.testprep.adapter.QuestionsPagerAdapter
import com.testprep.old.PageActivity
import com.testprep.old.models.QuestionResponse
import com.testprep.retrofit.WebClient
import com.testprep.retrofit.WebInterface
import com.testprep.utils.DialogUtils
import com.testprep.utils.Utils
import kotlinx.android.synthetic.main.activity_tabwise_question.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.util.concurrent.TimeUnit

class TabwiseQuestionActivity : AppCompatActivity() {

    var questionpagerAdapret: QuestionsPagerAdapter? = null
    var mToolbar: Toolbar? = null
    var movies: ArrayList<QuestionResponse.QuestionList> = ArrayList()

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setContentView(com.testprep.R.layout.activity_tabwise_question)

//        mToolbar = (Toolbar) findViewById(com.testprep.R.id.sliding_tabs)
//        sliding_tabs.setOnTabSelectedListener(this)
        setSupportActionBar(mToolbar)

        queTab_sliding_tabs.setupWithViewPager(queTab_viewpager)

        queTab_btnNext.setOnClickListener {
            getLastPage()
            queTab_viewpager.currentItem = queTab_viewpager.currentItem + 1
            queTab_tvTotal.text = """${queTab_viewpager.currentItem + 1}/${movies.size}"""
        }

        queTab_ivPlayPause.setOnCheckedChangeListener { buttonView, isChecked ->

            stopTimer()

            val dialog = Dialog(this@TabwiseQuestionActivity)
            dialog.setContentView(R.layout.play_pause_alertdialog)
            dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
//            var wmlp: WindowManager.LayoutParams = dialog.window.attributes
//            wmlp.width = WindowManager.LayoutParams.MATCH_PARENT
//            wmlp.height = WindowManager.LayoutParams.WRAP_CONTENT
            dialog.window!!.setGravity(Gravity.BOTTOM)
            dialog.setCanceledOnTouchOutside(false)

            val cancelBtn: TextView = dialog.findViewById(R.id.dialog_tvCancel)
            val resumeBtn: TextView = dialog.findViewById(R.id.dialog_tvResume)
            val abortBtn: TextView = dialog.findViewById(R.id.dialog_tvAbort)

            cancelBtn.setOnClickListener {
                dialog.dismiss()
                timerResume()
            }

            resumeBtn.setOnClickListener {
                timerResume()
                dialog.dismiss()
            }

            abortBtn.setOnClickListener { onBackPressed() }

            dialog.show()

            queTab_ivPlayPause.isChecked = !isChecked

        }

        queTab_btnPrevious.setOnClickListener { v: View? ->

            queTab_viewpager.currentItem = queTab_viewpager.currentItem - 1
            queTab_tvTotal.text = """${queTab_viewpager.currentItem + 1}/${movies.size}"""
        }

        queTab_viewpager.addOnPageChangeListener(object : OnPageChangeListener {

            // This method will be invoked when a new page becomes selected.
            override fun onPageSelected(position: Int) {
                queTab_tvTotal.text = """${position + 1}/${movies.size}"""
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

        queTab_tvSubmit.setOnClickListener {

            //            DialogUtils.createConfirmDialog(this@TabwiseQuestionActivity, "Submit Test?", "are you sure you want to end this test?",
//                "OK", "Cancel",
//                DialogInterface.OnClickListener { dialog, which ->
//
//                       dialog.dismiss()
//                },
//            DialogInterface.OnClickListener { dialog, which ->
//
//                dialog.dismiss()
//            }).show()

            onBackPressed()

        }

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

        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getQuestions("2")
        call.enqueue(object : Callback<QuestionResponse> {
            override fun onResponse(call: Call<QuestionResponse>, response: Response<QuestionResponse>) {

                DialogUtils.dismissDialog()

                setCountdown(30 * 60 * 1000)

//                if (response.body()!!.Msg == "Question List ") {
                    movies = response.body()!!.data

                    queTab_tvTotal.text = """1/${movies.size}"""
//                    totall.text = "Total" + movies.size

//                    qno.text = "Q." + (PageActivity.countt +1)

                Log.d("qid", "" + movies[0].QuestionID)

                    if (PageActivity.countt >= 0) {

                        Utils.saveArrayList(this@TabwiseQuestionActivity, movies, "que_list")

                        if ("http://content.testcraft.co.in/question/" + movies[0].QuestionImage != "") {

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
//                } else {
//
//                    Toast.makeText(this@TabwiseQuestionActivity, "No Question at that time", Toast.LENGTH_LONG).show()
//                }
            }

            override fun onFailure(call: Call<QuestionResponse>, t: Throwable) {
                // Log error here since request failed
                Log.e("question res", t.toString())
                DialogUtils.dismissDialog()
            }
        })
    }

    var waitTimer: CountDownTimer? = null
    var milliLeft: Long = 0

    private fun stopTimer() {
        if (waitTimer != null) {
            waitTimer!!.cancel()
            waitTimer = null
        }
    }

    private fun timerResume() {
//        Log.i("min", java.lang.Long.toString(min))
//        Log.i("Sec", java.lang.Long.toString(sec))
        setCountdown(milliLeft)
    }

    private fun setCountdown(minute: Long) {

//        val mili = TimeUnit.MINUTES.toMillis(minute.toLong())

        waitTimer = object : CountDownTimer(minute, 1000) {

            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                milliLeft = millisUntilFinished
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
                queTab_tvTimer.text = getString(com.testprep.R.string._00_00)
            }
        }.start()
    }

    private fun getLastPage() {

        if (queTab_viewpager.currentItem == movies.size - 1) {

            queTab_btnNext.text = getString(com.testprep.R.string.finish)

        } else {
            queTab_btnNext.text = getString(com.testprep.R.string.next)
        }
    }

    override fun onBackPressed() {

        DialogUtils.createConfirmDialog(
            this@TabwiseQuestionActivity,
            "Submit Test?",
            "are you sure you want to end this test?",
            "OK",
            "Cancel",
            DialogInterface.OnClickListener { dialog, which ->
                super.onBackPressed()

            },
            DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()


            }).show()
    }

}
