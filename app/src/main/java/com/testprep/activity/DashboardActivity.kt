package com.testprep.activity

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActionBarDrawerToggle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.testprep.R
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
import java.lang.System.exit
import kotlin.system.exitProcess

class DashboardActivity : AppCompatActivity() {

    companion object {
        var containerr: Int? = null
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    var mGoogleSignInClient: GoogleSignInClient? = null
    internal lateinit var mDrawerToggle: ActionBarDrawerToggle
    var drawerMenuListAdapter: DrawerMenuListAdapter? = null
    var menuList = arrayOf("Test", "Profile", "Logout")
    var ON_BACK = 0

    var testid = ""
    var studenttestid = ""

    var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//        )
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setContentView(R.layout.activity_dashboard)

        ON_BACK = 0

//        containerr = findViewById(R.id.container)

//        mDrawerToggle = object : ActionBarDrawerToggle(
//            this, drawer_layout, R.drawable.menu, // nav menu toggle icon
//            R.string.app_name, // nav drawer open - description for accessibility
//            R.string.app_name // nav drawer close - description for accessibility
//        ) {
//        }

//        drawer_layout.setDrawerListener(mDrawerToggle)

        setFragments()

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
                setFragments()
            }

            dash_llMarket -> {

                AppConstants.isFirst = 0
                setFragments()
            }

            dash_llExplore -> {

                AppConstants.isFirst = 3
                setFragments()
            }

