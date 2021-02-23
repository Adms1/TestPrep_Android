package com.testcraft.testcraft.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.testcraft.testcraft.Connectivity
import com.testcraft.testcraft.R
import com.testcraft.testcraft.activity.IntroActivity.Companion.disconnectFromFacebook
import com.testcraft.testcraft.activity.ViewSolutionFragment.Companion.curr_index1
import com.testcraft.testcraft.activity.ViewSolutionFragment.Companion.solution_grppos1
import com.testcraft.testcraft.adapter.DrawerMenuListAdapter
import com.testcraft.testcraft.fragments.*
import com.testcraft.testcraft.models.PackageData
import com.testcraft.testcraft.utils.*
import com.testcraft.testcraft.utils.Utils.Companion.clearPrefrence
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlin.system.exitProcess

@Suppress("DEPRECATION")
class DashboardActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    internal lateinit var mDrawerToggle: ActionBarDrawerToggle
    var drawerMenuListAdapter: DrawerMenuListAdapter? = null
    var menuList = arrayOf("Test", "Profile", "Logout")
    var ON_BACK = 0

    var doubleBackToExitPressedOnce = false

    var connectivity: Connectivity? = null

//    override fun onResume() {
//        super.onResume()
//        val filter = IntentFilter()
//        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
//        registerReceiver(connectivity, filter)
//    }
//
//    override fun onStop() {
//        super.onStop()
//        unregisterReceiver(connectivity)
//    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        setContentView(R.layout.activity_dashboard)

//        connectivity = Connectivity()

        Utils.deleteCache(this@DashboardActivity)

        ON_BACK = 0

        main_header = findViewById(R.id.dashboard_header)
        tvExpired = findViewById(R.id.dashboard_tvExpirepkg)
//        ivCart = findViewById(R.id.dashboard_ivCart)
        ivHome = findViewById(R.id.dash_ivHome)
        ivMarket = findViewById(R.id.dash_ivMarket)
        ivExplore = findViewById(R.id.dash_ivSearch)
        ivProfile = findViewById(R.id.dash_ivUser)
        tvHome = findViewById(R.id.dash_tvHome)
        tvMarket = findViewById(R.id.dash_tvMarket)
        tvExplore = findViewById(R.id.dash_tvSearch)
        tvProfile = findViewById(R.id.dash_tvUser)
        btnBack = findViewById(R.id.dashboard_ivBack)
//        btnLogout = findViewById(R.id.dashboard_ivLogout)
        llBottom = findViewById(R.id.dashboard_llBottom)
        rlFilter = findViewById(R.id.dashboard_rlFilter)
        tvFilter = findViewById(R.id.dashboard_tvFilter)
        ivSort = findViewById(R.id.dashboard_ivSort)

        fragManager = this.supportFragmentManager
        context = this@DashboardActivity

//        containerr = findViewById(R.id.container)

//        mDrawerToggle = object : ActionBarDrawerToggle(
//            this, drawer_layout, R.drawable.menu, // nav menu toggle icon
//            R.string.app_name, // nav drawer open - description for accessibility
//            R.string.app_name // nav drawer close - description for accessibility
//        ) {
//        }

