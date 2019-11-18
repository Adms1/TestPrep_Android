package com.testprep.fragments

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import com.testprep.R
import com.testprep.activity.DashboardActivity
import com.testprep.activity.DashboardActivity.Companion.rlFilter
import com.testprep.activity.DashboardActivity.Companion.setFragments
import com.testprep.activity.DashboardActivity.Companion.testid
import com.testprep.activity.FilterActivity
import com.testprep.activity.NewActivity
import com.testprep.adapter.MyPackageAdapter
import com.testprep.carouselPkg.CarouselParameters
import com.testprep.carouselPkg.CarouselView1
import com.testprep.carouselPkg.Metrics
import com.testprep.models.GetMarketPlaceData
import com.testprep.models.PackageData
import com.testprep.retrofit.WebClient
import com.testprep.retrofit.WebInterface
import com.testprep.utils.AppConstants
import com.testprep.utils.DialogUtils
import com.testprep.utils.Utils
import com.testprep.utils.WebRequests
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

    internal var lblSelectedIndex: TextView? = null

    companion object {
        var rv: RecyclerView? = null
    }

    var carousel: CarouselView1? = null
    var carousel1: CarouselView1? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

//        callFilterListApi()
//        callTutorsListApi()

        val vieww = inflater.inflate(R.layout.fragment_market_place, container, false)

        activity!!.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        DashboardActivity.main_header!!.text = "Market Place"
        DashboardActivity.btnBack!!.visibility = View.GONE

        return vieww
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rlFilter!!.visibility = View.GONE

        rlCoverflow!!.setPadding(35, 0, 35, 0)
        rlCoverflow!!.clipToPadding = false
        rlCoverflow!!.pageMargin = 1

//        AppConstants.ON_BACK = 1

//        mDataList!!.add(PackageData.PackageDataList(R.drawable.pp_1, "Packagies"))
//        tutorList!!.add(PackageData.PackageDataList(R.drawable.pp_1, "Packages"))

