package com.testprep.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.testprep.R
import com.testprep.activity.TabwiseQuestionActivity
import com.testprep.models.TestListModel

class TestListAdapter(val context: Context, var dataList: ArrayList<TestListModel.TestData>) :
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

        p0.test.setOnClickListener {

            val intent = Intent(context, TabwiseQuestionActivity::class.java)
            intent.putExtra("testid", dataList[p1].TestID.toString())
            intent.putExtra("studenttestid", dataList[p1].StudentTestID.toString())
            context.startActivity(intent)

        }
    }

    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var name: TextView = itemView.findViewById(R.id.test_item_tvTestName)
        var test: TextView = itemView.findViewById(R.id.test_item_tvStart)

    }
}