            dash_llProfile -> {

                AppConstants.isFirst = 4
                setFragments()
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

    var fragment: Fragment? = null
    fun setFragments() {

        when (AppConstants.isFirst) {
            0 -> {

                dashboard_header.text = "Market Place"

                fragment = MarketPlaceFragment()
//                var bundle = Bundle()
//                bundle.putString("subid", intent.getStringExtra("subject_id"))
//                fragment.arguments = bundle

//                dashboard_ivFilter.visibility = View.VISIBLE
                dashboard_ivCart.visibility = View.GONE
//                dashboard_ivPencil.visibility = View.VISIBLE
                dashboard_ivBack.visibility = View.VISIBLE
//                dashboard_ivPencil.visibility = View.VISIBLE

                dash_ivMarket.setImageResource(R.drawable.blue_list)
                dash_ivHome.setImageResource(R.drawable.home)
                dash_ivUser.setImageResource(R.drawable.menu_one)
                dash_ivSearch.setImageResource(R.drawable.search)

                dash_tvHome.setTextColor(resources.getColor(R.color.light_gray))
                dash_tvMarket.setTextColor(resources.getColor(R.color.nfcolor))
                dash_tvSearch.setTextColor(resources.getColor(R.color.light_gray))
                dash_tvUser.setTextColor(resources.getColor(R.color.light_gray))

                //        dashboard_ivPencil.visibility = View.VISIBLE

            }
            1 -> {

                dashboard_header.text = "My Dashboard"

               fragment = ChooseMarketPlaceFragment()

//                dashboard_ivFilter.visibility = View.GONE
                dashboard_ivCart.visibility = View.GONE
//                dashboard_ivPencil.visibility = View.GONE
                dashboard_ivBack.visibility = View.GONE
//                dashboard_ivFilter.visibility = View.GONE

                dash_ivHome.setImageResource(R.drawable.blue_home)
                dash_ivMarket.setImageResource(R.drawable.list)
                dash_ivSearch.setImageResource(R.drawable.search)
                dash_ivUser.setImageResource(R.drawable.menu_one)

                dash_tvHome.setTextColor(resources.getColor(R.color.nfcolor))
                dash_tvMarket.setTextColor(resources.getColor(R.color.light_gray))
                dash_tvSearch.setTextColor(resources.getColor(R.color.light_gray))
                dash_tvUser.setTextColor(resources.getColor(R.color.light_gray))

            }

            3 -> {
                dashboard_header.text = "Explore"

                fragment = ExploreFragment()

//                supportFragmentManager.beginTransaction().replace(R.id.container, ExploreFragment())
//                    .commit()

//                dashboard_ivFilter.visibility = View.GONE
                dashboard_ivCart.visibility = View.GONE
//                dashboard_ivPencil.visibility = View.GONE
                dashboard_ivBack.visibility = View.GONE
//                dashboard_ivFilter.visibility = View.GONE

                dash_ivSearch.setImageResource(R.drawable.blue_search)
                dash_ivUser.setImageResource(R.drawable.menu_one)
                dash_ivMarket.setImageResource(R.drawable.list)
                dash_ivHome.setImageResource(R.drawable.home)

                dash_tvHome.setTextColor(resources.getColor(R.color.light_gray))
                dash_tvMarket.setTextColor(resources.getColor(R.color.light_gray))
                dash_tvSearch.setTextColor(resources.getColor(R.color.nfcolor))
                dash_tvUser.setTextColor(resources.getColor(R.color.light_gray))
            }
            4 -> {

                dashboard_header.text = "Other"

                fragment = OtherFragment()
                //                dashboard_ivFilter.visibility = View.GONE
                dashboard_ivCart.visibility = View.GONE
//                dashboard_ivPencil.visibility = View.GONE
                dashboard_ivBack.visibility = View.GONE
//                dashboard_ivFilter.visibility = View.GONE

//                dashboard_ivLogout.visibility = View.GONE
//                supportFragmentManager.beginTransaction().replace(R.id.container, OtherFragment()).commit()

                dash_ivUser.setImageResource(R.drawable.blue_menu)
                dash_ivSearch.setImageResource(R.drawable.search)
                dash_ivMarket.setImageResource(R.drawable.list)
                dash_ivHome.setImageResource(R.drawable.home)

                dash_tvHome.setTextColor(resources.getColor(R.color.light_gray))
                dash_tvMarket.setTextColor(resources.getColor(R.color.light_gray))
                dash_tvSearch.setTextColor(resources.getColor(R.color.light_gray))
                dash_tvUser.setTextColor(resources.getColor(R.color.nfcolor))
            }
            5 -> {
                dashboard_header.text = "My Payments"

                fragment = MyPaymentActivity()
//                supportFragmentManager.beginTransaction().add(R.id.container, MyPaymentActivity()).commit()
                dash_ivUser.setImageResource(R.drawable.blue_menu)
                dashboard_ivBack.visibility = View.VISIBLE
                dashboard_ivLogout.visibility = View.GONE

                dash_tvUser.setTextColor(resources.getColor(R.color.nfcolor))

            }
            6 -> {
                dashboard_header.text = getString(R.string.change_pass)

                fragment = ChangePasswordActivity()
                val bundle = Bundle()
                bundle.putString("come_from", "other")

                (fragment as ChangePasswordActivity).arguments = bundle
//                supportFragmentManager.beginTransaction().add(R.id.container, fragment).commit()

                dash_ivUser.setImageResource(R.drawable.blue_menu)
                dashboard_ivBack.visibility = View.VISIBLE
                dashboard_ivLogout.visibility = View.GONE

                dash_tvUser.setTextColor(resources.getColor(R.color.nfcolor))

            }
            7 -> {
                dashboard_header.text = getString(R.string.change_pass)

                fragment = ChangePasswordActivity()
                val bundle = Bundle()
                bundle.putString("come_from", "otp")

                (fragment as ChangePasswordActivity).arguments = bundle
//                supportFragmentManager.beginTransaction().add(R.id.container, fragment).commit()

                dash_ivUser.setImageResource(R.drawable.blue_menu)
                dashboard_ivBack.visibility = View.VISIBLE
                dashboard_ivLogout.visibility = View.GONE

                dash_tvUser.setTextColor(resources.getColor(R.color.nfcolor))

            }
            8 -> {
                dashboard_header.text = "Profile"

                fragment = UpdateProfileActivity()

//                supportFragmentManager.beginTransaction().add(R.id.container, UpdateProfileActivity()).commit()

                dash_ivUser.setImageResource(R.drawable.blue_menu)
                dashboard_ivBack.visibility = View.VISIBLE
                dashboard_ivLogout.visibility = View.GONE

                dash_tvUser.setTextColor(resources.getColor(R.color.nfcolor))

            }
            9 -> {

                testid = intent.getStringExtra("testid")
                studenttestid = intent.getStringExtra("studenttestid")

                dashboard_header.text = "View Solution"

                fragment = ViewSolutionActivity()
                val bundle = Bundle()
                bundle.putString("testid", testid)
                bundle.putString("studenttestid", studenttestid)

                (fragment as ViewSolutionActivity).arguments = bundle
//                supportFragmentManager.beginTransaction().add(R.id.container, UpdateProfileActivity()).commit()

                dash_ivHome.setImageResource(R.drawable.blue_home)
                dashboard_ivBack.visibility = View.VISIBLE
                dashboard_ivLogout.visibility = View.GONE

                dash_tvHome.setTextColor(resources.getColor(R.color.nfcolor))

            }
            10 -> {

                testid = intent.getStringExtra("testid")
                studenttestid = intent.getStringExtra("studenttestid")

                dashboard_header.text = "Analysis"

                fragment = TestReviewActivity()
                val bundle = Bundle()
                bundle.putString("testid", testid)
                bundle.putString("studenttestid", studenttestid)

                (fragment as TestReviewActivity).arguments = bundle
//                supportFragmentManager.beginTransaction().add(R.id.container, UpdateProfileActivity()).commit()

                dash_ivHome.setImageResource(R.drawable.blue_home)
                dashboard_ivBack.visibility = View.VISIBLE
                dashboard_ivLogout.visibility = View.GONE

                dash_tvHome.setTextColor(resources.getColor(R.color.nfcolor))

            }
        }

        supportFragmentManager.beginTransaction().replace(R.id.container, fragment!!).commit()

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
            setFragments()
        } else if (AppConstants.isFirst == 9) {

            AppConstants.isFirst = 10
            setFragments()
        }else if (AppConstants.isFirst == 10) {

            super.onBackPressed()
        }

//        when {
//            ON_BACK == 1 -> {
//                super.onBackPressed()
//            }
//            ON_BACK == 2 ->
//                supportFragmentManager.beginTransaction().replace(
//                    R.id.container,
//                    MarketPlaceFragment()
//                ).commit()
//            else -> {
//
//
//            }
//        }
    }
}
