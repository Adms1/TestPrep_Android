package com.testcraftdemo.old

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.testcraftdemo.R
import com.testcraftdemo.old.models.QuestionResponse
import kotlinx.android.synthetic.main.activity_webview_page.*

class WebviewPageActivity : AppCompatActivity() {

    companion object {
        var count = 0
        var questions: ArrayList<QuestionResponse.QuestionList>? = null

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview_page)

//        val mActionBarToolbar = findViewById(R.id.toolbar_actionbar)
//        setSupportActionBar(mActionBarToolbar)
//        supportActionBar!!.setTitle("Web View")

        var fragment = WebviewFragment()
         supportFragmentManager.beginTransaction().add(R.id.web_container, fragment).commit()

        btnNext.setOnClickListener { v: View? ->

            count += 1

            if (count >= 0) {

                btnPrevious.visibility = View.VISIBLE

                var fragment1 = WebviewFragment()
//            var bundle1 = Bundle()
//            bundle.putInt("posi", count)
//            fragment.arguments = bundle
                supportFragmentManager.beginTransaction().replace(R.id.web_container, fragment1).commit()
            } else {
                btnPrevious.visibility = View.GONE
            }
        }

        btnPrevious.setOnClickListener { v: View? ->

            if (count > 0) {
                count -= 1

                if (count == 0) {
                    btnPrevious.visibility = View.GONE

                    var fragment2 = WebviewFragment()

                    supportFragmentManager.beginTransaction().replace(R.id.web_container, fragment2).commit()

                } else {

                    btnPrevious.visibility = View.VISIBLE

                    var fragment2 = WebviewFragment()

                    supportFragmentManager.beginTransaction().replace(R.id.web_container, fragment2).commit()
                }
            }
        }
    }

//    fun callQuestionApi() {
//
//        val sortDialog = Dialog(this@WebviewPageActivity)//,R.style.PauseDialog);//, R.style.PauseDialog);
//        val window = sortDialog.window
//        val wlp = window!!.attributes
//        sortDialog.window!!.attributes.verticalMargin = 0.10f
//        wlp.gravity = Gravity.BOTTOM
//        window.attributes = wlp
//
////        sortDialog.window!!.setBackgroundDrawableResource(R.drawable.filter1_1)
//
//        sortDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        sortDialog.setCancelable(true)
////        sortDialog.setContentView(getRoot())
//        sortDialog.show()
//
//        val apiService = ApiClient.getClient().create(ApiInterface::class.java)
//
//        val call = apiService.getTopRatedMovies("t1506-o2506-u3506-r4506")
//        call.enqueue(object : Callback<QuestionResponse> {
//            override fun onResponse(call: Call<QuestionResponse>, response: Response<QuestionResponse>) {
//
//                if (response.body()!!.message == "Success") {
//
//                    val movies = response.body()!!.data
//
//                    total.text = "Total" + movies.size
//
//                    questions = response.body()!!.data
//
//                    var fragment = WebviewFragment()
//
//                    supportFragmentManager.beginTransaction().add(R.id.web_container, fragment).commit()
//
//                    sortDialog.dismiss()
//
//                    Log.d("", "Number of movies received: " + movies.size)
//                } else {
//                    sortDialog.dismiss()
//                    Toast.makeText(this@WebviewPageActivity, "No Question at that time", Toast.LENGTH_LONG).show()
//                }
//            }
//
//            override fun onFailure(call: Call<QuestionResponse>, t: Throwable) {
//                // Log error here since request failed
//                Log.e("", t.toString())
//                sortDialog.dismiss()
//            }
//        })
//    }
//
}
