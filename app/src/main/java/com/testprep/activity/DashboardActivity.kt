package com.testprep.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.testprep.R
import com.testprep.adapter.DrawerMenuListAdapter
import com.testprep.fragments.ChooseMarketPlaceFragment
import com.testprep.fragments.MyPackagesFragment
import com.testprep.utils.AppConstants
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

//    super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setContentView(R.layout.activity_dashboard)

//        mDrawerToggle = object : ActionBarDrawerToggle(
//            this, drawer_layout, R.drawable.menu, // nav menu toggle icon
//            R.string.app_name, // nav drawer open - description for accessibility
//            R.string.app_name // nav drawer close - description for accessibility
//        ) {
//        }

//        drawer_layout.setDrawerListener(mDrawerToggle)

        supportFragmentManager.beginTransaction().add(R.id.container, MyPackagesFragment()).commit()
        dash_ivHome.setImageResource(R.drawable.blue_home)
        dash_tvHome.setTextColor(resources.getColor(R.color.nfcolor))
        dashboard_header.text = "My Packages"

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        dashboard_ivLogout.setOnClickListener { signOut() }

//        init()

//        supportFragmentManager.beginTransaction().add(R.id.container, SelectCoarseFragment()).commit()
    }

    fun onClick(v: View) {
        when (v) {

            dashboard_ivLogout -> {
                signOut()
            }

            dash_llDashboard -> {

                dashboard_header.text = "My Packages"

                supportFragmentManager.beginTransaction().add(R.id.container, MyPackagesFragment()).commit()

                dash_ivHome.setImageResource(R.drawable.blue_home)
                dash_ivMarket.setImageResource(R.drawable.list)
                dash_ivSearch.setImageResource(R.drawable.search)
                dash_ivUser.setImageResource(R.drawable.user)

                dash_tvHome.setTextColor(resources.getColor(R.color.nfcolor))
                dash_tvMarket.setTextColor(resources.getColor(R.color.light_gray))
                dash_tvSearch.setTextColor(resources.getColor(R.color.light_gray))
                dash_tvUser.setTextColor(resources.getColor(R.color.light_gray))
            }

            dash_llMarket -> {

                dashboard_header.text = "Market Place"

                supportFragmentManager.beginTransaction().add(R.id.container, ChooseMarketPlaceFragment()).commit()

                dash_ivMarket.setImageResource(R.drawable.blue_list)
                dash_ivHome.setImageResource(R.drawable.home)
                dash_ivUser.setImageResource(R.drawable.user)
                dash_ivSearch.setImageResource(R.drawable.search)

                dash_tvHome.setTextColor(resources.getColor(R.color.light_gray))
                dash_tvMarket.setTextColor(resources.getColor(R.color.nfcolor))
                dash_tvSearch.setTextColor(resources.getColor(R.color.light_gray))
                dash_tvUser.setTextColor(resources.getColor(R.color.light_gray))
            }

            dash_llExplore -> {

                dashboard_header.text = "Explore"

                dash_ivSearch.setImageResource(R.drawable.blue_search)
                dash_ivUser.setImageResource(R.drawable.user)
                dash_ivMarket.setImageResource(R.drawable.list)
                dash_ivHome.setImageResource(R.drawable.home)

                dash_tvHome.setTextColor(resources.getColor(R.color.light_gray))
                dash_tvMarket.setTextColor(resources.getColor(R.color.light_gray))
                dash_tvSearch.setTextColor(resources.getColor(R.color.nfcolor))
                dash_tvUser.setTextColor(resources.getColor(R.color.light_gray))
            }

            dash_llProfile -> {

                dashboard_header.text = "Profile"

                dash_ivUser.setImageResource(R.drawable.blue_user)
                dash_ivSearch.setImageResource(R.drawable.search)
                dash_ivMarket.setImageResource(R.drawable.list)
                dash_ivHome.setImageResource(R.drawable.home)

                dash_tvHome.setTextColor(resources.getColor(R.color.light_gray))
                dash_tvMarket.setTextColor(resources.getColor(R.color.light_gray))
                dash_tvSearch.setTextColor(resources.getColor(R.color.light_gray))
                dash_tvUser.setTextColor(resources.getColor(R.color.nfcolor))
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
//            supportFragmentManager.beginTransaction().replace(R.id.container, UpdateProfileFragment()).commit()
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
//            supportFragmentManager.beginTransaction().replace(R.id.container, MyPaymentFragment()).commit()
//            drawer_layout.closeDrawer(nav_view)
//
//        }
//
//        dashboard_tvCoin.setOnClickListener {
//            //            supportFragmentManager.beginTransaction().replace(R.id.container, PageViewFragment()).commit()
////            drawer_layout.closeDrawer(nav_view)
//
//            supportFragmentManager.beginTransaction().replace(R.id.container, CoinFragment()).commit()
//            drawer_layout.closeDrawer(nav_view)
//        }
//
//    }


    private fun signOut() {

        clearPrefrence(this@DashboardActivity)
        AppConstants.COURSE_FLOW_ARRAY.clear()

        mGoogleSignInClient!!.signOut()
            .addOnCompleteListener(this) {
                // ...

                Utils.setStringValue(this@DashboardActivity, "is_login", "false")

                val intent = Intent(this@DashboardActivity, IntroActivity::class.java)
                startActivity(intent)
//                overridePendingTransition(R.anim.slide_in_leftt, R.anim.slide_out_right)
                finish()

            }
    }

}
