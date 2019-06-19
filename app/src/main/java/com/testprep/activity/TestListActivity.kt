package com.testprep.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.testprep.R
import com.testprep.adapter.TestListAdapter
import kotlinx.android.synthetic.main.activity_test_list.*

class TestListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setContentView(R.layout.activity_test_list)

        test_rvPkgList.layoutManager = LinearLayoutManager(this@TestListActivity, LinearLayoutManager.VERTICAL, false)
        test_rvPkgList.adapter = TestListAdapter(this@TestListActivity)

    }
}
