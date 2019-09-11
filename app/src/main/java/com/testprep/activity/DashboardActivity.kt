package com.testprep.activity

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActionBarDrawerToggle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.testprep.adapter.DrawerMenuListAdapter
import com.testprep.fragments.*
import com.testprep.models.PackageData
import com.testprep.utils.AppConstants
import com.testprep.utils.DialogUtils
import com.testprep.utils.Utils
import com.testprep.utils.Utils.Companion.clearPrefrence
import kotlinx.android.synthetic.main.activity_dashboard.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import kotlin.system.exitProcess


class DashboardActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    var mGoogleSignInClient: GoogleSignInClient? = null
    internal lateinit var mDrawerToggle: ActionBarDrawerToggle
    var drawerMenuListAdapter: DrawerMenuListAdapter? = null
    var menuList = arrayOf("Test", "Profile", "Logout")
    var ON_BACK = 0

    var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//        )
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setContentView(com.testprep.R.layout.activity_dashboard)

        ON_BACK = 0

        main_header = findViewById(com.testprep.R.id.dashboard_header)
        btnBack = findViewById(com.testprep.R.id.dashboard_ivBack)
        btnLogout = findViewById(com.testprep.R.id.dashboard_ivLogout)

        intent1 = intent
        fragManager = this.supportFragmentManager

//        containerr = findViewById(R.id.container)

//        mDrawerToggle = object : ActionBarDrawerToggle(
//            this, drawer_layout, R.drawable.menu, // nav menu toggle icon
//            R.string.app_name, // nav drawer open - description for accessibility
//            R.string.app_name // nav drawer close - description for accessibility
//        ) {
//        }

//        drawer_layout.setDrawerListener(mDrawerToggle)

        setFragments(null)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        dashboard_ivBack.setOnClickListener {

            onBackPressed()

        }

        dashboard_ivLogout.setOnClickListener { signOut() }

//        init()

