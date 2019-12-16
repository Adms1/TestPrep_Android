package com.testcraft.testcraft.sectionmodule

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.*
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
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.webkit.WebView
import android.widget.*
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import com.testcraft.testcraft.R
import com.testcraft.testcraft.activity.DashboardActivity
import com.testcraft.testcraft.activity.ResultActivity
import com.testcraft.testcraft.adapter.SelectImageOptionAdapter
import com.testcraft.testcraft.interfaces.FilterTypeSelectionInteface
import com.testcraft.testcraft.models.AnswerModel
import com.testcraft.testcraft.models.QuestionTypeModel
import com.testcraft.testcraft.retrofit.WebClient
import com.testcraft.testcraft.retrofit.WebInterface
import com.testcraft.testcraft.utils.*
import kotlinx.android.synthetic.main.activity_tabwise_question.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.TimeUnit

class QuestionActivity : FragmentActivity(), FilterTypeSelectionInteface {

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
    var total_hint = ""
    var total_hint_used = ""

    var integeranswer = ""

    private var mLastClickTime: Long = 0

    var hintCount = 0

    var shouldExecuteOnResume: Boolean? = null

    internal var mDrawerToggle: ActionBarDrawerToggle? = null

    var qtime = 0
    var continuetime = 0
    var que_number = 1

    var reportdialog: Dialog? = null

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

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
        total_hint = intent.getStringExtra("totalhint")
        total_hint_used = intent.getStringExtra("totalhintused")

        if (total_hint_used != "0" && total_hint_used != "") {
            hintCount = total_hint_used.toInt()
        }

        curr_index = 0
        q_grppos1 = 0

        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        ansArr = ArrayList()
        sectionList = ArrayList()
        finalArr = HashMap()

        queTab_tvFillBlanks.setText(integeranswer)

        filterTypeSelectionInteface = this

        context = this@QuestionActivity

        imgQue = findViewById(R.id.page_img_que_img)
        ansList = findViewById(R.id.wv_question_list)
        nextButton = findViewById(R.id.queTab_btnNext)
        sideList = findViewById(R.id.queTab_expQueList)

        imgQue!!.isDrawingCacheEnabled = true

        mDrawerToggle = ActionBarDrawerToggle(
            this, drawer_layout, R.mipmap.tc_logo, // nav menu toggle icon
            R.string.app_name, // nav drawer open - description for accessibility
            R.string.app_name
        )

        wv_question_list.isNestedScrollingEnabled = false

        drawer_layout.setDrawerListener(mDrawerToggle)

        var isshow = false
        queTab_expQueList.setOnGroupClickListener { parent, v, groupPosition, id ->

            val dialog = Dialog(this@QuestionActivity)

            if (!isshow && !dialog.isShowing) {

                dialog.setContentView(R.layout.hint_dialog)
                dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.setCanceledOnTouchOutside(false)

                val hintWebview: WebView = dialog.findViewById(R.id.dialog_hint_wvHint)
                val header: TextView = dialog.findViewById(R.id.dialog_hint_tvHeader)

                val closeBtn: View = dialog.findViewById(R.id.dialog_hint_btnClose)

                header.text = movies[groupPosition].SectionName

//            hintWebview.text = movies[groupPosition].SectionInstruction

                hintWebview.settings.javaScriptEnabled = true
                hintWebview.loadDataWithBaseURL(
                    "",
                    "<html><body style='background-color:clear;'><p>" + movies[groupPosition].SectionInstruction + "</p></body></html>",
                    "text/html",
                    "UTF-8",
                    ""
                )

                closeBtn.setOnClickListener { dialog.dismiss() }

                dialog.show()

                isshow = true

            } else {

                isshow = false
                dialog.dismiss()
            }

            false

        }

        queTab_tvFillBlanks.setOnEditorActionListener(object : TextView.OnEditorActionListener {

            override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {

                if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {

                    integeranswer = queTab_tvFillBlanks.text.toString()

                }
                return false
            }
        })

