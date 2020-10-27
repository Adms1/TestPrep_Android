package com.testcraft.testcraft.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import co.lujun.androidtagview.TagView.OnTagClickListener
import com.google.gson.JsonObject
import com.testcraft.testcraft.R
import com.testcraft.testcraft.adapter.CreatetestQtypeAdapter
import com.testcraft.testcraft.adapter.SelectSubjectAdapter
import com.testcraft.testcraft.adapter.TemplateAdapter
import com.testcraft.testcraft.adapter.TemplateSectionAdapter
import com.testcraft.testcraft.interfaces.ChapterListInterface
import com.testcraft.testcraft.models.CreateTestQTypeModel
import com.testcraft.testcraft.models.GetChapterList
import com.testcraft.testcraft.models.TemplateSectionModel
import com.testcraft.testcraft.retrofit.WebClient
import com.testcraft.testcraft.retrofit.WebInterface
import com.testcraft.testcraft.utils.AppConstants
import com.testcraft.testcraft.utils.DialogUtils
import com.testcraft.testcraft.utils.Utils
import kotlinx.android.synthetic.main.activity_create_test.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class CreateTestActivity : AppCompatActivity(), ChapterListInterface {

    var qtypeArr: ArrayList<GetChapterList.GetChapterData> = ArrayList()
    var templateArr: ArrayList<GetChapterList.GetChapterData> = ArrayList()
    var mainQtypeArr: ArrayList<CreateTestQTypeModel> = ArrayList()

    var templateAdapter: TemplateAdapter? = null
    var templateSectionAdapter: TemplateSectionAdapter? = null

    var chapterInterface: ChapterListInterface? = null

    var template_id = ""

    var coursetypeid = ""
    var boardid = ""
    //    var courseid = ""
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
        boardid = intent.getStringExtra("board_id")
//        courseid = intent.getStringExtra("courseid")
        subid = intent.getStringExtra("sub_id")
        stdid = intent.getStringExtra("std_id")
        subname = intent.getStringExtra("sub_name")

        createtest_tvHeading.text = "Create Test ($subname)"

        if (coursetypeid == "1") {

            createtest_tvChapters.text = "Chapters"
            createtest_spTemplate.visibility = View.GONE
            createtest_tvTemplate.visibility = View.GONE
            createtest_clSection.visibility = View.GONE

            callChapterList()
        } else {

            createtest_tvMarks.visibility = View.GONE
            createtest_etMarks.visibility = View.GONE
            createtest_tvChapters.visibility = View.GONE
            createtest_ivSelect.visibility = View.GONE
            createtest_tagQtype.visibility = View.GONE
            createtest_tvChapters.text = "Subjects"
            createtest_spTemplate.visibility = View.VISIBLE
            createtest_tvTemplate.visibility = View.VISIBLE
            createtest_clSection.visibility = View.VISIBLE

            callSubjects()
            callTemplate()
        }

        createtest_spTemplate.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) { // Get the value selected by the user
                // e.g. to store it as a field or immediately call a method
                template_id = templateArr[position].ID
                callSectionList(template_id)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        createtest_ivBack.setOnClickListener {
            onBackPressed()
        }

        createtest_ivSelect.setOnClickListener {
            createtest_llSelect.visibility = View.VISIBLE

            try {
                val imm: InputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)
            } catch (e: Exception) { // TODO: handle exception

            }
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
            } else {
                if (isValid()) {
                    if ((createtest_etDuration.text.toString()).toInt() <= 180) {

                        callCreateCompetitiveTest()

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

    }

    override fun onBackPressed() {

        val intent = Intent(this@CreateTestActivity, DashboardActivity::class.java)
        if (coursetypeid == "1") {
            intent.putExtra("createtest", "true")
            intent.putExtra("coursetypeid", coursetypeid)

            intent.putExtra("board_id", boardid)
            intent.putExtra("sub_id", subid.toInt())
            intent.putExtra("std_id", stdid)
            intent.putExtra("sub_name", subname)
            intent.putExtra("isCompetitive", false)

        } else {
            intent.putExtra("createtest", "true")
            intent.putExtra("coursetypeid", coursetypeid)

            intent.putExtra("board_id", "0")
            intent.putExtra("sub_id", subid.toInt())
            intent.putExtra("std_id", "0")
            intent.putExtra("sub_name", subname)
            intent.putExtra("isCompetitive", true)

        }
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

        if (coursetypeid == "1") {
            if (TextUtils.isEmpty(createtest_etMarks.text.toString()) || createtest_etMarks.text.toString() == "0") {
                createtest_etMarks.error = "Please enter total questions"
                isvalid = false
            }

            if (qtypeArr.size <= 0) {
                isvalid = false
                Toast.makeText(this@CreateTestActivity, "Please select Chapters", Toast.LENGTH_SHORT).show()
            }
        }
//        } else {
//            if (qtypeArr.size <= 0) {
//                isvalid = false
//                Toast.makeText(this@CreateTestActivity, "Please select Subjects", Toast.LENGTH_SHORT).show()
//            }
//        }

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
            chapid = chapid + qtypeArr[i].ID + ","
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
        hashmap["ChapterID"] = chapid.substring(0, chapid.length - 1)
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
                        intent.putExtra("coursetypeid", coursetypeid)
                        intent.putExtra("isCompetitive", false)
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

    fun callSubjects() {

        if (!DialogUtils.isNetworkConnected(this@CreateTestActivity)) {
            Utils.ping(this@CreateTestActivity, AppConstants.NETWORK_MSG)
        }

        DialogUtils.showDialog(this@CreateTestActivity)

        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val hashmap = HashMap<String, String>()
        hashmap["CourseID"] = subid

        val call = apiService.callCreateTestSubject(hashmap)

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

    fun callTemplate() {

        if (!DialogUtils.isNetworkConnected(this@CreateTestActivity)) {
            Utils.ping(this@CreateTestActivity, AppConstants.NETWORK_MSG)
        }

        DialogUtils.showDialog(this@CreateTestActivity)

        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val hashmap = HashMap<String, String>()
        hashmap["CourseID"] = subid

        val call = apiService.callTemplate(hashmap)

        call.enqueue(object : Callback<GetChapterList> {
            override fun onResponse(call: Call<GetChapterList>, response: Response<GetChapterList>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.Status == "true") {

                        templateArr = response.body()!!.data

                        templateAdapter =
                            TemplateAdapter(this@CreateTestActivity, response.body()!!.data)
                        createtest_spTemplate.adapter = templateAdapter
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

    fun callSectionList(tempid: String) {

        if (!DialogUtils.isNetworkConnected(this@CreateTestActivity)) {
            Utils.ping(this@CreateTestActivity, AppConstants.NETWORK_MSG)
        }

        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val hashmap = HashMap<String, String>()
        hashmap["TemplateID"] = tempid

        val call = apiService.callTemplateSection(hashmap)

        call.enqueue(object : Callback<TemplateSectionModel> {
            override fun onResponse(call: Call<TemplateSectionModel>, response: Response<TemplateSectionModel>) {

                if (response.body() != null) {

                    if (response.body()!!.Status == "true") {

                        var total = 0.0

                        createtest_rvSection.layoutManager =
                            LinearLayoutManager(this@CreateTestActivity, LinearLayoutManager.VERTICAL, false)

                        for (i in 0 until response.body()!!.data.size) {
                            total += response.body()!!.data[i].Marks.toDouble() * response.body()!!.data[i].NoOfQue.toDouble()
                        }
                        createtest_total.text = total.toString()

                        templateSectionAdapter =
                            TemplateSectionAdapter(this@CreateTestActivity, response.body()!!.data)
                        createtest_rvSection.adapter = templateSectionAdapter

                    } else {
                        Toast.makeText(
                            this@CreateTestActivity,
                            response.body()!!.Msg,
                            Toast.LENGTH_LONG
                        ).show()

                    }
                }
            }

            override fun onFailure(call: Call<TemplateSectionModel>, t: Throwable) {
                // Log error here since request failed
                Log.e("", t.toString())
            }
        })
    }

    fun callQueLimit() {

        var chapid = ""
        for (i in 0 until qtypeArr.size) {
            chapid = chapid + qtypeArr[i].ID + ","
        }

        if (!DialogUtils.isNetworkConnected(this@CreateTestActivity)) {
            Utils.ping(this@CreateTestActivity, AppConstants.NETWORK_MSG)
        }

        DialogUtils.showDialog(this@CreateTestActivity)

        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val hashmap = HashMap<String, String>()
        if (coursetypeid == "1") {
            hashmap["BoardID"] = boardid
            hashmap["StandardID"] = stdid
            hashmap["SubjectID"] = subid
            hashmap["ChapterID"] = chapid.substring(0, chapid.length - 1)
        } else {

            hashmap["BoardID"] = subid
            hashmap["StandardID"] = "0"
            hashmap["SubjectID"] = "0"
            hashmap["ChapterID"] = chapid.substring(0, chapid.length - 1)
        }

        val call = apiService.callGetQueLimit(hashmap)

        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.get("Status").asString == "true") {

                        quelimit = response.body()!!.get("data").asInt

                        if ((createtest_etMarks.text.toString()).toInt() <= response.body()!!.get("data").asInt) {

                            if (coursetypeid == "1") {
                                callCreateBoardTest()
                            } else {
                                callCreateCompetitiveTest()
                            }

                        } else {
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

    fun callCreateCompetitiveTest() {

        var chapid = ""
        for (i in 0 until qtypeArr.size) {
            chapid = chapid + qtypeArr[i].ID + ","
        }

        if (!DialogUtils.isNetworkConnected(this@CreateTestActivity)) {
            Utils.ping(this@CreateTestActivity, AppConstants.NETWORK_MSG)
        }

        DialogUtils.showDialog(this@CreateTestActivity)

        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val hashmap = HashMap<String, String>()
        hashmap["UserID"] =
            Utils.getStringValue(this@CreateTestActivity, AppConstants.USER_ID, "0")!!
        hashmap["CourseID"] = subid
        hashmap["BoardID"] = "0"
        hashmap["StandardID"] = "0"
        hashmap["SubjectID"] = "0"
        hashmap["TypeID"] = coursetypeid
        hashmap["TestName"] = createtest_etTestname.text.toString()
        hashmap["desc"] = ""
        hashmap["Hint"] = ""
        hashmap["Duration"] = createtest_etDuration.text.toString()
        hashmap["DifficultyLevelIDs"] = "1,2,3,4"
        hashmap["TopicID"] = ""
//        hashmap["QueCount"] = createtest_etMarks.text.toString()
        hashmap["TemplateID"] = template_id

        val call = apiService.callCompetitiveCreateTest(hashmap)

        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!["Status"].asString == "true") {

                        val intent = Intent(this@CreateTestActivity, DashboardActivity::class.java)

                        intent.putExtra("createtest", "true")
                        intent.putExtra("sub_id", subid.toInt())
                        intent.putExtra("board_id", "")
                        intent.putExtra("std_id", "0")
                        intent.putExtra("sub_name", subname)
                        intent.putExtra("isCompetitive", true)
                        intent.putExtra("coursetypeid", coursetypeid)
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
