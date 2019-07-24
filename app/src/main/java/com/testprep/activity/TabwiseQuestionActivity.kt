package com.testprep.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.app.ActionBarDrawerToggle
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.Picasso
import com.testprep.R
import com.testprep.adapter.QuestionListSideMenuAdapter
import com.testprep.interfaces.FilterTypeSelectionInteface
import com.testprep.models.AnswerModel
import com.testprep.models.QuestionTypeModel
import com.testprep.old.PageActivity
import com.testprep.old.PageViewFragment
import com.testprep.old.adapter.SelectImageOptionAdapter
import com.testprep.old.adapter.SolutionAdapter
import com.testprep.old.models.QuestionResponse
import com.testprep.retrofit.WebClient
import com.testprep.retrofit.WebInterface
import com.testprep.utils.AppConstants
import com.testprep.utils.DialogUtils
import kotlinx.android.synthetic.main.activity_tabwise_question.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.util.concurrent.TimeUnit

class TabwiseQuestionActivity : FragmentActivity(), FilterTypeSelectionInteface {

    //    var questionpagerAdapret: QuestionsPagerAdapter? = null
//    var mToolbar: Toolbar? = null

    var where = ""
    var reviewQue: ArrayList<Int> = ArrayList()

    var childList = HashMap<String, ArrayList<String>>()

    private var que_list: ArrayList<QuestionResponse.QuestionList> = ArrayList()
    private var que_num = 0
    private var come = ""
    private var imgQue: ImageView? = null
    private var ansList: RecyclerView? = null

    var testid = ""
    var qsize = 0
    internal var mDrawerToggle: ActionBarDrawerToggle? = null

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

//    override fun onPostCreate(savedInstanceState: Bundle?) {
//        super.onPostCreate(savedInstanceState)
//        mDrawerToggle!!.syncState()
//    }

//    override fun onConfigurationChanged(newConfig: Configuration) {
//        super.onConfigurationChanged(newConfig)
//        // Pass any configuration change to the drawer toggls
//        mDrawerToggle!!.onConfigurationChanged(newConfig)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setContentView(R.layout.activity_tabwise_question)

        testid = intent.getStringExtra("testid")

        AppConstants.QUE_NUMBER = 0

        ansArr = ArrayList()
        sectionList = ArrayList()
        sectionList1 = ArrayList()

//        mToolbar = (Toolbar) findViewById(R.id.sliding_tabs)
//        sliding_tabs.setOnTabSelectedListener(this)
//        setSupportActionBar(mToolbar)

        filterTypeSelectionInteface = this

        context = this@TabwiseQuestionActivity

        imgQue = findViewById(R.id.page_img_que_img)
        ansList = findViewById(R.id.wv_question_list)
        nextButton = findViewById(R.id.queTab_btnNext)
        sideList = findViewById(R.id.queTab_expQueList)

        imgQue!!.isDrawingCacheEnabled = true

        queTab_ivBack.setOnClickListener { onBackPressed() }

        queTab_ivSubmit.setOnClickListener {
            onBackPressed()
        }

        mDrawerToggle = ActionBarDrawerToggle(
            this, drawer_layout, R.mipmap.tc_logo, // nav menu toggle icon
            R.string.app_name, // nav drawer open - description for accessibility
            R.string.app_name
        )

        package_btnNextt.setOnClickListener {
            drawer_layout.openDrawer(Gravity.END)
        }

        queTab_ivInfo.setOnClickListener {

            val dialog = Dialog(this@TabwiseQuestionActivity)
            dialog.setContentView(R.layout.dialog_question_information)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCanceledOnTouchOutside(true)
            dialog.show()
        }

