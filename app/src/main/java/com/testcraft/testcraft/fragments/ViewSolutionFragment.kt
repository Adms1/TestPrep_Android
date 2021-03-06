package com.testcraft.testcraft.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.webkit.WebView
import android.widget.Button
import android.widget.ExpandableListView
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.testcraft.testcraft.R
import com.testcraft.testcraft.adapter.ImageViewAdapter1.Companion.SolutionPageNumber
import com.testcraft.testcraft.adapter.SolutionAdapter
import com.testcraft.testcraft.adapter.SoutionSideMenuAdapter
import com.testcraft.testcraft.interfaces.FilterTypeSelectionInteface
import com.testcraft.testcraft.models.AnswerModel
import com.testcraft.testcraft.models.QuestionTypeModel
import com.testcraft.testcraft.retrofit.WebClient
import com.testcraft.testcraft.sectionmodule.NewQuestionResponse
import com.testcraft.testcraft.utils.*
import kotlinx.android.synthetic.main.activity_view_solution.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewSolutionFragment : Fragment(), FilterTypeSelectionInteface {

    var testid = ""
    var studenttestid = ""
    var hintData = ""

    var movies: ArrayList<NewQuestionResponse.QuestionList> = ArrayList()

    private var ansList: RecyclerView? = null
    private var imgQue: ImageView? = null
    internal var mDrawerToggle: ActionBarDrawerToggle? = null
    var childList = HashMap<String, ArrayList<String>>()

    var bundle: Bundle? = null

    var solution_que_number = 1
    var solution_testque = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.activity_view_solution, container, false)

    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//
//        setContentView(R.layout.activity_view_solution)

//        activity.setSupportActionBar(mToolbar)

//        context = activity!!

        CommonWebCalls.callToken(activity!!, "1", "", ActionIdData.C2400, ActionIdData.T2400)

        filterTypeSelectionInteface = this

        AppConstants.QUE_NUMBER1 = 0

        finalArr1 = HashMap()

        bundle = this.arguments

        testid = bundle!!.getString("testid")!!
        studenttestid = bundle!!.getString("studenttestid")!!
        solution_testque = bundle!!.getString("totalque")!!

        drawer_layout1.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        sectionList = ArrayList()
//        sectionList1 = ArrayList()

        imgQue = view.findViewById(R.id.solution_page_img_que_img)
        ansList = view.findViewById(R.id.solution_wv_question_list)
        nextButton = view.findViewById(R.id.solution_btnNext)
        sideList = view.findViewById(R.id.solution_expQueList)

        imgQue!!.isDrawingCacheEnabled = true

