package com.testprep.old.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.squareup.picasso.Picasso
import com.testprep.models.SelectedCheckboxModel
import com.testprep.sectionmodule.NewQuestionResponse
import com.testprep.sectionmodule.NewTabQuestionActivity

class SelectImageOptionAdapter(
    val context: Context,
    val dataList: ArrayList<NewQuestionResponse.QuestionDataList>,
    var qsize: Int,
    var qtype: Int,
    var qid: Int,
    var answer: String
) : RecyclerView.Adapter<SelectImageOptionAdapter.viewholder>() {

    private var lastCheckedRadioGroup: RadioGroup? = null
    var ansstr = ""
    var ansArr: ArrayList<SelectedCheckboxModel> = ArrayList()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): viewholder {

        return viewholder(
            LayoutInflater.from(context).inflate(
                com.testprep.R.layout.select_image_option_list_view,
                p0,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(p0: viewholder, p1: Int) {

        if ("http://content.testcraft.co.in/question/" + dataList[p1].AnswerImage != "") {

//            Log.d("qsize", "" + qsize)

            Picasso.get().load("http://content.testcraft.co.in/question/" + dataList[p1].AnswerImage)
                .resize(qsize, p0.opone1.height)
//                .fit()
//                .centerInside()
                .into(p0.opone1)
        }

        var id = (p1 + 1) * 100

        when (qtype) {
            1 -> {

                p0.opone.visibility = View.VISIBLE
                p0.opone1.visibility = View.VISIBLE
                p0.llCheckBox.visibility = View.GONE

                var rb = RadioButton(this@SelectImageOptionAdapter.context)

                rb.layoutParams = RadioGroup.LayoutParams(
                    RadioGroup.LayoutParams.MATCH_PARENT,
                    RadioGroup.LayoutParams.WRAP_CONTENT
                )

                rb.id = dataList[p1].MultipleChoiceQuestionAnswerID.toInt()

                if (answer == dataList[p1].MultipleChoiceQuestionAnswerID) {
                    lastCheckedRadioGroup = p0.opone
////                   p0.opone.check(rb.id)
                    rb.isChecked = true
                }

//                NewTabQuestionActivity.setButton(p1, dataList[p1].MultipleChoiceQuestionAnswerID, qid)

                p0.opone.addView(rb)

            }
            7 -> {

                p0.opone.visibility = View.GONE
                p0.opone1.visibility = View.VISIBLE
                p0.llCheckBox.visibility = View.VISIBLE

                val cb = CheckBox(this@SelectImageOptionAdapter.context)

                cb.id = dataList[p1].MultipleChoiceQuestionAnswerID.toInt()

//                ansstr = answer
                var tempArr = answer.split(",")

                for (i in 0 until tempArr.size) {
                    if (tempArr[i] == cb.id.toString()) {
                        val selectedCheckboxModel = SelectedCheckboxModel()
                        selectedCheckboxModel.ids = cb.id.toString()
                        selectedCheckboxModel.selected = true
                        ansArr.add(selectedCheckboxModel)

                        cb.isChecked = true

                    }/*else {
                        val selectedCheckboxModel = SelectedCheckboxModel()
                        selectedCheckboxModel.ids = cb.id.toString()
                        selectedCheckboxModel.selected = false
                        ansArr.add(selectedCheckboxModel)
                    }*/
                }

//                if (ansArr.contains(dataList[p1].MultipleChoiceQuestionAnswerID)) {
////                   p0.opone.check(rb.id)
//                    cb.isChecked = true
//                }

                cb.setOnCheckedChangeListener { buttonView, isChecked ->

                    if (isChecked) {
                        if (ansArr.size > 0) {
                            for (i in 0 until ansArr.size) {
                                if (ansArr[i].ids == cb.id.toString()) {

                                    ansArr[i].selected = true

                                } else {
                                    val selectedCheckboxModel = SelectedCheckboxModel()
                                    selectedCheckboxModel.ids = cb.id.toString()
                                    selectedCheckboxModel.selected = true
                                    ansArr.add(selectedCheckboxModel)
                                }

                                break
                            }
                        } else {
                            val selectedCheckboxModel = SelectedCheckboxModel()
                            selectedCheckboxModel.ids = cb.id.toString()
                            selectedCheckboxModel.selected = true
                            ansArr.add(selectedCheckboxModel)
                        }

                        multiSelection(p1)

                    } else {
                        for (i in 0 until ansArr.size) {
                            if (ansArr[i].ids == cb.id.toString()) {
                                ansArr[i].selected = false
                            }
                        }

                        multiSelection(p1)
                    }
                }

                p0.llCheckBox.addView(cb)

            }
        }

        p0.opone.setOnCheckedChangeListener { group, checkedId ->

            //            if(answer == dataList[p1].MultipleChoiceQuestionAnswerID){
//
//                p0.opone.checkedRadioButtonId
//                p0.opone.check(dataList[p1].MultipleChoiceQuestionAnswerID.toInt())
//            }

            if (lastCheckedRadioGroup != null
                && lastCheckedRadioGroup!!.checkedRadioButtonId != p0.opone.checkedRadioButtonId
                && lastCheckedRadioGroup!!.checkedRadioButtonId != -1
            ) {
                Log.d("grp", "clear")
                lastCheckedRadioGroup!!.clearCheck()

            } else if (lastCheckedRadioGroup == null) {

                Log.d("grp", "null")

            } else {

                Log.d("grp", "else")
            }
            lastCheckedRadioGroup = p0.opone

            NewTabQuestionActivity.setButton(p1, dataList[p1].MultipleChoiceQuestionAnswerID, qid)
        }
    }

    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var opone1: ImageView = itemView.findViewById(com.testprep.R.id.option_one1)
        var opone: RadioGroup = itemView.findViewById(com.testprep.R.id.option_one)
        var llmain: RelativeLayout = itemView.findViewById(com.testprep.R.id.option_lll)
        var llCheckBox: LinearLayout = itemView.findViewById(com.testprep.R.id.option_llCheckbox)
    }

    fun multiSelection(p1: Int) {
        for (i in 0 until ansArr.size) {
            if (ansArr[i].selected) {
                ansstr = ansstr + ansArr[i].ids + ","
            }
        }

        NewTabQuestionActivity.setButton(p1, ansstr, qid)
    }

}