//        mTitle = view.findViewById(R.id.title) as TextSwitcher
//        mTitle!!.setFactory {
//            val inflater = LayoutInflater.from(activity!!)
//            inflater.inflate(R.layout.item_title, null) as TextView
//        }
//        val `in` = AnimationUtils.loadAnimation(activity, R.anim.slide_in_top)
//        val out = AnimationUtils.loadAnimation(activity, R.anim.slide_out_bottom)
//        mTitle!!.inAnimation = `in`
//        mTitle!!.outAnimation = out

        rv = view.findViewById(R.id.rvPkgs)

        main_pkg_item_rvSingleTest.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        main_pkg_item_tvChange.setOnClickListener {

            val intent = Intent(activity, NewActivity::class.java)
            startActivity(intent)
            activity!!.finish()
        }

        rlFilter!!.setOnClickListener {
            val intent = Intent(activity!!, FilterActivity::class.java)
            startActivityForResult(intent, 101)
        }

        AppConstants.FILTER_STANDARD_ID = "0"
        AppConstants.FILTER_SUBJECT_ID = "0"
        AppConstants.FILTER_TUTOR_ID = "0"
        AppConstants.FILTER_BOARD_ID = "0"
        AppConstants.FILTER_FROM_PRICE = "0"
        AppConstants.FILTER_TO_PRICE = "5000"

        main_freetest_item_tvSeeall.setOnClickListener {

            AppConstants.isFirst = 13
            val bundle = Bundle()
            bundle.putString("type", "free")
            bundle.putString("pname1", "Packages")
            bundle.putString("filtertypeid", "0")
            bundle.putString(
                "boardid",
                Utils.getStringValue(activity!!, AppConstants.COURSE_ID, "0")!!
            )
            bundle.putString(
                "course_type",
                Utils.getStringValue(activity!!, AppConstants.COURSE_TYPE_ID, "1")
            )
            bundle.putString(
                "stdid",
                Utils.getStringValue(activity!!, AppConstants.STANDARD_ID, "0")!!
            )
            bundle.putString(
                "subid",
                Utils.getStringValue(activity!!, AppConstants.SUBJECT_ID, "0")!!
            )
            bundle.putString("tutorid", "")
            bundle.putString("maxprice", "")
            bundle.putString("minprice", "")
            bundle.putString("search_name", "")
            setFragments(bundle)

        }

        main_pkg_item_tvSeeall.setOnClickListener {

            AppConstants.isFirst = 13
            val bundle = Bundle()
            bundle.putString("type", "pkg")
            bundle.putString("pname1", "Packages")
            bundle.putString("filtertypeid", "1")
            bundle.putString(
                "boardid",
                Utils.getStringValue(activity!!, AppConstants.COURSE_ID, "0")!!
            )
            bundle.putString(
                "course_type",
                Utils.getStringValue(activity!!, AppConstants.COURSE_TYPE_ID, "1")
            )
            bundle.putString(
                "stdid",
                Utils.getStringValue(activity!!, AppConstants.STANDARD_ID, "0")!!
            )
            bundle.putString(
                "subid",
                Utils.getStringValue(activity!!, AppConstants.SUBJECT_ID, "0")!!
            )
            bundle.putString("tutorid", "")
            bundle.putString("maxprice", "")
            bundle.putString("minprice", "")
            bundle.putString("search_name", "")
            setFragments(bundle)

        }

        main_pkg_item_tvTSeeall.setOnClickListener {
            AppConstants.isFirst = 13
            val bundle = Bundle()
            bundle.putString("type", "tutor")
            bundle.putString("pname1", "Tutors")
            bundle.putString("filtertypeid", "2")
            bundle.putString(
                "course_type",
                Utils.getStringValue(activity!!, AppConstants.COURSE_TYPE_ID, "1")
            )
            bundle.putString("boardid", "")
            bundle.putString("stdid", "")
            bundle.putString("subid", "")
            bundle.putString("tutorid", "")
            bundle.putSerializable("parr", tutorList)
            bundle.putString("maxprice", "")
            bundle.putString("minprice", "")
            bundle.putString("search_name", "")
            setFragments(bundle)
        }

        main_pkg_item_tvSSeeall.setOnClickListener {
            AppConstants.isFirst = 13
            val bundle = Bundle()
            bundle.putString("type", "single")
            bundle.putString("pname1", "Single Test")
            bundle.putString("filtertypeid", "3")
            bundle.putString(
                "boardid",
                Utils.getStringValue(activity!!, AppConstants.COURSE_ID, "0")!!
            )
            bundle.putString(
                "course_type",
                Utils.getStringValue(activity!!, AppConstants.COURSE_TYPE_ID, "1")
            )
            bundle.putString(
                "stdid",
                Utils.getStringValue(activity!!, AppConstants.STANDARD_ID, "0")!!
            )
            bundle.putString(
                "subid",
                Utils.getStringValue(activity!!, AppConstants.SUBJECT_ID, "0")!!
            )
            bundle.putString("tutorid", "")
            bundle.putString("maxprice", "")
            bundle.putString("minprice", "")
            bundle.putString("search_name", "")
            setFragments(bundle)
        }

        mp_view_pager!!.addOnPageChangeListener(introViewPagerListener)
//        //new carousel library
        carousel = view.findViewById(R.id.carousel) as CarouselView1
////        val rootView = layoutInflater.inflate(R.layout.fragment_main, vg, false) as View
//
        carousel1 = view.findViewById(R.id.carousel1) as CarouselView1
//        lblSelectedIndex = findViewById<View>(R.id.lblSelectedIndex) as TextView
        val lp = carousel!!.layoutParams
        //		https://github.com/davidschreiber/FancyCoverFlow.git
        lp.width = Metrics.convertDpToPixel(400f, activity!!).toInt()
        lp.height = Metrics.convertDpToPixel(400f, activity!!).toInt()
        carousel!!.layoutParams = lp
        carousel!!.requestLayout()
//
        val isClipChildren = false
        carousel!!.clipChildren = isClipChildren
        (carousel!!.parent as ViewGroup).clipChildren = isClipChildren
        (carousel!!.parent as ViewGroup).clipToPadding = isClipChildren

        carousel!!.isInfinite = true
        carousel!!.extraVisibleChilds = 0
        carousel!!.layoutManager!!.setDrawOrder(CarouselView1.DrawOrder.values()[1])

        carousel!!.gravity = 17

        carousel!!.isScrollingAlignToViews = true

        val lp1 = carousel1!!.layoutParams
        //		https://github.com/davidschreiber/FancyCoverFlow.git
        lp1.width = Metrics.convertDpToPixel(400f, activity!!).toInt()
        lp1.height = Metrics.convertDpToPixel(400f, activity!!).toInt()
        carousel1!!.layoutParams = lp
        carousel1!!.requestLayout()

        carousel1!!.clipChildren = isClipChildren
        (carousel1!!.parent as ViewGroup).clipChildren = isClipChildren
        (carousel1!!.parent as ViewGroup).clipToPadding = isClipChildren

        carousel1!!.isInfinite = true
        carousel1!!.extraVisibleChilds = 0
        carousel1!!.layoutManager!!.setDrawOrder(CarouselView1.DrawOrder.values()[1])

        carousel1!!.gravity = 17

        carousel1!!.isScrollingAlignToViews = true