//        mDrawerToggle = ActionBarDrawerToggle(
//            activity, drawer_layout1, R.mipmap.tc_logo, // nav menu toggle icon
//            R.string.app_name, // nav drawer open - description for accessibility
//            R.string.app_name
//        )

        mDrawerToggle = ActionBarDrawerToggle(
            activity, drawer_layout1, null, // nav menu toggle icon
            R.string.app_name, // nav drawer open - description for accessibility
            R.string.app_name
        )

        drawer_layout1.setDrawerListener(mDrawerToggle)

        val dialog = Dialog(activity!!)

        solution_expQueList.setOnGroupClickListener { parent, v, groupPosition, id ->

            CommonWebCalls.callToken(activity!!, "1", "", ActionIdData.C2404, ActionIdData.T2404)

            if (movies[groupPosition].SectionInstruction != "" && !dialog.isShowing) {

                dialog.setContentView(R.layout.hint_dialog)
                dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.setCanceledOnTouchOutside(false)

                val hintWebview: WebView = dialog.findViewById(R.id.dialog_hint_wvHint)
                val header: TextView = dialog.findViewById(R.id.dialog_hint_tvHeader)
                val closeBtn: View = dialog.findViewById(R.id.dialog_hint_btnClose)

                header.text = movies[groupPosition].SectionName

                hintWebview.settings.javaScriptEnabled = true
//            hintWebview.loadDataWithBaseURL("", movies[solution_grppos1].SectionInstruction, "text/html", "UTF-8", "")
                hintWebview.loadDataWithBaseURL(
                    AppConstants.EXPHINT_IMAGE_BASE_URL,
                    "<html><body style='background-color:clear;'><p>" + movies[groupPosition].SectionInstruction + "</p></body></html>",
                    "text/html",
                    "UTF-8",
                    ""
                )

                closeBtn.setOnClickListener { dialog.dismiss() }

                dialog.show()

            }

            false

        }

        solution_ivReporttxt.setOnClickListener {

            CommonWebCalls.callToken(activity!!, "1", "", ActionIdData.C2401, ActionIdData.T2401)

            val dialog1 = Dialog(activity!!)
            dialog1.setContentView(R.layout.dialog_report_issue)
            dialog1.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog1.setCanceledOnTouchOutside(false)

            val close: TextView = dialog1.findViewById(R.id.dialog_report_tvClose)
            val queproblem: TextView = dialog1.findViewById(R.id.dialog_report_tvQueProblem)
            val ansproblem: TextView = dialog1.findViewById(R.id.dialog_report_tvAnsProblem)
            val hintexplanation: TextView = dialog1.findViewById(R.id.dialog_report_tvHintProblem)

            hintexplanation.text = getString(R.string.explanation_has_a_problem)

            queproblem.setOnClickListener {

                CommonWebCalls.callToken(
                    activity!!,
                    "1",
                    "",
                    ActionIdData.C2501,
                    ActionIdData.T2501
                )

                CommonWebCalls.callReportIssue(
                    activity!!,
                    "1",
                    queproblem.text.toString(),
                    movies[solution_grppos1].TestQuestion[curr_index1].QuestionID.toString()
                )

                dialog1.dismiss()
            }

            ansproblem.setOnClickListener {

                CommonWebCalls.callToken(
                    activity!!,
                    "1",
                    "",
                    ActionIdData.C2502,
                    ActionIdData.T2502
                )

                CommonWebCalls.callReportIssue(
                    activity!!,
                    "2",
                    ansproblem.text.toString(),
                    movies[solution_grppos1].TestQuestion[curr_index1].QuestionID.toString()
                )
                dialog1.dismiss()
            }

            hintexplanation.setOnClickListener {

                CommonWebCalls.callToken(
                    activity!!,
                    "1",
                    "",
                    ActionIdData.C2503,
                    ActionIdData.T2503
                )

                CommonWebCalls.callReportIssue(
                    activity!!,
                    "4",
                    hintexplanation.text.toString(),
                    movies[solution_grppos1].TestQuestion[curr_index1].QuestionID.toString()
                )
                dialog1.dismiss()
            }

            close.setOnClickListener {

                CommonWebCalls.callToken(
                    activity!!,
                    "1",
                    "",
                    ActionIdData.C2504,
                    ActionIdData.T2504
                )

                dialog1.dismiss()
            }

            dialog1.show()
        }

        solution_btnNextt.setOnClickListener {
            drawer_layout1.openDrawer(GravityCompat.END)
        }

        solution_btnNext.setOnClickListener {

            CommonWebCalls.callToken(activity!!, "1", "", ActionIdData.C2402, ActionIdData.T2402)

            if (!DialogUtils.isNetworkConnected(activity!!)) {
                val netdialog = Dialog(activity!!)
                netdialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                netdialog.setContentView(R.layout.dialog_network)
                netdialog.setCanceledOnTouchOutside(false)

                val btnRetry: TextView = netdialog.findViewById(R.id.network_btnRetry)

                btnRetry.setOnClickListener {
                    if (DialogUtils.isNetworkConnected(activity!!)) {
                        netdialog.dismiss()

                    }
                }

                netdialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                netdialog.setCanceledOnTouchOutside(false)
                netdialog.setCancelable(false)
                netdialog.show()
                DialogUtils.dismissDialog()
            } else {
                getNextQuestion1()
            }

        }

        dialog_hint_wvHint.settings.javaScriptEnabled = true

        solution_ivReview.setOnClickListener {

            //            dialog_hint_wvHint.visibility = View.VISIBLE


//            val dialog = Dialog(activity!!)
//            dialog.setContentView(R.layout.hint_dialog)
//            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            dialog.setCanceledOnTouchOutside(false)
//
//            val hintWebview: WebView = dialog.findViewById(R.id.dialog_hint_wvHint)
//
//            val closeBtn: View = dialog.findViewById(R.id.dialog_hint_btnClose)
//
//            hintWebview.settings.javaScriptEnabled = true
//            hintWebview.loadDataWithBaseURL("", hintData, "text/html", "UTF-8", "")
//
//            closeBtn.setOnClickListener { dialog.dismiss() }
//
//            dialog.show()
        }

        ansList!!.layoutManager =
            LinearLayoutManager(activity!!, LinearLayoutManager.VERTICAL, false)

        solution_expQueList.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->

            solution_grppos1 = groupPosition

            false

        }

