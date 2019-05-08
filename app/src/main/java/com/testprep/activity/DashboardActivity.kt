package com.testprep.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.testprep.R
import com.testprep.adapter.DrawerMenuListAdapter
import com.testprep.fragments.TestFragment
import com.testprep.fragments.UpdateProfileFragment
import com.testprep.utils.AppConstants
import com.testprep.utils.Utils
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity() {

    var mGoogleSignInClient: GoogleSignInClient? = null
    internal lateinit var mDrawerToggle: ActionBarDrawerToggle
    var drawerMenuListAdapter: DrawerMenuListAdapter? = null
    var menuList = arrayOf("Test", "Profile", "Logout")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        mDrawerToggle = object : ActionBarDrawerToggle(
            this, drawer_layout, R.drawable.menu, // nav menu toggle icon
            R.string.app_name, // nav drawer open - description for accessibility
            R.string.app_name // nav drawer close - description for accessibility
        ) {

        }

        drawer_layout.setDrawerListener(mDrawerToggle)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        init()

        supportFragmentManager.beginTransaction().add(R.id.container, UpdateProfileFragment()).commit()
    }

    private fun init() {

        dashboard_tvUsername.text = Utils.getStringValue(this@DashboardActivity, AppConstants.FIRST_NAME, "")

//        dashboard_rvMenuList.layoutManager = LinearLayoutManager(this@DashboardActivity, LinearLayoutManager.VERTICAL, false)
//        drawerMenuListAdapter = DrawerMenuListAdapter(this@DashboardActivity, menuList)
//        dashboard_rvMenuList.adapter = drawerMenuListAdapter

        dashboard_tvLogout.setOnClickListener {
            LoginManager.getInstance().logOut()
            signOut()
        }

        dashboard_ivMenu.setOnClickListener { drawer_layout.openDrawer(nav_view) }

        dashboard_tvtest.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.container, TestFragment()).commit()
            drawer_layout.closeDrawer(nav_view)

        }

        dashboard_tvedit.setOnClickListener {

            supportFragmentManager.beginTransaction().replace(R.id.container, UpdateProfileFragment()).commit()
            drawer_layout.closeDrawer(nav_view)

        }
    }

    private fun signOut() {
        mGoogleSignInClient!!.signOut()
            .addOnCompleteListener(this) {
                // ...

                val intent = Intent(this@DashboardActivity, LoginActivity ::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                finish()

            }
    }

}