//        drawer_layout.setDrawerListener(mDrawerToggle)

        if (intent != null && intent.hasExtra("dplinkparams")) {

            setFragments(intent.getBundleExtra("dplinkparams"))

        } else {
            if (intent != null && intent.hasExtra("testid") && intent.hasExtra("studenttestid")) {

                val bundle = Bundle()
                bundle.putString("testid", intent.getStringExtra("testid"))
                bundle.putString("studenttestid", intent.getStringExtra("studenttestid"))
                bundle.putString("testname", intent.getStringExtra("testname"))
                setFragments(bundle)

            } else if (intent != null && intent.hasExtra("tutor_id")) {

                val bundle = Bundle()
                bundle.putString("tutor_id", intent.getStringExtra("tutor_id"))
                setFragments(bundle)

            } else if(intent.hasExtra("createtest")){

                AppConstants.isFirst = 11
                val bundle = Bundle()

                if(intent.getStringExtra("coursetypeid") == "1"){
                    bundle.putBoolean("isCompetitive", false)
                }else{
                    bundle.putBoolean("isCompetitive", true)
                }

                    bundle.putInt("sub_id", intent.getIntExtra("sub_id",0))
                    bundle.putString("board_id", intent.getStringExtra("board_id"))
                    bundle.putString("std_id", intent.getStringExtra("std_id"))
                    bundle.putString("sub_name", intent.getStringExtra("sub_name"))
//                    bundle.putBoolean("isCompetitive", intent.getBooleanExtra("isCompetitive", false))

                setFragments(bundle)

            }else {

                dash_ivMarket.setImageResource(R.drawable.blue_list)
                dash_ivHome.setImageResource(R.drawable.home)
                dash_ivUser.setImageResource(R.drawable.menu_one)
                dash_ivSearch.setImageResource(R.drawable.search)

                dash_tvHome.setTextColor(resources.getColor(R.color.light_gray))
                dash_tvMarket.setTextColor(resources.getColor(R.color.nfcolor))
                dash_tvSearch.setTextColor(resources.getColor(R.color.light_gray))
                dash_tvUser.setTextColor(resources.getColor(R.color.light_gray))

                setFragments(null)
            }
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        dashboard_ivBack.setOnClickListener {

            onBackPressed()

        }

        dashboard_ivNotification.setOnClickListener {

            val intent = Intent(this@DashboardActivity, NotificationActivity::class.java)
            startActivity(intent)

        }

//        dashboard_ivLogout.setOnClickListener { signOut() }

//        init()

//        supportFragmentManager.beginTransaction().add(R.id.container, SelectCoarseFragment()).commit()

        AppConstants.FILTER_STANDARD_ID = "0"
        AppConstants.FILTER_SUBJECT_ID = "0"
        AppConstants.FILTER_TUTOR_ID = "0"
        AppConstants.FILTER_BOARD_ID = "0"
        AppConstants.FILTER_FROM_PRICE = "0"
        AppConstants.FILTER_TO_PRICE = "5000"

    }

    fun onClick(v: View) {
        when (v) {

//            dashboard_ivCart -> {
//
//                val intent = Intent(this@DashboardActivity, CartActivity::class.java)
//                startActivity(intent)
//
//            }

//            dashboard_ivPencil -> {
//
//                val intent = Intent(this@DashboardActivity, FilterActivity::class.java)
//                startActivity(intent)
//                finish()
//
//            }

//            dashboard_ivLogout -> {
//                signOut()
//            }

            dash_llDashboard -> {

                if (AppConstants.isFirst != 1) {
                    CommonWebCalls.callToken(
                        this@DashboardActivity,
                        "1",
                        "",
                        ActionIdData.C810,
                        ActionIdData.T810
                    )

                    AppConstants.isFirst = 1

                    setFragments(null)
                }
            }

            dash_llMarket    -> {

                if (AppConstants.isFirst != 0) {
                    CommonWebCalls.callToken(
                        this@DashboardActivity,
                        "1",
                        "",
                        ActionIdData.C813,
                        ActionIdData.T813
                    )

                    AppConstants.isFirst = 0

                    setFragments(null)
                }
            }

            dash_llExplore   -> {

                if (AppConstants.isFirst != 3) {
                    CommonWebCalls.callToken(
                        this@DashboardActivity,
                        "1",
                        "",
                        ActionIdData.C811,
                        ActionIdData.T811
                    )

                    AppConstants.isFirst = 3

                    setFragments(null)
                }

            }

            dash_llProfile   -> {

                CommonWebCalls.callToken(
                    this@DashboardActivity,
                    "1",
                    "",
                    ActionIdData.C812,
                    ActionIdData.T812
                )

                AppConstants.isFirst = 4

                setFragments(null)

            }
        }
    }

    companion object {
        var fragment: Fragment? = null
        var testid = ""
        var studenttestid = ""
        var subid = 0
        var subname = ""
        //        var pkgid = ""
        var pname = ""
        var isExpire = ""
        var isCompetitive = false

        var main_header: TextView? = null
        var tvExpired: TextView? = null

        //        var ivCart: ImageView? = null
        var ivHome: ImageView? = null
        var ivMarket: ImageView? = null
        var ivExplore: ImageView? = null
        var ivProfile: ImageView? = null
        var tvHome: TextView? = null
        var tvMarket: TextView? = null
        var tvExplore: TextView? = null
        var tvProfile: TextView? = null
        var btnBack: ImageView? = null
        //        var btnLogout: ImageView? = null
        var rlFilter: RelativeLayout? = null
        var tvFilter: TextView? = null
        var ivSort: ImageView? = null

        var course_type = ""
        var boardid = ""
        var stdid = ""
        var tutorid = ""
        var tutorpid = ""
        var ptype = ""
        var subid1 = ""
        var parr: ArrayList<PackageData.PackageDataList> = ArrayList()
        var maxprice = ""
        var minprice = ""
        var search_name = ""
        var testname = ""
        var testque = ""
        var pkgid = ""
        var filtertypeid = ""
        var iscompetitive = ""
        var llBottom: LinearLayout? = null

        var context: Context? = null

        var mGoogleSignInClient: GoogleSignInClient? = null

        var fragManager: FragmentManager? = null

        fun signOut() {

            DialogUtils.createConfirmDialog(
                context!!,
                "Logout?",
                "Are you sure you want to logout?",
                "Yes",
                "No",
                DialogInterface.OnClickListener { dialog, which ->
                    clearPrefrence(context!!)
                    AppConstants.COURSE_FLOW_ARRAY.clear()

                    AppConstants.FILTER_STANDARD_ID = "0"
                    AppConstants.FILTER_SUBJECT_ID = "0"
                    AppConstants.FILTER_TUTOR_ID = "0"
                    AppConstants.FILTER_BOARD_ID = "0"
                    AppConstants.isFirst = 0

                    disconnectFromFacebook()

                    mGoogleSignInClient!!.signOut()
                        .addOnCompleteListener(context as DashboardActivity) {
                            // ...

                            Utils.setStringValue(context!!, AppConstants.IS_LOGIN, "false")

                            val intent = Intent(context, IntroActivity::class.java)
                            context!!.startActivity(intent)
//                overridePendingTransition(R.anim.slide_in_leftt, R.anim.slide_out_right)
                            (context as DashboardActivity).finish()
                            dialog.dismiss()
                        }
                },
                DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()

                }).show()
        }

        @SuppressLint("SetTextI18n")
        fun setFragments(bundle: Bundle?) {

//            var bundle = intent1!!.extras

            rlFilter!!.visibility = View.GONE
            ivSort!!.visibility = View.GONE
            tvExpired!!.visibility = View.GONE

            when (AppConstants.isFirst) {
                0  -> {

                    fragment = MarketPlaceFragment()

                    llBottom!!.visibility = View.VISIBLE

                    main_header!!.text = "Market Place"
                    btnBack!!.visibility = View.GONE
//                    btnLogout!!.visibility = View.GONE

                    //                var bundle = Bundle()
//                bundle.putString("subid", intent.getStringExtra("subject_id"))
//                fragment.arguments = bundle

//                dashboard_ivFilter.visibility = View.VISIBLE
//                    ivCart!!.visibility = View.GONE
//                dashboard_ivPencil.visibility = View.VISIBLE
//                dashboard_ivBack.visibility = View.VISIBLE
//                dashboard_ivPencil.visibility = View.VISIBLE

                    ivMarket!!.setImageResource(R.drawable.blue_list)
                    ivHome!!.setImageResource(R.drawable.home)
                    ivProfile!!.setImageResource(R.drawable.menu_one)
                    ivExplore!!.setImageResource(R.drawable.search)

                    tvHome!!.setTextColor(context!!.resources.getColor(R.color.light_gray))
                    tvMarket!!.setTextColor(context!!.resources.getColor(R.color.nfcolor))
                    tvExplore!!.setTextColor(context!!.resources.getColor(R.color.light_gray))
                    tvProfile!!.setTextColor(context!!.resources.getColor(R.color.light_gray))

                    fragManager!!.beginTransaction().replace(R.id.container, fragment!!, "marketplace").addToBackStack(null)
                        .commitAllowingStateLoss()

                }
                1  -> {

                    fragment = ChooseMarketPlaceFragment()

                    llBottom!!.visibility = View.VISIBLE

                    main_header!!.text = "Dashboard"
                    btnBack!!.visibility = View.GONE
//                    btnLogout!!.visibility = View.GONE
//                    dashboard_ivPencil.visibility = View.VISIBLE
//                    dashboard_ivFilter.visibility = View.GONE
//                    ivCart!!.visibility = View.GONE
//                    dashboard_ivPencil.visibility = View.GONE
//                    dashboard_ivBack.visibility = View.GONE
//                    dashboard_ivFilter.visibility = View.GONE

                    ivHome!!.setImageResource(R.drawable.blue_home)
                    ivMarket!!.setImageResource(R.drawable.list)
                    ivExplore!!.setImageResource(R.drawable.search)
                    ivProfile!!.setImageResource(R.drawable.menu_one)

                    tvHome!!.setTextColor(context!!.resources.getColor(R.color.nfcolor))
                    tvMarket!!.setTextColor(context!!.resources.getColor(R.color.light_gray))
                    tvExplore!!.setTextColor(context!!.resources.getColor(R.color.light_gray))
                    tvProfile!!.setTextColor(context!!.resources.getColor(R.color.light_gray))

                    fragManager!!.beginTransaction().replace(R.id.container, fragment!!)
//                .addToBackStack(null)
                        .commitAllowingStateLoss()

                }

                3  -> {

                    fragment = ExploreFragment()

                    llBottom!!.visibility = View.VISIBLE

                    main_header!!.text = "Explore"
                    btnBack!!.visibility = View.GONE
//                    btnLogout!!.visibility = View.GONE

                    //                supportFragmentManager.beginTransaction().replace(R.id.container, ExploreFragment())
//                    .commit()

//                dashboard_ivFilter.visibility = View.GONE
//                    ivCart!!.visibility = View.GONE
//                dashboard_ivPencil.visibility = View.GONE
//                dashboard_ivBack.visibility = View.GONE
//                dashboard_ivFilter.visibility = View.GONE

                    ivExplore!!.setImageResource(R.drawable.blue_search)
                    ivProfile!!.setImageResource(R.drawable.menu_one)
                    ivMarket!!.setImageResource(R.drawable.list)
                    ivHome!!.setImageResource(R.drawable.home)

                    tvHome!!.setTextColor(context!!.resources.getColor(R.color.light_gray))
                    tvMarket!!.setTextColor(context!!.resources.getColor(R.color.light_gray))
                    tvExplore!!.setTextColor(context!!.resources.getColor(R.color.nfcolor))
                    tvProfile!!.setTextColor(context!!.resources.getColor(R.color.light_gray))

                    fragManager!!.beginTransaction().replace(R.id.container, fragment!!)
//                .addToBackStack(null)
                        .commitAllowingStateLoss()

                }
                4 -> {

                    fragment = OtherFragment()

                    llBottom!!.visibility = View.VISIBLE

                    main_header!!.text = "My Profile"
                    btnBack!!.visibility = View.GONE
//                    btnLogout!!.visibility = View.GONE

                    //                dashboard_ivFilter.visibility = View.GONE
//                    ivCart!!.visibility = View.GONE
//                dashboard_ivPencil.visibility = View.GONE
//                dashboard_ivBack.visibility = View.GONE
//                dashboard_ivFilter.visibility = View.GONE

//                dashboard_ivLogout.visibility = View.GONE
//                supportFragmentManager.beginTransaction().replace(R.id.container, OtherFragment()).commit()

                    ivProfile!!.setImageResource(R.drawable.blue_menu)
                    ivExplore!!.setImageResource(R.drawable.search)
                    ivMarket!!.setImageResource(R.drawable.list)
                    ivHome!!.setImageResource(R.drawable.home)

                    tvHome!!.setTextColor(context!!.resources.getColor(R.color.light_gray))
                    tvMarket!!.setTextColor(context!!.resources.getColor(R.color.light_gray))
                    tvExplore!!.setTextColor(context!!.resources.getColor(R.color.light_gray))
                    tvProfile!!.setTextColor(context!!.resources.getColor(R.color.nfcolor))

                    fragManager!!.beginTransaction().replace(R.id.container, fragment!!)
//                .addToBackStack(null)
                        .commitAllowingStateLoss()

                }
                5 -> {

                    fragment =
                        MyPaymentFragment()

                    llBottom!!.visibility = View.VISIBLE

                    main_header!!.text = "Packages"
                    btnBack!!.visibility = View.VISIBLE
//                    btnLogout!!.visibility = View.GONE

                    fragManager!!.beginTransaction().replace(R.id.container, fragment!!)
//                .addToBackStack(null)
                        .commitAllowingStateLoss()

                }
                6  -> {

                    fragment =
                        ChangePasswordFragment()

                    llBottom!!.visibility = View.VISIBLE

                    val bundle1 = Bundle()
                    bundle1.putString("come_from", "other")

                    (fragment as ChangePasswordFragment).arguments = bundle1

                    main_header!!.text = "Change Password"
                    btnBack!!.visibility = View.VISIBLE
//                    btnLogout!!.visibility = View.GONE

                    fragManager!!.beginTransaction().replace(R.id.container, fragment!!)
//                .addToBackStack(null)
                        .commitAllowingStateLoss()

                }
                7  -> {

                    fragment =
                        ChangePasswordFragment()

                    llBottom!!.visibility = View.GONE

                    val bundle2 = Bundle()
                    bundle2.putString("come_from", "otp")

                    (fragment as ChangePasswordFragment).arguments = bundle2

                    main_header!!.text = "Change Password"
                    btnBack!!.visibility = View.VISIBLE
//                    btnLogout!!.visibility = View.GONE

                    fragManager!!.beginTransaction().replace(R.id.container, fragment!!)
//                .addToBackStack(null)
                        .commitAllowingStateLoss()

                }
                8  -> {

                    fragment = UpdateProfileActivity()

                    llBottom!!.visibility = View.VISIBLE

                    btnBack!!.visibility = View.VISIBLE
//                    btnLogout!!.visibility = View.GONE

                    main_header!!.text = "Profile"

                    fragManager!!.beginTransaction().replace(R.id.container, fragment!!)
//                .addToBackStack(null)
                        .commitAllowingStateLoss()

                }
                9  -> {

                    testid = bundle!!.getString("testid")!!
                    studenttestid = bundle.getString("studenttestid")!!
                    testque = bundle.getString("totalque")!!
                    testname = bundle.getString("testname")!!

                    fragment = ViewSolutionFragment()

                    llBottom!!.visibility = View.VISIBLE

                    val bundle6 = Bundle()
                    bundle6.putString("testid", testid)
                    bundle6.putString("studenttestid", studenttestid)
                    bundle6.putString("totalque", testque)

                    (fragment as ViewSolutionFragment).arguments = bundle6

                    main_header!!.text = testname
                    btnBack!!.visibility = View.VISIBLE
//                    btnLogout!!.visibility = View.GONE

                    fragManager!!.beginTransaction().replace(R.id.container, fragment!!)
//                .addToBackStack(null)
                        .commitAllowingStateLoss()

                }
                10 -> {

                    testid = bundle!!.getString("testid")!!
                    studenttestid = bundle.getString("studenttestid")!!
                    testname = bundle.getString("testname")!!
//                    iscompetitive = bundle.getString("isCompetitive")!!

                    fragment = TestReviewFragment()

                    llBottom!!.visibility = View.VISIBLE

                    val bundle5 = Bundle()
                    bundle5.putString("testid", testid)
                    bundle5.putString("studenttestid", studenttestid)
                    bundle5.putString("testname", testname)
//                    bundle5.putString("isCompetitive", iscompetitive)

                    (fragment as TestReviewFragment).arguments = bundle5

                    main_header!!.text = testname
                    btnBack!!.visibility = View.VISIBLE
//                    btnLogout!!.visibility = View.GONE

                    fragManager!!.beginTransaction().replace(R.id.container, fragment!!)
//                .addToBackStack(null)
                        .commitAllowingStateLoss()

                }
                11 -> {

                    subid = bundle!!.getInt("sub_id", 0)
                    stdid = bundle.getString("std_id", "")
                    boardid = bundle.getString("board_id", "")
                    subname = bundle.getString("sub_name")!!
                    isCompetitive = bundle.getBoolean("isCompetitive", false)
                    isExpire = bundle.getString("isExpired", "")

                    fragment = MyPackagesFragment()

                    llBottom!!.visibility = View.VISIBLE

                    val bundle3 = Bundle()
                    bundle3.putInt("sub_id", subid)
                    bundle3.putString("std_id", stdid)
                    bundle3.putString("board_id", boardid)
                    bundle3.putString("sub_name", subname)
                    bundle3.putBoolean("isCompetitive", isCompetitive)
                    bundle3.putString("isExpire", isExpire)

                    (fragment as MyPackagesFragment).arguments = bundle3

                    main_header!!.text = bundle.getString("sub_name")
                    btnBack!!.visibility = View.VISIBLE
//                    btnLogout!!.visibility = View.GONE

                    fragManager!!.beginTransaction().replace(R.id.container, fragment!!)
//                .addToBackStack(null)
                        .commitAllowingStateLoss()

                }
                12 -> {

//                    pkgid = bundle!!.getString("pkgid")!!
//                    pname = bundle.getString("pname")!!

                    fragment = TestListFragment()

                    llBottom!!.visibility = View.VISIBLE

//                    val bundle4 = Bundle()
//                    bundle4.putString("pkgid", pkgid)
//                    bundle4.putString("pname", pname)

//                    (fragment as TestListActivity).arguments = bundle4

                    main_header!!.text = AppConstants.PKG_NAME
                    btnBack!!.visibility = View.VISIBLE
//                    btnLogout!!.visibility = View.GONE

                    ivHome!!.setImageResource(R.drawable.blue_home)
                    ivMarket!!.setImageResource(R.drawable.list)
                    ivExplore!!.setImageResource(R.drawable.search)
                    ivProfile!!.setImageResource(R.drawable.menu_one)

                    tvHome!!.setTextColor(context!!.resources.getColor(R.color.nfcolor))
                    tvMarket!!.setTextColor(context!!.resources.getColor(R.color.light_gray))
                    tvExplore!!.setTextColor(context!!.resources.getColor(R.color.light_gray))
                    tvProfile!!.setTextColor(context!!.resources.getColor(R.color.light_gray))

                    fragManager!!.beginTransaction().replace(R.id.container, fragment!!)
                        .commitAllowingStateLoss()

                }
                13 -> {

                    llBottom!!.visibility = View.VISIBLE

                    ptype = bundle!!.getString("type")!!
                    pname = bundle.getString("pname1")!!
                    boardid = bundle.getString("boardid")!!
                    course_type = bundle.getString("course_type")!!
                    stdid = bundle.getString("stdid")!!
                    subid1 = bundle.getString("subid")!!
                    tutorid = bundle.getString("tutorid")!!
                    filtertypeid = bundle.getString("filtertypeid")!!

                    if (bundle.containsKey("parr")) {
                        parr =
                            (bundle.getSerializable("parr") as ArrayList<PackageData.PackageDataList>?)!!
                    }

                    maxprice = bundle.getString("maxprice")!!
                    minprice = bundle.getString("minprice")!!
                    search_name = bundle.getString("search_name")!!

                    fragment = TutorDetailFragment()

                    val bundle7 = Bundle()
                    bundle7.putString("type", ptype)
                    bundle7.putString("pname", pname)
                    bundle7.putString("course_type", course_type)
                    bundle7.putString("boardid", boardid)
                    bundle7.putString("stdid", stdid)
                    bundle7.putString("subid", subid1)
                    bundle7.putString("tutorid", tutorid)
                    bundle7.putString("filtertypeid", filtertypeid)

                    if (parr.size > 0) {
                        bundle7.putSerializable("parr", parr)
                    }
                    bundle7.putString("maxprice", maxprice)
                    bundle7.putString("minprice", minprice)
                    bundle7.putString("search_name", search_name)

                    (fragment as TutorDetailFragment).arguments = bundle7

                    main_header!!.text = pname
                    btnBack!!.visibility = View.VISIBLE
//                    btnLogout!!.visibility = View.GONE
//                    btnLogout!!.visibility = View.GONE
//                    btnLogout!!.visibility = View.GONE

                    ivHome!!.setImageResource(R.drawable.home)
                    ivProfile!!.setImageResource(R.drawable.menu_one)
                    tvHome!!.setTextColor(context!!.resources.getColor(R.color.light_gray))
                    tvProfile!!.setTextColor(context!!.resources.getColor(R.color.light_gray))

                    if (filtertypeid == "-1") {

                        ivMarket!!.setImageResource(R.drawable.list)
                        ivExplore!!.setImageResource(R.drawable.blue_search)

                        tvMarket!!.setTextColor(context!!.resources.getColor(R.color.light_gray))
                        tvExplore!!.setTextColor(context!!.resources.getColor(R.color.nfcolor))

                    } else {

                        ivMarket!!.setImageResource(R.drawable.blue_list)
                        ivExplore!!.setImageResource(R.drawable.search)
                        tvMarket!!.setTextColor(context!!.resources.getColor(R.color.nfcolor))
                        tvExplore!!.setTextColor(context!!.resources.getColor(R.color.light_gray))

                    }

                    fragManager!!.beginTransaction().replace(R.id.container, fragment!!)
//                .addToBackStack(null)
                        .commitAllowingStateLoss()

                }
                14 -> {

                    pkgid = bundle!!.getString("pkgid")!!
                    pname = bundle.getString("come_from")!!

                    fragment = PackageDetailFragment()

                    llBottom!!.visibility = View.VISIBLE

                    val bundle10 = Bundle()
                    bundle10.putString("pkgid", pkgid)
                    bundle10.putString("come_from", pname)

                    (fragment as PackageDetailFragment).arguments = bundle10

                    main_header!!.text = "Package Details"
                    btnBack!!.visibility = View.VISIBLE
//                    btnLogout!!.visibility = View.GONE

                    ivHome!!.setImageResource(R.drawable.home)
                    ivMarket!!.setImageResource(R.drawable.blue_list)
                    ivExplore!!.setImageResource(R.drawable.search)
                    ivProfile!!.setImageResource(R.drawable.menu_one)

                    tvHome!!.setTextColor(context!!.resources.getColor(R.color.light_gray))
                    tvMarket!!.setTextColor(context!!.resources.getColor(R.color.nfcolor))
                    tvExplore!!.setTextColor(context!!.resources.getColor(R.color.light_gray))
                    tvProfile!!.setTextColor(context!!.resources.getColor(R.color.light_gray))

                    fragManager!!.beginTransaction().replace(R.id.container, fragment!!)
                        .addToBackStack(null)
                        .commit()

                }
                15 -> {

                    tutorpid = bundle!!.getString("tutor_id")!!
//                    pname = bundle.getString("pname")!!

                    fragment = TutorProfileFragment()

                    llBottom!!.visibility = View.VISIBLE

                    val bundle9 = Bundle()
                    bundle9.putString("tutor_id", tutorpid)

                    (fragment as TutorProfileFragment).arguments = bundle9

                    btnBack!!.visibility = View.VISIBLE
//                    btnLogout!!.visibility = View.GONE

                    ivHome!!.setImageResource(R.drawable.home)
                    ivMarket!!.setImageResource(R.drawable.blue_list)
                    ivExplore!!.setImageResource(R.drawable.search)
                    ivProfile!!.setImageResource(R.drawable.menu_one)

                    tvHome!!.setTextColor(context!!.resources.getColor(R.color.light_gray))
                    tvMarket!!.setTextColor(context!!.resources.getColor(R.color.nfcolor))
                    tvExplore!!.setTextColor(context!!.resources.getColor(R.color.light_gray))
                    tvProfile!!.setTextColor(context!!.resources.getColor(R.color.light_gray))

                    fragManager!!.beginTransaction().replace(R.id.container, fragment!!)
                        .addToBackStack(null)
                        .commit()

                }
                16 -> {

                    fragment = ContactUsFragment()

                    llBottom!!.visibility = View.VISIBLE

                    btnBack!!.visibility = View.VISIBLE
//                    btnLogout!!.visibility = View.GONE

                    main_header!!.text = "Contact Us"

                    fragManager!!.beginTransaction().replace(R.id.container, fragment!!)
//                .addToBackStack(null)
                        .commitAllowingStateLoss()
                }
                17 -> {

                    fragment = ReferfriendFragment()

                    llBottom!!.visibility = View.VISIBLE

                    btnBack!!.visibility = View.VISIBLE
//                    btnLogout!!.visibility = View.GONE

                    main_header!!.text = "Refer a Friend"

                    fragManager!!.beginTransaction().replace(R.id.container, fragment!!)
//                .addToBackStack(null)
                        .commitAllowingStateLoss()
                }
                18 -> {

                    fragment = RedeemCouponFragment()

                    llBottom!!.visibility = View.VISIBLE

                    btnBack!!.visibility = View.VISIBLE
//                    btnLogout!!.visibility = View.GONE

                    main_header!!.text = "Redeem Coupon"

                    fragManager!!.beginTransaction().replace(R.id.container, fragment!!)
//                .addToBackStack(null)
                        .commitAllowingStateLoss()
                }
                19 -> {

                    fragment = MySubscriptionFragment()

                    llBottom!!.visibility = View.VISIBLE

                    btnBack!!.visibility = View.VISIBLE
//                    btnLogout!!.visibility = View.GONE

                    main_header!!.text = "Subscriptions"

                    fragManager!!.beginTransaction().replace(R.id.container, fragment!!)
//                .addToBackStack(null)
                        .commitAllowingStateLoss()
                }
            }

//            fragManager!!.beginTransaction().replace(R.id.container, fragment!!)
//                .addToBackStack(null)
//                .commitAllowingStateLoss()

        }
    }

    override fun onBackPressed() {

        if (currentFocus != null) {
            val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }

        if (AppConstants.isFirst == 0) {

            if (doubleBackToExitPressedOnce) {
                exitProcess(0)
            }

            this.doubleBackToExitPressedOnce = true
            Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_SHORT).show()

            Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)

        } else if (AppConstants.isFirst == 5 || AppConstants.isFirst == 6 || AppConstants.isFirst == 8 ||
            AppConstants.isFirst == 16 || AppConstants.isFirst == 17 || AppConstants.isFirst == 18 || AppConstants.isFirst == 19) {

            AppConstants.isFirst = 4
            setFragments(null)

        } else if (AppConstants.isFirst == 7) {

            AppConstants.isFirst = 0
//            val intent = Intent(this@DashboardActivity, ForgotPasswordActivity::class.java)
//            startActivity(intent)
            finish()

        } else if (AppConstants.isFirst == 9) {

            curr_index1 = 0
            solution_grppos1 = 0

            AppConstants.isFirst = 10
            val bundle = Bundle()
            bundle.putString("testid", testid)
            bundle.putString("studenttestid", studenttestid)
            bundle.putString("testname", testname)

            setFragments(bundle)

        } else if (AppConstants.isFirst == 10) {
            if (Utils.getStringValue(this@DashboardActivity, AppConstants.IS_DEEPLINK_STEP, "") == "2" || Utils.getStringValue(this@DashboardActivity, AppConstants.IS_DEEPLINK_STEP, "") == "3") {

                Utils.setStringValue(this@DashboardActivity, AppConstants.IS_DEEPLINK_STEP, "4")

                AppConstants.isFirst = 0
                setFragments(null)

            } else {
                AppConstants.isFirst = 12
//            val bundle4 = Bundle()
//            bundle4.putString("pkgid", pkgid)
//            bundle4.putString("pname", pname)
                setFragments(null)
            }
        } else if (AppConstants.isFirst == 11) {

            AppConstants.isFirst = 1
            setFragments(null)

        } else if (AppConstants.isFirst == 12) {

            AppConstants.isFirst = 11
            val bundle3 = Bundle()
            bundle3.putInt("sub_id", subid)
            bundle3.putString("std_id", stdid)
            bundle3.putString("sub_name", subname)
//            if(isCompetitive){
//                bundle3.putString("board_id", )
//            }else {
            bundle3.putString("board_id", boardid)
//            }
            bundle3.putBoolean("isCompetitive", isCompetitive)
            setFragments(bundle3)

        } else if (AppConstants.isFirst == 13) {

            if (filtertypeid == "-1") {
                AppConstants.isFirst = 3
                setFragments(null)
            } else {
                AppConstants.isFirst = 0
                setFragments(null)
            }

        } else if (AppConstants.isFirst == 14 || AppConstants.isFirst == 1 || AppConstants.isFirst == 3 || AppConstants.isFirst == 4) {

            AppConstants.isFirst = 0
            setFragments(null)

        } else if (AppConstants.isFirst == 15) {

            supportFragmentManager.popBackStack()
        }
    }
}
