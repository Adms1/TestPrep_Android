package com.testprep.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Toast
import com.testprep.R
import com.testprep.activity.PackageDetailActivity
import com.testprep.activity.TutorDetailActivity
import com.testprep.adapter.CoverFlowAdapter2
import com.testprep.models.PackageData
import com.testprep.retrofit.WebClient
import com.testprep.retrofit.WebInterface
import com.testprep.utils.AppConstants
import com.testprep.utils.DialogUtils
import com.testprep.utils.Utils
import com.testprep.utils.WebRequests
import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow
import kotlinx.android.synthetic.main.fragment_market_place.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */

class MarketPlaceFragment : Fragment() {

    private var mDataList: ArrayList<PackageData.PackageDataList>? = ArrayList()
    private var tutorList: ArrayList<PackageData.PackageDataList>? = ArrayList()

    private var myViewPagerAdapter: MyViewPagerAdapter? = null
    private var layouts: IntArray? = null

    private var mAdapter: CoverFlowAdapter2? = null
    private var mAdapterr: CoverFlowAdapter2? = null
//    private var mTitle: TextSwitcher? = null
//    private var mCoverFlow: FeatureCoverFlow? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        callFilterListApi()
        callTutorsListApi()

        val vieww = inflater.inflate(R.layout.fragment_market_place, container, false)

        activity!!.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        return vieww
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        AppConstants.ON_BACK = 1

        layouts = intArrayOf(R.drawable.pp_2, R.drawable.pp_1, R.drawable.pp_2)

        mDataList!!.add(PackageData.PackageDataList(R.drawable.pp_1, "Packages"))
        tutorList!!.add(PackageData.PackageDataList(R.drawable.pp_1, "Packages"))

//        mTitle = view.findViewById(R.id.title) as TextSwitcher
//        mTitle!!.setFactory {
//            val inflater = LayoutInflater.from(activity!!)
//            inflater.inflate(R.layout.item_title, null) as TextView
//        }
        val `in` = AnimationUtils.loadAnimation(activity, R.anim.slide_in_top)
        val out = AnimationUtils.loadAnimation(activity, R.anim.slide_out_bottom)
//        mTitle!!.inAnimation = `in`
//        mTitle!!.outAnimation = out

        main_pkg_item_tvSeeall.setOnClickListener {
            val intent = Intent(context, TutorDetailActivity::class.java)
            intent.putExtra("type", "pkg")
            intent.putExtra("pname", "Packages")
            intent.putExtra("parr", mDataList)
            startActivity(intent)
        }

        main_pkg_item_tvTSeeall.setOnClickListener {
            val intent = Intent(context, TutorDetailActivity::class.java)
            intent.putExtra("type", "tutor")
            intent.putExtra("pname", "Tutors")
            intent.putExtra("parr", tutorList)
            startActivity(intent)
        }

        if (mDataList!!.size > 0) {

            mAdapter = CoverFlowAdapter2(activity!!, "pkg")
            mAdapterr = CoverFlowAdapter2(activity!!, "tutor")

            mAdapter!!.setData(mDataList!!)
            mAdapterr!!.setDataa(tutorList!!)

//            mCoverFlow = view.findViewById(R.id.coverflow)
            coverflow.adapter = mAdapter
            tutorcoverflow.adapter = mAdapterr

            coverflow.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
                val intent = Intent(context, PackageDetailActivity::class.java)
                intent.putExtra("pkgid", mDataList!![position].TestPackageID)
                intent.putExtra("pname", mDataList!![position].TestPackageName)
                intent.putExtra("sprice", mDataList!![position].TestPackageSalePrice)
                intent.putExtra("lprice", mDataList!![position].TestPackageListPrice)
                intent.putExtra("desc", mDataList!![position].TestPackageDescription)
                intent.putExtra("test_type_list", mDataList!![position].TestType)
                if (mDataList!![position].InstituteName != "" && mDataList!![position].InstituteName != null) {
                    intent.putExtra("created_by", mDataList!![position].InstituteName)
                } else {
                    intent.putExtra("created_by", mDataList!![position].TutorName)
                }
                intent.putExtra("tutor_id", mDataList!![position].TutorID)
                intent.putExtra("come_from", "selectpackage")
                intent.putExtra("position", mDataList!![position].TestPackageName.substring(0, 1).single())
                context!!.startActivity(intent)
            }

