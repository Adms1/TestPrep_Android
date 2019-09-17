package com.testprep.sectionmodule

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.AsyncTask
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.support.v4.app.ActionBarDrawerToggle
import android.support.v4.app.FragmentActivity
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.View
import android.webkit.WebView
import android.widget.*
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import com.testprep.R
import com.testprep.activity.DashboardActivity
import com.testprep.activity.ResultActivity
import com.testprep.interfaces.FilterTypeSelectionInteface
import com.testprep.models.AnswerModel
import com.testprep.models.QuestionTypeModel
import com.testprep.old.adapter.SelectImageOptionAdapter
import com.testprep.retrofit.WebClient
import com.testprep.retrofit.WebInterface
import com.testprep.utils.AppConstants
import com.testprep.utils.DialogUtils
import com.testprep.utils.WebRequests
import com.testprep.utils.transform
import kotlinx.android.synthetic.main.activity_tabwise_question.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.TimeUnit

class NewTabQuestionActivity : FragmentActivity(), FilterTypeSelectionInteface {

    var where = ""
    var reviewQue: ArrayList<Int> = ArrayList()

    var answer = ""
    var childList = HashMap<String, ArrayList<String>>()

    var hintData = ""
    private var imgQue: ImageView? = null
    private var ansList: RecyclerView? = null

    var testid = ""
    var studenttestid = ""
    var testtime = ""

    var testname = ""
    var testmarks = ""
    var testcourse = ""
    var testque = ""
    var testtutor = ""
    var testsubject = ""

    var shouldExecuteOnResume: Boolean? = null

    internal var mDrawerToggle: ActionBarDrawerToggle? = null

    var qtime = 0
    var continuetime = 0
    var que_number = 1

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setContentView(R.layout.activity_tabwise_question)

        shouldExecuteOnResume = false

        testid = intent.getStringExtra("testid")
        testtime = intent.getStringExtra("testtime")
        studenttestid = intent.getStringExtra("studenttestid")

        queTab_header.text = intent.getStringExtra("testname")

        testname = intent.getStringExtra("testname")
        testque = intent.getStringExtra("totalque")
        testsubject = intent.getStringExtra("subjectname")
        testcourse = intent.getStringExtra("coursename")
        testmarks = intent.getStringExtra("totalmarks")
        testtutor = intent.getStringExtra("tutorname")

        curr_index = 0
        q_grppos1 = 0

        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        ansArr = ArrayList()
        sectionList = ArrayList()
        finalArr = HashMap()
//        sectionList1 = ArrayList()

        filterTypeSelectionInteface = this

        context = this@NewTabQuestionActivity

        imgQue = findViewById(R.id.page_img_que_img)
        ansList = findViewById(R.id.wv_question_list)
        nextButton = findViewById(R.id.queTab_btnNext)
        sideList = findViewById(R.id.queTab_expQueList)

        imgQue!!.isDrawingCacheEnabled = true

        queTab_ivBack.setOnClickListener {

            //            stopTimer()

            onBackPressed()
        }

        queTab_ivSubmit.setOnClickListener {
            DialogUtils.createConfirmDialog(
                this@NewTabQuestionActivity,
                "Done?",
                "Are you sure you want to submit this test?",
                "OK",
                "Cancel",
                DialogInterface.OnClickListener { dialog, which ->

                    var ansstr = ""

                    for (i in 0 until ansArr.size) {
                        ansstr = ansstr + ansArr[i].qid + "|" + ansArr[i].ansid + ","

                    }

                    Log.d("ansstr", ansstr)

                    callSubmitAPI()

                },
                DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()

                }).show()
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

            val dialog = Dialog(this@NewTabQuestionActivity)
            dialog.setContentView(R.layout.dialog_question_information)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCanceledOnTouchOutside(true)

            var testname1: TextView = dialog.findViewById(R.id.dialog_queinfo_tvHeader)
            var subjectname: TextView = dialog.findViewById(R.id.dialog_queinfo_tvSubjectName)
            var course: TextView = dialog.findViewById(R.id.dialog_queinfo_tvCourse)
            var marks: TextView = dialog.findViewById(R.id.dialog_queinfo_tvTotalMarks)
            var totalque: TextView = dialog.findViewById(R.id.dialog_queinfo_tvQue)
            var tutorname: TextView = dialog.findViewById(R.id.dialog_queinfo_tvTeacherName)

            testname1.text = testname
            subjectname.text = testsubject
            course.text = testcourse
            marks.text = testmarks
            totalque.text = testque
            tutorname.text = testtutor