//        carousel!!.setOnItemSelectedListener(object : CarouselView1.OnItemSelectedListener{
//            override fun onItemSelected(
//                carouselView: CarouselView1,
//                position: Int,
//                adapterPosition: Int,
//                adapter: RecyclerView.Adapter<*>
//            ) {
//                Toast.makeText(
//                    activity,
//                    "Selected Position $position",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//
//            override fun onItemDeselected(
//                carouselView: CarouselView1,
//                position: Int,
//                adapterPosition: Int,
//                adapter: RecyclerView.Adapter<*>
//            ) {
//
//            }
//        })

//        carousel!!.setOnItemSelectedListener(object : CarouselView1.OnItemSelectedListener {
//            override fun onItemSelected(
//                carouselView: CarouselView1,
//                position: Int,
//                adapterPosition: Int,
//                adapter: RecyclerView.Adapter<*>
//            ) {
//                val intent = Intent(activity, PackageDetailActivity::class.java)
//            intent.putExtra("pkgid", "1")
//            startActivity(intent)
//
//                lblSelectedIndex!!.text = "Selected Position $position"
//
//                Toast.makeText(
//                    activity,
//                    "Selected Position $position",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//
//            override fun onItemDeselected(
//                carouselView: CarouselView1,
//                position: Int,
//                adapterPosition: Int,
//                adapter: RecyclerView.Adapter<*>
//            ) {
//
//            }
//        })
//
//        carousel!!.setOnItemSelectedListener() { adapter, view, position, adapterPosition ->
//
//            val intent = Intent(activity, PackageDetailActivity::class.java)
//            intent.putExtra("pkgid", "1")
//            startActivity(intent)
//
//        }

        val hashMap = HashMap<String, Number>()
        hashMap["numPies"] = 5
        hashMap["farScale"] = 0
        hashMap["viewPerspective"] = 0.1
        hashMap["horizontalViewPort"] = 0.75
        hashMap["farAlpha"] = 0

        val transformerSelectedPos = 1
        var transformer: CarouselView1.ViewTransformer? = null
        if (transformerSelectedPos < CarouselParameters.TRANSFORMER_CLASSES.size) {
            // built-in transformer
            transformer = CarouselParameters.createTransformer<CarouselView1.ViewTransformer>(
                CarouselParameters.TRANSFORMER_CLASSES[1],
                hashMap
            )
        }

        carousel!!.transformer = transformer
        carousel1!!.transformer = transformer

        carousel!!.post {
            // smooth scroll to the 'centermost' item
            carousel!!.smoothScrollToPosition((5 - 1) / 2)
        }

        carousel1!!.post {
            // smooth scroll to the 'centermost' item
            carousel1!!.smoothScrollToPosition((5 - 1) / 2)
        }

        callFilterListApi()