        queTab_ivReview.setOnClickListener {

            setSideMenu(4)

            getNextQuestion()

        }

//        mDrawerToggle = ActionBarDrawerToggle(
//            this, drawer_layout, R.mipmap.tc_logo, // nav menu toggle icon
//            R.string.app_name, // nav drawer open - description for accessibility
//            R.string.app_name // nav drawer close - description for accessibility
//        ) {
//            @SuppressLint("NewApi")
//            override fun onDrawerClosed(view: View?) {
//                invalidateOptionsMenu()
//            }
//
//            @SuppressLint("NewApi")
//            override fun onDrawerOpened(drawerView: View?) {
//                invalidateOptionsMenu()
//            }
//        }
        drawer_layout.setDrawerListener(mDrawerToggle)

//        queTab_expQueList.setOnGroupClickListener(ExpandableListView.OnGroupClickListener { parent, v, groupPosition, id ->
//            //            if (headerList[groupPosition].isGroup) {
////                if (!headerList[groupPosition].hasChildren) {
////                    val webView = findViewById<WebView>(R.id.webView)
////                    webView.loadUrl(headerList[groupPosition].url)
////                    onBackPressed()
////                }
////            }
//
//            false
//        })

//        queTab_sliding_tabs.setupWithViewPager(queTab_viewpager)

        queTab_btnNext.setOnClickListener {
            //            getLastPage()
//            queTab_viewpager.currentItem = queTab_viewpager.currentItem + 1

            queTab_btnNext.visibility = View.GONE

            if (queTab_btnNext.text == "Next") {
                queTab_btnNext.text = "Skip"

                setSideMenu(2)
                getNextQuestion()

            } else {

                setSideMenu(3)

                getNextQuestion()
            }
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

            abortBtn.setOnClickListener {
                where = "testlist"
                onBackPressed()
            }

            dialog.show()

            queTab_ivPlayPause.isChecked = !isChecked

        }

        queTab_btnPrevious.setOnClickListener { v: View? ->

            if (AppConstants.QUE_NUMBER != 0) {
                AppConstants.QUE_NUMBER = AppConstants.QUE_NUMBER - 1

                Log.d("que_number", "" + AppConstants.QUE_NUMBER)

                queTab_expQueList.adapter = QuestionListSideMenuAdapter(
                    this@TabwiseQuestionActivity,
                    sectionList!!,
                    sectionList1!!,
                    filterTypeSelectionInteface!!,
                    AppConstants.QUE_NUMBER
                )

                getType(AppConstants.QUE_NUMBER)

            }

//            queTab_viewpager.currentItem = queTab_viewpager.currentItem - 1
//            queTab_tvTotal.text = """${AppConstants.QUE_NUMBER - 1}/${movies.size}"""
        }

        ansList!!.isNestedScrollingEnabled = false

