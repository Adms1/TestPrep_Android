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
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import com.testprep.R
import com.testprep.adapter.QuestionListSideMenuAdapter
import com.testprep.interfaces.FilterTypeSelectionInteface
import com.testprep.models.AnswerModel
import com.testprep.models.QuestionTypeModel
import com.testprep.old.adapter.SelectImageOptionAdapter
import com.testprep.old.models.QuestionResponse
import com.testprep.retrofit.WebClient
import com.testprep.retrofit.WebInterface
import com.testprep.utils.AppConstants
import com.testprep.utils.DialogUtils
import com.testprep.utils.WebRequests
import kotlinx.android.synthetic.main.activity_tabwise_question.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.util.concurrent.TimeUnit

/* 1. Single Selection(RadioButton)
 * 2. Fill the blanks
 * 3. Match the following
 * 4. True/false
 * 7. MCQ-Multiple Selection(Checkbox)*/

class TabwiseQuestionActivity : FragmentActivity(), FilterTypeSelectionInteface {

    var where = ""
    var reviewQue: ArrayList<Int> = ArrayList()

    var answer = ""
    var childList = HashMap<String, ArrayList<String>>()

    private var imgQue: ImageView? = null
    private var ansList: RecyclerView? = null

    var testid = ""
    var studenttestid = ""

    internal var mDrawerToggle: ActionBarDrawerToggle? = null

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setContentView(R.layout.activity_tabwise_question)

        testid = intent.getStringExtra("testid")
        studenttestid = intent.getStringExtra("studenttestid")

        queTab_header.text = intent.getStringExtra("testname")

        AppConstants.QUE_NUMBER = 0

        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        ansArr = ArrayList()
        sectionList = ArrayList()
        sectionList1 = ArrayList()

        filterTypeSelectionInteface = this

        context = this@TabwiseQuestionActivity

        imgQue = findViewById(R.id.page_img_que_img)
        ansList = findViewById(R.id.wv_question_list)
        nextButton = findViewById(R.id.queTab_btnNext)
        sideList = findViewById(R.id.queTab_expQueList)

        imgQue!!.isDrawingCacheEnabled = true

        queTab_ivBack.setOnClickListener {
            where = "testlist"
            onBackPressed()
        }

        queTab_ivSubmit.setOnClickListener {
            onBackPressed()
        }

        mDrawerToggle = ActionBarDrawerToggle(
            this, drawer_layout, R.mipmap.tc_logo, // nav menu toggle icon
            R.string.app_name, // nav drawer open - description for accessibility
            R.string.app_name
        )

        queTab_btnNextt.setOnClickListener {
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

        queTab_rbTruefalse.setOnCheckedChangeListener { group, checkedId ->

            if (checkedId == R.id.queTab_rbTrue) {
                nextButton!!.text = "Next"
//                answer = "1"

            } else if (checkedId == R.id.queTab_rbFalse) {
                nextButton!!.text = "Next"
//                answer = "0"
            }
        }

        queTab_tvFillBlanks.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {

                if (queTab_tvFillBlanks.text.toString() == "") {
                    nextButton!!.text = "Skip"
                } else {
                    nextButton!!.text = "Next"
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {

            }
        })

        queTab_btnNext.setOnClickListener {

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

//        queTab_btnPrevious.setOnClickListener { v: View? ->
//
//            if (AppConstants.QUE_NUMBER != 0) {
//                AppConstants.QUE_NUMBER = AppConstants.QUE_NUMBER - 1
//
//                Log.d("que_number", "" + AppConstants.QUE_NUMBER)
//
//                queTab_expQueList.adapter = QuestionListSideMenuAdapter(
//                    this@TabwiseQuestionActivity,
//                    sectionList!!,
//                    sectionList1!!,
//                    filterTypeSelectionInteface!!,
//                    "question"
//                )
//
//                getType(AppConstants.QUE_NUMBER)
//
//            }
//
////            queTab_viewpager.currentItem = queTab_viewpager.currentItem - 1
////            queTab_tvTotal.text = """${AppConstants.QUE_NUMBER - 1}/${movies.size}"""
//        }

//        ansList!!.isNestedScrollingEnabled = false

        ansList!!.layoutManager = LinearLayoutManager(this@TabwiseQuestionActivity, LinearLayoutManager.VERTICAL, false)

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

            onBackPressed()

        }

        if (testid != "") {
            callQuestionApi()
        }
    }

