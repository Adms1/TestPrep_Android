package com.testprep.activity

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.ActionBarDrawerToggle
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Gravity
import android.view.View
import android.webkit.WebView
import android.widget.Button
import android.widget.ExpandableListView
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.testprep.R
import com.testprep.adapter.SoutionSideMenuAdapter
import com.testprep.interfaces.FilterTypeSelectionInteface
import com.testprep.models.AnswerModel
import com.testprep.models.QuestionTypeModel
import com.testprep.old.adapter.SolutionAdapter
import com.testprep.retrofit.WebClient
import com.testprep.retrofit.WebInterface
import com.testprep.sectionmodule.NewQuestionResponse
import com.testprep.utils.AppConstants
import com.testprep.utils.DialogUtils
import com.testprep.utils.Utils
import kotlinx.android.synthetic.main.activity_view_solution.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class ViewSolutionActivity : AppCompatActivity(), FilterTypeSelectionInteface {

    var mToolbar: Toolbar? = null

    var testid = ""
    var studenttestid = ""
    var hintData = ""
    var explanationData = ""

    var movies: ArrayList<NewQuestionResponse.QuestionList> = ArrayList()

    private var ansList: RecyclerView? = null
    private var imgQue: ImageView? = null
    internal var mDrawerToggle: ActionBarDrawerToggle? = null
    var childList = HashMap<String, ArrayList<String>>()

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setContentView(R.layout.activity_view_solution)

        setSupportActionBar(mToolbar)

        context = this@ViewSolutionActivity

        filterTypeSelectionInteface = this

        AppConstants.QUE_NUMBER1 = 0

        testid = intent.getStringExtra("testid")
        studenttestid = intent.getStringExtra("studenttestid")

        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        sectionList = ArrayList()
//        sectionList1 = ArrayList()

        imgQue = findViewById(R.id.solution_page_img_que_img)
        ansList = findViewById(R.id.solution_wv_question_list)
        nextButton = findViewById(R.id.solution_btnNext)
        sideList = findViewById(R.id.solution_expQueList)

        imgQue!!.isDrawingCacheEnabled = true

        mDrawerToggle = ActionBarDrawerToggle(
            this, drawer_layout, R.mipmap.tc_logo, // nav menu toggle icon
            R.string.app_name, // nav drawer open - description for accessibility
            R.string.app_name
        )

        drawer_layout.setDrawerListener(mDrawerToggle)

        solution_btnNextt.setOnClickListener {
            drawer_layout.openDrawer(Gravity.END)
        }

        solution_btnNext.setOnClickListener {

            getNextQuestion1()

        }

        solution_ivReview.setOnClickListener {

            val dialog = Dialog(this@ViewSolutionActivity)
            dialog.setContentView(R.layout.hint_dialog)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCanceledOnTouchOutside(false)

            val hintWebview: WebView = dialog.findViewById(R.id.dialog_hint_wvHint)

            val closeBtn: View = dialog.findViewById(R.id.dialog_hint_btnClose)

            hintWebview.settings.javaScriptEnabled = true
            hintWebview.loadDataWithBaseURL("", hintData, "text/html", "UTF-8", "")

            closeBtn.setOnClickListener { dialog.dismiss() }

            dialog.show()
        }

        ansList!!.layoutManager = LinearLayoutManager(this@ViewSolutionActivity, LinearLayoutManager.VERTICAL, false)

        solution_expQueList.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->

            solution_grppos1 = groupPosition

            false

        }

        solution_ivBack.setOnClickListener {

            curr_index1 = 0
            solution_grppos1 = 0
            onBackPressed()

        }

        if (testid != "") {
            callSolutionApi()
        }
    }

    fun callSolutionApi() {

        if (!DialogUtils.isNetworkConnected(this@ViewSolutionActivity)) {
            Utils.ping(this@ViewSolutionActivity, "Connetion not available")
        }

        DialogUtils.showDialog(this@ViewSolutionActivity)

        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getNewQuestions(testid, studenttestid)
        call.enqueue(object : Callback<NewQuestionResponse> {
            override fun onResponse(call: Call<NewQuestionResponse>, response: Response<NewQuestionResponse>) {

                DialogUtils.dismissDialog()

                movies = response.body()!!.data

                hintData =
                    "<html><body style='background-color:clear;'><p align=center><font size=4  align=center><b>" + "Explanation" + "</b></font></p><p><font size=2>" + movies[0].TestQuestion[0].Explanation + "</font></p></body></html>"
//                explanationData = movies[0].TestQuestion[0].Explanation

                if (movies.size > 0) {

                    Log.d("header", "" + sectionList)
                    Log.d("child", "" + childList)

                    Log.d("qid", "" + movies[0].SectionID)

                    for (i in 0 until movies.size) {
                        val ansmodel = AnswerModel()
//                        ansmodel.qid = movies[i].SectionID
                        ansmodel.ansid = movies[i].SectionName
                        sectionList!!.add(movies[i].SectionName)
                    }

                    if ("http://content.testcraft.co.in/question/" + movies[0].TestQuestion[0].QuestionImage != "") {

                        for (i in 0 until movies.size) {
//
                            var sectionList1: ArrayList<QuestionTypeModel> = ArrayList()

                            for (j in 0 until movies[i].TestQuestion.size) {
                                val questionTypeModel = QuestionTypeModel()
                                questionTypeModel.qnumber = j

                                if (movies[i].TestQuestion[j].Answer != "") {

                                    if (movies[i].TestQuestion[j].IsCorrect.equals("true", true)) {
                                        questionTypeModel.type = 2
                                    } else {
                                        questionTypeModel.type = 3
                                    }

                                } else {
                                    questionTypeModel.type = 5
                                }

                                sectionList1.add(questionTypeModel)

                            }
                            finalArr[movies[i].SectionName] = sectionList1
//
                        }

                        solution_expQueList.setAdapter(
                            SoutionSideMenuAdapter(
                                this@ViewSolutionActivity,
                                sectionList!!,
                                finalArr,
                                filterTypeSelectionInteface!!,
                                "solution"
                            )
                        )
                    }

                    if ("http://content.testcraft.co.in/question/" + movies[0].TestQuestion[0].QuestionImage != "") {

                        Picasso.get()
                            .load("http://content.testcraft.co.in/question/" + movies[0].TestQuestion[0].QuestionImage)
                            .into(solution_page_img_que_img)

                        Log.d("imgcall", "Number of movies received: " + movies.size)
                    }

                    if (movies[0].TestQuestion[0].QuestionTypeID == 1) {
                        ansList!!.layoutManager =
                            LinearLayoutManager(this@ViewSolutionActivity, LinearLayoutManager.VERTICAL, false)

                        ansList!!.adapter = SolutionAdapter(
                            this@ViewSolutionActivity,
                            movies[0].TestQuestion[0].StudentTestQuestionMCQ,
                            solution_page_img_que_img.width, 1
                        )

                    } else if (movies[0].TestQuestion[0].QuestionTypeID == 2) {

                        solution_tvFillBlanks.visibility = View.VISIBLE
                        ansList!!.visibility = View.GONE
                        solution_rbTruefalse.visibility = View.GONE

                        solution_tvFillBlanks.text = movies[0].TestQuestion[0].Answer

                    } else if (movies[0].TestQuestion[0].QuestionTypeID == 4) {
                        solution_rbTruefalse.visibility = View.VISIBLE
                        solution_tvFillBlanks.visibility = View.GONE
                        ansList!!.visibility = View.GONE

                        setTrueFalse(0, 0)

                    } else if (movies[0].TestQuestion[0].QuestionTypeID == 7) {
                        ansList!!.layoutManager =
                            LinearLayoutManager(this@ViewSolutionActivity, LinearLayoutManager.VERTICAL, false)

                        ansList!!.adapter = SolutionAdapter(
                            this@ViewSolutionActivity,
                            movies[0].TestQuestion[0].StudentTestQuestionMCQ,
                            solution_page_img_que_img.width, 7
                        )
                    }

                }
            }

            override fun onFailure(call: Call<NewQuestionResponse>, t: Throwable) {
                // Log error here since request failed
                Log.e("question res", t.toString())
                DialogUtils.dismissDialog()
            }
        })
    }

    override fun getType(itype: String, p010: Int, p10: Int) {

        curr_index1 = p10

        drawer_layout.closeDrawer(Gravity.END)

        hintData =
            "<html><body style='background-color:clear;'><p align=center><font size=4  align=center><b>" + "Explanation" + "</b></font></p><p><font size=2>" + movies[solution_grppos1].TestQuestion[curr_index1].Explanation + "</font></p></body></html>"

        if ("http://content.testcraft.co.in/question/" + movies[solution_grppos1].TestQuestion[curr_index1].QuestionImage != "") {

            Picasso.get()
                .load("http://content.testcraft.co.in/question/" + movies[solution_grppos1].TestQuestion[curr_index1].QuestionImage)
                .into(imgQue)

            ansList!!.layoutManager =
                LinearLayoutManager(this@ViewSolutionActivity, LinearLayoutManager.VERTICAL, false)

            when (movies[solution_grppos1].TestQuestion[curr_index1].QuestionTypeID) {
                1 -> ansList!!.adapter = SolutionAdapter(
                    this@ViewSolutionActivity,
                    movies[solution_grppos1].TestQuestion[curr_index1].StudentTestQuestionMCQ,
                    solution_page_img_que_img.width, 1
                )
                2 -> {

                    solution_tvFillBlanks.visibility = View.VISIBLE
                    ansList!!.visibility = View.GONE
                    solution_rbTruefalse.visibility = View.GONE

                    solution_tvFillBlanks.text = movies[solution_grppos1].TestQuestion[curr_index1].Answer

                }
                4 -> {
                    solution_rbTruefalse.visibility = View.VISIBLE
                    solution_tvFillBlanks.visibility = View.GONE
                    ansList!!.visibility = View.GONE

                    setTrueFalse(solution_grppos1, curr_index1)

                }
                7 -> {
                    ansList!!.layoutManager =
                        LinearLayoutManager(this@ViewSolutionActivity, LinearLayoutManager.VERTICAL, false)

                    ansList!!.adapter = SolutionAdapter(
                        this@ViewSolutionActivity,
                        movies[solution_grppos1].TestQuestion[curr_index1].StudentTestQuestionMCQ,
                        solution_page_img_que_img.width, 7
                    )
                }
            }

            sideList!!.setAdapter(
                SoutionSideMenuAdapter(
                    context!!,
                    sectionList!!,
                    finalArr,
                    filterTypeSelectionInteface!!,
                    "solution"
                )
            )

        }

        solution_btnNext.visibility = View.VISIBLE
    }

    fun getNextQuestion1() {

//        if ((finalArr.size - 1) > solution_grppos1) {
//
//            if (finalArr[sectionList!![solution_grppos1]]!!.size == curr_index1 && p1 == (curr_index1 - 1)) {
//                solution_grppos1 += 1
//                curr_index1 = 0
//                p1 = 0
//                finalArr[sectionList!![solution_grppos1]]!![p1].type =
//                    1
//            }
//
//            sideList!!.setAdapter(
//                SoutionSideMenuAdapter(
//                    context!!,
//                    sectionList!!,
//                    finalArr,
//                    filterTypeSelectionInteface!!,
//                    "solution"
//                )
//            )
//        }
//
//
//        if ((movies[solution_grppos1].TestQuestion.size - 1) > AppConstants.QUE_NUMBER1) {
//            AppConstants.QUE_NUMBER1 = AppConstants.QUE_NUMBER1 + 1
//
//            getType("solution", solution_grppos1, AppConstants.QUE_NUMBER1)
//
//            sideList!!.setAdapter(
//                SoutionSideMenuAdapter(
//                    context!!,
//                    sectionList!!,
//                    finalArr,
//                    filterTypeSelectionInteface!!,
//                    "solution"
//                )
//            )
//        }
//
//        Log.d("que_number", "" + AppConstants.QUE_NUMBER1)

        if (curr_index1 <= finalArr[sectionList!![solution_grppos1]]!!.size - 1) {
            curr_index1 += 1
        }

//                if ((finalArr.size - 1) == solution_grppos1) {
//                    if ((finalArr[sectionList!![solution_grppos1]]!!.size - 1) == (com.testprep.sectionmodule.Companion.curr_index1+1)) {
//                        solution_btnNext.text = "Submit Test"
//                    }
//                }

        if ((finalArr.size - 1) > solution_grppos1) {

            if (finalArr[sectionList!![solution_grppos1]]!!.size == curr_index1) {
                solution_grppos1 += 1
                curr_index1 = 0
                finalArr[sectionList!![solution_grppos1]]!![curr_index1].type = 1
            }

        }

        getType("solution", solution_grppos1, curr_index1)
    }

    companion object {

        var nextButton: Button? = null
        var sideList: ExpandableListView? = null
        var sectionList: ArrayList<String>? = null
        var sectionList1: ArrayList<QuestionTypeModel>? = null
        var filterTypeSelectionInteface: FilterTypeSelectionInteface? = null
        var context: Context? = null
        var finalArr: HashMap<String, ArrayList<QuestionTypeModel>> = HashMap()
        var solution_grppos1 = 0
        var curr_index1 = 0

    }

    fun setTrueFalse(gr: Int, ch: Int) {

        if (movies[gr].TestQuestion[ch].Answer != "") {

            if (movies[gr].TestQuestion[ch].CorrectAnswer.equals("true", true)) {
                if (movies[gr].TestQuestion[ch].Answer == "1") {
                    solution_rbTrue.setImageResource(R.drawable.wrong)
                    solution_rbFalse.setImageResource(R.drawable.grey_round)
                } else {
                    solution_rbTrue.setImageResource(R.drawable.wrong)
                    solution_rbFalse.setImageResource(R.drawable.correct)
                }
            } else {
                if (movies[gr].TestQuestion[ch].Answer == "1") {
                    solution_rbTrue.setImageResource(R.drawable.correct)
                    solution_rbFalse.setImageResource(R.drawable.wrong)
                } else {
                    solution_rbTrue.setImageResource(R.drawable.grey_round)
                    solution_rbFalse.setImageResource(R.drawable.wrong)
                }
            }

        } else {
            if (movies[gr].TestQuestion[ch].CorrectAnswer.equals("true", true)) {
                solution_rbTrue.setImageResource(R.drawable.wrong)
                solution_rbFalse.setImageResource(R.drawable.grey_round)
            } else {
                solution_rbTrue.setImageResource(R.drawable.grey_round)
                solution_rbFalse.setImageResource(R.drawable.wrong)
            }
        }

    }


}
