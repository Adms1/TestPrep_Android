package com.testcraft.testcraft.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.squareup.picasso.Picasso
import com.testcraft.testcraft.R
import com.testcraft.testcraft.activity.DashboardActivity
import com.testcraft.testcraft.activity.DashboardActivity.Companion.rlFilter
import com.testcraft.testcraft.activity.DashboardActivity.Companion.setFragments
import com.testcraft.testcraft.activity.DashboardActivity.Companion.testid
import com.testcraft.testcraft.activity.FilterActivity
import com.testcraft.testcraft.activity.NewActivity
import com.testcraft.testcraft.activity.OtpActivity
import com.testcraft.testcraft.adapter.MyPackageAdapter
import com.testcraft.testcraft.adapter.TutorAdapter
import com.testcraft.testcraft.carouselPkg.CarouselParameters
import com.testcraft.testcraft.carouselPkg.CarouselView1
import com.testcraft.testcraft.carouselPkg.Metrics
import com.testcraft.testcraft.models.GetMarketPlaceData
import com.testcraft.testcraft.models.PackageData
import com.testcraft.testcraft.models.VerifyMobileData
import com.testcraft.testcraft.retrofit.WebClient
import com.testcraft.testcraft.retrofit.WebInterface
import com.testcraft.testcraft.utils.*
import kotlinx.android.synthetic.main.fragment_market_place.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("SetTextI18n")
class MarketPlaceFragment : Fragment() {

    private var mDataList: ArrayList<PackageData.PackageDataList>? = ArrayList()
    private var tutorList: ArrayList<PackageData.PackageDataList>? = ArrayList()

    //    private var myViewPagerAdapter: MyViewPagerAdapter? = null
    private var layouts: IntArray? = null

    internal var lblSelectedIndex: TextView? = null

    var free_test_banner = ""

    private var call: Call<GetMarketPlaceData>? = null

