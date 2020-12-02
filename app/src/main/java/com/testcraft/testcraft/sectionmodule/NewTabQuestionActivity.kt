package com.testcraft.testcraft.sectionmodule

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.*
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
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import com.testcraft.testcraft.Connectivity
import com.testcraft.testcraft.R
import com.testcraft.testcraft.activity.DashboardActivity
import com.testcraft.testcraft.activity.ResultActivity
import com.testcraft.testcraft.adapter.QuestionAttemptAdapter
import com.testcraft.testcraft.adapter.SelectImageOptionAdapter
import com.testcraft.testcraft.interfaces.FilterTypeSelectionInteface
import com.testcraft.testcraft.models.AnswerModel
import com.testcraft.testcraft.models.AttemptModel
import com.testcraft.testcraft.models.QuestionTypeModel
import com.testcraft.testcraft.retrofit.WebClient
import com.testcraft.testcraft.retrofit.WebInterface
import com.testcraft.testcraft.sectionmodule.ImageViewAdapter.Companion.getPageNumber
import com.testcraft.testcraft.utils.*
import kotlinx.android.synthetic.main.activity_tabwise_question.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.util.concurrent.TimeUnit

@SuppressLint("SetJavaScriptEnabled", "SetTextI18n")
class NewTabQuestionActivity : FragmentActivity(), FilterTypeSelectionInteface {

    var come_from = ""

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

    private var mDrawerToggle: ActionBarDrawerToggle? = null

    var qtime = 0
    var continuetime = 0
    var que_number = 1

    var reportdialog: Dialog? = null

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    var connectivity: Connectivity? = null

