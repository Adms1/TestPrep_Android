package com.testprep.activity

import android.content.Context
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
import android.widget.Button
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.testprep.R
import com.testprep.adapter.QuestionListSideMenuAdapter
import com.testprep.interfaces.FilterTypeSelectionInteface
import com.testprep.models.QuestionTypeModel
import com.testprep.old.adapter.SolutionAdapter
import com.testprep.old.models.QuestionResponse
import com.testprep.retrofit.WebClient
import com.testprep.retrofit.WebInterface
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
    var movies: ArrayList<QuestionResponse.QuestionList> = ArrayList()

    var testid = ""
    var studenttestid = ""

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
        sectionList1 = ArrayList()

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

        ansList!!.layoutManager = LinearLayoutManager(this@ViewSolutionActivity, LinearLayoutManager.VERTICAL, false)

        solution_ivBack.setOnClickListener { onBackPressed() }

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

        val call = apiService.getSolution(testid, studenttestid)
        call.enqueue(object : Callback<QuestionResponse> {
            override fun onResponse(call: Call<QuestionResponse>, response: Response<QuestionResponse>) {

                DialogUtils.dismissDialog()

                movies = response.body()!!.data

                if (movies.size > 0) {
                    sectionList!!.add("Section 1")
                    for (i in 0 until movies.size) {
                        val questionTypeModel = QuestionTypeModel()
                        questionTypeModel.qnumber = i

//                        for (j in 0 until movies[i].StudentTestAnswerMCQ.size) {
//                            if (movies[i].StudentTestAnswerMCQ[j].IsCorrectAnswer && movies[i].StudentTestAnswerMCQ[j].StudentAnswer) {
//                                questionTypeModel.type = 2
//                                break
//                            } else {
//                                questionTypeModel.type = 3
//                            }

                        when {
                            movies[i].QuestionAns == "1" -> questionTypeModel.type = 2
                            movies[i].QuestionAns == "0" -> questionTypeModel.type = 3
                            else -> questionTypeModel.type = 5
                        }
//                        }

                        sectionList1!!.add(questionTypeModel)

                    }

                    Log.d("header", "" + sectionList)
                    Log.d("child", "" + childList)

                    solution_expQueList.layoutManager =
                        LinearLayoutManager(this@ViewSolutionActivity, LinearLayoutManager.VERTICAL, false)

                    solution_expQueList.adapter =
                        QuestionListSideMenuAdapter(
                            this@ViewSolutionActivity,
                            sectionList!!,
                            sectionList1!!,
                            filterTypeSelectionInteface!!,
                            "solution"
                        )
                }

                if ("http://content.testcraft.co.in/question/" + movies[0].QuestionImage != "") {

                    Picasso.get().load("http://content.testcraft.co.in/question/" + movies[0].QuestionImage)
                        .into(solution_page_img_que_img)

                    ansList!!.layoutManager =
                        LinearLayoutManager(this@ViewSolutionActivity, LinearLayoutManager.VERTICAL, false)

                    ansList!!.adapter = SolutionAdapter(
                        this@ViewSolutionActivity,
                        movies[0].StudentTestAnswerMCQ,
                        solution_page_img_que_img.width
                    )

                    Log.d("imgcall", "Number of movies received: " + movies.size)
                }
            }

            override fun onFailure(call: Call<QuestionResponse>, t: Throwable) {
                // Log error here since request failed
                Log.e("question res", t.toString())
                DialogUtils.dismissDialog()
            }
        })
    }

    override fun getType(p0: Int) {

        drawer_layout.closeDrawer(Gravity.END)

        if ("http://content.testcraft.co.in/question/" + movies[p0].QuestionImage != "") {

            Picasso.get().load("http://content.testcraft.co.in/question/" + movies[p0].QuestionImage)
                .into(imgQue)

            ansList!!.layoutManager =
                LinearLayoutManager(this@ViewSolutionActivity, LinearLayoutManager.VERTICAL, false)

            ansList!!.adapter = SolutionAdapter(
                this@ViewSolutionActivity,
                movies[p0].StudentTestAnswerMCQ,
                solution_page_img_que_img.width
            )
        }

        solution_btnNext.visibility = View.VISIBLE
    }

    fun getNextQuestion1() {
        if ((movies.size - 1) > AppConstants.QUE_NUMBER1) {
            AppConstants.QUE_NUMBER1 = AppConstants.QUE_NUMBER1 + 1

            getType(AppConstants.QUE_NUMBER1)

            sideList!!.adapter = QuestionListSideMenuAdapter(
                context!!,
                sectionList!!,
                sectionList1!!,
                filterTypeSelectionInteface!!,
                "solution"
            )

//            setSideMenu1(1)
        }
    }

    companion object {

        var nextButton: Button? = null
        var sideList: RecyclerView? = null
        var sectionList: ArrayList<String>? = null
        var sectionList1: ArrayList<QuestionTypeModel>? = null
        var filterTypeSelectionInteface: FilterTypeSelectionInteface? = null
        var context: Context? = null

        fun setSideMenu1(type: Int) {
            for (i in 0 until sectionList1!!.size) {
                if (sectionList1!![i].qnumber == AppConstants.QUE_NUMBER1) {
                    sectionList1!![i].type = type
                }
            }

            sideList!!.adapter = QuestionListSideMenuAdapter(
                context!!,
                sectionList!!,
                sectionList1!!,
                filterTypeSelectionInteface!!,
                "solution"
            )
        }
    }
}