        ansList!!.layoutManager = LinearLayoutManager(this@TabwiseQuestionActivity, LinearLayoutManager.VERTICAL, false)

//        Handler().postDelayed(
//            /* Runnable
//                 * Showing splash screen with a timer. This will be useful when you
//                 * want to show case your app logo / company
//                 */
//
//            {
//                // This method will be executed once the timer is over
//                // Start your app main activity
//                Log.d("sizeee", "" + imgQue!!.width + ", " + imgQue!!.height)
//
//                Picasso.get().load("http://content.testcraft.co.in/question/" + que_list[que_num].QuestionImage)
//
//                    .resize(imgQue!!.width, imgQue!!.height)
////            .transform(imageTransform(200, true))
//                    .into(imgQue)
//
////        qsize = page_img_que_img.width
//
//                if (this@TabwiseQuestionActivity != null) {
//
//                    if (come != "solution") {
//
//                        ansList!!.adapter = SelectImageOptionAdapter(
//                            this@TabwiseQuestionActivity,
//                            que_list[que_num].StudentTestQuestionMCQ,
//                            imgQue!!.width
//                        )
//                    } else {
//                        ansList!!.adapter = SolutionAdapter(
//                            this@TabwiseQuestionActivity,
//                            que_list[que_num].StudentTestQuestionMCQ,
//                            imgQue!!.width
//                        )
//                    }
//                }
//
//            }, 1000
//        )


//        queTab_viewpager.addOnPageChangeListener(object : OnPageChangeListener {
//
//            // This method will be invoked when a new page becomes selected.
//            override fun onPageSelected(position: Int) {
//                queTab_tvTotal.text = """${position + 1}/${movies.size}"""
//
////                for (i in 0 until reviewQue.size){
////                    if(reviewQue.contains(position + 1)){
////                        queTab_ivReview.background = resources.getDrawable(R.drawable.rotate_eye_blue)
////                        queTab_ivReview.isSelected = true
//
////                }else{
////                    queTab_ivReview.background = resources.getDrawable(R.drawable.rotate_eye_gray)
////
////                }
////                }
//
//                getLastPage()
//            }
//
//            // This method will be invoked when the current page is scrolled
//            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//                // Code goes here
////                getLastPage()
//            }
//
//            // Called when the scroll state changes:
//            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
//            override fun onPageScrollStateChanged(state: Int) {
//                // Code goes here
////                getLastPage()
//            }
//        })

//        queTab_ivReview.setOnClickListener {
//
//            queTab_sliding_tabs.getTabAt(queTab_viewpager.currentItem)!!.setCustomView(R.layout.custom_tab)
//
//            if(reviewQue.contains(queTab_viewpager.currentItem)){
//
//                queTab_ivReview.background = resources.getDrawable(R.drawable.rotate_eye_gray)
//                reviewQue.remove(queTab_viewpager.currentItem)
//
//                queTab_ivReview.isSelected = false
//
//            }else{
////                queTab_sliding_tabs.getTabAt(queTab_viewpager.currentItem)!!.setCustomView(R.layout.custom_tab_normal)
//
//                queTab_ivReview.background = resources.getDrawable(R.drawable.rotate_eye_blue)
//                reviewQue.add(queTab_viewpager.currentItem)
//
//                queTab_ivReview.isSelected = true
//
//            }
//
////            if(queTab_ivReview.isPressed){
////
////                queTab_sliding_tabs.getTabAt(queTab_viewpager.currentItem)!!.setCustomView(R.layout.custom_tab)
////
////            }else{
////                queTab_sliding_tabs.getTabAt(queTab_viewpager.currentItem)!!.setCustomView(R.layout.custom_tab_normal)
////            }
//        }

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

        if (testid != "") {
            callQuestionApi()
        }
    }

//    var mTabLayout_config: Runnable = Runnable {
//        if (queTab_sliding_tabs.width < this@TabwiseQuestionActivity.resources.displayMetrics.widthPixels) {
//            queTab_sliding_tabs.tabMode = TabLayout.MODE_FIXED
//            val mParams = queTab_sliding_tabs.layoutParams
//            mParams.width = ViewGroup.LayoutParams.MATCH_PARENT
//            queTab_sliding_tabs.layoutParams = mParams
//
//        } else {
//            queTab_sliding_tabs.tabMode = TabLayout.MODE_SCROLLABLE
//        }
//    }