//        callTutorsListApi()
    }

    class PkgPageAdapter(
        internal var size: Int,
        internal var width: Int,
        internal var height: Int,
        internal var context: Context,
        var mDataList: java.util.ArrayList<PackageData.PackageDataList>
    ) : RecyclerView.Adapter<PkgPageAdapter.viewholder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {

            val view =
                LayoutInflater.from(context).inflate(R.layout.carousel_container, parent, false)
            return viewholder(view)
        }

        override fun onBindViewHolder(holder: viewholder, position: Int) {
//                RandomPageFragment.initializeTextView(holder.textView!!, position + 1)

//            images\\package/package_default.png
//            images\\package/1567672611.png

            if (mDataList[position].Icon != null) {

                Picasso.get()
                    .load(AppConstants.IMAGE_BASE_URL + mDataList[position].Icon)
                    .into(holder.title)

            }

            holder.title1.text = mDataList[position].TestPackageName

            holder.title.setOnClickListener {

                //                AppConstants.isBackFirst = 0

                AppConstants.isFirst = 14
                val bundle = Bundle()
                bundle.putString("pkgid", testid)
                bundle.putString("come_from", "selectpackage")
                setFragments(bundle)

//                val intent = Intent(context, PackageDetailActivity::class.java)
//                intent.putExtra("pkgid", mDataList[position].TestPackageID)
//                intent.putExtra("pname", mDataList[position].TestPackageName)
//                intent.putExtra("sprice", mDataList[position].TestPackageSalePrice)
//                intent.putExtra("lprice", mDataList[position].TestPackageListPrice)
//                intent.putExtra("desc", mDataList[position].TestPackageDescription)
//                intent.putExtra("test_type_list", mDataList[position].TestType)
//                if (mDataList[position].InstituteName != "" && mDataList[position].InstituteName != null) {
//                    intent.putExtra("created_by", mDataList[position].InstituteName)
//                } else {
//                    intent.putExtra("created_by", mDataList[position].TutorName)
//                }
//                intent.putExtra("tutor_id", mDataList[position].TutorID)
//                intent.putExtra("come_from", "selectpackage")
//                intent.putExtra("position", mDataList[position].TestPackageName.substring(0, 1).single())
//                context.startActivity(intent)
            }

        }

        override fun getItemCount(): Int {
            return mDataList.size
        }

        class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            var title: ImageView = itemView.findViewById(R.id.img)
            var title1: TextView = itemView.findViewById(R.id.title11)

        }


