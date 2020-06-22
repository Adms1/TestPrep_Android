package com.testcraft.testcraft.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import co.lujun.androidtagview.TagView.OnTagClickListener
import com.google.gson.JsonObject
import com.testcraft.testcraft.R
import com.testcraft.testcraft.adapter.CreatetestQtypeAdapter
import com.testcraft.testcraft.adapter.SelectSubjectAdapter
import com.testcraft.testcraft.interfaces.ChapterListInterface
import com.testcraft.testcraft.models.CreateTestQTypeModel
import com.testcraft.testcraft.models.GetChapterList
import com.testcraft.testcraft.retrofit.WebClient
import com.testcraft.testcraft.retrofit.WebInterface
import com.testcraft.testcraft.utils.AppConstants
import com.testcraft.testcraft.utils.DialogUtils
import com.testcraft.testcraft.utils.Utils
import kotlinx.android.synthetic.main.activity_create_test.*
import okhttp3.internal.toNonNegativeInt
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class CreateTestActivity : AppCompatActivity(), ChapterListInterface {

    var qtypeArr: ArrayList<GetChapterList.GetChapterData> = ArrayList()
    var mainQtypeArr: ArrayList<CreateTestQTypeModel> = ArrayList()

    var chapterInterface: ChapterListInterface? = null

    var coursetypeid = ""
    var boardid = ""
    var courseid = ""
    var subid = ""
    var stdid = ""
    var subname = ""

    var quelimit = 0

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        setContentView(R.layout.activity_create_test)

        chapterInterface = this

        coursetypeid = intent.getStringExtra("coursetypeid")
        boardid = intent.getStringExtra("boardid")
        courseid = intent.getStringExtra("courseid")
        subid = intent.getStringExtra("subid")
        stdid = intent.getStringExtra("stdid")
        subname = intent.getStringExtra("subname")

        createtest_ivBack.setOnClickListener {
            onBackPressed()
        }

        createtest_ivSelect.setOnClickListener {
            createtest_llSelect.visibility = View.VISIBLE
        }

        createtest_btnCreate.setOnClickListener {
            if (coursetypeid == "1") {

                if (isValid()) {
                    if ((createtest_etDuration.text.toString()).toInt() <= 180) {

                        callQueLimit()

                    } else {
                        createtest_etDuration.error = "Max. durations is 180 minutes"
                    }

                }

            }
        }

        createtest_btnClose.setOnClickListener {
            createtest_llSelect.visibility = View.GONE
        }

        createtest_rvChapter.layoutManager =
            LinearLayoutManager(this@CreateTestActivity, LinearLayoutManager.VERTICAL, false)
//        createtest_rvChapter.adapter = SelectSubjectAdapter(this@CreateTestActivity)

        createtest_btnSelect.setOnClickListener {

            val arr: ArrayList<String> = ArrayList()

            for (i in 0 until qtypeArr.size) {
                arr.add(qtypeArr[i].Name)
            }

            createtest_tagQtype.tags = arr

            createtest_llSelect.visibility = View.GONE
        }

        createtest_tagQtype.setOnTagClickListener(object : OnTagClickListener {

            override fun onTagClick(position: Int, text: String) { // ...
            }

            override fun onTagLongClick(position: Int, text: String) { // ...
            }

            override fun onSelectedTagDrag(position: Int, text: String) { // ...
            }

            override fun onTagCrossClick(position: Int) {
            }

        })

//        AddQType()
        callChapterList()

    }

    override fun onBackPressed() {

        val intent = Intent(this@CreateTestActivity, DashboardActivity::class.java)
        intent.putExtra("createtest", "true")
        intent.putExtra("sub_id", subid.toInt())
        intent.putExtra("board_id", boardid)
        intent.putExtra("std_id", stdid)
        intent.putExtra("sub_name", subname)
        intent.putExtra("isCompetitive", coursetypeid)
        startActivity(intent)

    }

    fun isValid(): Boolean {

        var isvalid = true

        if (TextUtils.isEmpty(createtest_etTestname.text.toString())) {
            createtest_etTestname.error = "Please enter test name"
            isvalid = false
        }

        if (TextUtils.isEmpty(createtest_etDuration.text.toString()) || createtest_etDuration.text.toString() == "0") {
            createtest_etDuration.error = "Please enter duration"
            isvalid = false
        }

        if (TextUtils.isEmpty(createtest_etMarks.text.toString()) || createtest_etMarks.text.toString() == "0") {
            createtest_etMarks.error = "Please enter total questions"
            isvalid = false
        }

        if (qtypeArr.size <= 0) {
            isvalid = false
            Toast.makeText(this@CreateTestActivity, "Please select Chapters", Toast.LENGTH_SHORT).show()
        }

        return isvalid

    }

    fun AddQType() {
        val createTestQTypeModel = CreateTestQTypeModel()
        createTestQTypeModel.qtype = "1"
        createTestQTypeModel.qname = "MCQ"
        createTestQTypeModel.count = createtest_etQnumber.text.toString()
        mainQtypeArr.add(createTestQTypeModel)

        val createTestQTypeModel1 = CreateTestQTypeModel()
        createTestQTypeModel1.qtype = "2"
        createTestQTypeModel1.qname = "Fill In The Blank"
        createTestQTypeModel1.count = createtest_etQnumber.text.toString()
        mainQtypeArr.add(createTestQTypeModel1)

        val createTestQTypeModel2 = CreateTestQTypeModel()
        createTestQTypeModel2.qtype = "3"
        createTestQTypeModel2.qname = "Match The Following"
        createTestQTypeModel2.count = createtest_etQnumber.text.toString()
        mainQtypeArr.add(createTestQTypeModel2)

        val createTestQTypeModel3 = CreateTestQTypeModel()
        createTestQTypeModel3.qtype = "4"
        createTestQTypeModel3.qname = "True and False"
        createTestQTypeModel3.count = createtest_etQnumber.text.toString()
        mainQtypeArr.add(createTestQTypeModel3)

        val createTestQTypeModel4 = CreateTestQTypeModel()
        createTestQTypeModel4.qtype = "5"
        createTestQTypeModel4.qname = "Descriptive"
        createTestQTypeModel4.count = createtest_etQnumber.text.toString()
        mainQtypeArr.add(createTestQTypeModel4)

        val createTestQTypeModel5 = CreateTestQTypeModel()
        createTestQTypeModel5.qtype = "6"
        createTestQTypeModel5.qname = "Comprehension"
        createTestQTypeModel5.count = createtest_etQnumber.text.toString()
        mainQtypeArr.add(createTestQTypeModel5)

        val createTestQTypeModel6 = CreateTestQTypeModel()
        createTestQTypeModel6.qtype = "7"
        createTestQTypeModel6.qname = "MCQ 2"
        createTestQTypeModel6.count = createtest_etQnumber.text.toString()
        mainQtypeArr.add(createTestQTypeModel6)

        val createTestQTypeModel7 = CreateTestQTypeModel()
        createTestQTypeModel7.qtype = "8"
        createTestQTypeModel7.qname = "Integer Type"
        createTestQTypeModel7.count = createtest_etQnumber.text.toString()
        mainQtypeArr.add(createTestQTypeModel7)

        createtest_spQtype.adapter = CreatetestQtypeAdapter(this@CreateTestActivity, mainQtypeArr)

    }

    fun callCreateBoardTest() {

        var chapid = ""
        for (i in 0 until qtypeArr.size) {
            chapid += qtypeArr[i].ID
        }

        if (!DialogUtils.isNetworkConnected(this@CreateTestActivity)) {
            Utils.ping(this@CreateTestActivity, AppConstants.NETWORK_MSG)
        }

        DialogUtils.showDialog(this@CreateTestActivity)

        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val hashmap = HashMap<String, String>()
        hashmap["UserID"] =
            Utils.getStringValue(this@CreateTestActivity, AppConstants.USER_ID, "0")!!
        hashmap["CourseID"] = "0"
        hashmap["BoardID"] = boardid
        hashmap["StandardID"] = stdid
        hashmap["SubjectID"] = subid
        hashmap["TypeID"] = coursetypeid
        hashmap["TestName"] = createtest_etTestname.text.toString()
        hashmap["desc"] = ""
        hashmap["Hint"] = ""
        hashmap["Duration"] = createtest_etDuration.text.toString()
        hashmap["DifficultyLevelIDs"] = "1,2,3,4"
        hashmap["ChapterID"] = chapid.substring(chapid.length - 1)
        hashmap["QueCount"] = createtest_etMarks.text.toString()

        val call = apiService.callCreateTest(hashmap)

        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!["Status"].asString == "true") {

                        val intent = Intent(this@CreateTestActivity, DashboardActivity::class.java)
                        intent.putExtra("createtest", "true")
                        intent.putExtra("sub_id", subid.toInt())
                        intent.putExtra("board_id", boardid)
                        intent.putExtra("std_id", stdid)
                        intent.putExtra("sub_name", subname)
                        intent.putExtra("isCompetitive", coursetypeid)
                        startActivity(intent)

                    } else {
                        Toast.makeText(
                            this@CreateTestActivity,
                            response.body()!!["Msg"].asString,
                            Toast.LENGTH_LONG
                        ).show()

                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // Log error here since request failed
                Log.e("", t.toString())
                DialogUtils.dismissDialog()
            }
        })
    }

    fun callChapterList() {

        if (!DialogUtils.isNetworkConnected(this@CreateTestActivity)) {
            Utils.ping(this@CreateTestActivity, AppConstants.NETWORK_MSG)
        }

        DialogUtils.showDialog(this@CreateTestActivity)

        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val hashmap = HashMap<String, String>()
        hashmap["BoardID"] = boardid
        hashmap["StandardID"] = stdid
        hashmap["SubjectID"] = subid

        val call = apiService.callChapterList(hashmap)

        call.enqueue(object : Callback<GetChapterList> {
            override fun onResponse(call: Call<GetChapterList>, response: Response<GetChapterList>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.Status == "true") {

                        createtest_rvChapter.adapter =
                            SelectSubjectAdapter(this@CreateTestActivity, response.body()!!.data, chapterInterface!!)

                    } else {
                        Toast.makeText(
                            this@CreateTestActivity,
                            response.body()!!.Msg,
                            Toast.LENGTH_LONG
                        ).show()

                    }
                }
            }

            override fun onFailure(call: Call<GetChapterList>, t: Throwable) {
                // Log error here since request failed
                Log.e("", t.toString())
                DialogUtils.dismissDialog()
            }
        })
    }

    fun callQueLimit() {

        var chapid = ""
        for (i in 0 until qtypeArr.size) {
            chapid += qtypeArr[i].ID
        }

        if (!DialogUtils.isNetworkConnected(this@CreateTestActivity)) {
            Utils.ping(this@CreateTestActivity, AppConstants.NETWORK_MSG)
        }

        DialogUtils.showDialog(this@CreateTestActivity)

        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val hashmap = HashMap<String, String>()
        hashmap["BoardID"] = boardid
        hashmap["StandardID"] = stdid
        hashmap["SubjectID"] = subid
        hashmap["ChapterID"] = chapid

        val call = apiService.callGetQueLimit(hashmap)

        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.get("Status").asString == "true") {

                        quelimit = response.body()!!.get("data").asInt

                        if((createtest_etMarks.text.toString()).toInt() <= response.body()!!.get("data").asInt) {
                            callCreateBoardTest()
                        }else{
                            Toast.makeText(this@CreateTestActivity, "Question limit according to your selection is $quelimit", Toast.LENGTH_SHORT).show()
                        }

                    } else {

                        Toast.makeText(
                            this@CreateTestActivity,
                            response.body()!!.get("Msg").asString,
                            Toast.LENGTH_LONG
                        ).show()

                    }

                }

            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                // Log error here since request failed
                Log.e("", t.toString())
                DialogUtils.dismissDialog()
            }
        })

    }

    override fun getSelectedChapter(list: ArrayList<GetChapterList.GetChapterData>) {

        qtypeArr = ArrayList()

        if (list.size > 0) {
            for (i in 0 until list.size) {
                if (list[i].isSelected) {
                    qtypeArr.add(list[i])
                }
            }
        }

    }

}