//    fun onLeft() {
//        // TODO Auto-generated method stub
//        //        mDrawerList.setSelectionAfterHeaderView();
//        drawer_layout.openDrawer(leftRl)
//    }

    fun callQuestionApi() {

        val sortDialog = Dialog(this@TabwiseQuestionActivity)//,R.style.PauseDialog);//, R.style.PauseDialog);
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

        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getQuestions(intent.getStringExtra("testid"))
        call.enqueue(object : Callback<QuestionResponse> {
            override fun onResponse(call: Call<QuestionResponse>, response: Response<QuestionResponse>) {

                if (response.body()!!.Status == "true") {

                    setCountdown(30 * 60 * 1000)

                    movies = response.body()!!.data

//                    totall.text = "Total" + movies.size

//                    qno.text = "Q." + (countt+1)

                    queTab_tvTotal.text = "1/${movies.size}"

                    Log.d("qid", "" + movies[0].QuestionID)

                    if (PageActivity.countt >= 0) {
                        if ("http://content.testcraft.co.in/question/" + movies[0].QuestionImage != "") {

//                            var url = URL("http://content.testcraft.co.in/question/" + movies[pos].titleimg)
//                            var bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())

//                            var widhtx  = bmp.width
//                            var heightx = bmp.height

//                            Log.d("imgsize", "widht" + widhtx + "  height" + heightx)

                            // setting list adapter


                            sectionList!!.add("Section 1")
//                            sectionList.add("Section 2")
//                            sectionList.add("Section 3")
//                            sectionList.add("Section 4")


//                            sectionList1.add("I")
//                            sectionList1.add("II")
//                            sectionList1.add("III")
//                            sectionList1.add("IV")
//                            sectionList1.add("V")
//                            sectionList1.add("VI")
//                            sectionList1.add("VII")
//                            sectionList1.add("VIII")
//                            sectionList1.add("IX")
//                            sectionList1.add("X")

                            for (i in 0 until movies.size) {
                                val questionTypeModel = QuestionTypeModel()
                                questionTypeModel.qnumber = i
                                questionTypeModel.type = 5
                                sectionList1!!.add(questionTypeModel)
                            }

                            Log.d("header", "" + sectionList)
                            Log.d("child", "" + childList)

                            queTab_expQueList.layoutManager =
                                LinearLayoutManager(this@TabwiseQuestionActivity, LinearLayoutManager.VERTICAL, false)

                            queTab_expQueList.adapter =
                                QuestionListSideMenuAdapter(
                                    this@TabwiseQuestionActivity,
                                    sectionList!!,
                                    sectionList1!!,
                                    filterTypeSelectionInteface!!,
                                    -1
                                )

                            Picasso.get().load("http://content.testcraft.co.in/question/" + movies[0].QuestionImage)
//                                .resize(page_img_que_img.width, page_img_que_img.height)
                                .into(page_img_que_img)
//
                            PageViewFragment.qsize = page_img_que_img.width

//                            Log.d("imgsize", "widht" + page_img_que_img.width + "  height" + page_img_que_img.height)
//                            Picasso.get().load("https://homeshealth.info/wp-content/uploads/2018/02/classy-algebra-distance-formula-problems-in-distance-formula-of-algebra-distance-formula-problems.jpg")
//                                .resize(page_img_que_img.width, page_img_que_img.height)
//                                .into(page_img_que_img)

//
//                        val photoPath = Environment.getExternalStorageDirectory().toString() + "testcraftimg/pic.jpg"
//                        val options = BitmapFactory.Options()
//                        options.inSampleSize = 8
//                        val b = BitmapFactory.decodeFile(photoPath, options)
//                        page_img_que_img.setImageBitmap(b)

                            ansList!!.layoutManager =
                                LinearLayoutManager(this@TabwiseQuestionActivity, LinearLayoutManager.VERTICAL, false)

                            if (come != "solution") {

                                ansList!!.adapter = SelectImageOptionAdapter(
                                    this@TabwiseQuestionActivity,
                                    movies[0].StudentTestQuestionMCQ,
                                    page_img_que_img.width,
                                    movies[0].QuestionID
                                )
                            } else {
                                ansList!!.adapter = SolutionAdapter(
                                    this@TabwiseQuestionActivity,
                                    movies[0].StudentTestQuestionMCQ,
                                    page_img_que_img.width
                                )
                            }


                        }
                    } else {
                        if ("http://content.testcraft.co.in/question/" + movies[0].QuestionImage != "") {
                            Picasso.get().load("http://content.testcraft.co.in/question/" + movies[0].QuestionImage)
//                                .resize(page_img_que_img.width, page_img_que_img.height)
                                .into(page_img_que_img)

                        }

                        ansList!!.layoutManager =
                            LinearLayoutManager(this@TabwiseQuestionActivity, LinearLayoutManager.VERTICAL, false)

                        if (come != "solution") {

                            ansList!!.adapter = SelectImageOptionAdapter(
                                this@TabwiseQuestionActivity,
                                movies[0].StudentTestQuestionMCQ,
                                page_img_que_img.width,
                                movies[0].QuestionID
                            )
                        } else {
                            ansList!!.adapter = SolutionAdapter(
                                this@TabwiseQuestionActivity,
                                movies[0].StudentTestQuestionMCQ,
                                page_img_que_img.width
                            )
                        }

                    }

//                    html_text.setHtml(movies[pos].titlehtml,
//                        HtmlHttpImageGetter(html_text)
//                    );

//                    val url = URL("http://content.testcraft.co.in/question/9b734bff-db1d-42f5-8c54-0cf96612193d.jpeg")
//                    val bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())
//
//                    val drawable = BitmapDrawable(resources, bmp)
//
//                    page_img_que_img.setImageDrawable(drawable)

//                    DownLoadImageTask(page_img_que_img).execute("https://www.jagranjosh.com/imported/images/E/Articles/JEE_integrals.jpg");

//                    page_img_que_img.setImageDrawable(activity!!.resources.getDrawable(R.drawable.pic))

                    sortDialog.dismiss()

                    Log.d("imgcall", "Number of movies received: " + movies.size)
                } else {
                    sortDialog.dismiss()
                    Toast.makeText(this@TabwiseQuestionActivity, "No Question at that time", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<QuestionResponse>, t: Throwable) {
                // Log error here since request failed
                Log.e("", t.toString())
                sortDialog.dismiss()
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
                queTab_tvTimer.text = getString(R.string._00_00)
            }
        }.start()
    }

    private fun getLastPage() {

        if (queTab_viewpager.currentItem == movies.size - 1) {

            queTab_btnNext.text = getString(R.string.finish)

        } else {
            queTab_btnNext.text = getString(R.string.next)
        }
    }

    override fun onBackPressed() {

        if (where == "testlist") {
            super.onBackPressed()
        } else {
            DialogUtils.createConfirmDialog(
                this@TabwiseQuestionActivity,
                "Submit Test?",
                "Are you sure you want to submit this test?",
                "OK",
                "Cancel",
                DialogInterface.OnClickListener { dialog, which ->
                    val intent = Intent(this@TabwiseQuestionActivity, ResultActivity::class.java)
                    startActivity(intent)

                    finish()
                },
                DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()


                }).show()
        }
    }

    override fun getType(p0: Int) {

        drawer_layout.closeDrawer(Gravity.END)

        queTab_tvTotal.text = """${p0 + 1}/${movies.size}"""

        if ("http://content.testcraft.co.in/question/" + movies[p0].QuestionImage != "") {

            Picasso.get().load("http://content.testcraft.co.in/question/" + movies[p0].QuestionImage)
//                .resize(imgQue!!.width, qsize)
                .into(imgQue)

//            PageViewFragment.qsize = page_img_que_img.width

            ansList!!.layoutManager =
                LinearLayoutManager(this@TabwiseQuestionActivity, LinearLayoutManager.VERTICAL, false)

            ansList!!.adapter = SelectImageOptionAdapter(
                this@TabwiseQuestionActivity,
                movies[p0].StudentTestQuestionMCQ,
                imgQue!!.width,
                movies[p0].QuestionID
            )
        }

        queTab_btnNext.visibility = View.VISIBLE

    }

    fun getNextQuestion() {
        if ((movies.size - 1) > AppConstants.QUE_NUMBER) {
            AppConstants.QUE_NUMBER = AppConstants.QUE_NUMBER + 1

            getType(AppConstants.QUE_NUMBER)

            setSideMenu(1)
        }
    }

    companion object {

        var nextButton: Button? = null
        var sideList: RecyclerView? = null
        var sectionList: ArrayList<String>? = null
        var sectionList1: ArrayList<QuestionTypeModel>? = null
        var filterTypeSelectionInteface: FilterTypeSelectionInteface? = null
        var movies: ArrayList<QuestionResponse.QuestionList> = ArrayList()
        var ansArr: ArrayList<AnswerModel> = ArrayList()

        var context: Context? = null

        fun setSideMenu(type: Int) {
            for (i in 0 until sectionList1!!.size) {
                if (sectionList1!![i].qnumber == AppConstants.QUE_NUMBER) {
                    sectionList1!![i].type = type
                }
            }

            sideList!!.adapter = QuestionListSideMenuAdapter(
                context!!,
                sectionList!!,
                sectionList1!!,
                filterTypeSelectionInteface!!,
                AppConstants.QUE_NUMBER
            )
        }

        fun setButton(ansid: String, qid: String, result: Boolean) {
            nextButton!!.text = "Next"

            val answerModel = AnswerModel()
            answerModel.ansid = ansid
            answerModel.qid = qid
            answerModel.ansresult = result

            ansArr.add(answerModel)

        }
    }

}