//            class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//                @Bind(R.id.img)
//                var textView: ImageView? = null
//
////                init {
////                    ButterKnife.bind(this, itemView)
//                }
//            }

    }

    class TutorPageAdapter(
        internal var size: Int,
        internal var width: Int,
        internal var height: Int,
        internal var context: Context,
        var mDataList: java.util.ArrayList<PackageData.PackageDataList>
    ) : RecyclerView.Adapter<TutorPageAdapter.viewholder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {

            val view =
                LayoutInflater.from(context).inflate(R.layout.carousel_container, parent, false)
            return viewholder(view)
        }

        override fun onBindViewHolder(holder: viewholder, position: Int) {
//                RandomPageFragment.initializeTextView(holder.textView!!, position + 1)

            if (mDataList[position].Icon != null) {
                Picasso.get()
                    .load(AppConstants.IMAGE_BASE_URL + mDataList[position].Icon)
                    .into(holder.title)
            }

            holder.title1.text = mDataList[position].TutorName
//            holder.title.setImageDrawable(context.resources.getDrawable(R.drawable.pro_pic1))

            holder.title.setOnClickListener {

                AppConstants.isFirst = 15
                var bundle: Bundle = Bundle()
                bundle.putString("tutor_id", mDataList[position].TutorID)
                setFragments(bundle)

//                val intent = Intent(context, TutorProfileFragment::class.java)
//                intent.putExtra("tutor_id", mDataList[position].TutorID)
//                context.startActivity(intent)
            }

        }

        override fun getItemCount(): Int {
            return mDataList.size
        }

        class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            var title: ImageView = itemView.findViewById(R.id.img)
            var title1: TextView = itemView.findViewById(R.id.title11)

        }

    }

    private var introViewPagerListener: ViewPager.OnPageChangeListener =
        object : ViewPager.OnPageChangeListener {

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

    inner class MyViewPagerAdapter(var arrList: ArrayList<PackageData.PackageDataList>) :
        PagerAdapter() {
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val layoutInflater: LayoutInflater = LayoutInflater.from(activity)
            val view = layoutInflater.inflate(R.layout.slider_item_layout, container, false)

            var iv: ImageView = view.findViewById(R.id.imageView)
//            var tv: TextView = view.findViewById(R.id.testName)
            var btn: Button = view.findViewById(R.id.btnBuy)

            iv.setOnClickListener {

                //                AppConstants.isBackFirst = 0

                AppConstants.isFirst = 14
                val bundle = Bundle()
                bundle.putString("pkgid", arrList[position].TestPackageID)
                bundle.putString("come_from", "selectpackage")
                setFragments(bundle)

//                val intent = Intent(context, PackageDetailActivity::class.java)
//                intent.putExtra("pkgid", arrList[position].TestPackageID)
//                intent.putExtra("tutor_id", arrList[position].TutorID)
//                intent.putExtra("come_from", "selectpackage")
//                context!!.startActivity(intent)

//                callAddTestPackageApi(arrList[position].TestPackageID)
            }

            iv.setImageResource(R.drawable.free_test)
//            tv.text = arrList[position].TestPackageName

            container.addView(view)
            return view
        }

        override fun getCount(): Int {
            return arrList.size
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

        val call = apiService.getPackage(
            WebRequests.getPackageParams(
                Utils.getStringValue(activity!!, AppConstants.COURSE_TYPE_ID, "0")!!,
                Utils.getStringValue(activity!!, AppConstants.COURSE_ID, "0")!!,
                Utils.getStringValue(activity!!, AppConstants.STANDARD_ID, "0")!!,
                Utils.getStringValue(activity!!, AppConstants.SUBJECT_ID, "0")!!,
                Utils.getStringValue(activity!!, AppConstants.USER_ID, "0")!!
            )
        )
        call.enqueue(object : Callback<GetMarketPlaceData> {
            override fun onResponse(
                call: Call<GetMarketPlaceData>,
                response: Response<GetMarketPlaceData>
            ) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.Status == "true") {

                        main_pkg_item_tvdatanotfound!!.visibility = View.GONE
                        main_pkg_item_mainll!!.visibility = View.VISIBLE
                        main_pkg_item_tvChange.visibility = View.GONE

                        mDataList = ArrayList()
                        mDataList = response.body()!!.data.TestPackage
                        var mSingleDataList = response.body()!!.data.SingleTestPackage
                        var freeTestList = response.body()!!.data.FreeTestPackage

                        Log.d("dsize", "" + mDataList!!.size)

//                        mAdapter!!.setData(mDataList!!)
//                        coverflow.adapter = mAdapter
                        tutorList = response.body()!!.data.Tutors

                        Log.d("dtsize", "" + tutorList!!.size)

                        if (tutorList!!.size > 0) {

                            main_pkg_item_rlTutor.visibility = View.VISIBLE
                            rltutorCoverflow.visibility = View.VISIBLE

                            carousel1!!.adapter =
                                TutorPageAdapter(5, 330, 160, activity!!, tutorList!!)

                        } else {

                            main_pkg_item_rlTutor.visibility = View.GONE
                            rltutorCoverflow.visibility = View.GONE

                        }

                        if (mSingleDataList.size > 0) {

//                            AppConstants.isBackFirst = 0

                            main_pkg_item_rvSingleTest.visibility = View.VISIBLE
                            main_pkg_item_tvSingleTest.visibility = View.VISIBLE
                            main_pkg_item_tvSSeeall.visibility = View.VISIBLE

                            main_pkg_item_rvSingleTest.adapter =
                                MyPackageAdapter(activity!!, mSingleDataList, "market_place")
                        } else {
                            main_pkg_item_rvSingleTest.visibility = View.GONE
                            main_pkg_item_tvSingleTest.visibility = View.GONE
                            main_pkg_item_tvSSeeall.visibility = View.GONE
                        }

                        if (freeTestList.size > 0) {

                            mp_view_pager.visibility = View.VISIBLE
                            main_freetest_item_tvSeeall.visibility = View.VISIBLE

                            myViewPagerAdapter = MyViewPagerAdapter(freeTestList)
                            mp_view_pager!!.adapter = myViewPagerAdapter

                        } else {
                            mp_view_pager.visibility = View.GONE
                            main_freetest_item_tvSeeall.visibility = View.GONE

                        }

                        if (mDataList!!.size > 0) {

                            rlCoverflow!!.visibility = View.VISIBLE
                            rlCoverflow1.visibility = View.VISIBLE
                            main_pkg_item_tvSeeall.visibility = View.VISIBLE
                            main_pkg_item_tvCategory.visibility = View.VISIBLE

                            carousel!!.adapter =
                                PkgPageAdapter(5, 330, 160, activity!!, mDataList!!)

//                            rvPkgs.layoutManager =
//                                LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//                            rvPkgs.adapter = MarketPlacePkgAdapter(activity!!, mDataList!!)
                            rlCoverflow!!.adapter = MyViewPagerAdapter1(mDataList!!)

                        } else {
                            rlCoverflow!!.visibility = View.GONE
                            rlCoverflow1.visibility = View.GONE
                            main_pkg_item_tvSeeall.visibility = View.GONE
                            main_pkg_item_tvCategory.visibility = View.GONE
                        }

                    } else {

                        main_pkg_item_tvdatanotfound.visibility = View.VISIBLE
                        main_pkg_item_tvChange.visibility = View.VISIBLE
                        main_pkg_item_mainll.visibility = View.GONE

                        Toast.makeText(activity!!, response.body()!!.Msg, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<GetMarketPlaceData>, t: Throwable) {
                // Log error here since request failed
                Log.e("", t.toString())
                DialogUtils.dismissDialog()
            }
        })
    }

//    fun callFilterListApi() {
//
//        if (!DialogUtils.isNetworkConnected(activity!!)) {
//            Utils.ping(activity!!, "Connetion not available")
//        }
//
//        DialogUtils.showDialog(activity!!)
//        val apiService = WebClient.getClient().create(WebInterface::class.java)
//
//        val call = apiService.getFilterData(
//            WebRequests.getFilterParams(
//                Utils.getStringValue(activity!!, AppConstants.COURSE_TYPE_ID, "")!!,
//                "",
//                Utils.getStringValue(activity!!, AppConstants.COURSE_ID, "")!!,
//                Utils.getStringValue(activity!!, AppConstants.STANDARD_ID, "")!!,
//                Utils.getStringValue(activity!!, AppConstants.SUBJECT_ID, "")!!,
//                Utils.getStringValue(activity!!, AppConstants.TUTOR_ID, "")!!,
//                "",
//                ""
//            )
//        )
//
////        val call = apiService.getFilterData(
////            WebRequests.getFilterParams(
////                Utils.getStringValue(activity!!, AppConstants.COURSE_TYPE_ID, "")!!,
////                "",
////                examids,
////                stdids,
////                subids,
////                tutorids,
////                "",
////                ""
////            )
////        )
//
//        call.enqueue(object : Callback<PackageData> {
//            override fun onResponse(call: Call<PackageData>, response: Response<PackageData>) {
//
//                if (response.body() != null) {
//
//                    DialogUtils.dismissDialog()
//
//                    if (response.body()!!.Status == "true") {
//
//                        mDataList = ArrayList()
//                        mDataList = response.body()!!.data
//
//                        Log.d("dsize", "" + mDataList!!.size)
//
////                        mAdapter!!.setData(mDataList!!)
////                        coverflow.adapter = mAdapter
//
//                        carousel!!.adapter = PkgPageAdapter(5, 330, 160, activity!!, mDataList!!)
//
//                    } else {
//
//                        Toast.makeText(activity!!, response.body()!!.Msg, Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<PackageData>, t: Throwable) {
//                // Log error here since request failed
//                Log.e("", t.toString())
//                DialogUtils.dismissDialog()
//            }
//        })
//    }

//    fun callTutorsListApi() {
//
//        if (!DialogUtils.isNetworkConnected(activity!!)) {
//            Utils.ping(activity!!, "Connetion not available")
//        }
//
//        DialogUtils.showDialog(activity!!)
//        val apiService = WebClient.getClient().create(WebInterface::class.java)
//
//        val call = apiService.getTutorList()
//
//        call.enqueue(object : Callback<PackageData> {
//            override fun onResponse(call: Call<PackageData>, response: Response<PackageData>) {
//
//                if (response.body() != null) {
//
//                    DialogUtils.dismissDialog()
//
//                    if (response.body()!!.Status == "true") {
//
//                        tutorList = ArrayList()
//                        tutorList = response.body()!!.data
//
//                        Log.d("dsize", "" + tutorList!!.size)
//                        carousel1!!.adapter = TutorPageAdapter(5, 330, 160, activity!!, tutorList!!)
////                        mAdapterr!!.setDataa(tutorList!!)
////                        tutorcoverflow.adapter = mAdapterr
//
//                    } else {
//
//                        Toast.makeText(activity!!, response.body()!!.Msg, Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<PackageData>, t: Throwable) {
//                // Log error here since request failed
//                Log.e("", t.toString())
//                DialogUtils.dismissDialog()
//            }
//        })
//    }

    class MarketPlacePkgAdapter(
        val context: Context,
        val dataList: ArrayList<PackageData.PackageDataList>
    ) :
        RecyclerView.Adapter<MarketPlacePkgAdapter.viewholder>() {

        var row_index = -1

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): viewholder {

            val view = LayoutInflater.from(p0.context)
                .inflate(R.layout.marketplace_pkg_list_item, p0, false)
//            val width = rv!!.width
//            val params = view.layoutParams
//            params.width = (width * 0.9).toInt()
//            view.layoutParams = params
            return viewholder(view)

//            return viewholder(


//            LayoutInflater.from(context).inflate(R.layout.list_item_test_package, p0, false)
//                LayoutInflater.from(context).inflate(R.layout.marketplace_pkg_list_item, p0, false)
//            )
        }

        override fun getItemCount(): Int {
            return dataList.size

        }

        override fun onBindViewHolder(p0: viewholder, p1: Int) {

            if (dataList != null && dataList.size > 0) {
                p0.std.text = dataList[p1].TestPackageName
                p0.sub.text = dataList[p1].SubjectName
                p0.price.text = "Created by " + dataList[p1].TutorName

                if (dataList[p1].Icon != null) {
                    Picasso.get().load(AppConstants.IMAGE_BASE_URL + dataList[p1].Icon)
                        .into(p0.image)
                }

//        p0.image.setImageDrawable(Utils.newcreateDrawable(dataList[p1].TestPackageName.substring(0, 1)))

//            if (dataList[p1].InstituteName != "" && dataList[p1].InstituteName != null) {
//                p0.createdby.text =
//                    Html.fromHtml("created by " + "<font color=\"#3ea7e0\">" + dataList[p1].InstituteName + "</font>")
//            } else {
//                p0.createdby.text =
//                    Html.fromHtml("created by " + "<font color=\"#3ea7e0\">" + dataList[p1].TutorName + "</font>")
//            }
//        p0.stitle.text = dataList[p1].TestPackageName

                p0.tvBuy.setOnClickListener {

                    //                val intent = Intent(context, PackageDetailActivity::class.java)
//                intent.putExtra("pkgid", dataList[p1].TestPackageID)
//                intent.putExtra("pname", dataList[p1].TestPackageName)
//                intent.putExtra("sprice", dataList[p1].TestPackageSalePrice)
//                intent.putExtra("lprice", dataList[p1].TestPackageListPrice)
//                intent.putExtra("desc", dataList[p1].TestPackageDescription)
//                intent.putExtra("test_type_list", dataList[p1].TestType)
//                intent.putExtra("come_from", "selectpackage")
//                intent.putExtra("position", dataList[p1].TestPackageName.substring(0, 1).single())
//                context.startActivity(intent)

                    DialogUtils.createConfirmDialog(
                        context,
                        "",
                        "Are you sure you want to buy this package?",
                        "Yes",
                        "No",
                        DialogInterface.OnClickListener { dialog, which ->
                            callAddTestPackageApi(dataList[p1].TestPackageID)

                        },
                        DialogInterface.OnClickListener { dialog, which ->
                            dialog.dismiss()


                        }).show()

                }

                p0.mainll.setOnClickListener {

                    //                    AppConstants.isBackFirst = 0

                    AppConstants.isFirst = 14
                    val bundle = Bundle()
                    bundle.putString("pkgid", dataList[p1].TestPackageID)
                    bundle.putString("come_from", "selectpackage")
                    setFragments(bundle)

//                    val intent = Intent(context, PackageDetailActivity::class.java)
//                    intent.putExtra("pkgid", dataList[p1].TestPackageID)
//                    intent.putExtra("tutor_id", dataList[p1].TutorID)
//                    intent.putExtra("come_from", "selectpackage")
//                    context.startActivity(intent)
                }
            }
        }

        class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            var image: ImageView = itemView.findViewById(R.id.package_item_ivImage)
//        var title: TextView = itemView.findViewById(R.id.package_name)
//        var stitle: TextView = itemView.findViewById(R.id.package_name_short)
//        var p_select: ImageView = itemView.findViewById(R.id.package_select)

            var std: TextView = itemView.findViewById(R.id.package_item_tvStd)
            var sub: TextView = itemView.findViewById(R.id.package_item_tvSub)
            var price: TextView = itemView.findViewById(R.id.package_item_tvPrice)
            var mainll: ConstraintLayout = itemView.findViewById(R.id.mall)
            var tvBuy: ImageView = itemView.findViewById(R.id.package_item_ivCart)
//        var createdby: TextView = itemView.findViewById(R.id.testpkg_item_tvCreated)
        }

        fun callAddTestPackageApi(pkgid: String) {

            if (!DialogUtils.isNetworkConnected(context)) {
                Utils.ping(context, "Connetion not available")
            }

            DialogUtils.showDialog(context)
            val apiService = WebClient.getClient().create(WebInterface::class.java)

            val call = apiService.addTestPackage(
                Utils.getStringValue(context, AppConstants.USER_ID, "0")!!,
                pkgid
            )
            call.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                    if (response.body() != null) {

                        DialogUtils.dismissDialog()

                        if (response.body()!!["Status"].toString() == "true") {

//                        Toast.makeText(
//                            context,
//                            response.body()!!["Msg"].toString().replace("\"", ""),
//                            Toast.LENGTH_SHORT
//                        ).show()
//                        onBackPressed()
                        } else {

//                        Toast.makeText(
//                            context,
//                            response.body()!!["Msg"].toString().replace("\"", ""),
//                            Toast.LENGTH_SHORT
//                        ).show()
                        }
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    // Log error here since request failed
                    Log.e("", t.toString())
                    DialogUtils.dismissDialog()
                }
            })
        }

    }

    //new market place

    inner class MyViewPagerAdapter1(var arrList: ArrayList<PackageData.PackageDataList>) :
        PagerAdapter() {
        override fun instantiateItem(container: ViewGroup, position: Int): Any {

            val layoutInflater: LayoutInflater = LayoutInflater.from(activity)
            val view = layoutInflater.inflate(R.layout.marketplace_pkg_list_item, container, false)

            val image: ImageView = view.findViewById(R.id.package_item_ivImage)
            val imagebg: ImageView = view.findViewById(R.id.package_item_ivBg)

            val pkgname: TextView = view.findViewById(R.id.package_item_tvPkgname)
            val sub: TextView = view.findViewById(R.id.package_item_tvSub)
            val created: TextView = view.findViewById(R.id.package_item_tvCreated)
            val price: TextView = view.findViewById(R.id.package_item_tvPrice)
            val mainll: ConstraintLayout = view.findViewById(R.id.mall)

            if (arrList.size > 0) {
                pkgname.text = arrList[position].TestPackageName
                sub.text = arrList[position].SubjectName
                created.text = "Created by " + arrList[position].TutorName
                price.text = arrList[position].TestPackageSalePrice

                if (arrList[position].Icon != null) {
                    Picasso.get().load(AppConstants.IMAGE_BASE_URL + arrList[position].Icon)
                        .into(image)

                    Picasso.get().load(AppConstants.IMAGE_BASE_URL + arrList[position].Icon)
                        .into(imagebg)

//                    AsyncCaller(image, arrList[position].Icon).execute()
//                    BlurImage.with(getApplicationContext()).load(R.drawable.pp_1).intensity(20F).Async(true).into(image)
//                    arrList[position].Icon

                }

                mainll.setOnClickListener {

                    //                    AppConstants.isBackFirst = 0

                    AppConstants.isFirst = 14
                    val bundle = Bundle()
                    bundle.putString("pkgid", arrList[position].TestPackageID)
                    bundle.putString("come_from", "selectpackage")
                    setFragments(bundle)

//                    val intent = Intent(context, PackageDetailActivity::class.java)
//                    intent.putExtra("pkgid", arrList[position].TestPackageID)
//                    intent.putExtra("tutor_id", arrList[position].TutorID)
//                    intent.putExtra("come_from", "selectpackage")
//                    startActivity(intent)
                }
            }

            container.addView(view)
            return view
        }

        override fun getCount(): Int {
            return arrList.size
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

//    private class AsyncCaller(imv: ImageView, url: String): AsyncTask<Object, Void, Bitmap>() {
//
//        var imv: ImageView = imv
//        var path: String = url
//
//        override fun doInBackground(vararg params: Object?): Bitmap {
//            var image1: Bitmap? = null
//
//            try {
//                val url = URL(AppConstants.IMAGE_BASE_URL + path)
//                image1 = BitmapFactory.decodeStream(url.openConnection().getInputStream())
//
//            } catch (e: IOException) {
//                println(e)
//            }
//
//            return image1!!
//        }
//
//        override fun onPostExecute(result: Bitmap?) {
//
//            if(result != null){
//                BlurImage.with(getApplicationContext()).load(result).intensity(20F).Async(true).into(imv)
//            }
//        }
//    }

}