//        solution_ivBack.setOnClickListener {
//
//            curr_index1 = 0
//            solution_grppos1 = 0
////            onBackPressed()
//
//        }

        if (testid != "") {
            callSolutionApi()
        }
    }

    fun callSolutionApi() {

        if (!DialogUtils.isNetworkConnected(activity!!)) {

            val netdialog = Dialog(activity!!)
            netdialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            netdialog.setContentView(R.layout.dialog_network)
            netdialog.setCanceledOnTouchOutside(false)

            val btnRetry: TextView = netdialog.findViewById(R.id.network_btnRetry)

            btnRetry.setOnClickListener {
                if (DialogUtils.isNetworkConnected(activity!!)) {
                    netdialog.dismiss()
                    callSolutionApi()
                }
            }

            netdialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            netdialog.setCanceledOnTouchOutside(false)
            netdialog.setCancelable(false)
            netdialog.show()
            DialogUtils.dismissDialog()

        }

        DialogUtils.showDialog(activity!!)

        val call = WebClient.buildService().getNewQuestions(testid, studenttestid)
        call.enqueue(object : Callback<NewQuestionResponse> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<NewQuestionResponse>,
                response: Response<NewQuestionResponse>
            ) {

                DialogUtils.dismissDialog()

                movies = response.body()!!.data

                hintData =
                    "<html><body style='background-color:clear;'><p align=center><font size=4px  align=center>" + "Explanation" + "</font></p><p>" + movies[0].TestQuestion[0].Explanation + "</p></body></html>"
//                explanationData = movies[0].TestQuestion[0].Explanation

                if (movies[0].TestQuestion[0].Explanation != "") {

                    dialog_hint_wvHint.visibility = View.VISIBLE

                    dialog_hint_wvHint.loadDataWithBaseURL(
                        AppConstants.EXPHINT_IMAGE_BASE_URL,
                        hintData,
                        "text/html",
                        "UTF-8",
                        ""
                    )

                } else {
                    dialog_hint_wvHint.visibility = View.GONE
                }

                solution_tvQMarks.text =
                    "Marks : " + movies[0].TestQuestion[0].ObtainMarks + "/" + movies[solution_grppos1].TestQuestion[curr_index1].Marks

                solution_tvTime.text = "Time : " + movies[0].TestQuestion[0].QuestionTime
                solution_tvTotal.text = "$solution_que_number/$solution_testque"

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

                    if (movies[0].TestQuestion[0].QuestionImage != "") {

                        var pagenumber1 = 0

                        for (i in 0 until movies.size) {
//
                            val sectionList1: ArrayList<QuestionTypeModel> = ArrayList()

                            for (j in 0 until movies[i].TestQuestion.size) {
                                val questionTypeModel = QuestionTypeModel()
                                questionTypeModel.qnumber = j

                                pagenumber1++

                                questionTypeModel.page_number = pagenumber1

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
                            finalArr1[movies[i].SectionName] = sectionList1

                        }

                        solution_expQueList.setAdapter(
                            SoutionSideMenuAdapter(
                                activity!!,
                                sectionList!!,
                                finalArr1,
                                filterTypeSelectionInteface!!,
                                "solution"
                            )
                        )
                    }

                    if (movies[0].TestQuestion[0].QuestionImage != "") {

                        Picasso.get()
                            .load(movies[0].TestQuestion[0].QuestionImage)
                            .transform(transform.getTransformation(imgQue!!))
                            .into(imgQue!!)

                        Log.d("imgcall", "Number of movies received: " + movies.size)

                        when {
                            movies[0].TestQuestion[0].IsCorrect.equals("true", true)  -> {
                                solution_ivAnsimg.setImageResource(R.drawable.wrong)
                            }
                            movies[0].TestQuestion[0].IsCorrect.equals("false", true) -> {
                                solution_ivAnsimg.setImageResource(R.drawable.correct)
                            }
                            else                                                      -> {
                                solution_ivAnsimg.setImageResource(0)

                            }
                        }

                        solution_tvYranswer.text =
                            "Your Answer : " + movies[0].TestQuestion[0].YourAnswer
                        solution_tvCorrectanswer.text =
                            "Correct Answer : " + movies[0].TestQuestion[0].SystemAnswer

                    }

                    if (movies[0].TestQuestion[0].QuestionTypeID == 1) {
                        ansList!!.layoutManager =
                            LinearLayoutManager(activity!!, LinearLayoutManager.VERTICAL, false)

                        ansList!!.adapter = SolutionAdapter(
                            activity!!,
                            movies[0].TestQuestion[0].StudentTestQuestionMCQ,
                            imgQue!!.width, 1
                        )

                    } else if (movies[0].TestQuestion[0].QuestionTypeID == 2 || movies[0].TestQuestion[0].QuestionTypeID == 8) {

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
                            LinearLayoutManager(activity!!, LinearLayoutManager.VERTICAL, false)

                        ansList!!.adapter = SolutionAdapter(
                            activity!!,
                            movies[0].TestQuestion[0].StudentTestQuestionMCQ,
                            imgQue!!.width, 7
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

    @SuppressLint("SetTextI18n")
    override fun getType(itype: String, p010: Int, p10: Int) {

        curr_index1 = p10

        Log.d("solution_current_index", "" + curr_index1)

//        dialog_hint_wvHint.visibility = View.GONE

        drawer_layout1.closeDrawer(GravityCompat.END)

        if (finalArr1[sectionList!![solution_grppos1]]!!.size > curr_index1) {

            hintData =
                "<html><body style='background-color:clear;'><p align=center><font size=4px  align=center>" + "Explanation" + "</font></p><p>" + movies[solution_grppos1].TestQuestion[curr_index1].Explanation + "</p></body></html>"

            if (movies[solution_grppos1].TestQuestion[curr_index1].Explanation != "") {

                dialog_hint_wvHint.visibility = View.VISIBLE

                dialog_hint_wvHint.loadDataWithBaseURL(
                    AppConstants.EXPHINT_IMAGE_BASE_URL,
                    hintData,
                    "text/html",
                    "UTF-8",
                    ""
                )
            } else {
                dialog_hint_wvHint.visibility = View.GONE
            }

            solution_tvQMarks.text =
                "Marks : " + movies[solution_grppos1].TestQuestion[curr_index1].ObtainMarks + "/" + movies[solution_grppos1].TestQuestion[curr_index1].Marks
            solution_tvTime.text =
                "Time : " + movies[solution_grppos1].TestQuestion[curr_index1].QuestionTime

            if (itype == "adapter") {

                solution_que_number = SolutionPageNumber()

            }

            if (solution_que_number <= solution_testque.toInt()) {
                solution_tvTotal.text = "$solution_que_number/$solution_testque"
            }

            if (movies[solution_grppos1].TestQuestion[curr_index1].QuestionImage != "") {

                Picasso.get()
                    .load(movies[solution_grppos1].TestQuestion[curr_index1].QuestionImage)
                    .transform(transform.getTransformation(imgQue!!))
                    .into(imgQue)

                var isAswerIcon = false

                if (movies[solution_grppos1].TestQuestion[curr_index1].ObtainMarks != "") {

                    isAswerIcon =
                        !(movies[solution_grppos1].TestQuestion[curr_index1].QuestionTypeID == 7 && movies[solution_grppos1].TestQuestion[curr_index1].IsCorrect.equals(
                            "false",
                            true
                        ) && movies[solution_grppos1].TestQuestion[curr_index1].ObtainMarks.toFloat() > 0)
                }

                if (!isAswerIcon) {
                    solution_ivAnsimg.setImageResource(0)

                } else {
                    when {
                        movies[solution_grppos1].TestQuestion[curr_index1].IsCorrect.equals(
                            "true",
                            true
                        ) -> {
                            solution_ivAnsimg.setImageResource(R.drawable.wrong)
                        }
                        movies[solution_grppos1].TestQuestion[curr_index1].IsCorrect.equals(
                            "false",
                            true
                        ) -> {
                            solution_ivAnsimg.setImageResource(R.drawable.correct)
                        }
                        else -> {
                            solution_ivAnsimg.setImageResource(0)

                        }
                    }
                }

                solution_tvYranswer.text =
                    "Your Answer : " + movies[solution_grppos1].TestQuestion[curr_index1].YourAnswer
                solution_tvCorrectanswer.text =
                    "Correct Answer : " + movies[solution_grppos1].TestQuestion[curr_index1].SystemAnswer

                ansList!!.layoutManager =
                    LinearLayoutManager(activity!!, LinearLayoutManager.VERTICAL, false)

                when (movies[solution_grppos1].TestQuestion[curr_index1].QuestionTypeID) {

                    1 -> {

                        solution_rbTruefalse.visibility = View.GONE
                        solution_tvFillBlanks.visibility = View.GONE
                        ansList!!.visibility = View.VISIBLE

                        ansList!!.adapter = SolutionAdapter(
                            activity!!,
                            movies[solution_grppos1].TestQuestion[curr_index1].StudentTestQuestionMCQ,
                            imgQue!!.width, 1
                        )
                    }
                    2 -> {

                        solution_tvFillBlanks.visibility = View.VISIBLE
                        ansList!!.visibility = View.GONE
                        solution_rbTruefalse.visibility = View.GONE

                        solution_tvFillBlanks.text =
                            movies[solution_grppos1].TestQuestion[curr_index1].Answer

                    }
                    4 -> {
                        solution_rbTruefalse.visibility = View.VISIBLE
                        solution_tvFillBlanks.visibility = View.GONE
                        ansList!!.visibility = View.GONE

                        setTrueFalse(solution_grppos1, curr_index1)

                    }
                    7 -> {

                        solution_rbTruefalse.visibility = View.GONE
                        solution_tvFillBlanks.visibility = View.GONE
                        ansList!!.visibility = View.VISIBLE

                        ansList!!.layoutManager =
                            LinearLayoutManager(activity!!, LinearLayoutManager.VERTICAL, false)

                        ansList!!.adapter = SolutionAdapter(
                            activity!!,
                            movies[solution_grppos1].TestQuestion[curr_index1].StudentTestQuestionMCQ,
                            imgQue!!.width, 7
                        )
                    }
                    8 -> {

                        solution_tvFillBlanks.visibility = View.VISIBLE
                        ansList!!.visibility = View.GONE
                        solution_rbTruefalse.visibility = View.GONE

                        solution_tvFillBlanks.text =
                            movies[solution_grppos1].TestQuestion[curr_index1].Answer

                    }
                }

                sideList!!.setAdapter(
                    SoutionSideMenuAdapter(
                        context!!,
                        sectionList!!,
                        finalArr1,
                        filterTypeSelectionInteface!!,
                        "solution"
                    )
                )
            }

            if (solution_que_number >= solution_testque.toInt()) {
                solution_btnNext.visibility = View.GONE
            } else {
                solution_btnNext.visibility = View.VISIBLE
            }

//            if ((finalArr1.size - 1) > solution_grppos1) {
//
//                solution_btnNext.visibility = View.VISIBLE
//
//            } else {
//
//                if (finalArr1[sectionList!![solution_grppos1]]!!.size == curr_index1) {
//                    solution_btnNext.visibility = View.GONE
//                } else {
//                    solution_btnNext.visibility = View.VISIBLE
//                }
//            }

        }
//        else {
//            solution_btnNext.visibility = View.GONE
//        }
    }

    fun getNextQuestion1() {

//        if ((finalArr1.size - 1) > solution_grppos1) {
//
//            if (finalArr1[sectionList!![solution_grppos1]]!!.size == curr_index1 && p1 == (curr_index1 - 1)) {
//                solution_grppos1 += 1
//                curr_index1 = 0
//                p1 = 0
//                finalArr1[sectionList!![solution_grppos1]]!![p1].type =
//                    1
//            }
//
//            sideList!!.setAdapter(
//                SoutionSideMenuAdapter(
//                    context!!,
//                    sectionList!!,
//                    finalArr1,
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
//                    finalArr1,
//                    filterTypeSelectionInteface!!,
//                    "solution"
//                )
//            )
//        }
//
//        Log.d("que_number", "" + AppConstants.QUE_NUMBER1)

        solution_que_number += 1

        if (curr_index1 < finalArr1[sectionList!![solution_grppos1]]!!.size - 1) {

            curr_index1 += 1

        } else {

            if ((finalArr1.size - 1) > solution_grppos1) {

                if ((finalArr1[sectionList!![solution_grppos1]]!!.size - 1) == curr_index1) {
                    solution_grppos1 += 1
                    curr_index1 = 0
//                    finalArr1[sectionList!![solution_grppos1]]!![curr_index1].type = 1

                }
            }
        }

//                if ((finalArr1.size - 1) == solution_grppos1) {
//                    if ((finalArr1[sectionList!![solution_grppos1]]!!.size - 1) == (com.testprep.sectionmodule.Companion.curr_index1+1)) {
//                        solution_btnNext.text = "Submit Test"
//                    }
//                }

        if (!DialogUtils.isNetworkConnected(activity!!)) {

            val netdialog = Dialog(activity!!)
            netdialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            netdialog.setContentView(R.layout.dialog_network)
            netdialog.setCanceledOnTouchOutside(false)

            val btnRetry: TextView = netdialog.findViewById(R.id.network_btnRetry)

            btnRetry.setOnClickListener {
                if (DialogUtils.isNetworkConnected(activity!!)) {
                    netdialog.dismiss()
                    getType("solution", solution_grppos1, curr_index1)
                }
            }

            netdialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            netdialog.setCanceledOnTouchOutside(false)
            netdialog.setCancelable(false)
            netdialog.show()
            DialogUtils.dismissDialog()

        } else {
            getType("solution", solution_grppos1, curr_index1)
        }
    }

    companion object {

        var nextButton: Button? = null
        var sideList: ExpandableListView? = null
        var sectionList: ArrayList<String>? = null
        var filterTypeSelectionInteface: FilterTypeSelectionInteface? = null
        var context: Context? = null
        var finalArr1: HashMap<String, ArrayList<QuestionTypeModel>> = HashMap()
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