            dialog.show()
        }

        wv_question_list.isNestedScrollingEnabled = false

        ansList!!.setOnClickListener {
            if (nextButton!!.text != "Submit Test") {
                setNextSkipButtonText(1)
            }
        }

        queTab_expQueList.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->

            //            q_grppos1 = groupPosition

            false

        }

        queTab_ivHint.setOnClickListener {

            val dialog = Dialog(this@NewTabQuestionActivity)
            dialog.setContentView(R.layout.hint_dialog)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCanceledOnTouchOutside(false)

//            var hintHeader: TextView = dialog.findViewById(R.id.dialog_hint_tvHint)
            val hintWebview: WebView = dialog.findViewById(R.id.dialog_hint_wvHint)
//            val hintExplanation: TextView = dialog.findViewById(R.id.dialog_hint_tvExplanation)
//            val explanationWebview: WebView = dialog.findViewById(R.id.dialog_hint_wvExplanation)
//            val line1: View = dialog.findViewById(R.id.dialog_hint_viewLine1)
            val closeBtn: View = dialog.findViewById(R.id.dialog_hint_btnClose)

//            hintExplanation.visibility = View.GONE
//            explanationWebview.visibility = View.GONE
//            line1.visibility = View.GONE

            hintWebview.settings.javaScriptEnabled = true
            hintWebview.loadDataWithBaseURL("", hintData, "text/html", "UTF-8", "")

            closeBtn.setOnClickListener { dialog.dismiss() }

            dialog.show()
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

//        queTab_rbTruefalse.setOnCheckedChangeListener { group, checkedId ->

        queTab_rbTrue.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked) {
                queTab_rbTrue.isChecked = true
                queTab_rbFalse.isChecked = false

                if (nextButton!!.text != "Submit Test") {
                    setNextSkipButtonText(1)
                }
            }

        }

        queTab_rbFalse.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked) {
                queTab_rbTrue.isChecked = false
                queTab_rbFalse.isChecked = true

                if (nextButton!!.text != "Submit Test") {
                    setNextSkipButtonText(1)
                }
            }

        }

        queTab_tvFillBlanks.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {

                if (queTab_tvFillBlanks.text.toString() == "") {
                    setNextSkipButtonText(0)
                } else {
                    setNextSkipButtonText(1)
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

        nextButton!!.setOnClickListener {

            nextButtonClick()

            queTab_tvCurrTotal.text = que_number.toString()
        }

//        queTab_ivPlayPause.setOnCheckedChangeListener { buttonView, isChecked ->
//
//            stopTimer()
//
//            val dialog = Dialog(this@NewTabQuestionActivity)
//            dialog.setContentView(R.layout.play_pause_alertdialog)
//            dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
////            var wmlp: WindowManager.LayoutParams = dialog.window.attributes
////            wmlp.width = WindowManager.LayoutParams.MATCH_PARENT
////            wmlp.height = WindowManager.LayoutParams.WRAP_CONTENT
//            dialog.window!!.setGravity(Gravity.BOTTOM)
//            dialog.setCanceledOnTouchOutside(false)
//
//            val cancelBtn: TextView = dialog.findViewById(R.id.dialog_tvCancel)
//            val resumeBtn: TextView = dialog.findViewById(R.id.dialog_tvResume)
//            val abortBtn: TextView = dialog.findViewById(R.id.dialog_tvAbort)
//
//            cancelBtn.setOnClickListener {
//                dialog.dismiss()
//                timerResume()
//            }
//
//            resumeBtn.setOnClickListener {
//                timerResume()
//                dialog.dismiss()
//            }
//
//            abortBtn.setOnClickListener {
//                onBackPressed()
//            }
//
//            dialog.show()
//
//            queTab_ivPlayPause.isChecked = !isChecked
//
//        }

        ansList!!.layoutManager = LinearLayoutManager(this@NewTabQuestionActivity, LinearLayoutManager.VERTICAL, false)

//        queTab_tvSubmit.setOnClickListener {
//
//            onBackPressed()
//
//        }

        if (testid != "") {
            callQuestionApi()
        }
    }

    fun callQuestionApi() {

//        val sortDialog = Dialog(this@NewTabQuestionActivity)//,R.style.PauseDialog);//, R.style.PauseDialog);
//        val window = sortDialog.window
//        val wlp = window!!.attributes
//        sortDialog.window!!.attributes.verticalMargin = 0.10f
//        wlp.gravity = Gravity.BOTTOM
//        window.attributes = wlp
//
//        sortDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        sortDialog.setCancelable(true)
////        sortDialog.setContentView(getRoot())
//        sortDialog.show()

        DialogUtils.showDialog(this@NewTabQuestionActivity)

        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getNewQuestions(testid, studenttestid)
        call.enqueue(object : Callback<NewQuestionResponse> {
            override fun onResponse(call: Call<NewQuestionResponse>, response: Response<NewQuestionResponse>) {

                if (response.body()!!.Status == "true") {

                    var time = testtime.toFloat()

                    queTab_tvCurrTotal.text = que_number.toString()
                    queTab_tvTotal.text = "/" + testque

                    if (time > 0) {
                        setCountdown(time.toLong() * 1000)
                    } else {
                        DialogUtils.createConfirmDialog1(
                            this@NewTabQuestionActivity,
                            "Submit",
                            "Your test time is over",

                            DialogInterface.OnClickListener { dialog, which ->

                                var ansstr = ""

                                for (i in 0 until ansArr.size) {
                                    ansstr = ansstr + ansArr[i].qid + "|" + ansArr[i].ansid + ","

                                }

                                Log.d("ansstr", ansstr)

                                callSubmitAPI()

                            }).show()
                    }

                    movies = response.body()!!.data

//                    totall.text = "Total" + movies.size

//                    qno.text = "Q." + (countt+1)

                    if (movies.size > 0) {

//                        queTab_tvTotal.text = "1/${movies.size}"

                        queTab_tvQMarks.text = "Marks : " + movies[0].TestQuestion[0].Marks

                        hintData =
                            "<html><body style='background-color:clear;'><p align=center><font size=4><b>" + "Hint" + "</b></font></p><p><font size=2>" + movies[0].TestQuestion[0].Hint + "</font></p></body></html>"

                        Log.d("qid", "" + movies[0].SectionID)

                        for (i in 0 until movies.size) {
                            val ansmodel = AnswerModel()
//                        ansmodel.qid = movies[i].SectionID
                            ansmodel.ansid = movies[i].SectionName
                            sectionList!!.add(movies[i].SectionName)
                        }

//                    sectionList!!.add("Section 1")
//                    sectionList!!.add("Section 2")
//                    sectionList!!.add("Section 3")
//                    sectionList!!.add("Section 4")

//                    queTab_expQueList.layoutManager =
//                        LinearLayoutManager(this@NewTabQuestionActivity, LinearLayoutManager.VERTICAL, false)

                        if ("http://content.testcraft.co.in/question/" + movies[0].TestQuestion[0].QuestionImage != "") {

                            if (movies[0].TestQuestion[0].Answer != "") {

                                setNextSkipButtonText(1)

                            } else {

                                setNextSkipButtonText(0)
                            }

                            for (i in 0 until movies.size) {
//
                                var sectionList1: ArrayList<QuestionTypeModel> = ArrayList()

                                for (j in 0 until movies[i].TestQuestion.size) {
                                    val questionTypeModel = QuestionTypeModel()
                                    questionTypeModel.qnumber = j

                                    if (movies[i].TestQuestion[j].Answer != "") {

                                        questionTypeModel.type = 2

                                    } else {

                                        questionTypeModel.type = 5
                                    }

                                    sectionList1.add(questionTypeModel)

                                }
                                finalArr[movies[i].SectionName] = sectionList1
//
                            }

                            queTab_expQueList.setAdapter(
                                NewSideMenuAdapter(
                                    this@NewTabQuestionActivity,
                                    sectionList!!,
                                    finalArr,
                                    filterTypeSelectionInteface!!,
                                    "question"
                                )
                            )

                            Log.d("header", "" + sectionList)
                            Log.d("child", "" + childList)

                            Picasso.get()
                                .load("http://content.testcraft.co.in/question/" + movies[0].TestQuestion[0].QuestionImage)
                                .transform(transform.getTransformation(imgQue!!))
                                .into(imgQue)
//
                            Log.d("qsize", "width: " + page_img_que_img.width + ", height" + page_img_que_img.height)

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
                                LinearLayoutManager(this@NewTabQuestionActivity, LinearLayoutManager.VERTICAL, false)

                            if (movies[0].TestQuestion[0].QuestionTypeID == 1 || movies[0].TestQuestion[0].QuestionTypeID == 7) {

                                queTab_tvFillBlanks.visibility = View.GONE
                                ansList!!.visibility = View.VISIBLE
                                queTab_rbTrue.visibility = View.GONE
                                queTab_rbFalse.visibility = View.GONE

                                val answerModel = AnswerModel()
                                answerModel.ansid = movies[0].TestQuestion[0].Answer
                                answerModel.ansresult = true
                                ansArr.add(answerModel)

                                ansList!!.adapter = SelectImageOptionAdapter(
                                    this@NewTabQuestionActivity,
                                    movies[0].TestQuestion[0].StudentTestQuestionMCQ,
                                    page_img_que_img.width,
                                    movies[0].TestQuestion[0].QuestionTypeID,
                                    movies[0].TestQuestion[0].QuestionID,
                                    movies[0].TestQuestion[0].Answer
                                )

                            } else if (movies[0].TestQuestion[0].QuestionTypeID == 2 || movies[0].TestQuestion[0].QuestionTypeID == 8) {

                                if (movies[0].TestQuestion[0].QuestionTypeID == 8) {
                                    queTab_tvFillBlanks.inputType = InputType.TYPE_CLASS_NUMBER
                                }

                                queTab_tvFillBlanks.visibility = View.VISIBLE
                                ansList!!.visibility = View.GONE
                                queTab_rbTrue.visibility = View.GONE
                                queTab_rbFalse.visibility = View.GONE

                                queTab_tvFillBlanks.setText(movies[0].TestQuestion[0].Answer)

                            } else if (movies[0].TestQuestion[0].QuestionTypeID == 4) {
                                queTab_rbTrue.visibility = View.VISIBLE
                                queTab_rbFalse.visibility = View.VISIBLE
                                queTab_tvFillBlanks.visibility = View.GONE
                                ansList!!.visibility = View.GONE

                                if (movies[0].TestQuestion[0].Answer == "1") {
                                    queTab_rbTrue.isChecked = true
                                    queTab_rbFalse.isChecked = false

                                } else if (movies[0].TestQuestion[0].Answer == "0") {
                                    queTab_rbFalse.isChecked = true
                                    queTab_rbTrue.isChecked = false
                                } else {
                                    queTab_rbTrue.isChecked = false
                                    queTab_rbFalse.isChecked = false
                                }
                            }
                        }
                    }
                }

                Handler().postDelayed(
                    /* Runnable
                         * Showing splash screen with a timer. This will be useful when you
                         * want to show case your app logo / company
                         */

                    {
                        DialogUtils.dismissDialog()
                    }, 1500
                )
                Log.d("imgcall", "Number of movies received: " + movies.size)

            }

            override fun onFailure(call: Call<NewQuestionResponse>, t: Throwable) {
                // Log error here since request failed
                Log.e("", t.toString())
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

    override fun onPause() {
        super.onPause()

        stopTimer()
    }

    override fun onResume() {
        super.onResume()

        if (shouldExecuteOnResume!!) {
            timerResume()
        } else {
            shouldExecuteOnResume = true
        }


    }

    private fun timerResume() {
//        Log.i("min", java.lang.Long.toString(min))
//        Log.i("Sec", java.lang.Long.toString(sec))
        setCountdown(milliLeft)
//        setCountdown(500)
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

                if (qtime > 0 && qtime % 10 == 0) {

                    Log.d("10 second", "" + qtime)

                    getType("continue", q_grppos1, curr_index)

//                    callEvery10(
//                        movies[q_grppos1].TestQuestion[curr_index].TestQuestionID,
//                        movies[q_grppos1].TestQuestion[curr_index].QuestionID,
//                        movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID,
//                        answer
//                    )

                }

                if (continuetime == 60) {

                    DialogUtils.createConfirmDialog(this@NewTabQuestionActivity, "",
                        "Do you require more time in this question?",
                        "Yes", "No",

                        DialogInterface.OnClickListener { dialog, which ->

                            continuetime = 0

                            dialog.dismiss()

                        },
                        DialogInterface.OnClickListener { dialog, which ->

                            continuetime = 0

                            nextButtonClick()

                        }).show()

                } else if (continuetime == 120) {

                    continuetime = 0

                    AppConstants.isFirst = 12
                    val intent = Intent(this@NewTabQuestionActivity, DashboardActivity::class.java)
                    startActivity(intent)
                    finish()

                }

                qtime += 1
                continuetime += 1

                Log.d("question_time", "" + qtime)
                Log.d("continue time", "" + continuetime)
            }

            override fun onFinish() {
                queTab_tvTimer.text = getString(R.string._00_00)

                DialogUtils.createConfirmDialog(
                    this@NewTabQuestionActivity,
                    "Done?",
                    "Are you sure you want to submit this test?",
                    "OK",
                    "Cancel",
                    DialogInterface.OnClickListener { dialog, which ->

                        var ansstr = ""

                        for (i in 0 until ansArr.size) {
                            ansstr = ansstr + ansArr[i].qid + "|" + ansArr[i].ansid + ","

                        }

                        Log.d("ansstr", ansstr)

                        callSubmitAPI()

                    },
                    DialogInterface.OnClickListener { dialog, which ->
                        dialog.dismiss()

                    }).show()

            }
        }.start()
    }

    override fun onBackPressed() {

//        AppConstants.isFirst = 1
//        val intent = Intent(this@NewTabQuestionActivity, DashboardActivity::class.java)
//        startActivity(intent)

        DialogUtils.createConfirmDialog(
            this@NewTabQuestionActivity,
            "Resume?",
            "Are you sure you want to resume a test?",
            "OK",
            "Cancel",
            DialogInterface.OnClickListener { dialog, which ->

                AppConstants.isFirst = 12
                val intent = Intent(this@NewTabQuestionActivity, DashboardActivity::class.java)
                startActivity(intent)
                finish()

            },
            DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()

            }).show()

    }

    fun callSubmitAPI() {
//        val sortDialog = Dialog(this@NewTabQuestionActivity)
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

        DialogUtils.showDialog(this@NewTabQuestionActivity)

        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.submitTest(studenttestid)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                DialogUtils.dismissDialog()

                if (response.body()!!.get("Status").asString == "true") {

//                    Toast.makeText(this@NewTabQuestionActivity, response.body()!!.get("Msg").asString , Toast.LENGTH_LONG).show()

                    val intent = Intent(this@NewTabQuestionActivity, ResultActivity::class.java)
                    intent.putExtra("testid", testid)
                    intent.putExtra("testname", testname)

                    intent.putExtra(
                        "marks",
                        response.body()!!.get("data").asJsonArray[0].asJsonObject.get("TotalGetMarks").asString
                    )
                    intent.putExtra(
                        "display_totalmarks",
                        response.body()!!.get("data").asJsonArray[0].asJsonObject.get("TotalGetMarks").asString + "/" + response.body()!!.get(
                            "data"
                        ).asJsonArray[0].asJsonObject.get("TotalMarks").asString
                    )
                    intent.putExtra("studenttestid", studenttestid)
                    intent.putExtra(
                        "totalmarks",
                        response.body()!!.get("data").asJsonArray[0].asJsonObject.get("TotalMarks").asString
                    )
                    startActivity(intent)
                    finish()

                } else {

                    Toast.makeText(
                        this@NewTabQuestionActivity,
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

    class callEvery10(callback: (String?) -> Unit) : AsyncTask<String, Unit, String>() {

        var callback = callback

        override fun doInBackground(vararg params: String?): String? {
            val url = URL(params[1])
            val httpClient = url.openConnection() as HttpURLConnection
            httpClient.readTimeout = 30
            httpClient.connectTimeout = 30
            httpClient.requestMethod = params[0]

            if (params[0] == "POST") {
                httpClient.instanceFollowRedirects = false
                httpClient.doOutput = true
                httpClient.doInput = true
                httpClient.useCaches = false
                httpClient.setRequestProperty("Content-Type", "application/json; charset=utf-8")
            }
            try {
                if (params[0] == "POST") {
                    httpClient.connect()
                    val os = httpClient.outputStream
                    val writer = BufferedWriter(OutputStreamWriter(os, "UTF-8"))
                    writer.write(params[2])
                    writer.flush()
                    writer.close()
                    os.close()
                }
                if (httpClient.responseCode == HttpURLConnection.HTTP_OK) {
                    val stream = BufferedInputStream(httpClient.inputStream)
                    val data: String = readStream(inputStream = stream)
                    return data
                } else {
                    println("ERROR ${httpClient.responseCode}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                httpClient.disconnect()
            }

            return null

        }

        fun readStream(inputStream: BufferedInputStream): String {
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val stringBuilder = StringBuilder()
            bufferedReader.forEachLine { stringBuilder.append(it) }
            return stringBuilder.toString()
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            callback(result)
        }
    }

    fun callEvery10(
        testqueid: Int, queid: Int,
        quetypeid: Int,
        answerr: String
    ) {

        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.submitAnswer(
            WebRequests.submitAnswerParams(
                studenttestid,
                testqueid,
                queid,
                quetypeid,
                answerr,
                qtime.toString()
            )
        )

        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // Log error here since request failed
            }
        })
    }

    fun callSubmitAnswer(
        type: String,
        testqueid: Int,
        queid: Int,
        quetypeid: Int,
        answerr: String,
        p11: Int
    ) {

        var p1 = p11

        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.submitAnswer(
            WebRequests.submitAnswerParams(
                studenttestid,
                testqueid,
                queid,
                quetypeid,
                answerr,
                qtime.toString()
            )
        )
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.body()!!.get("Status").asString == "true") {

                    qtime = 0

                    if (type != "continue") {

                        DialogUtils.showDialog(this@NewTabQuestionActivity)

                        if (type == "activity" || type == "skip") {
                            if ((finalArr.size - 1) > q_grppos1) {

                                if (finalArr[sectionList!![q_grppos1]]!!.size == curr_index && p1 == (curr_index - 1)) {
                                    q_grppos1 += 1
                                    curr_index = 0
                                    p1 = 0
                                    finalArr[sectionList!![q_grppos1]]!![p1].type = 1
                                }

                                if (nextButton!!.text != "Submit Test") {
                                    if (movies[q_grppos1].TestQuestion[curr_index].Answer != "") {
                                        setNextSkipButtonText(1)
                                    } else {
                                        setNextSkipButtonText(0)
                                    }
                                }

                                sideList!!.setAdapter(
                                    NewSideMenuAdapter(
                                        context!!,
                                        sectionList!!,
                                        finalArr,
                                        filterTypeSelectionInteface!!,
                                        "question"
                                    )
                                )

//                    answer = ""
//                    queTab_tvFillBlanks.setText("")
                                ansArr = ArrayList()

//                    queTab_btnNext.visibility = View.VISIBLE

//                    if(movies[p0].TestQuestion[p1].Answer != ""){
//                        queTab_btnNext.text = "Next"
//                    }else{
//                        queTab_btnNext.text = "Skip"
//                    }

//                    queTab_tvTotal.text = """${p0 + 1}/${movies.size}"""

                                queTab_tvQMarks.text = "Marks : " + movies[q_grppos1].TestQuestion[curr_index].Marks

                                if ("http://content.testcraft.co.in/question/" + movies[q_grppos1].TestQuestion[curr_index].QuestionImage != "") {

                                    Picasso.get()
                                        .load("http://content.testcraft.co.in/question/" + movies[q_grppos1].TestQuestion[curr_index].QuestionImage)
                                        .transform(transform.getTransformation(imgQue!!))
                                        .into(imgQue)

                                    Log.d(
                                        "qsize",
                                        "width: " + page_img_que_img.width + ", height" + page_img_que_img.height
                                    )
//            PageViewFragment.qsize = page_img_que_img.width

                                    ansList!!.layoutManager =
                                        LinearLayoutManager(
                                            this@NewTabQuestionActivity,
                                            LinearLayoutManager.VERTICAL,
                                            false
                                        )

                                    if (movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID == 1 || movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID == 7) {

                                        queTab_tvFillBlanks.visibility = View.GONE
                                        ansList!!.visibility = View.VISIBLE
                                        queTab_rbTrue.visibility = View.GONE
                                        queTab_rbFalse.visibility = View.GONE

                                        ansList!!.adapter = SelectImageOptionAdapter(
                                            this@NewTabQuestionActivity,
                                            movies[q_grppos1].TestQuestion[curr_index].StudentTestQuestionMCQ,
                                            imgQue!!.width,
                                            movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID,
                                            movies[q_grppos1].TestQuestion[curr_index].QuestionID,
                                            movies[q_grppos1].TestQuestion[curr_index].Answer
                                        )

                                    } else if (movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID == 2 || movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID == 8) {

                                        if (movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID == 8) {
                                            queTab_tvFillBlanks.inputType = InputType.TYPE_CLASS_NUMBER
                                        }

                                        queTab_tvFillBlanks.visibility = View.VISIBLE
                                        ansList!!.visibility = View.GONE
                                        queTab_rbTrue.visibility = View.GONE
                                        queTab_rbFalse.visibility = View.GONE

//                            if (queTab_tvFillBlanks.text.toString() == "") {

                                        queTab_tvFillBlanks.setText(movies[q_grppos1].TestQuestion[curr_index].Answer)

//                            } else {
//                                answer = queTab_tvFillBlanks.text.toString()
//                            }

                                    } else if (movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID == 4) {
                                        queTab_rbTrue.visibility = View.VISIBLE
                                        queTab_rbFalse.visibility = View.VISIBLE
                                        queTab_tvFillBlanks.visibility = View.GONE
                                        ansList!!.visibility = View.GONE

                                        if (movies[q_grppos1].TestQuestion[curr_index].Answer == "1") {
                                            queTab_rbTrue.isChecked = true
                                            queTab_rbFalse.isChecked = false
                                        } else if (movies[q_grppos1].TestQuestion[curr_index].Answer == "0") {
                                            queTab_rbFalse.isChecked = true
                                            queTab_rbTrue.isChecked = false
                                        } else {
                                            queTab_rbTrue.isChecked = false
                                            queTab_rbFalse.isChecked = false
                                        }

                                    }
                                }
                            } else {
                                if ((finalArr[sectionList!![q_grppos1]]!!.size - 1) != curr_index - 1) {
                                    if (nextButton!!.text != "Submit Test") {
                                        if (movies[q_grppos1].TestQuestion[curr_index].Answer != "") {
                                            setNextSkipButtonText(1)
                                        } else {
                                            setNextSkipButtonText(0)
                                        }
                                    }

                                    sideList!!.setAdapter(
                                        NewSideMenuAdapter(
                                            context!!,
                                            sectionList!!,
                                            finalArr,
                                            filterTypeSelectionInteface!!,
                                            "question"
                                        )
                                    )

//                    answer = ""
//                    queTab_tvFillBlanks.setText("")
                                    ansArr = ArrayList()

//                    queTab_btnNext.visibility = View.VISIBLE

//                    if(movies[p0].TestQuestion[p1].Answer != ""){
//                        queTab_btnNext.text = "Next"
//                    }else{
//                        queTab_btnNext.text = "Skip"
//                    }

//                    queTab_tvTotal.text = """${p0 + 1}/${movies.size}"""

                                    queTab_tvQMarks.text = "Marks : " + movies[q_grppos1].TestQuestion[curr_index].Marks

                                    if ("http://content.testcraft.co.in/question/" + movies[q_grppos1].TestQuestion[curr_index].QuestionImage != "") {

                                        Picasso.get()
                                            .load("http://content.testcraft.co.in/question/" + movies[q_grppos1].TestQuestion[curr_index].QuestionImage)
                                            .transform(transform.getTransformation(imgQue!!))
                                            .into(imgQue)

                                        Log.d(
                                            "qsize",
                                            "width: " + page_img_que_img.width + ", height" + page_img_que_img.height
                                        )
//            PageViewFragment.qsize = page_img_que_img.width

                                        ansList!!.layoutManager =
                                            LinearLayoutManager(
                                                this@NewTabQuestionActivity,
                                                LinearLayoutManager.VERTICAL,
                                                false
                                            )

                                        if (movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID == 1 || movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID == 7) {

                                            queTab_tvFillBlanks.visibility = View.GONE
                                            ansList!!.visibility = View.VISIBLE
                                            queTab_rbTrue.visibility = View.GONE
                                            queTab_rbFalse.visibility = View.GONE

                                            ansList!!.adapter = SelectImageOptionAdapter(
                                                this@NewTabQuestionActivity,
                                                movies[q_grppos1].TestQuestion[curr_index].StudentTestQuestionMCQ,
                                                imgQue!!.width,
                                                movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID,
                                                movies[q_grppos1].TestQuestion[curr_index].QuestionID,
                                                movies[q_grppos1].TestQuestion[curr_index].Answer
                                            )

                                        } else if (movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID == 2 || movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID == 8) {

                                            if (movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID == 8) {
                                                queTab_tvFillBlanks.inputType = InputType.TYPE_CLASS_NUMBER
                                            }
                                            queTab_tvFillBlanks.visibility = View.VISIBLE
                                            ansList!!.visibility = View.GONE
                                            queTab_rbTrue.visibility = View.GONE
                                            queTab_rbFalse.visibility = View.GONE

//                            if (queTab_tvFillBlanks.text.toString() == "") {

                                            queTab_tvFillBlanks.setText(movies[q_grppos1].TestQuestion[curr_index].Answer)

//                            } else {
//                                answer = queTab_tvFillBlanks.text.toString()
//                            }

                                        } else if (movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID == 4) {
                                            queTab_rbTrue.visibility = View.VISIBLE
                                            queTab_rbFalse.visibility = View.VISIBLE
                                            queTab_tvFillBlanks.visibility = View.GONE
                                            ansList!!.visibility = View.GONE

                                            if (movies[q_grppos1].TestQuestion[curr_index].Answer == "1") {
                                                queTab_rbTrue.isChecked = true
                                                queTab_rbFalse.isChecked = false
                                            } else if (movies[q_grppos1].TestQuestion[curr_index].Answer == "0") {
                                                queTab_rbFalse.isChecked = true
                                                queTab_rbTrue.isChecked = false
                                            } else {
                                                queTab_rbTrue.isChecked = false
                                                queTab_rbFalse.isChecked = false
                                            }

                                        }
                                    }
                                }
                            }
                        } else {
//                        DialogUtils.createConfirmDialog(
//                            this@NewTabQuestionActivity,
//                            "Done?",
//                            "Are you sure you want to submit this test?",
//                            "OK",
//                            "Cancel",
//                            DialogInterface.OnClickListener { dialog, which ->
//
//                                callSubmitAPI()
//
//                            },
//                            DialogInterface.OnClickListener { dialog, which ->
//                                dialog.dismiss()
//
//                            }).show()
                        }
                    }

                    DialogUtils.dismissDialog()

                } else {

                    Toast.makeText(
                        this@NewTabQuestionActivity,
                        response.body()!!.get("Msg").asString,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // Log error here since request failed
                Log.e("", t.toString())
            }
        })
    }

    override fun getType(itype: String, p00: Int, p1: Int) {

        curr_index = p1

        drawer_layout.closeDrawer(Gravity.END)

//        answer = ""

//        queTab_rbTrue.isChecked = false
//        queTab_rbFalse.isChecked = false

        hintData =
            "<html><body style='background-color:clear;'><p align=center><font size=4><b>" + "Hint" + "</b></font></p><p><font size=2>" + movies[q_grppos1].TestQuestion[curr_index].Hint + "</font></p></body></html>"

        when (movies[p00].TestQuestion[curr_index].QuestionTypeID) {

            1 -> {

                if (itype == "activity" || itype == "submit" || itype == "continue") {

                    answer = if (ansArr.size > 0) {
                        ansArr[0].ansid
                    } else {
                        movies[p00].TestQuestion[curr_index].Answer
                    }
                }

//                answer = answer.substring(0, answer.length)

            }

            7 -> {

//                for (i in 0 until ansArr.size) {
//                    answer = answer + ansArr[i].ansid + "|"
//                }

                if (itype == "activity" || itype == "submit" || itype == "continue") {

                    answer = if (ansArr.size > 0) {
                        ansArr[0].ansid
                    } else {
                        movies[p00].TestQuestion[curr_index].Answer
                    }
                }

//                answer = answer.substring(0, answer.length)

            }

            2 -> {

                answer = queTab_tvFillBlanks.text.toString()

            }

            8 -> {

                answer = queTab_tvFillBlanks.text.toString()

            }

            4 -> {
                if (queTab_rbFalse.isChecked) {
                    answer = "0"
                } else if (queTab_rbTrue.isChecked) {
                    answer = "1"
                }

            }
        }

        if (itype == "activity") {

            Log.d("current_index_next", "" + curr_index)

            movies[q_grppos1].TestQuestion[curr_index].Answer = answer

            queTab_rbTrue.isChecked = false
            queTab_rbFalse.isChecked = false

            if (curr_index <= finalArr[sectionList!![q_grppos1]]!!.size - 1) {
                curr_index += 1
            }

            callSubmitAnswer(
                itype,
                movies[q_grppos1].TestQuestion[curr_index - 1].TestQuestionID,
                movies[q_grppos1].TestQuestion[curr_index - 1].QuestionID,
                movies[q_grppos1].TestQuestion[curr_index - 1].QuestionTypeID,
                answer,
                curr_index - 1
            )

//            curr_index = curr_index + 1

            if ((finalArr.size - 1) == q_grppos1) {
                if ((finalArr[sectionList!![q_grppos1]]!!.size - 1) == curr_index) {
                    setNextSkipButtonText(2)
                }
            }

        } else if (itype == "continue") {

//            val json = JSONObject()
//            json.put("StudentTestID", studenttestid)
//            json.put("TestQuestionID", movies[q_grppos1].TestQuestion[curr_index].TestQuestionID)
//            json.put("QuestionID", movies[q_grppos1].TestQuestion[curr_index].QuestionID)
//            json.put("QuestionTypeID", movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID)
//            json.put("Answer", answer)
//            json.put("UseTime", qtime)

//            callEvery10 {
//                if (it == null) {
//                    println("connection error")
//                }
//                println(it)
//            }.execute("POST", AppConstants.BASE_URL + "Insert_Test_Answer ", json.toString())

            callSubmitAnswer(
                itype,
                movies[q_grppos1].TestQuestion[curr_index].TestQuestionID,
                movies[q_grppos1].TestQuestion[curr_index].QuestionID,
                movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID,
                answer,
                curr_index
            )

        } else if (itype == "submit") {

            Log.d("current_index_next", "" + curr_index)

            movies[q_grppos1].TestQuestion[curr_index].Answer = answer

            queTab_rbTrue.isChecked = false
            queTab_rbFalse.isChecked = false

            if (curr_index <= finalArr[sectionList!![q_grppos1]]!!.size - 1) {
                curr_index += 1
            }

            callSubmitAnswer(
                itype,
                movies[q_grppos1].TestQuestion[curr_index - 1].TestQuestionID,
                movies[q_grppos1].TestQuestion[curr_index - 1].QuestionID,
                movies[q_grppos1].TestQuestion[curr_index - 1].QuestionTypeID,
                answer,
                curr_index + 1
            )

//            curr_index = curr_index + 1

//            if ((finalArr.size - 1) == q_grppos1) {
//                if ((finalArr[sectionList!![q_grppos1]]!!.size - 1) == curr_index) {
//                    setNextSkipButtonText(2)
//                }
//            }
        } else if (itype == "skip") {

            Log.d("current_index_next", "" + curr_index)

            queTab_rbTrue.isChecked = false
            queTab_rbFalse.isChecked = false

            if (curr_index <= finalArr[sectionList!![q_grppos1]]!!.size - 1) {
                curr_index += 1
            }

            callSubmitAnswer(
                itype,
                movies[q_grppos1].TestQuestion[curr_index - 1].TestQuestionID,
                movies[q_grppos1].TestQuestion[curr_index - 1].QuestionID,
                movies[q_grppos1].TestQuestion[curr_index - 1].QuestionTypeID,
                "",
                curr_index + 1
            )

        } else {

            queTab_tvCurrTotal.text = curr_index.toString()

            DialogUtils.showDialog(this@NewTabQuestionActivity)

//            if (itype == "skip") {
//
//                if (curr_index <= finalArr[sectionList!![q_grppos1]]!!.size - 1) {
//                    curr_index += 1
//                }
//
////                if ((finalArr.size - 1) == q_grppos1) {
////                    if ((finalArr[sectionList!![q_grppos1]]!!.size - 1) == (com.testprep.sectionmodule.NewTabQuestionActivity.Companion.curr_index+1)) {
////                        queTab_btnNext.text = "Submit Test"
////                    }
////                }
//
//                if ((finalArr.size - 1) > q_grppos1) {
//
//                    if (finalArr[sectionList!![q_grppos1]]!!.size == curr_index) {
//                        q_grppos1 += 1
//                        curr_index = 0
//                        finalArr[sectionList!![q_grppos1]]!![curr_index].type = 1
//                    }
//
//                    if (movies[q_grppos1].TestQuestion[curr_index].Answer != "") {
//                        setNextSkipButtonText(1)
//                    } else {
//                        setNextSkipButtonText(0)
//                    }
//
//                } else {
//                    if (finalArr[sectionList!![q_grppos1]]!!.size - 1 == curr_index) {
//                        setNextSkipButtonText(2)
//                    } else {
//                        if (movies[q_grppos1].TestQuestion[curr_index].Answer != "") {
//                            setNextSkipButtonText(1)
//                        } else {
//                            setNextSkipButtonText(0)
//                        }
//                    }
//                }
//
//            } else {

            if ((finalArr.size - 1) > q_grppos1) {

                que_number += curr_index
                queTab_tvCurrTotal.text = que_number.toString()

                if (movies[q_grppos1].TestQuestion[curr_index].Answer != "") {
                    setNextSkipButtonText(1)
                } else {
                    setNextSkipButtonText(0)
                }

            } else {

                que_number = curr_index

                if (finalArr[sectionList!![q_grppos1]]!!.size - 1 == curr_index) {
                    setNextSkipButtonText(2)
                } else {
                    if (movies[q_grppos1].TestQuestion[curr_index].Answer != "") {
                        setNextSkipButtonText(1)
                    } else {
                        setNextSkipButtonText(0)
                    }
                }
            }
//            }

            Log.d("current_index_skip", "" + curr_index)
//            queTab_btnNext.visibility = View.VISIBLE

//            queTab_tvTotal.text = """${0 + 1}/${movies.size}"""

            queTab_tvQMarks.text = "Marks : " + movies[q_grppos1].TestQuestion[curr_index].Marks

            if ("http://content.testcraft.co.in/question/" + movies[q_grppos1].TestQuestion[curr_index].QuestionImage != "") {

                Picasso.get()
                    .load("http://content.testcraft.co.in/question/" + movies[q_grppos1].TestQuestion[curr_index].QuestionImage)
                    .transform(transform.getTransformation(imgQue!!))
                    .into(imgQue)

//                Picasso.get()
//                    .load("http://content.testcraft.co.in/question/" + movies[q_grppos1].TestQuestion[curr_index].QuestionImage)
//                    .resize(936, imgQue!!.height)
//                    .into(imgQue)

                Log.d("qsize", "width: " + page_img_que_img.width + ", height" + page_img_que_img.height)

//            PageViewFragment.qsize = page_img_que_img.width

                ansList!!.layoutManager =
                    LinearLayoutManager(this@NewTabQuestionActivity, LinearLayoutManager.VERTICAL, false)

                if (movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID == 1 || movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID == 7) {

                    queTab_tvFillBlanks.visibility = View.GONE
                    ansList!!.visibility = View.VISIBLE
                    queTab_rbTrue.visibility = View.GONE
                    queTab_rbFalse.visibility = View.GONE

                    ansList!!.adapter = SelectImageOptionAdapter(
                        this@NewTabQuestionActivity,
                        movies[q_grppos1].TestQuestion[curr_index].StudentTestQuestionMCQ,
                        imgQue!!.width,
                        movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID,
                        movies[q_grppos1].TestQuestion[curr_index].QuestionID,
                        movies[q_grppos1].TestQuestion[curr_index].Answer
                    )

                } else if (movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID == 2 || movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID == 8) {

                    if (movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID == 8) {
                        queTab_tvFillBlanks.inputType = InputType.TYPE_CLASS_NUMBER
                    }
                    queTab_tvFillBlanks.visibility = View.VISIBLE
                    ansList!!.visibility = View.GONE
                    queTab_rbTrue.visibility = View.GONE
                    queTab_rbFalse.visibility = View.GONE

//                            if (queTab_tvFillBlanks.text.toString() == "") {

                    queTab_tvFillBlanks.setText(movies[q_grppos1].TestQuestion[curr_index].Answer)

//                            } else {
//                                answer = queTab_tvFillBlanks.text.toString()
//                            }

                } else if (movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID == 4) {
                    queTab_rbTrue.visibility = View.VISIBLE
                    queTab_rbFalse.visibility = View.VISIBLE
                    queTab_tvFillBlanks.visibility = View.GONE
                    ansList!!.visibility = View.GONE

                    if (movies[q_grppos1].TestQuestion[curr_index].Answer == "1") {
                        queTab_rbTrue.isChecked = true
                        queTab_rbFalse.isChecked = false
                    } else if (movies[q_grppos1].TestQuestion[curr_index].Answer == "0") {
                        queTab_rbFalse.isChecked = true
                        queTab_rbTrue.isChecked = false
                    } else {
                        queTab_rbTrue.isChecked = false
                        queTab_rbFalse.isChecked = false
                    }
                }
            }

            DialogUtils.dismissDialog()

            sideList!!.setAdapter(
                NewSideMenuAdapter(
                    context!!,
                    sectionList!!,
                    finalArr,
                    filterTypeSelectionInteface!!,
                    "question"
                )
            )
        }

//        if (itype == "activity" || itype == "skip")
//        {
//            if ((movies[q_grppos1].TestQuestion.size - 1) > curr_index) {
//                curr_index = curr_index + 1
//
//            }
//        }

        Log.d("que_number_type", "" + curr_index)
    }

    fun setNextSkipButtonText(type: Int) {

        when (type) {
            0 -> nextButton!!.text = "Skip"
            1 -> nextButton!!.text = "Next"
            2 -> nextButton!!.text = "Submit Test"
        }

    }

    fun nextButtonClick() {

        when {
            nextButton!!.text == "Next" -> {

                que_number += 1

                for (i in 0 until finalArr[sectionList!![q_grppos1]]!!.size) {
                    if (finalArr[sectionList!![q_grppos1]]!![i].qnumber == curr_index) {
                        finalArr[sectionList!![q_grppos1]]!![i].type = 2
                    }
                }

                getType("activity", q_grppos1, curr_index)

            }
            nextButton!!.text == "Skip" -> {

                que_number += 1

                for (i in 0 until finalArr[sectionList!![q_grppos1]]!!.size) {
                    if (finalArr[sectionList!![q_grppos1]]!![i].qnumber == curr_index) {
                        finalArr[sectionList!![q_grppos1]]!![i].type = 3
                    }
                }

                getType("skip", q_grppos1, curr_index)
            }
            nextButton!!.text == "Submit Test" -> {
                DialogUtils.createConfirmDialog(
                    this@NewTabQuestionActivity,
                    "Done?",
                    "Are you sure you want to end this test?",
                    "OK",
                    "Cancel",
                    DialogInterface.OnClickListener { dialog, which ->

                        getType("activity", q_grppos1, curr_index)
                        callSubmitAPI()

                    },
                    DialogInterface.OnClickListener { dialog, which ->
                        dialog.dismiss()

                    }).show()
            }
        }
    }

    companion object {

        var nextButton: Button? = null
        var sideList: ExpandableListView? = null
        var sectionList: ArrayList<String>? = null
        //        var sectionList1: ArrayList<QuestionTypeModel>? = null
        var filterTypeSelectionInteface: FilterTypeSelectionInteface? = null
        var movies: ArrayList<NewQuestionResponse.QuestionList> = ArrayList()
        var ansArr: ArrayList<AnswerModel> = ArrayList()
        var context: Context? = null
        var finalArr: HashMap<String, ArrayList<QuestionTypeModel>> = HashMap()
        var q_grppos1 = 0
        var old_grppos1 = 0
        var curr_index = 0

        fun setSideMenu(type: Int) {
            for (i in 0 until finalArr.size) {
                if (finalArr[sectionList!![q_grppos1]]!![i].qnumber == curr_index) {
                    finalArr[sectionList!![q_grppos1]]!![i].type = type
                }
            }

            sideList!!.setAdapter(
                NewSideMenuAdapter(
                    context!!,
                    sectionList!!,
                    finalArr,
                    filterTypeSelectionInteface!!,
                    "question"
                )
            )
        }

        fun setButton(position: Int, ansid: String, qid: Int) {

            if (nextButton!!.text.toString() != "Submit Test") {
                nextButton!!.text = "Next"
            }

//            val answerModel = AnswerModel()
//            answerModel.ansid = ansid
//            answerModel.qid = qid
//            answerModel.ansresult = result

            ansArr = ArrayList()

            if (movies[0].TestQuestion[position].QuestionTypeID == 1) {

                val answerModel = AnswerModel()
                answerModel.ansid = ansid
                answerModel.ansresult = true
                ansArr.add(answerModel)

            } else {

//                if (ansArr.size > 0) {
//                    for (i in 0 until ansArr.size) {
//                        if (ansArr[i].qid == qid) {
//                            ansArr[i].ansid = ansid
//                            ansArr[i].ansresult = true
//                        }
//                    }
//                } else {

                if (ansid != "") {
                    val answerModel = AnswerModel()
                    answerModel.ansid = ansid
                    answerModel.ansresult = true
                    ansArr.add(answerModel)
                }
            }
        }
    }
}