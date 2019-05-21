package com.testprep.old

import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import com.testprep.R
import kotlinx.android.synthetic.main.activity_main2.*

class Main2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

//        supportFragmentManager.beginTransaction().add(R.id.container, ListViewFragment()).commit()

//        list_view.setOnClickListener { v: View? ->
//            drawer_layout.closeDrawers()
//            supportFragmentManager.beginTransaction().replace(R.id.container, ListViewFragment()).commit()
//
//        }

//        image_view.setOnClickListener { v: View? ->
//            drawer_layout.closeDrawers()
//            supportFragmentManager.beginTransaction().replace(
//                R.id.container,
//                ImageListViewFragment()
//            ).commit()
//
//        }

//        page_view.setOnClickListener { v: View? ->
//            drawer_layout.closeDrawers()
//
//            val intent = Intent(this@Main2Activity, PageActivity::class.java)
//            startActivity(intent)
//
//        }

//        single_web_view.setOnClickListener { v: View? ->
//            drawer_layout.closeDrawers()
//
//            val intent1 = Intent(this@Main2Activity, WebviewPageActivity::class.java)
//            startActivity(intent1)
//
//        }

//        nav_view.setNavigationItemSelectedListener { menuItem ->
//            drawer_layout.closeDrawers()
//
//            val itemId = menuItem.itemId
//
//
//                            when (menuItem.itemId) {
//                                R.id.list_view ->
//                                    supportFragmentManager.beginTransaction().replace(R.id.container, ListViewFragment()).commit()
//                                R.id.page_view ->
//                                    supportFragmentManager.beginTransaction().replace(R.id.container, PageViewFragment()).commit()
//                                R.id.image_view ->
//                                    supportFragmentManager.beginTransaction().replace(R.id.container, ImageListViewFragment()).commit()
//                            }
//                //navigationView.getMenu().findItem(R.id.drawer_5_reasons).setChecked(true);
//                true
//            }

//        nav_view.setNavigationItemSelectedListener {item: MenuItem
//
//            when (item.itemId) {
//                R.id.list_view ->
//                    supportFragmentManager.beginTransaction().replace(R.id.container, ListViewFragment()).commit()
//                R.id.page_view ->
//                    supportFragmentManager.beginTransaction().replace(R.id.container, PageViewFragment()).commit()
//                R.id.image_view ->
//                    supportFragmentManager.beginTransaction().replace(R.id.container, ImageListViewFragment()).commit()
//            }
//
//            drawer_layout.closeDrawer(GravityCompat.START)
//            return true
//
//        }

        }

        override fun onBackPressed() {
            if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                drawer_layout.closeDrawer(GravityCompat.START)
            } else {
                super.onBackPressed()
            }
        }
    }
