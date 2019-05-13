package com.testprep.old


import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import com.squareup.picasso.Picasso
import com.testprep.R
import com.testprep.old.PageActivity.Companion.countt
import com.testprep.old.adapter.SelectImageOptionAdapter
import com.testprep.old.models.QuestionResponse
import com.testprep.old.retrofit.ApiClient
import com.testprep.old.retrofit.ApiInterface
import kotlinx.android.synthetic.main.fragment_page_view.*
import kotlinx.android.synthetic.main.fragment_webview.wv_question_list
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URL

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class PageViewFragment : Fragment() {

    companion object {

        var qsize = 0
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_page_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        var bundle = arguments
//        var posi = bundle!!.getInt("posi", 0)

        wv_question_list.isNestedScrollingEnabled = false

        callQuestionApi()

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

        val apiService = ApiClient.getClient().create(ApiInterface::class.java)

        val call = apiService.getTopRatedMovies("t1506-o2506-u3506-r4506")
        call.enqueue(object : Callback<QuestionResponse> {
            override fun onResponse(call: Call<QuestionResponse>, response: Response<QuestionResponse>) {

                if (response.body()!!.message == "Success") {
                    val movies = response.body()!!.data

                    totall.text = "Total" + movies.size

                    qno.text = "Q." + (countt+1)

                    Log.d("qid", ""+movies[0].id)

                    if(countt >= 0) {
                        if ("http://content.testcraft.co.in/question/" + movies[0].titleimg != "") {

//                            var url = URL("http://content.testcraft.co.in/question/" + movies[pos].titleimg)
//                            var bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())
//
//                            var widhtx  = bmp.width
//                            var heightx = bmp.height
//
//                            Log.d("imgsize", "widht" + widhtx + "  height" + heightx)

                                Picasso.get().load("http://content.testcraft.co.in/question/" + movies[0].titleimg)
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


                        wv_question_list.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                            wv_question_list.adapter = SelectImageOptionAdapter(
                                activity!!,
                                movies[0].mcq,
                                page_img_que_img.width
                            )
                        }
                    }else{
                        if ("http://content.testcraft.co.in/question/" + movies[0].titleimg != "") {
                            Picasso.get().load("http://content.testcraft.co.in/question/" + movies[0].titleimg)
                                .resize(page_img_que_img.width, page_img_que_img.height)
                                .into(page_img_que_img)

                        }

                        wv_question_list.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                        wv_question_list.adapter = SelectImageOptionAdapter(
                            activity!!,
                            movies[0].mcq,
                            page_img_que_img.width
                        )

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
