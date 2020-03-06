package com.testcraft.testcraft.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.testcraft.testcraft.R
import com.testcraft.testcraft.activity.DashboardActivity
import com.testcraft.testcraft.activity.DashboardActivity.Companion.setFragments
import com.testcraft.testcraft.activity.DeeplinkTestActivity
import com.testcraft.testcraft.activity.IntroActivity
import com.testcraft.testcraft.activity.QuestionInstructionActivity
import com.testcraft.testcraft.models.TestListModel
import com.testcraft.testcraft.sectionmodule.NewTabQuestionActivity
import com.testcraft.testcraft.utils.ActionIdData
import com.testcraft.testcraft.utils.AppConstants
import com.testcraft.testcraft.utils.CommonWebCalls
import com.testcraft.testcraft.utils.Utils

@SuppressLint("SetTextI18n")
class TestListAdapter(
    val context: Context,
    var dataList: ArrayList<TestListModel.TestData>
) :
    RecyclerView.Adapter<TestListAdapter.viewholder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): viewholder {

        return viewholder(
            LayoutInflater.from(context).inflate(
                R.layout.test_list_item_layout,
                p0,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(p0: viewholder, p1: Int) {

        p0.name.text = dataList[p1].TestName
        p0.time.text = "Time : " + dataList[p1].TestDuration
        p0.marks.text = dataList[p1].TestMarks
        p0.test.text = dataList[p1].StatusName

        if (dataList[p1].StatusName == "Analyse") {

            p0.test.setTextColor(context.resources.getColor(R.color.white))
            p0.test.background = context.resources.getDrawable(R.drawable.green_bg)

        } else {

            p0.test.setTextColor(context.resources.getColor(R.color.white))
            p0.test.background = context.resources.getDrawable(R.drawable.login_round_bg)

        }

        p0.test.setOnClickListener {

            CommonWebCalls.callToken(context, "1", "", ActionIdData.C1901, ActionIdData.T1901)

            when (dataList[p1].StatusName) {

                "Analyse" -> {
                    if (Utils.getStringValue(context, AppConstants.APP_MODE, "") == AppConstants.NORMAL_MODE) {

                        AppConstants.isFirst = 10
                        val bundle = Bundle()
                        bundle.putString("testid", dataList[p1].TestID.toString())
                        bundle.putString("studenttestid", dataList[p1].StudentTestID.toString())
                        bundle.putString("testname", dataList[p1].TestName)
                        bundle.putString("isCompetitive", dataList[p1].IsCompetetive)

                        //                bundle.putString("pkgid", pkgid)
                        //                bundle.putString("pname", name)
                        setFragments(bundle)

                        //                val intent = Intent(context, TestReviewActivity::class.java)
                        //                intent.putExtra("testid", dataList[p1].TestID.toString())
                        //                intent.putExtra("studenttestid", dataList[p1].StudentTestID.toString())
                        //                context.startActivity(intent)
                    } else {
                        val intent = Intent(context, IntroActivity::class.java)
                        context.startActivity(intent)
                    }
                }

                "Resume" -> {
                    val intent = Intent(context, NewTabQuestionActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP

                    intent.putExtra("isComeFrom", "testlist")

                    intent.putExtra("testid", dataList[p1].TestID.toString())
                    intent.putExtra("studenttestid", dataList[p1].StudentTestID.toString())
                    intent.putExtra("testname", dataList[p1].TestName)
                    intent.putExtra("testtime", dataList[p1].RemainTime)
                    intent.putExtra("totalque", dataList[p1].TotalQuestions)
                    intent.putExtra("subjectname", dataList[p1].SubjectName)
                    intent.putExtra("coursename", dataList[p1].CourseName)
                    intent.putExtra("totalmarks", dataList[p1].TestMarks)
                    intent.putExtra("tutorname", dataList[p1].TutorName)
                    intent.putExtra("totalhint", dataList[p1].NumberOfHint)
                    intent.putExtra("totalhintused", dataList[p1].NumberOfHintUsed)
                    intent.putExtra("que_instruction", dataList[p1].TestInstruction)
                    context.startActivity(intent)

                    if (Utils.getStringValue(context, AppConstants.APP_MODE, "") == AppConstants.DEEPLINK_MODE) {
                        (context as DeeplinkTestActivity).finish()
                    } else {
                        (context as DashboardActivity).finish()
                    }
                }

                "Start Test" -> {
                    val intent = Intent(context, QuestionInstructionActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP

                    intent.putExtra("isComeFrom", "testlist")

                    intent.putExtra("testid", dataList[p1].TestID.toString())
                    intent.putExtra("studenttestid", dataList[p1].StudentTestID.toString())
                    intent.putExtra("testname", dataList[p1].TestName)
                    intent.putExtra("testtime", dataList[p1].RemainTime)
                    intent.putExtra("totalque", dataList[p1].TotalQuestions)
                    intent.putExtra("subjectname", dataList[p1].SubjectName)
                    intent.putExtra("coursename", dataList[p1].CourseName)
                    intent.putExtra("totalmarks", dataList[p1].TestMarks)
                    intent.putExtra("tutorname", dataList[p1].TutorName)
                    intent.putExtra("totalhint", dataList[p1].NumberOfHint)
                    intent.putExtra("totalhintused", dataList[p1].NumberOfHintUsed)
                    intent.putExtra("que_instruction", dataList[p1].TestInstruction)
                    context.startActivity(intent)
                }
            }
        }
    }

    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var name: TextView = itemView.findViewById(R.id.test_item_tvTestName)
        var time: TextView = itemView.findViewById(R.id.test_item_tvTestDuration)
        var marks: TextView = itemView.findViewById(R.id.test_item_tvMarks)
        var test: TextView = itemView.findViewById(R.id.test_item_btnStart)

    }
}