    override fun onStop() {
        super.onStop()

        unregisterReceiver(connectivity)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        setContentView(R.layout.activity_tabwise_question)

        connectivity = Connectivity()

        shouldExecuteOnResume = false

        come_from = intent.getStringExtra("isComeFrom")

        testid = intent.getStringExtra("testid")
        testtime = intent.getStringExtra("testtime")
        studenttestid = intent.getStringExtra("studenttestid")

        testname = intent.getStringExtra("testname")
        testque = intent.getStringExtra("totalque")
        testsubject = intent.getStringExtra("subjectname")
        testcourse = intent.getStringExtra("coursename")
        testmarks = intent.getStringExtra("totalmarks")
        testtutor = intent.getStringExtra("tutorname")
        total_hint = intent.getStringExtra("totalhint")
        total_hint_used = intent.getStringExtra("totalhintused")

        queTab_header.text = testname

        if (total_hint_used != "0" && total_hint_used != "") {
            hintCount = total_hint_used.toInt()
        }

        if (Utils.getStringValue(this@NewTabQuestionActivity, AppConstants.APP_MODE, "") != AppConstants.DEEPLINK_MODE) {
            queTab_ivBack.visibility = View.VISIBLE
        } else {
            queTab_ivBack.visibility = View.GONE
        }

        curr_index = 0
        q_grppos1 = 0

        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        ansArr = ArrayList()
        sectionList = ArrayList()
        finalArr = HashMap()

        queTab_tvFillBlanks.setText(integeranswer)

        filterTypeSelectionInteface = this

        context = this@NewTabQuestionActivity

        imgQue = findViewById(R.id.page_img_que_img)
        ansList = findViewById(R.id.wv_question_list)
        nextButton = findViewById(R.id.queTab_btnNext)
        sideList = findViewById(R.id.queTab_expQueList)

        imgQue!!.isDrawingCacheEnabled = true

        mDrawerToggle = ActionBarDrawerToggle(
            this, drawer_layout, null, // nav menu toggle icon
            R.string.app_name, // nav drawer open - description for accessibility
            R.string.app_name
        )

        wv_question_list.isNestedScrollingEnabled = false

        drawer_layout.setDrawerListener(mDrawerToggle)

        val Groupinfodialog = Dialog(this@NewTabQuestionActivity)

        queTab_ivRefresh.setOnClickListener {

            if (movies.size > 0) {
                Picasso.get()
                    .load(movies[q_grppos1].TestQuestion[curr_index].QuestionImage)
                    .transform(transform.getTransformation(imgQue!!))
                    .into(imgQue)

                if (answer == "" && movies[q_grppos1].TestQuestion[curr_index].Answer == "") {

                    if (movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID == 1 || movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID == 7) {

                        ansList!!.adapter = SelectImageOptionAdapter(
                            this@NewTabQuestionActivity,
                            movies[q_grppos1].TestQuestion[curr_index].StudentTestQuestionMCQ,
                            imgQue!!.width,
                            movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID,
                            movies[q_grppos1].TestQuestion[curr_index].Answer, 1
                        )
                    }
                }
            }
        }

        queTab_expQueList.setOnGroupClickListener { parent, v, groupPosition, id ->

            CommonWebCalls.callToken(
                this@NewTabQuestionActivity,
                "1",
                "",
                ActionIdData.C2009,
                ActionIdData.T2009
            )

            if (movies.size > 0) {
                if (movies[groupPosition].SectionInstruction != "" && !Groupinfodialog.isShowing) {

                    Groupinfodialog.setContentView(R.layout.hint_dialog)
                    Groupinfodialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    Groupinfodialog.setCanceledOnTouchOutside(false)

                    val hintWebview: WebView = Groupinfodialog.findViewById(R.id.dialog_hint_wvHint)
                    val header: TextView = Groupinfodialog.findViewById(R.id.dialog_hint_tvHeader)

                    val closeBtn: View = Groupinfodialog.findViewById(R.id.dialog_hint_btnClose)

                    header.text = movies[groupPosition].SectionName

                    hintWebview.settings.javaScriptEnabled = true

                    hintWebview.loadDataWithBaseURL(
                        "",
                        "<html><body style='background-color:clear;'><p>" + movies[groupPosition].SectionInstruction + "</p></body></html>",
                        "text/html",
                        "UTF-8",
                        ""
                    )

                    closeBtn.setOnClickListener { Groupinfodialog.dismiss() }

                    Groupinfodialog.show()

                }
            }
            false

        }

        queTab_tvFillBlanks.setOnEditorActionListener { v, actionId, event ->

            if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {

                integeranswer = queTab_tvFillBlanks.text.toString()

            }

            false
        }

        queTab_ivClear.setOnClickListener {

            if (queTab_ivReview.isChecked) {
                getType("clear", 1, curr_index)
            } else {
                getType("clear", 0, curr_index)
            }

        }

        queTab_tvFillBlanks.addTextChangedListener(
            object : TextWatcher {

                override fun afterTextChanged(s: Editable) {

                    CommonWebCalls.callToken(
                        this@NewTabQuestionActivity,
                        "1",
                        "",
                        ActionIdData.C2006,
                        ActionIdData.T2006
                    )

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

        clicks()

        ansList!!.layoutManager =
            LinearLayoutManager(this@NewTabQuestionActivity, LinearLayoutManager.VERTICAL, false)

        if (testid != "") {
            callQuestionApi()
        }
    }

    fun callQuestionApi() {

        if (!DialogUtils.isNetworkConnected(this@NewTabQuestionActivity)) {

            continuetime = 0

            DialogUtils.createConfirmDialog1(
                this@NewTabQuestionActivity,
                "Try Again",
                "Please check your internet Connection.",
                DialogInterface.OnClickListener { dialog, which ->

                    continuetime = 0

                    AppConstants.isFirst = 12
                    val intent = Intent(this@NewTabQuestionActivity, DashboardActivity::class.java)
                    startActivity(intent)
                    finish()

                }).show()

        } else {

            DialogUtils.showDialog(this@NewTabQuestionActivity)

            val apiService = WebClient.getClient().create(WebInterface::class.java)

            val call = apiService.getNewQuestions(testid, studenttestid)

            call.enqueue(object : Callback<NewQuestionResponse> {
                @SuppressLint("SetTextI18n")
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
                                this@NewTabQuestionActivity,
                                "Submit",
                                "Your test time is over",

                                DialogInterface.OnClickListener { dialog, which ->

                                    var ansstr = ""

                                    for (i in 0 until ansArr.size) {
                                        ansstr =
                                            ansstr + ansArr[i].qid + "|" + ansArr[i].ansid + ","

                                    }

                                    Log.d("ansstr", ansstr)

                                    if (queTab_ivReview.isChecked) {
                                        getType("submit", 1, curr_index)
                                    } else {
                                        getType("submit", 0, curr_index)
                                    }

                                    OpenAttemptDialog()

                                }).show()
                        }

                        movies = response.body()!!.data

                        if (movies.size > 0) {

                            queTab_tvQMarks.text = "Marks : " + movies[0].TestQuestion[0].Marks

                            if (movies[0].TestQuestion[0].QuestionTypeID == 1 || movies[0].TestQuestion[0].QuestionTypeID == 4) {

                                queTab_ivClear.visibility = View.VISIBLE
                            } else {

                                queTab_ivClear.visibility = View.GONE
                            }

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

                                            if (movies[i].TestQuestion[j].Answered != "") {
                                                if (movies[i].TestQuestion[j].Answer != "") {

                                                    questionTypeModel.type = 2
                                                } else {
                                                    questionTypeModel.type = 3
                                                }
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
                                    .load(movies[0].TestQuestion[0].QuestionImage)
                                    .transform(transform.getTransformation(imgQue!!))
                                    .into(imgQue)

                                ansList!!.layoutManager =
                                    LinearLayoutManager(
                                        this@NewTabQuestionActivity,
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
                                        this@NewTabQuestionActivity,
                                        movies[0].TestQuestion[0].StudentTestQuestionMCQ,
                                        imgQue!!.width,
                                        movies[0].TestQuestion[0].QuestionTypeID,
                                        movies[0].TestQuestion[0].Answer, 0
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

                                    when (movies[0].TestQuestion[0].Answer) {
                                        "1"  -> {
                                            queTab_rbTrue.isChecked = true
                                            queTab_rbFalse.isChecked = false

                                        }
                                        "0"  -> {
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
                    } else {

                    }

                    Handler().postDelayed(
                        {
                            DialogUtils.dismissDialog()
                        }, 1000
                    )
                    Log.d("imgcall", "Number of movies received: " + movies.size)
                }

                override fun onFailure(call: Call<NewQuestionResponse>, t: Throwable) {
                    Log.e("", t.toString())

                    DialogUtils.dismissDialog()
                }
            })
        }
    }

    var waitTimer: CountDownTimer? = null
    var milliLeft: Long = 0
    var isTestOver = false

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

    override fun onStart() {
        super.onStart()

        val filter = IntentFilter()
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        registerReceiver(connectivity, filter)

    }

    override fun onResume() {
        super.onResume()

        if (shouldExecuteOnResume!!) {
            if (!isTestOver) {
                timerResume()
            }
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

                    if (movies.size > 0) {
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
                    }

                }

                if (continuetime == 60) {

                    if (reportdialog != null && reportdialog!!.isShowing) {
                        reportdialog!!.dismiss()
                    }

                    DialogUtils.createConfirmDialog(this@NewTabQuestionActivity, "Alert",
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

                if (reportdialog != null && reportdialog!!.isShowing) {
                    reportdialog!!.dismiss()
                }

                queTab_tvTimer.text = getString(R.string._00_00)

                getType("continue", 0, curr_index)

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

                        if (queTab_ivReview.isChecked) {
                            getType("submit", 1, curr_index)
                        } else {
                            getType("submit", 0, curr_index)
                        }

                        OpenAttemptDialog()

                    }).show()

            }

        }.start()
    }

    override fun onBackPressed() {

        if (Utils.getStringValue(this@NewTabQuestionActivity, AppConstants.APP_MODE, "") != AppConstants.DEEPLINK_MODE) {

            DialogUtils.createConfirmDialog(
                this@NewTabQuestionActivity,
                "Leave?",
                "Are you sure you wish to leave the test?",
                "Yes",
                "Cancel",
                DialogInterface.OnClickListener { dialog, which ->

                    continuetime = 0

                    if (come_from == "testlist") {

                        AppConstants.isFirst = 12
                        val intent =
                            Intent(this@NewTabQuestionActivity, DashboardActivity::class.java)
                        startActivity(intent)
                        finish()

                    } else if (come_from == "freetest") {

                        AppConstants.isFirst = 1
                        val intent =
                            Intent(this@NewTabQuestionActivity, DashboardActivity::class.java)
                        startActivity(intent)
                        finish()

                    } else {

                        super.onBackPressed()

                    }

                },
                DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()

                }).show()
        }

    }

    fun callSubmitAPI() {

        continuetime = 0
        isTestOver = true
        stopTimer()

        DialogUtils.showDialog(this@NewTabQuestionActivity)

        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.submitTest(studenttestid)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                DialogUtils.dismissDialog()

                if (response.body()!!.get("Status").asString == "true") {

                    if (attemptDialog!!.isShowing) {
                        attemptDialog!!.dismiss()
                    }

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

//                Toast.makeText(
//                    this@NewTabQuestionActivity,
//                    "submit fail",
//                    Toast.LENGTH_LONG
//                ).show()

                Toast.makeText(
                    this@NewTabQuestionActivity,
                    "submit fail" + t.message,
                    Toast.LENGTH_LONG
                ).show()

                Log.e("", t.toString())
                DialogUtils.dismissDialog()
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
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                nextButton!!.isClickable = true
                nextButton!!.isEnabled = true

                if (response.body()!!.get("Status").asString == "true") {

                    if (que_number >= testque.toInt()) {
                        setNextSkipButtonText(2)
                    }

                    ansArr = ArrayList()

                    if (type != "continue" && type != "review") {

                        if (type == "clear" || type == "next" || type == "skip") {

                            queTab_tvCurrTotal.text = "$que_number/$testque"

                            if ((finalArr.size - 1) > q_grppos1) {

                                if (finalArr[sectionList!![q_grppos1]]!!.size == curr_index && p1 == (curr_index - 1)) {

                                    continuetime = 0

                                    q_grppos1 += 1
                                    curr_index = 0
                                    p1 = 0
                                    finalArr[sectionList!![q_grppos1]]!![p1].type = 5
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

                                if (movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID == 1 || movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID == 4) {

                                    queTab_ivClear.visibility = View.VISIBLE
                                } else {

                                    queTab_ivClear.visibility = View.GONE
                                }

                                if (movies[q_grppos1].TestQuestion[curr_index].QuestionImage != "") {

                                    Picasso.get()
                                        .load(movies[q_grppos1].TestQuestion[curr_index].QuestionImage)
                                        .transform(transform.getTransformation(imgQue!!))
                                        .into(imgQue)

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
                                            movies[q_grppos1].TestQuestion[curr_index].Answer, 0
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

                                        when (movies[q_grppos1].TestQuestion[curr_index].Answer) {
                                            "1"  -> {
                                                queTab_rbTrue.isChecked = true
                                                queTab_rbFalse.isChecked = false
                                            }
                                            "0"  -> {
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

                                    if (movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID == 1 || movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID == 4) {

                                        queTab_ivClear.visibility = View.VISIBLE
                                    } else {

                                        queTab_ivClear.visibility = View.GONE
                                    }

                                    if (movies[q_grppos1].TestQuestion[curr_index].QuestionImage != "") {

                                        Picasso.get()
                                            .load(movies[q_grppos1].TestQuestion[curr_index].QuestionImage)
                                            .transform(transform.getTransformation(imgQue!!))
                                            .into(imgQue)

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
                                                movies[q_grppos1].TestQuestion[curr_index].Answer, 0
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

                                            when (movies[q_grppos1].TestQuestion[curr_index].Answer) {
                                                "1"  -> {
                                                    queTab_rbTrue.isChecked = true
                                                    queTab_rbFalse.isChecked = false
                                                }
                                                "0"  -> {
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

                        } else if (type == "submit") {

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

//                    } else if(type == "clear") {
//
//                        sideList!!.setAdapter(
//                            NewSideMenuAdapter(
//                                context!!,
//                                sectionList!!,
//                                finalArr,
//                                filterTypeSelectionInteface!!,
//                                "")
//                        )

                    } else {
                        if (reviewid != "0") {

                            queTab_ivReview.setBackgroundResource(R.drawable.rotate_eye_blue)
                            queTab_ivReview.isChecked = true

                        } else {

                            queTab_ivReview.setBackgroundResource(R.drawable.rotate_eye_gray)
                            queTab_ivReview.isChecked = false

                        }

                    }

                } else {

                    Toast.makeText(
                        this@NewTabQuestionActivity,
                        response.body()!!.get("Msg").asString,
                        Toast.LENGTH_LONG
                    ).show()
                }

            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.e("", t.toString())
            }
        })
    }

    @SuppressLint("RtlHardcoded", "SetTextI18n")
    override fun getType(itype: String, p0: Int, p1: Int) {

        if (movies.size > 0) {
            curr_index = p1

            if (itype != "clear" && itype != "adapter") {

                when (movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID) {

                    1 -> {

                        if (itype == "next" || itype == "submit" || itype == "continue" || itype == "review" || itype == "sidemenu") {

                            answer = if (ansArr.size > 0) {
                                ansArr[0].ansid
                            } else {
                                movies[q_grppos1].TestQuestion[curr_index].Answer
                            }
                        }
                    }

                    7 -> {

                        if (itype == "next" || itype == "submit" || itype == "continue" || itype == "review" || itype == "sidemenu") {

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

                            answer = integeranswer
                        }
                    }

                    4 -> {

                        if (itype == "next" || itype == "submit" || itype == "continue" || itype == "review" || itype == "sidemenu") {

                            answer = when {
                                queTab_rbFalse.isChecked -> {
                                    "0"
                                }
                                queTab_rbTrue.isChecked  -> {
                                    "1"
                                }
                                else                     -> {
                                    ""
                                }
                            }
                        }
                    }
                }

            } else if (itype == "adapter") {

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

                que_number = getPageNumber()

                queTab_tvCurrTotal.text = "$que_number/$testque"

                if ((finalArr.size - 1) > q_grppos1) {

                    if (movies[q_grppos1].TestQuestion[curr_index].Answer != "") {
                        setNextSkipButtonText(1)
                    } else {
                        setNextSkipButtonText(0)
                    }

                } else {

                    if (que_number >= testque.toInt()) {
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

                if (movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID == 1 || movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID == 4) {

                    queTab_ivClear.visibility = View.VISIBLE
                } else {

                    queTab_ivClear.visibility = View.GONE
                }

                if (movies[q_grppos1].TestQuestion[curr_index].QuestionImage != "") {

                    Picasso.get()
                        .load(movies[q_grppos1].TestQuestion[curr_index].QuestionImage)
                        .transform(transform.getTransformation(imgQue!!))
                        .into(imgQue)

                    ansList!!.layoutManager = LinearLayoutManager(
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
                            movies[q_grppos1].TestQuestion[curr_index].Answer, 0
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

                        when (movies[q_grppos1].TestQuestion[curr_index].Answer) {

                            "1"  -> {
                                queTab_rbTrue.isChecked = true
                                queTab_rbFalse.isChecked = false
                            }
                            "0"  -> {
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

            }

            if (itype == "next") {

                Log.d("current_index_next", "" + curr_index)

                movies[q_grppos1].TestQuestion[curr_index].Answer = answer

                if (curr_index <= finalArr[sectionList!![q_grppos1]]!!.size - 1) {
                    curr_index += 1
                }

                callSubmitAnswer(
                    itype,
                    movies[q_grppos1].TestQuestion[curr_index - 1].TestQuestionID,
                    movies[q_grppos1].TestQuestion[curr_index - 1].QuestionID,
                    movies[q_grppos1].TestQuestion[curr_index - 1].QuestionTypeID,
                    answer,
                    curr_index - 1, p0.toString()
                )

            } else if (itype == "continue" || itype == "review") {

                queTab_tvCurrTotal.text = "$que_number/$testque"

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

            } else if (itype == "submit") {

                Log.d("current_index_next", "" + curr_index)

                movies[q_grppos1].TestQuestion[curr_index].Answer = answer

                if (p0 == 1) {
                    finalArr[sectionList!![q_grppos1]]!![curr_index].type = 4

                } else {

                    if (answer != "") {
                        finalArr[sectionList!![q_grppos1]]!![curr_index].type = 2

                    } else {
                        finalArr[sectionList!![q_grppos1]]!![curr_index].type = 3

                    }

                }

                callSubmitAnswer(
                    itype,
                    movies[q_grppos1].TestQuestion[curr_index].TestQuestionID,
                    movies[q_grppos1].TestQuestion[curr_index].QuestionID,
                    movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID,
                    answer,
                    curr_index, p0.toString()
                )

            } else if (itype.equals("skip", ignoreCase = true)) {

                Log.d("current_index_next", "" + curr_index)

                if (curr_index <= finalArr[sectionList!![q_grppos1]]!!.size - 1) {
                    curr_index += 1
                }

                callSubmitAnswer(
                    itype,
                    movies[q_grppos1].TestQuestion[curr_index - 1].TestQuestionID,
                    movies[q_grppos1].TestQuestion[curr_index - 1].QuestionID,
                    movies[q_grppos1].TestQuestion[curr_index - 1].QuestionTypeID,
                    "",
                    curr_index - 1, p0.toString()
                )

            } else if (itype == "sidemenu") {

                Log.d("current_index_next", "" + curr_index)

                when {
                    queTab_btnNext.text.toString() == "Next" -> {

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

                        movies[q_grppos1].TestQuestion[curr_index].Answer = answer

                        callSubmitAnswer(
                            "next",
                            movies[q_grppos1].TestQuestion[curr_index].TestQuestionID,
                            movies[q_grppos1].TestQuestion[curr_index].QuestionID,
                            movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID,
                            answer,
                            curr_index, p0.toString()
                        )

                    }
                    queTab_btnNext.text.toString() == "Skip" -> {

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

                        callSubmitAnswer(
                            "skip",
                            movies[q_grppos1].TestQuestion[curr_index].TestQuestionID,
                            movies[q_grppos1].TestQuestion[curr_index].QuestionID,
                            movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID,
                            "",
                            curr_index, p0.toString()
                        )

                    }
                    else                                     -> {

                        movies[q_grppos1].TestQuestion[curr_index].Answer = answer

                        if (p0 == 1) {
                            finalArr[sectionList!![q_grppos1]]!![curr_index].type = 4

                        } else {

                            if (answer != "") {
                                finalArr[sectionList!![q_grppos1]]!![curr_index].type = 2

                            } else {
                                finalArr[sectionList!![q_grppos1]]!![curr_index].type = 3

                            }
                        }

                        callSubmitAnswer(
                            "submit",
                            movies[q_grppos1].TestQuestion[curr_index].TestQuestionID,
                            movies[q_grppos1].TestQuestion[curr_index].QuestionID,
                            movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID,
                            answer,
                            curr_index, p0.toString()
                        )
                    }
                }

            } else if (itype == "clear") {

                queTab_tvCurrTotal.text = "$que_number/$testque"

                Log.d("current_index_clear", "" + curr_index)

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

                movies[q_grppos1].TestQuestion[curr_index].Answer = ""

                callSubmitAnswer(
                    itype,
                    movies[q_grppos1].TestQuestion[curr_index].TestQuestionID,
                    movies[q_grppos1].TestQuestion[curr_index].QuestionID,
                    movies[q_grppos1].TestQuestion[curr_index].QuestionTypeID,
                    "",
                    curr_index, p0.toString()
                )

            }

            Log.d("que_number_type", "" + curr_index)
        }
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

    @SuppressLint("SetJavaScriptEnabled", "WrongConstant")
    fun clicks() {

        queTab_ivReview.setOnCheckedChangeListener { buttonView, isChecked ->

            CommonWebCalls.callToken(
                this@NewTabQuestionActivity,
                "1",
                "",
                ActionIdData.C2004,
                ActionIdData.T2004
            )

            if (queTab_ivReview.isPressed) {
                if (finalArr.size > 0 && movies.size > 0) {
                    if (isChecked) {

                        Log.d("isreview3", "" + 1)

                        queTab_ivReview.setBackgroundResource(R.drawable.rotate_eye_blue)

                        for (i in 0 until finalArr[sectionList!![q_grppos1]]!!.size) {
                            if (finalArr[sectionList!![q_grppos1]]!![i].qnumber == curr_index) {

                                finalArr[sectionList!![q_grppos1]]!![i].type = 4
                                movies[q_grppos1].TestQuestion[curr_index].Review = "1"

                                break
                            }
                        }

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

            if (que_number <= testque.toInt()) {
                nextButtonClick()
            }
        }

        queTab_rbTrue.setOnCheckedChangeListener { buttonView, isChecked ->

            CommonWebCalls.callToken(
                this@NewTabQuestionActivity,
                "1",
                "",
                ActionIdData.C2006,
                ActionIdData.T2006
            )

            if (isChecked) {
                queTab_rbTrue.isChecked = true
                queTab_rbFalse.isChecked = false

                if (nextButton!!.text != "Submit Test") {
                    setNextSkipButtonText(1)
                }
            }
        }

        queTab_rbFalse.setOnCheckedChangeListener { buttonView, isChecked ->

            CommonWebCalls.callToken(
                this@NewTabQuestionActivity,
                "1",
                "",
                ActionIdData.C2006,
                ActionIdData.T2006
            )

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
                        this@NewTabQuestionActivity,
                        "Sorry! Your hint limit is over",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } else {

                val hintdialog = Dialog(this@NewTabQuestionActivity)
                hintdialog.setContentView(R.layout.hint_dialog)
                hintdialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                hintdialog.setCanceledOnTouchOutside(false)

                val hintWebview: WebView = hintdialog.findViewById(R.id.dialog_hint_wvHint)
                val header: TextView = hintdialog.findViewById(R.id.dialog_hint_tvHeader)
                val closeBtn: View = hintdialog.findViewById(R.id.dialog_hint_btnClose)

                header.text = "Hint"

                hintWebview.settings.javaScriptEnabled = true
                hintWebview.loadDataWithBaseURL(
                    AppConstants.EXPHINT_IMAGE_BASE_URL,
                    hintData,
                    "text/html",
                    "UTF-8",
                    ""
                )

                closeBtn.setOnClickListener { hintdialog.dismiss() }

                hintdialog.show()

            }
        }

        queTab_btnNextt.setOnClickListener {

            drawer_layout.openDrawer(Gravity.END)

            Handler().postDelayed(
                {

                    if (queTab_ivReview.isChecked) {
                        getType("sidemenu", 1, curr_index)
                    } else {
                        getType("sidemenu", 0, curr_index)
                    }

                }, 500
            )
        }

        queTab_ivReporttxt.setOnClickListener {

            CommonWebCalls.callToken(
                this@NewTabQuestionActivity,
                "1",
                "",
                ActionIdData.C2005,
                ActionIdData.T2005
            )

            reportdialog = Dialog(this@NewTabQuestionActivity)
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

                CommonWebCalls.callToken(
                    this@NewTabQuestionActivity,
                    "1",
                    "",
                    ActionIdData.C2101,
                    ActionIdData.T2101
                )

                CommonWebCalls.callReportIssue(
                    this@NewTabQuestionActivity,
                    "1",
                    queproblem.text.toString(),
                    movies[q_grppos1].TestQuestion[curr_index].QuestionID.toString()
                )

                reportdialog!!.dismiss()
            }

            ansproblem.setOnClickListener {

                CommonWebCalls.callToken(
                    this@NewTabQuestionActivity,
                    "1",
                    "",
                    ActionIdData.C2102,
                    ActionIdData.T2102
                )

                CommonWebCalls.callReportIssue(
                    this@NewTabQuestionActivity,
                    "2",
                    ansproblem.text.toString(),
                    movies[q_grppos1].TestQuestion[curr_index].QuestionID.toString()
                )

                reportdialog!!.dismiss()
            }

            hintexplanation.setOnClickListener {

                CommonWebCalls.callToken(
                    this@NewTabQuestionActivity,
                    "1",
                    "",
                    ActionIdData.C2103,
                    ActionIdData.T2103
                )

                CommonWebCalls.callReportIssue(
                    this@NewTabQuestionActivity,
                    "3",
                    hintexplanation.text.toString(),
                    movies[q_grppos1].TestQuestion[curr_index].QuestionID.toString()
                )

                reportdialog!!.dismiss()
            }

            close.setOnClickListener {

                CommonWebCalls.callToken(
                    this@NewTabQuestionActivity,
                    "1",
                    "",
                    ActionIdData.C2104,
                    ActionIdData.T2104
                )

                reportdialog!!.dismiss()
            }

            reportdialog!!.show()
        }

        queTab_ivInfo.setOnClickListener {

            CommonWebCalls.callToken(
                this@NewTabQuestionActivity,
                "1",
                "",
                ActionIdData.C2002,
                ActionIdData.T2002
            )

            val testinfodialog = Dialog(this@NewTabQuestionActivity)
            testinfodialog.setContentView(R.layout.dialog_question_information)
            testinfodialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            testinfodialog.setCanceledOnTouchOutside(true)

            val testname1: TextView = testinfodialog.findViewById(R.id.dialog_queinfo_tvHeader)
            val subjectname: TextView =
                testinfodialog.findViewById(R.id.dialog_queinfo_tvSubjectName)
            val course: TextView = testinfodialog.findViewById(R.id.dialog_queinfo_tvCourse)
            val marks: TextView = testinfodialog.findViewById(R.id.dialog_queinfo_tvTotalMarks)
            val totalque: TextView = testinfodialog.findViewById(R.id.dialog_queinfo_tvQue)
            val tutorname: TextView = testinfodialog.findViewById(R.id.dialog_queinfo_tvTeacherName)

            testname1.text = testname
            subjectname.text = testsubject
            course.text = testcourse
            marks.text = testmarks
            totalque.text = testque
            tutorname.text = testtutor

            testinfodialog.show()
        }

        queTab_ivBack.setOnClickListener {

            onBackPressed()
        }

        queTab_ivSubmit.setOnClickListener {

            CommonWebCalls.callToken(
                this@NewTabQuestionActivity,
                "1",
                "",
                ActionIdData.C2003,
                ActionIdData.T2003
            )

            if (reportdialog != null && reportdialog!!.isShowing) {
                reportdialog!!.dismiss()
            }

            if (queTab_ivReview.isChecked) {
                getType("submit", 1, curr_index)
            } else {
                getType("submit", 0, curr_index)
            }

            OpenAttemptDialog()

        }
    }

    fun nextButtonClick() {

        nextButton!!.isClickable = false
        nextButton!!.isEnabled = false

        var isReview = 0

        if (queTab_ivReview.isChecked) {

            isReview = 1
        }

        integeranswer = queTab_tvFillBlanks.text.toString()

        when (nextButton!!.text) {

            "Next" -> {

                CommonWebCalls.callToken(
                    this@NewTabQuestionActivity,
                    "1",
                    "",
                    ActionIdData.C2007,
                    ActionIdData.T2007
                )

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

                Log.d("itype", "next")

                getType("next", isReview, curr_index)
            }

            "Skip" -> {

                CommonWebCalls.callToken(
                    this@NewTabQuestionActivity,
                    "1",
                    "",
                    ActionIdData.C2007,
                    ActionIdData.T2007
                )

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

            "Submit Test" -> {

                CommonWebCalls.callToken(
                    this@NewTabQuestionActivity,
                    "1",
                    "",
                    ActionIdData.C2010,
                    ActionIdData.T2010
                )

                if (reportdialog != null && reportdialog!!.isShowing) {
                    reportdialog!!.dismiss()
                }

                if (queTab_ivReview.isChecked) {
                    getType("submit", 1, curr_index)
                } else {
                    getType("submit", 0, curr_index)
                }

                OpenAttemptDialog()

            }
        }
    }

    companion object {

        var nextButton: Button? = null
        var sideList: ExpandableListView? = null
        var sectionList: ArrayList<String>? = null
        var filterTypeSelectionInteface: FilterTypeSelectionInteface? = null
        var movies: ArrayList<NewQuestionResponse.QuestionList> = ArrayList()
        var ansArr: ArrayList<AnswerModel> = ArrayList()
        var context: Context? = null
        var finalArr: HashMap<String, ArrayList<QuestionTypeModel>> = HashMap()
        var q_grppos1 = 0
        var curr_index = 0

        fun setButton(ansid: String) {

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

        if (!DialogUtils.isNetworkConnected(this@NewTabQuestionActivity)) {
            Utils.ping(this@NewTabQuestionActivity, AppConstants.NETWORK_MSG)
        }

        DialogUtils.showDialog(this@NewTabQuestionActivity)

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

                    val hintdialog = Dialog(this@NewTabQuestionActivity)
                    hintdialog.setContentView(R.layout.hint_dialog)
                    hintdialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    hintdialog.setCanceledOnTouchOutside(false)

                    val hintWebview: WebView = hintdialog.findViewById(R.id.dialog_hint_wvHint)
                    val header: TextView = hintdialog.findViewById(R.id.dialog_hint_tvHeader)
                    val closeBtn: View = hintdialog.findViewById(R.id.dialog_hint_btnClose)

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
                            queTab_tvCurrHint.text = "$hintCount/$total_hint"
                            queTab_ivHint.visibility = View.VISIBLE
                        } else {
                            queTab_tvCurrHint.text = ""
                            queTab_ivHint.visibility = View.GONE
                        }

                        hintdialog.dismiss()
                    }

                    hintdialog.show()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.e("", t.toString())
                DialogUtils.dismissDialog()
            }
        })
    }

    var attemptDialog: Dialog? = null

    fun OpenAttemptDialog() {

        CommonWebCalls.callToken(
            this@NewTabQuestionActivity,
            "1",
            "",
            ActionIdData.C2200,
            ActionIdData.T2200
        )

        attemptDialog = Dialog(this@NewTabQuestionActivity)

        attemptDialog!!.setContentView(R.layout.dialog_que_attempt_report)
        attemptDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        attemptDialog!!.setCanceledOnTouchOutside(false)

        val btnCancel: TextView = attemptDialog!!.findViewById(R.id.attempt_btnClose)
        val btnOk: TextView = attemptDialog!!.findViewById(R.id.attempt_tvOK)
        val rvList: RecyclerView = attemptDialog!!.findViewById(R.id.attempt_rvList)

        if (!attemptDialog!!.isShowing) {

            rvList.layoutManager =
                LinearLayoutManager(
                    this@NewTabQuestionActivity,
                    LinearLayoutManager.VERTICAL,
                    false
                )

//            callAttempReport()

            continuetime = 0

            DialogUtils.showDialog(this@NewTabQuestionActivity)

            val apiService = WebClient.getClient().create(WebInterface::class.java)

            val call = apiService.attemptReport(studenttestid)
            call.enqueue(object : Callback<AttemptModel> {
                override fun onResponse(call: Call<AttemptModel>, response: Response<AttemptModel>) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.Status == "true") {

                        rvList.adapter =
                            QuestionAttemptAdapter(
                                this@NewTabQuestionActivity,
                                "test",
                                response.body()!!.data
                            )

                        attemptDialog!!.show()

                    }
                }

                override fun onFailure(call: Call<AttemptModel>, t: Throwable) {
                    Log.e("", t.toString())
                    DialogUtils.dismissDialog()
                }
            })

        } else {
            attemptDialog!!.dismiss()
        }

        btnCancel.setOnClickListener {

            if (queTab_tvTimer.text.toString().equals("00:00:00", true)) {

                getType("continue", 0, curr_index)

                DialogUtils.createConfirmDialog1(
                    this@NewTabQuestionActivity,
                    "Submit",
                    "Your test time is over",

                    DialogInterface.OnClickListener { dialog, which ->

                        callSubmitAPI()

                    }).show()
                attemptDialog!!.dismiss()

            } else {
                attemptDialog!!.dismiss()
            }
        }

        btnOk.setOnClickListener {

            stopTimer()

            var ansstr = ""

            for (i in 0 until ansArr.size) {
                ansstr = ansstr + ansArr[i].qid + "|" + ansArr[i].ansid + ","

            }

            Log.d("ansstr", ansstr)

            callSubmitAPI()
        }

//        fun callAttempReport() {
//        }
    }
}
