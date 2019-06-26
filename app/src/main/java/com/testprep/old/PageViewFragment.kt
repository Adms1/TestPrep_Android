package com.testprep.old

import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import com.squareup.picasso.Picasso
import com.testprep.R
import com.testprep.old.PageActivity.Companion.countt
import com.testprep.old.adapter.SelectImageOptionAdapter
import com.testprep.old.adapter.SolutionAdapter
import com.testprep.old.models.QuestionResponse
import com.testprep.retrofit.WebClient
import com.testprep.retrofit.WebInterface
import com.testprep.utils.Utils
import kotlinx.android.synthetic.main.fragment_page_view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URL

/**
 * A simple [Fragment] subclass.
 *
 */

class PageViewFragment : Fragment() {

    private var que_list: ArrayList<QuestionResponse.QuestionList> = ArrayList()
    private var que_num = 0
    private var come = ""
    private var imgQue: ImageView? = null
    private var ansList: RecyclerView? = null

    companion object {

        var qsize = 0

        fun newInstance(sectionNumber: Int, come: String): PageViewFragment {
            val fragment = PageViewFragment()
            val args = Bundle()
            args.putInt("que_number", sectionNumber)
            args.putString("come", come)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var vieww: View = inflater.inflate(R.layout.fragment_page_view, container, false)

        imgQue = vieww.findViewById(R.id.page_img_que_img)
        ansList = vieww.findViewById(R.id.wv_question_list)

        imgQue!!.isDrawingCacheEnabled = true

        return vieww

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        que_list = Utils.getArrayList(activity!!, "que_list")

        val args = arguments
        que_num = args!!.getInt("que_number")
        come = args.getString("come")!!

        ansList!!.isNestedScrollingEnabled = false

        ansList!!.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        Handler().postDelayed(
            /* Runnable
                 * Showing splash screen with a timer. This will be useful when you
                 * want to show case your app logo / company
                 */

            {
                // This method will be executed once the timer is over
                // Start your app main activity
                Log.d("sizeee", "" + imgQue!!.width + ", " + imgQue!!.height)

                Picasso.get().load("http://content.testcraft.co.in/question/" + que_list[que_num].QuestionImage)

                    .resize(imgQue!!.width, imgQue!!.height)
//            .transform(imageTransform(200, true))
                    .into(imgQue)

//        qsize = page_img_que_img.width

                if (activity != null) {

                    if (come != "solution") {

                        ansList!!.adapter = SelectImageOptionAdapter(
                            activity!!,
                            que_list[que_num].StudentTestQuestionMCQ,
                            imgQue!!.width
                        )
                    } else {
                        ansList!!.adapter = SolutionAdapter(
                            activity!!,
                            que_list[que_num].StudentTestQuestionMCQ,
                            imgQue!!.width
                        )
                    }
                }

            }, 1000
        )

//        callQuestionApi()

    }

    fun callQuestionApi() {

        val sortDialog = Dialog(activity!!)//,R.style.PauseDialog);//, R.style.PauseDialog);
        val window = sortDialog.window
        val wlp = window!!.attributes
        sortDialog.window!!.attributes.verticalMargin = 0.10f
        wlp.gravity = Gravity.BOTTOM
        window.attributes = wlp

//        sortDialog.window!!.setBackgroundDrawableResource(R.drawable.filter1_1)

        sortDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        sortDialog.setCancelable(true)
//        sortDialog.setContentView(getRoot())
        sortDialog.show()

        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getQuestions("2")
        call.enqueue(object : Callback<QuestionResponse> {
            override fun onResponse(call: Call<QuestionResponse>, response: Response<QuestionResponse>) {

                if (response.body()!!.Msg == "Success") {
                    val movies = response.body()!!.data

//                    totall.text = "Total" + movies.size

//                    qno.text = "Q." + (countt+1)

                    Log.d("qid", "" + movies[0].QuestionID)

                    if(countt >= 0) {
                        if ("http://content.testcraft.co.in/question/" + movies[0].QuestionImage != "") {

//                            var url = URL("http://content.testcraft.co.in/question/" + movies[pos].titleimg)
//                            var bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())

//                            var widhtx  = bmp.width
//                            var heightx = bmp.height

//                            Log.d("imgsize", "widht" + widhtx + "  height" + heightx)

                            Picasso.get().load("http://content.testcraft.co.in/question/" + movies[0].QuestionImage)
                                .resize(page_img_que_img.width, page_img_que_img.height)
                                .into(page_img_que_img)

                            qsize = page_img_que_img.width


//                            Log.d("imgsize", "widht" + page_img_que_img.width + "  height" + page_img_que_img.height)
//                            Picasso.get().load("https://homeshealth.info/wp-content/uploads/2018/02/classy-algebra-distance-formula-problems-in-distance-formula-of-algebra-distance-formula-problems.jpg")
//                                .resize(page_img_que_img.width, page_img_que_img.height)
//                                .into(page_img_que_img)

//
//                        val photoPath = Environment.getExternalStorageDirectory().toString() + "testcraftimg/pic.jpg"
//                        val options = BitmapFactory.Options()
//                        options.inSampleSize = 8
//                        val b = BitmapFactory.decodeFile(photoPath, options)
//                        page_img_que_img.setImageBitmap(b)


                            ansList!!.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

                            if (come != "solution") {

                                ansList!!.adapter = SelectImageOptionAdapter(
                                    activity!!,
                                    movies[0].StudentTestQuestionMCQ,
                                    page_img_que_img.width
                                )
                            } else {
                                ansList!!.adapter = SolutionAdapter(
                                    activity!!,
                                    movies[0].StudentTestQuestionMCQ,
                                    page_img_que_img.width
                                )
                            }


                        }
                    }else{
                        if ("http://content.testcraft.co.in/question/" + movies[0].QuestionImage != "") {
                            Picasso.get().load("http://content.testcraft.co.in/question/" + movies[0].QuestionImage)
                                .resize(page_img_que_img.width, page_img_que_img.height)
                                .into(page_img_que_img)

                        }

                        ansList!!.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

                        if (come != "solution") {

                            ansList!!.adapter = SelectImageOptionAdapter(
                                activity!!,
                                movies[0].StudentTestQuestionMCQ,
                                page_img_que_img.width
                            )
                        } else {
                            ansList!!.adapter = SolutionAdapter(
                                activity!!,
                                movies[0].StudentTestQuestionMCQ,
                                page_img_que_img.width
                            )
                        }

                    }

//                    html_text.setHtml(movies[pos].titlehtml,
//                        HtmlHttpImageGetter(html_text)
//                    );

//                    val url = URL("http://content.testcraft.co.in/question/9b734bff-db1d-42f5-8c54-0cf96612193d.jpeg")
//                    val bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())
//
//                    val drawable = BitmapDrawable(resources, bmp)
//
//                    page_img_que_img.setImageDrawable(drawable)

//                    DownLoadImageTask(page_img_que_img).execute("https://www.jagranjosh.com/imported/images/E/Articles/JEE_integrals.jpg");

//                    page_img_que_img.setImageDrawable(activity!!.resources.getDrawable(R.drawable.pic))

                    sortDialog.dismiss()

                    Log.d("imgcall", "Number of movies received: " + movies.size)
                } else {
                    sortDialog.dismiss()
                    Toast.makeText(activity!!, "No Question at that time", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<QuestionResponse>, t: Throwable) {
                // Log error here since request failed
                Log.e("", t.toString())
                sortDialog.dismiss()
            }
        })
    }

    private inner class DownLoadImageTask(internal var imageView: ImageView) : AsyncTask<String, Void, Drawable>() {

        /*
            doInBackground(Params... params)
                Override this method to perform a computation on a background thread.
         */

        override fun doInBackground(vararg urls: String): Drawable? {
            val urlOfImage = urls[0]
            var logo: Bitmap? = null
            var drawable: Drawable? = null
            try {
                val `is` = URL(urlOfImage).openStream()
                /*
                    decodeStream(InputStream is)
                        Decode an input stream into a bitmap.
                 */
                logo = BitmapFactory.decodeStream(`is`)

                drawable = BitmapDrawable(resources, logo)
            } catch (e: Exception) { // Catch the download exception
                e.printStackTrace()
            }

            return drawable
        }

        /*
            onPostExecute(Result result)
                Runs on the UI thread after doInBackground(Params...).
         */

        override fun onPostExecute(result: Drawable) {
            imageView.setImageDrawable(result)
        }
    }

}
