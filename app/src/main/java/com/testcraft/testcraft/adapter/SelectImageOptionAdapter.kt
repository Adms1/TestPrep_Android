package com.testcraft.testcraft.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.testcraft.testcraft.R
import com.testcraft.testcraft.models.SelectedCheckboxModel
import com.testcraft.testcraft.sectionmodule.NewQuestionResponse
import com.testcraft.testcraft.sectionmodule.NewTabQuestionActivity
import com.testcraft.testcraft.utils.ActionIdData
import com.testcraft.testcraft.utils.CommonWebCalls

class SelectImageOptionAdapter(
    val context: Context,
    val dataList: ArrayList<NewQuestionResponse.QuestionDataList>,
    var qsize: Int,
    var qtype: Int,
    var answer: String, var typee: Int
) : RecyclerView.Adapter<SelectImageOptionAdapter.viewholder>() {

    private var lastCheckedRadioGroup: RadioGroup? = null
    var ansstr = ""
    var ansArr: ArrayList<SelectedCheckboxModel> = ArrayList()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): viewholder {

        return viewholder(
            LayoutInflater.from(context).inflate(
                R.layout.select_image_option_list_view,
                p0,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(p0: viewholder, p1: Int) {

        p0.abcd.text = dataList[p1].optiontext

        if (dataList[p1].AnswerImage != "") {

//            Log.d("qsize", "" + qsize)

            Picasso.get().load(dataList[p1].AnswerImage)
//                .transform(transform.getTransformation(p0.opone1))
                .resize(qsize, p0.opone1.height)
//                .fit()
                .centerInside()
                .into(p0.opone1)
        }

//        var id = (p1 + 1) * 100


        when (qtype) {
            1 -> {

                p0.opone.visibility = View.VISIBLE
                p0.opone1.visibility = View.VISIBLE
                p0.llCheckBox.visibility = View.GONE

                val rb = RadioButton(this@SelectImageOptionAdapter.context)

                rb.layoutParams = RadioGroup.LayoutParams(
                    RadioGroup.LayoutParams.MATCH_PARENT,
                    RadioGroup.LayoutParams.WRAP_CONTENT
                )

                rb.id = dataList[p1].MultipleChoiceQuestionAnswerID.toInt()

                if (typee == 0) {
                    if (answer == dataList[p1].MultipleChoiceQuestionAnswerID) {
                        lastCheckedRadioGroup = p0.opone
////                   p0.opone.check(rb.id)
                        rb.isChecked = true
                    }
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

                if (typee == 0) {
//                ansstr = answer
                    val tempArr = answer.split(",")

                    for (i in tempArr) {
                        if (i == cb.id.toString()) {
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
                }

//                if (ansArr.contains(dataList[p1].MultipleChoiceQuestionAnswerID)) {
////                   p0.opone.check(rb.id)
//                    cb.isChecked = true
//                }

                cb.setOnCheckedChangeListener { buttonView, isChecked ->

                    CommonWebCalls.callToken(
                        context,
                        "1",
                        "",
                        ActionIdData.C2006,
                        ActionIdData.T2006
                    )

                    if (cb.isPressed) {
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

                            multiSelection()

                        } else {
                            for (i in 0 until ansArr.size) {
                                if (ansArr[i].ids == cb.id.toString()) {
                                    ansArr[i].selected = false
                                }
                            }

                            multiSelection()
                        }
                    }
                }

                p0.llCheckBox.addView(cb)

            }
        }

        p0.opone.setOnCheckedChangeListener { group, checkedId ->

            CommonWebCalls.callToken(context, "1", "", ActionIdData.C2006, ActionIdData.T2006)

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

            NewTabQuestionActivity.setButton(
                dataList[p1].MultipleChoiceQuestionAnswerID
            )
        }
    }

    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var opone1: ImageView = itemView.findViewById(R.id.option_one1)
        var abcd: TextView = itemView.findViewById(R.id.option_abcd)
        var opone: RadioGroup = itemView.findViewById(R.id.option_one)
        var llCheckBox: LinearLayout = itemView.findViewById(R.id.option_llCheckbox)
    }

    fun multiSelection() {

        ansstr = ""

        val sortArr: ArrayList<String> = ArrayList()

        for (i in 0 until ansArr.size) {
            if (ansArr[i].selected) {
                sortArr.add(ansstr + ansArr[i].ids)
            }
        }

        sortArr.sort()

        ansstr = sortArr.toString().replace("[", "").replace("]", "").replace(" ", "").trim()

        Log.d("multichoiceanswer", "answerrrrrr   $ansstr")

//        for (i in 0 until sortArr.size) {
//            if (ansArr[i].selected) {
//                ansstr = ansstr + sortArr[i] + ","
//            }
//        }

        NewTabQuestionActivity.setButton(ansstr)
    }

}
