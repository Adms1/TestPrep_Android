//package com.testprep.old
//
//import android.app.Dialog
//import android.os.Bundle
//import android.support.v7.app.AppCompatActivity
//import android.util.Log
//import android.view.Gravity
//import android.view.View
//import android.view.Window
//import android.widget.Toast
//import com.testprep.R
//import com.testprep.fragments.NewPageviewFragment
//import com.testprep.old.WebviewPageActivity.Companion.count
//import com.testprep.old.models.QuestionResponse
//import com.testprep.retrofit.WebClient
//import com.testprep.retrofit.WebInterface
//import kotlinx.android.synthetic.main.activity_page.*
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//
//class NewPageActivity : AppCompatActivity() {
//    companion object {
//        var countt = 0
//        var questionss: ArrayList<QuestionResponse.QuestionList>? = null
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_page)
//
////        toolbar.title = "Image View"
//
//        var fragment = NewPageviewFragment()
//        var bundle = Bundle()
//        bundle.putInt("posi", count)
//        fragment.arguments = bundle
//        supportFragmentManager.beginTransaction().add(R.id.web_container, fragment).commit()
//
//        ivBack.setOnClickListener {
//            onBackPressed()
//        }
//
//        btnNext.setOnClickListener { v: View? ->
//
//            countt += 1
//
//            if (countt >= 0) {
//
//                btnPrevious.visibility = View.VISIBLE
//
//                var fragment1 = NewPageviewFragment()
//                var bundle = Bundle()
//                bundle.putInt("posi", count)
//                fragment.arguments = bundle
//                supportFragmentManager.beginTransaction().replace(R.id.web_container, fragment1).commit()
//            } else {
//                btnPrevious.visibility = View.GONE
//            }
//
////            countt += 1
////
////            var fragment1 = com.testprep.fragments.NewPageviewFragment()
//////            var bundle1 = Bundle()
//////            bundle.putInt("posi", count)
//////            fragment.arguments = bundle
////            supportFragmentManager.beginTransaction().replace(R.id.web_container, fragment1).commit()
//
//        }
//
//        btnPrevious.setOnClickListener { v: View? ->
//
//
//            if (countt > 0) {
//                countt -= 1
//
//                if (countt == 0) {
//                    btnPrevious.visibility = View.GONE
//
//                    var fragment2 = NewPageviewFragment()
//                    var bundle = Bundle()
//                    bundle.putInt("posi", count)
//                    fragment.arguments = bundle
//                    supportFragmentManager.beginTransaction().replace(R.id.web_container, fragment2).commit()
//
//                } else {
//
//
//                    btnPrevious.visibility = View.VISIBLE
//
//                    var fragment2 = NewPageviewFragment()
//                    var bundle = Bundle()
//                    bundle.putInt("posi", count)
//                    fragment.arguments = bundle
//                    supportFragmentManager.beginTransaction().replace(R.id.web_container, fragment2).commit()
//                }
//            }
//
////            countt -= 1
////
////            if(countt > 0) {
////
////                btnNext.visibility = View.VISIBLE
////
////                var fragment2 = com.testprep.fragments.NewPageviewFragment()
//////            var bundle2 = Bundle()
//////            bundle.putInt("posi", count)
//////            fragment.arguments = bundle
////                supportFragmentManager.beginTransaction().replace(R.id.web_container, fragment2).commit()
////
////            }else{
////                btnNext.visibility = View.GONE
////            }
//        }
//
//    }
//
//    fun callQuestionApi() {
//
//        val sortDialog = Dialog(this@NewPageActivity)//,R.style.PauseDialog);//, R.style.PauseDialog);
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
//        val apiService = WebClient.getClient().create(WebInterface::class.java)
//
//        val call = apiService.getQuestions("2")
//        call.enqueue(object : Callback<QuestionResponse> {
//            override fun onResponse(call: Call<QuestionResponse>, response: Response<QuestionResponse>) {
//
//                if (response.body()!!.Msg == "Success") {
//                    val movies = response.body()!!.data
//
//                    questionss = response.body()!!.data
//
//                    var fragment = NewPageviewFragment()
//                    var bundle = Bundle()
//                    bundle.putInt("posi", count)
//                    fragment.arguments = bundle
//                    supportFragmentManager.beginTransaction().add(R.id.web_container, fragment).commit()
//
//                    sortDialog.dismiss()
//
//                    Log.d("", "Number of movies received: " + movies.size)
//                } else {
//                    sortDialog.dismiss()
//                    Toast.makeText(this@NewPageActivity, "No Question at that time", Toast.LENGTH_LONG).show()
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
//}