    companion object {
        var rv: RecyclerView? = null

        var bottomSheetFragment: MarketPlaceBottomSheetFragment? = null

        fun sheetClose() {

            AppConstants.isFirst = 1
            setFragments(null)

            bottomSheetFragment!!.dismiss()
        }
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

        if (Utils.getStringValue(activity!!, AppConstants.USER_ACCOUNT_TYPE, "") != "2"
            && Utils.getStringValue(activity!!, AppConstants.USER_ACCOUNT_TYPE, "") != "3") {

            if(Utils.getStringValue(activity!!, AppConstants.USER_MOBILE, "") == "") {
                val phndialog = Dialog(activity!!)
                phndialog.setContentView(R.layout.dialog_phone_number)
                phndialog.setCanceledOnTouchOutside(false)

                val phoneet: EditText =
                    phndialog.findViewById(R.id.phone)
                val btnv: TextView = phndialog.findViewById(R.id.vbtn)

                btnv.setOnClickListener {
                    if (TextUtils.isEmpty(phoneet.text.toString()) || !Patterns.PHONE.matcher(phoneet.text.toString()).matches() || phoneet.length() < 10
                    ) {
                        phoneet.error = "Please enter valid mobile number"
                    } else {
                        callVerifyAccountApi(phoneet.text.toString())
                    }
                }

                phndialog.show()
            }
        }

        return vieww
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CommonWebCalls.callToken(activity!!, "1", "", ActionIdData.C800, ActionIdData.T800)

        rlFilter!!.visibility = View.GONE

        rlCoverflow!!.setPadding(30, 0, 30, 0)
        rlCoverflow!!.clipToPadding = false
        rlCoverflow!!.pageMargin = 20

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

        rvTutor.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        main_pkg_item_tvChange.setOnClickListener {

            val intent = Intent(activity, NewActivity::class.java)
            intent.putExtra("comeadater", "changeprefrence")
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

        img_subscription.setOnClickListener {

//            val intent = Intent(activity!!, SubscriptionActivity::class.java)
//            startActivity(intent)

// click event for show-dismiss bottom sheet

//        if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
//            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//            btn_bottom_sheet.setText("Close sheet");
//        } else {
//            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//            btn_bottom_sheet.setText("Expand sheet");
//        }
            bottomSheetFragment = MarketPlaceBottomSheetFragment()
            bottomSheetFragment!!.show(activity!!.supportFragmentManager, bottomSheetFragment!!.tag)

        }

        mp_view_pager.setOnClickListener {

            CommonWebCalls.callToken(activity!!, "1", "", ActionIdData.C801, ActionIdData.T801)

            AppConstants.isFirst = 13
            val bundle = Bundle()
            bundle.putString("type", "free")
            bundle.putString("pname1", "Free Test")
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

        main_freetest_item_tvSeeall.setOnClickListener {

            CommonWebCalls.callToken(activity!!, "1", "", ActionIdData.C802, ActionIdData.T802)

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

            CommonWebCalls.callToken(activity!!, "1", "", ActionIdData.C804, ActionIdData.T804)

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

            CommonWebCalls.callToken(activity!!, "1", "", ActionIdData.C807, ActionIdData.T807)

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

            CommonWebCalls.callToken(activity!!, "1", "", ActionIdData.C809, ActionIdData.T809)

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

//        mp_view_pager!!.addOnPageChangeListener(introViewPagerListener)

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

        if (getView() != null) {
            callFilterListApi()
        }
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

            if (mDataList[position].Icon != "") {

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

            if (mDataList[position].Icon != "") {
                Picasso.get()
                    .load(AppConstants.IMAGE_BASE_URL + mDataList[position].Icon).placeholder(R.drawable.person_placeholder)
                    .into(holder.title)
            }

            holder.title1.text = mDataList[position].TutorName
//            holder.title.setImageDrawable(context.resources.getDrawable(R.drawable.pro_pic1))

            holder.title.setOnClickListener {

                CommonWebCalls.callToken(context, "1", "", ActionIdData.C806, ActionIdData.T806)

                AppConstants.isFirst = 15
                val bundle = Bundle()
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

//    private var introViewPagerListener: ViewPager.OnPageChangeListener =
//        object : ViewPager.OnPageChangeListener {
//
//            override fun onPageSelected(position: Int) {
//            addBottomDots(position)
//                /*Based on the page position change the button text*/
//
//                AppConstants.isFirst = 13
//                val bundle = Bundle()
//                bundle.putString("type", "free")
//                bundle.putString("pname1", "Packages")
//                bundle.putString("filtertypeid", "0")
//                bundle.putString(
//                    "boardid",
//                    Utils.getStringValue(activity!!, AppConstants.COURSE_ID, "0")!!
//                )
//                bundle.putString(
//                    "course_type",
//                    Utils.getStringValue(activity!!, AppConstants.COURSE_TYPE_ID, "1")
//                )
//                bundle.putString(
//                    "stdid",
//                    Utils.getStringValue(activity!!, AppConstants.STANDARD_ID, "0")!!
//                )
//                bundle.putString(
//                    "subid",
//                    Utils.getStringValue(activity!!, AppConstants.SUBJECT_ID, "0")!!
//                )
//                bundle.putString("tutorid", "")
//                bundle.putString("maxprice", "")
//                bundle.putString("minprice", "")
//                bundle.putString("search_name", "")
//                setFragments(bundle)
//
//            }
//
//            override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {
//                //Do nothing for now
//            }
//
//            override fun onPageScrollStateChanged(arg0: Int) {
//                //Do nothing for now
//            }
//        }

//    inner class MyViewPagerAdapter(var arrList: ArrayList<PackageData.PackageDataList>) :
//        PagerAdapter() {
//        override fun instantiateItem(container: ViewGroup, position: Int): Any {
//            val layoutInflater: LayoutInflater = LayoutInflater.from(activity)
//            val view = layoutInflater.inflate(R.layout.slider_item_layout, container, false)
//
//            var iv: ImageView = view.findViewById(R.id.imageView)
////            var tv: TextView = view.findViewById(R.id.testName)
//            var btn: Button = view.findViewById(R.id.btnBuy)
//
//            iv.setOnClickListener {
//
//                //                AppConstants.isBackFirst = 0
//
////                AppConstants.isFirst = 14
////                val bundle = Bundle()
////                bundle.putString("pkgid", arrList[position].TestPackageID)
////                bundle.putString("come_from", "selectpackage")
////                setFragments(bundle)
//
//                AppConstants.isFirst = 13
//                val bundle = Bundle()
//                bundle.putString("type", "free")
//                bundle.putString("pname1", "Packages")
//                bundle.putString("filtertypeid", "0")
//                bundle.putString(
//                    "boardid",
//                    Utils.getStringValue(activity!!, AppConstants.COURSE_ID, "0")!!
//                )
//                bundle.putString(
//                    "course_type",
//                    Utils.getStringValue(activity!!, AppConstants.COURSE_TYPE_ID, "1")
//                )
//                bundle.putString(
//                    "stdid",
//                    Utils.getStringValue(activity!!, AppConstants.STANDARD_ID, "0")!!
//                )
//                bundle.putString(
//                    "subid",
//                    Utils.getStringValue(activity!!, AppConstants.SUBJECT_ID, "0")!!
//                )
//                bundle.putString("tutorid", "")
//                bundle.putString("maxprice", "")
//                bundle.putString("minprice", "")
//                bundle.putString("search_name", "")
//                setFragments(bundle)
//
////                val intent = Intent(context, PackageDetailActivity::class.java)
////                intent.putExtra("pkgid", arrList[position].TestPackageID)
////                intent.putExtra("tutor_id", arrList[position].TutorID)
////                intent.putExtra("come_from", "selectpackage")
////                context!!.startActivity(intent)
//
////                callAddTestPackageApi(arrList[position].TestPackageID)
//            }
//
//            iv.setImageResource(R.drawable.free_test)
////            tv.text = arrList[position].TestPackageName
//
//            container.addView(view)
//            return view
//        }
//
//        override fun getCount(): Int {
//            return arrList.size
//        }
//
//        override fun isViewFromObject(view: View, obj: Any): Boolean {
//            return view === obj
//        }
//
//        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
//            val view = `object` as View
//            container.removeView(view)
//        }
//
////        override fun getPageWidth(position: Int): Float {
////            return 0.6f
////        }
//
//    }

    fun callFilterListApi() {

        if (!DialogUtils.isNetworkConnected(activity!!)) {
//            Utils.ping(activity!!, AppConstants.NETWORK_MSG)
            val netdialog = Dialog(activity!!)
            netdialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            netdialog.setContentView(R.layout.dialog_network)
            netdialog.setCanceledOnTouchOutside(false)

            val btnRetry: TextView = netdialog.findViewById(R.id.network_btnRetry)

            btnRetry.setOnClickListener {
                if (DialogUtils.isNetworkConnected(activity!!)) {
                    netdialog.dismiss()
                    callFilterListApi()
                }
            }

            netdialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            netdialog.setCanceledOnTouchOutside(false)
            netdialog.setCancelable(false)
            netdialog.show()

            DialogUtils.dismissDialog()
        }

        DialogUtils.showDialog(activity!!)
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        call = apiService.getPackage(
            WebRequests.getPackageParams(
                Utils.getStringValue(activity!!, AppConstants.COURSE_TYPE_ID, "0")!!,
                Utils.getStringValue(activity!!, AppConstants.COURSE_ID, "0")!!,
                Utils.getStringValue(activity!!, AppConstants.STANDARD_ID, "0")!!,
                Utils.getStringValue(activity!!, AppConstants.SUBJECT_ID, "0")!!,
                Utils.getStringValue(activity!!, AppConstants.USER_ID, "0")!!
            )
        )
        call!!.enqueue(object : Callback<GetMarketPlaceData> {
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

                        val mSingleDataList = response.body()!!.data.SingleTestPackage
                        val freeTestList = response.body()!!.data.FreeTestPackage
                        val subscriptionList = response.body()!!.data.Subscription

//                        Picasso.get().load(AppConstants.IMAGE_BASE_URL + response.body()!!.data.BannerList[0].BannerURL).into(mp_view_pager)
                        if (response.body()!!.data.Subscription.size > 0) {
//                            Picasso.get().load(AppConstants.IMAGE_BASE_URL + response.body()!!.data.Subscription[0].BannerURL)
//                                .placeholder(R.drawable.prog).into(img_subscription)

                            Glide.with(activity!!)
                                .load(AppConstants.IMAGE_BASE_URL + response.body()!!.data.Subscription[0].BannerURL)
                                .apply(RequestOptions()
                                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                    .placeholder(R.drawable.prog))
                                .into(img_subscription)

                        } else {
                            img_subscription.visibility = View.GONE
                        }
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

                            rvTutor.adapter = TutorAdapter(activity!!, tutorList!!)

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
                                MyPackageAdapter(activity!!, mSingleDataList, "market_place", "1")
                        } else {
                            main_pkg_item_rvSingleTest.visibility = View.GONE
                            main_pkg_item_tvSingleTest.visibility = View.GONE
                            main_pkg_item_tvSSeeall.visibility = View.GONE
                        }

                        if (freeTestList.size > 0) {

                            mp_view_pager.visibility = View.GONE
                            main_freetest_item_tvSeeall.visibility = View.GONE

//                            myViewPagerAdapter = MyViewPagerAdapter(freeTestList)
//                            mp_view_pager!!.adapter = myViewPagerAdapter

                        } else {
                            mp_view_pager.visibility = View.GONE
                            main_freetest_item_tvSeeall.visibility = View.GONE

                        }

                        if (subscriptionList.size > 0) {
                            img_subscription.visibility = View.VISIBLE
                        } else {
                            img_subscription.visibility = View.GONE
                        }

                        if (mDataList!!.size > 0) {

                            rlCoverflow!!.visibility = View.VISIBLE
                            rlCoverflow1.visibility = View.VISIBLE
                            main_pkg_item_tvSeeall.visibility = View.VISIBLE
                            main_pkg_item_tvCategory.visibility = View.VISIBLE

                            carousel!!.adapter =
                                PkgPageAdapter(5, 300, 160, activity!!, mDataList!!)

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

//                        Toast.makeText(activity!!, response.body()!!.Msg, Toast.LENGTH_SHORT).show()
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

    //new market place

    inner class MyViewPagerAdapter1(var arrList: ArrayList<PackageData.PackageDataList>) :
        PagerAdapter() {

        override fun instantiateItem(container: ViewGroup, position: Int): Any {

            val layoutInflater: LayoutInflater = LayoutInflater.from(activity)
            val view = layoutInflater.inflate(R.layout.marketplace_pkg_list_item, container, false)

            val image: ImageView = view.findViewById(R.id.package_item_ivImage)
//            val imagebg: ImageView = view.findViewById(R.id.package_item_ivBg)

            val pkgname: TextView = view.findViewById(R.id.package_item_tvPkgname)
            val sub: TextView = view.findViewById(R.id.package_item_tvSub)
            val created: TextView = view.findViewById(R.id.package_item_tvCreated)
            val price: TextView = view.findViewById(R.id.package_item_tvPrice)
            val mainll: TextView = view.findViewById(R.id.package_item_tvDetail)
            val mainll1: CardView = view.findViewById(R.id.mall)

            val t1: TextView = view.findViewById(R.id.package_item_tv1)
            val t2: TextView = view.findViewById(R.id.package_item_tv2)
            val t3: TextView = view.findViewById(R.id.package_item_tv3)
            val t4: TextView = view.findViewById(R.id.package_item_tv4)
            val t5: TextView = view.findViewById(R.id.package_item_tv5)
            val t6: TextView = view.findViewById(R.id.package_item_tv6)

            if (arrList.size > 0) {
                pkgname.text = arrList[position].TestPackageName
                sub.text = arrList[position].SubjectName + " by " + arrList[position].TutorName
//                created.text = "By " + arrList[position].TutorName
                created.text =
                    Html.fromHtml("By " + "<font color=\"#3ea7e0\">" + arrList[position].TutorName + "</font>")
                price.text = arrList[position].TestPackageSalePrice
//                desc.text = arrList[position].TestPackageDescription

                if(arrList[position].TestList != null) {
                    if (arrList[position].TestList.size > 0) {
                        if (arrList[position].TestList[0].Name != "") {
                            t1.text = arrList[position].TestList[0].Name
                        } else {
                            t1.text = ""
                        }

                        if (arrList[position].TestList[1].Name != "") {
                            t2.text = arrList[position].TestList[1].Name
                        } else {
                            t2.text = ""
                        }

                        if (arrList[position].TestList[2].Name != "") {
                            t3.text = arrList[position].TestList[2].Name
                        } else {
                            t3.text = ""
                        }

                        if (arrList[position].TestList[3].Name != "") {
                            t4.text = "\u2022 " + arrList[position].TestList[3].Name
                        } else {
                            t4.text = ""
                        }

                        if (arrList[position].TestList[4].Name != "") {
                            t5.text = "\u2022 " + arrList[position].TestList[4].Name
                        } else {
                            t5.text = ""
                        }

                        if (arrList[position].TestList[5].Name != "") {
                            t6.text = "\u2022 " + arrList[position].TestList[5].Name
                        } else {
                            t6.text = ""
                        }
                    }
                }

                if (arrList[position].Icon != "") {
                    Picasso.get().load(AppConstants.IMAGE_BASE_URL + arrList[position].Icon)
                        .into(image)

//                    Picasso.get().load(AppConstants.IMAGE_BASE_URL + arrList[position].Icon)
//                        .into(imagebg)

//                    AsyncCaller(image, arrList[position].Icon).execute()
//                    BlurImage.with(getApplicationContext()).load(R.drawable.pp_1).intensity(20F).Async(true).into(image)
//                    arrList[position].Icon

                }

                 created.setOnClickListener {

                    CommonWebCalls.callToken(
                        activity!!,
                        "1",
                        "",
                        ActionIdData.C805,
                        ActionIdData.T805
                    )

                    AppConstants.isFirst = 15
                    val bundle = Bundle()
                    bundle.putString("tutor_id", arrList[position].TutorID)
                    setFragments(bundle)

                }

                mainll1.setOnClickListener {

                    CommonWebCalls.callToken(
                        activity!!,
                        "1",
                        "",
                        ActionIdData.C803,
                        ActionIdData.T803
                    )

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

                mainll.setOnClickListener {

                    CommonWebCalls.callToken(
                        activity!!,
                        "1",
                        "",
                        ActionIdData.C803,
                        ActionIdData.T803
                    )

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

    fun callVerifyAccountApi(phone: String) {

        if (!DialogUtils.isNetworkConnected(activity!!)) {
//            Utils.ping(activity!!, AppConstants.NETWORK_MSG)
            DialogUtils.NetworkDialog(activity!!)
            DialogUtils.dismissDialog()
        }

        DialogUtils.showDialog(activity!!)

        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.checkMobile(phone)

        call.enqueue(object : Callback<VerifyMobileData> {
            override fun onResponse(call: Call<VerifyMobileData>, response: Response<VerifyMobileData>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.Status == "true") {

                        Utils.setStringValue(
                            activity!!,
                            AppConstants.OTP,
                            response.body()!!.data[0].OTP
                        )

                        val intent = Intent(context, OtpActivity::class.java)
                        intent.putExtra("mobile_number", phone)
                        intent.putExtra("otp", response.body()!!.data[0].OTP)
                        intent.putExtra("come_from", "prefrence")
                        intent.putExtra("first_name", Utils.getStringValue(activity!!, AppConstants.FIRST_NAME, ""))
                        intent.putExtra("last_name", Utils.getStringValue(activity!!, AppConstants.LAST_NAME, ""))
                        intent.putExtra("email", Utils.getStringValue(activity!!, AppConstants.USER_EMAIL, ""))
                        intent.putExtra("password", "")
                        intent.putExtra("account_type", "5")
                        (context as DashboardActivity).startActivity(intent)

                    } else {
                        Toast.makeText(
                            context,
                            response.body()!!.Msg,
                            Toast.LENGTH_LONG
                        ).show()

//                    Log.d("loginresponse", response.body()!!.asString)
                    }
                }
            }

            override fun onFailure(call: Call<VerifyMobileData>, t: Throwable) {
                // Log error here since request failed
                Log.e("", t.toString())
                DialogUtils.dismissDialog()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (call != null && call!!.isExecuted) {
            call!!.cancel()
        }
    }
}