            coverflow.setOnScrollPositionListener(object : FeatureCoverFlow.OnScrollPositionListener {
                override fun onScrolledToPosition(position: Int) {
//                mTitle!!.setText(mDataList!![position].TestPackageName)
                }

                override fun onScrolling() {
//                    mTitle!!.setText("")
                }
            })

            tutorcoverflow.setOnScrollPositionListener(object : FeatureCoverFlow.OnScrollPositionListener {
                override fun onScrolledToPosition(position: Int) {
//                mTitle!!.setText(mDataList!![position].TestPackageName)
                }

                override fun onScrolling() {
//                    mTitle!!.setText("")
                }
            })
        }

        myViewPagerAdapter = MyViewPagerAdapter()
        mp_view_pager!!.adapter = myViewPagerAdapter
        mp_view_pager!!.addOnPageChangeListener(introViewPagerListener)

    }


    private var introViewPagerListener: ViewPager.OnPageChangeListener = object : ViewPager.OnPageChangeListener {

        override fun onPageSelected(position: Int) {
//            addBottomDots(position)
            /*Based on the page position change the button text*/

        }

        override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {
            //Do nothing for now
        }

        override fun onPageScrollStateChanged(arg0: Int) {
            //Do nothing for now
        }
    }

    inner class MyViewPagerAdapter : PagerAdapter() {
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val layoutInflater: LayoutInflater = LayoutInflater.from(activity)
            val view = layoutInflater.inflate(R.layout.slider_item_layout, container, false)

            var iv: ImageView = view.findViewById(R.id.imageView)

            iv.setImageResource(layouts!![position])

            container.addView(view)
            return view
        }

        override fun getCount(): Int {
            return 3
        }

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view === obj
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val view = `object` as View
            container.removeView(view)
        }

//        override fun getPageWidth(position: Int): Float {
//            return 0.6f
//        }

    }

    fun callFilterListApi() {

        if (!DialogUtils.isNetworkConnected(activity!!)) {
            Utils.ping(activity!!, "Connetion not available")
        }

        DialogUtils.showDialog(activity!!)
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getFilterData(
            WebRequests.getFilterParams(
                Utils.getStringValue(activity!!, AppConstants.COURSE_TYPE_ID, "")!!,
                "",
                Utils.getStringValue(activity!!, AppConstants.COURSE_ID, "")!!,
                Utils.getStringValue(activity!!, AppConstants.STANDARD_ID, "")!!,
                Utils.getStringValue(activity!!, AppConstants.SUBJECT_ID, "")!!,
                Utils.getStringValue(activity!!, AppConstants.TUTOR_ID, "")!!,
                "",
                ""
            )
        )

//        val call = apiService.getFilterData(
//            WebRequests.getFilterParams(
//                Utils.getStringValue(activity!!, AppConstants.COURSE_TYPE_ID, "")!!,
//                "",
//                examids,
//                stdids,
//                subids,
//                tutorids,
//                "",
//                ""
//            )
//        )

        call.enqueue(object : Callback<PackageData> {
            override fun onResponse(call: Call<PackageData>, response: Response<PackageData>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.Status == "true") {

                        mDataList = ArrayList()
                        mDataList = response.body()!!.data

                        Log.d("dsize", "" + mDataList!!.size)

                        mAdapter!!.setData(mDataList!!)
                        coverflow.adapter = mAdapter

                    } else {

                        Toast.makeText(activity!!, response.body()!!.Msg, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<PackageData>, t: Throwable) {
                // Log error here since request failed
                Log.e("", t.toString())
                DialogUtils.dismissDialog()
            }
        })
    }

    fun callTutorsListApi() {

        if (!DialogUtils.isNetworkConnected(activity!!)) {
            Utils.ping(activity!!, "Connetion not available")
        }

        DialogUtils.showDialog(activity!!)
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getTutorList()

        call.enqueue(object : Callback<PackageData> {
            override fun onResponse(call: Call<PackageData>, response: Response<PackageData>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.Status == "true") {

                        tutorList = ArrayList()
                        tutorList = response.body()!!.data

                        Log.d("dsize", "" + tutorList!!.size)

                        mAdapterr!!.setDataa(tutorList!!)
                        tutorcoverflow.adapter = mAdapterr

                    } else {

                        Toast.makeText(activity!!, response.body()!!.Msg, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<PackageData>, t: Throwable) {
                // Log error here since request failed
                Log.e("", t.toString())
                DialogUtils.dismissDialog()
            }
        })
    }


}