    fun callQuestionApi() {

        val sortDialog = Dialog(this@TabwiseQuestionActivity)//,R.style.PauseDialog);//, R.style.PauseDialog);
        val window = sortDialog.window
        val wlp = window!!.attributes
        sortDialog.window!!.attributes.verticalMargin = 0.10f
        wlp.gravity = Gravity.BOTTOM
        window.attributes = wlp

        sortDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        sortDialog.setCancelable(true)
//        sortDialog.setContentView(getRoot())
        sortDialog.show()

        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getQuestions(testid, studenttestid)
        call.enqueue(object : Callback<QuestionResponse> {
            override fun onResponse(call: Call<QuestionResponse>, response: Response<QuestionResponse>) {

                if (response.body()!!.Status == "true") {

                    setCountdown(30 * 60 * 1000)

                    movies = response.body()!!.data

//                    totall.text = "Total" + movies.size

//                    qno.text = "Q." + (countt+1)

                    queTab_tvTotal.text = "1/${movies.size}"

                    Log.d("qid", "" + movies[0].QuestionID)

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

//                            val answerModel = AnswerModel()
//                            answerModel.qid = movies[i].QuestionID
//                            answerModel.ansid = ""
//                            answerModel.ansresult = false
//                            ansArr.add(answerModel)

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
                                "question"
                            )

                        Picasso.get().load("http://content.testcraft.co.in/question/" + movies[0].QuestionImage)
//                                .resize(page_img_que_img.width, page_img_que_img.height)
                            .into(page_img_que_img)
//
//                            PageViewFragment.qsize = page_img_que_img.width

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

                        if (movies[0].QuestionTypeID == 1 || movies[0].QuestionTypeID == 7) {

                            queTab_tvFillBlanks.visibility = View.GONE
                            ansList!!.visibility = View.VISIBLE
                            queTab_rbTruefalse.visibility = View.GONE

                            val answerModel = AnswerModel()
                            answerModel.ansid = movies[0].Answer
                            answerModel.ansresult = true
                            ansArr.add(answerModel)

                            ansList!!.adapter = SelectImageOptionAdapter(
                                this@TabwiseQuestionActivity,
                                movies[0].StudentTestQuestionMCQ,
                                page_img_que_img.width,
                                movies[0].QuestionTypeID,
                                movies[0].QuestionID,
                                movies[0].Answer
                            )

                        } else if (movies[0].QuestionTypeID == 2) {

                            queTab_tvFillBlanks.visibility = View.VISIBLE
                            ansList!!.visibility = View.GONE
                            queTab_rbTruefalse.visibility = View.GONE

                            queTab_tvFillBlanks.setText(movies[0].Answer)

                        } else if (movies[0].QuestionTypeID == 4) {
                            queTab_rbTruefalse.visibility = View.VISIBLE
                            queTab_tvFillBlanks.visibility = View.GONE
                            ansList!!.visibility = View.GONE

                            if (movies[0].Answer == "1") {
                                queTab_rbTrue.isChecked = true
                            } else {
                                queTab_rbFalse.isChecked = true
                            }
                        }
                    }
                }

                sortDialog.dismiss()

                Log.d("imgcall", "Number of movies received: " + movies.size)

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

                    var ansstr = ""

                    for (i in 0 until ansArr.size) {
                        ansstr = ansstr + ansArr[i].qid + "|" + ansArr[i].ansid + ","

                    }

                    Log.d("ansstr", ansstr)

                    callSubmitAPI(ansstr.substring(0, ansstr.length - 1))

                },
                DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()

                }).show()
        }
    }

    fun callSubmitAPI(ansstr: String) {
//        val sortDialog = Dialog(this@TabwiseQuestionActivity)
//        sortDialog.setContentView(R.layout.progressbar_dialog)//,R.style.PauseDialog);//, R.style.PauseDialog);
//        val window = sortDialog.window
//        val wlp = window!!.attributes
//        sortDialog.window!!.attributes.verticalMargin = 0.10f
//        wlp.gravity = Gravity.BOTTOM
//        window.attributes = wlp
//
////        sortDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        sortDialog.setCancelable(true)
//        sortDialog.show()

        DialogUtils.showDialog(this@TabwiseQuestionActivity)

        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.submitTest(studenttestid)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                DialogUtils.dismissDialog()

                if (response.body()!!.get("Status").asString == "true") {

//                    Toast.makeText(this@TabwiseQuestionActivity, response.body()!!.get("Msg").asString , Toast.LENGTH_LONG).show()

                    val intent = Intent(this@TabwiseQuestionActivity, ResultActivity::class.java)
                    intent.putExtra("testid", testid)
                    intent.putExtra(
                        "marks",
                        response.body()!!.get("data").asJsonArray[0].asJsonObject.get("Correct").asString
                    )
                    intent.putExtra("studenttestid", studenttestid)
                    startActivity(intent)
                    finish()

                } else {

                    Toast.makeText(
                        this@TabwiseQuestionActivity,
                        response.body()!!.get("Msg").asString,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // Log error here since request failed
                Log.e("", t.toString())
                DialogUtils.dismissDialog()
            }
        })
    }

    fun callSubmitAnswer(testqueid: Int, queid: Int, quetypeid: Int, answerr: String, time: String, p0: Int) {

        DialogUtils.showDialog(this@TabwiseQuestionActivity)

        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.submitAnswer(
            WebRequests.submitAnswerParams(
                studenttestid,
                testqueid,
                queid,
                quetypeid,
                answerr,
                time
            )
        )
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                DialogUtils.dismissDialog()

                if (response.body()!!.get("Status").asString == "true") {

//                    answer = ""
//                    queTab_tvFillBlanks.setText("")
                    ansArr = ArrayList()

                    queTab_btnNext.visibility = View.VISIBLE

                    queTab_tvTotal.text = """${p0 + 1}/${movies.size}"""

                    if ("http://content.testcraft.co.in/question/" + movies[p0].QuestionImage != "") {

                        Picasso.get().load("http://content.testcraft.co.in/question/" + movies[p0].QuestionImage)
//                .resize(imgQue!!.width, qsize)
                            .into(imgQue)

//            PageViewFragment.qsize = page_img_que_img.width

                        ansList!!.layoutManager =
                            LinearLayoutManager(this@TabwiseQuestionActivity, LinearLayoutManager.VERTICAL, false)

                        if (movies[p0].QuestionTypeID == 1 || movies[p0].QuestionTypeID == 7) {

                            queTab_tvFillBlanks.visibility = View.GONE
                            ansList!!.visibility = View.VISIBLE
                            queTab_rbTruefalse.visibility = View.GONE

                            ansList!!.adapter = SelectImageOptionAdapter(
                                this@TabwiseQuestionActivity,
                                movies[p0].StudentTestQuestionMCQ,
                                imgQue!!.width,
                                movies[p0].QuestionTypeID,
                                movies[p0].QuestionID,
                                movies[p0].Answer
                            )

                        } else if (movies[p0].QuestionTypeID == 2) {
                            queTab_tvFillBlanks.visibility = View.VISIBLE
                            ansList!!.visibility = View.GONE
                            queTab_rbTruefalse.visibility = View.GONE

//                            if (queTab_tvFillBlanks.text.toString() == "") {

                            queTab_tvFillBlanks.setText(movies[p0].Answer)

//                            } else {
//                                answer = queTab_tvFillBlanks.text.toString()
//                            }

                        } else if (movies[p0].QuestionTypeID == 4) {
                            queTab_rbTruefalse.visibility = View.VISIBLE
                            queTab_tvFillBlanks.visibility = View.GONE
                            ansList!!.visibility = View.GONE

                            if (movies[p0].Answer == "1") {
                                queTab_rbTrue.isChecked = true
                            } else {
                                queTab_rbFalse.isChecked = true
                            }

                        }
                    }

                } else {

                    Toast.makeText(
                        this@TabwiseQuestionActivity,
                        response.body()!!.get("Msg").asString,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // Log error here since request failed
                Log.e("", t.toString())
                DialogUtils.dismissDialog()
            }
        })
    }

    override fun getType(itype: String, p0: Int) {

        drawer_layout.closeDrawer(Gravity.END)
        //                            if (queTab_tvFillBlanks.text.toString() == "") {

//                            } else {
//                                answer = queTab_tvFillBlanks.text.toString()
//                            }

        //                .resize(imgQue!!.width, qsize)

//            PageViewFragment.qsize = page_img_que_img.width
        when {
            movies[p0].QuestionTypeID == 1 -> {

                if (itype == "activity") {
                    answer = ansArr[0].ansid
                }

//                answer = answer.substring(0, answer.length)

            }
            movies[p0].QuestionTypeID == 7 -> {

                for (i in 0 until ansArr.size) {
                    answer = answer + ansArr[i].ansid + "|"
                }

                answer = answer.substring(0, answer.length)

            }
            movies[p0].QuestionTypeID == 2 -> {

                answer = queTab_tvFillBlanks.text.toString()

            }
            movies[p0].QuestionTypeID == 4 -> answer = if (queTab_rbFalse.isChecked) {
                "0"
            } else {
                "1"
            }
        }

        if (itype == "activity") {

            movies[p0].Answer = answer

            callSubmitAnswer(
                movies[p0].TestQuestionID,
                movies[p0].QuestionID,
                movies[p0].QuestionTypeID,
                answer,
                "10",
                p0 + 1
            )
        } else {
            queTab_tvTotal.text = """${p0 + 1}/${movies.size}"""

            if ("http://content.testcraft.co.in/question/" + movies[p0].QuestionImage != "") {

                Picasso.get().load("http://content.testcraft.co.in/question/" + movies[p0].QuestionImage)
//                .resize(imgQue!!.width, qsize)
                    .into(imgQue)

//            PageViewFragment.qsize = page_img_que_img.width

                ansList!!.layoutManager =
                    LinearLayoutManager(this@TabwiseQuestionActivity, LinearLayoutManager.VERTICAL, false)

                if (movies[p0].QuestionTypeID == 1 || movies[p0].QuestionTypeID == 7) {

                    queTab_tvFillBlanks.visibility = View.GONE
                    ansList!!.visibility = View.VISIBLE
                    queTab_rbTruefalse.visibility = View.GONE

                    ansList!!.adapter = SelectImageOptionAdapter(
                        this@TabwiseQuestionActivity,
                        movies[p0].StudentTestQuestionMCQ,
                        imgQue!!.width,
                        movies[p0].QuestionTypeID,
                        movies[p0].QuestionID,
                        movies[p0].Answer
                    )

                } else if (movies[p0].QuestionTypeID == 2) {
                    queTab_tvFillBlanks.visibility = View.VISIBLE
                    ansList!!.visibility = View.GONE
                    queTab_rbTruefalse.visibility = View.GONE

//                            if (queTab_tvFillBlanks.text.toString() == "") {

                    queTab_tvFillBlanks.setText(movies[p0].Answer)

//                            } else {
//                                answer = queTab_tvFillBlanks.text.toString()
//                            }

                } else if (movies[p0].QuestionTypeID == 4) {
                    queTab_rbTruefalse.visibility = View.VISIBLE
                    queTab_tvFillBlanks.visibility = View.GONE
                    ansList!!.visibility = View.GONE

                    if (movies[p0].Answer == "1") {
                        queTab_rbTrue.isChecked = true
                    } else {
                        queTab_rbFalse.isChecked = true
                    }
                }
            }
        }
    }

    fun getNextQuestion() {
        if ((movies.size - 1) > AppConstants.QUE_NUMBER) {
            AppConstants.QUE_NUMBER = AppConstants.QUE_NUMBER + 1

            getType("activity", AppConstants.QUE_NUMBER - 1)

            sideList!!.adapter = QuestionListSideMenuAdapter(
                context!!,
                sectionList!!,
                sectionList1!!,
                filterTypeSelectionInteface!!,
                "question"
            )

//            setSideMenu(1)
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
                "question"
            )
        }

        fun setButton(position: Int, ansid: String, qid: Int) {

            nextButton!!.text = "Next"

//            val answerModel = AnswerModel()
//            answerModel.ansid = ansid
//            answerModel.qid = qid
//            answerModel.ansresult = result

            if (movies[position].QuestionTypeID == 1) {

                ansArr = ArrayList()

                val answerModel = AnswerModel()
                answerModel.ansid = ansid
                answerModel.ansresult = true
                ansArr.add(answerModel)

            } else {

                if (ansArr.size > 0) {
                    for (i in 0 until ansArr.size) {
                        if (ansArr[i].qid == qid) {
                            ansArr[i].ansid = ansid
                            ansArr[i].ansresult = true
                        }
                    }
                } else {

                    val answerModel = AnswerModel()
                    answerModel.ansid = ansid
                    answerModel.ansresult = true
                    ansArr.add(answerModel)

                }
            }
        }
    }
}
