package com.testprep.activity

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.testprep.adapter.DrawerMenuListAdapter
import com.testprep.fragments.ChooseMarketPlaceFragment
import com.testprep.fragments.ExploreFragment
import com.testprep.fragments.MarketPlaceFragment
import com.testprep.fragments.OtherFragment
import com.testprep.utils.AppConstants
import com.testprep.utils.DialogUtils
import com.testprep.utils.Utils
import com.testprep.utils.Utils.Companion.clearPrefrence
import kotlinx.android.synthetic.main.activity_dashboard.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class DashboardActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    var mGoogleSignInClient: GoogleSignInClient? = null
    internal lateinit var mDrawerToggle: ActionBarDrawerToggle
    var drawerMenuListAdapter: DrawerMenuListAdapter? = null
    var menuList = arrayOf("Test", "Profile", "Logout")


    var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//        )
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setContentView(com.testprep.R.layout.activity_dashboard)

        AppConstants.ON_BACK = 0

//        mDrawerToggle = object : ActionBarDrawerToggle(
//            this, drawer_layout, R.drawable.menu, // nav menu toggle icon
//            R.string.app_name, // nav drawer open - description for accessibility
//            R.string.app_name // nav drawer close - description for accessibility
//        ) {
//        }

//        drawer_layout.setDrawerListener(mDrawerToggle)

        if (AppConstants.isFirst == 0) {

            val fragment = MarketPlaceFragment()
            var bundle = Bundle()
            bundle.putString("subid", intent.getStringExtra("subject_id"))
            fragment.arguments = bundle

            supportFragmentManager.beginTransaction().add(com.testprep.R.id.container, fragment).commit()
            dash_ivMarket.setImageResource(com.testprep.R.drawable.blue_list)
            dashboard_ivBack.visibility = View.VISIBLE
//        dashboard_ivPencil.visibility = View.VISIBLE

            dash_tvMarket.setTextColor(resources.getColor(com.testprep.R.color.nfcolor))
            dashboard_header.text = "Market Place"

        } else if (AppConstants.isFirst == 1) {

            supportFragmentManager.beginTransaction().add(com.testprep.R.id.container, ChooseMarketPlaceFragment())
                .commit()
            dash_ivHome.setImageResource(com.testprep.R.drawable.blue_home)
            dashboard_ivBack.visibility = View.GONE

            dash_tvHome.setTextColor(resources.getColor(com.testprep.R.color.nfcolor))
            dashboard_header.text = "My Dashboard"
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        dashboard_ivBack.setOnClickListener {

            AppConstants.ON_BACK = 1
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

                dashboard_header.text = "My Dashboard"

                supportFragmentManager.beginTransaction()
                    .replace(com.testprep.R.id.container, ChooseMarketPlaceFragment()).commit()

//                dashboard_ivFilter.visibility = View.GONE
                dashboard_ivCart.visibility = View.GONE
//                dashboard_ivPencil.visibility = View.GONE
                dashboard_ivBack.visibility = View.GONE
//                dashboard_ivFilter.visibility = View.GONE

                dash_ivHome.setImageResource(com.testprep.R.drawable.blue_home)
                dash_ivMarket.setImageResource(com.testprep.R.drawable.list)
                dash_ivSearch.setImageResource(com.testprep.R.drawable.search)
                dash_ivUser.setImageResource(com.testprep.R.drawable.menu_one)

                dash_tvHome.setTextColor(resources.getColor(com.testprep.R.color.nfcolor))
                dash_tvMarket.setTextColor(resources.getColor(com.testprep.R.color.light_gray))
                dash_tvSearch.setTextColor(resources.getColor(com.testprep.R.color.light_gray))
                dash_tvUser.setTextColor(resources.getColor(com.testprep.R.color.light_gray))
            }

            dash_llMarket -> {

                dashboard_header.text = "Market Place"

                val fragment = MarketPlaceFragment()
                var bundle = Bundle()
                bundle.putString("subid", intent.getStringExtra("subject_id"))
                fragment.arguments = bundle

                supportFragmentManager.beginTransaction().replace(com.testprep.R.id.container, fragment).commit()

//                dashboard_ivFilter.visibility = View.VISIBLE
                dashboard_ivCart.visibility = View.GONE
//                dashboard_ivPencil.visibility = View.VISIBLE
                dashboard_ivBack.visibility = View.VISIBLE
//                dashboard_ivPencil.visibility = View.VISIBLE

                dash_ivMarket.setImageResource(com.testprep.R.drawable.blue_list)
                dash_ivHome.setImageResource(com.testprep.R.drawable.home)
                dash_ivUser.setImageResource(com.testprep.R.drawable.menu_one)
                dash_ivSearch.setImageResource(com.testprep.R.drawable.search)

                dash_tvHome.setTextColor(resources.getColor(com.testprep.R.color.light_gray))
                dash_tvMarket.setTextColor(resources.getColor(com.testprep.R.color.nfcolor))
                dash_tvSearch.setTextColor(resources.getColor(com.testprep.R.color.light_gray))
                dash_tvUser.setTextColor(resources.getColor(com.testprep.R.color.light_gray))
            }

            dash_llExplore -> {

                dashboard_header.text = "Explore"

                supportFragmentManager.beginTransaction().replace(com.testprep.R.id.container, ExploreFragment())
                    .commit()

//                dashboard_ivFilter.visibility = View.GONE
                dashboard_ivCart.visibility = View.GONE
//                dashboard_ivPencil.visibility = View.GONE
                dashboard_ivBack.visibility = View.GONE
//                dashboard_ivFilter.visibility = View.GONE

                dash_ivSearch.setImageResource(com.testprep.R.drawable.blue_search)
                dash_ivUser.setImageResource(com.testprep.R.drawable.menu_one)
                dash_ivMarket.setImageResource(com.testprep.R.drawable.list)
                dash_ivHome.setImageResource(com.testprep.R.drawable.home)

                dash_tvHome.setTextColor(resources.getColor(com.testprep.R.color.light_gray))
                dash_tvMarket.setTextColor(resources.getColor(com.testprep.R.color.light_gray))
                dash_tvSearch.setTextColor(resources.getColor(com.testprep.R.color.nfcolor))
                dash_tvUser.setTextColor(resources.getColor(com.testprep.R.color.light_gray))
            }

            dash_llProfile -> {

                dashboard_header.text = "Other"

//                dashboard_ivFilter.visibility = View.GONE
                dashboard_ivCart.visibility = View.GONE
//                dashboard_ivPencil.visibility = View.GONE
                dashboard_ivBack.visibility = View.GONE
//                dashboard_ivFilter.visibility = View.GONE

//                dashboard_ivLogout.visibility = View.GONE
                supportFragmentManager.beginTransaction().replace(com.testprep.R.id.container, OtherFragment()).commit()

                dash_ivUser.setImageResource(com.testprep.R.drawable.blue_menu)
                dash_ivSearch.setImageResource(com.testprep.R.drawable.search)
                dash_ivMarket.setImageResource(com.testprep.R.drawable.list)
                dash_ivHome.setImageResource(com.testprep.R.drawable.home)

                dash_tvHome.setTextColor(resources.getColor(com.testprep.R.color.light_gray))
                dash_tvMarket.setTextColor(resources.getColor(com.testprep.R.color.light_gray))
                dash_tvSearch.setTextColor(resources.getColor(com.testprep.R.color.light_gray))
                dash_tvUser.setTextColor(resources.getColor(com.testprep.R.color.nfcolor))
            }
        }
    }

