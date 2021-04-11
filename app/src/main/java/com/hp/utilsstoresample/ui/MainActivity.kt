package com.hp.utilsstoresample.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.hp.utils_store.widget.bottombar.BottomBar
import com.hp.utils_store.widget.bottombar.BottomBarTab
import com.hp.utilsstoresample.R
import com.hp.utilsstoresample.ui.base.BaseActivity

class MainActivity : BaseActivity() {

    private val mFragments = arrayOfNulls<Fragment>(3)
    private lateinit var mBottomBar: BottomBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
    }

    private fun initView() {
        mBottomBar = findViewById(R.id.bottomBar)
        mBottomBar.addItem(BottomBarTab(this, R.drawable.ic_home_black_24dp, getString(R.string.title_home)))
            .addItem(BottomBarTab(this, R.drawable.ic_dashboard_black_24dp, getString(R.string.title_dashboard)))
            .addItem(BottomBarTab(this, R.drawable.ic_notifications_black_24dp, getString(R.string.title_notifications)))
        mBottomBar.setOnTabSelectedListener(onTabSelectedListener)
        mBottomBar.setCurrentItem(0)
    }

    private val onTabSelectedListener = object : BottomBar.OnTabSelectedListener {
            override fun onTabSelected(position: Int, prePosition: Int) {
//                showHideFragment(mFragments[position], mFragments[prePosition])
            }

            override fun onTabUnselected(position: Int) {}
            override fun onTabReselected(position: Int) {
            }
        }
}