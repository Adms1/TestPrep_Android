//package com.testprep.activity
//
//import android.app.Dialog
//import android.content.Context
//import android.graphics.Color
//import android.graphics.drawable.ColorDrawable
//import android.os.Bundle
//import android.support.v4.app.ActionBarDrawerToggle
//import android.support.v4.widget.DrawerLayout
//import android.support.v7.app.AppCompatActivity
//import android.support.v7.widget.LinearLayoutManager
//import android.support.v7.widget.RecyclerView
//import android.support.v7.widget.Toolbar
//import android.util.Log
//import android.view.Gravity
//import android.view.View
//import android.webkit.WebView
//import android.widget.Button
//import android.widget.ImageView
//import com.squareup.picasso.Picasso
//import com.testprep.R
//import com.testprep.adapter.QuestionListSideMenuAdapter
//import com.testprep.interfaces.FilterTypeSelectionInteface
//import com.testprep.models.QuestionTypeModel
//import com.testprep.old.adapter.SolutionAdapter
//import com.testprep.old.models.QuestionResponse
//import com.testprep.retrofit.WebClient
//import com.testprep.retrofit.WebInterface
//import com.testprep.utils.AppConstants
//import com.testprep.utils.DialogUtils
//import com.testprep.utils.Utils
//import kotlinx.android.synthetic.main.activity_view_solution.*
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
//
//class ViewSolutionActivity : AppCompatActivity(), FilterTypeSelectionInteface {
//
//    var mToolbar: Toolbar? = null
//    var movies: ArrayList<QuestionResponse.QuestionList> = ArrayList()
//
//    var testid = ""
//    var studenttestid = ""
//    var hintData = ""
//    var explanationData = ""
//
//    private var ansList: RecyclerView? = null
//    private var imgQue: ImageView? = null
//    internal var mDrawerToggle: ActionBarDrawerToggle? = null
//    var childList = HashMap<String, ArrayList<String>>()
//
//    override fun attachBaseContext(newBase: Context?) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//
//        setContentView(R.layout.activity_view_solution)
//
//        setSupportActionBar(mToolbar)
//
//        context = this@ViewSolutionActivity
//
//        filterTypeSelectionInteface = this
//
//        AppConstants.QUE_NUMBER1 = 0
//
//        testid = intent.getStringExtra("testid")
//        studenttestid = intent.getStringExtra("studenttestid")
//
//        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
//
//        sectionList = ArrayList()
//        sectionList1 = ArrayList()
//
//        imgQue = findViewById(R.id.solution_page_img_que_img)
//        ansList = findViewById(R.id.solution_wv_question_list)
//        nextButton = findViewById(R.id.solution_btnNext)
//        sideList = findViewById(R.id.solution_expQueList)
//
//        imgQue!!.isDrawingCacheEnabled = true
//
//        mDrawerToggle = ActionBarDrawerToggle(
//            this, drawer_layout, R.mipmap.tc_logo, // nav menu toggle icon
//            R.string.app_name, // nav drawer open - description for accessibility
//            R.string.app_name
//        )
//
//        drawer_layout.setDrawerListener(mDrawerToggle)
//
//        solution_btnNextt.setOnClickListener {
//            drawer_layout.openDrawer(Gravity.END)
//        }
//
//        solution_btnNext.setOnClickListener {
//
//            getNextQuestion1()
//
//        }
//
//        solution_ivReview.setOnClickListener {
//
//            val dialog = Dialog(this@ViewSolutionActivity)
//            dialog.setContentView(R.layout.hint_dialog)
//            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            dialog.setCanceledOnTouchOutside(false)
//
////            var hintHeader: TextView = dialog.findViewById(R.id.dialog_hint_tvHint)
//            val hintWebview: WebView = dialog.findViewById(R.id.dialog_hint_wvHint)
////            val hintExplanation: TextView = dialog.findViewById(R.id.dialog_hint_tvExplanation)
////            val explanationWebview: WebView = dialog.findViewById(R.id.dialog_hint_wvExplanation)
////            val line1: View = dialog.findViewById(R.id.dialog_hint_viewLine1)
//            val closeBtn: View = dialog.findViewById(R.id.dialog_hint_btnClose)
//
//            hintWebview.settings.javaScriptEnabled = true
//            hintWebview.loadDataWithBaseURL("", hintData, "text/html", "UTF-8", "")
//
////            ex.settings.javaScriptEnabled = true
////            hintWebview.loadDataWithBaseURL("", explanationData, "text/html", "UTF-8", "")
//
//            closeBtn.setOnClickListener { dialog.dismiss() }
//
//            dialog.show()
//        }
//
//        ansList!!.layoutManager = LinearLayoutManager(this@ViewSolutionActivity, LinearLayoutManager.VERTICAL, false)
//
//        solution_ivBack.setOnClickListener { onBackPressed() }
//
//        if (testid != "") {
//            callSolutionApi()
//        }
//    }
//
//    fun callSolutionApi() {
//
//        if (!DialogUtils.isNetworkConnected(this@ViewSolutionActivity)) {
//            Utils.ping(this@ViewSolutionActivity, "Connetion not available")
//        }
//
//        DialogUtils.showDialog(this@ViewSolutionActivity)
//
//        val apiService = WebClient.getClient().create(WebInterface::class.java)
//
//        val call = apiService.getSolution(testid, studenttestid)
//        call.enqueue(object : Callback<QuestionResponse> {
//            override fun onResponse(call: Call<QuestionResponse>, response: Response<QuestionResponse>) {
//
//                DialogUtils.dismissDialog()
//
//                movies = response.body()!!.data
//
//                hintData =
//                    "<html><body style='background-color:clear;'><p align=center><font size=4><b>" + "Hint" + "</b></font></p><p><font size=2>" + movies[0].Hint + "</font></p><br><p align=center><font size=4  align=center><b>" + "Explanation" + "</b></font></p><p><font size=2>" + movies[0].Explanation + "</font></p></body></html>"
//                explanationData = movies[0].Explanation
//
//                if (movies.size > 0) {
//                    sectionList!!.add("Section 1")
//                    for (i in 0 until movies.size) {
//                        val questionTypeModel = QuestionTypeModel()
//                        questionTypeModel.qnumber = i
//
////                        for (j in 0 until movies[i].StudentTestAnswerMCQ.size) {
////                            if (movies[i].StudentTestAnswerMCQ[j].IsCorrectAnswer && movies[i].StudentTestAnswerMCQ[j].StudentAnswer) {
////                                questionTypeModel.type = 2
////                                break
////                            } else {
////                                questionTypeModel.type = 3
////                            }
//
//                        when {
//                            movies[i].QuestionAns == "1" -> questionTypeModel.type = 2
//                            movies[i].QuestionAns == "0" -> questionTypeModel.type = 3
//                            else -> questionTypeModel.type = 5
//                        }
////                        }
//
//                        sectionList1!!.add(questionTypeModel)
//
//                    }
//
//                    Log.d("header", "" + sectionList)
//                    Log.d("child", "" + childList)
//
//                    solution_expQueList.layoutManager =
//                        LinearLayoutManager(this@ViewSolutionActivity, LinearLayoutManager.VERTICAL, false)
//
//                    solution_expQueList.adapter =
//                        QuestionListSideMenuAdapter(
//                            this@ViewSolutionActivity,
//                            sectionList!!,
//                            sectionList1!!,
//                            filterTypeSelectionInteface!!,
//                            "solution"
//                        )
//                }
//
//                if ("http://content.testcraft.co.in/question/" + movies[0].QuestionImage != "") {
//
//                    Picasso.get().load("http://content.testcraft.co.in/question/" + movies[0].QuestionImage)
//                        .into(solution_page_img_que_img)
//
//                    Log.d("imgcall", "Number of movies received: " + movies.size)
//                }
//
//                if (movies[0].QuestionTypeID == 1) {
//                    ansList!!.layoutManager =
//                        LinearLayoutManager(this@ViewSolutionActivity, LinearLayoutManager.VERTICAL, false)
//
//                    ansList!!.adapter = SolutionAdapter(
//                        this@ViewSolutionActivity,
//                        movies[0].StudentTestQuestionMCQ,
//                        solution_page_img_que_img.width, 1
//                    )
//
//                } else if (movies[0].QuestionTypeID == 2) {
//
//                    solution_tvFillBlanks.visibility = View.VISIBLE
//                    ansList!!.visibility = View.GONE
//                    solution_rbTruefalse.visibility = View.GONE
//
//                    solution_tvFillBlanks.text = movies[0].Answer
//
//                } else if (movies[0].QuestionTypeID == 4) {
//                    solution_rbTruefalse.visibility = View.VISIBLE
//                    solution_tvFillBlanks.visibility = View.GONE
//                    ansList!!.visibility = View.GONE
//
//                    when {
//                        movies[0].CorrectAnswer != "True" -> {
//                            solution_rbTrue.setImageResource(R.drawable.wrong)
//                        }
//                        movies[0].CorrectAnswer != "False" -> {
//                            solution_rbFalse.setImageResource(R.drawable.wrong)
//                        }
//                        else -> {
//                            solution_rbTrue.setImageResource(R.drawable.grey_round)
//                            solution_rbFalse.setImageResource(R.drawable.grey_round)
//                        }
//                    }
//
//                } else if (movies[0].QuestionTypeID == 7) {
//                    ansList!!.layoutManager =
//                        LinearLayoutManager(this@ViewSolutionActivity, LinearLayoutManager.VERTICAL, false)
//
//                    ansList!!.adapter = SolutionAdapter(
//                        this@ViewSolutionActivity,
//                        movies[0].StudentTestAnswerMCQ,
//                        solution_page_img_que_img.width, 7
//                    )
//
//                }
//
//            }
//
//            override fun onFailure(call: Call<QuestionResponse>, t: Throwable) {
//                // Log error here since request failed
//                Log.e("question res", t.toString())
//                DialogUtils.dismissDialog()
//            }
//        })
//    }
//
//    override fun getType(itype: String, p0: Int) {
//
//        drawer_layout.closeDrawer(Gravity.END)
//
//        hintData =
//            "<html><body style='background-color:clear;'><p align=center><font size=4><b>" + "Hint" + "</b></font></p><p><font size=2>" + movies[p0].Hint + "</font></p><br><p align=center><font size=4  align=center><b>" + "Explanation" + "</b></font></p><p><font size=2>" + movies[p0].Explanation + "</font></p></body></html>"
//
//        explanationData = movies[p0].Explanation
//
//        if ("http://content.testcraft.co.in/question/" + movies[p0].QuestionImage != "") {
//
//            Picasso.get().load("http://content.testcraft.co.in/question/" + movies[p0].QuestionImage)
//                .into(imgQue)
//
//            ansList!!.layoutManager =
//                LinearLayoutManager(this@ViewSolutionActivity, LinearLayoutManager.VERTICAL, false)
//
//            if (movies[p0].QuestionTypeID == 1) {
//                ansList!!.adapter = SolutionAdapter(
//                    this@ViewSolutionActivity,
//                    movies[p0].StudentTestQuestionMCQ,
//                    solution_page_img_que_img.width, 1
//                )
//            } else if (movies[p0].QuestionTypeID == 2) {
//
//                solution_tvFillBlanks.visibility = View.VISIBLE
//                ansList!!.visibility = View.GONE
//                solution_rbTruefalse.visibility = View.GONE
//
//                solution_tvFillBlanks.text = movies[p0].Answer
//
//            } else if (movies[p0].QuestionTypeID == 4) {
//                solution_rbTruefalse.visibility = View.VISIBLE
//                solution_tvFillBlanks.visibility = View.GONE
//                ansList!!.visibility = View.GONE
//
//                when {
//                    movies[p0].CorrectAnswer != "True" -> {
//                        solution_rbTrue.setImageResource(R.drawable.wrong)
//                    }
//                    movies[p0].CorrectAnswer != "False" -> {
//                        solution_rbFalse.setImageResource(R.drawable.wrong)
//                    }
//                    else -> {
//                        solution_rbTrue.setImageResource(R.drawable.grey_round)
//                        solution_rbFalse.setImageResource(R.drawable.grey_round)
//                    }
//                }
//
//            } else if (movies[p0].QuestionTypeID == 7) {
//                ansList!!.layoutManager =
//                    LinearLayoutManager(this@ViewSolutionActivity, LinearLayoutManager.VERTICAL, false)
//
//                ansList!!.adapter = SolutionAdapter(
//                    this@ViewSolutionActivity,
//                    movies[p0].StudentTestAnswerMCQ,
//                    solution_page_img_que_img.width, 7
//                )
//            }
//        }
//
//        solution_btnNext.visibility = View.VISIBLE
//    }
//
//    fun getNextQuestion1() {
//        if ((movies.size - 1) > AppConstants.QUE_NUMBER1) {
//            AppConstants.QUE_NUMBER1 = AppConstants.QUE_NUMBER1 + 1
//
//            getType("solution", AppConstants.QUE_NUMBER1)
//
//            sideList!!.adapter = QuestionListSideMenuAdapter(
//                context!!,
//                sectionList!!,
//                sectionList1!!,
//                filterTypeSelectionInteface!!,
//                "solution"
//            )
//
////            setSideMenu1(1)
//        }
//    }
//
//    companion object {
//
//        var nextButton: Button? = null
//        var sideList: RecyclerView? = null
//        var sectionList: ArrayList<String>? = null
//        var sectionList1: ArrayList<QuestionTypeModel>? = null
//        var filterTypeSelectionInteface: FilterTypeSelectionInteface? = null
//        var context: Context? = null
//
//        fun setSideMenu1(type: Int) {
//            for (i in 0 until sectionList1!!.size) {
//                if (sectionList1!![i].qnumber == AppConstants.QUE_NUMBER1) {
//                    sectionList1!![i].type = type
//                }
//            }
//
//            sideList!!.adapter = QuestionListSideMenuAdapter(
//                context!!,
//                sectionList!!,
//                sectionList1!!,
//                filterTypeSelectionInteface!!,
//                "solution"
//            )
//        }
//    }
//}