//    private fun init() {
//
//        dashboard_tvUsername.text = Utils.getStringValue(this@DashboardActivity, AppConstants.FIRST_NAME, "")
//
////        dashboard_rvMenuList.layoutManager = LinearLayoutManager(this@DashboardActivity, LinearLayoutManager.VERTICAL, false)
////        drawerMenuListAdapter = DrawerMenuListAdapter(this@DashboardActivity, menuList)
////        dashboard_rvMenuList.adapter = drawerMenuListAdapter
//
//        dashboard_tvLogout.setOnClickListener {
//            LoginManager.getInstance().logOut()
//            signOut()
//        }
//
//        dashboard_ivMenu.setOnClickListener { drawer_layout.openDrawer(nav_view) }
//
//        dashboard_tvhome.setOnClickListener {
//            supportFragmentManager.beginTransaction().replace(R.id.container, SelectCoarseFragment()).commit()
//            drawer_layout.closeDrawer(nav_view)
//
//        }
//
//        dashboard_tvNew.setOnClickListener {
//            val intent = Intent(this@DashboardActivity, NewActivity::class.java)
//            startActivity(intent)
//            drawer_layout.closeDrawer(nav_view)
//        }
//
//        dashboard_tvedit.setOnClickListener {
//
//            supportFragmentManager.beginTransaction().replace(R.id.container, UpdateProfileActivity()).commit()
//            drawer_layout.closeDrawer(nav_view)
//
//        }
//
//        dashboard_tvquestions.setOnClickListener {
//            //            supportFragmentManager.beginTransaction().replace(R.id.container, PageViewFragment()).commit()
//            //            drawer_layout.closeDrawer(nav_view)
//
//            val intent = Intent(this@DashboardActivity, TabwiseQuestionActivity::class.java)
//            startActivity(intent)
//            drawer_layout.closeDrawer(nav_view)
//        }
//
//        dashboard_tvmypackages.setOnClickListener {
//
//            supportFragmentManager.beginTransaction().replace(R.id.container, MyPackagesFragment()).commit()
//            drawer_layout.closeDrawer(nav_view)
//
//        }
//
//        dashboard_tvmypayments.setOnClickListener {
//
//            supportFragmentManager.beginTransaction().replace(R.id.container, MyPaymentActivity()).commit()
//            drawer_layout.closeDrawer(nav_view)
//
//        }
//
//        dashboard_tvCoin.setOnClickListener {
//            //            supportFragmentManager.beginTransaction().replace(R.id.container, PageViewFragment()).commit()
////            drawer_layout.closeDrawer(nav_view)
//
//            supportFragmentManager.beginTransaction().replace(R.id.container, CoinActivity()).commit()
//            drawer_layout.closeDrawer(nav_view)
//        }
//
//    }

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

    override fun onBackPressed() {

        when {
            AppConstants.ON_BACK == 1 -> {
                super.onBackPressed()
            }
            AppConstants.ON_BACK == 2 ->
                supportFragmentManager.beginTransaction().replace(
                    com.testprep.R.id.container,
                    MarketPlaceFragment()
                ).commit()
            else -> {

                this.doubleBackToExitPressedOnce = true
                Toast.makeText(this, "Please click BACK again to exit.", Toast.LENGTH_SHORT).show()

                Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
            }
        }
    }
}