        queTab_tvFillBlanks.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {

                if (nextButton!!.text != "Submit Test") {

                    if (queTab_tvFillBlanks.text.toString() == "") {
                        setNextSkipButtonText(0)
                    } else {
                        setNextSkipButtonText(1)
                    }
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

//        queTab_ivPlayPause.setOnCheckedChangeListener { buttonView, isChecked ->
//
//            stopTimer()
//
//            val dialog = Dialog(this@QuestionActivity)
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

        clicks()

        ansList!!.layoutManager =
            LinearLayoutManager(this@QuestionActivity, LinearLayoutManager.VERTICAL, false)

        if (testid != "") {
            callQuestionApi()
        }
    }

    fun callQuestionApi() {

        DialogUtils.showDialog(this@QuestionActivity)

        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getNewQuestions(testid, studenttestid)
        call.enqueue(object : Callback<NewQuestionResponse> {
            override fun onResponse(
                call: Call<NewQuestionResponse>,
                response: Response<NewQuestionResponse>
            ) {

                if (response.body()!!.Status == "true") {

                    val time = testtime.toFloat()

                    queTab_tvCurrTotal.text = "$que_number/$testque"

                    if (total_hint != "0") {
                        queTab_tvCurrHint.text = "$hintCount/$total_hint"
                        queTab_ivHint.visibility = View.VISIBLE

                    } else {
                        queTab_tvCurrHint.text = ""
                        queTab_ivHint.visibility = View.GONE
                    }


                    if (time > 0) {
                        setCountdown(time.toLong() * 1000)
                    } else {
                        DialogUtils.createConfirmDialog1(
                            this@QuestionActivity,
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

                    if (movies.size > 0) {

//                        queTab_tvTotal.text = "1/${movies.size}"

                        queTab_tvQMarks.text = "Marks : " + movies[0].TestQuestion[0].Marks

                        if (total_hint != "0") {
                            queTab_tvCurrHint.text = "$hintCount/$total_hint"
                            queTab_ivHint.visibility = View.VISIBLE
                        } else {
                            queTab_tvCurrHint.text = ""
                            queTab_ivHint.visibility = View.GONE
                        }

                        Log.d("qid", "" + movies[0].SectionID)

                        for (i in 0 until movies.size) {

                            val ansmodel = AnswerModel()
//                        ansmodel.qid = movies[i].SectionID
                            ansmodel.ansid = movies[i].SectionName
                            sectionList!!.add(movies[i].SectionName)
                        }

                        if (movies[0].TestQuestion[0].QuestionImage != "") {

                            if (movies[0].TestQuestion[0].Answer != "") {

                                setNextSkipButtonText(1)

                            } else {

                                setNextSkipButtonText(0)
                            }

                            if (total_hint != "0" && movies[0].TestQuestion[0].Hint != "") {

                                queTab_ivHint.visibility = View.VISIBLE

                                if (movies[0].TestQuestion[0].HintUsed == "0") {

                                    queTab_ivHint.setImageResource(R.drawable.hint_bulb)

                                } else {

                                    queTab_ivHint.setImageResource(R.drawable.hint_bulb_yellow)

                                }

                                hintData =
                                    "<html><body style='background-color:clear;'><p>" + movies[0].TestQuestion[0].Hint + "</p></body></html>"

                            } else {
                                queTab_ivHint.visibility = View.GONE
                            }

                            if (movies[0].TestQuestion[0].Review.equals("0", true)) {

                                queTab_ivReview.isChecked = false
                                queTab_ivReview.setBackgroundResource(R.drawable.rotate_eye_gray)

                            } else {

                                queTab_ivReview.isChecked = true
                                queTab_ivReview.setBackgroundResource(R.drawable.rotate_eye_blue)
                            }

                            var pagenumber = 0

                            for (i in 0 until movies.size) {
//
                                val sectionList1: ArrayList<QuestionTypeModel> = ArrayList()

                                for (j in 0 until movies[i].TestQuestion.size) {
                                    val questionTypeModel = QuestionTypeModel()
                                    questionTypeModel.qnumber = j

                                    pagenumber++

                                    questionTypeModel.page_number = pagenumber

                                    if (movies[i].TestQuestion[j].Review.equals("0", true)) {

                                        Log.d("isreview1", "" + 0)

                                        if (movies[i].TestQuestion[j].Answer != "") {

                                            questionTypeModel.type = 2

                                        } else {

                                            questionTypeModel.type = 5
                                        }
                                    } else {

                                        Log.d("isreview2", "" + 1)

                                        questionTypeModel.type = 4

                                    }

                                    sectionList1.add(questionTypeModel)

                                }
                                finalArr[movies[i].SectionName] = sectionList1
//
                            }

                            queTab_expQueList.setAdapter(
                                NewSideMenuAdapter(
                                    this@QuestionActivity,
                                    sectionList!!,
                                    finalArr,
                                    filterTypeSelectionInteface!!,
                                    "question"
                                )
                            )

                            Log.d("header", "" + sectionList)
                            Log.d("child", "" + childList)

                            Picasso.get()
                                .load(movies[0].TestQuestion[0].QuestionImage)
                                .transform(transform.getTransformation(imgQue!!))
                                .into(imgQue)

                            Log.d(
                                "qsize",
                                "width: " + page_img_que_img.width + ", height" + page_img_que_img.height
                            )

                            ansList!!.layoutManager =
                                LinearLayoutManager(
                                    this@QuestionActivity,
                                    LinearLayoutManager.VERTICAL,
                                    false
                                )

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
                                    this@QuestionActivity,
                                    movies[0].TestQuestion[0].StudentTestQuestionMCQ,
                                    page_img_que_img.width,
                                    movies[0].TestQuestion[0].QuestionTypeID,
                                    movies[0].TestQuestion[0].QuestionID,
                                    movies[0].TestQuestion[0].Answer
                                )

                            } else if (movies[0].TestQuestion[0].QuestionTypeID == 2 || movies[0].TestQuestion[0].QuestionTypeID == 8) {

                                if (movies[0].TestQuestion[0].QuestionTypeID == 8) {

                                    queTab_tvFillBlanks.inputType =
                                        InputType.TYPE_NUMBER_FLAG_SIGNED + InputType.TYPE_CLASS_NUMBER + InputType.TYPE_NUMBER_FLAG_DECIMAL
                                }

                                queTab_tvFillBlanks.visibility = View.VISIBLE
                                ansList!!.visibility = View.GONE
                                queTab_rbTrue.visibility = View.GONE
                                queTab_rbFalse.visibility = View.GONE

                                if (movies[0].TestQuestion[0].Answer != "") {

                                    integeranswer = movies[0].TestQuestion[0].Answer

                                    queTab_tvFillBlanks.setText(integeranswer)
                                } else {
                                    queTab_tvFillBlanks.setText("")
                                }

                            } else if (movies[0].TestQuestion[0].QuestionTypeID == 4) {
                                queTab_rbTrue.visibility = View.VISIBLE
                                queTab_rbFalse.visibility = View.VISIBLE
                                queTab_tvFillBlanks.visibility = View.GONE
                                ansList!!.visibility = View.GONE

                                when {
                                    movies[0].TestQuestion[0].Answer == "1" -> {
                                        queTab_rbTrue.isChecked = true
                                        queTab_rbFalse.isChecked = false

                                    }
                                    movies[0].TestQuestion[0].Answer == "0" -> {
                                        queTab_rbFalse.isChecked = true
                                        queTab_rbTrue.isChecked = false
                                    }
                                    else -> {
                                        queTab_rbTrue.isChecked = false
                                        queTab_rbFalse.isChecked = false
                                    }
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
                    }, 1000
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
        setCountdown(milliLeft)
    }

    private fun setCountdown(minute: Long) {

        waitTimer = object : CountDownTimer(minute, 1000) {

            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                milliLeft = millisUntilFinished
                queTab_tvTimer.text = "" + String.format(
                    "%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                        TimeUnit.MILLISECONDS.toHours(
                            millisUntilFinished
                        )
                    ),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(
                            millisUntilFinished
                        )
                    )
                )

                if (qtime > 0 && qtime == 10) {

                    Log.d("10 second", "" + qtime)

                    if (movies[q_grppos1].TestQuestion[curr_index].Review.equals("1", true)) {

                        Log.d("itype", "continue true")

                        getType("continue", 1, curr_index)
                        queTab_ivReview.isChecked = true
                        queTab_ivReview.setBackgroundResource(R.drawable.rotate_eye_blue)
                    } else {

                        Log.d("itype", "continue false")

                        getType("continue", 0, curr_index)
                        queTab_ivReview.isChecked = false
                        queTab_ivReview.setBackgroundResource(R.drawable.rotate_eye_gray)
                    }

//                    callEvery10(
//                        movies[q_grppos1].TestQuestion[curr_index].TestQuestionID,
//                        movies[q_grppos1].TestQuestion[curr_index].QuestionID,
//                        movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID,
//                        answer
//                    )

                }

                if (continuetime == 60) {

                    if (reportdialog != null && reportdialog!!.isShowing) {
                        reportdialog!!.dismiss()
                    }

                    DialogUtils.createConfirmDialog(this@QuestionActivity, "Alert",
                        "Do you require more time in this question?",
                        "No", "Yes",

                        DialogInterface.OnClickListener { dialog, which ->

                            continuetime = 0

                            nextButtonClick()

                        },
                        DialogInterface.OnClickListener { dialog, which ->

                            continuetime = 0

                            dialog.dismiss()

                        }).show()

                } else if (continuetime == 120) {

                    if (reportdialog != null && reportdialog!!.isShowing) {
                        reportdialog!!.dismiss()
                    }

                    continuetime = 0

                    AppConstants.isFirst = 12
                    val intent = Intent(this@QuestionActivity, DashboardActivity::class.java)
                    startActivity(intent)
                    finish()

                }

                qtime += 1
                continuetime += 1

                Log.d("question_time", "" + qtime)
                Log.d("continue time", "" + continuetime)
            }

            override fun onFinish() {

                if (reportdialog != null && reportdialog!!.isShowing) {
                    reportdialog!!.dismiss()
                }

                queTab_tvTimer.text = getString(R.string._00_00)

//                DialogUtils.createConfirmDialog(
//                    this@QuestionActivity,
//                    "Done?",
//                    "Are you sure you want to submit this test?",
//                    "OK",
//                    "Cancel",
//                    DialogInterface.OnClickListener { dialog, which ->
//
//                        var ansstr = ""
//
//                        for (i in 0 until ansArr.size) {
//                            ansstr = ansstr + ansArr[i].qid + "|" + ansArr[i].ansid + ","
//
//                        }
//
//                        Log.d("ansstr", ansstr)
//
//                        callSubmitAPI()
//
//                    },
//                    DialogInterface.OnClickListener { dialog, which ->
//                        if (queTab_tvTimer.text.toString().equals("00:00:00", true)) {

                getType("continue", 0, curr_index)

                DialogUtils.createConfirmDialog1(
                    this@QuestionActivity,
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

//                        } else {
//                            dialog.dismiss()
//                        }
//
//
//                    }).show()

            }
        }.start()
    }

    override fun onBackPressed() {

        DialogUtils.createConfirmDialog(
            this@QuestionActivity,
            "Resume?",
            "Are you sure you want to resume a test?",
            "OK",
            "Cancel",
            DialogInterface.OnClickListener { dialog, which ->

                continuetime = 0

                AppConstants.isFirst = 12
                val intent = Intent(this@QuestionActivity, DashboardActivity::class.java)
                startActivity(intent)
                finish()

            },
            DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()

            }).show()

    }

    fun callSubmitAPI() {

        continuetime = 0
        stopTimer()

        DialogUtils.showDialog(this@QuestionActivity)

        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.submitTest(studenttestid)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                DialogUtils.dismissDialog()

                if (response.body()!!.get("Status").asString == "true") {

                    val intent = Intent(this@QuestionActivity, ResultActivity::class.java)
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
                        this@QuestionActivity,
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
                    return readStream(inputStream = stream)
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
                qtime.toString(), "0"
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
        p11: Int, reviewid: String
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
                qtime.toString(), reviewid
            )
        )

        qtime = 0

        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.body()!!.get("Status").asString == "true") {

                    ansArr = ArrayList()

                    if (type != "continue" && type != "review") {

                        if (type == "activity" || type == "skip") {

//                            DialogUtils.showDialog(this@QuestionActivity)

                            queTab_tvCurrTotal.text = "$que_number/$testque"

                            if ((finalArr.size - 1) > q_grppos1) {

                                if (finalArr[sectionList!![q_grppos1]]!!.size == curr_index && p1 == (curr_index - 1)) {

                                    continuetime = 0

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

                                queTab_tvQMarks.text =
                                    "Marks : " + movies[q_grppos1].TestQuestion[curr_index].Marks

                                if (movies[q_grppos1].TestQuestion[curr_index].QuestionImage != "") {

                                    Picasso.get()
                                        .load(movies[q_grppos1].TestQuestion[curr_index].QuestionImage)
                                        .transform(transform.getTransformation(imgQue!!))
                                        .into(imgQue)

                                    Log.d(
                                        "qsize",
                                        "width: " + page_img_que_img.width + ", height" + page_img_que_img.height
                                    )

                                    ansList!!.layoutManager =
                                        LinearLayoutManager(
                                            this@QuestionActivity,
                                            LinearLayoutManager.VERTICAL,
                                            false
                                        )

                                    if (movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID == 1 || movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID == 7) {

                                        queTab_tvFillBlanks.visibility = View.GONE
                                        ansList!!.visibility = View.VISIBLE
                                        queTab_rbTrue.visibility = View.GONE
                                        queTab_rbFalse.visibility = View.GONE

                                        ansList!!.adapter = SelectImageOptionAdapter(
                                            this@QuestionActivity,
                                            movies[q_grppos1].TestQuestion[curr_index].StudentTestQuestionMCQ,
                                            imgQue!!.width,
                                            movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID,
                                            movies[q_grppos1].TestQuestion[curr_index].QuestionID,
                                            movies[q_grppos1].TestQuestion[curr_index].Answer
                                        )

                                    } else if (movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID == 2 || movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID == 8) {

                                        if (movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID == 8) {
                                            queTab_tvFillBlanks.inputType =
                                                InputType.TYPE_CLASS_NUMBER + InputType.TYPE_NUMBER_FLAG_DECIMAL + InputType.TYPE_NUMBER_FLAG_SIGNED

                                        }

                                        queTab_tvFillBlanks.visibility = View.VISIBLE
                                        ansList!!.visibility = View.GONE
                                        queTab_rbTrue.visibility = View.GONE
                                        queTab_rbFalse.visibility = View.GONE

                                        if (movies[q_grppos1].TestQuestion[curr_index].Answer != "") {

                                            integeranswer =
                                                movies[q_grppos1].TestQuestion[curr_index].Answer

                                            queTab_tvFillBlanks.setText(integeranswer)
                                        } else {
                                            queTab_tvFillBlanks.setText("")
                                        }

                                    } else if (movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID == 4) {
                                        queTab_rbTrue.visibility = View.VISIBLE
                                        queTab_rbFalse.visibility = View.VISIBLE
                                        queTab_tvFillBlanks.visibility = View.GONE
                                        ansList!!.visibility = View.GONE

                                        when {
                                            movies[q_grppos1].TestQuestion[curr_index].Answer == "1" -> {
                                                queTab_rbTrue.isChecked = true
                                                queTab_rbFalse.isChecked = false
                                            }
                                            movies[q_grppos1].TestQuestion[curr_index].Answer == "0" -> {
                                                queTab_rbFalse.isChecked = true
                                                queTab_rbTrue.isChecked = false
                                            }
                                            else -> {
                                                queTab_rbTrue.isChecked = false
                                                queTab_rbFalse.isChecked = false
                                            }
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

                                    ansArr = ArrayList()

                                    queTab_tvQMarks.text =
                                        "Marks : " + movies[q_grppos1].TestQuestion[curr_index].Marks

                                    if (movies[q_grppos1].TestQuestion[curr_index].QuestionImage != "") {

                                        Picasso.get()
                                            .load(movies[q_grppos1].TestQuestion[curr_index].QuestionImage)
                                            .transform(transform.getTransformation(imgQue!!))
                                            .into(imgQue)

                                        Log.d(
                                            "qsize",
                                            "width: " + page_img_que_img.width + ", height" + page_img_que_img.height
                                        )

                                        ansList!!.layoutManager =
                                            LinearLayoutManager(
                                                this@QuestionActivity,
                                                LinearLayoutManager.VERTICAL,
                                                false
                                            )

                                        if (movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID == 1 || movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID == 7) {

                                            queTab_tvFillBlanks.visibility = View.GONE
                                            ansList!!.visibility = View.VISIBLE
                                            queTab_rbTrue.visibility = View.GONE
                                            queTab_rbFalse.visibility = View.GONE

                                            ansList!!.adapter = SelectImageOptionAdapter(
                                                this@QuestionActivity,
                                                movies[q_grppos1].TestQuestion[curr_index].StudentTestQuestionMCQ,
                                                imgQue!!.width,
                                                movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID,
                                                movies[q_grppos1].TestQuestion[curr_index].QuestionID,
                                                movies[q_grppos1].TestQuestion[curr_index].Answer
                                            )

                                        } else if (movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID == 2 || movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID == 8) {

                                            if (movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID == 8) {
                                                queTab_tvFillBlanks.inputType =
                                                    InputType.TYPE_CLASS_NUMBER + InputType.TYPE_NUMBER_FLAG_DECIMAL + InputType.TYPE_NUMBER_FLAG_SIGNED

                                            }
                                            queTab_tvFillBlanks.visibility = View.VISIBLE
                                            ansList!!.visibility = View.GONE
                                            queTab_rbTrue.visibility = View.GONE
                                            queTab_rbFalse.visibility = View.GONE

                                            if (movies[q_grppos1].TestQuestion[curr_index].Answer != "") {

                                                integeranswer =
                                                    movies[q_grppos1].TestQuestion[curr_index].Answer

                                                queTab_tvFillBlanks.setText(integeranswer)

                                            } else {
                                                queTab_tvFillBlanks.setText("")
                                            }

                                        } else if (movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID == 4) {
                                            queTab_rbTrue.visibility = View.VISIBLE
                                            queTab_rbFalse.visibility = View.VISIBLE
                                            queTab_tvFillBlanks.visibility = View.GONE
                                            ansList!!.visibility = View.GONE

                                            when {
                                                movies[q_grppos1].TestQuestion[curr_index].Answer == "1" -> {
                                                    queTab_rbTrue.isChecked = true
                                                    queTab_rbFalse.isChecked = false
                                                }
                                                movies[q_grppos1].TestQuestion[curr_index].Answer == "0" -> {
                                                    queTab_rbFalse.isChecked = true
                                                    queTab_rbTrue.isChecked = false
                                                }
                                                else -> {
                                                    queTab_rbTrue.isChecked = false
                                                    queTab_rbFalse.isChecked = false
                                                }
                                            }

                                        }
                                    }
                                }
                            }

                        }
//                        else {
//                        DialogUtils.createConfirmDialog(
//                            this@QuestionActivity,
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
//                        }
                    } else {

                        if (reviewid != "0") {


                            queTab_ivReview.setBackgroundResource(R.drawable.rotate_eye_blue)
                            queTab_ivReview.isChecked = true

                        } else {

                            queTab_ivReview.setBackgroundResource(R.drawable.rotate_eye_gray)
                            queTab_ivReview.isChecked = false

                        }

//                        if (nextButton!!.text != "Submit Test") {
//                            if (answerr != "") {
//
//                                nextButton!!.setText("Next")
//
//                            } else {
//
//                                nextButton!!.setText("Skip")
//
//                            }
//                        }

                    }

                } else {

                    Toast.makeText(
                        this@QuestionActivity,
                        response.body()!!.get("Msg").asString,
                        Toast.LENGTH_LONG
                    ).show()
                }


//                Handler().postDelayed(
//                    /* Runnable
//                         * Showing splash screen with a timer. This will be useful when you
//                         * want to show case your app logo / company
//                         */
//
//                    {
//                        DialogUtils.dismissDialog()
//                    }, 10000
//                )

            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // Log error here since request failed
                Log.e("", t.toString())
            }
        })
    }

    override fun getType(itype: String, p0: Int, p1: Int) {

//        curr_index = p1

//        if (itype != "adapter") {

        when (movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID) {

            1 -> {

                if (itype == "activity" || itype == "adapter" || itype == "submit" || itype == "continue" || itype == "review") {

                    answer = if (ansArr.size > 0) {
                        ansArr[0].ansid
                    } else {
                        movies[q_grppos1].TestQuestion[curr_index].Answer
                    }
                }
            }

            7 -> {

                if (itype == "activity" || itype == "adapter" || itype == "submit" || itype == "continue" || itype == "review") {

                    answer = if (ansArr.size > 0) {
                        ansArr[0].ansid
                    } else {
                        movies[q_grppos1].TestQuestion[curr_index].Answer
                    }
                }
            }

            2 -> {

                answer = queTab_tvFillBlanks.text.toString()

            }

            8 -> {

                if (queTab_tvFillBlanks.text.toString() != "") {

//                        try {
//                            val int_answer = Integer.parseInt(queTab_tvFillBlanks.text.toString())
//
//                            if (int_answer <= 999999) {
//                                answer = int_answer.toString()
//                            }
//                        }catch (e: NumberFormatException ){
//                            System.out.println("not a number")
//                        }
//                    }else{
                    answer = integeranswer
//                        answer = queTab_tvFillBlanks.text.toString()
                }
            }

            4 -> {

                if (itype == "activity" || itype == "adapter" || itype == "submit" || itype == "continue" || itype == "review") {

                    if (queTab_rbFalse.isChecked) {
                        answer = "0"
                    } else if (queTab_rbTrue.isChecked) {
                        answer = "1"
                    } else {
                        answer = ""
                    }
                }
            }
        }
//        } else {

        if (itype == "adapter") {
//            DialogUtils.showDialog(this@QuestionActivity)

            movies[p0].TestQuestion[p1].Answer = answer

            callSubmitAnswer(
                itype,
                movies[p0].TestQuestion[p1].TestQuestionID,
                movies[p0].TestQuestion[p1].QuestionID,
                movies[p0].TestQuestion[p1].QuestionTypeID,
                answer,
                p1, p0.toString()
            )

            if (total_hint != "0" && movies[q_grppos1].TestQuestion[curr_index].Hint != "") {

                queTab_ivHint.visibility = View.VISIBLE

                if (movies[q_grppos1].TestQuestion[curr_index].HintUsed == "0") {

                    queTab_ivHint.setImageResource(R.drawable.hint_bulb)

                } else {

                    queTab_ivHint.setImageResource(R.drawable.hint_bulb_yellow)

                }

                hintData =
                    "<html><body style='background-color:clear;'><p>" + movies[q_grppos1].TestQuestion[curr_index].Hint + "</p></body></html>"
            } else {
                queTab_ivHint.visibility = View.GONE
            }

            drawer_layout.closeDrawer(Gravity.RIGHT)

            continuetime = 0

            ansArr = ArrayList()

            que_number = ImageViewAdapter.getPageNumber()

            queTab_tvCurrTotal.text = que_number.toString() + "/" + testque

            if ((finalArr.size - 1) > q_grppos1) {

                if (movies[q_grppos1].TestQuestion[curr_index].Answer != "") {
                    setNextSkipButtonText(1)
                } else {
                    setNextSkipButtonText(0)
                }

            } else {

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

            Log.d("current_index_skip", "" + curr_index)

            queTab_tvQMarks.text = "Marks : " + movies[q_grppos1].TestQuestion[curr_index].Marks

            if (movies[q_grppos1].TestQuestion[curr_index].QuestionImage != "") {

                Picasso.get()
                    .load(movies[q_grppos1].TestQuestion[curr_index].QuestionImage)
                    .transform(transform.getTransformation(imgQue!!))
                    .into(imgQue)

                Log.d(
                    "qsize",
                    "width: " + page_img_que_img.width + ", height" + page_img_que_img.height
                )

                ansList!!.layoutManager = LinearLayoutManager(
                    this@QuestionActivity,
                    LinearLayoutManager.VERTICAL,
                    false
                )

                if (movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID == 1 || movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID == 7) {

                    queTab_tvFillBlanks.visibility = View.GONE
                    ansList!!.visibility = View.VISIBLE
                    queTab_rbTrue.visibility = View.GONE
                    queTab_rbFalse.visibility = View.GONE

                    ansList!!.adapter = SelectImageOptionAdapter(
                        this@QuestionActivity,
                        movies[q_grppos1].TestQuestion[curr_index].StudentTestQuestionMCQ,
                        imgQue!!.width,
                        movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID,
                        movies[q_grppos1].TestQuestion[curr_index].QuestionID,
                        movies[q_grppos1].TestQuestion[curr_index].Answer
                    )

                } else if (movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID == 2 || movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID == 8) {

                    if (movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID == 8) {
                        queTab_tvFillBlanks.inputType =
                            InputType.TYPE_CLASS_NUMBER + InputType.TYPE_NUMBER_FLAG_DECIMAL + InputType.TYPE_NUMBER_FLAG_SIGNED

                    }
                    queTab_tvFillBlanks.visibility = View.VISIBLE
                    ansList!!.visibility = View.GONE
                    queTab_rbTrue.visibility = View.GONE
                    queTab_rbFalse.visibility = View.GONE

                    if (movies[q_grppos1].TestQuestion[curr_index].Answer != "") {

                        integeranswer = movies[q_grppos1].TestQuestion[curr_index].Answer

                        queTab_tvFillBlanks.setText(integeranswer)
                    } else {
                        queTab_tvFillBlanks.setText("")
                    }

                } else if (movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID == 4) {
                    queTab_rbTrue.visibility = View.VISIBLE
                    queTab_rbFalse.visibility = View.VISIBLE
                    queTab_tvFillBlanks.visibility = View.GONE
                    ansList!!.visibility = View.GONE

                    when {
                        movies[q_grppos1].TestQuestion[curr_index].Answer == "1" -> {
                            queTab_rbTrue.isChecked = true
                            queTab_rbFalse.isChecked = false
                        }
                        movies[q_grppos1].TestQuestion[curr_index].Answer == "0" -> {
                            queTab_rbFalse.isChecked = true
                            queTab_rbTrue.isChecked = false
                        }
                        else -> {
                            queTab_rbTrue.isChecked = false
                            queTab_rbFalse.isChecked = false
                        }
                    }
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

//            Handler().postDelayed(
//                /* Runnable
//                     * Showing splash screen with a timer. This will be useful when you
//                     * want to show case your app logo / company
//                     */
//
//                {
//                    DialogUtils.dismissDialog()
//                }, 5000
//            )

        } else if (itype == "activity") {

            Log.d("current_index_next", "" + curr_index)

            movies[q_grppos1].TestQuestion[curr_index].Answer = answer

            if (curr_index <= finalArr[sectionList!![q_grppos1]]!!.size - 1) {
                curr_index += 1
            }

//            DialogUtils.showDialog(this@QuestionActivity)

            callSubmitAnswer(
                itype,
                movies[q_grppos1].TestQuestion[curr_index - 1].TestQuestionID,
                movies[q_grppos1].TestQuestion[curr_index - 1].QuestionID,
                movies[q_grppos1].TestQuestion[curr_index - 1].QuestionTypeID,
                answer,
                curr_index - 1, p0.toString()
            )

            if ((finalArr.size - 1) == q_grppos1) {
                if ((finalArr[sectionList!![q_grppos1]]!!.size - 1) == curr_index) {
                    setNextSkipButtonText(2)
                }
            }

//            Handler().postDelayed(
//                /* Runnable
//                     * Showing splash screen with a timer. This will be useful when you
//                     * want to show case your app logo / company
//                     */
//
//                {
//                    DialogUtils.dismissDialog()
//                }, 2500
//            )


        } else if (itype == "continue" || itype == "review") {

            queTab_tvCurrTotal.text = que_number.toString() + "/" + testque

            Log.d("current_index_next", "" + curr_index)

            movies[q_grppos1].TestQuestion[curr_index].Answer = answer

            callSubmitAnswer(
                itype,
                movies[q_grppos1].TestQuestion[curr_index].TestQuestionID,
                movies[q_grppos1].TestQuestion[curr_index].QuestionID,
                movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID,
                answer,
                curr_index, p0.toString()
            )

            if ((finalArr.size - 1) == q_grppos1) {
                if ((finalArr[sectionList!![q_grppos1]]!!.size - 1) == curr_index) {
                    setNextSkipButtonText(2)
                }
            }

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
                curr_index + 1, p0.toString()
            )

        } else if (itype == "skip") {

            Log.d("current_index_next", "" + curr_index)

            if (curr_index <= finalArr[sectionList!![q_grppos1]]!!.size - 1) {
                curr_index += 1
            }

//            DialogUtils.showDialog(this@QuestionActivity)

            callSubmitAnswer(
                itype,
                movies[q_grppos1].TestQuestion[curr_index - 1].TestQuestionID,
                movies[q_grppos1].TestQuestion[curr_index - 1].QuestionID,
                movies[q_grppos1].TestQuestion[curr_index - 1].QuestionTypeID,
                "",
                curr_index - 1, p0.toString()
            )

//            Handler().postDelayed(
//                /* Runnable
//                     * Showing splash screen with a timer. This will be useful when you
//                     * want to show case your app logo / company
//                     */
//
//                {
//                    DialogUtils.dismissDialog()
//                }, 2500
//            )

            if ((finalArr.size - 1) == q_grppos1) {
                if ((finalArr[sectionList!![q_grppos1]]!!.size - 1) == curr_index) {
                    setNextSkipButtonText(2)
                }
            }

        }

        Log.d("que_number_type", "" + curr_index)
    }

    fun setNextSkipButtonText(type: Int) {

        if (total_hint != "0" && movies[q_grppos1].TestQuestion[curr_index].Hint != "") {

            queTab_ivHint.visibility = View.VISIBLE

            if (movies[q_grppos1].TestQuestion[curr_index].HintUsed == "0") {

                queTab_ivHint.setImageResource(R.drawable.hint_bulb)

            } else {

                queTab_ivHint.setImageResource(R.drawable.hint_bulb_yellow)

            }

            hintData =
                "<html><body style='background-color:clear;'><p>" + movies[q_grppos1].TestQuestion[curr_index].Hint + "</p></body></html>"

        } else {
            queTab_ivHint.visibility = View.GONE
        }


        if (movies[q_grppos1].TestQuestion[curr_index].Review.equals("0", true)) {

            queTab_ivReview.setBackgroundResource(R.drawable.rotate_eye_gray)
            queTab_ivReview.isChecked = false

        } else {

            queTab_ivReview.setBackgroundResource(R.drawable.rotate_eye_blue)
            queTab_ivReview.isChecked = true

        }

        when (type) {
            0 -> nextButton!!.text = "Skip"
            1 -> nextButton!!.text = "Next"
            2 -> nextButton!!.text = "Submit Test"
        }
    }

    fun clicks() {

        queTab_ivReview.setOnCheckedChangeListener { buttonView, isChecked ->

            if (queTab_ivReview.isPressed) {
                if (finalArr.size > 0 && movies.size > 0) {
                    if (isChecked) {

                        Log.d("isreview3", "" + 1)

                        queTab_ivReview.setBackgroundResource(R.drawable.rotate_eye_blue)

//                    if (finalArr.size > 0) {
                        for (i in 0 until finalArr[sectionList!![q_grppos1]]!!.size) {
                            if (finalArr[sectionList!![q_grppos1]]!![i].qnumber == curr_index) {

                                finalArr[sectionList!![q_grppos1]]!![i].type = 4
                                movies[q_grppos1].TestQuestion[curr_index].Review = "1"

                                break
                            }
                        }
//                    }

                        Log.d("itype", "review true")

                        getType("review", 1, curr_index)

                    } else {

                        Log.d("isreview4", "" + 0)

                        queTab_ivReview.setBackgroundResource(R.drawable.rotate_eye_gray)

                        for (i in 0 until finalArr[sectionList!![q_grppos1]]!!.size) {
                            if (finalArr[sectionList!![q_grppos1]]!![i].qnumber == curr_index) {
                                if (movies[q_grppos1].TestQuestion[curr_index].Answer != "") {
                                    finalArr[sectionList!![q_grppos1]]!![curr_index].type = 2
                                } else {
                                    finalArr[sectionList!![q_grppos1]]!![curr_index].type = 3
                                }

                                movies[q_grppos1].TestQuestion[curr_index].Review = "0"
                            }
                        }

                        Log.d("itype", "review false")

                        getType("review", 0, curr_index)
                    }
                }
            }
        }

        nextButton!!.setOnClickListener {

            nextButtonClick()
        }

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

        ansList!!.setOnClickListener {
            if (nextButton!!.text != "Submit Test") {
                setNextSkipButtonText(1)
            }
        }

        queTab_expQueList.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->

            false
        }

        queTab_ivHint.setOnClickListener {

            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return@setOnClickListener
            }
            mLastClickTime = SystemClock.elapsedRealtime()

            if (movies[q_grppos1].TestQuestion[curr_index].HintUsed == "0") {

                if (hintCount < total_hint.toInt()) {
                    hintCount += 1
                    callInsertHint()
                } else {
                    Toast.makeText(
                        this@QuestionActivity,
                        "Sorry! Your hint limit is over",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } else {

                val dialog = Dialog(this@QuestionActivity)
                dialog.setContentView(R.layout.hint_dialog)
                dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.setCanceledOnTouchOutside(false)

                val hintWebview: WebView = dialog.findViewById(R.id.dialog_hint_wvHint)
                val header: TextView = dialog.findViewById(R.id.dialog_hint_tvHeader)
                val closeBtn: View = dialog.findViewById(R.id.dialog_hint_btnClose)

                header.text = "Hint"

                hintWebview.settings.javaScriptEnabled = true
                hintWebview.loadDataWithBaseURL(
                    AppConstants.EXPHINT_IMAGE_BASE_URL,
                    hintData,
                    "text/html",
                    "UTF-8",
                    ""
                )

                closeBtn.setOnClickListener { dialog.dismiss() }

                dialog.show()

            }
        }

        queTab_btnNextt.setOnClickListener {
            drawer_layout.openDrawer(Gravity.END)
        }

        queTab_ivReporttxt.setOnClickListener {
            reportdialog = Dialog(this@QuestionActivity)
            reportdialog!!.setContentView(R.layout.dialog_report_issue)
            reportdialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            reportdialog!!.setCanceledOnTouchOutside(false)

            val close: TextView = reportdialog!!.findViewById(R.id.dialog_report_tvClose)
            val queproblem: TextView = reportdialog!!.findViewById(R.id.dialog_report_tvQueProblem)
            val ansproblem: TextView = reportdialog!!.findViewById(R.id.dialog_report_tvAnsProblem)
            val hintexplanation: TextView =
                reportdialog!!.findViewById(R.id.dialog_report_tvHintProblem)

            hintexplanation.text = getString(R.string.hint_has_a_problem)

            queproblem.setOnClickListener {

                CommonWebCalls.callReportIssue(
                    this@QuestionActivity,
                    "1",
                    queproblem.text.toString(),

                    QuestionActivity.movies[
                            QuestionActivity.q_grppos1].TestQuestion[
                            QuestionActivity.curr_index].QuestionID.toString()
                )

                reportdialog!!.dismiss()
            }

            ansproblem.setOnClickListener {

                CommonWebCalls.callReportIssue(
                    this@QuestionActivity,
                    "2",
                    ansproblem.text.toString(),

                    QuestionActivity.movies[
                            QuestionActivity.q_grppos1].TestQuestion[
                            QuestionActivity.curr_index].QuestionID.toString()
                )

                reportdialog!!.dismiss()
            }

            hintexplanation.setOnClickListener {

                CommonWebCalls.callReportIssue(
                    this@QuestionActivity,
                    "3",
                    hintexplanation.text.toString(),

                    QuestionActivity.movies[
                            QuestionActivity.q_grppos1].TestQuestion[
                            QuestionActivity.curr_index].QuestionID.toString()
                )

                reportdialog!!.dismiss()
            }

            close.setOnClickListener {
                reportdialog!!.dismiss()
            }

            reportdialog!!.show()
        }

        queTab_ivInfo.setOnClickListener {

            val dialog = Dialog(this@QuestionActivity)
            dialog.setContentView(R.layout.dialog_question_information)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCanceledOnTouchOutside(true)

            val testname1: TextView = dialog.findViewById(R.id.dialog_queinfo_tvHeader)
            val subjectname: TextView = dialog.findViewById(R.id.dialog_queinfo_tvSubjectName)
            val course: TextView = dialog.findViewById(R.id.dialog_queinfo_tvCourse)
            val marks: TextView = dialog.findViewById(R.id.dialog_queinfo_tvTotalMarks)
            val totalque: TextView = dialog.findViewById(R.id.dialog_queinfo_tvQue)
            val tutorname: TextView = dialog.findViewById(R.id.dialog_queinfo_tvTeacherName)

            testname1.text = testname
            subjectname.text = testsubject
            course.text = testcourse
            marks.text = testmarks
            totalque.text = testque
            tutorname.text = testtutor

            dialog.show()
        }

        queTab_ivBack.setOnClickListener {

            //            stopTimer()

            onBackPressed()
        }

        queTab_ivSubmit.setOnClickListener {

            if (reportdialog != null && reportdialog!!.isShowing) {
                reportdialog!!.dismiss()
            }

            DialogUtils.createConfirmDialog(
                this@QuestionActivity,
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

                    if (queTab_tvTimer.text.toString().equals("00:00:00", true)) {

                        getType("continue", 0, curr_index)

                        DialogUtils.createConfirmDialog1(
                            this@QuestionActivity,
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

                    } else {
                        dialog.dismiss()
                    }

                }).show()
        }
    }

    var dialog1: Dialog? = null

    fun nextButtonClick() {

        var isReview = 0

        if (queTab_ivReview.isChecked) {

            isReview = 1

        }

        integeranswer = queTab_tvFillBlanks.text.toString()

        when {
            nextButton!!.text == "Next" -> {

                que_number += 1

                continuetime = 0

                for (i in 0 until finalArr[sectionList!![q_grppos1]]!!.size) {
                    if (finalArr[sectionList!![q_grppos1]]!![i].qnumber == curr_index) {
                        if (finalArr[sectionList!![q_grppos1]]!![i].type != 4) {
                            finalArr[sectionList!![q_grppos1]]!![i].type = 2
                            movies[q_grppos1].TestQuestion[curr_index].Review = "0"
                        } else {
                            finalArr[sectionList!![q_grppos1]]!![i].type = 4
                            movies[q_grppos1].TestQuestion[curr_index].Review = "1"
                        }
                    }
                }

                Log.d("itype", "activity")

                getType("activity", isReview, curr_index)

            }
            nextButton!!.text == "Skip" -> {

                continuetime = 0

                que_number += 1

                for (i in 0 until finalArr[sectionList!![q_grppos1]]!!.size) {
                    if (finalArr[sectionList!![q_grppos1]]!![i].qnumber == curr_index) {
                        if (finalArr[sectionList!![q_grppos1]]!![i].type != 4) {
                            finalArr[sectionList!![q_grppos1]]!![i].type = 3
                            movies[q_grppos1].TestQuestion[curr_index].Review = "0"
                        } else {
                            finalArr[sectionList!![q_grppos1]]!![i].type = 4
                            movies[q_grppos1].TestQuestion[curr_index].Review = "1"
                        }
                    }
                }

                Log.d("itype", "skip")

                getType("skip", isReview, curr_index)
            }

            nextButton!!.text == "Submit Test" -> {

                DialogUtils.createConfirmDialog(
                    this@QuestionActivity,
                    "Done?",
                    "Are you sure you want to end this test?",
                    "OK",
                    "Cancel",
                    DialogInterface.OnClickListener { dialog, which ->

                        getType("activity", isReview, curr_index)
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
        var curr_index = 0

        fun setButton(position: Int, ansid: String, qid: Int) {

            if (nextButton!!.text.toString() != "Submit Test") {
                nextButton!!.text = "Next"
            }

            ansArr = ArrayList()

            if (movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID == 1) {

                val answerModel = AnswerModel()
                answerModel.ansid = ansid
                answerModel.ansresult = true
                ansArr.add(answerModel)

            } else {

                if (ansid != "") {
                    val answerModel = AnswerModel()
                    answerModel.ansid = ansid
                    answerModel.ansresult = true
                    ansArr.add(answerModel)
                } else {
                    nextButton!!.text = "Skip"
                }
            }
        }
    }

    fun callInsertHint() {

        if (!DialogUtils.isNetworkConnected(this@QuestionActivity)) {
            Utils.ping(this@QuestionActivity, "Connetion not available")
        }

        DialogUtils.showDialog(this@QuestionActivity)

        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.Inserttesthint(
            testid,
            movies[q_grppos1].TestQuestion[curr_index].QuestionID.toString()
        )

        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                DialogUtils.dismissDialog()

                if (response.body() != null) {

                    movies[q_grppos1].TestQuestion[curr_index].HintUsed = "1"
                    queTab_ivHint.setImageResource(R.drawable.hint_bulb_yellow)

                    val dialog = Dialog(this@QuestionActivity)
                    dialog.setContentView(R.layout.hint_dialog)
                    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    dialog.setCanceledOnTouchOutside(false)

                    val hintWebview: WebView = dialog.findViewById(R.id.dialog_hint_wvHint)
                    val header: TextView = dialog.findViewById(R.id.dialog_hint_tvHeader)
                    val closeBtn: View = dialog.findViewById(R.id.dialog_hint_btnClose)

                    header.text = "Hint"

                    hintWebview.settings.javaScriptEnabled = true
                    hintWebview.loadDataWithBaseURL(
                        AppConstants.EXPHINT_IMAGE_BASE_URL,
                        hintData,
                        "text/html",
                        "UTF-8",
                        ""
                    )

                    closeBtn.setOnClickListener {

                        if (total_hint != "0") {
                            queTab_tvCurrHint.text = hintCount.toString() + "/" + total_hint
                            queTab_ivHint.visibility = View.VISIBLE
                        } else {
                            queTab_tvCurrHint.text = ""
                            queTab_ivHint.visibility = View.GONE
                        }

                        dialog.dismiss()
                    }

                    dialog.show()

//                    Toast.makeText(this@QuestionActivity, response.body()!!["Msg"].asString, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // Log error here since request failed
                Log.e("", t.toString())
                DialogUtils.dismissDialog()
            }
        })
    }


}