//        supportFragmentManager.beginTransaction().add(R.id.container, SelectCoarseFragment()).commit()

        AppConstants.FILTER_STANDARD_ID = "111"
        AppConstants.FILTER_SUBJECT_ID = "111"
        AppConstants.FILTER_TUTOR_ID = "111"
        AppConstants.FILTER_BOARD_ID = "111"

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

            dashboard_ivLogout -> {
                signOut()
            }

            dash_llDashboard -> {

                AppConstants.isFirst = 1

                //                dashboard_ivFilter.visibility = View.GONE
                dashboard_ivCart.visibility = View.GONE
//                dashboard_ivPencil.visibility = View.GONE
//                dashboard_ivBack.visibility = View.GONE
//                dashboard_ivFilter.visibility = View.GONE

                dash_ivHome.setImageResource(com.testprep.R.drawable.blue_home)
                dash_ivMarket.setImageResource(com.testprep.R.drawable.list)
                dash_ivSearch.setImageResource(com.testprep.R.drawable.search)
                dash_ivUser.setImageResource(com.testprep.R.drawable.menu_one)

                dash_tvHome.setTextColor(resources.getColor(com.testprep.R.color.nfcolor))
                dash_tvMarket.setTextColor(resources.getColor(com.testprep.R.color.light_gray))
                dash_tvSearch.setTextColor(resources.getColor(com.testprep.R.color.light_gray))
                dash_tvUser.setTextColor(resources.getColor(com.testprep.R.color.light_gray))

                setFragments(null)
            }

            dash_llMarket -> {

                AppConstants.isFirst = 0

                //                var bundle = Bundle()
//                bundle.putString("subid", intent.getStringExtra("subject_id"))
//                fragment.arguments = bundle

//                dashboard_ivFilter.visibility = View.VISIBLE
                dashboard_ivCart.visibility = View.GONE
//                dashboard_ivPencil.visibility = View.VISIBLE
//                dashboard_ivBack.visibility = View.VISIBLE
//                dashboard_ivPencil.visibility = View.VISIBLE

                dash_ivMarket.setImageResource(com.testprep.R.drawable.blue_list)
                dash_ivHome.setImageResource(com.testprep.R.drawable.home)
                dash_ivUser.setImageResource(com.testprep.R.drawable.menu_one)
                dash_ivSearch.setImageResource(com.testprep.R.drawable.search)

                dash_tvHome.setTextColor(resources.getColor(com.testprep.R.color.light_gray))
                dash_tvMarket.setTextColor(resources.getColor(com.testprep.R.color.nfcolor))
                dash_tvSearch.setTextColor(resources.getColor(com.testprep.R.color.light_gray))
                dash_tvUser.setTextColor(resources.getColor(com.testprep.R.color.light_gray))

                setFragments(null)
            }

            dash_llExplore -> {

                AppConstants.isFirst = 3

                //                supportFragmentManager.beginTransaction().replace(R.id.container, ExploreFragment())
//                    .commit()

//                dashboard_ivFilter.visibility = View.GONE
                dashboard_ivCart.visibility = View.GONE
//                dashboard_ivPencil.visibility = View.GONE
//                dashboard_ivBack.visibility = View.GONE
//                dashboard_ivFilter.visibility = View.GONE

                dash_ivSearch.setImageResource(com.testprep.R.drawable.blue_search)
                dash_ivUser.setImageResource(com.testprep.R.drawable.menu_one)
                dash_ivMarket.setImageResource(com.testprep.R.drawable.list)
                dash_ivHome.setImageResource(com.testprep.R.drawable.home)

                dash_tvHome.setTextColor(resources.getColor(com.testprep.R.color.light_gray))
                dash_tvMarket.setTextColor(resources.getColor(com.testprep.R.color.light_gray))
                dash_tvSearch.setTextColor(resources.getColor(com.testprep.R.color.nfcolor))
                dash_tvUser.setTextColor(resources.getColor(com.testprep.R.color.light_gray))

                setFragments(null)
            }

            dash_llProfile -> {

                AppConstants.isFirst = 4

                //                dashboard_ivFilter.visibility = View.GONE
                dashboard_ivCart.visibility = View.GONE
//                dashboard_ivPencil.visibility = View.GONE
//                dashboard_ivBack.visibility = View.GONE
//                dashboard_ivFilter.visibility = View.GONE

//                dashboard_ivLogout.visibility = View.GONE
//                supportFragmentManager.beginTransaction().replace(R.id.container, OtherFragment()).commit()

                dash_ivUser.setImageResource(com.testprep.R.drawable.blue_menu)
                dash_ivSearch.setImageResource(com.testprep.R.drawable.search)
                dash_ivMarket.setImageResource(com.testprep.R.drawable.list)
                dash_ivHome.setImageResource(com.testprep.R.drawable.home)

                dash_tvHome.setTextColor(resources.getColor(com.testprep.R.color.light_gray))
                dash_tvMarket.setTextColor(resources.getColor(com.testprep.R.color.light_gray))
                dash_tvSearch.setTextColor(resources.getColor(com.testprep.R.color.light_gray))
                dash_tvUser.setTextColor(resources.getColor(com.testprep.R.color.nfcolor))

                setFragments(null)
            }
        }
    }

    private fun signOut() {

        DialogUtils.createConfirmDialog(
            this@DashboardActivity,
            "Logout?",
            "Are you sure you want to logout?",
            "Yes",
            "No",
            DialogInterface.OnClickListener { dialog, which ->
                clearPrefrence(this@DashboardActivity)
                AppConstants.COURSE_FLOW_ARRAY.clear()

                AppConstants.FILTER_STANDARD_ID = "111"
                AppConstants.FILTER_SUBJECT_ID = "111"
                AppConstants.FILTER_TUTOR_ID = "111"
                AppConstants.FILTER_BOARD_ID = "111"

                mGoogleSignInClient!!.signOut()
                    .addOnCompleteListener(this) {
                        // ...

                        Utils.setStringValue(this@DashboardActivity, "is_login", "false")

                        val intent = Intent(this@DashboardActivity, IntroActivity::class.java)
                        startActivity(intent)
//                overridePendingTransition(R.anim.slide_in_leftt, R.anim.slide_out_right)
                        finish()

                    }
            },
            DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()

            }).show()
    }

    companion object {
        var fragment: Fragment? = null
        var testid = ""
        var studenttestid = ""
        var subid = 0
        var subname = ""
        var pkgid = ""
        var pname = ""
        var isCompetitive = false
        var main_header: TextView? = null
        var btnBack: ImageView? = null
        var btnLogout: ImageView? = null
        var boardid = ""
        var stdid = ""
        var tutorid = ""
        var ptype = ""
        var subid1 = ""
        var parr: ArrayList<PackageData.PackageDataList> = ArrayList()
        var maxprice = ""
        var minprice = ""
        var search_name = ""

        var intent1: Intent? = null
        var fragManager: FragmentManager? = null

        fun setFragments(bundle: Bundle?) {

//            var bundle = intent1!!.extras

            when (AppConstants.isFirst) {
                0 -> {

                    fragment = MarketPlaceFragment()

                    main_header!!.text = "Market Place"
                    btnBack!!.visibility = View.VISIBLE
                    btnLogout!!.visibility = View.VISIBLE
                    //        dashboard_ivPencil.visibility = View.VISIBLE

                }
                1 -> {

                    fragment = ChooseMarketPlaceFragment()

                    main_header!!.text = "My Dashboard"
                    btnBack!!.visibility = View.GONE
                    btnLogout!!.visibility = View.VISIBLE

                }

                3 -> {

                    fragment = ExploreFragment()

                    main_header!!.text = "Explore"
                    btnBack!!.visibility = View.GONE
                    btnLogout!!.visibility = View.VISIBLE
                }
                4 -> {

                    fragment = OtherFragment()

                    main_header!!.text = "Other"
                    btnBack!!.visibility = View.GONE
                    btnLogout!!.visibility = View.VISIBLE
                }
                5 -> {

                    fragment = MyPaymentActivity()

                    main_header!!.text = "My Payments"
                    btnBack!!.visibility = View.VISIBLE
                    btnLogout!!.visibility = View.GONE

                }
                6 -> {

                    fragment = ChangePasswordActivity()
                    val bundle1 = Bundle()
                    bundle1.putString("come_from", "other")

                    (fragment as ChangePasswordActivity).arguments = bundle1

                    main_header!!.text = "Change Password"
                    btnBack!!.visibility = View.VISIBLE
                    btnLogout!!.visibility = View.GONE

                }
                7 -> {

                    fragment = ChangePasswordActivity()
                    val bundle2 = Bundle()
                    bundle2.putString("come_from", "otp")

                    (fragment as ChangePasswordActivity).arguments = bundle2

                    main_header!!.text = "Change Password"
                    btnBack!!.visibility = View.VISIBLE
                    btnLogout!!.visibility = View.GONE

                }
                8 -> {

                    fragment = UpdateProfileActivity()

                    main_header!!.text = "Profile"
                    btnBack!!.visibility = View.VISIBLE
                    btnLogout!!.visibility = View.GONE

                }
                9 -> {

                    testid = bundle!!.getString("testid")!!
                    studenttestid = bundle.getString("studenttestid")!!

                    fragment = ViewSolutionActivity()
                    val bundle6 = Bundle()
                    bundle6.putString("testid", testid)
                    bundle6.putString("studenttestid", studenttestid)

                    (fragment as ViewSolutionActivity).arguments = bundle6

                    main_header!!.text = "View Solution"
                    btnBack!!.visibility = View.VISIBLE
                    btnLogout!!.visibility = View.GONE

                }
                10 -> {

                    testid = bundle!!.getString("testid")!!
                    studenttestid = bundle.getString("studenttestid")!!

                    fragment = TestReviewActivity()
                    val bundle5 = Bundle()
                    bundle5.putString("testid", testid)
                    bundle5.putString("studenttestid", studenttestid)

                    (fragment as TestReviewActivity).arguments = bundle5

                    main_header!!.text = "Analysis"
                    btnBack!!.visibility = View.VISIBLE
                    btnLogout!!.visibility = View.GONE

                }
                11 -> {

                    subid = bundle!!.getInt("sub_id", 0)
                    subname = bundle.getString("sub_name")!!
                    isCompetitive = bundle.getBoolean("isCompetitive", false)

                    fragment = MyPackagesFragment()
                    val bundle3 = Bundle()
                    bundle3.putInt("sub_id", subid)
                    bundle3.putString("sub_name", subname)
                    bundle3.putBoolean("isCompetitive", isCompetitive)

                    (fragment as MyPackagesFragment).arguments = bundle3

                    main_header!!.text = bundle.getString("sub_name")
                    btnBack!!.visibility = View.VISIBLE
                    btnLogout!!.visibility = View.GONE

                }
                12 -> {

                    pkgid = bundle!!.getString("pkgid")!!
                    pname = bundle.getString("pname")!!

                    fragment = TestListActivity()
                    val bundle4 = Bundle()
                    bundle4.putString("pkgid", pkgid)
                    bundle4.putString("pname", pname)

                    (fragment as TestListActivity).arguments = bundle4

                    main_header!!.text = pname
                    btnBack!!.visibility = View.VISIBLE
                    btnLogout!!.visibility = View.GONE

                }
                13 -> {

                    ptype = bundle!!.getString("type")!!
                    pname = bundle.getString("pname1")!!
                    boardid = bundle.getString("boardid")!!
                    stdid = bundle.getString("stdid")!!
                    subid1 = bundle.getString("subid")!!
                    tutorid = bundle.getString("tutorid")!!

                    if (bundle.containsKey("parr")) {
                        parr = (bundle.getSerializable("parr") as ArrayList<PackageData.PackageDataList>?)!!
                    }
                    maxprice = bundle.getString("maxprice")!!
                    minprice = bundle.getString("minprice")!!
                    search_name = bundle.getString("search_name")!!

                    fragment = TutorDetailActivity()
                    val bundle7 = Bundle()
                    bundle7.putString("type", ptype)
                    bundle7.putString("pname", pname)
                    bundle7.putString("boardid", boardid)
                    bundle7.putString("stdid", stdid)
                    bundle7.putString("subid", subid1)
                    bundle7.putString("tutorid", tutorid)

                    if (parr.size > 0) {
                        bundle7.putSerializable("parr", parr)
                    }
                    bundle7.putString("maxprice", maxprice)
                    bundle7.putString("minprice", minprice)
                    bundle7.putString("search_name", search_name)

                    (fragment as TutorDetailActivity).arguments = bundle7

                    main_header!!.text = pname
                    btnBack!!.visibility = View.VISIBLE
                    btnLogout!!.visibility = View.GONE

                }
            }

            fragManager!!.beginTransaction().replace(com.testprep.R.id.container, fragment!!).commitAllowingStateLoss()

        }
    }

    override fun onBackPressed() {

        if (currentFocus != null) {
            val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        }

        if (AppConstants.isFirst == 0) {
//                AppConstants.ON_BACK = 1

            this.doubleBackToExitPressedOnce = true
            Toast.makeText(this, "Please click back again to exit.", Toast.LENGTH_SHORT).show()

            Handler().postDelayed({
                doubleBackToExitPressedOnce = false
                exitProcess(0)
            }, 2000)

        } else if (AppConstants.isFirst == 5 || AppConstants.isFirst == 6 || AppConstants.isFirst == 7 || AppConstants.isFirst == 8) {

            AppConstants.isFirst = 4
            setFragments(null)
        } else if (AppConstants.isFirst == 9) {

            AppConstants.isFirst = 10
            val bundle = Bundle()
            bundle.putString("testid", testid)
            bundle.putString("studenttestid", studenttestid)
            setFragments(bundle)
        } else if (AppConstants.isFirst == 10) {

            AppConstants.isFirst = 12
            val bundle4 = Bundle()
            bundle4.putString("pkgid", pkgid)
            bundle4.putString("pname", pname)
            setFragments(bundle4)
        } else if (AppConstants.isFirst == 11) {

            AppConstants.isFirst = 1
            setFragments(null)

        } else if (AppConstants.isFirst == 12) {

            AppConstants.isFirst = 11
            val bundle3 = Bundle()
            bundle3.putInt("sub_id", subid)
            bundle3.putString("sub_name", subname)
            bundle3.putBoolean("isCompetitive", isCompetitive)
            setFragments(bundle3)
        } else if (AppConstants.isFirst == 13) {

            AppConstants.isFirst = 0
            setFragments(null)
        }

    }
}
